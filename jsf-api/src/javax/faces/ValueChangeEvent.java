/*
 * $Id: ValueChangeEvent.java,v 1.5 2002/01/25 18:35:08 visvan Exp $
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
     * @param sourceId a String containing the id of the component 
     *        where this event originated
     * @param newValue an Object containing the new value of the source component
     * @throws NullPointerException if sourceId or newValue is null
     */
    public ValueChangeEvent(EventContext ec, String sourceId, 
			    Object newValue) {
	super(ec, sourceId);
	this.newValue = newValue;
    }

    /**
     * @return Object containing the new value of the source component
     */
    public Object getNewValue() {
	return newValue;
    }
}
