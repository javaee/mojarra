/*
 * $Id: RenderKitFactory.java,v 1.10 2002/07/26 19:02:39 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.render;


import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;


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
 *    FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
 * </pre>
 *
 * <p>The factory instance MUST return the same {@link RenderKit} instance
 * for all calls to the <code>getRenderKit()</code> method with the
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
     * available from this factory instance via a call to
     * <code>getRenderKit()</code> for the same render kit identifier.</p>
     *
     * @param renderKitId Identifier of the new RenderKit
     * @param renderKit RenderKit instance that we are registering
     *
     * @exception IllegalArgumentException if the render kit identifier
     *  of the new render kit is either the reserved default value, or
     *  is already registered
     * @exception NullPointerException if <code>renderKitId</code> or
     *  <code>renderKit</code> is <code>null</code>
     */
    public abstract void addRenderKit(String renderKitId, RenderKit renderKit);


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
    public abstract RenderKit getRenderKit(String renderKitId)
        throws FacesException;


    /**
     * <p>Create (if needed) and return a {@link RenderKit} instance
     * for the specified render kit identifier, possibly customized based
     * on dynamic characteristics of the specified {@link FacesContext}.
     * The set of available render kit identifiers is available via the
     * <code>getRenderKitIds()</code> method.</p>
     *
     * @param renderKitId Render kit identifier of the requested
     *  {@link RenderKit} instance
     * @param contxt FacesContext for the request currently being processed
     *
     * @exception FacesException if a {@link RenderKit} instance cannot be
     *  constructed
     * @exception NullPointerException if <code>renderKitId</code>
     *  or <code>context</code> is <code>null</code>
     */
    public abstract RenderKit getRenderKit(String renderKitId,
                                           FacesContext context)
        throws FacesException;


    /**
     * <p>Return an <code>Iterator</code> over the set of render kit
     * identifiers supported by this factory.  This set MUST include
     * the value specified by <code>RenderKitFactory.DEFAULT_RENDER_KIT</code>.
     * </p>
     */
    public abstract Iterator getRenderKitIds();




}
