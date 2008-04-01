/*
 * $Id: FacesTestCase.java,v 1.3 2002/01/15 02:17:32 rogerk Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FacesTestCase.java

package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import org.apache.cactus.FilterTestCase;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import javax.faces.ObjectManager;
import javax.faces.RenderContext;
import javax.faces.RenderContextFactory;
import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.EventContext;

import com.sun.faces.servlet.FacesFilter;
import com.sun.faces.EventContextFactory;

/**
 *

 *  <B>FacesTestCase</B> Is a base class for testcases that test Faces.
 *  It has setUp() and tearDown() methods that make the environment for
 *  faces testing more useful.  Extend this testcase to test faces.

 *
 * @version $Id: FacesTestCase.java,v 1.3 2002/01/15 02:17:32 rogerk Exp $
 * 
 * @see	setUp
 * @see	tearDown
 *
 */

public abstract class FacesTestCase extends FilterTestCase
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

public FacesFilter filter = null;
public ObjectManager objectManager = null;
public RenderContext renderContext = null;
public EventContext eventContext = null;

//
// Constructors and Initializers    
//

protected FacesTestCase(String name)
{
    super(name);
}

//
// Class methods
//

//
// General Methods
//

/**

* PRECONDITION: the filter has been created and init()ed <P>

* POSTCONDITION: Some of the stuff that happens in doFilter() is
* completed:  <P>

* renderContext is a valid renderContext for this TestCase's request.
* eventContext is a valid eventContext for this TestCase's request.

*/

public void simulateDoFilter() {

    RenderContextFactory factory = null;
    EventContextFactory ecFactory = null;
    // Just used to get the RenderContextFactory
    ObjectManager tempObjectManager = null;
    
    tempObjectManager = (ObjectManager)
        config.getServletContext().getAttribute(Constants.REF_OBJECTMANAGER);
    assertTrue(null != tempObjectManager);
    
    factory = (RenderContextFactory)
        tempObjectManager.get(Constants.REF_RENDERCONTEXTFACTORY);
    assertTrue(null != factory);

    ecFactory = (EventContextFactory)
        tempObjectManager.get(Constants.REF_EVENTCONTEXTFACTORY);
    assertTrue(null!= ecFactory);
    
    try {
        renderContext = factory.newRenderContext(request);
        eventContext = ecFactory.newEventContext(renderContext, request, response);
    } catch (FacesException e) {
        System.out.println("TestObjectManager.setUp: exception: " + 
            e.getMessage());
	    e.printStackTrace();
	    assertTrue(false);
    }
}

/**
   
* PRECONDITION: simulateDoFilter has been called <P>

* POSTCONDITION: the action that takes place in the sessionCreated of
* our SessionListener has been called: <P>

* the necessary attributes to allow the ObjectManager to function
* correctly have been installed. <P>

*/

public void simulateSessionCreated() {
    request.setAttribute(Constants.REF_REQUESTINSTANCE, request);
    HttpSession session = request.getSession();

    session.setAttribute(Constants.REF_SESSIONINSTANCE, session.getId());
    session.getServletContext().setAttribute(session.getId(), session.getId());
}

/**

* PRECONDITION: we're ready to destroy this session <P>

* POSTCONDITION: the session is no longer usable for us <P>

* The attributes installed in simulateSessionCreated are removed.

*/

public void simulateSessionDestroyed() {
    HttpSession session = request.getSession();
    session.removeAttribute(Constants.REF_SESSIONINSTANCE);
    session.getServletContext().removeAttribute(session.getId());
}

//
// Methods from TestCase
//

/**

* PRECONDITION: none

* POSTCONDITION: filter is a FacesFilter, objectManager is a valid
* ObjectManager, renderContext is a valid RenderContext, eventContext is
* a valid EventContext.

*/

public void setUp() {
    filter = new FacesFilter();
    // this creates the ObjectManager, puts it in the ServletContext,
    // and populates the ObjectManager with several key items.
    try {
	filter.init(config);
    }
    catch (ServletException e) {
	assertTrue(false);
    }

    simulateDoFilter();

    objectManager = renderContext.getObjectManager();

    simulateSessionCreated();
}


public void tearDown() {
    filter.destroy();
    simulateSessionDestroyed();
    objectManager = null;
    renderContext = null;
    filter = null;
}


} // end of class FacesTestCase
