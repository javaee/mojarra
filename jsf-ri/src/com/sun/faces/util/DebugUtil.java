/*
 * $Id: DebugUtil.java,v 1.2 2001/12/20 22:26:42 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.util;

// DebugUtil.java

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>DebugUtil</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: DebugUtil.java,v 1.2 2001/12/20 22:26:42 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class DebugUtil extends Object
{
//
// Protected Constants
//

//
// Class Variables
//

public static boolean keepWaiting = true;

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

public DebugUtil()
{
    super();
    // ParameterCheck.nonNull();
    this.init();
}

protected void init()
{
    // super.init();
}

//
// Class methods
//

/** 

* Usage: <P>

* Place a call to this method in the earliest possible entry point of
* your servlet app.  It will cause the app to enter into an infinite
* loop, sleeping until the static var keepWaiting is set to false.  The
* idea is that you attach your debugger to the servlet, then, set a
* breakpont in this method.  When it is hit, you use the debugger to set
* the keepWaiting class var to false.

*/

public static void waitForDebugger() {
    while (keepWaiting) {
	try {
	    Thread.sleep(5000);
	}
	catch (InterruptedException e) {
	    System.out.println("DebugUtil.waitForDebugger(): Exception: " + 
			       e.getMessage());
	}
    }
}
//
// General Methods
//


} // end of class DebugUtil
