/*
 * $Id: FacesException.java,v 1.11 2003/02/20 22:46:08 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package javax.faces;


/**
 * <p>This class encapsulates general JavaServer Faces exceptions.</p>
 */

public class FacesException extends RuntimeException {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new exception with no detail message or root cause.</p>
     */
    public FacesException() {

        super();

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * no root cause.</p>
     *
     * @param message The detail message for this exception
     */
    public FacesException(String message) {

        super(message);

    }


    /**
     * <p>Construct a new exception with the specified root cause.  The detail
     * message will be set to <code>(cause == null ? null :
     * cause.toString()</code>
     *
     * @param cause The root cause for this exception
     */
    public FacesException(Throwable cause) {

        super(cause == null ? (String) null : cause.toString());
        this.cause = cause;

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause The root cause for this exception
     */
    public FacesException(String message, Throwable cause) {

        super(message);
        this.cause = cause;

    }



    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The underlying exception that caused this exception.</p>
     */
    private Throwable cause = null;


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Return the cause of this exception, or <code>null</code> if the
     * cause is nonexistent or unknown.</p>
     */
    public Throwable getCause() {

        return (this.cause);

    }


}
