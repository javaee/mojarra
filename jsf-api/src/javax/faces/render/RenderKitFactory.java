/*
 * $Id: RenderKitFactory.java,v 1.1 2002/05/08 01:11:47 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.render;


import java.util.Iterator;
import javax.faces.FacesException;     // FIXME - subpackage?


/**
 * <p>A <strong>RenderKitFactory</strong> is a Factory object that creates
 * and returns new {@link RenderKit} instances.  Implementations of
 * JavaServer Faces MUST provide at least a default implementation of
 * {@link RenderKit}.  Advanced implementations (or external third party
 * libraries) MAY provide additional {@link RenderKit} implementations
 * for specialized uses.</p>
 *
 * <p>There shall be one <code>RenderKitFactory</code> instance per web
 * application that is utilizing JavaServer Faces.  <strong>FIXME</strong>
 * - document the discovery process for this factory implementation.</p>
 *
 * <p>The factory instance MUST return the same {@link RenderKit} instance
 * for all calls to the <code>createRenderKit()</code> method with no
 * parameters, or for all calls to the <code>createRenderKit()</code> method
 * with the same logical render kit name.</p>
 *
 * <p><strong>FIXME</strong> - Specify a way for third party
 * {@link RenderKit}s to register themselves.</p>
 */

public abstract class RenderKitFactory {


    /**
     * <p>Create (if needed) and return a {@link RenderKit} that is the
     * default implementation for all JavaServer Faces requests for this
     * web application.  All JavaServer Faces implementations MUST be able
     * to return a {@link RenderKit} instance via this call.</p>
     *
     * @exception FacesException if a {@link RenderKit} cannot be
     *  constructed
     */
    public abstract RenderKit createRenderKit() throws FacesException;


    /**
     * <p>Create (if needed) and return a {@link RenderKit} instance
     * for the specified render kit name.  The set of available render kit
     * names is available via the <code>getRenderKitNames()</code>
     * method.</p>
     *
     * @param renderKitName Name of the requested {@link RenderKit} instance
     *
     * @exception FacesException if a {@link RenderKit} cannot be
     *  constructed
     * @exception NullPointerException if <code>name</code>
     *  is <code>null</code>
     */
    public abstract RenderKit createRenderKit(String renderKitName)
        throws FacesException;


    /**
     * <p>Return an <code>Iterator</code> over the set of custom render kit
     * names supported by this factory.  If no custom names are supported,
     * an empty <code>Iterator</code> is returned.</p>
     */
    public abstract Iterator getRenderKitNames();


}
