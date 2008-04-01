/*
 * $Id: Renderer.java,v 1.11 2002/07/26 19:02:39 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.render;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.AttributeDescriptor;
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
 * and together support the rendering of all of the {@link UIComponent}s in
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
     *
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
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
     *
     * @exception NullPointerException if <code>componentType</code>
     *  is <code>null</code>
     */
    public abstract boolean supportsComponentType(String componentType);



    // ------------------------------------------------------ Rendering Methods


    /**
     * <p>Decode the current state of the specified {@link UIComponent}
     * from the request contained in the specified {@link FacesContext},
     * and attempt to convert this state information into an object of
     * the type required for this component.  If conversion is successful,
     * save the resulting object via a call to <code>getValue()</code>.
     * If conversion is not successful:</p>
     * <ul>
     * <li>Save the state inforamtion (inside the component) in such a way
     *     that encoding can reproduce the previous input
     *     (even though it was syntactically or semantically incorrect).</li>
     * <li>Add an appropriate conversion failure error message by calling
     *     <code>context.addMessage()</code>.</li>
     * </ul>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent to be decoded.
     *
     * @exception IOException if an input/output error occurs while decoding
     * @exception NullPointerException if <code>context</code>
     *  or <code>component</code> is null
     */
    public abstract void decode(FacesContext context, UIComponent component)
        throws IOException;


    /**
     * <p>Render the beginning specified {@link UIComponent} to the
     * output stream or writer associated with the response we are creating.
     * If the conversion attempted in a previous call to <code>decode</code>
     * for this component failed, the state information saved during execution
     * of <code>decode()</code> should be utilized to reproduce the incorrect
     * input.  If the conversion was successful, or if there was no previous
     * call to <code>decode()</code>, the value to be displayed should be
     * acquired by calling <code>component.currentValue()</code>, and
     * rendering the value as appropriate.</p>
     *
     * @param context FacesContext for the request we are processing
     * @param component UIComponent to be rendered
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  or <code>component</code> is null
     */
    public abstract void encodeBegin(FacesContext context,
                                     UIComponent component)
        throws IOException;


    /**
     * <p>Render the child components of this {@link UIComponent}, following
     * the rules described for <code>encodeBegin()</code> to acquire the
     * appropriate value to be rendered.  This method will only be called
     * if the <code>rendersChildren</code> property of this component
     * is <code>true</code>.</p>
     *
     * @param context FacesContext for the response we are creating
     * @param component UIComponent whose children are to be rendered
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  or <code>component</code> is <code>null</code>
     */
    public abstract void encodeChildren(FacesContext context,
                                        UIComponent component)
        throws IOException;


    /**
     * <p>Render the ending of the current state of the specified
     * {@link UIComponent}, following the rules described for
     * <code>encodeBegin()</code> to acquire the appropriate value
     * to be rendered.</p>
     *
     * @param context FacesContext for the response we are creating
     * @param component UIComponent whose children are to be rendered
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  or <code>component</code> is <code>null</code>
     */
    public abstract void encodeEnd(FacesContext context,
                                   UIComponent component)
        throws IOException;


}
