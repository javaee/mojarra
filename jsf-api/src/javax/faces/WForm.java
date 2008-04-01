package javax.faces;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * Class for representing a form user-interface component. 
 * A form encapsulates the process of taking a collection of
 * input from the user and submitting it to the application
 * in a single submit operation.  
 */
public class WForm extends WComponent {

    private static String TYPE = "Form";

    // PENDING(visvan) revisit later
    private Hashtable ht = null;

    public WForm() {
        ht = new Hashtable();
    }

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
    public void addFormListener(String listenerName) throws FacesException {
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


}
