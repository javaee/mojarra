/*
 * $Id: AbortProcessingException.java,v 1.4 2004/02/04 23:38:15 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package javax.faces.event;


import javax.faces.FacesException;


/**
 * <p>An exception that may be thrown by event listeners to terminate the
 * processing of the current event.</p>
 */

public class AbortProcessingException extends FacesException {

    /**
     * <p>Construct a new exception with no detail message or root cause.</p>
     */
    public AbortProcessingException() {
        super();
    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * no root cause.</p>
     *
     * @param message The detail message for this exception
     */
    public AbortProcessingException(String message) {
        super(message);
    }


    /**
     * <p>Construct a new exception with the specified root cause.</p>
     *
     * @param cause The root cause for this exception
     */
    public AbortProcessingException(Throwable cause) {
        super(cause);
    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause The root cause for this exception
     */
    public AbortProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

}
