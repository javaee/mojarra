package javax.faces;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * Class for representing a user-interface component which allows
 * the user to execute a command.
 */
public class WCommand extends WComponent {
    private static String COMMAND_TYPE = "Command";

//RWK:11-4-2001-kludge using temporarily for storing attributes...
//setAttribute/getAttribute should work differently...
    private Hashtable ht;

    public WCommand() {
        ht = new Hashtable();
    }

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
     * Returns the component attribute with the given name
     * within the specified render context or null if there is the
     * specified attribute is not set on this component.
     *
     * @param rc the render context used to render this component
     * @param attributeName a String specifying the name of the attribute
     * @return the Object bound to the attribute name, or null if the
     *          attribute does not exist.
     */
    public Object getAttribute(RenderContext rc, String attributeName) {
        return ht.get(attributeName); 
    }

    /**
     * Binds an object to the specified attribute name for this component
     * within the specified render context.
     *
     * @param rc the render context used to render this component
     * @param attributeName a String specifying the name of the attribute
     * @param value an Object representing the value of the attribute
     */
    public void setAttribute(RenderContext rc, String attributeName, 
        Object value) {
        if (attributeName != null && value != null) {
            ht.put(attributeName,value);
        }
    }

    /**
     * Returns the commandName property of this component. This
     * property will default to the name of this component.  The
     * commandName will be contained in any command events generated
     * by this component.
     * @see #addCommandListener
     * @see CommandEvent
     * @return String containing the name of the command associated
     *         with this component
     */
    public String getCommandName(RenderContext rc) {
	String commandName = (String)getAttribute(rc, "commandName");
	return commandName == null? getName() : commandName;
    }

    /**
     * Sets the commandName property of this component.
     * @param commandName String containing the name of the command
     *        associated with this component
     */
    public void setCommandName(RenderContext rc, String commandName) {
	setAttribute(rc, "commandName", commandName);
    }

    /**
     * Registers the specified listener name as a command listener
     * for this component.  The specified listener name must be registered
     * in the scoped namespace and it must be a listener which implements
     * the <code>CommandListener</code> interface, else an exception will
     * be thrown.
     * @see CommandListener
     * @see Command
     * @param listenerName the name of the command listener
     * @throws FacesException if listenerName is not registered in the
     *         scoped namespace or if the object referred to by listenerName
     *         does not implement the <code>CommandListener</code> interface.
     */
    public void addCommandListener(String listenerName) throws FacesException {
    }

    /**
     * Removes the specified listener name as a command listener
     * for this component.  
     * @param listenerName the name of the command listener
     * @throws FacesException if listenerName is not registered as a
     *         command listener for this component.
     */
    public void removeCommandListener(String listenerName) throws FacesException {
    }

    /**
     * @return Iterator containing the CommandListener instances registered
     *         for this component
     */
    public Iterator getCommandListeners() {
	return null;
    }
	
}
