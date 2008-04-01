/*
 * $Id: CommandEvent.java,v 1.3 2001/12/20 22:25:44 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import javax.servlet.ServletRequest;

/**
 * The class which encapsulates information associated
 * with a command event.  Command events are typically generated
 * as a result of a user invoking the command action on the
 * client representation of the WCommand component.
 * <p>
 * A command event has a single property:
 * <ul>
 * <li>commandName: a String which describes the application
 *                  command to be executed as a result of this event
 *                  e.g. &quot;login&quot;, &quot;place-order&quot;, etc.
 *                  This will contain the value of the commandName
 *                  property of the WCommand component where the command
 *                  event originated.
 * </ul>
 * @see CommandListener
 */
public class CommandEvent extends FacesEvent {

    private String commandName;

    /**
     * Creates a command event.
     * @param request the ServletRequest object where this event was derived
     * @param sourceName a String containing the name of the component 
     *        where this event originated
     * @param commandName a String containing the name of the command
     *        associated with this event
     * @throws NullPointerException if sourceName or commandName is null
     */
    public CommandEvent(ServletRequest request, String sourceName, String commandName) {
	super(request, sourceName);
	this.commandName = commandName;
    }

    /**
     * @return String containing the name of the command associated
     *         with this event
     */
    public String getCommandName() {
	return commandName;
    }
}
