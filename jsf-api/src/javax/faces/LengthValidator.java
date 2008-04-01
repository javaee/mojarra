/*
 * $Id: LengthValidator.java,v 1.1 2002/01/18 21:56:22 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

/**
 * Class which implements a validator object which will verify
 * that a String object's length (in characters) is within a specified
 * range: minimumChars >= value <= maximumChars
 */
public class LengthValidator extends AbstractValidator {

    private static String TYPE = "LengthValidator";

    public final static String VALUE_NOT_STRING_MESSAGE_KEY = "javax.faces.valueNotStringMessage";
    public final static String INVALID_LENGTH_MESSAGE_KEY = "javax.faces.invalidLengthMessage";

    private int minimumChars = 0;
    private int maximumChars = 0;

    /**
     * Instantiates a length validator object with 
     * minimumChars = 0 and maximumChars = 0;
     */
    public LengthValidator() {
    }

    /**
     * Creates a length validator object with the specified minimumChars
     * and maximumChars properties.
     * @param minimumChars integer defining the minimumChars value
     * @param maximumChars integer defining the maximumChars value
     */
    public LengthValidator(int minimumChars, int maximumChars) {
	this.minimumChars = minimumChars;
	this.maximumChars = maximumChars;
    }

    /**
     * @return String containing &quot;LengthValidator&quot;
     */
    public String getType() {
	return TYPE;
    }

    /**
     * The &quot;minimumChars&quot; property.  Value object lengths
     * (in characters) must be greater or equal to this value to be 
     * considered valid.
     * @see #setMinimumChars
     * @return integer containing the minimumChars value
     */
    public int getMinimumChars() {
	return minimumChars;
    }

    /**
     * Sets the &quot;minimumChars&quot; property.
     * @see #getMinimumChars
     * @param minimumChars integer defining the minimumChars value
     * @throws IllegalParameterException is minimumChars < 0
     */
    public void setMinimumChars(int minimumChars) {
	this.minimumChars = minimumChars;
    }

    /**
     * The &quot;maximumChars&quot; property.  Values object lengths
     * (in characters) must be less-than or equal to this value to be 
     * considered valid.
     * @see #setMaximumChars
     * @return integer containing the maximumChars value
     */
    public int getMaximumChars() {
	return maximumChars;
    }

    /**
     * Sets the &quot;maximumChars&quot; property.
     * @see #getMaximumChars
     * @param maximumChars integer defining the maximumChars value
     */
    public void setMaximumChars(int maximumChars) {
	this.maximumChars = maximumChars;
    }

    /**
     * Verifies that the specified String object's length (in characters) is
     * within the range defined by this object's minimumChars and maximumChars
     * properties.
     * @param ec EventContext object representing the event-processing 
     *           phase of this request
     * @param value Object containing the value to be validated
     * @return String containing a message describing why validation
     *         failed, or null if validation succeeded
     */
    public String validate(EventContext ec, Object value) {
	if (value == null || !(value instanceof String)) {
	    return getMessage(ec, VALUE_NOT_STRING_MESSAGE_KEY);
	}
        int length = ((String)value).length();
	if (length < minimumChars || length > maximumChars) {
	    return getMessage(ec, INVALID_LENGTH_MESSAGE_KEY);
	}
	return null;
    }
}
