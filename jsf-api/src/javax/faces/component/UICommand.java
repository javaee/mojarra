/*
 * $Id: UICommand.java,v 1.4 2002/05/15 18:20:07 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import javax.faces.context.FacesContext;


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


}
