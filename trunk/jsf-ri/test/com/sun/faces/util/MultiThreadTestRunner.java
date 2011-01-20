/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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
