
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
package com.sun.faces.config;

import com.sun.faces.spi.InjectionProviderException;
import com.sun.faces.util.FacesLogger;
import static com.sun.faces.spi.AnnotationScanner.ScannedAnnotation;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

class DelegateToGlassFishAnnotationScanner extends AnnotationScanner {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();
    private com.sun.faces.spi.AnnotationScanner annotationScanner = null;
    Set<String> jarNamesWithoutMetadataComplete = null;

    public DelegateToGlassFishAnnotationScanner(ServletContext sc) {
        super(sc);
    }

    public void setAnnotationScanner(com.sun.faces.spi.AnnotationScanner containerConnector, Set<String> jarNamesWithoutMetadataComplete) {
        this.annotationScanner = containerConnector;
        this.jarNamesWithoutMetadataComplete = jarNamesWithoutMetadataComplete;
    }

    @Override
    public Map<Class<? extends Annotation>, Set<Class<?>>> getAnnotatedClasses(Set<URI> uris) {

        Set<String> classList = new HashSet<String>();

        processAnnotations(classList);
        processScripts(classList);

        return processClassList(classList);
    }

    private Object getDeploymentContext(ExternalContext extContext) {
        Object result = null;
        Map<String, Object> appMap = extContext.getApplicationMap();

        // This will work in GlassFish 3.1.1.
        result = appMap.get("com.sun.enterprise.web.WebModule.DeploymentContext");

        if (null == result) {
            // Try the GlassFish 3.1 way.
            Object defaultHabitat = appMap.get("org.glassfish.servlet.habitat");
            if (null != defaultHabitat) {
                try {
                    Method getComponent = defaultHabitat.getClass().getDeclaredMethod("getComponent", Class.class);
                    Class serverConfigLookupClass = Thread.currentThread().getContextClassLoader().loadClass("com.sun.enterprise.web.ServerConfigLookup");
                    Object serverConfigLookup = getComponent.invoke(defaultHabitat, serverConfigLookupClass);
                    
                    Method getDeploymentContext = serverConfigLookupClass.getMethod("getDeploymentContext", ServletContext.class);
                    result = getDeploymentContext.invoke(serverConfigLookup, extContext.getContext());
                } catch(InvocationTargetException ite) { 
                    Throwable targetException = ite.getTargetException();
                    System.out.println(targetException);
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.log(Level.FINEST, "Problem getting deployment context information", e);
                    }
                }
            }
        
        }

        return result;
    }

    private String getCurrentWebModulePrefix(ExternalContext extContext) {
        String result = null;
        Object deploymentContext = getDeploymentContext(extContext);
        if (null != deploymentContext) {
            try {
                // If this module is a war or an exploded war, then this will give the 
                // prefix.
                Method getSource = deploymentContext.getClass().getDeclaredMethod("getSource");
                Object source = getSource.invoke(deploymentContext, (Object[]) null);
                if (null != source) {
                    Method getName = source.getClass().getDeclaredMethod("getName");
                    if (null != getName) {
                        result = (String) getName.invoke(source, (Object[]) null);
                    }

                }

            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, "Problem in getCurrentWebModulePrefix", e);
                }
            }
        }
        if (null == result && null != deploymentContext) {
            try {
                // If this module is an ear, then this will give the prefix.
                Method getModuleUri = deploymentContext.getClass().getMethod("getModuleUri");
                if (null != getModuleUri) {
                    result = (String) getModuleUri.invoke(deploymentContext, (Object[]) null);
                    if (null != result && result.endsWith(".war")) {
                        result = result.substring(0, result.length() - 4);
                    }
                }

            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, "Problem in getCurrentWebModulePrefix", e);
                }
            }
        }

        if (null == result) {
            result = extContext.getApplicationContextPath();
        }

        return result;
    }

    private void processAnnotations(Set<String> classList) {
        try {
            String archiveName = getCurrentWebModulePrefix(FacesContext.getCurrentInstance().getExternalContext());
            Map<String, List<ScannedAnnotation>> classesByAnnotation =
                    annotationScanner.getAnnotatedClassesInCurrentModule(this.sc);

            for (String curAnnotationName : classesByAnnotation.keySet()) {
                if (FACES_ANNOTATIONS.contains(curAnnotationName)) {
                    for (ScannedAnnotation curAnnotation : classesByAnnotation.get(curAnnotationName)) {
                        Collection<URI> definingUris = curAnnotation.getDefiningURIs();
                        Iterator<URI> iter = definingUris.iterator();
                        URI uri, jarUri = null;
                        String uriString, nameOfJarInJarUri = null;
                        boolean doAdd = false;
                        while (!doAdd && iter.hasNext()) {
                            uri = iter.next();
                            uriString = uri.toASCIIString();

                            // If the class is in the current web module
                            boolean currentClassIsInCurrentWebModule =
                                    (uriString.endsWith("WEB-INF/classes") || uriString.endsWith("WEB-INF/classes/"))
                                    && uriString.contains(archiveName);
                            // or it is from a jar that is *not* within a web module...
                            boolean currentClassIsInJarNotInAnyWebModule =
                                    uriString.endsWith(".jar")
                                    && !uriString.contains(archiveName)
                                    && !uriString.contains("WEB-INF/classes");
                            if (currentClassIsInCurrentWebModule
                                    || currentClassIsInJarNotInAnyWebModule) {
                                doAdd = true;
                            } else {
                                for (String jarName : jarNamesWithoutMetadataComplete) {
                                    if (uriString.contains(jarName)) {
                                        doAdd = true;
                                        jarUri = uri;
                                        nameOfJarInJarUri = jarName;
                                        break;
                                    }
                                }
                            }
                        }
                        if (doAdd) {
                            // If the annotationScanPackages context param is set, we
                            // may yet have to exclude the class
                            String fqcn = curAnnotation.getFullyQualifiedClassName();
                            if (isAnnotationScanPackagesSet()) {
                                // If the class with the annotation was found 
                                // in a jar file
                                if (null != jarUri) {
                                    // see if the jar file is in the list of jars to include
                                    String[] allowedPackages =
                                            (getClasspathPackages() != null
                                            && getClasspathPackages().get(nameOfJarInJarUri) != null)
                                            ? getClasspathPackages().get(nameOfJarInJarUri)
                                            : new String[0];
                                    if (0 == allowedPackages.length) {
                                        doAdd = false;
                                    }
                                    for (String curPackage : allowedPackages) {
                                        doAdd = fqcn.contains(curPackage);
                                        if (doAdd) {
                                            break;
                                        }
                                    }
                                } else {
                                    doAdd = processClass(fqcn);
                                }

                            }

                            if (doAdd) {
                                classList.add(fqcn);
                            }
                        }
                    }
                }

            }

        } catch (InjectionProviderException ex) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Unable to use GlassFish to perform "
                        + "annotation scanning.  Annotated artifacts will not "
                        + "be declared to runtime.", ex);
            }

        }

    }
}
