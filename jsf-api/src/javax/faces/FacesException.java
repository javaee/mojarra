/*
 * $Id: FacesException.java,v 1.15 2007/01/29 07:18:02 rlubke Exp $
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


package javax.faces;


/**
 * <p>This class encapsulates general JavaServer Faces exceptions.</p>
 */

public class FacesException extends RuntimeException {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new exception with no detail message or root cause.</p>
     */
    public FacesException() {

        super();

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * no root cause.</p>
     *
     * @param message The detail message for this exception
     */
    public FacesException(String message) {

        super(message);

    }


    /**
     * <p>Construct a new exception with the specified root cause.  The detail
     * message will be set to <code>(cause == null ? null :
     * cause.toString()</code>
     *
     * @param cause The root cause for this exception
     */
    public FacesException(Throwable cause) {

        super(cause == null ? null : cause.toString());
        this.cause = cause;

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause The root cause for this exception
     */
    public FacesException(String message, Throwable cause) {

        super(message);
        this.cause = cause;

    }



    // ----------------------------------------------------- Instance Variables


    /**
     * <p>The underlying exception that caused this exception.</p>
     */
    private Throwable cause = null;


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Return the cause of this exception, or <code>null</code> if the
     * cause is nonexistent or unknown.</p>
     */
    public Throwable getCause() {

        return (this.cause);

    }


}
