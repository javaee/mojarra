/*
 * $Id: TestUtil.java,v 1.1 2003/07/31 12:22:25 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

public class TestUtil extends Object {

    public static boolean keepWaiting = true;

    /** 
     * Usage: <P>
     *
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


}
