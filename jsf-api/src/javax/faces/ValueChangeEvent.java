/*
 * $Id: ValueChangeEvent.java,v 1.7 2002/04/05 19:40:20 jvisvanathan Exp $
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
     * @param fc FacesContext object representing the event-processing 
     *           phase of the request where this event originated
     * @param sourceComponent the component where this event originated
     * @param newValue an Object containing the new value of the source component
     * @throws NullPointerException if sourceComponent or newValue is null
     */
    public ValueChangeEvent(FacesContext fc, UIComponent sourceComponent, 
			    Object newValue) {
	super(fc, sourceComponent);
	this.newValue = newValue;
    }

    /**
     * @return Object containing the new value of the source component
     */
    public Object getNewValue() {
	return newValue;
    }
}
