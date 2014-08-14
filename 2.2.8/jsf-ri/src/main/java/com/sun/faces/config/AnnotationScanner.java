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

import javax.faces.view.facelets.FaceletsResourceResolver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.sun.faces.scripting.ScriptManager;
import com.sun.faces.scripting.groovy.GroovyHelper;
import com.sun.faces.scripting.groovy.GroovyScriptManager;
import com.sun.faces.spi.AnnotationProvider;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;

import javax.faces.bean.ManagedBean;
import javax.faces.component.FacesComponent;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.faces.event.NamedEvent;
import javax.faces.render.FacesBehaviorRenderer;
import javax.faces.render.FacesRenderer;
import javax.faces.validator.FacesValidator;
import javax.servlet.ServletContext;
import java.lang.annotation.Annotation;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.sun.faces.config.WebConfiguration.WebContextInitParameter.AnnotationScanPackages;


/**
 * This class is responsible for ensuring that the class file bytes of
 * classes contained within the web application are scanned for any of the known
 * Faces configuration Annotations:
 * <ul>
 *  <li>javax.faces.component.FacesBehavior</li>
 *  <li>javax.faces.render.FacesBehaviorRenderer</li>
 *  <li>javax.faces.component.FacesComponent</li>
 *  <li>javax.faces.convert.FacesConverter</li>
 *  <li>javax.faces.validator.FacesValidator</li>
 *  <li>javax.faces.render.FacesRenderer</li>
 *  <li>javax.faces.bean.ManagedBean</li>
 *  <li>javax.faces.event.NamedEvent</li>
 *  <li>javax.faces.view.facelets.FaceletsResourceResolver</li>
 * </ul>
 */
public abstract class AnnotationScanner extends AnnotationProvider {


    // <editor-fold defaultstate="collapsed" desc="data">

    // <editor-fold defaultstate="collapsed" desc="private class vars">

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();
    private static final String WILDCARD = "*";
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="protected class vars">

    protected static final Set<String> FACES_ANNOTATIONS;
    protected static final Set<Class<? extends Annotation>> FACES_ANNOTATION_TYPE;

    // </editor-fold>

    static {
        HashSet<String> annotations = new HashSet<String>(8, 1.0f);
        // JAVASERVERFACES-1835 this collection has the same information twice.
        // Once in javap -s format, and once as fully qualified Java class names.
        Collections.addAll(annotations,
                           "Ljavax/faces/component/FacesComponent;",
                           "Ljavax/faces/convert/FacesConverter;",
                           "Ljavax/faces/validator/FacesValidator;",
                           "Ljavax/faces/render/FacesRenderer;",
                           "Ljavax/faces/bean/ManagedBean;",
                           "Ljavax/faces/event/NamedEvent;",
                           "Ljavax/faces/component/behavior/FacesBehavior;",
                           "Ljavax/faces/render/FacesBehaviorRenderer;",
                           "Ljavax/faces/view/facelets/FaceletsResourceResolver;",
                           "javax.faces.component.FacesComponent",
                           "javax.faces.convert.FacesConverter",
                           "javax.faces.validator.FacesValidator",
                           "javax.faces.render.FacesRenderer",
                           "javax.faces.bean.ManagedBean",
                           "javax.faces.event.NamedEvent",
                           "javax.faces.component.behavior.FacesBehavior",
                           "javax.faces.render.FacesBehaviorRenderer",
                           "javax.faces.view.facelets.FaceletsResourceResolver");
        FACES_ANNOTATIONS = Collections.unmodifiableSet(annotations);
        HashSet<Class<? extends Annotation>> annotationInstances =
              new HashSet<Class<? extends Annotation>>(8, 1.0f);
        Collections.addAll(annotationInstances,
                           FacesComponent.class,
                           FacesConverter.class,
                           FacesValidator.class,
                           FacesRenderer.class,
                           ManagedBean.class,
                           NamedEvent.class,
                           FacesBehavior.class,
                           FacesBehaviorRenderer.class,
                           FaceletsResourceResolver.class);
        FACES_ANNOTATION_TYPE = Collections.unmodifiableSet(annotationInstances);
    }

    private List<ScriptManager> scriptManagers = new ArrayList<ScriptManager>();
    private boolean isAnnotationScanPackagesSet = false;
    private String[] webInfClassesPackages;
    private Map<String,String[]> classpathPackages;


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="constructors">

