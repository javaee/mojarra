/*
 * $Id: Lifecycle.java,v 1.29 2004/02/26 20:31:05 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.lifecycle;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;


/**
 * <p><strong>Lifecycle</strong> manages the
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


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Register a new {@link PhaseListener} instance that is interested in
     * being notified before and after the processing for standard phases of
     * the request processing lifecycle.</p>
     *
     * @param listener The {@link PhaseListener} to be registered
     *
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public abstract void addPhaseListener(PhaseListener listener);


    /**
     * <p>Execute all of the phases of the request processing lifecycle,
     * up to but not including the <em>Render Response</em> phase,
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
     * <p>Return the set of registered {@link PhaseListener}s for this
     * {@link Lifecycle} instance.  If there are no registered listeners,
     * a zero-length array is returned.</p>
     */
    public abstract PhaseListener[] getPhaseListeners();


    /**
     * <p>Deregister an existing {@link PhaseListener} instance that is no
     * longer interested in being notified before and after the processing
     * for standard phases of the request processing lifecycle.  If no such
     * listener instance has been registered, no action is taken.</p>
     *
     * @param listener The {@link PhaseListener} to be deregistered
     * @exception NullPointerException if <code>listener</code>
     *  is <code>null</code>
     */
    public abstract void removePhaseListener(PhaseListener listener);


    /**
     * <p>Execute the <em>Render Response</em> phase of the request
     * processing lifecycle, unless the <code>responseComplete()</code>
     * method has been called on the {@link FacesContext} instance
     * associated with the current request.</p>
     *
     * @param context FacesContext for the request being processed
     *
     * @exception FacesException if an exception is thrown during the execution
     *  of the request processing lifecycle
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public abstract void render(FacesContext context) throws FacesException;


}
