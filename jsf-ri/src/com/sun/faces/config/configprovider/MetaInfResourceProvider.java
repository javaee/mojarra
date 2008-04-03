/*
 * $Id: MetaInfResourceProvider.java,v 1.2 2007/04/27 22:00:55 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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
