package javax.faces;

import java.util.Hashtable;

/**
 * Class for representing a user-interface component which allows
 * the user to select a boolean value.
 */
public class WSelectBoolean extends WComponent {
    private static String TYPE = "SelectBoolean";

//RWK:11-4-2001-kludge using temporarily for storing attributes...
//setAttribute/getAttribute should work differently...
    private Hashtable ht;

    public WSelectBoolean() {
        ht = new Hashtable();
    }

    /** 
     * Returns a String representing the select-boolean type.  
     *
     * @return a String object containing &quot;SelectBoolean&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

    // Aim10-30-01: the model is of type Object to give us
    // flexibility
    /**
     * Returns an Object which models the value of this component.
     * This object will be an instance of one of the following:
     * <ul>
     * <li>Boolean
     * </ul>
     * The component will maintain a reference to this object
     * (the value will not be copied).
     *
     * @return Object which models the value of this component
     */
    public Object getModel() {
        return null;
    }

    /**
     * Sets the model of this component to the specified object.
     * This object must be an instance of one of the following:
     * <ul>
     * <li>Boolean
     * </ul>
     *
     * @param value the Boolean object containing the true/false value
     * @throws NullPointerException if value is null
     * @throws FacesException if model is not one of the valid instances
     */
    public void setModel(Object model) throws FacesException {}

    /**
     * Returns boolean value indicating whether the state of this
     * component is selected (true) or unselected (false).  This
     * component will derive this value from its model object.
     * @return boolean value representing the selected state of this
     *         component.
     */
    public boolean isSelected() {
	return false;
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

