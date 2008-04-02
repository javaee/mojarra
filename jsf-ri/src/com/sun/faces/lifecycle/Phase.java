/*
 * $Id: Phase.java,v 1.4 2004/02/06 18:55:08 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.lifecycle;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;


/**
 * <p>A <strong>Phase</strong> is a single step in the processing of a
 * JavaServer Faces request throughout its entire {@link Lifecycle}.  Each
 * <code>Phase</code> performs the required transitions on the state
 * information in the {@link FacesContext} associated with this request.
 */

public abstract class Phase {

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
     * @throws FacesException if a processing error occurred while
     *                        executing this phase
     */
    public abstract void execute(FacesContext context) throws FacesException;


    /**
     * <p>Returns the current {@link Lifecycle} <strong>Phase</strong> identifier.
     */
    public abstract PhaseId getId();

}
