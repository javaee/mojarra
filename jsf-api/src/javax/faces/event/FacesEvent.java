/*
 * $Id: FacesEvent.java,v 1.6 2003/07/27 00:48:27 craigmcc Exp $
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


    // ------------------------------------------------- Event Broadcast Methods


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
