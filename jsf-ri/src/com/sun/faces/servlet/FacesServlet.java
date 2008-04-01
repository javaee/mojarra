/*
 * $Id: FacesServlet.java,v 1.11 2002/01/10 22:20:10 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.Constants;
import javax.faces.EventQueue;
import javax.faces.EventQueueFactory;
import javax.faces.FacesException;
import javax.faces.ObjectManager;
import com.sun.faces.ObjectManagerFactory;
import javax.faces.RenderContext;
import javax.faces.RenderContextFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.faces.RenderKit;
import javax.faces.FacesEvent;
import javax.faces.EventDispatcher;
import javax.faces.EventDispatcherFactory;
import java.util.EventObject;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.util.Util;

public class FacesServlet extends HttpServlet {

    //
    // Helper methods
    //



    /**

    *  Write out some HTML describing the exception.  

    */


    private void outputException(HttpServletResponse resp, 
				 Throwable exOut) {
	ParameterCheck.nonNull(resp);

	resp.setContentType("text/html; charset=ISO-8859-4");
	resp.setStatus(HttpServletResponse.SC_CONFLICT);
	try {
	    PrintWriter writer = resp.getWriter();
	    writer.print("<HTML>\n" +
			 "<HEAD><TITLE>" + exOut.toString() + "</TITLE></HEAD>\n" +
			 "<BODY>\n" +
			 "<H1>" + exOut.toString() + "</H1>\n" +
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

    
    //
    // Methods from HttpServlet and superclasses
    //

    /**
     * PRECONDITION: Nothing, this is to be called at the very beginning of
     * the Faces app instance.
     *
     * POSTCONDITION: ObjectManager instance created using Factory.  Instance
     * put in ServletContext.  ObjectManager global scope contains
     * RenderContextFactory, EventQueueFactory.
     *
     * @param config The Servlet configuration object which contains
     *        parameters for this servlet.
     *
     * @exception ServletException If an exception occured during the 
     *         creation of the object table factory or object table.
     */
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
	
	// Uncomment this to cause the program to hang forever, until
	// you go in the debugger and un-hang it.
	// com.sun.faces.util.DebugUtil.waitForDebugger();

        ObjectManager objectManager;
        ObjectManagerFactory omFactory;
        EventQueueFactory eqFactory;
        RenderContextFactory rcFactory;
        EventDispatcherFactory edFactory;

        ServletContext servletContext = getServletContext();
        Assert.assert_it(null != servletContext);

	// Step 1: Create the singleton ObjectManager instance and put it
	// in the servletContext.

        objectManager = (ObjectManager) servletContext.getAttribute(
            Constants.REF_OBJECTMANAGER);
	// The objectManager must not exist at this point.  It is an error
	// if it does exist.
	Assert.assert_it(null == objectManager);

	// create the ObjectManager
	try {
	    omFactory = ObjectManagerFactory.newInstance();
	    objectManager = omFactory.newObjectManager();
	} catch (FacesException e) {
	    throw new ServletException(e.getMessage());
	}
        Assert.assert_it(null != objectManager);

        // Store the ObjectManager in the servlet context 
        // (application scope).
        //
        servletContext.setAttribute(Constants.REF_OBJECTMANAGER, objectManager);

	// Step 2: Create the EventQueueFactory and put it in the
	// ObjectManager in Global Scope.

        eqFactory = (EventQueueFactory)objectManager.get(
            Constants.REF_EVENTQUEUEFACTORY);

	// The EventQueueFactory must not exist at this point.  It is an
	// error if it does exist.
	Assert.assert_it(null == eqFactory);

	eqFactory = EventQueueFactory.newInstance();
        Assert.assert_it(null != eqFactory);
	objectManager.put(ObjectManager.GlobalScope,
			Constants.REF_EVENTQUEUEFACTORY, eqFactory);

	// Step 3: Create the RenderContextFactory and put it in the
	// ObjectManager in GlobalScope

        rcFactory = (RenderContextFactory)objectManager.get(
            Constants.REF_RENDERCONTEXTFACTORY);
	// The RenderContextFactory must not exist at this point.  It is an
	// error if it does exist.
	Assert.assert_it(null == rcFactory);

	rcFactory = RenderContextFactory.newInstance();
        Assert.assert_it(null != rcFactory);
	objectManager.put(ObjectManager.GlobalScope,
			Constants.REF_RENDERCONTEXTFACTORY, rcFactory);

        // Step 4: Create the EventDispatcherFactory and put it in the
        // ObjectManager in GlobalScope

        edFactory = (EventDispatcherFactory)objectManager.get(
            Constants.REF_EVENTDISPATCHERFACTORY);
        // The EventDispatcherFactory must not exist at this point.  It is an
        // error if it does exist.
        Assert.assert_it(null == edFactory);

        edFactory = EventDispatcherFactory.newInstance();
        Assert.assert_it(null != edFactory);
        objectManager.put(ObjectManager.GlobalScope,
                        Constants.REF_EVENTDISPATCHERFACTORY, edFactory);
    }

    /**
       
    * PRECONDITION: The faces app is to be taken out of service

    * POSTCONDITION: ObjectManager is de-populated.  Any attributes we
    * put in the ServletContext are removed.
    
    */

    public void destroy() {
	// PENDING(edburns): clear out the ObjectManager
	getServletContext().removeAttribute(Constants.REF_OBJECTMANAGER);
    }

    /**
     * Process an HTTP "POST" request.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doPost(HttpServletRequest req,
                      HttpServletResponse res)
        throws IOException, ServletException {
        processRequest(req, res);
    }

    /**
     * Process an HTTP "GET" request.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doGet(HttpServletRequest req,
                      HttpServletResponse res)
        throws IOException, ServletException {
        processRequest(req, res);
    }

    /** 

    * We override this so we can exit the scope for this request.

    * PRECONDITION: init() has been called

    * POSTCONDITION: The Scope for this request has been exited.

    */ 
    public void service(HttpServletRequest req,
			HttpServletResponse res) {
	// We set this attr here so that when ObjectManager.get() comes
	// around for this request, we pull it out and use it as the
	// value for the scopeKey.  This is necessary because various
	// entities modify the HttpServletRequest instance so it's not
	// suitable for being used as a key
	req.setAttribute(Constants.REF_REQUESTINSTANCE, req);

	try {
	    super.service(req, res);
	}
	catch (Exception e) {
	    System.out.println("Caught exception calling super.service: " + 
			       e.getMessage() + " " + e);
	    e.printStackTrace();
	} 
	
	// exit the scope for this request
	ObjectManager objectManager;
        objectManager = (ObjectManager) getServletContext().
	    getAttribute(Constants.REF_OBJECTMANAGER);
	objectManager.exit(req);
    } 


    /**
     * Process an HTTP request.
     *
     * PRECONDITION: ObjectManager exists in ServletContext.  
     * ObjectManager global scope contains RenderContextFactory, 
     * EventQueueFactory.
     *
     * POSTCONDITION: ObjectManager contains RenderContext and
     * EventQueue instances in session scope.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    protected void processRequest(HttpServletRequest req,
                      HttpServletResponse res)
        throws IOException, ServletException {

        RenderContext rc;
        RenderContextFactory rcFactory;
        EventQueue eq;
        EventQueueFactory eqFactory;
        EventDispatcherFactory edFactory;

	HttpSession thisSession = req.getSession();

        ObjectManager objectManager = (ObjectManager)getServletContext().
            getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it(null != objectManager); 

        // Attempt to get a render context from the object table
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
        }

        // Attempt to get an event queue from the object table
        // for the current session.  If one doesn't exist, create one.
         
        eq = (EventQueue)objectManager.get(thisSession, 
					 Constants.REF_EVENTQUEUE);
        if (eq == null) {
            eqFactory = (EventQueueFactory)
		objectManager.get(Constants.REF_EVENTQUEUEFACTORY);
            Assert.assert_it(null != eqFactory);
            try {
                eq = eqFactory.newEventQueue();
            } catch (FacesException e) {
                throw new ServletException(e.getMessage());
            }
	    Assert.assert_it(null != eq); 
            objectManager.put(thisSession, Constants.REF_EVENTQUEUE, eq);
        }

        // If there are no events in the queue, simply forward to the target.
        
        RenderKit rk = rc.getRenderKit();
        Assert.assert_it(rk != null);
	
	// Only check for a token and process events if this request has
	// parameters.
	if (Util.hasParameters(req)) {
	    // if we have a transaction token, see if it is valid
	    if (!Util.isTokenValid(req)) {
		// PENDING(edburns): look for servlet config param
		// pageFlowErrorPage if found, redirect to it, else spit
		// out simple page and return.
		ServletException e = new ServletException("Token not valid.  Perhaps your session timed out?");
		outputException(res, e);
		throw e;
	    }
	    Util.resetToken(req);
	
	    rk.queueEvents(req, eq);
	    
	    if (!eq.isEmpty()) {
		edFactory = (EventDispatcherFactory)
		    objectManager.get(Constants.REF_EVENTDISPATCHERFACTORY);
		Assert.assert_it(edFactory != null );
		
		while (!eq.isEmpty()) {
		    EventObject e = (EventObject) eq.getNext();
		    try {
			
			EventDispatcher d = edFactory.newEventDispatcher(e);
			Assert.assert_it ( d != null );
			
			d.dispatch(req, res,e);
		    } catch ( FacesException fe ) {
			throw new ServletException("Error while dispatching events " +
						   fe.getMessage() );
		    }    
		    eq.remove(e);
		}    
	    }
	}

	// Make sure the client does not cache the response.

	// PENDING(edburns): not sure if this is necessary in all cases.
	// We probably don't need to specify no cache if the request to
	// which we're forwarding is not a faces page.
	res.addHeader("Pragma:", "No-cache");
	res.addHeader("Cache-control:", "no-cache");
	res.addHeader("Expires:", "1");
  
        // Get the last portion of the request and use it as an index 
        // into mappings configuration table to get the target url.
        //  
        String forwardUrl = req.getPathInfo();

        // "getPathInfo" will return path information after the 
        // servlet address but before query data. 
        //     Example (assume "faces" maps to our servlet):
        //     /faces/survey.jsp --> /survey.jsp
        //     /faces/foo/survey.jsp --> /foo/survey.jsp
        // Use that url as the target.
        System.out.println("FORWARDURL:"+ forwardUrl);
        RequestDispatcher reqD = 
                getServletContext().getRequestDispatcher(forwardUrl);
        try {
            reqD.forward(req, res); 
        } catch ( IllegalStateException ie ) {
            // PENDING ( visvan ) skip this exception which is occuring
            // because of two forwards one in CommandDispatcher if there
            // was a Command and one in FacesServlet. Revisit later.
        }        
    }    

}
