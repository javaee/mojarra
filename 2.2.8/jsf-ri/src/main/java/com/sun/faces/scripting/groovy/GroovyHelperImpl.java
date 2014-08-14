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

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import groovy.util.GroovyScriptEngine;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

/**
 * Helper class to interface with the Groovy runtime.
 */
public class GroovyHelperImpl extends GroovyHelper {


    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    private static final String SCRIPT_PATH = "/WEB-INF/groovy/";

    private MojarraGroovyClassLoader loader;

    // ------------------------------------------------------------ Constructors


    GroovyHelperImpl() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extContext = facesContext.getExternalContext();
        ClassLoader curLoader = Thread.currentThread().getContextClassLoader();

        URL combinedRoots[] = getResourceRoots(extContext, curLoader);

        if (0 < combinedRoots.length) {
            GroovyScriptEngine engine =
                    new GroovyScriptEngine(combinedRoots, curLoader);
//            Class<?> c = Util.loadClass("groovy.util.GroovyScriptEngine", GroovyHelperFactory.class);
//            Constructor<?> ctor = c.getConstructor(URL[].class, ClassLoader.class);
//            GroovyScriptEngine engine = (GroovyScriptEngine)ctor.newInstance(combinedRoots, curLoader);
            loader = new MojarraGroovyClassLoader(engine);
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO,
                        "Groovy support enabled.");
            }
            extContext.getApplicationMap().put("com.sun.faces.groovyhelper",
                    this);
            ((ServletContext)(extContext.getContext())).setAttribute("com.sun.faces.groovyhelper", this);
        }

    }

    private URL[] getResourceRoots(ExternalContext extContext,
                                   ClassLoader curLoader) throws IOException {
        URL[] combinedRoots;
        Enumeration<URL> classpathResourceEnumeration =
                curLoader.getResources("META-INF/resources/");
        List<URL> classpathResourceList = new ArrayList<URL>();
        while (classpathResourceEnumeration.hasMoreElements()) {
            classpathResourceList.add(classpathResourceEnumeration.nextElement());
        }

        // only called during init - safe to cast and save.
        URL u = extContext.getResource(SCRIPT_PATH);
        URL webappRoots[] = getWebappResourceRoots(extContext),
                classpathRoots[] = new URL[classpathResourceList.size()];
        classpathResourceList.toArray(classpathRoots);

        if (null != u ||
                0 < webappRoots.length ||
                0 < classpathRoots.length) {
            combinedRoots = new URL[webappRoots.length + classpathRoots.length +
                    (null != u ? 1 : 0)];
            System.arraycopy(webappRoots, 0,
                    combinedRoots, 0,
                    webappRoots.length);
            System.arraycopy(classpathRoots, 0,
                    combinedRoots, webappRoots.length,
                    classpathRoots.length);
            if (null != u) {
                combinedRoots[webappRoots.length + classpathRoots.length] = u;
            }
        } else {
            combinedRoots = new URL[0];
        }

        return combinedRoots;
    }


    private URL[] getWebappResourceRoots(ExternalContext extContext) {
        URL[] result = null;
        int size = 0, i = 0;
        Set<String> resourceRoots = extContext.getResourcePaths("/resources/");
        if (null != resourceRoots && !resourceRoots.isEmpty()) {
            // Determine the size of script roots that end with "/"
            for (String cur : resourceRoots) {
                if (cur.endsWith("/")) {
                    size++;
                }
            }
            result = new URL[size];
            for (String cur : resourceRoots) {
                if (cur.endsWith("/")) {
                    try {
                        result[i++] = extContext.getResource(cur);
                    } catch (MalformedURLException ex) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        if (null == result) {
            result = new URL[0];
        }
        return result;
    }

    public void addURL(URL toAdd) {
        loader.getGroovyScriptEngine().getGroovyClassLoader().addURL(toAdd);
    }

    // ---------------------------------------------------------- Public Methods


    public Class<?> loadScript(String name) {
        try {
            String script = name;
            if (script.endsWith(".groovy")) {
                script = script.substring(0, script.indexOf(".groovy"));
            }
            //return engine.loadScriptByName(script);
            return Util.loadClass(script, this);
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }

    public void setClassLoader() {
        if (loader != null) {
            Thread.currentThread().setContextClassLoader(loader);
        }
    }

    // ----------------------------------------------------------- Inner Classes

    public static final class MojarraGroovyClassLoader extends URLClassLoader {

        private GroovyScriptEngine gse;

        public MojarraGroovyClassLoader(GroovyScriptEngine gse) {
            super(new URL[0], gse.getGroovyClassLoader());
	    gse.getGroovyClassLoader().setShouldRecompile(Boolean.TRUE);
            this.gse = gse;
        }

        public GroovyScriptEngine getGroovyScriptEngine() {
            return gse;
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            if (name == null) {
                throw new NullPointerException();
            }
            Class<?> c;
            try {
                c = gse.loadScriptByName(name);
            } catch (Exception e) {
                try {
                    c = gse.getGroovyClassLoader().loadClass(name);
                } catch (ClassNotFoundException cnfe) {
                    throw new ClassNotFoundException(name, cnfe);
                }
            }
            if (c == null) {
                throw new ClassNotFoundException(name);
            }
            return c;
        }

    }

}
