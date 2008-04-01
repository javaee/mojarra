/*
 * $Id: PhaseListener.java,v 1.2 2002/05/15 23:49:30 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.lifecycle;

import javax.faces.FacesException;     // FIXME - subpackage?
import javax.faces.context.FacesContext;


/**
 * <p>A <strong>PhaseListener</strong> is notified before and after the
 * execution of each {@link Phase} by the corresponding {@link Lifecycle}.
 * Such listeners can be added to a {@link Lifecycle} by calls to the
 * <code>addPhaseListener()</code> method.</p>
 */

public interface PhaseListener {


    /**
     * <p>Process a notification that a specified {@link Phase} is about to
     * be executed by the corresponding {@link Lifecycle}.</p>
     *
     * @param context The {@link FacesContext} for the current request
     * @param phaseId Phase identifier of the current processing phase
     * @param phase The {@link Phase} whose <code>execute()</code> method
     *  is about to be called
     */
    public void entering(FacesContext context, int phaseId, Phase phase);


    /**
     * <p>Process a notification that a specified {@link Phase} has just
     * returned from execution by the corresponding {@link Lifecycle}.</p>
     *
     * @param context The {@link FacesContext} for the current request
     * @param phaseId Phase identifier of the current processing phase
     * @param phase The {@link Phase} whose <code>execute()</code> method
     *  has just returned
     * @param stateChange The state change value returned by the
     *  <code>execute()</code> that has just returned
     */
    public void exiting(FacesContext context, int phaseId,
                        Phase phase, int stateChange);


}
