/*
 * $Id: Renderer.java,v 1.27 2003/10/29 15:13:19 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
 * <p>Families of {@link Renderer}s are packaged as a {@link RenderKit},
 * and together support the rendering of all of the {@link UIComponent}s
 * in a view associated with a {@link FacesContext}.  Within the set of
 * {@link Renderer}s for a particular {@link RenderKit}, each must be
 * uniquely identified by the <code>rendererType</code> property.</p>
 *
 * <p>Individual {@link Renderer} instances will be instantiated as requested
 * during the rendering process, and will remain in existence for the
 * remainder of the lifetime of a web application.  Because each instance
 * may be invoked from more than one request processing thread simultaneously,
 * they MUST be programmed in a thread-safe manner.</p>
 */

public abstract class Renderer {

    // ------------------------------------------------------ Rendering Methods


    /**
     * <p>Decode the current state of the specified {@link UIComponent}
     * from the request contained in the specified {@link FacesContext},
     * and attempt to convert this state information into an object of
     * the type required for this component (optionally using the registered
     * {@link javax.faces.convert.Converter} for this component,
     * if there is one).</p>
     *
     * <p>If conversion is successful:</p>
     * <ul>
     * <li>Save the new local value of this component by calling
     *     <code>setValue()</code> and passing the new value.</li>
     * <li>Set the <code>value</code> property of this component
     *     to <code>true</code>.</li>
     * </ul>
     *
     * <p>If conversion is not successful:</p>
     * <ul>
     * <li>Save the state information (inside the component) in such a way
     *     that encoding can reproduce the previous input
     *     (even though it was syntactically or semantically incorrect).</li>
     * <li>Add an appropriate conversion failure error message by calling
     *     <code>addMessage()</code> on the specified {@link FacesContext}.
     *     </li>
     * <li>Set the <code>valid</code> property of this component
     *     to <code>false</code>.</li>
     * </ul>
     *
     * <p>During decoding, events may be enqueued for later processing
     * (by event listeners that have registered an interest), by calling
     * <code>queueEvent()</code> on the associated {@link UIComponent}.
     * </p>
     *
     * @param context {@link FacesContext} for the request we are processing
     * @param component {@link UIComponent} to be decoded.
     *
     * @exception NullPointerException if <code>context</code>
     *  or <code>component</code> is <code>null</code>
     */
    public void decode(FacesContext context, UIComponent component) {
	if (null == context || null == component) {
	    throw new NullPointerException();
	}
    }


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
     * @param context {@link FacesContext} for the request we are processing
     * @param component {@link UIComponent} to be rendered
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  or <code>component</code> is null
     */
    public void encodeBegin(FacesContext context,
			    UIComponent component)
        throws IOException {
	if (null == context || null == component) {
	    throw new NullPointerException();
	}
    }


    /**
     * <p>Render the child components of this {@link UIComponent}, following
     * the rules described for <code>encodeBegin()</code> to acquire the
     * appropriate value to be rendered.  This method will only be called
     * if the <code>rendersChildren</code> property of this component
     * is <code>true</code>.</p>
     *
     * @param context {@link FacesContext} for the response we are creating
     * @param component {@link UIComponent} whose children are to be rendered
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  or <code>component</code> is <code>null</code>
     */
    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException();
        }
	Iterator kids = component.getChildren().iterator();
	while (kids.hasNext()) {
	    UIComponent kid = (UIComponent) kids.next();
	    kid.encodeBegin(context);
	    if (kid.getRendersChildren()) {
		kid.encodeChildren(context);
	    }
	    kid.encodeEnd(context);
	}
    }


    /**
     * <p>Render the ending of the current state of the specified
     * {@link UIComponent}, following the rules described for
     * <code>encodeBegin()</code> to acquire the appropriate value
     * to be rendered.</p>
     *
     * @param context {@link FacesContext} for the response we are creating
     * @param component {@link UIComponent} to be rendered
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  or <code>component</code> is <code>null</code>
     */
    public void encodeEnd(FacesContext context,
			  UIComponent component)
        throws IOException {
	if (null == context || null == component) {
	    throw new NullPointerException();
	}
    }

    /**
     * <p>Convert the component generated client id to a form suitable
     * for transmission to the client.</p>
     *
     * <p>The default implementation returns the argument
     * <code>clientId</code> unchanged.</p>
     *
     * @param context {@link FacesContext} for the current request
     * @param clientId the client identifier to be converted to client a
     * specific format.
     *
     * @exception NullPointerException if <code>context</code>
     *  or <code>clientId</code> is <code>null</code>
     */ 
    public String convertClientId(FacesContext context, String clientId) {

        if ((context == null) || (clientId == null)) {
            throw new NullPointerException();
        }
        return (clientId);

    }

    /**
     * <p>Return a flag indicating whether this renderer is responsible
     * for rendering the children the component it is asked to render.
     * The default implementation returns <code>false</code>.</p>
     */

    public boolean getRendersChildren() {
	return false;
    }


}
