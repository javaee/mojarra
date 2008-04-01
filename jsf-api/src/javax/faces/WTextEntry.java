package javax.faces;

import java.util.Hashtable;

/**
 * Class for representing a user-interface component which displays
 * output to the user.  This component type is not interactive -
 * a user cannot directly manipulate this component.
 */
public class WTextEntry extends WComponent {

    private static String TYPE = "TextEntry";
    private Object value = null;

    // PENDING(visvan) revisit later.
    private Hashtable ht = null;

    public WTextEntry() {
        ht = new Hashtable();
    }

    /** 
     * Returns a String representing the this component type.  
     *
     * @return a String object containing &quot;TextEntry&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

    public Object getValue() {
	return value;
    }

    public void setValue(Object value) {
        this.value = value;
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
     * Registers the specified listener name as a value-change listener
     * for this component.  The specified listener name must be registered
     * in the scoped namespace and it must be a listener which implements
     * the <code>ValueChangeListener</code> interface, else an exception will
     * be thrown.
     * @see ValueChangeListener
     * @param listenerName the name of the value-change listener
     * @throws FacesException if listenerName is not registered in the
     *         scoped namespace or if the object referred to by listenerName
     *         does not implement the <code>ValueChangeListener</code> interface.
     */
    public void addValueChangeListener(String listenerName) throws FacesException {
    }

    /**
     * Removes the specified listener name as a value-change listener
     * for this component.  
     * @param listenerName the name of the value-change listener
     * @throws FacesException if listenerName is not registered as a
     *         value-change listener for this component.
     */
    public void removeValueChangeListener(String listenerName) throws FacesException {
    }

    /**
     * @return Iterator containing the ValueChangeListener instances registered
     *         for this component
     */
    public Iterator getValueChangeListeners() {
	return null;
    }

}
