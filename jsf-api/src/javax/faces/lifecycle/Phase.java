/*
 * $Id: Phase.java,v 1.4 2003/02/03 22:57:50 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.lifecycle;

import javax.faces.FacesException;     // FIXME - subpackage?
import javax.faces.context.FacesContext;


/**
 * <p>A <strong>Phase</strong> is a single step in the processing of a
 * JavaServer Faces request throughout its entire {@link Lifecycle}.  Each
 * <code>Phase</code> performs the required transitions on the state
 * information in the {@link FacesContext} associated with this request,
 * and returns one of the specified state change values to determine which
 * <code>Phase</code> (if any) will be the next one to be processed.</p>
 *
 * <p>Individual <code>Phase</code> instances must be instantiated and
 * associated with a particular {@link Lifecycle} instance in the
 * {@link LifecycleFactory} instance for this web application, prior to
 * return from <code>getLifecycle()</code>.  Each <code>Phase</code>
 * instance will be associated with exactly one {@link Lifecycle} instance,
 * but must be programmed in a thread-safe manner because it will be used
 * to process simultaneous requests on multiple threads.</p>
 *
 * @deprecated The {@link Phase} API will likely be removed in a future
 *  version of JavaServer Faces.
 */

public abstract class Phase {


    // ---------------------------------------------------- State Change Values


    /**
     * <p>Return value from <code>execute()</code> that terminates execution
     * of the request processing lifecycle for this request.  This value will
     * normally be returned only from the <em>Render Response</em> phase, but
     * might also be returned in abnormal circumstances such as execution of
     * an HTTP redirect.  In all cases where this value is returned, the
     * phase implementation must have guaranteed that the
     * <code>ServletResponse</code> associated with this request has been
     * completed.</p>
     */
    public static final int GOTO_EXIT = -1;


    /**
     * <p>Return value from <code>execute()</code> that requests execution of
     * the next phase in order.</p>
     */
    public static final int GOTO_NEXT = -2;


    /**
     * <p>Return value from <code>execute()</code> that requests execution
     * to skip intervening lifecycle phases and resume with the
     * <em>Render Response</em> phase.  Execution will start with the latest
     * custom phase added (via <code>LifecycleFactory.registerBefore()</code>
     * for the <em>Render Response</em> phase for our lifecycle instance.</p>
     */
    public static final int GOTO_RENDER = -3;


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Perform all state transitions required by the current phase of the
     * request processing {@link Lifecycle} for a particular request.
     * Return one of the standard state change values (<code>GOTO_EXIT</code>,
     * <code>GOTO_NEXT</code>, or <code>GOTO_RENDER</code>) to indicate what
     * the request processing lifecycle should do next.<?p>
     *
     * @param context FacesContext for the current request being processed
     *
     * @exception FacesException if a processing error occurred while
     *  executing this phase
     */
    public abstract int execute(FacesContext context) throws FacesException;


}
