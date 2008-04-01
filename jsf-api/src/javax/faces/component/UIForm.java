/*
 * $Id: UIForm.java,v 1.15 2002/07/31 01:42:03 craigmcc Exp $
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
     * <p>Path Info prefix that indicates a form submit.</p>
     */
    static final String PREFIX = "/faces/form";


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
     * <p>Render the beginning of the current value of this component.</p>
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

        // Render the beginning of this form
        String value = (String) getFormName();
        if (value == null) {
            throw new NullPointerException();
        }
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<form method=\"post\" action=\"");
        writer.write(action(context));
        writer.write("\">");

    }


    /**
     * <p>Render the ending of the current value of this component.</p>
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

        Object value = currentValue(context);

        HttpServletRequest request =
            (HttpServletRequest) context.getServletRequest();
        HttpServletResponse response =
            (HttpServletResponse) context.getServletResponse();
        StringBuffer sb = new StringBuffer(request.getContextPath());
        sb.append(PREFIX);
        if (value != null) {
            String formName = value.toString();
            if (!formName.startsWith("/")) {
                sb.append("/");
            }
            sb.append(formName);
        }
        return (response.encodeURL(sb.toString()));

    }


}
