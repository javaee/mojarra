/*
 * $Id: RangeValidator.java,v 1.5 2002/04/05 19:40:17 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Iterator;

/**
 * Class which implements a validator object which will verify
 * that a value object's value is a numeric value within a specified
 * range: rangeMinimum >= value <= rangeMaximum.
 */
public class RangeValidator implements Validator {

    private static String TYPE = "RangeValidator";

    public RangeValidator() {
    }

    /**
     * @return String containing &quot;RangeValidator&quot;
     */
    public String getType() {
	return TYPE;
    }

    /**
     * Returns an iterator containing the names of this validator's
     * supported attributes. Validator attributes are set on a UI
     * component to control how the validator performs validation.
     * <p>
     * This validator supports the following attributes:
     * <ul>
     * <li>&quot;rangeMinimum&quot;
     * <li>&quot;rangeMaximum&quot;
     * </ul>
     * @return an iterator containing the Strings representing supported
     *          attribute names
     */
    public Iterator getSupportedAttributeNames() {
	return null; //compile
    }

    /**
     * Verifies that the specified numeric value object is
     * within the range defined by the specified component's
     * &quot;rangeMinimum&quot; and &quot;rangeMaximum&quot;
     * attributes.
     * @param fc FacesContext object representing the event-processing 
     *           phase of this request
     * @param value Object containing the value to be validated
     * @throws ValidationException if validation failed
     * @return String containing a message describing why validation
     *         failed, or null if validation succeeded
     */
    public void validate(FacesContext fc, UIComponent component, Object value) 
            throws ValidationException {

        MessageList msgList = fc.getMessageList();
        String componentId= component.getId();
    
        int intValue = -1;
	if (value instanceof Integer) {
            intValue = ((Integer)value).intValue();
	} else if (value instanceof String) {
	    try {
                intValue = Integer.parseInt((String)value);
            } catch (NumberFormatException e) {
                msgList.addMessage("MSG0001", componentId, value);
                throw new ValidationException("");
	     }
	} else {
            msgList.addMessage("MSG0001", componentId, value);
            throw new ValidationException("");
	}

        Object minValue = component.getAttribute(null, "rangeMinimum");
        Object  maxValue = component.getAttribute(null, "rangeMaximum");


        if ( minValue != null &&  minValue instanceof String) {
            minValue = new Integer((String)minValue);
        } else if ( minValue == null ) {
            minValue = new Integer(0);
        }

        if ( maxValue != null &&  maxValue instanceof String) {
            maxValue = new Integer((String)maxValue);
        } else if ( maxValue == null ) {
            // if maxValue is not specified, there is no point in doing
            // validation
            return;
        }
        int minimum = ((Integer)minValue).intValue();
        int maximum = ((Integer)maxValue).intValue();
	if (intValue < minimum || intValue > maximum) {
            msgList.addMessage("MSG0004", componentId, value, 
                minValue.toString(), maxValue.toString());
            throw new ValidationException("");
	}
    }

}
