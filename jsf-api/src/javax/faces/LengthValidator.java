/*
 * $Id: LengthValidator.java,v 1.2 2002/03/08 00:22:08 jvisvanathan Exp $
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
public class LengthValidator implements Validator {

    private static String TYPE = "LengthValidator";

    // PENDING (visvan) these messages have to be localized. Revisit while
    // integrating Gary's validation proposal.
    public final static String VALUE_NOT_STRING_MESSAGE_KEY = "Invalid Type";
    public final static String INVALID_LENGTH_MESSAGE_KEY = "Length not within the range";

    /**
     * Instantiates a length validator object.
     */
    public LengthValidator() {
    }

    /**
     * @return String containing &quot;LengthValidator&quot;
     */
    public String getType() {
        return TYPE;
    }

    /**
     * Verifies that the specified String object's length (in characters) is
     * within the range defined by this object's minimumChars and maximumChars
     * properties.
     * @param ec EventContext object representing the event-processing 
     *           phase of this request
     * @param value Object containing the value to be validated
     * @throws ValidationException if validation failed
     */
    public void validate(EventContext ec, UIComponent component, Object value) 
            throws ValidationException {
        
	if (value == null || !(value instanceof String)) {
	    throw new ValidationException(VALUE_NOT_STRING_MESSAGE_KEY);
	}
        Integer minChars = (Integer) component.getAttribute(null, "lengthMinimum");
        Integer maxChars =  (Integer) component.getAttribute(null, "lengthMaximum");
        if ( minChars == null || maxChars == null ) {
            throw new ValidationException(INVALID_LENGTH_MESSAGE_KEY);
        }
        int minimumChars = ((Integer)minChars).intValue();
        int maximumChars = ((Integer)maxChars).intValue();
        int length = ((String)value).length();
        if (length < minimumChars || length > maximumChars) {
            throw new ValidationException(INVALID_LENGTH_MESSAGE_KEY );
	}
	
    }
}
