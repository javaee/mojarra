package javax.faces;

/**
 * Class for representing a form user-interface component. 
 * A form encapsulates the process of taking a collection of
 * input from the user and submitting it to the application
 * in a single submit operation.  
 */
public class WForm extends WComponent {

    private static String TYPE = "Form";

    /** 
     * Returns a String representing the form's type.  
     *
     * @return a String object containing &quot;Form&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

}
