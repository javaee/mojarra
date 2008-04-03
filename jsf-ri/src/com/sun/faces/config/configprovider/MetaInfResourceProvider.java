/*
 * $Id: MetaInfResourceProvider.java,v 1.1 2007/04/22 21:41:42 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2007 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.config.configprovider;

import com.sun.faces.spi.ConfigurationResourceProvider;
import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 */
public class MetaInfResourceProvider implements ConfigurationResourceProvider {

    /**
     * <p>The resource path for faces-config files included in the
     * <code>META-INF</code> directory of JAR files.</p>
     */
    private static final String META_INF_RESOURCES =
         "META-INF/faces-config.xml";


    // ------------------------------ Methods From ConfigurationResourceProvider


    /**
     * @see ConfigurationResourceProvider#getResources(javax.servlet.ServletContext)
     */
    public List<URL> getResources(ServletContext context) {

            SortedMap<String, URL> sortedJarMap = new TreeMap<String, URL>();
            //noinspection CollectionWithoutInitialCapacity
            List<URL> unsortedResourceList = new ArrayList<URL>();

            try {
                for (Enumeration<URL> items = Util.getCurrentLoader(this)
                      .getResources(META_INF_RESOURCES);
                     items.hasMoreElements();) {

                    URL nextElement = items.nextElement();
                    String jarUrl = nextElement.toString();
                    String jarName = null;
                    int resourceIndex;
                    // If this resource has a faces-config file inside of it
                    if (-1 != (resourceIndex =
                          jarUrl.indexOf(META_INF_RESOURCES))) {
                        // Search backwards for the previous occurrence of File.SEPARATOR
                        int sepIndex = resourceIndex - 2;
                        char sep = ' ';
                        while (0 < sepIndex) {
                            sep = jarUrl.charAt(sepIndex);
                            if ('/' == sep) {
                                break;
                            }
                            sepIndex--;
                        }
                        if ('/' == sep) {
                            jarName =
                                  jarUrl.substring(sepIndex + 1, resourceIndex);
                        }
                    }
                    if (null != jarName) {
                        sortedJarMap.put(jarName, nextElement);
                    } else {
                        unsortedResourceList.add(0, nextElement);
                    }
                }
            } catch (IOException e) {
                throw new FacesException(e);
            }
            // Load the sorted resources first:
            List<URL> result =
                 new ArrayList(sortedJarMap.size() + unsortedResourceList.size());
            for (Map.Entry<String, URL> entry : sortedJarMap.entrySet()) {
                result.add(entry.getValue());
            }
            // Then load the unsorted resources
            result.addAll(unsortedResourceList);
            return result;
    }
    

}
