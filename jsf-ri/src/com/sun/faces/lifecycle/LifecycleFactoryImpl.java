/*
 * $Id: LifecycleFactoryImpl.java,v 1.1 2002/05/28 20:52:01 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// LifecycleFactoryImpl.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.FacesException;

import java.util.Iterator;
import java.util.HashMap;

/**
 *
 *  <B>LifecycleFactoryImpl</B> is the stock implementation of Lifecycle
 *  in the JSF RI. <P>
 *
 *
 * @version $Id: LifecycleFactoryImpl.java,v 1.1 2002/05/28 20:52:01 eburns Exp $
 * 
 * @see	javax.faces.lifecycle.LifecycleFactory
 *
 */

public class LifecycleFactoryImpl extends LifecycleFactory
{
//
// Protected Constants
//

static final int FIRST_PHASE = Lifecycle.CREATE_REQUEST_TREE_PHASE;
static final int LAST_PHASE = Lifecycle.RENDER_RESPONSE_PHASE;




//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

protected HashMap lifecycleMap = null;
protected Object lock = null;

//
// Constructors and Initializers    
//

public LifecycleFactoryImpl()
{
    super();
    lifecycleMap = new HashMap();

    // We must have an implementation under this key.
    lifecycleMap.put(LifecycleFactory.DEFAULT_LIFECYCLE, 
		     new LifecycleWrapper(new LifecycleImpl(),
					  false));
    lock = new Object();
}

//
// Class methods
//

//
// General Methods
//

void addLifecycle(String lifecycleId, LifecycleImpl lifecycle)
{
    lifecycleMap.put(lifecycleId, new LifecycleWrapper(lifecycle, false));
}

/**

* @return true iff lifecycleId was already created

*/

boolean alreadyCreated(String lifecycleId)
{
    LifecycleWrapper wrapper = (LifecycleWrapper)lifecycleMap.get(lifecycleId);
    return (null != wrapper && wrapper.created);
}

/**

* POSTCONDITION: If no exceptions are thrown, it is safe to proceed with
* register*().;

*/

LifecycleImpl verifyRegisterArgs(String lifecycleId, 
					int phaseId, Phase phase)
{
    LifecycleWrapper wrapper = null;
    LifecycleImpl result = null;
    if (null == lifecycleId || null == phase) {
	throw new NullPointerException("null lifecycleId || null phase");
    }

    if (null == (wrapper = (LifecycleWrapper) lifecycleMap.get(lifecycleId))) {
	throw new IllegalArgumentException("lifecycleId " + lifecycleId + 
					   " not found");
    }
    result = wrapper.instance;
    Assert.assert_it(null != result);
    
    if (alreadyCreated(lifecycleId)) {
	throw new IllegalStateException(lifecycleId + " already created");
    }
    
    if (!((FIRST_PHASE <= phaseId) &&
	  (phaseId <= LAST_PHASE))) {
	throw new IllegalArgumentException("phaseId " + phaseId + " out of bounds");
    }
    return result;
}

//
// Methods from LifecycleFactory
//

public Lifecycle createLifecycle(String lifecycleId) throws FacesException
{
    Lifecycle result = null;
    LifecycleWrapper wrapper = null;

    if (null == lifecycleId) {
	throw new NullPointerException("null lifecycleId");
    }
    
    try {
	wrapper = (LifecycleWrapper) lifecycleMap.get(lifecycleId);
	result = wrapper.instance;
	wrapper.created = true;
    }
    catch (Throwable e) {
	throw new FacesException("Can't create lifecycle for " +lifecycleId,e);
    }

    return result;
}

public Iterator getLifecycleIds()
{
    return lifecycleMap.entrySet().iterator();
}

public void registerAfter(String lifecycleId, int phaseId, Phase phase)
{
    LifecycleImpl life =verifyRegisterArgs(lifecycleId, phaseId, phase);
    synchronized (lock) {
	life.registerAfter(phaseId, phase);
    }
}

public void registerBefore(String lifecycleId, int phaseId, Phase phase)
{
    LifecycleImpl life =verifyRegisterArgs(lifecycleId, phaseId, phase);
    synchronized (lock) {
	life.registerBefore(phaseId, phase);
    }
}

//
// Helper classes
//

static class LifecycleWrapper extends Object
{


LifecycleImpl instance = null;
boolean created = false;

LifecycleWrapper(LifecycleImpl newInstance, boolean newCreated)
{
    instance = newInstance;
    created = newCreated;
}

} // end of class LifecycleWrapper


// The testcase for this class is TestLifecycleFactoryImpl.java 


} // end of class LifecycleFactoryImpl
