/*
 * $Id: UITextEntry.java,v 1.4 2002/05/22 21:37:03 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.context.FacesContext;


/**
 * <p><strong>UITextEntry</strong> is a {@link UIComponent} that represents
 * a text entry field in an input form.  It is typically rendered as
 * either a single-line text field or a multi-line text box.</p>
 */

public class UITextEntry extends UIComponent {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "TextEntry";


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
        String newValue =
            context.getServletRequest().getParameter(getCompoundId());
        setValue(newValue);

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
    public void encodeBegin(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        Object value = currentValue(context);
        if (value == null) {
            value = "";
        }
        PrintWriter writer = context.getServletResponse().getWriter();
        writer.print("<input type=\"text\" name=\"");
        writer.print(getCompoundId());
        writer.print("\" value=\"");
        writer.print(value.toString());
        writer.print("\">");

    }


}
