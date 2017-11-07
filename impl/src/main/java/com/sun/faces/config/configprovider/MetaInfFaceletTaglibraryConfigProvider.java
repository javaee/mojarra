/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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

import static com.sun.faces.util.Util.getCurrentLoader;
import static java.lang.System.arraycopy;
import static java.util.Collections.emptyList;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.faces.FacesException;
import javax.servlet.ServletContext;

import com.sun.faces.facelets.util.Classpath;
import com.sun.faces.spi.ConfigurationResourceProvider;

/**
 *
 */
public class MetaInfFaceletTaglibraryConfigProvider implements ConfigurationResourceProvider {

    private static final String SUFFIX = ".taglib.xml";
    private static final String WEB_INF_CLASSES = "/WEB-INF/classes/META-INF";

    /**
     * Array of taglib.xml files included with Facelets 1.1.x. If they are on the classpath, we don't want to process them.
     */
    private static final String[] FACELET_CONFIG_FILES = { 
            "META-INF/jsf-core.taglib.xml", 
            "META-INF/jsf-html.taglib.xml",
            "META-INF/jsf-ui.taglib.xml", 
            "META-INF/jstl-core.taglib.xml", 
            "META-INF/jstl-fn.taglib.xml" };

    private static final String[] BUILT_IN_TAGLIB_XML_FILES = { "META-INF/mojarra_ext.taglib.xml"

    };

    // -------------------------------------------- Methods from ConfigProcessor

    @Override
    public Collection<URI> getResources(ServletContext context) {

        try {
            URL[] externalTaglibUrls = Classpath.search(getCurrentLoader(this), "META-INF/", SUFFIX);
            URL[] builtInTaglibUrls = new URL[BUILT_IN_TAGLIB_XML_FILES.length];
            ClassLoader runtimeClassLoader = this.getClass().getClassLoader();
            
            for (int i = 0; i < BUILT_IN_TAGLIB_XML_FILES.length; i++) {
                builtInTaglibUrls[i] = runtimeClassLoader.getResource(BUILT_IN_TAGLIB_XML_FILES[i]);
            }
            
            URL[] urls = new URL[externalTaglibUrls.length + builtInTaglibUrls.length];
            arraycopy(externalTaglibUrls, 0, urls, 0, externalTaglibUrls.length);
            arraycopy(builtInTaglibUrls, 0, urls, externalTaglibUrls.length, builtInTaglibUrls.length);

            // Perform some 'correctness' checking. If the user has
            // removed the FaceletViewHandler from their configuration,
            // but has left the jsf-facelets.jar in the classpath, we
            // need to ignore the default configuration resouces from
            // that JAR.
            List<URI> urlsList = pruneURLs(urls);

            // Special case for finding taglib files in WEB-INF/classes/META-INF
            Set<String> paths = context.getResourcePaths(WEB_INF_CLASSES);
            if (paths != null) {
                for (String path : paths) {
                    if (path.endsWith(".taglib.xml")) {
                        try {
                            urlsList.add(new URI(context.getResource(path).toExternalForm().replaceAll(" ", "%20")));
                        } catch (URISyntaxException ex) {
                            throw new FacesException(ex);
                        }
                    }
                }
            }
            
            return urlsList;
        } catch (IOException ioe) {
            throw new FacesException("Error searching classpath from facelet-taglib documents", ioe);
        }

    }

    // --------------------------------------------------------- Private Methods

    private List<URI> pruneURLs(URL[] urls) {

        List<URI> ret = null;
        if (urls != null && urls.length > 0) {
            for (URL url : urls) {
                String u = url.toString();
                boolean found = false;
                for (String excludeName : FACELET_CONFIG_FILES) {
                    if (u.contains(excludeName)) {
                        found = true;
                        break;
                    }
                }
                
                if (!found) {
                    if (ret == null) {
                        ret = new ArrayList<>();
                    }
                    
                    try {
                        ret.add(new URI(url.toExternalForm().replaceAll(" ", "%20")));
                    } catch (URISyntaxException ex) {
                        throw new FacesException(ex);
                    }
                }
            }
        }

        if (ret == null) {
            ret = emptyList();
        }
        
        return ret;
    }

}
