package com.sun.faces.application.annotation;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.render.Renderer;
import javax.faces.validator.Validator;

import com.sun.faces.util.FacesLogger;

/**
 * This class represents the central point for annotation handling within a
 * web application.
 */
public class AnnotationManager {

    private static final Logger LOGGER = FacesLogger.APPLICATION.getLogger();
    private static final Scanner RESOURCE_DEPENDENCY_SCANNER = new ResourceDependencyScanner();
    private static final Scanner LISTENER_FOR_SCANNER = new ListenerForScanner();

    /**
     * {@link Scanner} instances to be used against {@link UIComponent} classes.
     */
    private static final Scanner[] UICOMPONENT_SCANNERS = {
          RESOURCE_DEPENDENCY_SCANNER,
          LISTENER_FOR_SCANNER
    };

    /**
     * {@link Scanner} instances to be used against {@link Validator} classes.
     */
    private static final Scanner[] VALIDATOR_SCANNERS = {
          RESOURCE_DEPENDENCY_SCANNER
    };

    /**
     * {@link Scanner} instances to be used against {@link Converter} classes.
     */
    private static final Scanner[] CONVERTER_SCANNERS = {
          RESOURCE_DEPENDENCY_SCANNER
    };

    /**
     * {@link Scanner} instances to be used against {@link Renderer} classes.
     */
    private static final Scanner[] RENDERER_SCANNERS = {
          RESOURCE_DEPENDENCY_SCANNER,
          LISTENER_FOR_SCANNER
    };

    /**
     * Enum of the different processing targets and their associated
     * {@link Scanner}s
     */
    private enum ProcessingTarget {
        UIComponent(UICOMPONENT_SCANNERS),
        Validator(VALIDATOR_SCANNERS),
        Converter(CONVERTER_SCANNERS),
        Renderer(RENDERER_SCANNERS);


        @SuppressWarnings({"NonSerializableFieldInSerializableClass"})
        private Scanner[] scanners;
        ProcessingTarget(Scanner[] scanners) {
            this.scanners = scanners;
        }

    }

    /**
     * The backing cache for all annotation metadata.
     */
    private ConcurrentMap<Class<?>,Future<Map<Class<? extends Annotation>,AnnotationHandler>>> cache;


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new AnnotationManager instance.
     */
    public AnnotationManager() {

        cache = new ConcurrentHashMap<Class<?>,Future<Map<Class<? extends Annotation>,AnnotationHandler>>>(40, .75f, 32);

    }



    // ---------------------------------------------------------- Public Methods


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


    // --------------------------------------------------------- Private Methods


    /**
     * Apply all annotations associated with <code>targetClass</code>
     *
     * @param ctx the {@link javax.faces.context.FacesContext} for the current request
     * @param targetClass class of the <code>processingTarget</code>
     * @param processingTarget the type of component that is being processed
     * @param params one or more parameters to be passed to each {@link com.sun.faces.application.annotation.AnnotationHandler}
     */
    private void applyAnnotations(FacesContext ctx,
                                  Class<?> targetClass,
                                  ProcessingTarget processingTarget,
                                  Object... params) {

        Map<Class<? extends Annotation>,AnnotationHandler> map = getHandlerMap(targetClass, processingTarget);
        if (map != null && !map.isEmpty()) {
            for (AnnotationHandler handler : map.values()) {
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
    private Map<Class<? extends Annotation>, AnnotationHandler> getHandlerMap(Class<?> targetClass,
                                                                              ProcessingTarget processingTarget) {

        while (true) {
            Future<Map<Class<? extends Annotation>, AnnotationHandler>> f =
                  cache.get(targetClass);
            if (f == null) {
                ProcessAnnotationsTask t =
                      new ProcessAnnotationsTask(targetClass, processingTarget.scanners);
                FutureTask<Map<Class<? extends Annotation>, AnnotationHandler>> ft =
                      new FutureTask<Map<Class<? extends Annotation>, AnnotationHandler>>(t);
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
          implements Callable<Map<Class<? extends Annotation>, AnnotationHandler>> {

        @SuppressWarnings({"unchecked"})
        private static final Map<Class<? extends Annotation>,AnnotationHandler> EMPTY =
              Collections.EMPTY_MAP;
        private Class<?> clazz;
        private Scanner[] scanners;


        // -------------------------------------------------------- Constructors



        public ProcessAnnotationsTask(Class<?> clazz, Scanner[] scanners) {

            this.clazz = clazz;
            this.scanners = scanners;

        }


        // ------------------------------------------------------ Public Methods


        public Map<Class<? extends Annotation>,AnnotationHandler> call() throws Exception {

            Map<Class<? extends Annotation>,AnnotationHandler> map = null;
            for (Scanner scanner : scanners) {
                AnnotationHandler handler = scanner.scan(clazz);
                if (handler != null) {
                    if (map == null) {
                        map = new HashMap<Class<? extends Annotation>,AnnotationHandler>(2, 1.0f);
                    }
                    map.put(scanner.getAnnotation(), handler);
                }
            }

            return ((map != null) ? map : EMPTY);
            
        }

    } // END ProcessAnnotationsTask





    

}
