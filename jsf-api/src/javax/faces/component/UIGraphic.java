/*
 * $Id: UIGraphic.java,v 1.4 2002/05/18 20:33:46 craigmcc Exp $
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
 * <p><strong>UIGraphic</strong> is a {@link UIComponent} that can display
 * a graphical image to the user.  The user cannot manipulate this component;
 * it is for display purposes only.</p>
 *
 * <p>The local value of the URL of the image that is to be displayed by
 * this component is stored in the <code>value</code> property, and
 * must be a <code>java.lang.String</code> (as must the model property
 * corresponding to any model reference for this component).  This URL is
 * interpreted as a context-relative path if it starts with a slash ('/')
 * character; otherwise, it is interpreted as an absolute or relative path
 * that should be used unchanged.</p>
 *
 * <p>For convenience, the local value of the image URL is accessible
 * via the <code>getImagePath()</code> and <code>setImagePath()</code>
 * methods.  The <code>currentValue()</code> method should be used to
 * retrieve the value to be rendered.</p>
 *
 * <h3>Default Behavior</h3>
 *
 * <p>In the absence of a Renderer performing more sophisticated processing,
 * this component supports the following functionality:</p>
 * <ul>
 * <li><em>encodeBegin()</em> - Create an HTML <code>&lt;img&gt;</code>
 *     element, with a <code>src</code> attribute based on the current
 *     value stored in the component.</li>
 * </ul>
 */

public class UIGraphic extends UIComponent {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "Graphic";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return the local value of the image path.</p>
     */
    public String getImagePath() {

        return ((String) getAttribute("value"));

    }


    /**
     * <p>Set the local value of the image path.</p>
     *
     * @param imagePath The new image path
     */
    public void setImagePath(String imagePath) {

        setAttribute("value", imagePath);

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
        String value = (String) currentValue();
        if (value == null) {
            throw new NullPointerException();
        }
        PrintWriter writer = context.getServletResponse().getWriter();
        writer.print("<img src=\"");
        if (value.startsWith("/")) {
            HttpServletRequest request =
                (HttpServletRequest) context.getServletRequest();
            writer.print(request.getContextPath());
        }
        writer.print(value);
        writer.print("\">");

    }


}
