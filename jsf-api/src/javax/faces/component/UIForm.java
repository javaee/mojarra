/*
 * $Id: UIForm.java,v 1.13 2002/07/29 00:47:05 craigmcc Exp $
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
import javax.faces.event.FormEvent;
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
    private static final String PREFIX = "/faces/form";


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
     * <p>Enqueue a {@link FormEvent} event to the application identifying
     * the form submission that has occurred, along with the command name
     * of the {@link UICommand} that caused the form to be submitted, if any.
     * </p>
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

        // Delegate to our associated Renderer if needed
        if (getRendererType() != null) {
            super.decode(context);
            return;
        }

        // Does the extra path info on this request identify a form submit?
        // for this UIForm component?
        String pathInfo = // FIXME - HTTP dependency
            ((HttpServletRequest) context.getServletRequest()).getPathInfo();
        if (pathInfo == null) {
            return;
        }
        if (!pathInfo.startsWith(PREFIX)) {
            return;
        }
        String formName = pathInfo.substring(PREFIX.length());
        int slash = pathInfo.indexOf('/');
        if (slash >= 0) {
            formName = formName.substring(0, slash);
        }
        if (!formName.equals(currentValue(context))) {
            return;
        }

        // Which of our nested UICommand children triggered this submit?
        // FIXME - assumes commandName won't have name clash with components!
        String commandName =
            extract(context, context.getServletRequest(), this);

        // Enqueue a form event to the application
        context.addApplicationEvent
            (new FormEvent(this, formName, commandName));

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
            sb.append(URLEncoder.encode(formName));
        }
        return (response.encodeURL(URLEncoder.encode(sb.toString())));

    }


    /**
     * <p>Extract the command name of the child {@link UICommand} that
     * caused this form to be submitted.  The specified component, and
     * all of its children, are to be checked.</p>
     *
     * @param context FacesContext for the request we are processing
     * @param request ServletRequest we are processing
     * @param component Component to be checked
     */
    private String extract(FacesContext context, ServletRequest request,
                           UIComponent component) {

        // Check the current component
        if (component instanceof UICommand) {
            Object value = component.currentValue(context);
            if (value != null) {
                String commandName = value.toString();
                if (request.getParameter(commandName) != null) {
                    return (commandName);
                }
            }
        }

        // Check the children of the current component
        Iterator kids = component.getChildren();
        while (kids.hasNext()) {
            String commandName =
                extract(context, request, (UIComponent) kids.next());
            if (commandName != null) {
                return (commandName);
            }
        }

        // No matching command name was found
        return (null);

    }


}
