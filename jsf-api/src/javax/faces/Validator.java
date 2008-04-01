/*
 * $Id: Validator.java,v 1.2 2002/03/08 00:22:09 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

/**
 * Interface for implementing objects which can perform
 * validation on value objects and return an appropriate
 * error message if validation fails.
 */
public interface Validator {

    /**
     * @return String identifying the type of validator
     */
    String getType();

    /**
     * @param ec EventContext object representing the event-processing 
     *           phase of this request
     * @param value Object containing the value to be validated
     * @throws ValidationException if validation failed
     */
    void validate(EventContext ec, UIComponent component, Object value) throws 
            ValidationException;

}
