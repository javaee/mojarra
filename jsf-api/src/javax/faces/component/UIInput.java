/*
 * $Id: UIInput.java,v 1.4 2002/12/17 23:30:52 eburns Exp $
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
 * <p><strong>UIInput</strong> is a {@link UIComponent} that represents
 * a component that both displays output to the user (like
 * {@link UIOutput} components do) and includes request parameters on the
 * subsequent request that need to be decoded.  There are no restrictions
 * on the data type of the local value, or the object referenced by the
 * model reference expression (if any); however, individual
 * <code>Renderer</code>s will generally impose restrictions on the type
 * of data they know how to display.</p>
 */

public class UIInput extends UIComponentBase {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UIInput";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

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
    public boolean decode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Delegate to our associated Renderer if needed
        if (getRendererType() != null) {
            return (super.decode(context));
        }

        // Perform the default decoding
        String newValue =
            context.getServletRequest().getParameter(getClientId(context));
        setValue(newValue);
        setValid(true);
        return (true);

    }


    /**
     * <p>Render the current value of this component if the value 
     * of the rendered attribute is <code>true</code>. </p>
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

        // if rendered is false, do not perform default encoding.
        if (!isRendered()) {
            return;
        }

        // Perform the default encoding
        Object value = currentValue(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<input type=\"text\" name=\"");
        writer.write(getClientId(context));
        writer.write("\" value=\"");
        if (value != null) {
            writer.write(value.toString());
        }
        writer.write("\">");

    }


}
