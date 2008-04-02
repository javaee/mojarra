/*
 * $Id: PhaseEvent.java,v 1.4 2004/02/26 20:31:01 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
