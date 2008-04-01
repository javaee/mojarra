/*
 * $Id: UIForm.java,v 1.3 2002/05/17 22:57:13 craigmcc Exp $
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
 * <p><strong>UIForm</strong> is a {@link UIComponent} that represents a
 * form to be submitted at the request of a user.  Typically, a
 * <code>UIForm</code> will be rendered as a form element, in a manner
 * appropriate to the markup language in use.</p>
 *
 * <h3>Properties</h3>
 *
 * <p>Each <code>UIForm</code> instance supports the following JavaBean
 * properties to describe its render-independent characteristics:</p>
 * <ul>
 * <li><strong>formName</strong> (java.lang.String) - Form name associated
 *     with this form.  Form names should be unique on a particular
 *     request, so that the application can tell which business logic
 *     should be processed based on the form name.</li>
 * </ul>
 *
 * <h3>Default Behavior</h3>
 *
 * <p>In the absence of a Renderer performing more sophisticated processing,
 * this component supports the following functionality:</p>
 * <ul>
 * <li><em>decode()</em> - Enqueue a {@link FormEvent} to the application,
 *     to pass the form name that was selected, if the form name
 *     included in the request matches our own.</li>
 * <li><em>encode()</em> - Render an HTML form, with a context-relative
 *     URL of <code>/faces?action=form&name=xxxxx&tree=yyyyy</code>,
 *     where "xxxxx" is the form name of this form, and "yyyyy" is
 *     the tree ID of the response tree we are rendering.</li>
 * </ul>
 *
 * <p><strong>FIXME</strong> - We cannot do the encode part correctly without
 * updating the APIs for child rendering support!</p>
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

        return ((String) getAttribute("formName"));

    }


    /**
     * <p>Set the form name for this <code>UIForm</code>.</p>
     *
     * @param formName The new form name
     *
     * @exception NullPointerException if <code>name</code> is
     *  <code>null</code>
     */
    public void setFormName(String formName) {

        if (formName == null) {
            throw new NullPointerException("setFormName");
        }
        setAttribute("formName", formName);

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

        // Does the command match our own name?
        String action = context.getServletRequest().getParameter("action");
        if (!"form".equals(action)) {
            return;
        }
        String name = context.getServletRequest().getParameter("name");
        if (name == null) {
            return;
        }
        if (!name.equals(getFormName())) {
            return;
        }

        // Enqueue a form event to the application
        context.addApplicationEvent(new FormEvent(this, name));

    }


    /**
     * <p>Render the current value of this component.</p>
     *
     * @param context FacesContext for the response we are creating
     *
     * @exception IOException if an input/output error occurs while rendering
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void encode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Render the beginning of this form
        String value = (String) getFormName();
        if (value == null) {
            throw new NullPointerException();
        }
        PrintWriter writer = context.getServletResponse().getWriter();
        writer.print("<form method=\"post\" action=\"");
        HttpServletRequest request =
            (HttpServletRequest) context.getServletRequest();
        writer.print(request.getContextPath());
        writer.print("/faces?action=form&name=");
        writer.print(value); // FIXME - URL encode?
        writer.print("&tree=");
        writer.print(context.getResponseTree().getTreeId()); // FIXME - URL encode?
        writer.print("\">");

        // FIXME - Render the nested content of this form

        // FIXME - Render the ending of this form
        writer.print("</form>");

    }


}
