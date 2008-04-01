/*
 * $Id: LengthValidator.java,v 1.3 2002/03/15 20:49:21 jvisvanathan Exp $
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
