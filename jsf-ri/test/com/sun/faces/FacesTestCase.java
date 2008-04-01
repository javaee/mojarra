/*
 * $Id: FacesTestCase.java,v 1.6 2002/04/05 19:41:20 jvisvanathan Exp $
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

import org.apache.cactus.ServletTestCase;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.faces.ObjectManager;
import javax.faces.FacesContextFactory;
import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.FacesContext;
import com.sun.faces.Page;

/**
 *

 *  <B>FacesTestCase</B> Is a base class for testcases that test Faces.
 *  It has setUp() and tearDown() methods that make the environment for
 *  faces testing more useful.  Extend this testcase to test faces.

 *
 * @version $Id: FacesTestCase.java,v 1.6 2002/04/05 19:41:20 jvisvanathan Exp $
 * 
 * @see	setUp
 * @see	tearDown
 *
 */

public abstract class FacesTestCase extends ServletTestCase
{
//
// Protected Constants
//

//
// Class Variables
//

public static class FacesTestCasePage extends Page
{

public FacesTestCasePage() {}

public void _jspService(HttpServletRequest request,
			HttpServletResponse response) 
    throws ServletException, IOException { }

public FacesContext testCallCreateFacesContext(ObjectManager objectManager, 
					       HttpServletRequest req, 
					       HttpServletResponse res,
                                             ServletContext sc ) throws ServletException
{
    return createFacesContext(objectManager, req, res, sc);
}
} // end of class FacesTestCasePage

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

public FacesTestCasePage page = null;
public ObjectManager objectManager = null;
public FacesContext facesContext = null;

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

* PRECONDITION: the page has been created and init()ed <P>

* POSTCONDITION: Some of the stuff that happens in service() is
* completed:  <P>

* facesContext is a valid facesContext for this TestCase's request.
* the REQUESTINSTANCE has been set in the Request's attr set.

*/

public void simulateService() {
    // Just used to pass into testCallCreateFacesContext
    ObjectManager tempObjectManager = null;

    request.setAttribute(Constants.REF_REQUESTINSTANCE, request);
    
    tempObjectManager = (ObjectManager)
        config.getServletContext().getAttribute(Constants.REF_OBJECTMANAGER);
    assertTrue(null != tempObjectManager);

    try {
        ServletContext sc = config.getServletContext();
	facesContext = page.testCallCreateFacesContext(tempObjectManager,
						       request, response, sc);
	assertTrue(null != facesContext);
	
    }
    catch (Throwable e) {
	e.printStackTrace();
	assertTrue(false);
    }
    
}

/**
   
* PRECONDITION: simulateService has been called <P>

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
    // PENDING(edburns): Hans doesn't want this in the public API
    // just cast to our implementation for now
    ((com.sun.faces.ObjectManagerImpl)objectManager).exit(session);

    session.removeAttribute(Constants.REF_SESSIONINSTANCE);
    session.getServletContext().removeAttribute(session.getId());
}

//
// Methods from TestCase
//

/**

* PRECONDITION: none

* POSTCONDITION: filter is a FacesFilter, objectManager is a valid
* ObjectManager, facesContext is a valid FacesContext

*/

public void setUp() {
    // Uncomment this to cause the debugger to wait at this point for
    // you to attach to it.
    // com.sun.faces.util.DebugUtil.waitForDebugger();
    page = new FacesTestCasePage();

    // this creates the ObjectManager, puts it in the ServletContext,
    // and populates the ObjectManager with several key items.
    try {
	page.init(config);
    }
    catch (ServletException e) {
	assertTrue(false);
    }

    simulateSessionCreated();
    simulateService();

    objectManager = facesContext.getObjectManager();

}


public void tearDown() {
    page.destroy();
    simulateSessionDestroyed();
    objectManager = null;
    facesContext = null;
    page = null;
}


} // end of class FacesTestCase
