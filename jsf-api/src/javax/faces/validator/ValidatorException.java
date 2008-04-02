/*
 * $Id: ValidatorException.java,v 1.1 2003/12/22 19:29:27 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.validator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;


/**
 * <p>A <strong>ValidatorException</strong> is an exception
 * thrown by the <code>validate()</code> method of a 
 * {@link Validator} to indicate that validation failed.
 */
public class ValidatorException extends FacesException {
    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new exception with the specified message and
     * no root cause.</p>
     *
     * @param message The message for this exception
     */
    public ValidatorException(FacesMessage message) {

        super(message.getSummary());
        this.message = message;
    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause The root cause for this exception
     */
    public ValidatorException(FacesMessage message, Throwable cause) {

        super(message.getSummary(), cause);
        this.message = message;
    }

    /**
     * Returns the FacesMessage associated with the exception.
     */
    public FacesMessage getFacesMessage() {
        return this.message;
    }

    private FacesMessage message;
}
