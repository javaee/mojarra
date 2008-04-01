/*
 * $Id: FacesException.java,v 1.6 2002/01/18 21:56:21 edburns Exp $
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

    protected String xcptMessage;

    /**
    * Constructs a FacesException with a detailed message.
    *
    * @param message
    *   Detailed message for this exception.
    */
    public FacesException(String message) {
        super(message);
        xcptMessage = message;
    }

    /**
    * Returns the message of this exception
    * @return the message string.
    */
    public String getMessage() {
        return xcptMessage;
    }
}
