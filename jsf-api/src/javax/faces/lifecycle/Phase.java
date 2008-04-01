/*
 * $Id: Phase.java,v 1.1 2002/05/07 05:18:58 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
 * and returns the phase identifier of the phase that is to be executed
 * next.</p>
 *
 * <h3>Lifecycle</h3>
 *
 * <p>At application startup time, one or more {@link Lifecycle} instances
 * will be instantiated and configured.  The set of <code>Phase</code>s
 * associated with each {@link Lifecycle} (both standard ones provided by
 * the JavaServer Faces implementation, and any application-specific
 * custom <code>Phase</code>s) will be registered at that time, and will
 * have the same lifetime as the corresponding {@link Lifecycle} instances.
 * </p>
 */

public abstract class Phase {


    /**
     * <p>Perform all transitions required by the current phase of the
     * request processing {@link Lifecycle} for a particular request.
     * In general, state transitions will be recorded in the
     * {@link FacesContext} for which our {@link Lifecycle} is performed.
     * Return the phase identifier of the next phase that should be executed,
     * <code>Lifecycle.NEXT_PHASE</code> to select the next registered
     * phase in sequence, or <code>Lifecycle.EXIT_PHASE</code> to indicate
     * that the processing for this request has been completed.</p>
     *
     * <p><strong>FIXME</strong> - Representation of phase ids
     * as integers?</p>
     *
     * @param context FacesContext for the current request being processed
     *
     * @exception FacesException if a processing error occurred while
     *  executing this phase
     */
    public abstract int execute(FacesContext context) throws FacesException;


    /**
     * <p>Return the {@link Lifecycle} with which this <code>Phase</code>
     * is associated.</p>
     *
     * <p><strong>FIXME</strong> - does this method need to be public?</p>
     */
    public abstract Lifecycle getLifecycle();


    /**
     * <p>Return the phase identifier of this <code>Phase</code>, which must be
     * unique among the <code>Phases</code> registered with our associated
     * {@link Lifecycle}.</p>
     *
     * <p><strong>FIXME</strong> - does this method need to be public?</p>
     *
     * <p><strong>FIXME<strong> - Representation of phase ids
     * as integers?</p>
     */
    public abstract int getPhaseId();


}
