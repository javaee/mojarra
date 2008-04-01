/*
 * $Id: UICommand.java,v 1.19 2002/08/15 20:12:27 craigmcc Exp $
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
        setValid(true);
        String value = context.getServletRequest().
            getParameter(getCompoundId());
        if (!"submit".equals(value)) {
            return;
        }

        // Construct and enqueue a FormEvent for the application
        String commandName = (String) currentValue(context);
        String formName = null;
        UIComponent parent = getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                formName = (String) parent.currentValue(context);
                break;
            }
            parent = parent.getParent();
        }
        if (formName == null) {
            return; // Not nested in a form
        }
        context.addApplicationEvent
            (new FormEvent(this, formName, commandName));

    }


    /**
     * <p>Render the beginning of an HTML submit button.</p>
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

        // Perform default encoding
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<button name=\"");
        writer.write(getCompoundId());
        writer.write("\" type=\"submit\" value=\"submit\">\n");

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
        writer.write("</button>\n");

    }


    /**
     * <p>Suppress model updates for this component.</p>
     *
     * @param context FacesContext for the request we are processing
     *
     * @exception FacesException if an error occurs during execution
     *  of the <code>setModelValue()</code> method
     * @exception IllegalArgumentException if the <code>modelReference</code>
     *  property has invalid syntax for an expression
     * @exception NullPointerException if <code>context</code>
     *  is <code>null</code>
     */
    public void updateModel(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        ; // No action required

    }


}
