/*
 * $Id: RenderKitFactory.java,v 1.14 2003/09/29 23:39:53 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.render;


import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;


/**
 * <p><strong>RenderKitFactory</strong> is a factory object that registers
 * and returns {@link RenderKit} instances.  Implementations of
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
     * <p>Register the specified {@link RenderKit} instance, associated with
     * the specified <code>renderKitId</code>, to be supported by this
     * {@link RenderKitFactory}, replacing any previously registered
     * {@link RenderKit} for this identifier.</p>
     *
     * @param renderKitId Identifier of the {@link RenderKit} to register
     * @param renderKit {@link RenderKit} instance that we are registering
     *
     * @exception NullPointerException if <code>renderKitId</code> or
     *  <code>renderKit</code> is <code>null</code>
     */
    public abstract void addRenderKit(String renderKitId,
                                      RenderKit renderKit);


    /**
     * <p>Return the {@link RenderKit} instance most recently registered for
     * the specified render kit identifier, if any; otherwise, return
     * <code>null</code>.  The set of available render kit identifiers is
     * available via the <code>getRenderKitIds()</code> method.</p>
     *
     * @param renderKitId Render kit identifier of the requested
     *  {@link RenderKit} instance
     *
     * @exception NullPointerException if <code>renderKitId</code>
     *  is <code>null</code>
     */
    public abstract RenderKit getRenderKit(String renderKitId);


    /**
     * <p>Return a {@link RenderKit} instance for the
     * specified render kit identifier, possibly customized based on
     * dynamic characteristics of the specified {@link FacesContext}.
     * If there is no registered {@link RenderKit} for the specified
     * identifier, return <code>null</code>.  The set of available render
     * kit identifiers is available via the <code>getRenderKitIds()</code>
     * method.</p>
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
     * identifiers registered with this factory.  This set must include
     * the value specified by <code>RenderKitFactory.DEFAULT_RENDER_KIT</code>.
     * </p>
     */
    public abstract Iterator getRenderKitIds();


}
