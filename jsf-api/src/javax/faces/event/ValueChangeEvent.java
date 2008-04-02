/*
 * $Id: ValueChangeEvent.java,v 1.5 2004/02/04 23:38:17 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import javax.faces.component.UIComponent;


/**
 * <p>A {@link ValueChangeEvent} is a notification that the local value of
 * the source component has been change as a result of user interface
 * activity.  It is not fired unless validation of the new value was
 * completed successfully.</p>
 */

public class ValueChangeEvent extends FacesEvent {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new event object from the specified source component,
     * old value, and new value.</p>
     *
     * <p>The default {@link PhaseId} for this event is {@link
     * PhaseId#ANY_PHASE}.</p>
     *
     * @param component Source {@link UIComponent} for this event
     * @param oldValue The previous local value of this {@link UIComponent}
     * @param newValue The new local value of thie {@link UIComponent}
     *
     * @exception IllegalArgumentException if <code>component</code> is
     *  <code>null</code>
     */
    public ValueChangeEvent(UIComponent component,
                             Object oldValue, Object newValue) {

        super(component);
        this.oldValue = oldValue;
        this.newValue = newValue;
    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>The previous local value of the source {@link UIComponent}.</p>
     */
    private Object oldValue = null;


    /**
     * <p>Return the previous local value of the source {@link UIComponent}.
     * </p>
     */
    public Object getOldValue() {

        return (this.oldValue);

    }


    /**
     * <p>The current local value of the source {@link UIComponent}.</p>
     */
    private Object newValue = null;


    /**
     * <p>Return the current local value of the source {@link UIComponent}.
     * </p>
     */
    public Object getNewValue() {

        return (this.newValue);

    }


    // ------------------------------------------------- Event Broadcast Methods


    public boolean isAppropriateListener(FacesListener listener) {

        return (listener instanceof ValueChangeListener);

    }

    /**
     * @exception AbortProcessingException {@inheritDoc}
     */ 
    public void processListener(FacesListener listener) {

        ((ValueChangeListener) listener).processValueChange(this);

    }


}
