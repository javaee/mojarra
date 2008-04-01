package javax.faces;

import java.util.Hashtable;

/**
 * Class for representing a user-interface component which allows
 * the user to execute a command.
 */
public class WCommand extends WComponent {
    private static String TYPE = "Command";

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
	return TYPE;
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
}
