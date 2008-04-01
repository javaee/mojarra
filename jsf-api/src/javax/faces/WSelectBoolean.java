package javax.faces;

/**
 * Class for representing a user-interface component which allows
 * the user to select a boolean value.
 */
public class WSelectBoolean extends WComponent {
    private static String TYPE = "SelectBoolean";

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
     * @param value the Boolean object containing the true/valse value
     * @throws NullPointerException if value is null
     * @throws FacesException if model is not one of the valid instances
     */
    public void setModel(Object model) {}

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

}
