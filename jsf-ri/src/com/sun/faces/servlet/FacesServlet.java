/*
 * $Id: FacesServlet.java,v 1.3 2001/11/29 01:54:35 rogerk Exp $
 *
 * Copyright 2000-2001 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package com.sun.faces.servlet;

import java.io.IOException;
import javax.faces.Constants;
import javax.faces.EventQueue;
import javax.faces.EventQueueFactory;
import javax.faces.FacesException;
import javax.faces.ObjectTable;
import javax.faces.ObjectTableFactory;
import javax.faces.RenderContext;
import javax.faces.RenderContextFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;


public class FacesServlet extends HttpServlet {

    /**
     * PRECONDITION: Nothing, this is to be called at the very beginning of
     * the Faces app instance.
     *
     * POSTCONDITION: ObjectTable instance created using Factory.  Instance
     * put in ServletContext.  ObjectTable global scope contains
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

        ObjectTable objectTable;
        ObjectTableFactory otFactory;
        EventQueueFactory eqFactory;
        RenderContextFactory rcFactory;

        ServletContext servletContext = getServletContext();
        Assert.assert_it(null != servletContext);

// PENDING(rogerk): standardize the name of the ObjectTable attribute
// names.

        // Try to get the ObjectTable from the servlet context.
        // If it doesn't exist, create one and store it in the
        // servlet context.
        //
        objectTable = (ObjectTable) servletContext.getAttribute(
            Constants.REF_OBJECTTABLE);
        if (objectTable == null) {
            try {
                otFactory = ObjectTableFactory.newInstance();
                objectTable = otFactory.newObjectTable();
            } catch (FacesException e) {
                throw new ServletException(e.getMessage());
            }
        }
        Assert.assert_it(null != objectTable);

        // Try to get the EventQueueFactory from the Object Table
        // (Global Scope).  If it doesn't exist, create it and stash it.
        //
        eqFactory = (EventQueueFactory)objectTable.get(
            Constants.REF_EVENTQUEUEFACTORY);

        if (eqFactory == null) {
            eqFactory = EventQueueFactory.newInstance();
            objectTable.put(ObjectTable.GlobalScope, 
                Constants.REF_EVENTQUEUEFACTORY, eqFactory);
        }
        Assert.assert_it(null != eqFactory);

        // Try to get the RenderContextFactory from the Object Table
        // (Global Scope).  If it doesn't exist, create it and stash it.
        //
        rcFactory = (RenderContextFactory)objectTable.get(
            Constants.REF_RENDERCONTEXTFACTORY);

        if (rcFactory == null) {
            rcFactory = RenderContextFactory.newInstance();
            objectTable.put(ObjectTable.GlobalScope, 
                Constants.REF_RENDERCONTEXTFACTORY, rcFactory);
        }
        Assert.assert_it(null != rcFactory);

        // Store the Object Table in the servlet context 
        // (application scope).
        //
        servletContext.setAttribute(Constants.REF_OBJECTTABLE, objectTable);
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
     * Process an HTTP request.
     *
     * PRECONDITION: ObjectTable exists in ServletContext.  
     * ObjectTable global scope contains RenderContextFactory, 
     * EventQueueFactory.
     *
     * POSTCONDITION: ObjectTable contains RenderContext and
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

        ObjectTable objectTable = (ObjectTable)getServletContext().
            getAttribute(Constants.REF_OBJECTTABLE);
        Assert.assert_it(null != objectTable); 

        // Attempt to get a render context from the object table
        // for the current session.  If one doesn't exist, create one.
        //
        rc = (RenderContext)objectTable.get(
            ObjectTable.SessionScope, Constants.REF_RENDERCONTEXT);

        if (rc == null) {
            rcFactory = (RenderContextFactory)objectTable.get(
                ObjectTable.GlobalScope, 
                Constants.REF_RENDERCONTEXTFACTORY);

            Assert.assert_it(null != rcFactory);
            try {
                rc = rcFactory.newRenderContext(req);
            } catch (FacesException e) {
                throw new ServletException(e.getMessage());
            }
            objectTable.put(req.getSession(), 
                Constants.REF_RENDERCONTEXT, rc);
        }
        Assert.assert_it(null != rc); 

        // Attempt to get an event queue from the object table
        // for the current session.  If one doesn't exist, create one.
        //
        eq = (EventQueue)objectTable.get(
            ObjectTable.SessionScope, Constants.REF_EVENTQUEUE);
        if (eq == null) {
            eqFactory = (EventQueueFactory)objectTable.get(
                ObjectTable.GlobalScope, Constants.REF_EVENTQUEUEFACTORY);
            Assert.assert_it(null != eqFactory);
            try {
                eq = eqFactory.newEventQueue();
            } catch (FacesException e) {
                throw new ServletException(e.getMessage());
            }
            objectTable.put(req.getSession(), Constants.REF_EVENTQUEUE, eq);
        }
        Assert.assert_it(null != eq); 

// PENDING (rogerk) plug in event handling helper class
// invocations here.
// If there are no events in the queue, simply forward to the target.
//
// eq = (EventQueue)objectTable.get(
//  ObjectTable.SessionScope, "faces.EventQueue");
// RenderKit rk = rc.getRenderKit();
// rk.queueEvents(req, eq);
// if (eq.isEmpty()) {
//      

        // Get the last portion of the request and use it as an index 
        // into mappings configuration table to get the target url.
        //  
        String selectUrl = null;
        String requestInfo = req.getRequestURI();
        int lastPathSeperator = requestInfo.lastIndexOf("/") + 1;
        if (lastPathSeperator != -1) { 
            selectUrl = requestInfo.substring(lastPathSeperator,
                requestInfo.length()); 
        }

//PENDING (rogerk) use selectUrl to determine where to forward
// (from action mappings table)
// Use that url as the target.

        String forwardUrl = "/"+selectUrl;
System.out.println("FORWARDURL:"+forwardUrl);

        RequestDispatcher reqD = 
            getServletContext().getRequestDispatcher(res.encodeURL(forwardUrl));
        reqD.forward(req, res);        

    }

}
