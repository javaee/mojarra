/*
 * $Id: UIForm.java,v 1.19 2002/12/03 23:02:01 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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

public class UIForm extends UIComponentBase {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UIForm";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return the form name associated with this form.</p>
     */
    public String getFormName() {

        return ((String) getAttribute("value"));

    }


    /**
     * <p>Set the form name for this <code>UIForm</code>.</p>
     *
     * @param formName The new form name
     */
    public void setFormName(String formName) {

        setAttribute("value", formName);

    }

    
    // ------------------------------------------- Lifecycle Processing Methods


    /**
     * <p>Render the beginning of the current value of this component 
     * if the value of the rendered attribute is <code>true</code>. </p>
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


    /**
     * <p>Render the ending of the current value of this component, 
     * if the value of the rendered attribute is <code>true</code>. </p>
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

        // Render the ending of this form
        ResponseWriter writer = context.getResponseWriter();
        writer.write("</form>");

    }


    /**
     * <p>Suppress model updates for this component.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception IllegalArgumentException if the <code>modelReference</code>
     *  property has invalid syntax for an expression
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public boolean updateModel(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        return (true);

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
        String treeId = context.getResponseTree().getTreeId();
        StringBuffer sb = new StringBuffer(request.getContextPath());
        sb.append("/faces");
        sb.append(treeId);
        return (response.encodeURL(sb.toString()));


    }


}
