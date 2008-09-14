package com.sun.faces.application.annotation;

import java.lang.annotation.Annotation;
import java.util.Collection;

import javax.faces.context.FacesContext;

/**
 * Implementations of the interface will be called during application
 * initialization to process any configuration annotations within the
 * web application.
 */
public interface ConfigAnnotationHandler {

    /**
     * @return a <code>Collection</code> of annotations handled by this
     *  ConfigAnnotationHandler implementation
     */
    public Collection<Class<? extends Annotation>> getHandledAnnotations();


    /**
     * <p>
     * Collect metadata based on the provided <code>Class</code> and
     * <code>Annotation</code> to be processed later by {@link #push(javax.faces.context.FacesContext)}.
     * </p>
     *
     * <p>
     * NOTE: This method may be called more than once.
     * </p>
     *
     *
     * @param target annotated class
     * @param annotation <code>Annotation</code> to process
     */
    public void collect(Class<?> target, Annotation annotation);


    /**
     * <code>Push<code> the configuration based on the collected metadata
     * to the current application.
     */
    public void push(FacesContext ctx);    

}
