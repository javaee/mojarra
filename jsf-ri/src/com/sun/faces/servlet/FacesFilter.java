/*
 * $Id: FacesFilter.java,v 1.7 2002/04/05 19:41:17 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.EventObject;

import com.sun.faces.ObjectAccessorFactory;
import com.sun.faces.ObjectManagerFactory;
import com.sun.faces.NavigationHandlerFactory;
import com.sun.faces.util.Util;
import com.sun.faces.ConverterManagerFactory;

import javax.faces.Constants;
import javax.faces.EventDispatcher;
import javax.faces.EventQueue;
import javax.faces.EventQueueFactory;
import javax.faces.FacesEvent;
import javax.faces.FacesException;
import javax.faces.ObjectManager;
import javax.faces.FacesContext;
import javax.faces.FacesContextFactory;
import javax.faces.RenderKit;
import javax.faces.UICommand;
import javax.faces.UISelectOne;
import javax.faces.UITextEntry;
import javax.faces.UISelectBoolean;
import javax.faces.NavigationHandler;
import javax.faces.ConverterManager;
import javax.faces.FacesContextFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

public class FacesFilter implements Filter {

    /**
     * The filter configuration object we are associated with.  If this value
     * is null, this filter instance is not currently configured.
     */
    private FilterConfig filterConfig;

    /**
     * Take this filter out of service.
     */
    public void destroy() {
        this.filterConfig.getServletContext().removeAttribute(
            Constants.REF_OBJECTMANAGER);
	// PENDING(edburns): clear ObjectManager
        this.filterConfig = null;
    }

    /**
     * PRECONDITION: Nothing, this is to be called at the very beginning of
     * the Faces app instance.
     *
     * POSTCONDITION: ObjectManager instance created using Factory.  Instance
     * put in ServletContext.  ObjectManager global scope contains
     * FacesContextFactory, EventQueueFactory.
     *
     * @param config The Filter configuration object which contains
     *        parameters for this filter.
     *
     * @exception ServletException If an exception occured during the
     *         creation of the object manager factory or object manager.
     */
    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;

        // Uncomment this to cause the program to hang forever, until
        // you go in the debugger and un-hang it.
        // com.sun.faces.util.DebugUtil.waitForDebugger();

        ObjectManager objectManager;
        ObjectManagerFactory omFactory;
        EventQueueFactory eqFactory;
        FacesContextFactory fcFactory;
        ObjectAccessorFactory oaFactory;
        NavigationHandlerFactory nhFactory;
        ConverterManager converterManager = null;

        ServletContext servletContext = config.getServletContext();
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
            objectManager = omFactory.newObjectManager(servletContext);
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
        objectManager.put(servletContext,
                        Constants.REF_EVENTQUEUEFACTORY, eqFactory);

        // Step 3: Create the FacesContextFactory and put it in the
        // ObjectManager in GlobalScope

        fcFactory = (FacesContextFactory)objectManager.get(
            Constants.REF_FACESCONTEXTFACTORY);
        // The FacesContextFactory must not exist at this point.  It is an
        // error if it does exist.
        Assert.assert_it(null == fcFactory);

        fcFactory = FacesContextFactory.newInstance();
        Assert.assert_it(null != fcFactory);
        objectManager.put(servletContext,
                        Constants.REF_FACESCONTEXTFACTORY, fcFactory);

        // Step 4: Create the ObjectAccessorFactory and put it in the
        // ObjectManager in GlobalScope
        oaFactory = (ObjectAccessorFactory)objectManager.get(
            Constants.REF_OBJECTACCESSORFACTORY);
        // The ObjectAccessorFactory must not exist at this point.  It is an
        // error if it does exist.
        Assert.assert_it(null == oaFactory);

        oaFactory = ObjectAccessorFactory.newInstance();
        Assert.assert_it(null != oaFactory);
        objectManager.put(servletContext,
                        Constants.REF_OBJECTACCESSORFACTORY, oaFactory);

        // Step 5 create an instance of navigationHandlerFactory
        // and put it in Application scope.
        nhFactory = (NavigationHandlerFactory)objectManager.get(
            Constants.REF_NAVIGATIONHANDLERFACTORY);

        // The NavigationHandlerFactory must not exist at this point.  It is an
        // error if it does exist.
        Assert.assert_it(null == nhFactory);

        nhFactory = NavigationHandlerFactory.newInstance();
        Assert.assert_it(null != nhFactory);
        objectManager.put(servletContext,
                        Constants.REF_NAVIGATIONHANDLERFACTORY, nhFactory);

        // Step 6 create an instance of ConverterManager
        // and put it in Application scope.
        converterManager = (ConverterManager)objectManager.get(
            Constants.REF_CONVERTERMANAGER);

        // The converterManager must not exist at this point.  It is an
        // error if it does exist.
        // PENDING(visvan)  ConverterManager should be request scoped
        // to avoid threading issues. 
        Assert.assert_it(null == converterManager);

        ConverterManagerFactory cmFactory = ConverterManagerFactory.newInstance();
        Assert.assert_it(null != cmFactory);

        converterManager = cmFactory.newConverterManager(servletContext);
        objectManager.put(servletContext,
                        Constants.REF_CONVERTERMANAGER, converterManager);

        // Step 7 create a default Message Factory and put it in Global scope
        javax.faces.MessageFactory mf = javax.faces.MessageFactory.newInstance();
        mf.setClassLoader(this.getClass().getClassLoader());
        objectManager.put(servletContext,
                Constants.DEFAULT_MESSAGE_FACTORY_ID, mf);
    }

    /**
     * Process a request.
     *
     * PRECONDITION: ObjectManager exists in ServletContext.
     * ObjectManager global scope contains FacesContextFactory,
     * EventQueueFactory.
     *
     * POSTCONDITION: ObjectManager contains FacesContext and
     * EventQueue instances in session scope.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public void doFilter(ServletRequest req,
        ServletResponse res, FilterChain chain) 
        throws IOException, ServletException {

        FacesContext fc;
        FacesContextFactory fcFactory;
        EventQueue eq;
        EventQueueFactory eqFactory;

        ServletContext servletContext = filterConfig.getServletContext();

        // PENDING(rogerk) Only deal with Http types???
        //
        if ((!(req instanceof HttpServletRequest))||
            (!(res instanceof HttpServletResponse))) {
            throw new ServletException("Request/Response are not type Http");
        }

        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;

        HttpSession thisSession = request.getSession();

        // We set this attr here so that when ObjectManager.get() comes
        // around for this request, we pull it out and use it as the
        // value for the scopeKey.  This is necessary because various
        // entities modify the HttpServletRequest instance so it's not
        // suitable for being used as a key
        //
        request.setAttribute(Constants.REF_REQUESTINSTANCE, request);

        // Get the object manager.
        // 
        ObjectManager objectManager = 
            (ObjectManager)servletContext.getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it(null != objectManager);

        // create a new instance of facesContext for every request.
        fcFactory = (FacesContextFactory)
            objectManager.get(Constants.REF_FACESCONTEXTFACTORY);

        Assert.assert_it(null != fcFactory);
        try {
            fc = fcFactory.newFacesContext(req, res, servletContext );
        } catch (FacesException e) {
            throw new ServletException(e.getMessage());
        }
        Assert.assert_it(null != fc);
        objectManager.put(req, Constants.REF_FACESCONTEXT, fc);
 
        // If there are no events in the queue, simply forward to the target.
        //
        boolean dispatched = false;

        // Only check for a token and process events if this request has
        // parameters.
        //
        if (Util.hasParameters(request)) {
            // if we have a transaction token, see if it is valid
            if (!Util.isTokenValid(request)) {
                // PENDING(edburns): look for servlet config param
                // pageFlowErrorPage if found, redirect to it, else spit
                // out simple page and return.
                ServletException e = new ServletException("Token not valid.  Perhaps your session timed out?");
                outputException(response, e);
                throw e;
            }
            Util.resetToken(request);
      
            RenderKit rk = fc.getRenderKit();
            Assert.assert_it(rk != null);

            rk.queueEvents(fc);

            eq = fc.getEventQueue();
            Assert.assert_it(eq != null ); 
            while (!eq.isEmpty()) {
                EventObject e = (EventObject) eq.getNext();
                try {
                    EventDispatcher src=fc.getEventDispatcher(e);
                    Assert.assert_it ( src != null );
                    src.dispatch ( e );
                } catch ( FacesException fe ) {
                    // abort processing events and lookup the navigation
                    // handler to find out where to go next.
                    // PENDING ( visvan ) should eventQueue be emptied here ?
                    eq.clear();
                    break;
                }
                eq.remove(e);
            }
            // all the events were processed successfully. Lookup the
            // navigation handler to find out where to go next.
            boolean result = doNavigation(fc, req, res );
            if ( result ) {
                dispatched = true;
            }
        }
        // Make sure the client does not cache the response.

        // PENDING(edburns): not sure if this is necessary in all cases.
        // We probably don't need to specify no cache if the request to
        // which we're forwarding is not a faces page.
        response.addHeader("Pragma:", "No-cache");
        response.addHeader("Cache-control:", "no-cache");
        response.addHeader("Expires:", "1");

        //PENDING (rogerk) only if we haven't already dispatched (forwarded)
        if (!dispatched) {
            chain.doFilter(request, response);
        }

        // exit the scope for this request
	// PENDING(edburns): Hans doesn't want this in the public API
	// just cast to our implementation for now
        ((com.sun.faces.ObjectManagerImpl)objectManager).exit(req);

    }

    /**
     * Navigates to the specified URL specifed by the targetPath
     * based on targetAction attribute
     */
    private boolean doNavigation(FacesContext facesContext, 
        ServletRequest req, ServletResponse res ) 
                throws ServletException, IOException {

        boolean navigate = false;

        NavigationHandler nh = facesContext.getNavigationHandler();
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
               try {
                   RequestDispatcher reqD =
                       req.getRequestDispatcher(targetPath);
                   reqD.forward(req, res);
               } catch ( IllegalStateException ie ) {
                   throw new ServletException("couldn't forward to " +
                           targetPath + ie.getMessage() );
               }
           } else if ( targetAction == NavigationHandler.REDIRECT) {
               try {
                   HttpServletResponse httpRes = (HttpServletResponse) res;
                   // PENDING ( visvan ) should URL be encoded ?
                   httpRes.sendRedirect(httpRes.encodeRedirectURL(targetPath));
               } catch ( IOException ioe ) {
                   throw new ServletException("couldn't redirect to " +
                           targetPath +  ioe.getMessage() );
               } 
           }
        }
        return navigate;
    }

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

    // FacesFilter is tested in FacesTestCase.java (test subdir)
}
