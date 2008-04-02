/*
 * $Id: UIForm.java,v 1.22 2003/02/03 22:57:46 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p><strong>UIForm</strong> is a {@link UIComponent} that represents an
 * input form to be presented to the user, and whose child components represent
 * (among other things) the input fields to be included when the form is
 * submitted.</p>
 */

public class UIForm extends UIOutput {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UIForm";


    // ------------------------------------------------------------- Attributes


    /**
     * <p>Return the form name for this {@link UIForm}.</p>
     */
    public String getFormName() {

        return ((String) getAttribute("value"));

    }


    /**
     * <p>Set the form name for this {@link UIForm}.</p>
     *
     * @param formName The new form name
     */
    public void setFormName(String formName) {

        setAttribute("value", formName);

    }

    
    // ------------------------------------------------------------- Properties


    public String getComponentType() {

        return (TYPE);

    }


    // ---------------------------------------------------- UIComponent Methods


    public void encodeBegin(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Delegate to our associated Renderer if needed
        if (getRendererType() != null) {
            super.encodeBegin(context);
            return;
        }

        // if rendered is false, do not perform default encoding.
        if (!isRendered()) {
            return;
        }

        // Render the beginning of this form
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<form action=\"");
        writer.write(action(context));
        writer.write("\" method=\"post\">");

    }


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

        // Render the ending of this form
        ResponseWriter writer = context.getResponseWriter();
        writer.write("</form>");

    }


    // -------------------------------------------------------- Private Methods


    /**
     * <p>Return the value to be rendered as the <code>action</code> attribute
     * of the form generated for this component.</p>
     *
     * @param context FacesContext for the response we are creating
     */
    private String action(FacesContext context) {

        HttpServletRequest request = (HttpServletRequest)
            context.getServletRequest();
        HttpServletResponse response = (HttpServletResponse)
            context.getServletResponse();
        String treeId = context.getTree().getTreeId();
        StringBuffer sb = new StringBuffer(request.getContextPath());
        sb.append("/faces");
        sb.append(treeId);
        return (response.encodeURL(sb.toString()));


    }


}
