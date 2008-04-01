/*
 * $Id: FormatValidator.java,v 1.3 2002/03/15 20:49:21 jvisvanathan Exp $
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
        MessageList msgList = ec.getMessageList();

	if (value == null || !(value instanceof String)) {
            msgList.addMessage("MSG0007", component.getId(), value);
	    throw new ValidationException("");
	}
    }
}
