/*
 * $Id: RenderKit.java,v 1.3 2002/05/17 05:06:13 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.render;


import java.util.Iterator;
import javax.faces.component.UIComponent;


/**
 * <p>A <strong>RenderKit</strong> represents a collection of
 * {@link Renderer} instances that, together, know how to render
 * JavaServer Faces {@link UIComponent} instances for a specific
 * client.  Typically, <code>RenderKit</code>s are specialized for
 * some combination of client device type, markup language, and/or
 * user <code>Locale</code>.  A <code>RenderKit</code> also acts as
 * a Factory for associated {@link Renderer} objects, which perform the
 * actual rendering process for each component.</p>
 *
 * <h3>Lifecycle</h3>
 *
 * <p>A typical JavaServer Faces implementation will configure one or more
 * <code>RenderKit</code> instances at web application startup.  They are
 * made available through calls to the <code>createRenderKit()</code> methods
 * of {@link RenderKitFactory}.  At least one <code>RenderKit</code>
 * implementation must be provided as the default.</p>
 */

public abstract class RenderKit {


    /**
     * <p>Return the logical name of this <code>RenderKit</code>.  The
     * default <code>RenderKit</code> instance for a given JavaServer Faces
     * implementation shall return
     * <code>RenderKitFactory.DEFAULT_RENDER_KIT</code></p>
     */
    public abstract String getRenderKitId();


    /**
     * <p>Create (if necessary) and return a {@link Renderer} instance
     * with the specified renderer type.  Subsequent calls to this method
     * with the same renderer type should return the same instance.</p>
     *
     * @param rendererType Renderer type to be returned
     *
     * @exception IllegalArgumentException if the requested renderer type
     *  is not supported by this <code>RenderKit</code>
     * @exception NullPointerException if <code>rendererType</code>
     *  is <code>null</code>
     */
    public abstract Renderer getRenderer(String rendererType);


    /**
     * <p>Return an <code>Iterator</code> of the {@link Renderer} instances
     * supported by this <code>RenderKit</code> that support components of
     * the specified {@link UIComponent} subclass.  If no {@link Renderer}
     * instances support this component class, an empty <code>Iterator</code>
     * is returned.</p>
     *
     * @param component UIComponent whose class is tested for support
     */
    public abstract Iterator getRenderers(UIComponent component);


    /**
     * <p>Return an <code>Iterator</code> of the {@link Renderer} instances
     * supported by this <code>RenderKit</code> that support components of
     * the specified {@link UIComponent} subclass.  If no {@link Renderer}
     * instances support this component class, an empty <code>Iterator</code>
     * is returned.</p>
     *
     * @param componentType Canonical name of the component type tested
     *  for support
     */
    public abstract Iterator getRenderers(String componentType);


    /**
     * <p>Return an <code>Iterator</code> of the renderer types supported
     * by this <code>RenderKit</code>.</p>
     */
    public abstract Iterator getRendererTypes();


}
