/*
 * $Id: LifecycleImpl.java,v 1.17 2003/02/04 19:57:31 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// LifecycleImpl.java

package com.sun.faces.lifecycle;

import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;
import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

/**
 *
 *  <B>LifecycleImpl</B> is the stock implementation of the standard
 *  Lifecycle in the JSF RI. <P>
 *
 *
 * @version $Id: LifecycleImpl.java,v 1.17 2003/02/04 19:57:31 rogerk Exp $
 * 
 * @see	javax.faces.lifecycle.Lifecycle
 *
 */

public class LifecycleImpl extends Lifecycle
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

/**

* INVARIANT: The contents of phaseWrappers must not change after the
* ctor returns.

*/

protected ArrayList phaseWrappers;


protected Object lock = null;

protected ApplicationHandler applicationHandler = null;
protected ViewHandler viewHandler = null;

// keeps track of total number of events processed 
// per event source component

public HashMap eventsProcessed = null;
public String limit= null;
public int eventLimit = RIConstants.MAX_EVENTS; // some default;

//
// Constructors and Initializers    
//

public LifecycleImpl()
{
    super();
    phaseWrappers  = new ArrayList();
    initPhases();
    lock = new Object();
}

//
// Class methods
//

//
// General Methods
//

protected void initPhases()
{
    phaseWrappers.add(new PhaseWrapper(new ReconstituteRequestTreePhase(this, 
            RIConstants.RECONSTITUTE_REQUEST_TREE_PHASE)));
    phaseWrappers.add(new PhaseWrapper(new ApplyRequestValuesPhase(this, 
            RIConstants.APPLY_REQUEST_VALUES_PHASE)));
    phaseWrappers.add(new PhaseWrapper(new ProcessValidationsPhase(this, 
            RIConstants.PROCESS_VALIDATIONS_PHASE)));
    phaseWrappers.add(new PhaseWrapper(new UpdateModelValuesPhase(this,
            RIConstants.UPDATE_MODEL_VALUES_PHASE)));
    phaseWrappers.add(new PhaseWrapper(new InvokeApplicationPhase(this, 
	    RIConstants.INVOKE_APPLICATION_PHASE)));
    phaseWrappers.add(new PhaseWrapper(new RenderResponsePhase(this, 
	    RIConstants.RENDER_RESPONSE_PHASE)));
}

protected int executeRender(FacesContext context) throws FacesException
{
    Assert.assert_it(null != phaseWrappers);
    int rc = 0;
    Phase renderPhase = null;
    PhaseWrapper wrapper = null;
  
    Iterator it = phaseWrappers.iterator();
    while ( it.hasNext() ) {
        wrapper = (PhaseWrapper) it.next();
        Assert.assert_it(wrapper != null);
        renderPhase = wrapper.instance;
        Assert.assert_it(renderPhase != null);
        if ( (((GenericPhaseImpl)renderPhase).getId()) == 
                RIConstants.RENDER_RESPONSE_PHASE) {
            break;
        }
    }
    
    Assert.assert_it(null != renderPhase);
    rc = renderPhase.execute(context);
    
    return rc;
}

//
// Methods from Lifecycle
//

public ApplicationHandler getApplicationHandler()
{
    return applicationHandler;
}

public void setApplicationHandler(ApplicationHandler handler)
{
    if (handler == null) {
        throw new NullPointerException(Util.getExceptionMessage(
            Util.NULL_HANDLER_ERROR_MESSAGE_ID));
    }

    applicationHandler = handler;
}

public ViewHandler getViewHandler()
{
    if (null == viewHandler) {
	viewHandler = new ViewHandlerImpl();
    }
    return viewHandler;
}

public void setViewHandler(ViewHandler handler)
{
    if (handler == null) {
        throw new NullPointerException(Util.getExceptionMessage(
            Util.NULL_HANDLER_ERROR_MESSAGE_ID));
    }

    viewHandler = handler;
}

