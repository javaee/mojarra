/*
 * $Id: MethodBindingAdapterBase.java,v 1.1 2005/05/05 20:51:02 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;

import java.lang.reflect.InvocationTargetException;

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
