/*
 * $Id: FacesContextFactory.java,v 1.1 2002/05/07 05:18:57 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;

import javax.faces.FacesException;     // FIXME - subpackage?
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;


/**
 * <p><strong>FacesContextFactory</strong> is a Factory object that creates
 * and returns new {@link FacesContext} instances, initialized for the
 * processing of the specified request and response objects.
 * Implementations may take advantage of the calls to the
 * <code>release()</code> method of the allocated {@link FacesContext}
 * instances to pool and recycle them, rather than creating a new instance
 * every time.</p>
 *
 * <p>There shall be one <code>FacesContextFactory</code> instance per web
 * application that is utilizing JavaServer Faces.  <strong>FIXME</strong>
 * - document the discovery process for this factory implementation.</p>
 */

public abstract class FacesContextFactory {


    /**
     * <p>Construct and return a {@link FacesContext} that is initialized
     * for the processing of the specified request and response objects,
     * utilizing the standard {@link Lifecycle} implementation for this
     * web application.</p>
     *
     * <p><strong>FIXME</strong> - Do we need to pass in the
     * <code>ServletContext</code>, or can we assume that the initialization
     * of the factory instance has already acquired a reference to this?</p>
     *
     * @param request The <code>ServletRequest</code> that is to be
     *  processed
     * @param response The <code>ServletResponse</code> that is to be
     *  created
     *
     * @exception FacesException if a {@link FacesContext} cannot be
     *  constructed for the specified parameters
     */
    public abstract FacesContext createFacesContext
        (ServletRequest request, ServletResponse response)
        throws FacesException;


    /**
     * <p>Construct and return a {@link FacesContext} that is initialized
     * for the processing of the specified request and response objects,
     * utilizing the specified {@link Lifecycle} implementation for this
     * web application.</p>
     *
     * <p><strong>FIXME</strong> - Do we need to pass in the
     * <code>ServletContext</code>, or can we assume that the initialization
     * of the factory instance has already acquired a reference to this?</p>
     *
     * @param request The <code>ServletRequest</code> that is to be
     *  processed
     * @param response The <code>ServletResponse</code> that is to be
     *  created
     * @param lifecycleId The logical name of the {@link Lifecycle}
     *  implementation to be utilized when processing this request.
     *
     * @exception FacesException if a {@link FacesContext} cannot be
     *  constructed for the specified parameters
     */
    public abstract FacesContext createFacesContext
        (ServletRequest request, ServletResponse response, String lifecycleId)
        throws FacesException;


}
