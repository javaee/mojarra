/*
 * $Id: UIOutput.java,v 1.6 2002/05/22 17:47:26 craigmcc Exp $
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
 * <p><strong>UIOutput</strong> is a {@link UIComponent} that can display
 * output to the user.  The user cannot manipulate this component; it is
 * for display purposes only.  Any localization of the text to be rendered
 * is the responsibility of the application.</p>
 *
 * <p>The local value of the text to be displayed by this component
 * is stored in the <code>value</code> property, and must be a
 * <code>java.lang.String</code> (as must the model property corresponding
 * to any model reference for this component).</p>
 *
 * <p>For convenience, the local value of the text to be displayed is
 * accessible via the <code>getText()</code> and <code>setText()</code>
 * methods.  The <code>currentValue()</code> method should be used to
 * retrieve the value to be rendered.</p>
 *
 * <h3>Default Behavior</h3>
 *
 * <p>In the absence of a Renderer performing more sophisticated processing,
 * this component supports the following functionality:</p>
 * <ul>
 * <li><em>encodeBegin()</em> - Render the current value of this component
 *     directly to the response.</li>
 * </ul>
 */

public class UIOutput extends UIComponent {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "Output";


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
        String value = (String) currentValue(context);
        if (value == null) {
            throw new NullPointerException();
        }
        PrintWriter writer = context.getServletResponse().getWriter();
        writer.print(value);

    }


}
