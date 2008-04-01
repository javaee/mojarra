/*
 * $Id: RenderKitFactory.java,v 1.3 2002/05/17 05:00:30 craigmcc Exp $
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
 * (keyed by render kit identifiers) for specialized uses.</p>
 *
 * <p>There shall be one <code>RenderKitFactory</code> instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 * <pre>
 *   RenderKitFactory factory = (RenderKitFactory)
 *    FactoryFactory.createFactory("javax.faces.render.RenderKitFactory");
 * </pre>
 *
 * <p>The factory instance MUST return the same {@link RenderKit} instance
 * for all calls to the <code>createRenderKit()</code> method with the
 * same render kit identifier value, from within the same web application.
 * </p>
 */

public abstract class RenderKitFactory {


    /**
     * <p>The render kit identifier of the default {@link RenderKit} instance
     * for this JavaServer Faces implementation.</p>
     */
    public static final String DEFAULT_RENDER_KIT = "DEFAULT";


    /**
     * <p>Register a new {@link RenderKit} instance that is immediately
     * available via this factory instance.</p>
     *
     * @param renderKit RenderKit instance that we are registering
     *
     * @exception IllegalArgumentException if the render kit identifier
     *  of the new render kit is either the reserved default value, or
     *  is already registered
     * @exception NullPointerException if <code>renderKit</code>
     *  is <code>null</code>
     */
    public abstract void addRenderKit(RenderKit renderKit);


    /**
     * <p>Create (if needed) and return a {@link RenderKit} instance
     * for the specified render kit identifier.  The set of available render
     * kit identifiers is available via the <code>getRenderKitIds()</code>
     * method.</p>
     *
     * @param renderKitId Render kit identifier of the requested
     *  {@link RenderKit} instance
     *
     * @exception FacesException if a {@link RenderKit} instance cannot be
     *  constructed
     * @exception NullPointerException if <code>renderKitId</code>
     *  is <code>null</code>
     */
    public abstract RenderKit createRenderKit(String renderKitId)
        throws FacesException;


    /**
     * <p>Return an <code>Iterator</code> over the set of render kit
     * identifiers supported by this factory.  This set MUST include
     * the value specified by <code>RenderKitFactory.DEFAULT_RENDER_KIT</code>.
     * </p>
     */
    public abstract Iterator getRenderKitIds();




}
