/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.facelets.impl;

import com.sun.faces.context.FacesFileNotFoundException;
import com.sun.faces.facelets.Facelet;
import javax.faces.view.facelets.FaceletCache;
import com.sun.faces.facelets.FaceletFactory;
import com.sun.faces.facelets.compiler.Compiler;
import com.sun.faces.util.Cache;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

import javax.faces.view.facelets.FaceletHandler;
import javax.faces.view.facelets.ResourceResolver;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.faces.FacesException;


/**
 * Default FaceletFactory implementation.
 *
 * @author Jacob Hookom
 * @version $Id: DefaultFaceletFactory.java,v 1.10 2007/04/09 01:13:17 youngm
 *          Exp $
 */
public class DefaultFaceletFactory extends FaceletFactory {

    protected final static Logger log = FacesLogger.FACELETS_FACTORY.getLogger();

    private final Compiler compiler;

    private Map<String, URL> relativeLocations;

    private final ResourceResolver resolver;

    private final URL baseUrl;
    
    private final long refreshPeriod;

    private final FaceletCache<DefaultFacelet> cache;

    Cache<String,IdMapper> idMappers;
    


    // ------------------------------------------------------------ Constructors


    public DefaultFaceletFactory(Compiler compiler, ResourceResolver resolver)
    throws IOException {

        this(compiler, resolver, -1, null);

    }


    public DefaultFaceletFactory(Compiler compiler,
                                 ResourceResolver resolver,
                                 long refreshPeriod) {
        this(compiler, resolver, refreshPeriod, null);
    }

    public DefaultFaceletFactory(Compiler compiler,
                                 ResourceResolver resolver,
                                 long refreshPeriod,
                                 FaceletCache cache) {

        Util.notNull("compiler", compiler);
        Util.notNull("resolver", resolver);
        this.compiler = compiler;
        this.relativeLocations = new ConcurrentHashMap<String, URL>();
        this.resolver = resolver;
        this.baseUrl = resolver.resolveUrl("/");
        this.idMappers = new Cache<String,IdMapper>(new IdMapperFactory());
        // this.location = url;
        refreshPeriod = (refreshPeriod >= 0) ? refreshPeriod * 1000 : -1;
        this.refreshPeriod = refreshPeriod;
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Using ResourceResolver: {0}", resolver);
            log.log(Level.FINE, "Using Refresh Period: {0}", refreshPeriod);
        }
        
        // We can cast to the FaceletCache<DefaultFacelet> here because we know
        // that the Generics information is only used at compile time, and all cache
        // implementations will be using instance factories provided by us and returning DefaultFacelet
        this.cache = (FaceletCache<DefaultFacelet>)cache;
        
