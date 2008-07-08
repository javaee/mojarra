package com.sun.faces.application.annotation;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

/**
 * Implementations of this class provide basic caching and processing of
 * of {@link java.lang.annotation.Annotation} instances.
 */
interface AnnotationHandler {

    /**
     * <p>Apply the {@link java.lang.annotation.Annotation}(s). The act
     * of doing so should affect the JSF runtime in some fashion (see the spec
     * for the specific annotation types).</p>
     *
     * <p>
     * <em>NOTE</em>: when adding new types of components that can be annotated,
     * the fact that we expose varargs here should be hidden.  Type-safe methods
     * should be added to {@link AnnotationManager} to clarify the contract.
     * </p>
     *
     * @param ctx the {@link javax.faces.context.FacesContext} for the current
     *  request
     * @param params one or more arguments to the handler instance.  The type
     *  and number may vary.
     */
    public void apply(FacesContext ctx, Object... params);
    
}
