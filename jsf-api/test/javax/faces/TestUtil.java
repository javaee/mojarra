/*
 * $Id: TestUtil.java,v 1.3 2004/02/04 23:38:40 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
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

    /**
     * <p>If both args are <code>null</code>, return
     * <code>true</code>.</p>
     *
     * <p>If both args are <code>non-null</code>, return
     * s1.equals(s2)</p>.
     *
     * <p>Otherwise, return <code>false</code>.</p>
     *
     */

    public static boolean equalsWithNulls(Object s1, Object s2) {
	if (null == s1 && null == s2) {
	    return true;
	}

	if (null != s1 && null != s2) {
	    return s1.equals(s2);
	}

	return false;
    }


}
