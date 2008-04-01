/*
 * $Id: UITextEntry.java,v 1.10 2002/07/29 00:47:06 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;


/**
 * <p><strong>UITextEntry</strong> is a {@link UIComponent} that represents
 * a text entry field in an input form.  It is typically rendered as
 * either a single-line text field or a multi-line text box.</p>
 */

public class UITextEntry extends UIComponentBase {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UITextEntry";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return the local value of the text to be displayed.</p>
     */
    public String getText() {

        return ((String) getAttribute("value"));

    }


    /**
     * <p>Set the local value of the text to be displayed.</p>
     *
     * @param text The new text
     */
    public void setText(String text) {

        setAttribute("value", text);

    }


    // ------------------------------------------- Lifecycle Processing Methods


    /**
     * <p>Decode the new value of this component from the incoming request.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception IOException if an input/output error occurs while reading
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void decode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Delegate to our associated Renderer if needed
        if (getRendererType() != null) {
            super.decode(context);
            return;
        }

        // Perform the default decoding
        String newValue =
            context.getServletRequest().getParameter(getCompoundId());
        setValue(newValue);
        setValid(true);

    }


    /**
     * <p>Render the current value of this component.</p>
     *
     * @param context FacesContext for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Delegate to our associated Renderer if needed
        if (getRendererType() != null) {
            super.encodeEnd(context);
            return;
        }

        // Perform the default encoding
        Object value = currentValue(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<input type=\"text\" name=\"");
        writer.write(getCompoundId());
        writer.write("\" value=\"");
        if (value != null) {
            writer.write(value.toString());
        }
        writer.write("\">");

    }


}
