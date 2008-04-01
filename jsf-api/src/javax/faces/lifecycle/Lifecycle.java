/*
 * $Id: Lifecycle.java,v 1.10 2002/06/12 21:51:27 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.lifecycle;

import java.util.SortedMap;
import javax.faces.FacesException;     // FIXME - subpackage?
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
 * it is a per-web-application Singleton.  Because this instance is
 * shared across multiple simultaneous requests, it must be implemented
 * in a thread-safe manner.</p>
 *
 * <p>The set of {@link Phase} instances associated with a particular
 * <code>Lifecycle</code> instance, as well as the order that they are
 * executed in, must be configured during execution of the
 * <code>getLifecycle()</code> method in {@link LifecycleFactory}.  For
 * each standard phase identifier, a JavaServer Faces implementation will
 * provide a default {@link Phase} instance that implements the
 * required behavior of that phase.  In addition, it is possible for an
 * application to register additional {@link Phase} instances, to be executed
 * before or after the standard instance, for a particular phase identifier,
 * by calling the <code>registerBefore()</code> or <code>registerAfter()</code>
 * methods of {@link LifecycleFactory}.  Note that registration of custom
 * {@link Phase} instances must be completed before the call to
 * <code>getLifecycle()</code> that creates this <code>Lifecycle</code>
 * instance.</p>
 *
 * <p>The <code>execute()</code> method of <code>Lifecycle</code> is run
 * once per incoming request processed by a Faces application, according
 * to the following algorithm:</p>
 * <blockquote>
 * <p><strong>FIXME</strong> - Describe the algorithm used to identify
 * which phase instances are called in which order (including transitions
 * to <em>Render Response</em> starting with custom phases registered to
 * run before the standard one), and when phase listeners are invoked.</p>
 * </blockquote>
 */

public abstract class Lifecycle {


    // ------------------------------------------------------ Phase Identifiers


    /**
     * <p>Phase identifier for <em>Create Request Tree</em>.</p>
     */
    public static final int CREATE_REQUEST_TREE_PHASE = 0;


    /**
     * <p>Phase identifier for <em>Reconstitute Request Tree</em>.</p>
     */
    public static final int RECONSTITUTE_REQUEST_TREE_PHASE = 10;


    /**
     * <p>Phase identifier for <em>Apply Request Values</em>.</p>
     */
    public static final int APPLY_REQUEST_VALUES_PHASE = 20;


    /**
     * <p>Phase identifier for <em>Handle Request Events</em>.</p>
     */
    public static final int HANDLE_REQUEST_EVENTS_PHASE = 30;


    /**
     * <p>Phase identifier for <em>Process Validations</em>.</p>
     */
    public static final int PROCESS_VALIDATIONS_PHASE = 40;


    /**
     * <p>Phase identifier for <em>Update Model Values</em>.</p>
     */
    public static final int UPDATE_MODEL_VALUES_PHASE = 50;


    /**
     * <p>Phase identifier for <em>Invoke Application</em>.</p>
     */
    public static final int INVOKE_APPLICATION_PHASE = 60;


    /**
     * <p>Phase identifier for <em>Render Response</em>.</p>
     */
    public static final int RENDER_RESPONSE_PHASE = 70;


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
     */
    public abstract void setApplicationHandler(ApplicationHandler handler);


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Execute the {@link Phase}s registered for this <code>Lifecycle</code>
     * instance, according to the algorithm described in the class description.
     * As phases are executed, call the <code>setPhaseId()</code> method of
     * {@link FacesContext} as transitions between phases occur.</p>
     *
     * <p><strong>FIXME</strong> - setPhaseId() to what value when done?</p>
     *
     * @param context FacesContext for the current request being processed
     *
     * @exception IllegalStateException if a {@link Phase} returned an invalid
     *  state change value from the <code>execute()</code> method
     * @exception FacesException if a {@link Phase} threw such an exception
     *  from its <code>execute()</code> method
     */
    public abstract void execute(FacesContext context) throws FacesException;


    /**
     * <p>Register a new {@link PhaseListener} that will receive notification
     * before and after the execution of each {@link Phase} of the request
     * processing lifecycle for this request.</p>
     *
     * <p><strong>FIXME</strong> - Should this registration perhaps be on
     * {@link LifecycleFactory} instead, keyed to the logical lifecycle id?</p>
     *
     * @param listener The {@link PhaseListener} instance to be added
     */
    public abstract void addPhaseListener(PhaseListener listener);


    /**
     * <p>Deregister a {@link PhaseListener} that was receiving notification
     * before and after the execution of each {@link Phase} of the request
     * processing lifecycle for this request.</p>
     *
     * @param listener The {@link PhaseListener} instance to be removed
     */
    public abstract void removePhaseListener(PhaseListener listener);


}
