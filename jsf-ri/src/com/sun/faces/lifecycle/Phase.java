/*
 * $Id: Phase.java,v 1.6 2005/08/22 22:10:15 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
     * @throws FacesException if a processing error occurred while
     *                        executing this phase
     */
    public abstract void execute(FacesContext context) throws FacesException;


    /**
     * <p>Returns the current {@link Lifecycle} <strong>Phase</strong> identifier.
     */
    public abstract PhaseId getId();

}
