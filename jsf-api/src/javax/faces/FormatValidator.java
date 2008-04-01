/*
 * $Id: FormatValidator.java,v 1.2 2002/03/08 00:22:07 jvisvanathan Exp $
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
public class FormatValidator implements Validator {

    private static String TYPE = "FormatValidator";

    // PENDING (visvan) these messages have to be localized. Revisit while
    // integrating Gary's validation proposal.
    public final static String VALUE_NOT_STRING_MESSAGE_KEY = "javax.faces.valueNotStringMessage";
    public final static String INVALID_FORMAT_MESSAGE_KEY = "javax.faces.invalidFormatMessage";

    /**
     * Instantiates a format validator object with a null format mask;
     */
    public FormatValidator() {
    }
   
    /**
     * @return String containing &quot;FormatValidator&quot;
     */ 
    public String getType() {
	return TYPE;
    }

    /**
     * Verifies that the specified String object's format matches the
     * format described in the formatMask property.
     * @param ec EventContext object representing the event-processing 
     *           phase of this request
     * @param value Object containing the value to be validated
     * @throws ValidationException if validation failed
     */
    public void validate(EventContext ec, UIComponent component, 
            Object value) throws ValidationException {
        // PENDING (visvan) not yet implemented. Look at Swing's
        // JFormattedTextField.
	if (value == null || !(value instanceof String)) {
	    throw new ValidationException( VALUE_NOT_STRING_MESSAGE_KEY);
	}
    }
}
