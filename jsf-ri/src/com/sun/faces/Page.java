/*
 * $Id: Page.java,v 1.2 2002/03/15 23:29:47 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// Page.java

package com.sun.faces;

import java.io.IOException;
import java.io.PrintWriter;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.servlet.jsp.HttpJspPage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.faces.Constants;
import javax.faces.ObjectManager;
import javax.faces.RenderContext;
import javax.faces.RenderContextFactory;
import javax.faces.EventContext;
import javax.faces.FacesContext;
import javax.faces.FacesException;
import javax.faces.TreeNavigator;
import javax.faces.UIPage;

import com.sun.faces.lifecycle.LifecycleDriverImpl;
import com.sun.faces.lifecycle.RenderWrapper;
import com.sun.faces.EventContextFactory;
import com.sun.faces.util.Util;

/**
 *
 *  <B>Page</B> is the class that Faces page authors must specify as the
 *  <B>extends</B> value in their pages.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: Page.java,v 1.2 2002/03/15 23:29:47 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

abstract public class Page extends Object implements HttpJspPage, RenderWrapper
{
//
// Protected Constants
//

protected static final String REF_LIFECYCLE = "faces.LifecycleDriver";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

private ServletConfig servletConfig = null;

private LifecycleDriverImpl lifecycle = null;

//
// Constructors and Initializers    
//

public Page()
{
    super();
}

//
// Class methods
//

//
// General Methods
//

private void outputException(HttpServletResponse resp,
			     Throwable exOut) {
    ParameterCheck.nonNull(resp);
    
    resp.setContentType("text/html; charset=ISO-8859-4");
    resp.setStatus(HttpServletResponse.SC_CONFLICT);
    try {
	PrintWriter writer = resp.getWriter();
	writer.print("<HTML>\n" +
		     "<HEAD><TITLE>" + exOut.toString() + "</TITLE></HEAD>\n" +
		     "<BODY>\n" + "<H1>" + exOut.toString() + "</H1>\n" +
		     "<CODE>\n");
	exOut.printStackTrace(writer);
	writer.print("</CODE>\n" +
		     "</BODY>\n" +
		     "</HTML>\n");
	resp.flushBuffer();
    }
    catch (Exception e) {
    }
}

protected FacesContext createFacesContext(ObjectManager objectManager, 
					  HttpServletRequest req, 
					  HttpServletResponse res) throws ServletException {
    FacesContext result;
    RenderContext rc;
    EventContext eventContext;
    HttpSession thisSession = req.getSession();
    RenderContextFactory rcFactory;
    EventContextFactory ecFactory;
    
    // Attempt to get a render context from the object manager 
    // for the current session.  If one doesn't exist, create one.
    //
    rc = (RenderContext)objectManager.get(thisSession, 
					  Constants.REF_RENDERCONTEXT);
    if (rc == null) {
	rcFactory = (RenderContextFactory)
	    objectManager.get(Constants.REF_RENDERCONTEXTFACTORY);
	
	Assert.assert_it(null != rcFactory);
	try {
	    rc = rcFactory.newRenderContext(req);
	} catch (FacesException e) {
	    throw new ServletException(e.getMessage());
	}
	Assert.assert_it(null != rc);
	objectManager.put(thisSession,
			  Constants.REF_RENDERCONTEXT, rc);
    } else {
	// set the RenderContext to have the correct request
	
	// PENDING(edburns): not sure what the lifetime of
	// RenderContext is.  Here we re-use the RC by setting its
	// request and session.
	if (rc instanceof
	    com.sun.faces.renderkit.html_basic.HtmlBasicRenderContext) {
	    ((com.sun.faces.renderkit.html_basic.HtmlBasicRenderContext)rc).
		setRequest(req);
	}
    }

    // create the EventContext
    ecFactory = (EventContextFactory)
	objectManager.get(Constants.REF_EVENTCONTEXTFACTORY);
    Assert.assert_it(ecFactory != null );
    
    // eventContext is request scope meaning created per
    // request, hence need not be put in ObjectManager
    try {
	eventContext = ecFactory.newEventContext(rc, req, res);
    } catch ( FacesException fe ) {
	throw new ServletException("Cannot create EventContext " +
				   fe.getMessage() );
    }
    
    result = new FacesContext(rc, eventContext);
    return result;
}
    
//
// Methods from Servlet
// 

final public void init(ServletConfig config) throws ServletException {
    servletConfig = config;
    ServletContext ctx = servletConfig.getServletContext();

    if (null == lifecycle) {
	// try to get it from the ServletContext
	if (null == (lifecycle = (LifecycleDriverImpl) 
		     ctx.getAttribute(REF_LIFECYCLE))) {
	    // if that fails, create one
	    lifecycle = new LifecycleDriverImpl();
	    ctx.setAttribute(REF_LIFECYCLE, lifecycle);

	    // IMPORTANT: only do this once per lifecycle instance!
	    lifecycle.init(ctx);
	}
    }
    Assert.assert_it(null != lifecycle);

    jspInit();
}

final public ServletConfig getServletConfig() {
    return servletConfig;
}

public String getServletInfo() {
    return "Faces Page";
}

final public void destroy() {
    Assert.assert_it(null != lifecycle);

    lifecycle.destroy();
    ServletContext ctx = servletConfig.getServletContext();
    ctx.removeAttribute(REF_LIFECYCLE);


    jspDestroy();
}

final public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
    // casting exceptions will be raised if an internal error.
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    ServletContext servletContext = servletConfig.getServletContext();
    HttpSession thisSession = request.getSession();
    ObjectManager objectManager;
    FacesContext facesContext;
    TreeNavigator treeNav = null;

    Assert.assert_it(null != lifecycle);

    // We set this attr here so that when ObjectManager.get() comes
    // around for this request, we pull it out and use it as the
    // value for the scopeKey.  This is necessary because various
    // entities modify the HttpServletRequest instance so it's not
    // suitable for being used as a key
    //
    request.setAttribute(Constants.REF_REQUESTINSTANCE, request);

    // Get the object manager.

    objectManager = 
       (ObjectManager)servletContext.getAttribute(Constants.REF_OBJECTMANAGER);
    Assert.assert_it(null != objectManager);
    
    // Put the RenderWrapper in the OM request scope
    objectManager.put(request, Constants.REF_RENDERWRAPPER, this);
    
    facesContext = createFacesContext(objectManager, request, response);

    // Rather than this class extending UIPage, we create a new one each
    // time.  This prevents problems with knowing when it is safe to
    // remove the children.

    // PENDING(edburns): check for leaks.

    UIPage root = new UIPage();
    root.setId(Util.generateId());
    
    try {
	treeNav = lifecycle.wireUp(facesContext, root);
	Assert.assert_it(null != treeNav);
	
	// This causes our _jspService() to be called via the
	// RenderWrapper interface.
	lifecycle.executeLifecycle(facesContext, treeNav);
    }
    catch (Throwable e) {
	outputException(response, e);
	return;
    }

    // exit the scope for this request
    // PENDING(edburns): Hans doesn't want this in the public API
    // just cast to our implementation for now
    ((com.sun.faces.ObjectManagerImpl)objectManager).exit(req);
    
}

// 
// Methods from JspPage
// 

public void jspInit() {
}

public void jspDestroy() {
}

//
// Methods from HttpJspPage
//
    
abstract public void _jspService(HttpServletRequest request,
				 HttpServletResponse response) throws ServletException, IOException;

//
// Methods from RenderWrapper
//

/**

* Called from the render lifecycle stage

*/ 

public void commenceRendering(FacesContext ctx, 
			      TreeNavigator treeNav) throws ServletException, IOException {
    EventContext eventContext = ctx.getEventContext();
    HttpServletResponse response = (HttpServletResponse) eventContext.getResponse();
    HttpServletRequest request =(HttpServletRequest) eventContext.getRequest();

    // Beacuse the tree is rooted at UIPage, and there is no tag
    // that matches UIPage, we have to prime the treeNav here.
    treeNav.getNextStart();
    _jspService(request, response);
    // For completeness, we pull off the end.
    treeNav.getNextEnd();

}

} // end of class Page
