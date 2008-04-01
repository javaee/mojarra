/*
 * $Id: UICommand.java,v 1.17 2002/07/31 01:42:02 craigmcc Exp $
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
 * <p><strong>UICommand</strong> is a {@link UIComponent} that represents
 * a user interface component which, when activated by the user, triggers
 * an application specific "command" or "action".  Such a component is
 * typically rendered as a push button, a menu item, or a hyperlink.</p>
 */

public class UICommand extends UIComponentBase {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UICommand";


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

        // Was our command the one that caused this submission?
        Object value = currentValue(context);
        String commandName = null;
        if (value != null) {
            commandName = value.toString();
            if (context.getServletRequest().getParameter(commandName) ==
                null) {
                return;
            }
        } else {
            return;
        }

        // Does the extra path info on this request identify a form submit?
        String pathInfo = (String)
          context.getServletRequest().getAttribute
          ("javax.servlet.include.path_info");
        if (pathInfo == null) {
          pathInfo =
            ((HttpServletRequest) context.getServletRequest()).getPathInfo();
        }
        if (pathInfo == null) {
            return;
        }
        if (!pathInfo.startsWith(UIForm.PREFIX)) {
            return;
        }
        String formName = pathInfo.substring(UIForm.PREFIX.length() + 1);

        // Enqueue a form event to the application
        context.addApplicationEvent
            (new FormEvent(this, formName, commandName));

    }


    /**
     * <p>Render the current value of this component as an HTML submit
     * button.</p>
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

        // Perform default encoding
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<input type=\"submit\"");
        Object currentValue = currentValue(context);
        if (currentValue != null) {
            writer.write(" name=\"");
            writer.write(currentValue.toString());
            writer.write("\"");
        }
        writer.write(">");

    }


}
