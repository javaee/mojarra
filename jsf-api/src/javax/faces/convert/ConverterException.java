/*
 * $Id: ConverterException.java,v 1.7 2004/02/04 23:38:04 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.convert;


import javax.faces.FacesException;
import javax.faces.application.FacesMessage;


/**
 * <p><strong>ConverterException</strong> is an exception thrown by the
 * <code>getAsObject()</code> or <code>getAsText()</code> method of a
 * {@link Converter}, to indicate that the requested conversion cannot
 * be performed.</p>
 */

public class ConverterException extends FacesException {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new exception with no detail message or root cause.</p>
     */
    public ConverterException() {

        super();

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * no root cause.</p>
     *
     * @param message The detail message for this exception
     */
    public ConverterException(String message) {

        super(message);

    }


    /**
     * <p>Construct a new exception with the specified root cause.  The detail
     * message will be set to <code>(cause == null ? null :
     * cause.toString()</code>
     *
     * @param cause The root cause for this exception
     */
    public ConverterException(Throwable cause) {

        super(cause);

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause The root cause for this exception
     */
    public ConverterException(String message, Throwable cause) {

        super(message, cause);

    }




    /**
     * <p>Construct a new exception with the specified detail message and
     * no root cause.</p>
     *
     * @param message The detail message for this exception
     */
    public ConverterException(FacesMessage message) {

        super(message.getSummary());
        this.facesMessage = message;
    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause The root cause for this exception
     */
    public ConverterException(FacesMessage message, Throwable cause) {

        super(message.getSummary(), cause);
        this.facesMessage = message;

    }

    /**
     * <p>Returns the FacesMessage associated with this exception;  this
     * will only be available if one of the Faces
     */
    public FacesMessage getFacesMessage() {
        return facesMessage;
    }

    private FacesMessage facesMessage;
}
