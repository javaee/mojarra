/*
 * $Id: ValidationException.java,v 1.1 2002/03/08 00:22:09 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

public class ValidationException extends FacesException {
   /**
    * Constructs a ValidationException with a detailed message.
    *
    * @param message
    *   Detailed message for this exception.
    */
    public ValidationException(String message) {
        super(message);
    }
}
