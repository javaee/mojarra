/*
 * $Id: Renderer.java,v 1.1 2002/05/08 01:11:47 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.render;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p>A <strong>Renderer</strong> converts the internal representation of
 * {@link UIComponent}s into the output stream (or writer) associated with
 * the response we are creating for a particular request.  Each
 * <code>Renderer</code> knows how to render one or more {@link UIComponent}
 * types (or classes), and advertises a set of render-dependent attributes
 * that it recognizes for each supported {@link UIComponent}.</p>
 *
 * <p>Families of <code>Renderer</code>s are packaged as a {@link RenderKit},
 * and together support the rendering of all of the {@link UIComponents} in
 * the response tree associated with a {@link FacesContext}.  Within the set
 * of <code>Renderers</code> for a particular {@link RenderKit}, each must be
 * uniquely identified by the <code>rendererType</code> property.</p>
 *
 * <h3>Lifecycle</h3>
 *
 * <p>Individual {@link Renderer} instances will be instantiated as requested
 * during the rendering process, and will remain in existence for the
 * remainder of the lifetime of a web application.  Because each instance
 * may be invoked from more than one request processing thread simultaneously,
 * they MUST be programmed in a thread-safe manner.</p>
 */

public abstract class Renderer {


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the logical name of the type of <code>Renderer</code>
     * for this instance.  This is used as a lookup key when mapping
     * {@link UIComponent}s to the corresponding {@link Renderer} instances
     * during the <em>Render Response</em> phase of the request processing
     * lifecycle.</p>
     */
    public abstract String getRendererType();


    // ------------------------------------------------------- Metadata Methods


    /**
     * <p>Return an {@link AttributeDescriptor} for the specified attribute
     * name, as supported for the specified component class.</p>
     *
     * @param component {@link UIComponent} whose implementation class
     *  is supported
     * @param name Name of the attribute for which to return a descriptor
     *
     * @exception IllegalArgumentException if the specified component class
     *  is not supported
     * @exception IllegalArgumentException if the specified attribute name
     *  is not supported for the specified component class
     * @exception NullPointerException if <code>component</code> or
     *  <code>name</code> is null
     */
    public abstract AttributeDescriptor getAttributeDescriptor
        (UIComponent component, String name);


    /**
     * <p>Return an {@link AttributeDescriptor} for the specified attribute
     * name, as supported for the specified component type.</p>
     *
     * @param componentType Canonical name of a supported component type
     * @param name Name of the attribute for which to return a descriptor
     *
     * @exception IllegalArgumentException if the specified component type
     *  is not supported
     * @exception IllegalArgumentException if the specified attribute name
     *  is not supported for the specified component type
     * @exception NullPointerException if <code>type</code> or
     *  <code>name</code> is <code>null</code>
     */
    public abstract AttributeDescriptor getAttributeDescriptor
        (String componentType, String name);


    /**
     * <p>Return an <code>Iterator</code> over the names of the supported
     * attributes for the specified {@link UIComponent} implementation class.
     * If no attributes are supported, an empty <code>Iterator</code>
     * is returned.</p>
     *
     * @param component {@link UIComponent} whose implementation class
     *  is supported
     *
     * @exception IllegalArgumentException if the specified component class
     *  is not supported
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public abstract Iterator getAttributeNames(UIComponent component);


    /**
     * <p>Return an <code>Iterator</code> over the names of the supported
     * attributes for the specified component type.  If no attributes are
     * supported, an empty <code>Iterator</code> is returned.</p>
     *
     * @param componentType Canonical name of a supported component type
     *
     * @exception IllegalArgumentException if the specified component type
     *  is not supported
     * @exception NullPointerException if <code>type</code>
     *  is <code>null</code>
     */
    public abstract Iterator getAttributeNames(String componentType);


    /**
     * <p>Return <code>true</code> if this <code>Renderer</code> supports
     * components of the specified {@link UIComponent} class.</p>
     *
     * @param component {@link UIComponent} whose implementation class is
     *  checked for compatibility
     */
    public abstract boolean supportsComponentType(UIComponent component);


    /**
     * <p>Return <code>true</code> if this <code>Renderer</code> supports
     * components of the specified <code>type</code>.  This is matched against
     * the <code>type</code> property of each {@link UIComponent} to determine
     * compatibility.</p>
     *
     * @param componentType Canonical name of the component type to be tested
     *  for compatibility
     */
    public abstract boolean supportsComponentType(String componentType);



    // ------------------------------------------------------ Rendering Methods


    /**
     * <p>Render the specified {@link UIComponent} to the output stream or
     * writer associated with the response we are creating.</p>
     *
     * <p><strong>FIXME</strong> - Not sufficient for child rendering.</p>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent to be rendered
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  or <code>component</code> is null
     */
    public abstract void render(FacesContext context, UIComponent component)
        throws IOException;


}
