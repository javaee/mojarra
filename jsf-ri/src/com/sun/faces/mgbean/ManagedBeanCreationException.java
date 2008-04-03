/*
 * $Id: ManagedBeanCreationException.java,v 1.1 2007/04/22 21:41:05 rlubke Exp $
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
 * Copyright 2007 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.mgbean;

import javax.faces.FacesException;

/**
 * <p>Indicates an error in the ManagedBean, beit a user or runtime.</p>
 */
public class ManagedBeanCreationException extends FacesException {


    // ------------------------------------------------------------ Constructors


    public ManagedBeanCreationException() {
        super();
    }


    public ManagedBeanCreationException(String message) {
        super(message);
    }


    public ManagedBeanCreationException(Throwable t) {
        super(t);
    }


    public ManagedBeanCreationException(String message, Throwable t) {
        super(message, t);  
    }


}
