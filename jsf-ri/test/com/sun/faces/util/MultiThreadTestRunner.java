/*
 * $Id: MultiThreadTestRunner.java,v 1.4 2006/03/29 23:05:02 rlubke Exp $
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

// MultiThreadTestRunner.java

package com.sun.faces.util;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.BitSet;

import java.io.PrintStream;

/**
 * <B>MultiThreadTestRunner.java</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: MultiThreadTestRunner.java,v 1.4 2006/03/29 23:05:02 rlubke Exp $
 */

public class MultiThreadTestRunner extends Object {

//
// Protected Constants
//

// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

    private Thread [] threads;
    private Object [] outcomes;

//
// Constructors and Initializers    
//

    public MultiThreadTestRunner(Thread [] yourThreads,
				 Object [] yourOutcomes) {
	threads = yourThreads;
	outcomes = yourOutcomes;

	if (null == threads || null == outcomes) {
	    throw new IllegalArgumentException();
	}
    }

    /**
     * @return true iff one of the threads has failed.
     */

    public boolean runThreadsAndOutputResults(PrintStream out) throws Exception {
	int i;

	if (outcomes.length != threads.length) {
	    throw new IllegalArgumentException();
	}
	
	for (i = 0; i < threads.length; i++) {
	    outcomes[i] = null;
	}
	for (i = 0; i < threads.length; i++) {
	    threads[i].start();
	}
	
	BitSet printed = new BitSet(threads.length);
	boolean foundFailedThread = false;
	// wait for all threads to complete
	while (true) {
	    boolean foundIncompleteThread = false;
	    for (i = 0; i < threads.length; i++) {
		if (null == outcomes[i]) {
		    foundIncompleteThread = true;
		    break;
		}
		else {
		    // print out the outcome for this thread
		    if (!printed.get(i)) {
			printed.set(i);
			out.print(threads[i].getName() + " outcome: ");
			if (outcomes[i] instanceof Exception) {
			    foundFailedThread = true;
			    out.println("Exception: " + 
					       outcomes[i] + " " + 
					       ((Exception)outcomes[i]).getMessage());
			}
			else {
			    out.println(outcomes[i].toString());
			}
			out.flush();
		    }
		}
	    }
	    if (!foundIncompleteThread) {
		break;
	    }
	    Thread.sleep(1000);
	}
	
	return foundFailedThread;
    }

} // end of class MultiThreadTestRunner
