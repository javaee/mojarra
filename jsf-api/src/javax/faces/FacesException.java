/*
 * $Id: FacesException.java,v 1.7 2002/03/16 00:09:02 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

/**
 * The class which encapsulates general JavaServer Faces exceptions.
 */
public class FacesException extends RuntimeException {

    private String xcptMessage;
    private Throwable rootCause;

    /**
    * Constructs a new Faces exception with the specified message.  The
    * message can be written to the server log and/or displayed to the user.
    *
    * @param message 
    *   String containing the detailed message for this exception.
    */
    public FacesException(String message) {
        super(message);
        xcptMessage = message;
    }

    /**
     * Constructs a new Faces exception with the specified message
     * and root cause.  This constructor should be used when Faces 
     * needs to throw an exception and include a message about the 
     * quot;root causequot; exception that interfered with its 
     * normal operation.
     *
     * @param message 
     *   String containing the detailed message for this exception.
     * @param rootCause
     *   the Throwable exception that interfered with its normal operation,
     *   making this Faces exception necessary
     *
     */
    public FacesException(String message, Throwable rootCause) {
        super(message);
	xcptMessage = message;
	this.rootCause = rootCause;
    }

    /**
     * Constructs a new Faces exception with the specified rootCause.
     * This constructor should be used when Faces needs to throw an 
     * exception and include a message about the &quot;root cause&quot; 
     * exception that interfered with its normal operation. 
     * The exception's message is based on the localized message 
     * of the underlying exception. 
     * <p>
     * This method calls the getLocalizedMessage method on the 
     * Throwable exception to get a localized exception message. When 
     * subclassing FacesException, this method can be overridden to 
     * create an exception message designed for a specific locale.
     * @param rootCause
     *   the Throwable exception that interfered with its normal operation,
     *   making this Faces exception necessary
     *
     */
    public FacesException(Throwable rootCause) {
	this.rootCause = rootCause;
    }

    /**
    * Returns the message of this exception
    * @return the message string.
    */
    public String getMessage() {
        return xcptMessage;
    }
}
