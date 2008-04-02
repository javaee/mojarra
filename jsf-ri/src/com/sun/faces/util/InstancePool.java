/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * $Id: InstancePool.java,v 1.3 2004/06/01 16:58:49 eburns Exp $
 */

package com.sun.faces.util;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A simple, thread-safe, instance pool offering checkout and checkin
 * services.</p>
 * 
 * <p>Usage:</p>
 *
 * <ol>
 *
 * <li><p>Instantiate the <code>InstancePool</code>: <code>InstancePool pool
 * = new InstancePool();</code></p>
 *
 * <li><p>Provide it with a <code>NewInstance</code> implementation: </p>
 *
 * <code><pre>
      pool.setInstantiator(new NewInstance() {
        public Object newInstance() {
          return new TheClassYouWantToPool();
        }
      });
 * </pre></code></li>
 *
 * <li><p>checkout instances as needed, making sure to check them in
 * when done.</p>
 *
 * <code><pre>
      TheClassYouWantToPool instance = (TheClassYouWantToPool) pool.checkout();
      //... code that uses the instance
      pool.checkin(instance);
 * </pre></code>
 * </li>
 *
 * <li><p>Optional: release all instances in the pool:
 * <code>pool.releasePooledInstances();</code></p></li>
 *
 * </ol>
 *
 */

public class InstancePool extends Object {

    // 
    // Attribute ivars
    // 

    private final int INITIAL_CAPACITY = 100;

    //
    // relationship ivars
    //

    private NewInstance instantiator;

    private List instances = null;

    private JSFBitSet checkedOut = null;

    public InstancePool() {
	instances = new ArrayList(INITIAL_CAPACITY);
	checkedOut = new JSFBitSet(INITIAL_CAPACITY);
    }

    /**
     * <p>This method must be called before any instances can be checked
     * out.  This method provides the <code>InstancePool</code> with a
     * means to instantiate new instances of the class whose instances
     * are to be pooled.</p>
     * 
     * @param instantiator the {@link #NewInstance} instance that will
     * be used to create instances of the class to be pooled.
     */

    public synchronized void setInstantiator(NewInstance instantiator) {
	if (null == this.instantiator) {
	    this.instantiator = instantiator;
	}
    }

    /**
     * <p>Release all pooled instances.  This does not reset the
     * <code>InstancePool</code> to an uninitialized state.</p>
     */

    public synchronized void releasePooledInstances() {
	instances.clear();
	checkedOut.clear();
    }

    /**
     * @return if this <code>InstancePool</code> is initialized and
     * ready to service requests.
     */

    public synchronized boolean isInitialized() {
	boolean result = false;
	result = (null != this.instantiator);
	return result;
    }

    /**
     * @return exclusive access to a pooled instance.  The instance will
     * not be returned from a future call to <code>checkout</code>
     * until it is returned to the pool by a call to {@link #checkin}.
     *
     * @throws IllegalStateException if the pool has not been
     * initialized.
     */
    
    public synchronized Object checkout() {
	if (null == instantiator) {
	    throw new IllegalStateException("InstancePool is uninitialized");
	}
	Object result = null;
	int nextClearBit = 0;
	nextClearBit = checkedOut.nextClearBit(0);
	if (instances.isEmpty() || 
	    (instances.size() <= nextClearBit)) {
	    instances.add(result = instantiator.newInstance());
	}
	else {
	    result = instances.get(nextClearBit);
	}
	checkedOut.set(nextClearBit);
	return result;
    }
    
    /**
     * @param toCheckin return to the pool an instance previously
     * checked out by a call to {@link #checkout}.
     *
     * @throws IllegalStateException if the pool has not been
     * initialized, or if the argument has not been checked out.
     */
    
    public synchronized void checkin(Object toCheckin) {
	if (null == instantiator) {
	    throw new IllegalStateException("InstancePool is uninitialized");
	}
	int i = 0;
	// find the instance in the pool
	if (-1 == (i = instances.indexOf(toCheckin))) {
	    throw new IllegalStateException("Trying to checkin an instance that is not in the pool: " + toCheckin);
	}
	Util.doAssert(checkedOut.get(i));
	checkedOut.clear(i);
    }

    public static class NewInstance extends Object {
	public Object newInstance() {
	    return null;
	}
    }


}
