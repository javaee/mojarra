/*
 * $Id: MultiThreadTestRunner.java,v 1.1 2004/10/14 22:08:52 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
 * @version $Id: MultiThreadTestRunner.java,v 1.1 2004/10/14 22:08:52 edburns Exp $
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
