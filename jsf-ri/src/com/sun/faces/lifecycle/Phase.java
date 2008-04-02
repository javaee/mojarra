/*
 * $Id: Phase.java,v 1.1 2003/03/12 19:51:06 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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

    /**
     * <p>Phase identifier for <em>Reconstitute Request Tree</em>.</p>
     */
    public static final int RECONSTITUTE_COMPONENT_TREE = 0;


    /**
     * <p>Phase identifier for <em>Apply Request Values</em>.</p>
     */
    public static final int APPLY_REQUEST_VALUES = 10;


    /**
     * <p>Phase identifier for <em>Process Validations</em>.</p>
     */
    public static final int PROCESS_VALIDATIONS = 20;


    /**
     * <p>Phase identifier for <em>Update Model Values</em>.</p>
     */
    public static final int UPDATE_MODEL_VALUES = 30;


    /**
     * <p>Phase identifier for <em>Invoke Application</em>.</p>
     */
    public static final int INVOKE_APPLICATION = 40;


    /**
     * <p>Phase identifier for <em>Render Response</em>.</p>
     */
    public static final int RENDER_RESPONSE = 50;


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
    public abstract void execute(FacesContext context) throws FacesException;

    /**
     * <p>Returns the current {@link Lifecycle} <strong>Phase</strong> identifier.
     */
    public abstract int getId();

}
