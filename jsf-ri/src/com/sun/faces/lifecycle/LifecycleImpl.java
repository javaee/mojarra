/*
 * $Id: LifecycleImpl.java,v 1.14 2002/10/08 00:26:30 jvisvanathan Exp $
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

import java.util.Iterator;
import java.util.ArrayList;

/**
 *
 *  <B>LifecycleImpl</B> is the stock implementation of the standard
 *  Lifecycle in the JSF RI. <P>
 *
 *
 * @version $Id: LifecycleImpl.java,v 1.14 2002/10/08 00:26:30 jvisvanathan Exp $
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
    phaseWrappers.add(new PhaseWrapper(new HandleRequestEventsPhase(this, 
            RIConstants.HANDLE_REQUEST_EVENTS_PHASE)));
    phaseWrappers.add(new PhaseWrapper(new ProcessValidationsPhase(this, 
            RIConstants.PROCESS_VALIDATIONS_PHASE)));
    phaseWrappers.add(new PhaseWrapper(new UpdateModelValuesPhase(this,
            RIConstants.UPDATE_MODEL_VALUES_PHASE)));
    phaseWrappers.add(new PhaseWrapper(new InvokeApplicationPhase(this, 
	    RIConstants.INVOKE_APPLICATION_PHASE)));
    phaseWrappers.add(new PhaseWrapper(new JspRenderResponsePhase(this, 
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

    while (phaseIter.hasNext()) {
	wrapper = (PhaseWrapper)phaseIter.next();
	curPhase = wrapper.instance;

        // Execute the current phase
	if (Phase.GOTO_EXIT == (rc = curPhase.execute(context))) {
	    return;
	}
	else if (rc == Phase.GOTO_RENDER) {
	    executeRender(context);
	    return;
	}
        curPhaseId++;
    }
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
