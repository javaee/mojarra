/*
 * $Id: UIGraphic.java,v 1.6 2002/05/22 21:37:02 craigmcc Exp $
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
import javax.servlet.http.HttpServletResponse;


/**
 * <p><strong>UIGraphic</strong> is a {@link UIComponent} that displays
 * a graphical image to the user.  The user cannot manipulate this component;
 * it is for display purposes only.</p>
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
        PrintWriter writer = context.getServletResponse().getWriter();
        writer.print("<img src=\"");
        writer.print(src(context));
        writer.print("\">");

    }


    /**
     * <p>Return the value to be rendered as the <code>src</code> attribute
     * of the image element generated for this component.</p>
     *
     * @param context FacesContext for the response we are creating
     */
    private String src(FacesContext context) {

        String value = (String) currentValue(context);
        if (value == null) {
            value = "";
        }
        HttpServletRequest request =
            (HttpServletRequest) context.getServletRequest();
        HttpServletResponse response =
            (HttpServletResponse) context.getServletResponse();
        StringBuffer sb = new StringBuffer();
        if (value.startsWith("/")) {
            sb.append(request.getContextPath());
        }
        sb.append(currentValue(context));
        return (response.encodeURL(sb.toString()));

    }


}
