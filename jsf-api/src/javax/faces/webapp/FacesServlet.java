/*
 * $Id: FacesServlet.java,v 1.19 2004/01/21 09:57:44 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
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
     * <p>Context initialization parameter name for a comma delimited list
     * of context-relative resource paths (in addition to
     * <code>/WEB-INF/faces-config.xml</code> which is loaded automatically
     * if it exists) containing JavaServer Faces configuration information.</p>
     */
    private static final String CONFIG_FILES_ATTR =
	"javax.faces.CONFIG_FILES";


    /**
     * <p>Context initialization parameter name for the lifecycle identifier
     * of the {@link Lifecycle} instance to be utilized.</p>
     */
    private static final String LIFECYCLE_ID_ATTR =
        "javax.faces.LIFECYCLE_ID";


    /**
     * <p>The {@link Application} instance for this web application.</p>
     */
    private Application application = null;


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

	application = null;
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
     *
     * @exception ServletException if, for any reason, the startup of
     * this Faces application failed.  This includes errors in the
     * config file that is parsed before or during the processing of
     * this <code>init()</code> method.
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

        // Acquire our Application instance
        try {
            ApplicationFactory applicationFactory = (ApplicationFactory)
                FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
            application = applicationFactory.getApplication();
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

        // Acquire the FacesContext instance for this request
        FacesContext context = facesContextFactory.getFacesContext
            (servletConfig.getServletContext(), request, response, lifecycle);

        // Execute the request processing lifecycle for this request
        try {
            lifecycle.execute(context);
	    lifecycle.render(context);
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
	finally {
	    // Release the FacesContext instance for this request
	    context.release();
	}
        
    }


}
