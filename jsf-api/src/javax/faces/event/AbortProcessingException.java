/*
 * $Id: AbortProcessingException.java,v 1.6 2005/08/22 22:08:05 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */


package javax.faces.event;


import javax.faces.FacesException;


/**
 * <p>An exception that may be thrown by event listeners to terminate the
 * processing of the current event.</p>
 */

public class AbortProcessingException extends FacesException {

    /**
     * <p>Construct a new exception with no detail message or root cause.</p>
     */
    public AbortProcessingException() {
        super();
    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * no root cause.</p>
     *
     * @param message The detail message for this exception
     */
    public AbortProcessingException(String message) {
        super(message);
    }


    /**
     * <p>Construct a new exception with the specified root cause.</p>
     *
     * @param cause The root cause for this exception
     */
    public AbortProcessingException(Throwable cause) {
        super(cause);
    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause The root cause for this exception
     */
    public AbortProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

}
