/*
 * $Id: TestUtil.java,v 1.5 2005/08/22 22:08:14 ofung Exp $
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
