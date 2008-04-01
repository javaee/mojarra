/*
 * $Id: FacesContext.java,v 1.1 2002/05/07 05:18:57 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.context;

import javax.faces.component.UIContainer;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;


/**
 * <p><strong>FacesContext</strong> contains all of the per-request state
 * information related to the processing of a single JavaServer Faces request,
 * and the rendering of the corresponding response.  It is passed to, and
 * potentially modified by, each stage of the request processing lifecycle.</p>
 *
 * <h3>Lifecycle</h3>
 *
 * <p>A <code>FacesContext</code> instance is associated with a particular
 * request at the beginning of request processing, by a call to the
 * <code>createFacesContext()</code> method of the {@link FacesContextFactory}
 * instance associated with the current web application.  The instance
 * remains active until its <code>release()</code> method is called, after
 * which no further references to this instance are allowed.  While a
 * <code>FacesContext</code> instance is active, it MUST NOT be referenced
 * from any thread other than the one upon which the servlet container
 * executing this web application utilizes for the processing of this request.
 * </p>
 */

public abstract class FacesContext {


    /**
     * <p>Return the <code>HttpSession</code> instance for the session
     * associated with the current request (if any); otherwise, return
     * <code>null</code>.</p>
     */
    public abstract HttpSession getHttpSession();


    /**
     * <p>Return the {@link Lifecycle} instance that is managing the
     * processing of the request represented by this <code>FacesContext</code>
     * instance.</p>
     */
    public abstract Lifecycle getLifecycle();


    /**
     * <p>Return the logical page identifier associated with the inbound
     * request.  Typically, this will be the same as the page identifier
     * of the JavaServer Pages response that rendered the page from which
     * this request was submitted.</p>
     */
    public abstract String getRequestPageId();


    /**
     * <p>Return the {@link UIContainer} that is the root of the component
     * tree for the inbound request.</p>
     */
    public abstract UIContainer getRequestTree();


    /**
     * <p>Return the logical page identifier associated with the outbound
     * response.</p>
     */
    public abstract String getResponsePageId();


    /**
     * <p>Return the {@link UIContainer} that is the root of the component
     * tree for the outbound response.  Unless otherwise specified (by a
     * call to <code>setResponseTree()</code>), this will return the same
     * component tree returned by <code>getRequestTree()</code>.</p>
     */
    public abstract UIContainer getResponseTree();


    /**
     * <p>Return the <code>ServletContext</code> object for the web application
     * associated with this request.</p>
     */
    public abstract ServletContext getServletContext();


    /**
     * <p>Return the <code>ServletRequest</code> object representing the
     * current request that is being processed.</p>
     */
    public abstract ServletRequest getServletRequest();


    /**
     * <p>Return the <code>ServletResponse</code> object representing the
     * current response that is being rendered.</p>
     */
    public abstract ServletResponse getServletResponse();


    /**
     * <p>Release any resources associated with this <code>FacesContext</code>
     * instance.  Faces implementations may choose to pool instances in the
     * associated {@link FacesContextFactory} to avoid repeated object creation
     * and garbage collection.</p>
     */
    public abstract void release();


    /**
     * <p>Set the logical page identifier of the associated with the
     * inbound request.</p>
     *
     * @param pageId The new request page identifier
     */
    public abstract void setRequestPageId(String pageId);
    

    /**
     * <p>Set the {@link UIContainer} that is the root of the component tree
     * for the inbound request.</p>
     *
     * @param root The root of the inbound component tree
     */
    public abstract void setRequestTree(UIContainer root);


    /**
     * <p>Set the logical page identifier associated with the outbound
     * response.</p>
     *
     * @param pageId The new response page identifier
     */
    public abstract void setResponsePageId(String pageId);


    /**
     * <p>Set the {@link UIContainer} that is the root of the component tree
     * for the outbound response.</p>
     *
     * @param root The root of the outbound component tree
     */
    public abstract void setResponseTree(UIContainer root);


}
