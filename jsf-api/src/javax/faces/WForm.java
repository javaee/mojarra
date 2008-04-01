package javax.faces;

import java.util.Hashtable;
import java.util.Iterator;
import javax.servlet.ServletRequest;

/**
 * Class for representing a form user-interface component. 
 * A form encapsulates the process of taking a collection of
 * input from the user and submitting it to the application
 * in a single submit operation.  
 */
public class WForm extends WComponent {

    private static String TYPE = "Form";
    private Object model = null;

    /** 
     * Returns a String representing the form's type.  
     *
     * @return a String object containing &quot;Form&quot;
     *         
     */
    public String getType() {
	return TYPE;
    }

    /**
     * Registers the specified listener name as a form listener
     * for this component.  The specified listener name must be registered
     * in the scoped namespace and it must be a listener which implements
     * the <code>FormListener</code> interface, else an exception will
     * be thrown.
     * @see FormListener
     * @param listenerName the name of the form listener
     * @throws FacesException if listenerName is not registered in the
     *         scoped namespace or if the object referred to by listenerName
     *         does not implement the <code>FormListener</code> interface.
     */
    public void addFormListener(ServletRequest req, String listenerName) 
            throws FacesException {
    }

    /**
     * Removes the specified listener name as a form listener
     * for this component.  
     * @param listenerName the name of the form listener
     * @throws FacesException if listenerName is not registered as a
     *         form listener for this component.
     */
    public void removeFormListener(String listenerName) throws FacesException {
    }

    /**
     * @return Iterator containing the FormListener instances registered
     *         for this component
     */
    public Iterator getFormListeners() {
	return null;
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
