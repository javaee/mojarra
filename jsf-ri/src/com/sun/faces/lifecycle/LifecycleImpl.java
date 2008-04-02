/*
 * $Id: LifecycleImpl.java,v 1.28 2003/08/13 21:07:25 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
import javax.faces.lifecycle.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

/**
 *
 *  <B>LifecycleImpl</B> is the stock implementation of the standard
 *  Lifecycle in the JSF RI. <P>
 *
 *
 * @version $Id: LifecycleImpl.java,v 1.28 2003/08/13 21:07:25 rkitain Exp $
 * 
 * @see	javax.faces.lifecycle.Lifecycle
 *
 */

public class LifecycleImpl extends Lifecycle
{
//
// Protected Constants
//

    protected static final int BEFORE = 0;
    protected static final int AFTER = 1;
    
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

    protected ArrayList phaseListeners;


    protected Object lock = null;

    protected ViewHandler viewHandler = null;

    // keeps track of total number of events processed 
    // per event source component

    public HashMap eventsProcessed = null;
    public String limit= null;
    public int eventLimit = RIConstants.MAX_EVENTS; // some default;

    //
    // Constructors and Initializers    
    //

    public LifecycleImpl() {
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

    protected void initPhases() {
        phaseWrappers.add(new PhaseWrapper(new ReconstituteComponentTreePhase()));
        phaseWrappers.add(new PhaseWrapper(new ApplyRequestValuesPhase()));
        phaseWrappers.add(new PhaseWrapper(new ProcessValidationsPhase()));
        phaseWrappers.add(new PhaseWrapper(new UpdateModelValuesPhase()));
        phaseWrappers.add(new PhaseWrapper(new InvokeApplicationPhase(this)));
        phaseWrappers.add(new PhaseWrapper(new RenderResponsePhase(this)));
    }

    protected void executeRender(FacesContext context) throws FacesException {
        Assert.assert_it(null != phaseWrappers);
        Phase renderPhase = null;
        PhaseWrapper wrapper = null;
  
        Iterator it = phaseWrappers.iterator();
        while ( it.hasNext() ) {
            wrapper = (PhaseWrapper) it.next();
            Assert.assert_it(wrapper != null);
            renderPhase = wrapper.instance;
            Assert.assert_it(renderPhase != null);
            if (((Phase)renderPhase).getId() == PhaseId.RENDER_RESPONSE) {
                break;
            }
        }
    
        Assert.assert_it(null != renderPhase);
        renderPhase.execute(context);
    }

    //
    // Methods from Lifecycle
    //

    public ViewHandler getViewHandler() {
        if (null == viewHandler) {
	    viewHandler = new ViewHandlerImpl();
        }
        return viewHandler;
    }

    public void setViewHandler(ViewHandler handler) {
        if (handler == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_HANDLER_ERROR_MESSAGE_ID));
        }

        viewHandler = handler;
    }

    public void execute(FacesContext context) throws FacesException {
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }

        PhaseWrapper wrapper = null;
        Phase curPhase = null;
        Iterator phaseIter = phaseWrappers.iterator();
        int curPhaseId = 0;
        Assert.assert_it(null != phaseIter);

        // for keeping track of events processed limit..
        //
        limit = context.getExternalContext().getInitParameter(
            RIConstants.EVENT_LIMIT);
        if (limit != null) {
            eventLimit = new Integer(limit).intValue();
        }
        eventsProcessed = new HashMap();

        while (phaseIter.hasNext()) {
	    wrapper = (PhaseWrapper)phaseIter.next();
	    curPhase = wrapper.instance;

	    maybeCallListeners(curPhase, BEFORE);

            // Execute the current phase
            curPhase.execute(context);

            // Process Events 

            if (curPhase.getId() == PhaseId.APPLY_REQUEST_VALUES) {
                processEvents(context, PhaseId.APPLY_REQUEST_VALUES);
            } else if (curPhase.getId() == PhaseId.PROCESS_VALIDATIONS) {
                processEvents(context, PhaseId.PROCESS_VALIDATIONS);
            } else if (curPhase.getId() == PhaseId.UPDATE_MODEL_VALUES) {
                processEvents(context, PhaseId.UPDATE_MODEL_VALUES);
            } else if (curPhase.getId() == PhaseId.INVOKE_APPLICATION) {
                processEvents(context, PhaseId.INVOKE_APPLICATION);
            }

            if (context.getResponseComplete()) {
                return;
            } else if (context.getRenderResponse()) {
                executeRender(context);
                return;
            }

	    maybeCallListeners(curPhase, AFTER);

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
		    Object [] params = { Integer.toString(eventLimit) };
                    throw new RuntimeException(Util.getExceptionMessage(Util.MAXIMUM_EVENTS_REACHED_ERROR_MESSAGE_ID, params));
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

    public void addPhaseListener(PhaseListener listener) {
	if (null == listener) {
	    throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}
	
	synchronized (lock) {
	    if (null == phaseListeners) {
		phaseListeners = new ArrayList();
	    }
	    phaseListeners.add(listener);
	}
    }

    public void removePhaseListener(PhaseListener listener) {
	if (null == listener) {
	    throw new NullPointerException(Util.getExceptionMessage(Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
	}
	
	synchronized (lock) {
	    if (null != phaseListeners) {
		phaseListeners.remove(listener);
	    }
	}
    }

    //
    // Helper methods
    //

    /**
     * <p>If we have no listeners, just return.</p>
     * <p>For each listener in our listener list.</p>
     */

    protected void maybeCallListeners(Phase curPhase, int listenerMethod) {
	if (null == phaseListeners) {
	    return;
	}
	Assert.assert_it(null != curPhase);
	Assert.assert_it(listenerMethod == BEFORE || listenerMethod == AFTER);
	PhaseEvent event = new PhaseEvent(FacesContext.getCurrentInstance(),
					  curPhase.getId());
	
	synchronized(lock) {
	    Iterator listenerIter = phaseListeners.iterator();
	    PhaseListener curListener = null;
	    while (listenerIter.hasNext()) {
		curListener = (PhaseListener) listenerIter.next();
		if (curPhase.getId() == curListener.getPhaseId() ||
		    PhaseId.ANY_PHASE == curListener.getPhaseId()) {
		    switch (listenerMethod) {
		    case BEFORE:
			curListener.beforePhase(event);
			break;
		    case AFTER:
			curListener.afterPhase(event);
			break;
		    default:
			Assert.assert_it(false);
			break;
		    }
		}
	    }
	}
    }
    

    //
    // Helper classes
    //

    static class PhaseWrapper extends Object {

        protected Phase instance = null;

        PhaseWrapper(Phase newInstance) {
            instance = newInstance;
        }

        /**
         * PRECONDITION: phaseIter is non null <P>
         * POSTCONDITION: All the phases in phaseIter have had their execute
         * method called, unless one of them either threw an exception or
         * returned a result other than Phase.GOTO_NEXT.
         */

        private void executePhaseIterator(Iterator phaseIter, 
				          FacesContext context) 
                                          throws FacesException {
            Phase curPhase = null;
            while (phaseIter.hasNext()) {
	        curPhase = (Phase) phaseIter.next();
	        curPhase.execute(context);
                if (context.getResponseComplete() || 
                    context.getRenderResponse()) {
                    return;
                }
            }
        }

    } // end of class PhaseWrapper


// The testcase for this class is TestLifecycleImpl.java 


} // end of class LifecycleImpl
