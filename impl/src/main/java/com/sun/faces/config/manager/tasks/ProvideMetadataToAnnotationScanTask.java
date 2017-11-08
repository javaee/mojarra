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

package com.sun.faces.config.manager.tasks;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.faces.config.manager.FacesConfigInfo;
import com.sun.faces.config.manager.documents.DocumentInfo;
import com.sun.faces.spi.AnnotationScanner;
import com.sun.faces.spi.InjectionProvider;

public final class ProvideMetadataToAnnotationScanTask {
    
    private static final Pattern JAR_PATTERN = Pattern.compile("(.*/(\\S*\\.jar)).*(/faces-config.xml|/*.\\.faces-config.xml)");

    private final DocumentInfo[] documentInfos;
    private final InjectionProvider containerConnector;
    
    private Set<URI> uris;
    private Set<String> jarNames;

    public ProvideMetadataToAnnotationScanTask(DocumentInfo[] documentInfos, InjectionProvider containerConnector) {
        this.documentInfos = documentInfos;
        this.containerConnector = containerConnector;
    }

    private void initializeIvars(Set<Class<?>> annotatedSet) {
        if (uris != null || jarNames != null) {
            return;
        }

        uris = new HashSet<>(documentInfos.length);
        jarNames = new HashSet<>(documentInfos.length);

        for (DocumentInfo docInfo : documentInfos) {

            URI sourceURI = docInfo.getSourceURI();
            Matcher jarMatcher = JAR_PATTERN.matcher(sourceURI == null ? "" : sourceURI.toString());

            if (jarMatcher.matches()) {
                String jarName = jarMatcher.group(2);
                if (!jarNames.contains(jarName)) {
                    FacesConfigInfo configInfo = new FacesConfigInfo(docInfo);
                    if (!configInfo.isMetadataComplete()) {
                        uris.add(sourceURI);
                        jarNames.add(jarName);
                    } else {
                        /*
                         * Because the container annotation scanning does not know anything about faces-config.xml metadata-complete the
                         * annotatedSet of classes will include classes that are not supposed to be included.
                         *
                         * The code below looks at the CodeSource of the class and determines whether or not it should be removed from the
                         * annotatedSet because the faces-config.xml that owns it has metadata-complete="true".
                         */
                        ArrayList<Class<?>> toRemove = new ArrayList<>(1);
                        String sourceURIString = sourceURI.toString();
                        if (annotatedSet != null) {
                            for (Class<?> clazz : annotatedSet) {
                                if (sourceURIString.contains(clazz.getProtectionDomain().getCodeSource().getLocation().toString())) {
                                    toRemove.add(clazz);
                                }
                            }
                            annotatedSet.removeAll(toRemove);
                        }
                    }
                }
            }
        }
    }

    public Set<URI> getAnnotationScanURIs(Set<Class<?>> annotatedSet) {
        initializeIvars(annotatedSet);

        return uris;

    }

    public Set<String> getJarNames(Set<Class<?>> annotatedSet) {
        initializeIvars(annotatedSet);

        return jarNames;
    }

    public AnnotationScanner getAnnotationScanner() {
        if (containerConnector instanceof AnnotationScanner) {
            return (AnnotationScanner) this.containerConnector;
        }
        
        return null;
    }
}
