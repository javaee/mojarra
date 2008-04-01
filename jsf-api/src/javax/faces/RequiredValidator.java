/*
 * $Id: RequiredValidator.java,v 1.3 2002/03/15 20:49:22 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

/**
 * Class which implements a validator object which will verify
 * that a value object is non-null.
 */
public class RequiredValidator implements Validator {

    private static String TYPE = "RequiredValidator";

    public RequiredValidator() {
    }

    /**
     * @return String containing &quot;RequiredValidator&quot;
     */
    public String getType() {
	return TYPE;
    }

    /**
     * Verifies that the specified value object is non-null.
     * If the value object is a String, an empty string (&quot;&quot;)
     * will be treated as a null value and return an error message. 
     * @param ec EventContext object representing the event-processing 
     *           phase of this request
     * @param value Object containing the value to be validated
     * @throws ValidationException if validation failed
     */
    public void validate(EventContext ec, UIComponent component, Object value) 
            throws ValidationException {

	if (value == null || (value instanceof String && value.equals(""))) {
            MessageList msgList = ec.getMessageList();
            msgList.addMessage("MSG0003", component.getId(), value);
	    throw new ValidationException("");
        } 
        return;
    }

}
