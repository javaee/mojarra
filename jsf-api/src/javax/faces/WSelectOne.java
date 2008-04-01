package javax.faces;

/**
 * Class for representing a user-interface component which allows
 * the user to select one value from many.
 */
public class WSelectOne extends WComponent {
    private static String TYPE = "SelectOne";

    /** 
     * Returns a String representing the select-one type.  
     *
     * @return a String object containing &quot;SelectOne&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

}
