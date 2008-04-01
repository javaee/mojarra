package javax.faces;

/**
 * Class for representing a user-interface component which displays
 * output to the user.  This component type is not interactive -
 * a user cannot directly manipulate this component.
 */
public class WOutput extends WComponent {
    private static String TYPE = "Output";

    /** 
     * Returns a String representing the this component type.  
     *
     * @return a String object containing &quot;Output&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

    public Object getValue() {
	return null;
    }

    public void setValue(Object value) {
    }

}
