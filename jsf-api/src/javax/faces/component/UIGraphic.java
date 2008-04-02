/*
 * $Id: UIGraphic.java,v 1.15 2003/02/20 22:46:12 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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

public class UIGraphic extends UIOutput {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UIGraphic";


    // ------------------------------------------------------------- Attributes


    /**
     * <p>Return the image URL for this {@link UIGraphic}.</p>
     */
    public String getURL() {

        return ((String) getAttribute("value"));

    }


    /**
     * <p>Set the image URL for this {@link UIGraphic}.</p>
     *
     * @param url The new image URL
     */
    public void setURL(String url) {

        setAttribute("value", url);

    }


    // ------------------------------------------------------------- Properties


    public String getComponentType() {

        return (TYPE);

    }


    // ---------------------------------------------------- UIComponent Methods


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
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<img src=\"");
        writer.write(src(context));
        writer.write("\">");

    }


    // -------------------------------------------------------- Private Methods


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
