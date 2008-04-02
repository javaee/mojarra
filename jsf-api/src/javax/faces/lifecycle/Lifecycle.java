/*
 * $Id: Lifecycle.java,v 1.20 2003/02/20 22:46:33 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.lifecycle;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p><strong>Lifecycle</strong> is a state machine that manages the
 * processing of the entire lifecycle of a particular JavaServer Faces
 * request.  It is responsible for executing all of the phases that have
 * been defined by the JavaServer Faces Specification, in the specified
 * order, unless otherwise directed by activities that occurred during
 * the execution of each phase.</p>
 *
 * <p>An instance of <code>Lifecycle</code> is created by calling the
 * <code>getLifecycle()</code> method of {@link LifecycleFactory}, for
 * a specified lifecycle identifier.  Because this instance is
 * shared across multiple simultaneous requests, it must be implemented
 * in a thread-safe manner.</p>
 */

public abstract class Lifecycle {


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the {@link ApplicationHandler} instance that will be utilized
     * during the <em>Invoke Application</em> phase of the request processing
     * lifecycle.</p>
     *
     * @deprecated The current mechanism for handling application events is a
     *  placeholder, and will be replaced in the next public release of
     *  JavaServer Faces.
     */
    public abstract ApplicationHandler getApplicationHandler();


    /**
     * <p>Set the {@link ApplicationHandler} instance that will be utilized
     * during the <em>Invoke Application</em> phase of the request processing
     * lifecycle.</p>
     *
     * @param handler The new {@link ApplicationHandler} instance
     *
     * @exception IllegalStateException if this method is called after at least
     *  one request has been processed by this <code>Lifecycle</code> instance
     * @exception NullPointerException if <code>handler</code>
     *  is <code>null</code>
     *
     * @deprecated The current mechanism for handling application events is a
     *  placeholder, and will be replaced in the next public release of
     *  JavaServer Faces.
     */
    public abstract void setApplicationHandler(ApplicationHandler handler);


    /**
     * <p>Return the {@link ViewHandler} instance that will be utilized
     * during the <em>Render Response</em> phase of the request processing
     * lifecycle.</p>
     */
    public abstract ViewHandler getViewHandler();


    /**
     * <p>Set the {@link ViewHandler} instance that will be utilized
     * during the <em>Render Response</em> phase of the request processing
     * lifecycle.</p>
     *
     * @param handler The new {@link ViewHandler} instance
     *
     * @exception IllegalStateException if this method is called after at least
     *  one request has been processed by this <code>Lifecycle</code> instance
     * @exception NullPointerException if <code>handler</code>
     *  is <code>null</code>
     */
    public abstract void setViewHandler(ViewHandler handler);


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Execute all of the phases of the request processing lifecycle,
     * as described in the JavaServer Faces Specification, in the specified
     * order.  The processing flow can be affected (by the application,
     * by components, or by event listeners) by calls to the
     * <code>renderResponse()</code> or <code>responseComplete()</code>
     * methods of the {@link FacesContext} instance associated with
     * the current request.</p>
     *
     * @param context FacesContext for the request to be processed
     *
     * @exception FacesException if thrown during the execution of the
     *  request processing lifecycle
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract void execute(FacesContext context) throws FacesException;


    /**
     * <p>Execute the specified {@link Phase} as part of the request processing
     * lifecycle being managed by this <code>Lifecycle</code> instance, by
     * calling its <code>execute()</code> method.  Return the <em>state
     * change indicator</em> value that was returned by the {@link Phase}.</p>
     *
     * @param context FacesContext for the current request being processed
     * @param phase Phase instance to be processed
     *
     * @exception FacesException if thrown by the <code>execute()</code>
     *  method of the {@link Phase} that was executed
     * @exception IllegalStateException if a {@link Phase} returned an invalid
     *  state change value from the <code>execute()</code> method
     * @exception NullPointerException if <code>context</code> or
     *  <code>phase</code> is <code>null</code>
     *
     * @deprecated The {@link Phase} API will likely be removed in a future
     *  version of JavaServer Faces.
     */
    public abstract int executePhase(FacesContext context, Phase phase)
        throws FacesException;


}
