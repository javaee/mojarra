/*
 * $Id: LengthValidator.java,v 1.4 2002/03/16 00:09:02 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Iterator;


/**
 * Class which implements a validator object which will verify
 * that a String object's length (in characters) is within a specified
 * range: minimumChars >= value <= maximumChars
 */
public class LengthValidator implements Validator {

    private static String TYPE = "LengthValidator";

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
     * Returns an iterator containing the names of this validator's
     * supported attributes. Validator attributes are set on a UI
     * component to control how the validator performs validation.
     * <p>
     * This validator supports the following attributes:
     * <ul>
     * <li>&quot;lengthMinimum&quot;
     * <li>&quot;lengthMaximum&quot;
     * </ul>
     * @return an iterator containing the Strings representing supported
     *          attribute names
     */
    public Iterator getSupportedAttributeNames() {
	return null; //compile
    }

    /**
     * Verifies that the specified String value's length (in characters) is
     * within the range defined by the specified component's
     * &quot;lengthMinimum&quot; and &quot;lengthMaximum&quot;
     * attributes.
     * @param ec EventContext object representing the event-processing 
     *           phase of this request
     * @param value Object containing the value to be validated
     * @throws ValidationException if validation failed
     * @return String containing a message describing why validation
     *         failed, or null if validation succeeded
     */
    public void validate(EventContext ec, UIComponent component, Object value) 
            throws ValidationException {
    
        MessageList msgList = ec.getMessageList();
        String componentId = component.getId();
 
	if (value == null || !(value instanceof String)) {
            msgList.addMessage("MSG0007", componentId, value);
	    throw new ValidationException("");
	}
        Object minChars = component.getAttribute(null, "lengthMinimum");
        Object maxChars = component.getAttribute(null, "lengthMaximum");
    
        if ( minChars != null &&  minChars instanceof String) {
            minChars = new Integer((String)minChars);
        } else if ( minChars == null ) {
            minChars = new Integer(0);
        } 

        if ( maxChars != null &&  maxChars instanceof String) {
            maxChars = new Integer((String)maxChars);
        } else if ( maxChars == null ) {
            // if maxchars is not specified, there is no point in doing
            // validation
            return;
        }

        int minimumChars = ((Integer)minChars).intValue();
        int maximumChars = ((Integer)maxChars).intValue();
        int length = ((String)value).length();
        if (length < minimumChars || length > maximumChars) {
            msgList.addMessage("MSG0005", componentId, value, minChars.toString(),
                    maxChars.toString());
            throw new ValidationException("");
	}
	
    }
}
