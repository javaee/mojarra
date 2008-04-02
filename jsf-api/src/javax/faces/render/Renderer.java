/*
 * $Id: Renderer.java,v 1.35 2005/07/28 15:35:34 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.render;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.convert.ConverterException;
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
     * <p>Decode any new state of the specified {@link UIComponent}
     * from the request contained in the specified {@link FacesContext},
     * and store that state on the {@link UIComponent}.</p>
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
     * If the conversion attempted in a previous call to
     * <code>getConvertedValue()</code> for this component failed, the state
     * information saved during execution
     * of <code>decode()</code> should be used to reproduce the incorrect
     * input.</p>
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
        if (component.getChildCount() > 0) {
        	Iterator kids = component.getChildren().iterator();
        	while (kids.hasNext()) {
        	    UIComponent kid = (UIComponent) kids.next();
        	    kid.encodeAll(context);
        	}
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
     * <p>Return a flag indicating whether this {@link Renderer} is responsible
     * for rendering the children the component it is asked to render.
     * The default implementation returns <code>false</code>.</p>
     */

    public boolean getRendersChildren() {
	return false;
    }


    /**
     * <p>Attempt to convert previously stored state information into an
     * object of the type required for this component (optionally using the
     * registered {@link javax.faces.convert.Converter} for this component,
     * if there is one).  If conversion is successful, the new value
     * should be returned from this method;  if not, a
     * {@link ConverterException} should be thrown.</p>
     * 
     * @param context {@link FacesContext} for the request we are processing
     * @param component {@link UIComponent} to be decoded.
     * @param submittedValue a value stored on the component during
     *    <code>decode</code>.
     * 
     * @exception ConverterException if the submitted value
     *   cannot be converted successfully.
     * @exception NullPointerException if <code>context</code>
     *  or <code>component</code> is <code>null</code>
     */
    public Object getConvertedValue(FacesContext context,
                                    UIComponent  component,
                                    Object       submittedValue)
        throws ConverterException {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        return submittedValue;
    }
}
