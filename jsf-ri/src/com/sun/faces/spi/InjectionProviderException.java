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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.spi;

/**
 * Wraps any exception thrown by an implementation
 * of <code>InjectionProvider</code>.
 */
public class InjectionProviderException extends Exception {

    /**
     * Creates a new <code>InjectionProviderException</code> with
     * the root cause of the error.    
     * @param cause the root cause
     */
    public InjectionProviderException(Throwable cause) {
        super(cause);
    }
    
    
    /**
     * Creates a new <code>InjectionProviderException</code> with
     * a descriptive message and the root cause of the error.
     * @param message descriptive message
     * @param cause the root cause
     */
    public InjectionProviderException(String message, Throwable cause) {
        super(message, cause);        
    }
    
} // END InjectionProviderException