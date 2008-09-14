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

    public Collection<Class<? extends Annotation>> getHandledAnnotations();

    public void collect(Class<?> target, Annotation annotation);

    public void push(FacesContext ctx);    

}
