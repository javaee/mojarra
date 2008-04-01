package javax.faces;


/**
 * Class for representing a user-interface component which displays
 * a graphic to the user.  This component type is not interactive -
 * a user cannot directly manipulate this component.
 */
public class UIGraphic extends UIComponent {

    private static String TYPE = "Graphic";

    private String modelReference;

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
     * The model-reference property for this data-bound component.
     * This property contains a reference to the object which acts
     * as the data-source for this component.  The model-reference
     * must resolve to an object which implements one of the following types:
     * <ul>
     * <li><code>java.lang.String</code>: path to image file or url
     * <li><code>java.awt.Image</code> of <code>String</code>
     * </ul>  
     * @see #setModelReference  
     * @return String containing the model-reference for this component
     */
    public String getModelReference() {
        return modelReference;
    }

    /**
     * Sets the model-reference property on this data-bound component.
     * @see #getModelReference
     * @param modelReference the String which contains a reference to the
     *        object which acts as the data-source for this component
     */
    public void setModelReference(String modelReference) {
        this.modelReference = modelReference;
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
    public String getImagePath() {
	return null; //compile
    }

    /**
     * Sets the local value of the &quot;imagePath&quot; attribute.
     * @see #getImagePath
     * @param imagePath String containing the path to the image file or url
     */
    public void setImagePath(String imagePath) {
    }
}
