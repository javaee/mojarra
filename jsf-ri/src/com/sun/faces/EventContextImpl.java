/*
 * $Id: EventContextImpl.java,v 1.1 2002/01/11 20:05:59 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// EventContextImpl.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import java.util.EventObject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;

import javax.faces.EventContext;
import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.EventQueue;
import javax.faces.EventDispatcher;
import javax.faces.NavigationHandler;
import javax.faces.ClientCapabilities;
import javax.faces.ObjectManager;
import javax.faces.ObjectAccessor;

import javax.faces.EventQueueFactory;

import javax.faces.EventDispatcherFactory;

/**
 *
 *  <B>EventContextImpl</B> is created in FacesServlet.processRequest()
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: EventContextImpl.java,v 1.1 2002/01/11 20:05:59 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class EventContextImpl extends EventContext
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

private RenderContext renderContext = null;
private ServletRequest request = null;
private ServletResponse response = null;

private EventQueue eventQueue = null;

/**

* Used in getEventDispatcher.

*/ 

private EventDispatcherFactory eventDispatcherFactory = null;

/**
   
* Convenience ivar.  Owning reference is in RenderContext implementation.

*/

private ObjectManager objectManager = null;

//
// Constructors and Initializers    
//

public EventContextImpl(RenderContext yourRenderContext, 
			ServletRequest yourRequest,
			ServletResponse yourResponse)
{
    super();
    renderContext = yourRenderContext;
    request = yourRequest;
    response = yourResponse;
    objectManager = renderContext.getObjectManager();
    
    eventDispatcherFactory = (EventDispatcherFactory)
	objectManager.get(Constants.REF_EVENTDISPATCHERFACTORY);
    Assert.assert_it(eventDispatcherFactory != null );
}

//
// Class methods
//

//
// General Methods
//

// 
// Methods from RenderContext
//

public ClientCapabilities getClientCapabilities() {
    return renderContext.getClientCapabilities();
}

/**
 * @return ServletRequest object representing the client request
 */
public ServletRequest getRequest() {
    return request;
}

/**
 * @return ServletResponse object used to write response to the client
 *         request
 */
public ServletResponse getResponse() {
    return response;
}

/**

* PRECONDITION: EventQueueFactory is in ObjectManager.

*

 * @see javax.faces.EventContext#getEventQueue
 */
public EventQueue getEventQueue() {
    EventQueueFactory eventQueueFactory;

    if (null == eventQueue) {
        eventQueue = (EventQueue)objectManager.get(request, 
						   Constants.REF_EVENTQUEUE);
        if (eventQueue == null) {
            eventQueueFactory = (EventQueueFactory)
		objectManager.get(Constants.REF_EVENTQUEUEFACTORY);
            Assert.assert_it(null != eventQueueFactory);
            try {
                eventQueue = eventQueueFactory.newEventQueue();
            } catch (FacesException e) {
                // PENDING(edburns): log message
		System.out.println("Exception getEventQueue: " + 
				   e.getMessage());
		e.printStackTrace();
		Assert.assert_it(false);
            }
	    Assert.assert_it(null != eventQueue); 
            objectManager.put(request, Constants.REF_EVENTQUEUE, eventQueue);
        }
    }
    return eventQueue;
}

/**
 * @throws NullPointerException if event is null
 * @return EventDispatcher object used to obtain an object which can
 *         dispatched the specified event
 */
public EventDispatcher getEventDispatcher(EventObject event) {
    EventDispatcher result = null;
    try {
	result = eventDispatcherFactory.newEventDispatcher(event);
    }
    catch (FacesException e) {
	// PENDING(edburns): log message
	System.out.println("Exception getEventDispatcher: " + e.getMessage());
	e.printStackTrace();
	Assert.assert_it(false);
    }
    return result;
}

/**
 * @return NavigationHandler object used to configure the navigational
 *         result of processing events originating from this request
 */
public NavigationHandler getNavigationHandler() {
    return null; //compile
}

/**
 * @return ObjectManager used to manage application objects in scoped
 *         namespace
 */
public ObjectManager getObjectManager() {
    return renderContext.getObjectManager();
}

/**
 * @return ObjectAccessor used to resolve object-reference Strings to
 *         objects
 */
public ObjectAccessor getObjectAccessor() {
    return null; //compile
}

// The testcase for this class is TestEventContext.java 


} // end of class EventContextImpl