        // Create instance factories for the  cache, so that the cache can
        // create Facelets and Metadata Facelets
        FaceletCache.MemberFactory<DefaultFacelet> faceletFactory =
            new FaceletCache.MemberFactory<DefaultFacelet>() {
                public DefaultFacelet newInstance(final URL key) throws IOException {
                    return createFacelet(key);
                }
            };
        FaceletCache.MemberFactory<DefaultFacelet> metadataFaceletFactory =
            new FaceletCache.MemberFactory<DefaultFacelet>() {
                public DefaultFacelet newInstance(final URL key) throws IOException {
                    return createMetadataFacelet(key);
                }
            };
        try {
            // We must call this method using reflection because it is protected.
            Method m = FaceletCache.class.getDeclaredMethod("setMemberFactories", FaceletCache.MemberFactory.class, FaceletCache.MemberFactory.class);
            m.setAccessible(true);
            m.invoke(this.cache, faceletFactory, metadataFaceletFactory);
        } catch (Exception ex) {
            if (log.isLoggable(Level.SEVERE)) {
                log.log(Level.SEVERE, null, ex);
            }
            throw new FacesException(ex);
        } 
        
    }


    /*
      * (non-Javadoc)
      *
      * @see com.sun.facelets.FaceletFactory#getResourceResolver
      */
    public ResourceResolver getResourceResolver() {
        return resolver;
    }

    
    /*
      * (non-Javadoc)
      *
      * @see com.sun.facelets.FaceletFactory#getFacelet(java.lang.String)
      */
    public Facelet getFacelet(String uri) throws IOException {

        return this.getFacelet(resolveURL(uri));

    }



    public Facelet getMetadataFacelet(String uri) throws IOException {

        return this.getMetadataFacelet(resolveURL(uri));

    }


    /**
     * Resolves a path based on the passed URL. If the path starts with '/', then
     * resolve the path against {@link javax.faces.context.ExternalContext#getResource(java.lang.String)
     * javax.faces.context.ExternalContext#getResource(java.lang.String)}.
     * Otherwise create a new URL via {@link URL#URL(java.net.URL,
     * java.lang.String) URL(URL, String)}.
     *
     * @param source base to resolve from
     * @param path   relative path to the source
     *
     * @return resolved URL
     *
     * @throws IOException
     */
    public URL resolveURL(URL source, String path) throws IOException {
        if (path.startsWith("/")) {
            URL url = this.resolver.resolveUrl(path);
            if (url == null) {
                throw new FacesFileNotFoundException(path
                                                + " Not Found in ExternalContext as a Resource");
            }
            return url;
        } else {
            return new URL(source, path);
        }
    }

    /**
     * Create a Facelet from the passed URL. This method checks if the cached
     * Facelet needs to be refreshed before returning. If so, uses the passed URL
     * to build a new instance;
     *
     * @param url source url
     *
     * @return Facelet instance
     *
     * @throws IOException
     * @throws FaceletException
     * @throws FacesException
     * @throws ELException
     */
    public Facelet getFacelet(URL url) throws IOException {
        DefaultFacelet _facelet = null;
        Facelet result = this.cache.getFacelet(url);
        if (result instanceof DefaultFacelet) {
            _facelet = (DefaultFacelet) result;
            String docType = _facelet.getSavedDoctype();
            if (null != docType) {
                Util.saveDOCTYPEToFacesContextAttributes(docType);
            }
            
            String xmlDecl = _facelet.getSavedXMLDecl();
            if (null != xmlDecl) {
                Util.saveXMLDECLToFacesContextAttributes(xmlDecl);
            }
        }
        
        return result;
    }

    public Facelet getMetadataFacelet(URL url) throws IOException {
        return this.cache.getViewMetadataFacelet(url);
    }

    public boolean needsToBeRefreshed(URL url) {
        return !this.cache.isFaceletCached(url);
    }

    

    private URL resolveURL(String uri) throws IOException {

        URL url = this.relativeLocations.get(uri);
        if (url == null) {
            url = this.resolveURL(this.baseUrl, uri);
            if (url != null) {
                this.relativeLocations.put(uri, url);
            } else {
                throw new IOException("'" + uri + "' not found.");
            }
        }
        return url;

    }


    /**
     * Uses the internal Compiler reference to build a Facelet given the passed
     * URL.
     *
     * @param url source
     *
     * @return a Facelet instance
     *
     * @throws IOException
     * @throws FaceletException
     * @throws FacesException
     * @throws ELException
     */
    private DefaultFacelet createFacelet(URL url) throws IOException {
        if (log.isLoggable(Level.FINE)) {
            log.fine("Creating Facelet for: " + url);
        }
        String escapedBaseURL = Pattern.quote(this.baseUrl.getFile());
        String alias = '/' + url.getFile().replaceFirst(escapedBaseURL, "");
        try {
            FaceletHandler h = this.compiler.compile(url, alias);
            return new DefaultFacelet(this,
                                      this.compiler.createExpressionFactory(),
                                      url,
                                      alias,
                                      h);
        } catch (FileNotFoundException fnfe) {
            throw new FileNotFoundException("Facelet "
                                            + alias
                                            + " not found at: "
                                            + url.toExternalForm());
        }
    }

    private DefaultFacelet createMetadataFacelet(URL url) throws IOException {

        if (log.isLoggable(Level.FINE)) {
            log.fine("Creating Metadata Facelet for: " + url);
        }
        String escapedBaseURL = Pattern.quote(this.baseUrl.getFile());
        String alias = '/' + url.getFile().replaceFirst(escapedBaseURL, "");
        try {
            FaceletHandler h = this.compiler.metadataCompile(url, alias);
            return new DefaultFacelet(this,
                                      this.compiler.createExpressionFactory(),
                                      url,
                                      alias,
                                      h);
        } catch (FileNotFoundException fnfe) {
            throw new FileNotFoundException("Facelet "
                                            + alias
                                            + " not found at: "
                                            + url.toExternalForm());
        }

    }


    public long getRefreshPeriod() {
        return this.refreshPeriod;
    }


    // ---------------------------------------------------------- Nested Classes


    private static final class IdMapperFactory implements Cache.Factory<String,IdMapper> {


        // ------------------------------------------ Methods from Cache.Factory


        public IdMapper newInstance(String arg) throws InterruptedException {

            return new IdMapper();

        }

    }    
    
}
