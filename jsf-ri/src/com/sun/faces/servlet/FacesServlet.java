package com.sun.faces.servlet;

import java.io.*;
import java.text.*;
import java.util.*;
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
            "faces.ObjectTable");
        if (objectTable == null) {
            try {
                otFactory = ObjectTableFactory.newInstance();
                objectTable = otFactory.newObjectTable();
            } catch (FacesException e) {
                throw new ServletException(e.getMessage());
            }
        }

        // Try to get the EventQueueFactory from the Object Table
        // (Global Scope).  If it doesn't exist, create it and stash it.
        //
        eqFactory = (EventQueueFactory)objectTable.get(
            "faces.EventQueueFactory");

        if (eqFactory == null) {
            eqFactory = EventQueueFactory.newInstance();
            objectTable.put(ObjectTable.GlobalScope, 
                "faces.EventQueueFactory", eqFactory);
        }

        // Try to get the RenderContextFactory from the Object Table
        // (Global Scope).  If it doesn't exist, create it and stash it.
        //
        rcFactory = (RenderContextFactory)objectTable.get(
            "faces.RenderContextFactory");

        if (rcFactory == null) {
            rcFactory = RenderContextFactory.newInstance();
            objectTable.put(ObjectTable.GlobalScope, 
                "faces.RenderContextFactory", rcFactory);
        }

        // Store the Object Table in the servlet context 
        // (application scope).
        //
        servletContext.setAttribute("faces.ObjectTable", objectTable);
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
            getAttribute("faces.ObjectTable");
        Assert.assert_it(null != objectTable); 

        // Attempt to get a render context from the object table
        // for the current session.  If one doesn't exist, create one.
        //
        rc = (RenderContext)objectTable.get(
            ObjectTable.SessionScope, "faces.RenderContext");

        if (rc == null) {
            rcFactory = (RenderContextFactory)objectTable.get(
                ObjectTable.GlobalScope, "faces.RenderContextFactory");

            Assert.assert_it(null != rcFactory);
            try {
                rc = rcFactory.newRenderContext(req);
            } catch (FacesException e) {
                throw new ServletException(e.getMessage());
            }
            objectTable.put(req.getSession(), "faces.RenderContext", rc);
        }

        // Attempt to get an event queue from the object table
        // for the current session.  If one doesn't exist, create one.
        //
        eq = (EventQueue)objectTable.get(
            ObjectTable.SessionScope, "faces.EventQueue");
        if (eq == null) {
            eqFactory = (EventQueueFactory)objectTable.get(
                ObjectTable.GlobalScope, "faces.EventQueueFactory");
            Assert.assert_it(null != eqFactory);
            try {
                eq = eqFactory.newEventQueue();
            } catch (FacesException e) {
                throw new ServletException(e.getMessage());
            }
            objectTable.put(req.getSession(), "faces.EventQueue", eq);
        }

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

        String forwardUrl = "/Login.jsp";

        getServletContext().getRequestDispatcher(res.encodeURL(forwardUrl)).
            forward(req, res);        
    }

}

