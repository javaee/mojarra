/*
 * $Id: MethodNotFoundException.java,v 1.4 2005/05/05 20:51:10 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package javax.faces.el;


/**
 * <p>An exception caused by a method name that cannot be resolved
 * against a base object.</p>
 *
 * @deprecated This has been replaced by {@link
 * javax.el.MethodNotFoundException}.
 */

public class MethodNotFoundException extends EvaluationException {


    /**
     * <p>Construct a new exception with no detail message or root cause.</p>
     */
    public MethodNotFoundException() {

        super();

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * no root cause.</p>
     *
     * @param message The detail message for this exception
     */
    public MethodNotFoundException(String message) {

        super(message);

    }


    /**
     * <p>Construct a new exception with the specified root cause.  The detail
     * message will be set to <code>(cause == null ? null :
     * cause.toString()</code>
     *
     * @param cause The root cause for this exception
     */
    public MethodNotFoundException(Throwable cause) {

        super(cause);

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause The root cause for this exception
     */
    public MethodNotFoundException(String message, Throwable cause) {

        super(message, cause);

    }



}
