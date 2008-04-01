/*
 * $Id: ValueChangeEvent.java,v 1.6 2002/03/07 23:44:04 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

/**
 * The class which encapsulates information associated
 * with a value-change event.  Value-change events are
 * generated when the value associated with a user-interface 
 * component is changed.
 * 
 * @see ValueChangeListener
 */
public class ValueChangeEvent extends FacesEvent {

    private Object newValue;

    /**
     * Creates a value-change event.
     * @param ec EventContext object representing the event-processing 
     *           phase of the request where this event originated
     * @param sourceComponent the component where this event originated
     * @param newValue an Object containing the new value of the source component
     * @throws NullPointerException if sourceComponent or newValue is null
     */
    public ValueChangeEvent(EventContext ec, UIComponent sourceComponent, 
			    Object newValue) {
	super(ec, sourceComponent);
	this.newValue = newValue;
    }

    /**
     * @return Object containing the new value of the source component
     */
    public Object getNewValue() {
	return newValue;
    }
}
