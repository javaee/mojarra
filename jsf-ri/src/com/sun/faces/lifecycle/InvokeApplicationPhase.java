/*
 * $Id: InvokeApplicationPhase.java,v 1.7 2003/03/12 19:51:05 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// InvokeApplicationPhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.CommandEvent;
import javax.faces.event.FormEvent;

import java.util.Iterator;


/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: InvokeApplicationPhase.java,v 1.7 2003/03/12 19:51:05 rkitain Exp $
 * 
 */

public class InvokeApplicationPhase extends Phase {
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
private Lifecycle lifecycleDriver = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Genericializers    
//

public InvokeApplicationPhase(Lifecycle newDriver) {
    lifecycleDriver = newDriver;
}

public int getId() {
    return Phase.INVOKE_APPLICATION;
}

public void execute(FacesContext facesContext) throws FacesException
{
    ApplicationHandler handler = null;

    if (null == lifecycleDriver) {
        return;
    }

    handler = lifecycleDriver.getApplicationHandler();
    if (null == handler) {
        return;
    }

    // Process all events that have been queued to the
    // application
    Iterator events = facesContext.getApplicationEvents();
    while (events.hasNext()) {
        FacesEvent event = (FacesEvent) events.next();
	if (!handler.processEvent(facesContext, event)) {
            facesContext.renderResponse();
        }  
    }
}

//
// Class methods
//

//
// General Methods
//

//
// Methods from Phase
//


// The testcase for this class is TestInvokeApplicationPhase.java


} // end of class InvokeApplicationPhase
