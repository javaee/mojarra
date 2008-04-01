/*
 * $Id: TestLifecycleDriverImpl.java,v 1.2 2002/03/15 20:58:04 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestLifecycleDriverImpl.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.sun.faces.FacesTestCase;
import com.sun.faces.Page;
import com.sun.faces.ParamBlockingRequestWrapper;

import javax.faces.Constants;
import javax.faces.ObjectManager;
import javax.faces.TreeNavigator;
import javax.faces.MessageFactory;

import javax.faces.EventQueueFactory;
import javax.faces.RenderContextFactory;
import javax.faces.FacesContext;
import com.sun.faces.EventContextFactory;
import com.sun.faces.ObjectAccessorFactory;
import com.sun.faces.NavigationHandlerFactory;
import com.sun.faces.lifecycle.RenderWrapper;
import javax.faces.ConverterManager;
import com.sun.faces.treebuilder.TreeEngine;
/**
 *
 *  Exercise LifecycleDriverImpl
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestLifecycleDriverImpl.java,v 1.2 2002/03/15 20:58:04 jvisvanathan Exp $
 * 
 * @see	javax.faces.TreeNavigator
 * @see	com.sun.faces.TreeEngine
 *
 */

public class TestLifecycleDriverImpl extends FacesTestCase implements RenderWrapper
{
//
// Protected Constants
//

public static final String TEST_URI = "/Faces_Basic.jsp";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

protected boolean commenceRenderingCalled = false;

// Relationship Instance Variables

protected LifecycleDriverImpl lifecycle = null;
//
// Constructors and Initializers    
//

    public TestLifecycleDriverImpl() {super("TestLifecycleDriverImpl");}
    public TestLifecycleDriverImpl(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

public void setUp() {
    lifecycle = new LifecycleDriverImpl();
    simulateSessionCreated();
}

public void tearDown() {
    lifecycle = null;
    config.getServletContext().removeAttribute(Constants.REF_OBJECTMANAGER);
    simulateSessionDestroyed();
}

//
// Methods from RenderWrapper
//

/**

* Called from the render lifecycle stage

*/ 

public void commenceRendering(ServletRequest req, ServletResponse res) throws ServletException, IOException {
    commenceRenderingCalled = true;
}

//
// General Methods
//

public void testInit() 
{
    EventQueueFactory eqf = null;
    RenderContextFactory rcf = null;
    EventContextFactory ecf = null;
    ObjectAccessorFactory oaf = null;
    NavigationHandlerFactory nhf = null;
    ConverterManager cm = null;
    TreeEngine te = null;
    MessageFactory mf = null;
 
    try {
	lifecycle.init(config.getServletContext());
    }
    catch (Throwable e) {
	System.out.println("Caught Throwable: " + e);
	assertTrue(false);
    }

    System.out.print("Testing that init postconditions are met: ");
    objectManager = (ObjectManager) 
	config.getServletContext().getAttribute(Constants.REF_OBJECTMANAGER);
    assertTrue(null != objectManager);

    eqf = (EventQueueFactory) objectManager.get(Constants.REF_EVENTQUEUEFACTORY);
    assertTrue(null != eqf);

    rcf = (RenderContextFactory) objectManager.get(Constants.REF_RENDERCONTEXTFACTORY);
    assertTrue(null != rcf);

    ecf = (EventContextFactory) objectManager.get(Constants.REF_EVENTCONTEXTFACTORY);
    assertTrue(null != ecf);

    oaf = (ObjectAccessorFactory) objectManager.get(Constants.REF_OBJECTACCESSORFACTORY);
    assertTrue(null != oaf);

    nhf = (NavigationHandlerFactory) objectManager.get(Constants.REF_NAVIGATIONHANDLERFACTORY);
    assertTrue(null != nhf);

    cm = (ConverterManager) objectManager.get(Constants.REF_CONVERTERMANAGER);
    assertTrue(null != cm);

    te = (TreeEngine) objectManager.get(Constants.REF_TREEENGINE);
    assertTrue(null != te);
    System.out.println(" true.");

    mf = (MessageFactory) objectManager.get(com.sun.faces.MessageListImpl.DEFAULT_MESSAGE_FACTORY_ID);
    assertTrue(null != mf);
}

public void beginLifecycle(WebRequest theRequest) 
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    System.out.println("lifecycle test url: " + theRequest.toString());
}

public void testLifecycle()
{
    FacesContext facesContext  = null;
    commenceRenderingCalled = false;
    Page page = null;
    TreeNavigator treeNav = null;
    HttpServletRequest wrapped = new ParamBlockingRequestWrapper(request);

    try {
	lifecycle.init(config.getServletContext());
    }
    catch (Throwable e) {
	System.out.println("Caught Throwable: " + e);
	e.printStackTrace();
	assertTrue(false);
    }
    wrapped.setAttribute(Constants.REF_REQUESTINSTANCE, wrapped);

    objectManager = 
       (ObjectManager)config.getServletContext().getAttribute(Constants.REF_OBJECTMANAGER);

    // Put the RenderWrapper in the OM request scope
    objectManager.put(wrapped, Constants.REF_RENDERWRAPPER, this);

    page = new Page() {
	    public void _jspService(HttpServletRequest request,
				    HttpServletResponse response) 
		throws ServletException, IOException { }
	};

    try {
	facesContext = page.createFacesContext(objectManager, wrapped, 
					       response);
	assertTrue(null != facesContext);

	treeNav = lifecycle.wireUp(facesContext);
	assertTrue(null != treeNav);
	
	// This causes our _jspService() to be called via the
	// RenderWrapper interface.
	lifecycle.executeLifecycle(facesContext, treeNav);
    }
    catch (Throwable e) {
	e.printStackTrace();
	assertTrue(false);
	return;
    }

    assertTrue(commenceRenderingCalled);
}


} // end of class TestLifecycleDriverImpl
