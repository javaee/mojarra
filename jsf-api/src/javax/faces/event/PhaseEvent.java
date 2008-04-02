/*
 * $Id: PhaseEvent.java,v 1.1 2003/06/21 03:23:41 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import java.util.EventObject;
import javax.faces.context.FacesContext;


/**
 * <p><strong>PhaseEvent</strong> represents the beginning or ending of
 * processing for a particular phase of the request processing lifecycle,
 * for the request encapsulated by the specified {@link FacesContext}.</p>
 */

public class PhaseEvent extends EventObject {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new event object from the specified parameters.
     * The specified {@link FacesContext} will be the source of this event.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param phaseId Identifier of the current request processing
     *  lifecycle phase
     *
     * @exception NullPointerException if <code>context</code> or
     *  <code>phaseId</code> is <code>null</code>
     */
    public PhaseEvent(FacesContext context, PhaseId phaseId) {

        super(context);
	this.phaseId = phaseId;

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the {@link FacesContext} for the request being processed.</p>
     */
    public FacesContext getFacesContext() {

        return ((FacesContext) getSource());

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
