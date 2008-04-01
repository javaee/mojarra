/*
 * $Id: UICommand.java,v 1.2 2002/05/08 01:11:46 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


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

public abstract class UICommand extends UIComponent {


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "Command";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the command name (or the <code>compoundId</code> if no command
     * name has been specified).</p>
     *
     * <p><strong>FIXME</strong> - Should the default value be
     * just the <code>componentId</code> property instead?</p>
     */
    public abstract String getName();


    /**
     * <p>Set the command name for this <code>UICommand</code>.</p>
     *
     * @param name The new command name
     *
     * @exception NullPointerException if <code>name</code> is
     *  <code>null</code>
     */
    public abstract void setName(String name);

    
}
