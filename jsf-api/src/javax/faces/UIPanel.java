package javax.faces;


/**
 * Class for representing a user-interface component which is
 * responsible for laying out its child components in a methodology
 * defined by a renderer on a specific render kit. 
 */
public class UIPanel extends UIComponent {

    private static String TYPE = "Panel";

    /** 
     * Returns a String representing the panel's type.  
     *
     * @return a String object containing &quot;Panel&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

    /**
     * The rendersChildren attribute.  Returns <code>true</code>.
     * 
     * @param rc the faces context used to render this component, or null
     * @return boolean value indicating whether or not this component
     *         takes responsibility for laying out and rendering its
     *         children.
     */
    public boolean getRendersChildren(FacesContext fc) {
	return true;
    }
}
