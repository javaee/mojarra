/*
 * $Id: HandleRequestEventsPhase.java,v 1.8 2002/12/19 03:09:27 rkitain Exp $
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
 * @version $Id: HandleRequestEventsPhase.java,v 1.8 2002/12/19 03:09:27 rkitain Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Lifecycle#HANDLE_REQUEST_EVENTS_PHASE
 *
 */

public class HandleRequestEventsPhase extends GenericPhaseImpl {

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

    public HandleRequestEventsPhase(Lifecycle newDriver, int newId) {
        super(newDriver, newId); 
    }

    public int execute(FacesContext facesContext) throws FacesException {

        UIComponent component = 
            (UIComponent)facesContext.getRequestTree().getRoot();
        Assert.assert_it(null != component);

        if (!component.processEvents(facesContext)) {
            return Phase.GOTO_RENDER;
        }
        return Phase.GOTO_NEXT;
    }

//
// Class methods
//

//
// General Methods
//


// The testcase for this class is TestHandleRequestEventsPhase.java


} // end of class HandleRequestEventsPhase
