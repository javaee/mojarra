package javax.faces;

import java.util.Hashtable;

/**
 * Class for representing a user-interface component which displays
 * output to the user.  This component type is not interactive -
 * a user cannot directly manipulate this component.
 */
public class WOutput extends WComponent {

    private static String TYPE = "Output";
    private Object value = null;
    private Object model = null;

    // PENDING(visvan) revisit later.
    private Hashtable ht = null;

    public WOutput() {
        ht = new Hashtable();
    }

    /** 
     * Returns a String representing the this component type.  
     *
     * @return a String object containing &quot;Output&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

    /**
     * Returns the current text value for this component.
     * If this component's model property is non-null, it will
     * return the current value contained in the object
     * referenced by the model property. If the model property
     * is null, it will return a locally stored value.
     *
     * @see #getModel
     * @param rc the render context used to render this component
     * @return String containing the current text value
     */
    public String getValue(RenderContext rc) {

        String label = null;
        if ( model == null )  {
            return (String) value;
        }
        else {
            try {
                label = (String) ModelAccessor.
                        getModelObject(rc, (String) model);
            } catch ( FacesException e ) {
                // PENDING (visvan) skip this exception ??
                return (String) value;
            }
            return label;
        }
    }

    /**
     * Sets the current text value for this component.
     * If this component's model property is non-null, it will
     * store the new value in the object referenced by the
     * model property.  If the model property is null, it
     * will store the value locally.
     * @param rc the render context used to render this component
     * @param text String containing the new text value for this component
     */
    public void setValue(RenderContext rc, String label) {
        if ( model == null ) {
            value = label;
        } else {
            try {
                ModelAccessor.setModelObject(rc, (String)model, label);
            } catch ( FacesException e ) {
                // PENDING ( visvan ) skip this exception ??
                value = label;
            }
        }
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

    /**
     * The model property for this data-bound component.
     * This property contains a reference to the object which acts
     * as the data-source for this component.  The supported types
     * for this reference:
     * <ul>
     * <li>String containing a model-reference in the scoped namespace
     *     e.g. &quot;user.lastName&quot; refers to an object named
     *          &quot;user&quot;
     *          with a property named &quot;lastName&quot;.
     * </ul>
     * @return Object describing the data-source for this component
     */
    public Object getModel() {
        return model;
    }

    /**
     * Sets the model property on this data-bound component.
     * @param model the Object which contains a reference to the
     *        object which acts as the data-source for this component
     */
    public void setModel(Object model) {
        this.model = model;
    }

}
