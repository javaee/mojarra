/*
 * $Id: UITextEntry.java,v 1.1 2002/05/17 04:55:39 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;


/**
 * <p><strong>UITextEntry</strong> is a {@link UIComponent} that can display
 * output to, and then accept input text from, the user.  It is typically
 * rendered as either a single-line text field or a multi-line text box.</p>
 *
 * <p>The local value of the text to be displayed by this component
 * is stored in the <code>value</code> property, and must be a
 * <code>java.lang.String</code> (as must the model property corresponding
 * to any model reference for this component).</p>
 *
 * <p>For convenience, the local value of the text value is
 * accessible via the <code>getText()</code> and <code>setText()</code>
 * methods.  The <code>currentValue()</code> method should be used to
 * retrieve the value to be rendered.</p>
 *
 * <h3>Default Behavior</h3>
 *
 * <p>In the absence of a Renderer performing more sophisticated processing,
 * this component supports the following functionality:</p>
 * <ul>
 * <li><em>decode()</em> - Copy the value of the request parameter that
 *     corresponds to this field to the local value.  If there is no such
 *     parameter, set the local value to a zero-length String.</li>
 * <li><em>encode()</em> - Render the current value of this component
 *     as a single-line text field.</li>
 * </ul>
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
        if (newValue == null) {
            newValue = "";
        }
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
    public void encode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String value = (String) currentValue();
        if (value == null) {
            value = "";
        }
        PrintWriter writer = context.getServletResponse().getWriter();
        writer.print("<input type=\"text\" name=\"");
        writer.print(getCompoundId());
        writer.print("\" value=\"");
        writer.print(value);
        writer.print("\">");

    }


}
