/*
 * $Id: RenderKitFactory.java,v 1.12 2003/02/03 22:57:50 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.render;


import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;


/**
 * <p><strong>RenderKitFactory</strong> is a factory object that creates
 * (if needed) and returns new {@link RenderKit} instances.  Implementations of
 * JavaServer Faces must provide at least a default implementation of
 * {@link RenderKit}.  Advanced implementations (or external third party
 * libraries) may provide additional {@link RenderKit} implementations
 * (keyed by render kit identifiers) for performing different types of
 * rendering for the same components.</p>
 *
 * <p>There must be one {@link RenderKitFactory} instance per web
 * application that is utilizing JavaServer Faces.  This instance can be
 * acquired, in a portable manner, by calling:</p>
 * <pre>
 *   RenderKitFactory factory = (RenderKitFactory)
 *    FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
 * </pre>
 */

public abstract class RenderKitFactory {


    /**
     * <p>The render kit identifier of the default {@link RenderKit} instance
     * for this JavaServer Faces implementation.</p>
     */
    public static final String DEFAULT_RENDER_KIT = "DEFAULT";


    /**
     * <p>Register a new {@link RenderKit} instance, associated with
     * the specified <code>renderKitId</code>, to be supported by this
     * {@link RenderKitFactory}.  This method may be called at
     * any time, and makes the corresponding {@link RenderKit} instance
     * available throughout the remaining lifetime of this web application.
     * </p>
     *
     * @param renderKitId Identifier of the new {@link RenderKit}
     * @param renderKit {@link RenderKit} instance that we are registering
     *
     * @exception IllegalArgumentException if a {@link RenderKit} with the
     *  specified <code>renderKitId</code> has already been registered
     * @exception NullPointerException if <code>renderKitId</code> or
     *  <code>renderKit</code> is <code>null</code>
     */
    public abstract void addRenderKit(String renderKitId,
                                      RenderKit renderKit);


    /**
     * <p>Create (if needed) and return a {@link RenderKit} instance
     * for the specified render kit identifier.  The set of available render
     * kit identifiers is available via the <code>getRenderKitIds()</code>
     * method.</p>
     *
     * <p>Each call to <code>getRenderKit()</code> for the same
     * <code>renderKitId</code>, from within the same web application,
     * must return the same {@link RenderKit} instance.</p>
     *
     * @param renderKitId Render kit identifier of the requested
     *  {@link RenderKit} instance
     *
     * @exception IllegalArgumentException if no {@link RenderKit} instance
     *  can be returned for the specified identifier
     * @exception NullPointerException if <code>renderKitId</code>
     *  is <code>null</code>
     */
    public abstract RenderKit getRenderKit(String renderKitId);


    /**
     * <p>Create (if needed) and return a {@link RenderKit} instance
     * for the specified render kit identifier, possibly customized based
     * on dynamic characteristics of the specified {@link FacesContext}.
     * The set of available render kit identifiers is available via the
     * <code>getRenderKitIds()</code> method.</p>
     *
     * @param renderKitId Render kit identifier of the requested
     *  {@link RenderKit} instance
     * @param context FacesContext for the request currently being processed
     *
     * @exception IllegalArgumentException if no {@link RenderKit} instance
     *  can be returned for the specified identifier
     * @exception NullPointerException if <code>renderKitId</code>
     *  or <code>context</code> is <code>null</code>
     */
    public abstract RenderKit getRenderKit(String renderKitId,
                                           FacesContext context);


    /**
     * <p>Return an <code>Iterator</code> over the set of render kit
     * identifiers supported by this factory.  This set must include
     * the value specified by <code>RenderKitFactory.DEFAULT_RENDER_KIT</code>.
     * </p>
     */
    public abstract Iterator getRenderKitIds();




}
