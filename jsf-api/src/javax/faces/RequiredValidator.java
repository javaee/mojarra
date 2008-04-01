/*
 * $Id: RequiredValidator.java,v 1.1 2002/01/18 21:56:22 edburns Exp $
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
public class RequiredValidator extends AbstractValidator {

    private static String TYPE = "RequiredValidator";

    public final static String NULL_VALUE_MESSAGE_KEY = "javax.faces.nullValueMessage";

    /**
     * Subclass must override this method and return an appropriate
     * type String.
     * @return String identifying the type of validator
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
     * @return String containing a message describing why validation
     *         failed, or null if validation succeeded
     */
    public String validate(EventContext ec, Object value) {
	if (value != null && 
	    (value instanceof String && !value.equals(""))) {
	    return null;
	}
	return getMessage(ec, NULL_VALUE_MESSAGE_KEY);
    }

}
