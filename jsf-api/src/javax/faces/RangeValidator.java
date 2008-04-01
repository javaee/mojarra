/*
 * $Id: RangeValidator.java,v 1.1 2002/01/18 21:56:22 edburns Exp $
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

    public final static String NON_NUMERIC_MESSAGE_KEY = "javax.faces.nonNumericMessage";
    public final static String OUT_OF_RANGE_MESSAGE_KEY = "javax.faces.outOfRangeMessage";

    private int minimum = 0;
    private int maximum = 0;

    /**
     * Instantiates a range validator object with 
     * minimum = 0 and maximum = 0;
     */
    public RangeValidator() {
    }

    /**
     * Creates a range validator object with the specified minimum
     * and maximum properties.
     * @param minimum integer defining the minimum value
     * @param maximum integer defining the maximum value
     */
    public RangeValidator(int minimum, int maximum) {
	this.minimum = minimum;
	this.maximum = maximum;
    }

    /**
     * @return String containing &quot;RangeValidator&quot;
     */
    public String getType() {
	return TYPE;
    }

    /**
     * The &quot;minimum&quot; property.  Values must be
     * greater or equal to this value to be considered valid.
     * @see #setMinimum
     * @return integer containing the minimum value
     */
    public int getMinimum() {
	return minimum;
    }

    /**
     * Sets the &quot;minimum&quot; property.
     * @see #getMinimum
     * @param minimum integer defining the minimum value
     */
    public void setMinimum(int minimum) {
	this.minimum = minimum;
    }

    /**
     * The &quot;maximum&quot; property.  Values must be
     * less-than or equal to this value to be considered valid.
     * @see #setMaximum
     * @return integer containing the maximum value
     */
    public int getMaximum() {
	return maximum;
    }

    /**
     * Sets the &quot;maximum&quot; property.
     * @see #getMaximum
     * @param maximum integer defining the maximum value
     */
    public void setMaximum(int maximum) {
	this.maximum = maximum;
    }

    /**
     * Verifies that the specified value object is numeric and
     * within the range defined by this object's minimum and maximum
     * properties.
     * @param ec EventContext object representing the event-processing 
     *           phase of this request
     * @param value Object containing the value to be validated
     * @return String containing a message describing why validation
     *         failed, or null if validation succeeded
     */
    public String validate(EventContext ec, Object value) {
	String result = super.validate(ec, value); // RequiredValidator:validate()
	if (result != null) {
	    return result; // null value
	} else { // non null value
	    int intValue = -1;
	    if (value instanceof Integer) {
		intValue = ((Integer)value).intValue();
	    } else if (value instanceof String) {
	        try {
		    intValue = Integer.parseInt((String)value);
		} catch (NumberFormatException e) {
		    return getMessage(ec, NON_NUMERIC_MESSAGE_KEY);
		}
	    } else {
		return getMessage(ec, NON_NUMERIC_MESSAGE_KEY);
	    }
	    if (intValue < minimum || intValue > maximum) {
		return getMessage(ec, OUT_OF_RANGE_MESSAGE_KEY);
	    }
	}
	return null;
    }
}
