/*
 * $Id: CommandFailedException.java,v 1.2 2001/12/20 22:25:44 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

public class CommandFailedException extends FacesException {
   /**
    * Constructs a CommandFailedException with a detailed message.
    *
    * @param message
    *   Detailed message for this exception.
    */
    public CommandFailedException(String message) {
        super(message);
    }
}
