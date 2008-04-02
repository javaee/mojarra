/*
 * $Id: GenericPhaseImpl.java,v 1.6 2003/01/17 18:07:15 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// GenericPhaseImpl.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

import com.sun.faces.RIConstants;
import com.sun.faces.tree.TreeNavigator;
import com.sun.faces.tree.TreeNavigatorImpl;

/**

 *  <B>GenericPhaseImpl</B> is the base class for Phase implementations
 *  in the JSF RI. <P>

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: GenericPhaseImpl.java,v 1.6 2003/01/17 18:07:15 rkitain Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Phase
 *
 */

public class GenericPhaseImpl extends Phase
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

protected int id = -1;

// Relationship Instance Variables

protected Lifecycle lifecycleDriver = null;

protected LifecycleCallback callback = null;

protected Object lock = null;

//
// Constructors and Genericializers    
//

public GenericPhaseImpl(Lifecycle newDriver, int newId)
{
    super();
    this.init(newDriver, newId, null);
}

public GenericPhaseImpl(Lifecycle newDriver, int newId, 
			LifecycleCallback newCallback)
{
    super();
    this.init(newDriver, newId, newCallback);
}

public void init(Lifecycle newDriver, int newId, LifecycleCallback newCallback)
{
    ParameterCheck.withinRange(newId, LifecycleFactoryImpl.FIRST_PHASE, 
			       LifecycleFactoryImpl.LAST_PHASE);

    lifecycleDriver = newDriver;
    id = newId;
    callback = newCallback;
    lock = new Object();
}
    

//
// Class methods
//

//
// General Methods
//

public void setLifecycle(Lifecycle newDriver)
{
    ParameterCheck.nonNull(newDriver);
    lifecycleDriver = newDriver;
}

public int getId()
{
    return id;
}

void setLifecycleCallback(LifecycleCallback newCallback)
{
    callback = newCallback;
}

LifecycleCallback getLifecycleCallback()
{
    return callback;
}

/**

* @param facesContext the facesContext for this request

* @param callback May be null.  If non-null, the callback will be
* invoked on each component in the tree.

*/

public int traverseTreeInvokingCallback(FacesContext facesContext) throws FacesException
{
    TreeNavigator treeNav = null;
    UIComponent next = null;
    int result = Phase.GOTO_NEXT;

    // PENDING(edburns): use a factory for the TreeNavigator instance
    if (RIConstants.RENDER_RESPONSE_PHASE == id) {
	treeNav = 
	    new TreeNavigatorImpl(facesContext.getTree().getRoot());
    }
    else {
	treeNav = 
	    new TreeNavigatorImpl(facesContext.getTree().getRoot());
    }
    if (null == treeNav) {
	throw new FacesException("Can't create TreeNavigator");
    }

    while (null != (next = treeNav.getNextStart())) {
	if (null != callback) {
	    result = callback.takeActionOnComponent(facesContext, next);
	    if (Phase.GOTO_NEXT != result) {
		break;
	    }
	}
    }
    treeNav.reset();

    return result;
}

// 
// Methods from Phase
//


/**


*/

public int execute(FacesContext facesContext) throws FacesException
{
    return traverseTreeInvokingCallback(facesContext);
}


// The testcase for this class is TestLifecycleImpl.java 
// The testcase for this class is TestGenericPhaseImpl.java 


} // end of class GenericPhaseImpl
