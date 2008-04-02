/*
 * $Id: CommandEvent.java,v 1.5 2002/09/20 02:30:17 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;


/**
 * <p><strong>CommandEvent</strong> is a subclass of {@link FacesEvent} that
 * indicates that a particular {@link UICommand} was selected by the user.
 * It is queued to the application, for processing during the
 * <em>Invoke Application</em> phase of the request processing lifecycle.</p>
 */

public class CommandEvent extends FacesEvent {


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


    /**
     * <p>Return a String rendition of this object value.</p>
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("CommandEvent[source=");
        sb.append(getSource());
        sb.append(",commandName=");
        sb.append(getCommandName());
        sb.append("]");
        return (sb.toString());

    }


}
