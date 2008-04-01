/*
 * $Id: FormatValidator.java,v 1.4 2002/03/16 00:09:02 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Iterator;

/**
 * Class which implements a validator object which will verify
 * that a String object's format matches a format mask.
 * <p>
 * The format mask is specified as follows:
 * TBD.
 * 
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
     * Returns an iterator containing the names of this validator's
     * supported attributes. Validator attributes are set on a UI
     * component to control how the validator performs validation.
     * <p>
     * This validator supports the following attributes:
     * <ul>
     * <li>&quot;formatMask&quot;
     * </ul>
     * @return an iterator containing the Strings representing supported
     *          attribute names
     */
    public Iterator getSupportedAttributeNames() {
	return null; //compile
    }

    /**
     * Verifies that the specified String value's format matches the
     * format described in the &quot;formatMask&quot; attribute on
     * the specified component.
     * @param ec EventContext object representing the event-processing 
     *           phase of this request
     * @param value Object containing the value to be validated
     * @throws ValidationException if validation failed
     * @return String containing a message describing why validation
     *         failed, or null if validation succeeded
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
