/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
 */

package com.sun.faces.config.configprovider;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.faces.FacesException;

import com.sun.faces.config.WebConfiguration;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.JavaxFacesConfigFiles;
import com.sun.faces.util.Util;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.spi.ConfigurationResourceProvider;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 
 */
public abstract class BaseWebConfigResourceProvider implements
      ConfigurationResourceProvider {


    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();


    // ------------------------------ Methods from ConfigurationResourceProvider


    public Collection<URI> getResources(ServletContext context) {

        WebConfiguration webConfig = WebConfiguration.getInstance(context);
        String paths = webConfig.getOptionValue(getParameter());
        Set<URI> urls = new LinkedHashSet<URI>(6);
        if (paths != null) {
            for (String token : Util.split(context, paths.trim(), getSeparatorRegex())) {
                String path = token.trim();
                if (!isExcluded(path) && path.length() != 0) {
                    URI u = getContextURLForPath(context, path);
                    if (u != null) {
                        urls.add(u);
                    } else {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING,
                                       "jsf.config.web_resource_not_found",
                                       new Object[] { path, JavaxFacesConfigFiles.getQualifiedName() });
                        }
                    }
                }

            }
        }

        return urls;
        
    }


    // ------------------------------------------------------- Protected Methods


    protected abstract WebContextInitParameter getParameter();

    protected abstract String[] getExcludedResources();

    protected abstract String getSeparatorRegex();


    protected URI getContextURLForPath(ServletContext context, String path) {

        URI result = null;
        try {
            URL url = context.getResource(path);
            if (null != url) {
                String urlString = url.toExternalForm();
                urlString = urlString.replaceAll(" ", "%20");
                result = new URI(urlString);            
            }
        } catch (MalformedURLException mue) {
            throw new FacesException(mue);
        } catch (URISyntaxException use) {
            throw new FacesException(use);
        }
        return result;

    }


    protected boolean isExcluded(String path) {

        return (Arrays.binarySearch(getExcludedResources(), path) >= 0);

    }

}
