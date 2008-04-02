/*
 * $Id: MethodBindingAdapterBase.java,v 1.3 2006/09/01 01:22:15 tony_robertson Exp $
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

package javax.faces.component;


/**
 * <p>Base class for classes that wrap a <code>MethodBinding</code> and
 * implement a faces listener-like interface.</p>
 *
 */ 

abstract class MethodBindingAdapterBase extends Object {
    
    /**
     * <p>Recursively interrogate the <code>cause</code> property of the
     * argument <code>exception</code> and stop recursing either when it
     * is an instance of <code>expectedExceptionClass</code> or
     * <code>null</code>.  Return the result.</p>
     */

    Throwable getExpectedCause(Class expectedExceptionClass, 
			       Throwable exception) {
	Throwable result = exception.getCause();
	if (null != result) {
	    if (!result.getClass().isAssignableFrom(expectedExceptionClass)) {
		result = getExpectedCause(expectedExceptionClass, result);
	    }
	}
	return result;
    }
    
}
