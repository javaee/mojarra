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

import com.sun.faces.RIConstants;
import com.sun.faces.spi.ConfigurationResourceProvider;

import com.sun.faces.util.FacesLogger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

/**
 *
 */
public class WebAppFlowConfigResourceProvider implements
      ConfigurationResourceProvider {
    
    private static final Logger logger = FacesLogger.CONFIG.getLogger();

    // ------------------------------ Methods from ConfigurationResourceProvider


    /**
     * @see ConfigurationResourceProvider#getResources(javax.servlet.ServletContext)
     */
    @Override
    public Collection<URI> getResources(ServletContext context) {

        List<URI> list = Collections.emptyList();
        Set<String> allPaths = context.getResourcePaths("/");
        
        if (null == allPaths) {
            return list;
        }
        list = null;

        for (String cur : allPaths) {
            if (!cur.startsWith("/META-INF")) {
                if (cur.equals("/WEB-INF/")) {
                    Set<String> webInfPaths = context.getResourcePaths(cur);
                    if (null != webInfPaths) {
                        for (String webInfCur : webInfPaths) {
                            if (!cur.equals("/WEB-INF/classes/") &&
                                webInfCur.endsWith("/")) {
                                list = inspectDirectory(context, webInfCur, list);
                            }
                        }
                    }
                } else if (cur.endsWith("/")) {
                    list = inspectDirectory(context, cur, list);
                }
            }
        }
        
        return (null == list) ? Collections.EMPTY_LIST : list;

    }
    
    private List<URI> inspectDirectory(ServletContext context, String toInspect, List<URI> list) {
        URL curUrl = null;
        
        Set<String> allPaths = context.getResourcePaths(toInspect);
        if (null == allPaths) {
            return list;
        }
        
        for (String cur : allPaths) {
            if (cur.endsWith(RIConstants.FLOW_DEFINITION_ID_SUFFIX)) {
                int suffixIndex = cur.length() - RIConstants.FLOW_DEFINITION_ID_SUFFIX_LENGTH;
                int slash = cur.lastIndexOf("/", suffixIndex);
                if (-1 == slash) {
                    continue;
                }
                String flowName = cur.substring(slash + 1, suffixIndex);
                int prevSlash = cur.lastIndexOf("/", slash - 1);
                if (-1 == prevSlash) {
                    continue;
                }
                // Ensure cur matches the pattern <flowName>/<flowName>-flow.xml
                String dirName = cur.substring(prevSlash + 1, slash);
                if (dirName.equals(flowName)) {
                    if (null == list) {
                        list = new ArrayList<URI>();
                    }
                    try {
                        curUrl = context.getResource(cur);
                        list.add(curUrl.toURI());
                    } catch (MalformedURLException ex) {
                        if (logger.isLoggable(Level.SEVERE)) {
                            logger.log(Level.SEVERE, "Unable to get resource for {0}" + cur, ex);
                        }
                    } catch (URISyntaxException use) {
                        if (logger.isLoggable(Level.SEVERE)) {
                            logger.log(Level.SEVERE, "Unable to get URI for {0}" + curUrl.toExternalForm(), use);
                        }
                        
                    }
                }
            }
        }
        return list;
    }

}
