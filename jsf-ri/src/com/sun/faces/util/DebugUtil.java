/*
 * $Id: DebugUtil.java,v 1.1 2001/12/02 01:23:38 edburns Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
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
 * @version $Id: DebugUtil.java,v 1.1 2001/12/02 01:23:38 edburns Exp $
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
