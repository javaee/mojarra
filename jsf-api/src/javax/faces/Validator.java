/*
 * $Id: Validator.java,v 1.1 2002/01/18 21:56:22 edburns Exp $
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
     * @return String containing a message describing why validation
     *         failed, or null if validation succeeded
     */
    String validate(EventContext ec, Object value);

}
