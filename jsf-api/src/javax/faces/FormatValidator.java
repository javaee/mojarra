/*
 * $Id: FormatValidator.java,v 1.1 2002/01/18 21:56:21 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

/**
 * Class which implements a validator object which will verify
 * that a String object's format matches a format mask.
 * <p>
 * The format mask is specified as follows:
 * TBD.
 */
public class FormatValidator extends AbstractValidator {

    private static String TYPE = "FormatValidator";

    public final static String VALUE_NOT_STRING_MESSAGE_KEY = "javax.faces.valueNotStringMessage";
    public final static String INVALID_FORMAT_MESSAGE_KEY = "javax.faces.invalidFormatMessage";

    private String formatMask;

    /**
     * Instantiates a format validator object with a null format mask;
     */
    public FormatValidator() {
    }

    /**
     * Creates a format validator object with the specified formatMask
     * property.
     * @param formatMask String containing the mask used to verify a value's format
     */
    public FormatValidator(String formatMask) {
	this.formatMask = formatMask;
    }

    /**
     * @return String containing &quot;FormatValidator&quot;
     */
    public String getType() {
	return TYPE;
    }

    /**
     * The &quot;formatMask&quot; property.  Value object Strings must
     * match this format to be considered valid.
     * @see #setFormatMask
     * @return String containing the mask used to verify a value's format
     */
    public String getFormatMask() {
	return formatMask;
    }

    /**
     * Sets the &quot;formatMask&quot; property.
     * @see #getFormatMask
     * @param formatMask String containing the mask used to verify a value's format
     * @throws NullPointerException if formatMask is null
     */
    public void setFormatMask(String formatMask) {
	this.formatMask = formatMask;
    }

    /**
     * Verifies that the specified String object's format matches the
     * format described in the formatMask property.
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
	return null; //compile
    }
}
