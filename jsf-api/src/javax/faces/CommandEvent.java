/*
 * $Id: CommandEvent.java,v 1.6 2002/01/25 18:35:06 visvan Exp $
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
 * client representation of the UICommand component.
 * <p>
 * A command event has a single property:
 * <ul>
 * <li>commandName: a String which describes the application
 *                  command to be executed as a result of this event
 *                  e.g. &quot;login&quot;, &quot;place-order&quot;, etc.
 *                  This will contain the value of the commandName
 *                  property of the UICommand component where the command
 *                  event originated.
 * </ul>
 * @see CommandListener
 */
public class CommandEvent extends FacesEvent {

    private String commandName;

    /**
     * Creates a command event.
     * @param ec EventContext object representing the event-processing
     *           phase of the request where this event originated
     * @param sourceId a String containing the id of the component
     *        where this event originated
     * @param commandName a String containing the name of the command
     *        associated with this event
     * @throws NullPointerException if sourceId or commandName is null
     */
    public CommandEvent(EventContext ec, String sourceId, String commandName) {
        super(ec, sourceId);
        this.commandName = commandName;
    }

    /**
     * This property will contain the commandName attribute value of
     * the source component where this event originated.
     * @return String containing the name of the command associated
     *         with this event
     */
    public String getCommandName() {
        return commandName;
    }

}
