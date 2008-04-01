package javax.faces;

/**
 * Class for representing a user-interface component which allows
 * the user to execute a command.
 */
public class WCommand extends WComponent {
    private static String TYPE = "Command";

    /** 
     * Returns a String representing the this component type.  
     *
     * @return a String object containing &quot;Command&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

}
