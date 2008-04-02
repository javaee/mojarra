/*
 * $Id: ValidatorException.java,v 1.4 2005/08/22 22:08:10 ofung Exp $
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

package javax.faces.validator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;


/**
 * <p>A <strong>ValidatorException</strong> is an exception
 * thrown by the <code>validate()</code> method of a 
 * {@link Validator} to indicate that validation failed.
 */
public class ValidatorException extends FacesException {
    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new exception with the specified message and
     * no root cause.</p>
     *
     * @param message The message for this exception
     */
    public ValidatorException(FacesMessage message) {

        super(message.getSummary());
        this.message = message;
    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause The root cause for this exception
     */
    public ValidatorException(FacesMessage message, Throwable cause) {

        super(message.getSummary(), cause);
        this.message = message;
    }

    /**
     * Returns the FacesMessage associated with the exception.
     */
    public FacesMessage getFacesMessage() {
        return this.message;
    }

    private FacesMessage message;
}
