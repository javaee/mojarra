/*
 * $Id: HandleRequestEventsPhase.java,v 1.6 2002/10/07 20:39:49 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// HandleRequestEventsPhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;

import com.sun.faces.RIConstants;
import com.sun.faces.tree.TreeNavigator;
import com.sun.faces.tree.TreeNavigatorImpl;

import java.util.Iterator;


/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: HandleRequestEventsPhase.java,v 1.6 2002/10/07 20:39:49 jvisvanathan Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Lifecycle#HANDLE_REQUEST_EVENTS_PHASE
 *
 */

public class HandleRequestEventsPhase extends GenericPhaseImpl
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

// Relationship Instance Variables

//
// Constructors and Genericializers    
//

public HandleRequestEventsPhase(Lifecycle newDriver, int newId)
{
    super(newDriver, newId, 
	  new LifecycleCallback() {
	      public int takeActionOnComponent(FacesContext context,
					       UIComponent comp) throws FacesException {
		  if (comp.processEvents(context)) {
		      return Phase.GOTO_RENDER;
		  }
		  // PENDING(): how to skip to rendering?
		  return Phase.GOTO_NEXT;
	      }
	  });
}

// Overridden from GenericPhaseImpl to process all events
// before either proceedng to Render Response phase or the next
// phase in the lifecycle.
//

public int traverseTreeInvokingCallback(FacesContext facesContext)
    throws FacesException {

    TreeNavigator treeNav = null;
    UIComponent next = null;
    int result = Phase.GOTO_NEXT;
    boolean gotoRender = false;

    // PENDING(edburns): use a factory for the TreeNavigator instance
    if (RIConstants.RENDER_RESPONSE_PHASE == id) {
        treeNav =
            new TreeNavigatorImpl(facesContext.getResponseTree().getRoot());
    }
    else {
        treeNav =
            new TreeNavigatorImpl(facesContext.getRequestTree().getRoot());
    }
    if (null == treeNav) {
        throw new FacesException("Can't create TreeNavigator");
    }

    while (null != (next = treeNav.getNextStart())) {
        if (null != callback) {
            result = callback.takeActionOnComponent(facesContext, next);
            if (Phase.GOTO_NEXT != result) {
                gotoRender = true;
            }
        }
    }
    treeNav.reset();

    if (gotoRender) {
        result = Phase.GOTO_RENDER;
    }
    return result;
}

//
// Class methods
//

//
// General Methods
//


// The testcase for this class is TestHandleRequestEventsPhase.java


} // end of class HandleRequestEventsPhase
