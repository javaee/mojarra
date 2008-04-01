/*
 * $Id: ValidationException.java,v 1.2 2002/03/16 00:09:04 eburns Exp $
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
    *   String containing the detailed message for this exception.
    */
    public ValidationException(String message) {
        super(message);
    }
}
