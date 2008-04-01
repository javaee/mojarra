/*
 * $Id: AbstractValidator.java,v 1.1 2002/01/18 21:56:21 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

/**
 * Base class for implementing Validator objects with
 * configurable error messages.
 */
public abstract class AbstractValidator implements Validator {

    /**
     * Subclass must override this method and return an appropriate
     * type String.
     * @return String identifying the type of validator
     */
    public abstract String getType();

    /**
     * Performs validation on the specified value object. 
     * Subclasses must override this method to perform appropriate
     * validation.
     * @param ec EventContext object representing the event-processing 
     *           phase of this request
     * @param value Object containing the value to be validated
     * @return String containing a message describing why validation
     *         failed, or null if validation succeeded
     */
    public abstract String validate(EventContext ec, Object value);

    /**
     * Sets the message which should be associated with the specified
     * error key.
     * @param errorKey String containing the key representing the error message
     * @param errorMessage String containing the error message which should
     *        be displayed for the specified errorKey
     * @throws NullPointerException if errorKey is null
     */
    public void setMessage(String errorKey, String errorMessage) {
    }

    /**
     * @param ec EventContext object representing the event-processing 
     *           phase of this request
     * @param errorKey String containing the key representing the error message
     * @throws NullPointerException if errorKey is null
     * @return String containing the error message which should
     *        be displayed for the specified errorKey
     */
    public String getMessage(EventContext ec, String errorKey) {
	return null; //compile
    }

}
