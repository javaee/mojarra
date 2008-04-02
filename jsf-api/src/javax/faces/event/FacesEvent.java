/*
 * $Id: FacesEvent.java,v 1.8 2003/12/17 15:10:53 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;


/**
 * <p><strong>FacesEvent</strong> is the base class for user interface and
 * application events that can be fired by {@link UIComponent}s.  Concrete
 * event classes must subclass {@link FacesEvent} in order to be supported
 * by the request processing lifecycle.</p>
 */

public abstract class FacesEvent extends EventObject {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new event object from the specified source component.</p>
     *
     * @param component Source {@link UIComponent} for this event
     *
     * @exception IllegalArgumentException if <code>component</code> is
     *  <code>null</code>
     */
    public FacesEvent(UIComponent component) {

        super(component);

    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the source {@link UIComponent} that sent this event.
     */
    public UIComponent getComponent() {

        return ((UIComponent) getSource());

    }

    private PhaseId phaseId = PhaseId.ANY_PHASE;

    /**
     * <p>Return the identifier of the request processing phase during
     * which this event should be delivered.  Legal values are the
     * singleton instances defined by the {@link PhaseId} class,
     * including <code>PhaseId.ANY_PHASE</code>, which is the default
     * value.</p>
     */
    public PhaseId getPhaseId() {
	return phaseId;
    }
   
    /**
     * <p>Set the {@link PhaseId} during which this event will be
     * delivered.</p>
     *
     * @exception IllegalArgumentException phaseId is null.
     *
     */ 

    public void setPhaseId(PhaseId phaseId) {
	if (null == phaseId) {
	    throw new IllegalArgumentException();
	}
	this.phaseId = phaseId;
    }


    // ------------------------------------------------- Event Broadcast Methods


    /**
     * <p>Convenience method to queue this event for broadcast at the end
     * of the current request processing lifecycle phase.</p>
     *
     * @exception IllegalStateException if the source component for this
     *  event is not a descendant of a {@link UIViewRoot}
     */
    public void queue() {

        getComponent().queueEvent(this);

    }


    /**
     * <p>Return <code>true</code> if this {@link FacesListener} is an instance
     * of a listener class that this event supports.  Typically, this will be
     * accomplished by an "instanceof" check on the listener class.</p>
     *
     * @param listener {@link FacesListener} to evaluate
     */
    public abstract boolean isAppropriateListener(FacesListener listener);


    /**
     * <p>Broadcast this {@link FacesEvent} to the specified
     * {@link FacesListener}, by whatever mechanism is appropriate.  Typically,
     * this will be accomplished by calling an event processing method, and
     * passing this {@link FacesEvent} as a paramter.</p>
     *
     * @param listener {@link FacesListener} to send this {@link FacesEvent} to
     *
     * @exception AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     */
    public abstract void processListener(FacesListener listener);


}
