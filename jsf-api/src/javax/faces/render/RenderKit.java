/*
 * $Id: RenderKit.java,v 1.14 2003/04/29 18:51:44 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.render;


import java.util.Iterator;
import javax.faces.component.UIComponent;


/**
 * <p><strong>RenderKit</strong> represents a collection of
 * {@link Renderer} instances that, together, know how to render
 * JavaServer Faces {@link UIComponent} instances for a specific
 * client.  Typically, {@link RenderKit}s are specialized for
 * some combination of client device type, markup language, and/or
 * user <code>Locale</code>.  A {@link RenderKit} also acts as
 * a Factory for associated {@link Renderer} instances, which perform the
 * actual rendering process for each component.</p>
 *
 * <p>A typical JavaServer Faces implementation will configure one or more
 * {@link RenderKit} instances at web application startup.  They are
 * made available through calls to the <code>getRenderKit()</code> methods
 * of {@link RenderKitFactory}.  Because {@link RenderKit} instances
 * are shared, they must be implemented in a thread-safe manner.</p>
 */

public abstract class RenderKit {


    /**
     * <p>Add a new {@link Renderer} instance, associated with the
     * specified <code>rendererType</code>, to the set of
     * {@link Renderer}s registered with this {@link RenderKit}.
     *
     * @param rendererType Renderer type of the new {@link Renderer}
     * @param renderer The new {@link Renderer} instance
     *
     * @exception IllegalArgumentException if a {@link Renderer} with the
     *  specified <code>rendererType</code> has already been registered
     * @exception NullPointerException if <code>rendererType</code> or
     *  <code>renderer</code> is null
     */
    public abstract void addRenderer(String rendererType, Renderer renderer);


    /**
     * <p>Create (if necessary) and return a {@link Renderer} instance
     * with the specified renderer type.  Subsequent calls to this method
     * with the same <code>rendererType</code>, from the same web application,
     * must return the same instance.</p>
     *
     * @param rendererType Renderer type to be returned
     *
     * @exception IllegalArgumentException if the requested renderer type
     *  is not supported by this {@link RenderKit}
     * @exception NullPointerException if <code>rendererType</code>
     *  is <code>null</code>
     */
    public abstract Renderer getRenderer(String rendererType);

}
