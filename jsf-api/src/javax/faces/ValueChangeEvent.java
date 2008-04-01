package javax.faces;

/**
 * The class which encapsulates information associated
 * with a value-change event.  Value-change events are typically
 * generated when a user changes the value associated with
 * a user-interface component.
 * <p>
 * A value-change event has two properties:
 * <ul>
 * <li>modelReference: a String which identifies the object (and associated
 *                     property) in the scoped namespace which stores
 *                     the component's value for the application
 *                     e.g. &quot;user.firstName&quot; or 
 *                          &quot;user.address.street&quot;
 * <li>newValue: an Object representing the new value for the component 
 * </ul>
 * @see ValueChangeListener
 */
public class ValueChangeEvent extends FacesEvent {

    private String modelReference;
    private Object newValue;

    /**
     * Creates a value-change event.
     * @param request the ServletRequest object where this event was derived
     * @param sourceName a String containing the name of the component 
     *        where this event originated
     * @param commandName a String containing the name of the command
     *        associated with this event
     * @throws NullPointerException if sourceName or commandName is null
     */
    public ValueChangeEvent(ServletRequest request, String sourceName, 
			String modelReference, Object newValue) {
	super(request, sourceName);
	this.modelReference = modelReference;
	this.newValue = newValue;
    }

    /**
     * @return String containing the model-reference which identifies
     *         the object in the scoped namespace which holds the
     *         component's value
     */
    public String getModelReference() {
	return modelReference;
    }

    /**
     * @return Object containing the new value that was set for
     *         the associated source component
     */
    public Object getNewValue() {
	return newValue;
    }
}
