package javax.faces;


/**
 * Class for representing a user-interface component which displays
 * a graphic to the user.  This component type is not interactive -
 * a user cannot directly manipulate this component.
 */
public class UIGraphic extends UIComponent {

    private static String TYPE = "Graphic";

    /** 
     * Returns a String representing the this component type.  
     *
     * @return a String object containing &quot;Graphic&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

    /**
     * The &quot;imagePath&quot; attribute.
     * If a local attribute value has been set, that value is returned,
     * else if the model-reference has been set, that reference is
     * resolved and the associated model object is converted to a
     * String value and returned, else null is returned.
     * @see #getImagePath
     * @return String containing the path to the image file or url
     */
    public String getImagePath(FacesContext fc) {
	return (String) getValue(fc);
    }

    /**
     * Sets the local value of the &quot;imagePath&quot; attribute.
     * @see #getImagePath
     * @param imagePath String containing the path to the image file or url
     */
    public void setImagePath(String imagePath) {
	setValue(imagePath);
    }
}
