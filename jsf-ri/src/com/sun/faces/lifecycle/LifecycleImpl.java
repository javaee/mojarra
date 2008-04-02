/*
 * $Id: LifecycleImpl.java,v 1.26 2003/06/26 19:08:42 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// LifecycleImpl.java

package com.sun.faces.lifecycle;

import com.sun.faces.context.FacesContextImpl;
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
 * @version $Id: LifecycleImpl.java,v 1.26 2003/06/26 19:08:42 horwat Exp $
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
            if ( (((Phase)renderPhase).getId()) == 
                    Phase.RENDER_RESPONSE) {
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

            // Execute the current phase
            curPhase.execute(context);

            // Process Events 

            int phaseNumber = ((Phase)curPhase).getId();

            if (phaseNumber == Phase.APPLY_REQUEST_VALUES) {
                processEvents(context, PhaseId.APPLY_REQUEST_VALUES);
            } else if (phaseNumber == Phase.PROCESS_VALIDATIONS) {
                processEvents(context, PhaseId.PROCESS_VALIDATIONS);
            } else if (phaseNumber == Phase.UPDATE_MODEL_VALUES) {
                processEvents(context, PhaseId.UPDATE_MODEL_VALUES);
            } else if (phaseNumber == Phase.INVOKE_APPLICATION) {
                processEvents(context, PhaseId.INVOKE_APPLICATION);
            }

            if (((FacesContextImpl)context).getResponseComplete()) {
                return;
            } else if (((FacesContextImpl)context).getRenderResponse()) {
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

    /**
     * <p>Register a new {@link PhaseListener} instance that is interested in
     * being notified before and after the processing for standard phases of
     * the request processing lifecycle.</p>
     *
     * @param listener The {@link PhaseListener} to be registered
     *
     * @exception NullPointerException if <code>listener</code>
     *	is <code>null</code>
     */
    public void addPhaseListener(PhaseListener listener) {
        //PENDING I am just a placeholder. Implement me.
    }

    /**
     * <p>Deregister an existing {@link PhaseListener} instance that is no
     * longer interested in being notified before and after the processing
     * for standard phases of the request processing lifecycle.	 If no such
     * listener instance has been registered, no action is taken.</p>
     *
     * @param listener The {@link PhaseListener} to be deregistered
     */
    public void removePhaseListener(PhaseListener listener) {
        //PENDING I am just a placeholder. Implement me.
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
                if (((FacesContextImpl)context).getResponseComplete() || 
                    ((FacesContextImpl)context).getRenderResponse()) {
                    return;
                }
            }
        }

    } // end of class PhaseWrapper


// The testcase for this class is TestLifecycleImpl.java 


} // end of class LifecycleImpl
