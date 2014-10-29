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
 */

package com.sun.faces.config.configprovider;

import com.sun.faces.util.Util;
import com.sun.faces.config.WebConfiguration;
import com.sun.faces.facelets.util.Classpath;
import com.sun.faces.spi.ConfigurationResourceProvider;
import java.net.URISyntaxException;

import javax.faces.FacesException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *
 */
public class MetaInfFacesConfigResourceProvider implements
      ConfigurationResourceProvider {

    /**
     * <p>This <code>Pattern</code> will pick the the JAR file name if present
     * within a URL.</p>
     */
    private static final Pattern JAR_PATTERN = Pattern.compile(".*/(\\S*\\.jar).*");

    /**
     * <p>The resource path for faces-config files included in the
     * <code>META-INF</code> directory of JAR files.</p>
     */
    private static final String META_INF_RESOURCES =
         "META-INF/faces-config.xml";

    private static final String WEB_INF_CLASSES =
          "/WEB-INF/classes/META-INF";


    // ------------------------------ Methods From ConfigurationResourceProvider


    /**
     * @see com.sun.faces.spi.ConfigurationResourceProvider#getResources(javax.servlet.ServletContext)
     */
    public Collection<URI> getResources(ServletContext context) {

        WebConfiguration webConfig = WebConfiguration.getInstance(context);
        String duplicateJarPattern = webConfig.getOptionValue(WebConfiguration.WebContextInitParameter.DuplicateJARPattern);
        Pattern duplicatePattern = null;
        if (duplicateJarPattern != null) {
            duplicatePattern = Pattern.compile(duplicateJarPattern);
        }
        SortedMap<String, Set<URI>> sortedJarMap = new TreeMap<String, Set<URI>>();
        //noinspection CollectionWithoutInitialCapacity
        List<URI> unsortedResourceList = new ArrayList<URI>();

        try {
            for (URI uri : loadURLs(context)) {

                String jarUrl = uri.toString();
                String jarName = null;
                Matcher m = JAR_PATTERN.matcher(jarUrl);
                if (m.matches()) {
                    jarName = m.group(1);
                }
                if (jarName != null) {
                    if (duplicatePattern != null) {
                        m = duplicatePattern.matcher(jarName);
                        if (m.matches()) {
                            jarName = m.group(1);
                        }
                    }

                    Set<URI> uris = sortedJarMap.get(jarName);
                    if (uris == null) {
                        uris = new HashSet<URI>();
                        sortedJarMap.put(jarName, uris);
                    }
                    uris.add(uri);
                } else {
                    unsortedResourceList.add(0, uri);
                }
            }
        } catch (IOException e) {
            throw new FacesException(e);
        }
        // Load the sorted resources first:
        List<URI> result =
              new ArrayList<URI>(sortedJarMap.size() + unsortedResourceList
                    .size());
        for (Map.Entry<String, Set<URI>> entry : sortedJarMap.entrySet()) {
            result.addAll(entry.getValue());
        }
        // Then load the unsorted resources
        result.addAll(unsortedResourceList);

        return result;
        
    }


    // --------------------------------------------------------- Private Methods


    private Collection<URI> loadURLs(ServletContext context) throws IOException {

        Set<URI> urls = new HashSet<URI>();
        try {
            for (Enumeration<URL> e = Util.getCurrentLoader(this).getResources(META_INF_RESOURCES); e.hasMoreElements();) {
                String urlString = e.nextElement().toExternalForm();
                urlString = urlString.replaceAll(" ", "%20");                
                urls.add(new URI(urlString));
            }
            URL [] urlArray = Classpath.search("META-INF/", ".faces-config.xml");
            for (URL cur : urlArray) {
                String urlString = cur.toExternalForm();
                urlString = urlString.replaceAll(" ", "%20");
                urls.add(new URI(urlString));
            }
            // special case for finding taglib files in WEB-INF/classes/META-INF
            Set paths = context.getResourcePaths(WEB_INF_CLASSES);
            if (paths != null) {
                for (Object path : paths) {
                    String p = path.toString();
                    if (p.endsWith(".taglib.xml")) {
                        String urlString = context.getResource(p).toExternalForm();
                        urlString = urlString.replaceAll(" ", "%20");
                        urls.add(new URI(urlString));
                    }
                }
            }
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
        return urls;
        
    }
}