public void execute(FacesContext context) throws FacesException
{
    if (context == null) {
        throw new NullPointerException(Util.getExceptionMessage(
            Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
    }

    PhaseWrapper wrapper = null;
    Phase curPhase = null;
    Iterator phaseIter = phaseWrappers.iterator();
    int curPhaseId = 0, rc = 0;
    Assert.assert_it(null != phaseIter);

    PhaseId phaseId = null;

    // for keeping track of events processed limit..
    //
    limit = context.getServletContext().getInitParameter(
        RIConstants.EVENT_LIMIT);
    if (limit != null) {
        eventLimit = new Integer(limit).intValue();
    }
    eventsProcessed = new HashMap();

    while (phaseIter.hasNext()) {
	wrapper = (PhaseWrapper)phaseIter.next();
	curPhase = wrapper.instance;

        // Execute the current phase
        rc = curPhase.execute(context);

        // Process Events 

        int phaseNumber = ((GenericPhaseImpl)curPhase).getId();

        if (phaseNumber == RIConstants.APPLY_REQUEST_VALUES_PHASE) {
            phaseId = PhaseId.APPLY_REQUEST_VALUES;
            processEvents(context, phaseId);
        } else if (phaseNumber == RIConstants.PROCESS_VALIDATIONS_PHASE) {
            phaseId = PhaseId.PROCESS_VALIDATIONS;
            processEvents(context, phaseId);
        } else if (phaseNumber == RIConstants.UPDATE_MODEL_VALUES_PHASE) {
            phaseId = PhaseId.UPDATE_MODEL_VALUES;
            processEvents(context, phaseId);
        }

        if (rc == Phase.GOTO_EXIT) {
            return;
        } else if (rc == Phase.GOTO_RENDER) {
	    executeRender(context);
	    return;
	}

        curPhaseId++;
    }
}

// This method processes the events in the queue;

//We call iter.remove() to remove the event from the queue
//after all listeners for the event have received the event; The "broadcast"
//method returns "false" to indicate this.

private void processEvents(FacesContext context, PhaseId phaseId) {
    Iterator iter = context.getFacesEvents();
    while (iter.hasNext()) {
        FacesEvent event = (FacesEvent)iter.next();
        UIComponent source = event.getComponent();
        if (!source.broadcast(event, phaseId)) {
            iter.remove();
            if (limitReached(source, eventsProcessed)) {
                throw new RuntimeException("Maximum number of events ("+
                    eventLimit+") processed");
            }

        }
    }
}

//PENDING(rogerk) maybe we can optimize this method a bit..

private boolean limitReached(UIComponent source, HashMap eventsProcessed) {
    if (!eventsProcessed.containsKey(source)) {
        eventsProcessed.put(source, new Integer(1));
        return false;
    }
    
    int count = ((Integer)eventsProcessed.get(source)).intValue()+1;
    if (count > eventLimit) {
        return true;
    }


    eventsProcessed.put(source, new Integer(count));
    return false;
}

public int executePhase(FacesContext context, Phase phase) throws FacesException
{
    if (null == context || null == phase) { 
            throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
    } 
    
    return phase.execute(context); 
}


//
// Helper classes
//

static class PhaseWrapper extends Object
{

protected Phase instance = null;

PhaseWrapper(Phase newInstance)
{
    instance = newInstance;
}

/**

* PRECONDITION: phaseIter is non null <P>

* POSTCONDITION: All the phases in phaseIter have had their execute
* method called, unless one of them either threw an exception or
* returned a result other than Phase.GOTO_NEXT.

*/

private int executePhaseIterator(Iterator phaseIter, 
				 FacesContext context) throws FacesException
{
    Phase curPhase = null;
    int rc = Phase.GOTO_NEXT;
    while (phaseIter.hasNext()) {
	curPhase = (Phase) phaseIter.next();
	rc = curPhase.execute(context);
	if (Phase.GOTO_NEXT != rc) {
	    return rc;
	}
    }
    return rc;
}

} // end of class PhaseWrapper


// The testcase for this class is TestLifecycleImpl.java 


} // end of class LifecycleImpl
