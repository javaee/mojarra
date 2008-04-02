/*
 * $Id: PropertyNotFoundException.java,v 1.2 2003/03/13 01:12:11 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package javax.faces.el;


/**
 * <p>An exception caused by a property name that cannot be resolved
 * against a base object.</p>
 */

public class PropertyNotFoundException extends EvaluationException {


    /**
     * <p>Construct a new exception with no detail message or root cause.</p>
     */
    public PropertyNotFoundException() {

        super();

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * no root cause.</p>
     *
     * @param message The detail message for this exception
     */
    public PropertyNotFoundException(String message) {

        super(message);

    }


    /**
     * <p>Construct a new exception with the specified root cause.  The detail
     * message will be set to <code>(cause == null ? null :
     * cause.toString()</code>
     *
     * @param cause The root cause for this exception
     */
    public PropertyNotFoundException(Throwable cause) {

        super(cause);

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause The root cause for this exception
     */
    public PropertyNotFoundException(String message, Throwable cause) {

        super(message, cause);

    }



}
