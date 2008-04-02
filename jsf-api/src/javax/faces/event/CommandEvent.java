/*
 * $Id: CommandEvent.java,v 1.9 2003/02/20 22:46:28 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;


/**
 * <p><strong>CommandEvent</strong> is a subclass of {@link ApplicationEvent}
 * that indicates that a particular {@link UICommand} was selected by the user.
 * It is queued to the application, for processing during the
 * <em>Invoke Application</em> phase of the request processing lifecycle.
 * </p>
 *
 * @deprecated The current mechanism for handling application events is a
 *  placeholder, and will be replaced in the next public release of
 *  JavaServer Faces.
 */

public class CommandEvent extends ApplicationEvent {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new event object from the specified source component.</p>
     *
     * @param component Source {@link UIComponent} for this event
     * @param commandName Command name of the command this event signifies
     *
     * @exception IllegalArgumentException if <code>component</code> is
     *  <code>null</code>
     * @exception NullPointerException if <code>commandName</code>
     *  is <code>null</code>
     */
    public CommandEvent(UIComponent source, String commandName) {

        super(source);
        if (commandName == null) {
            throw new NullPointerException();
        }
        this.commandName = commandName;

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>The command namd whose selection this event signifies.</p>
     */
    protected String commandName = null;


    /**
     * <p>Return the command name of the {@link UICommand} whose selection
     * this event signifies.</p>
     */
    public String getCommandName() {

        return (commandName);

    }


    public String toString() {

        StringBuffer sb = new StringBuffer("CommandEvent[source=");
        sb.append(getSource());
        sb.append(",commandName=");
        sb.append(getCommandName());
        sb.append("]");
        return (sb.toString());

    }


}
