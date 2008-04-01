/*
 * $Id: Util.java,v 1.7 2002/05/28 22:43:39 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Util.java

package com.sun.faces.util;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>Util</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Util.java,v 1.7 2002/05/28 22:43:39 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class Util extends Object
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
private static long id = 0;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

private Util()
{
    throw new IllegalStateException();
}

//
// Class methods
//
    public static Class loadClass(String name) throws ClassNotFoundException {
	ClassLoader loader =
	    Thread.currentThread().getContextClassLoader();
	if (loader == null) {
	    return Class.forName(name);
	}
	else {
	    return loader.loadClass(name);
	}
    }

//
// General Methods
//

} // end of class Util
