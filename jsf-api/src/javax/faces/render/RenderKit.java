/*
 * $Id: RenderKit.java,v 1.12 2003/02/03 22:57:50 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
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
     * <p>Add a new {@link UIComponent} subclass to the set of component
     * classes registered with this {@link RenderKit} instance
     * and supported by its constituent {@link Renderer}s.</p>
     *
     * @param componentClass {@link UIComponent} subclass to be supported
     *
     * @exception IllegalArgumentException if <code>componentClass</code>
     *  is not a {@link UIComponent} subclass
     * @exception NullPointerException if <code>componentClass</code>
     *  is <code>null</code>
     */
    public abstract void addComponentClass(Class componentClass);


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
     * <p>Return an <code>Iterator</code> of the {@link UIComponent}
     * classes that are registered with this {@link RenderKit}
     * instance and supported by its constituent {@link Renderer}s.  If
     * there are no such registered component classes, an empty
     * <code>Iterator</code> is returned.</p>
     */
    public abstract Iterator getComponentClasses();


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


    /**
     * <p>Return an <code>Iterator</code> of all the renderer types registered
     * with this <code>RenderKit</code>.</p>
     */
    public abstract Iterator getRendererTypes();


    /**
     * <p>Return an <code>Iterator</code> of the renderer types of all
     * {@link Renderer} instances registered with this {@link RenderKit}
     * that support components of the specified component type.
     * If no {@link Renderer}s support this component type, an empty
     * <code>Iterator</code> is returned.</p>
     *
     * @param componentType Canonical name of the component type tested
     *  for support
     *
     * @exception NullPointerException if <code>componentType</code>
     *  is <code>null</code>
     */
    public abstract Iterator getRendererTypes(String componentType);


    /**
     * <p>Return an <code>Iterator</code> of the renderer types of all
     * {@link Renderer} instances registered with this {@link RenderKit}
     * that support components of the specified {@link UIComponent} subclass.
     * If no {@link Renderer}s support this component class, an empty
     * <code>Iterator</code> is returned.</p>
     *
     * @param component UIComponent whose class is tested for support
     *
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public abstract Iterator getRendererTypes(UIComponent component);


}
