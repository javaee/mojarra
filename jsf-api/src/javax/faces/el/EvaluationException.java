/*
 * $Id: EvaluationException.java,v 1.2 2003/03/13 01:12:10 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package javax.faces.el;


import javax.faces.FacesException;


/**
 * <p>An exception caused by invalid syntax of an expression or reference.</p>
 */

public class EvaluationException extends FacesException {


    /**
     * <p>Construct a new exception with no detail message or root cause.</p>
     */
    public EvaluationException() {

        super();

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * no root cause.</p>
     *
     * @param message The detail message for this exception
     */
    public EvaluationException(String message) {

        super(message);

    }


    /**
     * <p>Construct a new exception with the specified root cause.  The detail
     * message will be set to <code>(cause == null ? null :
     * cause.toString()</code>
     *
     * @param cause The root cause for this exception
     */
    public EvaluationException(Throwable cause) {

        super(cause);

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause The root cause for this exception
     */
    public EvaluationException(String message, Throwable cause) {

        super(message, cause);

    }



}
