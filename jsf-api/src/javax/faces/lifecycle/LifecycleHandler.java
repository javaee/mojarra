/*
 * $Id: LifecycleHandler.java,v 1.1 2002/05/15 01:03:53 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.lifecycle;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p><strong>LifecycleHandler</strong> encapsulates the behavioral aspects
 * of component-level processing of the standard lifecycle phases.
 * It may be implemented by components that wish to provide all of the
 * required processing logic in a single place, or in separate classes to
 * which lifecycle processing is delegated by the components.</p>
 */

public interface LifecycleHandler {


    /**
     * <p>Handler for the <em>Reconstitute Request Tree</em> phase.  Ensure
     * that the properties and attributes of the specified component
     * reflect the values that were current as of the previous response.</p>
     *
     * @param context FacesContext of the request we are processing
     * @param component Current UIComponent being processed
     */
    public void reconstituteRequestTree(FacesContext context,
                                        UIComponent component);


    /**
     * <p>Handler for the <em>Apply Request Values</em> phase.  Extract
     * the new value (if any) from the current request, and update the
     * local value of the specified component.  While values are being
     * extracted, optionally queue events to be processed during the
     * next phase by (<strong>FIXME</strong> - specify mechanism).</p>
     *
     * @param context FacesContext of the request we are processing
     * @param component Current UIComponent being processed
     */
    public void applyRequestValues(FacesContext context,
                                   UIComponent component);


    /**
     * <p>Handler for the <em>Handle Request Events</em> phase.  Process
     * the specified event, for the specified component, that was queued
     * during the previous phase.  This method will be called once per
     * event that was queued.</p>
     *
     * @param context FacesContext of the request we are processing
     * @param component Current UIComponent being processed
     * @param event <strong>FIXME</strong> - event data???
     */
    public void handleRequestEvents(FacesContext context,
                                    UIComponent component); // FIXME - event



    /**
     * <p>Handler for the <em>Process Validations</em> phase.  Perform
     * all registered validations for the specified component, accumulating
     * error messages by (<strong>FIXME</strong> - specify mechanism).
     *
     * @param context FacesContext of the request we are processing
     * @param component Current UIComponent being processed
     */
    public void processValidations(FacesContext context,
                                   UIComponent component);


    /**
     * <p>Handler for the <em>Update Model Values</em> phase.  Copy the
     * local value of this component to the corresponding model object, if
     * a model reference has been defined for this component.
     * 
     * @param context FacesContext of the request we are processing
     * @param component Current UIComponent being processed
     */
    public void updateModelValues(FacesContext context,
                                  UIComponent component);


    /**
     * <p>Handler for the <em>Render Response</em> phase.  If a
     * <code>rendererType</code> has been defined for this component,
     * acquire a corresponding {@link Renderer} instance
     * from our {@link RenderKit}, and use it to render the state of this
     * component to the response.  If no <code>rendererType</code> has
     * been defined for this component, render the state of this
     * component directly.</p>
     *
     * <p><strong>FIXME</strong> - Deal with the complexities of
     * conditionally rendering child components.</p>
     * 
     * @param context FacesContext of the request we are processing
     * @param component Current UIComponent being processed
     */
    public void renderResponse(FacesContext context,
                               UIComponent component);



}
