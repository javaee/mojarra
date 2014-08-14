/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.application.annotation;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.validator.Validator;

import com.sun.faces.util.FacesLogger;
import javax.faces.event.SystemEvent;

/**
 * This class represents the central point for annotation handling within a
 * web application.
 */
public class AnnotationManager {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    private static final Scanner RESOURCE_DEPENDENCY_SCANNER = new ResourceDependencyScanner();
    private static final Scanner LISTENER_FOR_SCANNER = new ListenerForScanner();
    /*
     * This code is the prototype implementation for @EJB, @Resource, ..... support.
     * 
    private static final Scanner EJB_SCANNER = new DelegatedEJBScanner();
    private static final Scanner RESOURCE_SCANNER = new DelegatedResourceScanner();
    private static final Scanner WEBSERVICE_REF_SCANNER = new DelegatedWebServiceRefScanner();
    private static final Scanner PERSISTENCE_UNIT_SCANNER = new DelegatedPersistenceUnitScanner();
    private static final Scanner PERSISTENCE_CONTEXT_SCANNER = new DelegatedPersistenceContextScanner();
    */

    /**
     * {@link Scanner} instances to be used against {@link Behavior} classes.
     */
    private static final Scanner[] BEHAVIOR_SCANNERS = {
          RESOURCE_DEPENDENCY_SCANNER /*,
          EJB_SCANNER,
          RESOURCE_SCANNER,
          WEBSERVICE_REF_SCANNER,
          PERSISTENCE_UNIT_SCANNER,
          PERSISTENCE_CONTEXT_SCANNER */
    };

    /**
     * {@link Scanner} instances to be used against {@link ClientBehaviorRenderer} classes.
     */
    private static final Scanner[] CLIENT_BEHAVIOR_RENDERER_SCANNERS = {
          RESOURCE_DEPENDENCY_SCANNER /*,
          EJB_SCANNER,
          RESOURCE_SCANNER,
          WEBSERVICE_REF_SCANNER,
          PERSISTENCE_UNIT_SCANNER,
          PERSISTENCE_CONTEXT_SCANNER */
    };

    /**
     * {@link Scanner} instances to be used against {@link UIComponent} classes.
     */
    private static final Scanner[] UICOMPONENT_SCANNERS = {
          RESOURCE_DEPENDENCY_SCANNER,
          LISTENER_FOR_SCANNER /*,
          EJB_SCANNER,
          RESOURCE_SCANNER,
          WEBSERVICE_REF_SCANNER,
          PERSISTENCE_UNIT_SCANNER,
          PERSISTENCE_CONTEXT_SCANNER */
    };

    /**
     * {@link Scanner} instances to be used against {@link Validator} classes.
     */
    private static final Scanner[] VALIDATOR_SCANNERS = {
          RESOURCE_DEPENDENCY_SCANNER /*,
          EJB_SCANNER,
          RESOURCE_SCANNER,
          WEBSERVICE_REF_SCANNER,
          PERSISTENCE_UNIT_SCANNER,
          PERSISTENCE_CONTEXT_SCANNER */
    };

    /**
     * {@link Scanner} instances to be used against {@link Converter} classes.
     */
    private static final Scanner[] CONVERTER_SCANNERS = {
          RESOURCE_DEPENDENCY_SCANNER /*,
          EJB_SCANNER,
          RESOURCE_SCANNER,
          WEBSERVICE_REF_SCANNER,
          PERSISTENCE_UNIT_SCANNER,
          PERSISTENCE_CONTEXT_SCANNER */
    };

    /**
     * {@link Scanner} instances to be used against {@link Renderer} classes.
     */
    private static final Scanner[] RENDERER_SCANNERS = {
          RESOURCE_DEPENDENCY_SCANNER,
          LISTENER_FOR_SCANNER
    };

    private static final Scanner[] EVENTS_SCANNERS = {
          RESOURCE_DEPENDENCY_SCANNER
    };

    /**
     * Enum of the different processing targets and their associated
     * {@link Scanner}s
     */
    private enum ProcessingTarget {
        Behavior(BEHAVIOR_SCANNERS),
        ClientBehaviorRenderer(CLIENT_BEHAVIOR_RENDERER_SCANNERS),
        UIComponent(UICOMPONENT_SCANNERS),
        Validator(VALIDATOR_SCANNERS),
        Converter(CONVERTER_SCANNERS),
        Renderer(RENDERER_SCANNERS),
        SystemEvent(EVENTS_SCANNERS);


        @SuppressWarnings({"NonSerializableFieldInSerializableClass"})
        private Scanner[] scanners;
        ProcessingTarget(Scanner[] scanners) {
            this.scanners = scanners;
        }

    }

