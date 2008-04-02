/*
 * $Id: PhaseEvent.java,v 1.5 2005/08/22 22:08:05 ofung Exp $
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

package javax.faces.event;


import java.util.EventObject;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;


/**
 * <p><strong>PhaseEvent</strong> represents the beginning or ending of
 * processing for a particular phase of the request processing lifecycle,
 * for the request encapsulated by the specified {@link FacesContext}.</p>
 */

public class PhaseEvent extends EventObject {

    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new event object from the specified parameters.
     * The specified {@link Lifecycle} will be the source of this event.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param phaseId Identifier of the current request processing
     *  lifecycle phase
     * @param lifecycle Lifecycle instance
     *
     * @exception NullPointerException if <code>context</code> or
     *  <code>phaseId</code> or <code>Lifecycle</code>is <code>null</code>
     */
    public PhaseEvent(FacesContext context, PhaseId phaseId, 
            Lifecycle lifecycle) {

        super(lifecycle);
        if ( phaseId == null || context == null || lifecycle == null) {
            throw new NullPointerException();
        }
	this.phaseId = phaseId;
        this.context = context;

    }


    // ------------------------------------------------------------- Properties

    private FacesContext context = null;
    
    /**
     * <p>Return the {@link FacesContext} for the request being processed.</p>
     */
    public FacesContext getFacesContext() {

        return context;

    }


    private PhaseId phaseId = null;


    /**
     * <p>Return the {@link PhaseId} representing the current request
     * processing lifecycle phase.</p>
     */
    public PhaseId getPhaseId() {

	return (this.phaseId);

    }


}
