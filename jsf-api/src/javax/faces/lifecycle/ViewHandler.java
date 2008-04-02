/*
 * $Id: ViewHandler.java,v 1.3 2003/02/03 22:57:50 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.lifecycle;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;


/**
 * <p><strong>ViewHandler</strong> is an interface defining a mechanism
 * by which the <em>Render Response</em> phase of the request processing
 * lifecycle can use a pluggable mechanism to support different response
 * generation technologies.  JSF implementations, or JSF-based applications,
 * can register an instance of this interface by calling the
 * <code>setViewHandler()</code> method of the {@link Lifecycle} instance
 * used to process incoming requests, prior to receiving the first request.
 * </p>
 *
 * <p>A default implementation of <code>ViewHandler</code> must be provided
 * by the JSF implementation, which will be utilized unless
 * <code>setViewHandler()</code> is called to establish a different one.
 * This default instance will treat the <code>treeId</code> property of the
 * response component tree as a context-relative path (after prefixing it
 * with a slash), and will perform a <code>RequestDispatcher.forward()</code>
 * call to that path.</p>
 */

public interface ViewHandler {


    /**
     * <p>Perform whatever actions are required to render the response
     * component tree to the <code>ServletResponse</code> associated with
     * the specified {@link FacesContext}.</p>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     * @exception ServletException if a servlet error occurs
     */
    public void renderView(FacesContext context)
        throws IOException, ServletException;


}