    /**
     * The backing cache for all annotation metadata.
     */
    private ConcurrentMap<Class<?>,Future<Map<Class<? extends Annotation>, RuntimeAnnotationHandler>>> cache;


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new AnnotationManager instance.
     */
    public AnnotationManager() {

        cache = new ConcurrentHashMap<Class<?>,Future<Map<Class<? extends Annotation>, RuntimeAnnotationHandler>>>(40, .75f, 32);

    }



    // ---------------------------------------------------------- Public Methods


    /**
     * <p>
     * Apply the configuration metadata contained with in the <code>Collection</code>
     * of annotated classes.
     * </p>
     *
     * @param ctx FacesContext available during application initialization
     * @param annotatedClasses <code>Collection</code> of class names known
     *  to contain one or more Faces configuration annotations
     */
    public void applyConfigAnnotations(FacesContext ctx,
                                      Class<? extends Annotation> annotationType,
                                      Set<? extends Class> annotatedClasses) {

        if (annotatedClasses != null && !annotatedClasses.isEmpty()) {
            ConfigAnnotationHandler handler =
                  getConfigAnnotationHandlers().get(annotationType);
            if (handler == null) {
                throw new IllegalStateException("Internal Error: No ConfigAnnotationHandler for type: " + annotationType);
            }
            for (Class<?> clazz : annotatedClasses) {
                handler.collect(clazz, clazz.getAnnotation(annotationType));
            }
            // metadata collected, now push the configuration to the system
            handler.push(ctx);
        }
        
    }

    /**
     * Apply annotations relevant to {@link javax.faces.component.behavior.Behavior} instances.
     * @param ctx the {@link javax.faces.context.FacesContext} for the current request
     * @param b the target <code>Behavior</code> to process
     */
    public void applyBehaviorAnnotations(FacesContext ctx, Behavior b) {

        applyAnnotations(ctx, b.getClass(), ProcessingTarget.Behavior, b);
        if (b instanceof ClientBehaviorBase) {
            ClientBehaviorBase clientBehavior = (ClientBehaviorBase) b;
            String rendererType = clientBehavior.getRendererType();
            RenderKit renderKit = ctx.getRenderKit();
            if( null != rendererType && null != renderKit){
                ClientBehaviorRenderer behaviorRenderer = renderKit.getClientBehaviorRenderer(rendererType);
                if(null != behaviorRenderer){
                    applyClientBehaviorRendererAnnotations(ctx, behaviorRenderer);
                }
            }
        }

    }

    /**
     * Apply annotations relevant to {@link javax.faces.render.ClientBehaviorRenderer} instances.
     * @param ctx the {@link javax.faces.context.FacesContext} for the current request
     * @param b the target <code>ClientBehaviorRenderer</code> to process
     */
    public void applyClientBehaviorRendererAnnotations(FacesContext ctx, ClientBehaviorRenderer b) {

        applyAnnotations(ctx, b.getClass(), ProcessingTarget.ClientBehaviorRenderer, b);

    }
    
    /**
     * Apply annotations relevant to {@link javax.faces.component.UIComponent} instances.
     * @param ctx the {@link javax.faces.context.FacesContext} for the current request
     * @param c the target <code>UIComponent</code> to process
     */
    public void applyComponentAnnotations(FacesContext ctx, UIComponent c) {

        applyAnnotations(ctx, c.getClass(), ProcessingTarget.UIComponent, c);

    }


    /**
     * Apply annotations relevant to {@link javax.faces.validator.Validator} instances.
     * @param ctx the {@link javax.faces.context.FacesContext} for the current request
     * @param v the target <code>Validator</code> to process
     */
    public void applyValidatorAnnotations(FacesContext ctx, Validator v) {

        applyAnnotations(ctx, v.getClass(), ProcessingTarget.Validator, v);

    }


    /**
     * Apply annotations relevant to {@link javax.faces.convert.Converter} instances.
     * @param ctx the {@link javax.faces.context.FacesContext} for the current request
     * @param c the target <code>Converter</code> to process
     */
    public void applyConverterAnnotations(FacesContext ctx, Converter c) {

        applyAnnotations(ctx, c.getClass(), ProcessingTarget.Converter, c);

    }


    /**
     * Apply annotations relevent to {@link javax.faces.render.Renderer} instances.
     * @param ctx the {@link javax.faces.context.FacesContext} for the current request
     * @param r the <code>Renderer</code> to process
     * @param c the <code>UIComponent</code> instances that is associated with this
     *  <code>Renderer</code>
     */
    public void applyRendererAnnotations(FacesContext ctx, Renderer r, UIComponent c) {

        applyAnnotations(ctx, r.getClass(), ProcessingTarget.Renderer, r, c);

    }

    public void applySystemEventAnnotations(FacesContext ctx, SystemEvent e) {
        applyAnnotations(ctx, e.getClass(), ProcessingTarget.SystemEvent, e);
    }


    // --------------------------------------------------------- Private Methods


