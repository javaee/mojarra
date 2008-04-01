/*
 * $Id: UICommand.java,v 1.9 2002/06/03 19:34:26 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.context.FacesContext;
import javax.faces.event.CommandEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p><strong>UICommand</strong> is a {@link UIComponent} that represents
 * a user interface component which, when activated by the user, triggers
 * an application specific "command" or "action".  Such a component is
 * typically rendered as a push button, a menu item, or a hyperlink.</p>
 */

public class UICommand extends UIComponent {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "Command";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the command name associated with this command.</p>
     */
    public String getCommandName() {

        return ((String) getAttribute("value"));

    }


    /**
     * <p>Set the command name for this <code>UICommand</code>.</p>
     *
     * @param commandName The new command name
     */
    public void setCommandName(String commandName) {

        setAttribute("value", commandName);

    }

    
    /**
     * <p>Return the component type of this <code>UIComponent</code>.</p>
     */
    public String getComponentType() {

        return (TYPE);

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
        if (!"command".equals(action)) {
            return;
        }
        String name = context.getServletRequest().getParameter("name");
        if (name == null) {
            return;
        }
        if (!name.equals(currentValue(context))) {
            return;
        }

        // Enqueue a command event to the application
        context.addApplicationEvent(new CommandEvent(this, name));

        // FIXME - bypass stages up to Invoke Application?

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
    public void encodeBegin(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        PrintWriter writer = context.getServletResponse().getWriter();
        writer.print("<a href=\"");
        writer.print(href(context));
        writer.print("\">");
        writer.print(currentValue(context));
        writer.print("</a>");

    }


    /**
     * <p>Return the value to be rendered as the <code>href</code> attribute
     * of the hyperlink generated for this component.</p>
     *
     * @param context FacesContext for the response we are creating
     */
    private String href(FacesContext context) {

        HttpServletRequest request =
            (HttpServletRequest) context.getServletRequest();
        HttpServletResponse response =
            (HttpServletResponse) context.getServletResponse();
        StringBuffer sb = new StringBuffer(request.getContextPath());
        sb.append("/faces?action=command&name=");
        sb.append(currentValue(context)); // FIXME - null handling?
        sb.append("&tree=");
        sb.append(context.getResponseTree().getTreeId());
        return (response.encodeURL(sb.toString()));

    }


}
