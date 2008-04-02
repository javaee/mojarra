/*
 * $Id: InvokeApplicationPhase.java,v 1.5 2002/10/10 17:27:44 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// InvokeApplicationPhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
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
 * @version $Id: InvokeApplicationPhase.java,v 1.5 2002/10/10 17:27:44 jvisvanathan Exp $
 * 
 * @see	com.sun.faces.lifecycle.DefaultLifecycleImpl
 * @see	javax.faces.lifecycle.Lifecycle#INVOKE_APPLICATION_PHASE
 *
 */

public class InvokeApplicationPhase extends GenericPhaseImpl
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

public InvokeApplicationPhase(Lifecycle newDriver, int newId)
{
    super(newDriver, newId);
}

public int execute(FacesContext facesContext) throws FacesException
{
    int rc = Phase.GOTO_NEXT;
    ApplicationHandler handler = null;

    if (null == lifecycleDriver) {
        return rc;
    }

    handler = lifecycleDriver.getApplicationHandler();
    if (null == handler) {
        return rc;
    }

    // Process all events that have been queued to the
    // application
    Iterator events = facesContext.getApplicationEvents();
    while (events.hasNext()) {
        FacesEvent event = (FacesEvent) events.next();
	if (!handler.processEvent(facesContext, event)) {
            return Phase.GOTO_RENDER;
        }  
    }
    return rc;
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
