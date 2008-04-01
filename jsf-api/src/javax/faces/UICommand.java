/*
 * $Id: UICommand.java,v 1.3 2002/01/17 02:15:02 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * Class for representing a user-interface component which allows
 * the user to execute a command.
 */
public class UICommand extends UIComponent {
    private static String COMMAND_TYPE = "Command";

    /** 
     * Returns a String representing the this component type.  
     *
     * @return a String object containing &quot;Command&quot;
     *         
     */
    public String getType() {
	return COMMAND_TYPE;
    }

    /**
     * Returns the &quot;commandName&quot; attribute of this component. A single
     * commandName value may be shared across multiple UICommand instances.
     * This attribute will default to the id of this component.  The
     * commandName will be contained in any command events generated
     * by this component.
     * @see #setCommandName
     * @see #addCommandListener
     * @see CommandEvent
     * @return String containing the name of the command associated
     *         with this component
     */
    public String getCommandName() {
	String commandName = (String)getAttribute(null, "commandName");
	return commandName == null? getId() : commandName;
    }

    /**
     * Sets the &quot;commandName&quot; attribute of this component.
     * @see #getCommandName
     * @param commandName String containing the name of the command
     *        associated with this component
     */
    public void setCommandName(String commandName) {
	setAttribute("commandName", commandName);
    }

    /**
     * Registers the specified listener id as a command listener
     * for this component.  The specified listener id must be registered
     * in the scoped namespace and it must be a listener which implements
     * the <code>CommandListener</code> interface, else an exception will
     * be thrown.
     * @see CommandListener
     * @param listenerId the id of the command listener
     * @throws FacesException if listenerId is not registered in the
     *         scoped namespace or if the object referred to by listenerId
     *         does not implement the <code>CommandListener</code> interface.
     */
    public void addCommandListener(String listenerId) throws FacesException {
    }

    /**
     * Removes the specified listener id as a command listener
     * for this component.  
     * @param listenerId the id of the command listener
     * @throws FacesException if listenerId is not registered as a
     *         command listener for this component.
     */
    public void removeCommandListener(String listenerId) throws FacesException {
    }

    /**
     * @return Iterator containing the CommandListener instances registered
     *         for this component
     */
    public Iterator getCommandListeners() {
	return null;
    }
	
}
