/*
 * $Id: Lifecycle.java,v 1.16 2002/09/19 23:35:05 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.lifecycle;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p><strong>Lifecycle</strong> is a state machine that manages the
 * processing of the entire lifecycle of a particular JavaServer Faces
 * request.  It is responsible for executing all {@link Phase}s that have
 * been registered, in ascending order of phase identifiers, unless otherwise
 * directed by the value returned by execution of each {@link Phase}.</p>
 *
 * <p>An instance of <code>Lifecycle</code> is created by calling the
 * <code>getLifecycle()</code> method of {@link LifecycleFactory}, for
 * a specified lifecycle identifier.  Subsequent calls to
 * <code>getLifecycle()</code> will return the same instance (i.e.
 * it is a per-web-application Singleton).  Because this instance is
 * shared across multiple simultaneous requests, it must be implemented
 * in a thread-safe manner.  At least one <code>Lifecycle</code> implementation
 * must be provided as the default.</p>
 *
 * <p>The set of {@link Phase} instances associated with a particular
 * <code>Lifecycle</code> instance, as well as the order that they are
 * executed in, must be configured prior to, or during, execution of the
 * <code>getLifecycle()</code> method in {@link LifecycleFactory}.  For
 * each standard phase identifier, a JavaServer Faces implementation will
 * provide a default {@link Phase} instance that implements the
 * required behavior of that phase.  A particular instance of
 * <code>Lifecycle</code> may execute additional, implementation-defined,
 * {@link Phase}s in between the execution of the standard phases, as long
 * as the ordering of standard phases is maintained, and the state change
 * indicator values returned by the <code>execute()</code> method of
 * standard {@link Phase}s is respected.</p>
 *
 * <p><strong>FIXME</strong> - Ongoing EG discussion about whether a
 * JSF implementation must use the execute() method defined here, or may
 * use an adapter pattern to call <code>executePhase()</code> for each
 * phase individually.</p>
 *
 * <p><strong>FIXME</strong> - Do we really need defined phase identifiers
 * any more?  We will need them if we reintroduce registerBefore() and
 * registerAfter(), but that is probably not needed if the adapter pattern
 * is the one we end up with.</p>
 */

public abstract class Lifecycle {


    // ------------------------------------------------------ Phase Identifiers


    /**
     * <p>Phase identifier for <em>Reconstitute Request Tree</em>.</p>
     */
    public static final int RECONSTITUTE_REQUEST_TREE_PHASE = 0;


    /**
     * <p>Phase identifier for <em>Apply Request Values</em>.</p>
     */
    public static final int APPLY_REQUEST_VALUES_PHASE = 10;


    /**
     * <p>Phase identifier for <em>Handle Request Events</em>.</p>
     */
    public static final int HANDLE_REQUEST_EVENTS_PHASE = 20;


    /**
     * <p>Phase identifier for <em>Process Validations</em>.</p>
     */
    public static final int PROCESS_VALIDATIONS_PHASE = 30;


    /**
     * <p>Phase identifier for <em>Update Model Values</em>.</p>
     */
    public static final int UPDATE_MODEL_VALUES_PHASE = 40;


    /**
     * <p>Phase identifier for <em>Invoke Application</em>.</p>
     */
    public static final int INVOKE_APPLICATION_PHASE = 50;


    /**
     * <p>Phase identifier for <em>Render Response</em>.</p>
     */
    public static final int RENDER_RESPONSE_PHASE = 60;


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the {@link ApplicationHandler} instance that will be utilized
     * during the <em>Invoke Application</em> phase of the request processing
     * lifecycle.</p>
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
     * <p>Execute the {@link Phase}s registered for this <code>Lifecycle</code>
     * instance, in the order required by the specification.  The execution
     * of each individual {@link Phase} must be accomplished by calling the
     * <code>executePhase()</code> method for that phase.</p>
     *
     * @param context FacesContext for the current request being processed
     *
     * @exception IllegalStateException if a {@link Phase} returned an invalid
     *  state change value from the <code>execute()</code> method
     * @exception FacesException if a {@link Phase} threw such an exception
     *  from its <code>execute()</code> method
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
     */
    public abstract int executePhase(FacesContext context, Phase phase)
        throws FacesException;


}
