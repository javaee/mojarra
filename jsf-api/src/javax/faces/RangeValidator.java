/*
 * $Id: RangeValidator.java,v 1.2 2002/03/08 00:22:08 jvisvanathan Exp $
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
public class RangeValidator extends RequiredValidator {

    private static String TYPE = "RangeValidator";

    // PENDING ( visvan ) these messages have to be localized. Revisit while
    // integrating Gary's validation proposal.
    public final static String NON_NUMERIC_MESSAGE_KEY = "Invalid Type";
    public final static String OUT_OF_RANGE_MESSAGE_KEY = "Value out of range";

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

        // PENDING (visvan) RangeValidator need not invoke requiredValidator
        // because if the value is null, it will be caught during conversion.
        // so RangeValidator need not extend from RequiredValidator. Check before
        // changing the API.
        /*try {         
	    super.validate(ec, component, value); // RequiredValidator:validate()
        } catch (ValidationException ve ) {
            throw ve;
        } */ 
   
        int intValue = -1;
	if (value instanceof Integer) {
            intValue = ((Integer)value).intValue();
	} else if (value instanceof String) {
	    try {
                intValue = Integer.parseInt((String)value);
            } catch (NumberFormatException e) {
                throw new ValidationException(NON_NUMERIC_MESSAGE_KEY);
	     }
	} else {
            throw new ValidationException(NON_NUMERIC_MESSAGE_KEY);
	}

        Integer minValue = (Integer) component.getAttribute(null, "rangeMinimum");
        Integer maxValue =  (Integer) component.getAttribute(null, "rangeMaximum");

        if ( minValue == null || maxValue == null ) {
            throw new ValidationException(OUT_OF_RANGE_MESSAGE_KEY);
        }    
        int minimum = ((Integer)minValue).intValue();
        int maximum = ((Integer)maxValue).intValue();
	if (intValue < minimum || intValue > maximum) {
            throw new ValidationException(OUT_OF_RANGE_MESSAGE_KEY);
	}
    }
}
