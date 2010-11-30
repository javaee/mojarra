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
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


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
 * </ul>
 */
public class AnnotationScanner extends AnnotationProvider {

    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();


    protected static final Set<String> FACES_ANNOTATIONS;
    protected static final Set<Class<? extends Annotation>> FACES_ANNOTATION_TYPE;

    static {
        HashSet<String> annotations = new HashSet<String>(8, 1.0f);
        Collections.addAll(annotations,
                           "Ljavax/faces/component/FacesComponent;",
                           "Ljavax/faces/convert/FacesConverter;",
                           "Ljavax/faces/validator/FacesValidator;",
                           "Ljavax/faces/render/FacesRenderer;",
                           "Ljavax/faces/bean/ManagedBean;",
                           "Ljavax/faces/event/NamedEvent;",
                           "Ljavax/faces/component/behavior/FacesBehavior;",
                           "Ljavax/faces/render/FacesBehaviorRenderer;");
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
                           FacesBehaviorRenderer.class);
        FACES_ANNOTATION_TYPE = Collections.unmodifiableSet(annotationInstances);
    }

    private List<ScriptManager> scriptManagers = new ArrayList<ScriptManager>();

    // ------------------------------------------------------------ Constructors


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

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * @return a <code>Map</code> of classes mapped to a specific annotation type.
     *  If no annotations are present, or the application is considered
     * <code>metadata-complete</code> <code>null</code> will be returned.
     */
    public Map<Class<? extends Annotation>,Set<Class<?>>> getAnnotatedClasses(Set<URL> urls) {

        Set<String> classList = new HashSet<String>();

        processScripts(classList);

        return processClassList(classList);

    }


    // --------------------------------------------------------- Protected Methods

    protected Map<Class<? extends Annotation>,Set<Class<?>>> processClassList(Set<String> classList) {
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
    }

    protected void processScripts(Set<String> classList) {
        for (ScriptManager sm : scriptManagers) {
            classList.addAll(sm.getScripts());
        }
    }

}
