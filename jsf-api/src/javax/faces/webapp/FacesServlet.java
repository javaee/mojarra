/*
 * $Id: FacesServlet.java,v 1.5 2002/06/12 21:51:29 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.event.CommandEvent;
import javax.faces.event.FormEvent;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * <p><strong>FacesServlet</strong> is a servlet that manages the request
 * processing lifecycle for web applications that are utilizing JavaServer
 * Faces to construct the user interface.</p>
 */

public final class FacesServlet implements Servlet {


    /**
     * <p>Factory for {@link FacesContext} instances.</p>
     */
    private FacesContextFactory facesContextFactory = null;


    /**
     * <p>Factory for {@link Lifecycle} instances.</p>
     */
    private LifecycleFactory lifecycleFactory = null;


    /**
     * <p>The <code>ServletConfig</code> instance for this servlet.</p>
     */
    private ServletConfig servletConfig = null;


    /**
     * <p>Release all resources acquired at startup time.</p>
     */
    public void destroy() {

        facesContextFactory = null;
        lifecycleFactory = null;
        servletConfig = null;

    }


    /**
     * <p>Return the <code>ServletConfig</code> instance for this servlet.</p>
     */
    public ServletConfig getServletConfig() {

        return (this.servletConfig);

    }


    /**
     * <p>Return information about this Servlet.</p>
     */
    public String getServletInfo() {

        return (this.getClass().getName());

    }


    /**
     * <p>Acquire the factory instances we will require.</p>
     */
    public void init(ServletConfig servletConfig) throws ServletException {

        // Save our ServletConfig instance
        this.servletConfig = servletConfig;

        // Acquire our FacesContextFactory instance
        try {
            facesContextFactory = (FacesContextFactory)
                FactoryFinder.getFactory
                (FactoryFinder.FACES_CONTEXT_FACTORY);
        } catch (FacesException e) {
            Throwable rootCause = e.getRootCause();
            if (rootCause == null) {
                throw e;
            } else {
                throw new ServletException(e.getMessage(), rootCause);
            }
        }

        // Acquire our LifecycleFactory instance
        try {
            lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory
                (FactoryFinder.LIFECYCLE_FACTORY);
        } catch (FacesException e) {
            Throwable rootCause = e.getRootCause();
            if (rootCause == null) {
                throw e;
            } else {
                throw new ServletException(e.getMessage(), rootCause);
            }
        }

    }


    /**
     * <p>Process an incoming request, and create the corresponding
     * response, by executing the request processing lifecycle.</p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs during processing
     * @exception ServletException if a servlet error occurs during processing
     */
    public void service(ServletRequest request,
                        ServletResponse response)
        throws IOException, ServletException {

        // response.setContentType("text/html");  // FIXME - when/how to call?

        // Create and cache the FacesContext instance for this request
        FacesContext context = facesContextFactory.getFacesContext
            (servletConfig.getServletContext(), request, response);
        request.setAttribute(FacesContext.FACES_CONTEXT_ATTR, context);

        // Look up the Lifecycle instance for this request
        Lifecycle lifecycle = lifecycleFactory.getLifecycle
            (LifecycleFactory.DEFAULT_LIFECYCLE); // FIXME - how to choose?
        // lifecycle.setApplicationHandler(this); // FIXME - when/how to call?

        // Execute the request processing lifecycle for this request
        try {
            lifecycle.execute(context);
        } catch (FacesException e) {
            throw new ServletException(e.getMessage(), e);
        }

        // Release the FacesContext instance for this request
        request.removeAttribute(FacesContext.FACES_CONTEXT_ATTR);
        context.release();
        
    }


}
