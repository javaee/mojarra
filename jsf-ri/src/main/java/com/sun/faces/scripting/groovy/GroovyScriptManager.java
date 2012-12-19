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

package com.sun.faces.scripting.groovy;

import com.sun.faces.RIConstants;
import com.sun.faces.scripting.ScriptManager;
import com.sun.faces.util.FacesLogger;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroovyScriptManager implements ScriptManager {
    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    private static final String SCRIPT_PATH = "/WEB-INF/groovy/";
    private static final String SUFFIX = ".groovy";
    private ServletContext servletContext;

    // Borrowed from AnnotationScanner.  Perhaps this should be made public somewhere
    public static final Set<String> FACES_ANNOTATIONS;

    static {
        HashSet<String> annotations = new HashSet<String>(15, 1.0f);
        Collections.addAll(annotations,
                "javax.faces.component.FacesComponent",
                "javax.faces.component.*",
                "javax.faces.convert.FacesConverter",
                "javax.faces.convert.*",
                "javax.faces.validator.FacesValidator",
                "javax.faces.validator.*",
                "javax.faces.render.FacesRenderer",
                "javax.faces.render.*",
                "javax.faces.bean.ManagedBean",
                "javax.faces.bean.*",
                "javax.faces.event.NamedEvent",
                "javax.faces.event.*",
                "javax.faces.component.behavior.FacesBehavior",
                "javax.faces.component.behavior.*",
                "javax.faces.render.FacesBehaviorRenderer");
        FACES_ANNOTATIONS = Collections.unmodifiableSet(annotations);
    }

    public GroovyScriptManager(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public Set<String> getScripts() {
        Set<String> scripts = new HashSet<String>();
        processWebInfGroovy(servletContext, servletContext.getResourcePaths(SCRIPT_PATH), scripts);

        return scripts;
    }

    private void processWebInfGroovy(ServletContext sc, Set<String> paths, Set<String> classList) {
        if (paths != null && !paths.isEmpty()) {
            for (String pathElement : paths) {
                if (pathElement.endsWith("/")) {
                    processWebInfGroovy(sc, sc.getResourcePaths(pathElement), classList);
                } else {
                    if (pathElement.endsWith(SUFFIX)) {
                        String cname = convertToClassName(SCRIPT_PATH, pathElement);
                        if (containsAnnotation(sc, pathElement)) {
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.log(Level.FINE, "[WEB-INF/groovy] Found annotated Class: {0}", cname);
                            }
                            classList.add(cname);
                        }
                    }
                }
            }
        }
    }

    private boolean containsAnnotation(ServletContext sc, String pathElement) {
        boolean containsAnnotation = false;
        BufferedReader in = null;
        try {
            URL url = sc.getResource(pathElement);
            in = new BufferedReader(new InputStreamReader(url.openStream(), RIConstants.CHAR_ENCODING));
            String line = in.readLine();
            while ((line != null) && (!containsAnnotation)) {
                line = line.trim();
                if (line.length() != 0) {
                    for (String pattern : FACES_ANNOTATIONS) {
                        if (line.indexOf(pattern) > -1) {
                            containsAnnotation = true;
                            break;
                        }
                    }
                }

                line = in.readLine();
            }
        } catch (Exception ioe) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, null, ioe);
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST, "Closing stream", e);
                    }
                }
            }
        }
        return containsAnnotation;
    }

    /**
     * Utility method for converting paths to fully qualified class names.
     *
     * @param prefix    the prefix that should be stripped from the class name
     *                  before converting it
     * @param pathEntry a path to a class file
     * @return a fully qualified class name using dot notation
     */
    private String convertToClassName(String prefix, String pathEntry) {
        String className = pathEntry;

        if (prefix != null) {
            // remove the prefix
            className = className.substring(prefix.length());
        }
        // remove the .class suffix
        className = className.substring(0, (className.length() - 7));

        return className.replace('/', '.');
    }
}
