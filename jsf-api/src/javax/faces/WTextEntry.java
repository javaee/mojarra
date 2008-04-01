package javax.faces;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * Class for representing a user-interface component accepts
 * text input from a user.  
 * <p>
 * This is a data-bound component, which means that it's value
 * is derived from an application &quot;model&quot; object which
 * lives outside the component, in the scoped namespace.  The
 * component stores a reference to this model object.  Currently
 * the supported reference types for the model are:
 * <ul>
 * <li>String which describes a <bean-name>.<property-name> 
 *     reference in the scoped namespace.
 *     e.g. &quot;user.lastName&quot; would correspond to a bean
 *          named &quot;user&quot; with a property named &quot;lastName&quot;
 * </ul>
 * In order to make it convenient to set/get the text value
 * without interacting directly with this reference, the class
 * provides <code>getText()</code> and <code>setText</code> methods
 * which interact with the model object under the covers.
 *
 */
public class WTextEntry extends WComponent {

    private static String TYPE = "TextEntry";
    private Object model = null;

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
    public String getText(RenderContext rc) {
	return (String) model;
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
    public void setText(RenderContext rc, String text) {
	model = text;
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
