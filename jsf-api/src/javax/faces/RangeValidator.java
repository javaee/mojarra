/*
 * $Id: RangeValidator.java,v 1.3 2002/03/15 20:49:22 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

/**
 * Class which implements a validator object which will verify
 * that a value object's value is a numeric value within a specified
 * range: minimum >= value <= maximum.
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
     * Verifies that the specified value object is numeric and
     * within the range defined by this object's minimum and maximum
     * properties.
     * @param ec EventContext object representing the event-processing 
     *           phase of this request
     * @param value Object containing the value to be validated
     * @throws ValidationException if validation failed
     */
    public void validate(EventContext ec, UIComponent component, Object value) 
            throws ValidationException {

        MessageList msgList = ec.getMessageList();
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
