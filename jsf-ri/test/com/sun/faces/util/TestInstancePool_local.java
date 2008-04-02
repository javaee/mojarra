/*
 * $Id: TestInstancePool_local.java,v 1.1 2004/05/11 21:35:27 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestInstancePool_local.java

package com.sun.faces.util;

import com.sun.faces.util.InstancePool.NewInstance;

import junit.framework.TestCase;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.BitSet;

/**
 * <B>TestInstancePool_local.java</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestInstancePool_local.java,v 1.1 2004/05/11 21:35:27 eburns Exp $
 */

public class TestInstancePool_local extends TestCase {

//
// Protected Constants
//

// Class Variables
//

    public static final int POSITIVE_RUNNABLE = 0;
    public static final int NEGATIVE_RUNNABLE = 1;

//
// Instance Variables
//

// Attribute Instance Variables

    InstancePool pool = null;

// Relationship Instance Variables

    Random random = null;

    public Object threadOutcomes[] = null;

    public Object [] getThreadOutcomes() {
	return threadOutcomes;
    }


//
// Constructors and Initializers    
//

    public TestInstancePool_local() {
        super("TestInstancePool_local");
	random = new Random(32714);
    }


    public TestInstancePool_local(String name) {
        super(name);
	random = new Random(32714);
    }

    public void setUp() {
	pool = new InstancePool();
	pool.setInstantiator(new NewInstance() {
		public Object newInstance() {
		    Object result = new Object();
		    return result;
		}
	    });
    }
    
    public void tearDown() {
	pool.releasePooledInstances();
	pool = null;
    }

//
// Class methods
//

//
// General Methods
//

    public void testBasicCheckoutCheckinPositive() throws Exception {
	Object i1, i2;
	i1 = pool.checkout();
	i2 = pool.checkout();
	assertNotSame(i1, i2);

	pool.checkin(i2);
	pool.checkin(i1);
    }

    public void testBasicCheckoutCheckinNegative() throws Exception {
	
	InstancePool otherPool = new InstancePool();
	Object i1, i2;
	boolean exceptionThrown = false;

	try {
	    // uninitialized
	    i1 = otherPool.checkout();
	    assertTrue(false);
	}
	catch (IllegalStateException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);
	otherPool = null;

	exceptionThrown = false;
	try {
	    pool.checkin(new Object());
	    assertTrue(false);
	}
	catch (IllegalStateException e) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

    }

    public boolean doMultiThreadTest(int numThreads, 
				     int runnableType) throws Exception {
	int 
	    i, 
	    numToCheckout;
	
	Thread threads[] = new Thread[numThreads];
	Runnable runnable = null;
	for (i = 0; i < numThreads; i++) {
	    numToCheckout = (Math.abs(random.nextInt()) % 14) + 1;
	    switch(runnableType) {
	    case POSITIVE_RUNNABLE:
		runnable = new PositiveRunnable(pool, numToCheckout,
						random, i, this);
		break;
	    case NEGATIVE_RUNNABLE:
		runnable = new NegativeRunnable(pool, numToCheckout,
						random, i, this);
		break;
	    default:
		fail();
	    }
	    // check out a random number from 1 - 15 of instances
	    threads[i] = new Thread(runnable, "TestThread" + i);
	}
	
	threadOutcomes = new Object[numThreads];
	for (i = 0; i < numThreads; i++) {
	    threadOutcomes[i] = null;
	}
	for (i = 0; i < numThreads; i++) {
	    threads[i].start();
	}
	
	BitSet printed = new BitSet(numThreads);
	boolean foundFailedThread = false;
	// wait for all threads to complete
	while (true) {
	    boolean foundIncompleteThread = false;
	    for (i = 0; i < numThreads; i++) {
		if (null == threadOutcomes[i]) {
		    foundIncompleteThread = true;
		    break;
		}
		else {
		    // print out the outcome for this thread
		    if (!printed.get(i)) {
			printed.set(i);
			System.out.print(threads[i].getName() + " outcome: ");
			if (threadOutcomes[i] instanceof Exception) {
			    foundFailedThread = true;
			    System.out.println("Exception: " + 
					       threadOutcomes[i] + " " + 
					       ((Exception)threadOutcomes[i]).getMessage());
			}
			else {
			    System.out.println(threadOutcomes[i].toString());
			}
			System.out.flush();
		    }
		}
	    }
	    if (!foundIncompleteThread) {
		break;
	    }
	    Thread.sleep(1000);
	}
	
	return !foundFailedThread;
    }

    public void testMultiThreadBasicPositive() throws Exception {
	assertTrue(doMultiThreadTest(500, POSITIVE_RUNNABLE));
    }

    public void testMultiThreadBasicNegative() throws Exception {
	assertTrue(!doMultiThreadTest(30, NEGATIVE_RUNNABLE));
    }

    public static class PositiveRunnable implements Runnable {
	protected InstancePool pool = null;
	protected int numToCheckout;
	protected Random random = null;
	protected int index;
	protected TestInstancePool_local host = null;
	
	public PositiveRunnable(InstancePool pool, int numToCheckout, 
			     Random random, int index, 
			     TestInstancePool_local host) {
	    this.pool = pool;
	    this.numToCheckout = numToCheckout;
	    this.random = random;
	    this.index = index;
	    this.host = host;
	}

	public void run() {
	    Object instance = null;
	    for (int i = 0; i < numToCheckout; i++) {
		try {
		    instance = pool.checkout();
		}
		catch (Exception e) {
		    System.out.println("index: " + index + " exception on checkout(): " + e.getMessage());
		    host.getThreadOutcomes()[index] = e;
		    return;
		}
		try {
		    Thread.sleep(Math.abs(random.nextLong()) % 2000);
		}
		catch (InterruptedException e) {
		    host.getThreadOutcomes()[index] = e;
		    return;
		}
		try {
		    pool.checkin(instance);
		}
		catch (Exception e) {
		    System.out.println("index: " + index + " exception on checkin(): " + e.getMessage());
		    host.getThreadOutcomes()[index] = e;
		    return;
		}
		
	    }
	    host.getThreadOutcomes()[index] = 
		Thread.currentThread().getName()+" executed successfully.";
	    return;
	}
    }

    public static class NegativeRunnable extends PositiveRunnable {

	public NegativeRunnable(InstancePool pool, int numToCheckout, 
			     Random random, int index, 
			     TestInstancePool_local host) {
	    super(pool, numToCheckout, random, index, host);
	}

	public void run() {
	    Object instance = null;
	    for (int i = 0; i < numToCheckout; i++) {
		try {
		    pool.checkin(new Object());
		}
		catch (Exception e) {
		    System.out.println("index: " + index + " exception on checkin(): " + e.getMessage());
		    host.getThreadOutcomes()[index] = e;
		    return;
		}
		
	    }
	    host.getThreadOutcomes()[index] = 
		Thread.currentThread().getName()+" executed successfully.";
	    
	    return;
	}
    }


} // end of class TestInstancePool_local
