/*
 * $Id: ProcessEventsLifecycleStage.java,v 1.2 2002/03/15 23:29:48 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// ProcessEventsLifecycleStage.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesContext;
import javax.faces.RenderContext;
import javax.faces.EventContext;
import javax.faces.TreeNavigator;
import javax.faces.LifecycleStage;
import javax.faces.RenderKit;
import javax.faces.FacesException;
import javax.faces.EventQueue;
import javax.faces.EventDispatcher;
import javax.faces.NavigationHandler;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.EventObject;
import java.io.IOException;

import com.sun.faces.URIChangeRequestWrapper;
import com.sun.faces.util.Util;

/**
 *
 *  <B>ProcessEventsLifecycleStage</B> 
 *
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * LifecycleDriverImpl.
 *
 * @version $Id: ProcessEventsLifecycleStage.java,v 1.2 2002/03/15 23:29:48 eburns Exp $
 * 
 * @see	com.sun.faces.lifecycle.LifecycleDriverImpl
 *
 */

public class ProcessEventsLifecycleStage extends GenericLifecycleStage
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

protected LifecycleDriverImpl lifecycleDriver = null;

//
// Constructors and Initializers    
//

public ProcessEventsLifecycleStage(LifecycleDriverImpl newDriver, 
				   String newName)
{
    super(newDriver, newName);
}

//
// Class methods
//

//
// General Methods
//

/**
 * Navigates to the specified URL specifed by the targetPath
 * based on targetAction attribute
 * @return true if we navigated away from the current page
 */

private boolean doNavigation(EventContext eventContext) 
    throws ServletException, IOException {
    boolean navigate = false;
    HttpServletRequest req = (HttpServletRequest) eventContext.getRequest();
    HttpServletResponse res = (HttpServletResponse) eventContext.getResponse();
    
    NavigationHandler nh = eventContext.getNavigationHandler();
    // navigationHandler will be null if NavigationMap does not
    // exist. 
    if ( nh == null ) {
	return navigate;
    }
    int targetAction = nh.getTargetAction();
    String targetPath = nh.getTargetPath();
    // if targetAction is pass or undefined, continue with rendering
    // without navigation.
    if ( targetPath != null && (targetAction != NavigationHandler.UNDEFINED ||
				targetAction != NavigationHandler.PASS)) {
	navigate = true;
	
	if ( targetAction == NavigationHandler.FORWARD) {
	    HttpServletRequest request;
	    String newRequestURI = req.getContextPath() + "/" + 
		targetPath;
	    request = new URIChangeRequestWrapper(req, newRequestURI);
	    try {
		RequestDispatcher reqD =
		    request.getRequestDispatcher(targetPath);
		reqD.forward(request, res);
	    } catch ( IllegalStateException ie ) {
		throw new ServletException("couldn't forward to " +
					   targetPath + ie.getMessage() );
	    }
	} else if ( targetAction == NavigationHandler.REDIRECT) {
	    try {
		// PENDING ( visvan ) should URL be encoded ?
		res.sendRedirect(res.encodeRedirectURL(targetPath));
	    } catch ( IOException ioe ) {
		throw new ServletException("couldn't redirect to " +
					   targetPath +  ioe.getMessage() );
	    } 
	}
    }
    return navigate;
}

// 
// Methods from LifecycleStage
//

/**


*/

public boolean execute(FacesContext ctx, TreeNavigator root) throws FacesException
{
    boolean result = false;
    RenderContext renderContext = ctx.getRenderContext();
    HttpServletRequest request = 
	(HttpServletRequest) renderContext.getRequest();

    // if there are no params, just continue to next stage.
    if (!Util.hasParameters(request)) {
	return true;
    }

    RenderKit renderKit = renderContext.getRenderKit();
    EventContext eventContext = ctx.getEventContext();
    EventQueue eq;
    Assert.assert_it(renderKit != null);

    //
    // queue events
    //
    renderKit.queueEvents(eventContext);

    //
    // dispatch events
    //
    eq = eventContext.getEventQueue();
    Assert.assert_it(null != eq);
    while (!eq.isEmpty()) {
	EventObject e = (EventObject) eq.getNext();
	try {
	    EventDispatcher src=eventContext.getEventDispatcher(e);
	    Assert.assert_it ( src != null );
	    src.dispatch ( e );
	} catch ( Throwable fe ) {
	    fe.printStackTrace();
	    // abort processing events and lookup the navigation
	    // handler to find out where to go next.
	    // PENDING ( visvan ) should eventQueue be emptied here ?
	    eq.clear();
	    break;
	}
	eq.remove(e);
    } 

    //
    // do navigation
    //
    try {
	result = !doNavigation(eventContext);
    }
    catch (Throwable e) {
	throw new FacesException(e.getMessage());
    }
    return result;
}

// The testcase for this class is TestLifecycleDriverImpl.java 


} // end of class ProcessEventsLifecycleStage
