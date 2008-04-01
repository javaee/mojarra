/*
 * $Id: UICommand.java,v 1.5 2002/05/17 04:55:39 craigmcc Exp $
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
 * <p><strong>UICommand</strong> is a {@link UIComponent} that represents
 * a command to be executed at the request of a user.  Typically, a
 * <code>UICommand</code> will be rendered as a pushbutton, menu item,
 * or hyperlink in a web application.</p>
 *
 * <h3>Properties</h3>
 *
 * <p>Each <code>UICommand</code> instance supports the following JavaBean
 * properties to describe its render-independent characteristics:</p>
 * <ul>
 * <li><strong>name</strong> (java.lang.String) - Command name associated
 *     with this command.  Command names need not be unique, as many
 *     applications will wish to trigger the same application action
 *     no matter which of several alternative <code>UICommand</code>
 *     components the user chose to activate.</li>
 * </ul>
 *
 * <h3>Default Behavior</h3>
 *
 * <p>In the absence of a Renderer performing more sophisticated processing,
 * this component supports the following functionality:</p>
 * <ul>
 * <li><em>decode()</em> - Enqueue a {@link CommandEvent} to the application,
 *     to pass the command name that was selected, if the command name
 *     included in the request matches our own.</li>
 * <li><em>encode()</em> - Render an HTML hyperlink, with a context-relative
 *     URL of <code>/faces?action=command&name=xxxxx&tree=yyyyy</code>,
 *     where "xxxxx" is the command name of this command, and "yyyyy" is
 *     the tree ID of the response tree that we are rendering.</li>
 * </ul>
 *
 * <p><strong>FIXME</strong> - The above mechanism assumes that a
 * JavaServer Faces implementation is using a servlet as the implementation
 * mechanism, mapped to the context-relative URL pattern "/faces".</p>
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

        return ((String) getAttribute("commandName"));

    }


    /**
     * <p>Set the command name for this <code>UICommand</code>.</p>
     *
     * @param commandName The new command name
     *
     * @exception NullPointerException if <code>name</code> is
     *  <code>null</code>
     */
    public void setCommandName(String commandName) {

        if (commandName == null) {
            throw new NullPointerException("setCommandName");
        }
        setAttribute("commandName", commandName);

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
        if (!name.equals(getCommandName())) {
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
    public void encode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }
        String value = (String) getCommandName();
        if (value == null) {
            throw new NullPointerException();
        }
        PrintWriter writer = context.getServletResponse().getWriter();
        writer.print("<a href=\"");
        HttpServletRequest request =
            (HttpServletRequest) context.getServletRequest();
        writer.print(request.getContextPath());
        writer.print("/faces?action=command&name=");
        writer.print(value); // FIXME - URL encode?
        writer.print("&tree=");
        writer.print(context.getResponseTree().getTreeId()); // FIXME - URL encode?
        writer.print("\">");

    }


}
