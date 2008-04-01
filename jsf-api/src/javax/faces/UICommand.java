/*
 * $Id: UICommand.java,v 1.4 2002/01/25 18:35:07 visvan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.EventObject;
import javax.servlet.ServletRequest;

/**
 * Class for representing a user-interface component which allows
 * the user to execute a command.
 */
public class UICommand extends UIComponent implements EventDispatcher {
    private static String COMMAND_TYPE = "Command";
    private Vector cmdListeners = null;

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
        if ( listenerId != null ) {
             if ( cmdListeners == null ) {
                 cmdListeners = new Vector();
             }
             cmdListeners.add(listenerId);
         }
    }

    /**
     * Removes the specified listener id as a command listener
     * for this component.  
     * @param listenerId the id of the command listener
     * @throws FacesException if listenerId is not registered as a
     *         command listener for this component.
     */
    public void removeCommandListener(String listenerId) throws FacesException {
        // Assert.assert_it( cmdListeners != null );
        // PENDING ( visvan ) do assertion as per javadoc.
        if ( listenerId != null ) {
            cmdListeners.remove(listenerId);
        }
    }

    /**
     * @return Iterator containing the CommandListener instances registered
     *         for this component
     */
    public Iterator getCommandListeners() {
        return cmdListeners == null? null : cmdListeners.iterator();    
    }

    /**
     * Dispatches the specified event to any registered command listeners.
     * @param e the object describing the event
     */
    public void dispatch(EventObject e) throws FacesException {
   
        // ParameterCheck.nonNull(e);

        // if there are no listeners to dispatch just return
        if ( cmdListeners == null ) {
            return;
        }    
        CommandEvent cmd_event = null;
        if ( e instanceof CommandEvent)  {
            cmd_event = (CommandEvent) e;
        } else {
            throw new FacesException("Invalid event type. " +
                    "Expected CommandEvent");
        }

        EventContext eventContext = ((FacesEvent)cmd_event).getEventContext();
        // Assert.assert_it( eventContext != null );

        ObjectManager ot = eventContext.getObjectManager();
        //Assert.assert_it( ot != null );

        Iterator listeners = getCommandListeners();
        // Assert.assert_it( listeners != null);
        
        while ( listeners.hasNext() ) {
            String listenerName = (String) listeners.next();
            // Assert.assert_it( listenerName != null );

            ServletRequest request = eventContext.getRequest();
            CommandListener cl = (CommandListener)ot.get(request, listenerName);
            // Assert.assert_it ( cl != null );
            // Out come of the command execution will be set inside
            // the commandListener.
            try {
                NavigationHandler nh = eventContext.getNavigationHandler();
                // PENDING ( visvan ) what if there is no navigationMap
                // defined, then the handler will be null. revisit this.
                // For now comment out assertion.
                // Assert.assert_it( nh != null );
                cl.doCommand(cmd_event, nh);
            } catch ( CommandFailedException cfe ) {
                throw new FacesException ("CommandListener" + listenerName
                        + " execution unsuccessful");
            }
        }
    }
	
}
