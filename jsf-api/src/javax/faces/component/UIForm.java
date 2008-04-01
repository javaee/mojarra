/*
 * $Id: UIForm.java,v 1.7 2002/06/05 03:01:54 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.FormEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p><strong>UIForm</strong> is a {@link UIComponent} that represents an
 * input form to be presented to the user, and whose child components represent
 * (among other things) the input fields to be included when the form is
 * submitted.</p>
 */

public class UIForm extends UIComponent {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "Form";


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
     * <p>Enqueue a command event to the application if the incoming
     * command name matches our own.</p>
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

        // Does the form match our own name?
        String action = context.getServletRequest().getParameter("action");
        if (!"form".equals(action)) {
            return;
        }
        String name = context.getServletRequest().getParameter("name");
        if (name == null) {
            return;
        }
        if (!name.equals(currentValue(context))) {
            return;
        }

        // Enqueue a form event to the application
        context.addApplicationEvent(new FormEvent(this, name));

        // FIXME - bypass stages up to Invoke Application?

    }


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

        // Render the ending of this form
        ResponseWriter writer = context.getResponseWriter();
        writer.write("</form>");

    }


    /**
     * <p>Return the value to be rendered as the <code>action</code> attribute
     * of the form generated for this component.</p>
     *
     * @param context FacesContext for the response we are creating
     */
    private String action(FacesContext context) {

        HttpServletRequest request =
            (HttpServletRequest) context.getServletRequest();
        HttpServletResponse response =
            (HttpServletResponse) context.getServletResponse();
        StringBuffer sb = new StringBuffer(request.getContextPath());
        sb.append("/faces?action=form&name=");
        sb.append(currentValue(context)); // FIXME - null handling?
        sb.append("&tree=");
        sb.append(context.getResponseTree().getTreeId());
        return (response.encodeURL(sb.toString()));

    }


}