    /**
     * @return a new <code>Map</code> which maps the types of annotations to
     *  a specific <code>ConfigAnnotationHandler</code>.  Note that each invocation
     *  of this method constructs a new <code>Map</code> with new
     *  <code>ConfigAnnotationhandler</code> instances as they are not thread
     *  safe.
     */
    private Map<Class<? extends Annotation>,ConfigAnnotationHandler> getConfigAnnotationHandlers() {

        ConfigAnnotationHandler[] handlers = {
              new ComponentConfigHandler(),
              new ConverterConfigHandler(),
              new ValidatorConfigHandler(),
              new BehaviorConfigHandler(),
              new RenderKitConfigHandler(),
              new ManagedBeanConfigHandler(),
              new NamedEventConfigHandler()
        };
        Map<Class<? extends Annotation>,ConfigAnnotationHandler> handlerMap =
              new HashMap<Class<? extends Annotation>,ConfigAnnotationHandler>();
        for (ConfigAnnotationHandler handler : handlers) {
            Collection<Class<? extends Annotation>> handledClasses = handler.getHandledAnnotations();
            for (Class<? extends Annotation> handled : handledClasses) {
                handlerMap.put(handled, handler);
            }
        }

        return handlerMap;

    }
    

    /**
     * Apply all annotations associated with <code>targetClass</code>
     *
     * @param ctx the {@link javax.faces.context.FacesContext} for the current request
     * @param targetClass class of the <code>processingTarget</code>
     * @param processingTarget the type of component that is being processed
     * @param params one or more parameters to be passed to each {@link RuntimeAnnotationHandler}
     */
    private void applyAnnotations(FacesContext ctx,
                                  Class<?> targetClass,
                                  ProcessingTarget processingTarget,
                                  Object... params) {

        Map<Class<? extends Annotation>, RuntimeAnnotationHandler> map = getHandlerMap(targetClass, processingTarget);
        if (map != null && !map.isEmpty()) {
            for (RuntimeAnnotationHandler handler : map.values()) {
                handler.apply(ctx, params);
            }
        }

    }


    /**
     * Helper method to look up cached annotation metadata.
     * @param targetClass class of the <code>processingTarget</code>
     * @param processingTarget the type of component being processed
     * @return a Map keyed by Annotation class with an AnnotationHandler as the
     *  value
     */
    private Map<Class<? extends Annotation>, RuntimeAnnotationHandler> getHandlerMap(Class<?> targetClass,
                                                                              ProcessingTarget processingTarget) {

        while (true) {
            Future<Map<Class<? extends Annotation>, RuntimeAnnotationHandler>> f =
                  cache.get(targetClass);
            if (f == null) {
                ProcessAnnotationsTask t =
                      new ProcessAnnotationsTask(targetClass, processingTarget.scanners);
                FutureTask<Map<Class<? extends Annotation>, RuntimeAnnotationHandler>> ft =
                      new FutureTask<Map<Class<? extends Annotation>, RuntimeAnnotationHandler>>(t);
                f = cache.putIfAbsent(targetClass, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (CancellationException ce) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST,
                               ce.toString(),
                               ce);
                }
                cache.remove(targetClass);
            } catch (InterruptedException ie) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST,
                               ie.toString(),
                               ie);
                }
                cache.remove(targetClass);
            } catch (ExecutionException ee) {
                throw new FacesException(ee);
            }
        }

    }


    // ----------------------------------------------------------- Inner Classes


    /**
     * This <code>Callable</code> will leverage the provided <code>Scanner</code>s
     * to build a mapping between a particular annotation type and an
     * <code>AnnotationHandler</code> for that type.
     */
    private static final class ProcessAnnotationsTask
          implements Callable<Map<Class<? extends Annotation>, RuntimeAnnotationHandler>> {

        @SuppressWarnings({"unchecked"})
        private static final Map<Class<? extends Annotation>, RuntimeAnnotationHandler> EMPTY =
              Collections.EMPTY_MAP;
        private Class<?> clazz;
        private Scanner[] scanners;


        // -------------------------------------------------------- Constructors



        public ProcessAnnotationsTask(Class<?> clazz, Scanner[] scanners) {

            this.clazz = clazz;
            this.scanners = scanners;

        }


        // ------------------------------------------------------ Public Methods


        public Map<Class<? extends Annotation>, RuntimeAnnotationHandler> call() throws Exception {

            Map<Class<? extends Annotation>, RuntimeAnnotationHandler> map = null;
            for (Scanner scanner : scanners) {
                    RuntimeAnnotationHandler handler = scanner.scan(clazz);
                    if (handler != null) {
                        if (map == null) {
                            map = new HashMap<Class<? extends Annotation>, RuntimeAnnotationHandler>(2, 1.0f);
                        }
                        map.put(scanner.getAnnotation(), handler);
                    }
                }

            return ((map != null) ? map : EMPTY);
            
        }

    } // END ProcessAnnotationsTask
}
