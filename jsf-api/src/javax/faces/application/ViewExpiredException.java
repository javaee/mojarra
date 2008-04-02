/*
 * $Id: ViewExpiredException.java,v 1.1 2005/07/20 00:32:13 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package javax.faces.application;

import javax.faces.FacesException;

/**
 * <p>Implementations must throw this {@link FacesException} when
 * attempting to restore the view {@link StateManager#restoreView(javax.faces.context.FacesContext, String, String)} 
 * results in failure on postback.</p>
 */

public class ViewExpiredException extends FacesException {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new exception with no detail message or root cause.</p>
     */
    public ViewExpiredException() {

        super();

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * no root cause.</p>
     *
     * @param message The detail message for this exception
     */
    public ViewExpiredException(String message) {

        super(message);

    }


    /**
     * <p>Construct a new exception with the specified root cause.  The detail
     * message will be set to <code>(cause == null ? null :
     * cause.toString()</code>
     *
     * @param cause The root cause for this exception
     */
    public ViewExpiredException(Throwable cause) {

        super(cause);

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause The root cause for this exception
     */
    public ViewExpiredException(String message, Throwable cause) {

        super(message, cause);

    }
}
