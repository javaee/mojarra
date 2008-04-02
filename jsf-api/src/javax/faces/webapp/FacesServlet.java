/*
 * $Id: FacesServlet.java,v 1.11 2003/01/30 21:29:19 craigmcc Exp $
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
 *
 * <p>This servlet recognizes the following context initialization
 * parameters:</p>
 * <ul>
 * <li><strong>javax.faces.lifecycle.LIFECYCLE_ID</strong> - Lifecycle
 *     identifier of the {@link Lifecycle} instance to be used when
 *     processing JSF requests in this web application.  If not specified,
 *     the default instance, identified by
 *     <code>LifecycleFactory.DEFAULT_LIFECYCLE</code>, will be used.</li>
 * </ul>
 */

public final class FacesServlet implements Servlet {


    /**
     * <p>Context initialization parameter name for the lifecycle identifier
     * of the {@link Lifecycle} instance to be utilized.</p>
     */
    private static final String LIFECYCLE_ID_ATTR =
        "javax.faces.lifecycle.LIFECYCLE_ID";


    /**
     * <p>Factory for {@link FacesContext} instances.</p>
     */
    private FacesContextFactory facesContextFactory = null;


    /**
     * <p>The {@link Lifecycle} instance to use for request processing.</p>
     */
    private Lifecycle lifecycle = null;


    /**
     * <p>The <code>ServletConfig</code> instance for this servlet.</p>
     */
    private ServletConfig servletConfig = null;


    /**
     * <p>Release all resources acquired at startup time.</p>
     */
    public void destroy() {

        facesContextFactory = null;
        lifecycle = null;
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
            Throwable rootCause = e.getCause();
            if (rootCause == null) {
                throw e;
            } else {
                throw new ServletException(e.getMessage(), rootCause);
            }
        }

        // Acquire our Lifecycle instance
        try {
            LifecycleFactory lifecycleFactory = (LifecycleFactory)
                FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            String lifecycleId =
                servletConfig.getServletContext().getInitParameter
                (LIFECYCLE_ID_ATTR);
            if (lifecycleId == null) {
                lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
            }
            lifecycle = lifecycleFactory.getLifecycle(lifecycleId);
        } catch (FacesException e) {
            Throwable rootCause = e.getCause();
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

        // Acquire and cache the FacesContext instance for this request
        FacesContext context = facesContextFactory.getFacesContext
            (servletConfig.getServletContext(), request, response, lifecycle);
        request.setAttribute(FacesContext.FACES_CONTEXT_ATTR, context);

        // Execute the request processing lifecycle for this request
        try {
            lifecycle.execute(context);
        } catch (FacesException e) {
            Throwable t = ((FacesException) e).getCause();
            if (t == null) {
                throw new ServletException(e.getMessage(), e);
            } else {
                if (t instanceof ServletException) {
                    throw ((ServletException) t);
                } else if (t instanceof IOException) {
                    throw ((IOException) t);
                } else {
                    throw new ServletException(t.getMessage(), t);
                }
            }
        }

        // Remove and release the FacesContext instance for this request
        request.removeAttribute(FacesContext.FACES_CONTEXT_ATTR);
        context.release();
        
    }


}
