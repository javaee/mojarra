/*
 * $Id: UIGraphic.java,v 1.10 2002/07/15 18:08:24 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p><strong>UIGraphic</strong> is a {@link UIComponent} that displays
 * a graphical image to the user.  The user cannot manipulate this component;
 * it is for display purposes only.</p>
 */

public class UIGraphic extends UIComponentBase {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UIGraphic";


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
    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<img src=\"");
        writer.write(src(context));
        writer.write("\">");

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