    /**
     * Creates a new <code>AnnotationScanner</code> instance.
     *
     * @param sc the <code>ServletContext</code> for the application to be
     *  scanned
     */
    public AnnotationScanner(ServletContext sc) {
        super(sc);

        if (GroovyHelper.isGroovyAvailable(FacesContext.getCurrentInstance())) {
            scriptManagers.add(new GroovyScriptManager(sc));
        }

        WebConfiguration webConfig = WebConfiguration.getInstance(sc);
	initializeAnnotationScanPackages(sc, webConfig);

    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="implementation details">

    private void initializeAnnotationScanPackages(ServletContext sc, WebConfiguration webConfig) {
        if (!webConfig.isSet(AnnotationScanPackages)) {
            return;
        }
        isAnnotationScanPackagesSet = true;
        classpathPackages = new HashMap<String,String[]>(4);
        webInfClassesPackages = new String[0];
        String[] options = webConfig.getOptionValue(AnnotationScanPackages, "\\s+");
        List<String> packages = new ArrayList<String>(4);
        for (String option : options) {
            if (option.length() == 0) {
                continue;
            }
            if (option.startsWith("jar:")) {
                String[] parts = Util.split(sc, option, ":");
                if (parts.length != 3) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING,
                                   "jsf.annotation.scanner.configuration.invalid",
                                   new String[] { AnnotationScanPackages.getQualifiedName(), option });
                    }
                } else {
                    if (WILDCARD.equals(parts[1]) && !classpathPackages.containsKey(WILDCARD)) {
                        classpathPackages.clear();
                        classpathPackages.put(WILDCARD, normalizeJarPackages(Util.split(sc, parts[2], ",")));
                    } else if (WILDCARD.equals(parts[1]) && classpathPackages.containsKey(WILDCARD)) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING,
                                       "jsf.annotation.scanner.configuration.duplicate.wildcard",
                                       new String[] { AnnotationScanPackages.getQualifiedName(), option });
                        }
                    } else {
                        if (!classpathPackages.containsKey(WILDCARD)) {
                            classpathPackages.put(parts[1], normalizeJarPackages(Util.split(sc, parts[2], ",")));
                        }
                    }
                }
            } else {
                if (WILDCARD.equals(option) && !packages.contains(WILDCARD)) {
                    packages.clear();
                    packages.add(WILDCARD);
                } else {
                    if (!packages.contains(WILDCARD)) {
                        packages.add(option);
                    }
                }
            }
        }
        webInfClassesPackages = packages.toArray(new String[packages.size()]);
   }

    private String[] normalizeJarPackages(String[] packages) {

        if (packages.length == 0) {
            return packages;
        }
        List<String> normalizedPackages = new ArrayList<String>(packages.length);
        for (String pkg : packages) {
            if (WILDCARD.equals(pkg)) {
                normalizedPackages.clear();
                normalizedPackages.add(WILDCARD);
                break;
            } else {
                normalizedPackages.add(pkg);
            }
        }
        return normalizedPackages.toArray(new String[normalizedPackages.size()]);

    }

    // </editor-fold>


    // --------------------------------------------------------- Protected Methods

    protected boolean processJar(String entry) {

	// <editor-fold defaultstate="collapsed">

        return (classpathPackages == null
                  || (classpathPackages.containsKey(entry)
                         || classpathPackages.containsKey(WILDCARD)));

	// </editor-fold>

    }

    /**
     * @param candidate the class that should be processed
     * @return <code>true</code> if the class should be processed further,
     *  otherwise, <code>false</code>
     */
    protected boolean processClass(String candidate) {

	// <editor-fold defaultstate="collapsed">

        return processClass(candidate, webInfClassesPackages);

	// </editor-fold>
    }

    protected boolean processClass(String candidate, String [] packages) {

	// <editor-fold defaultstate="collapsed">

        if (packages == null) {
            return true;
        }

        for (String packageName : packages) {
            if (candidate.startsWith(packageName) || WILDCARD.equals(packageName)) {
                return true;
            }
        }
        return false;

	// </editor-fold>
    }


    protected Map<Class<? extends Annotation>,Set<Class<?>>> processClassList(Set<String> classList) {

	// <editor-fold defaultstate="collapsed">

        Map<Class<? extends Annotation>,Set<Class<?>>> annotatedClasses = null;
        if (classList.size() > 0) {
            annotatedClasses = new HashMap<Class<? extends Annotation>,Set<Class<?>>>(6, 1.0f);
            for (String className : classList) {
                try {
                    Class<?> clazz = Util.loadClass(className, this);
                    Annotation[] annotations = clazz.getAnnotations();
                    for (Annotation annotation : annotations) {
                        Class<? extends Annotation> annoType =
                              annotation.annotationType();
                        if (FACES_ANNOTATION_TYPE.contains(annoType)) {
                            Set<Class<?>> classes = annotatedClasses.get(annoType);
                            if (classes == null) {
                                classes = new HashSet<Class<?>>();
                                annotatedClasses.put(annoType, classes);
                            }
                            classes.add(clazz);
                        }
                    }
                } catch (ClassNotFoundException cnfe) {
                    // shouldn't happen..
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "Unable to load annotated class: {0}",
                                   className);
                        LOGGER.log(Level.SEVERE, "", cnfe);
                    }
                } catch (NoClassDefFoundError ncdfe) {
                    // this is more likely
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "Unable to load annotated class: {0}, reason: {1}",
                                   new Object[] { className, ncdfe.toString()});
                    }
                }
            }
        }

        return ((annotatedClasses != null)
                ? annotatedClasses
                : Collections.<Class<? extends Annotation>, Set<Class<?>>>emptyMap());

	// </editor-fold>

    }

    protected void processScripts(Set<String> classList) {

	// <editor-fold defaultstate="collapsed">

        for (ScriptManager sm : scriptManagers) {
            classList.addAll(sm.getScripts());
        }

	// </editor-fold>

    }

    protected boolean isAnnotationScanPackagesSet() {
        return isAnnotationScanPackagesSet;
    }

    protected Map<String,String[]> getClasspathPackages() {
        return classpathPackages;
    }

    protected String [] getWebInfClassesPackages() {
        return webInfClassesPackages;
    }

}
