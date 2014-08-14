/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2005-2007 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.faces.application.resource;

import com.sun.faces.util.FacesLogger;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Roland Huss
 * 
 */
public final class Resource {

    protected final static Logger log = FacesLogger.FACELETS_FACTORY.getLogger();

    /**
     * Get an URL of an internal resource. First,
     * {@link javax.faces.context.ExternalContext#getResource(String)} is
     * checked for an non-null URL return value. In the case of a null return
     * value (as it is the case for Weblogic 8.1 for a packed war), a URL with a
     * special URL handler is constructed, which can be used for
     * <em>opening</em> a serlvet resource later. Internally, this special URL
     * handler will call {@link ServletContext#getResourceAsStream(String)} when
     * an inputstream is requested. This works even on Weblogic 8.1
     * 
     * @param ctx
     *            the faces context from which to retrieve the resource
     * @param path
     *            an URL path
     * 
     * @return an url representing the URL and on which getInputStream() can be
     *         called to get the resource
     * @throws MalformedURLException
     */
    static URL getResourceUrl(FacesContext ctx, String path)
            throws MalformedURLException {
        final ExternalContext externalContext = ctx.getExternalContext();
        URL url = externalContext.getResource(path);
        if (log.isLoggable(Level.FINE)) {
            log.fine("Resource-Url from external context: " + url);
        }
        // This might happen on Servlet container which doesnot return
        // anything
        // for getResource() (like weblogic 8.1 for packaged wars) we
        // are trying
        // to use an own URL protocol in order to use
        // ServletContext.getResourceAsStream()
        // when opening the url
        if (url == null && resourceExist(externalContext, path)) {
            url = getUrlForResourceAsStream(externalContext, path);
        }
        return url;
    }

    // This method could be used above to provide a 'fail fast' if a
    // resource
    // doesnt exist. Otherwise, the URL will fail on the first access.
    private static boolean resourceExist(ExternalContext externalContext,
            String path) {
        if ("/".equals(path)) {
            // The root context exists always
            return true;
        }
        Object ctx = externalContext.getContext();
        if (ctx instanceof ServletContext) {
            ServletContext servletContext = (ServletContext) ctx;
            InputStream stream = servletContext.getResourceAsStream(path);
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    if (log.isLoggable(Level.FINEST)) {
                        log.log(Level.FINEST, "Closing stream", e);
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Construct URL with special URLStreamHandler for proxying
    // ServletContext.getResourceAsStream()
    private static URL getUrlForResourceAsStream(
            final ExternalContext externalContext, String path)
            throws MalformedURLException {
        URLStreamHandler handler = new URLStreamHandler() {
            protected URLConnection openConnection(URL u) throws IOException {
                final String file = u.getFile();
                return new URLConnection(u) {
                    public void connect() throws IOException {
                    }

                    public InputStream getInputStream() throws IOException {
                        if (log.isLoggable(Level.FINE)) {
                            log.fine("Opening internal url to " + file);
                        }
                        Object ctx = externalContext.getContext();
                        // Or maybe fetch the external context afresh ?
                        // Object ctx =
                        // FacesContext.getCurrentInstance().getExternalContext().getContext();

                        if (ctx instanceof ServletContext) {
                            ServletContext servletContext = (ServletContext) ctx;
                            InputStream stream = servletContext
                                    .getResourceAsStream(file);
                            if (stream == null) {
                                throw new FileNotFoundException(
                                        "Cannot open resource " + file);
                            }
                            return stream;
                        } else {
                            throw new IOException(
                                    "Cannot open resource for an context of "
                                            + (ctx != null ? ctx.getClass()
                                                    : null));
                        }
                    }
                };
            }
        };
        return new URL("internal", null, 0, path, handler);
    }
}
