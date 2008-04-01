package javax.faces;

import javax.servlet.ServletRequest;

/**
 * The class which encapsulates information associated
 * with a form event.  Form events are generated in response
 * to specific phases in a WForm component's lifecycle:
 * <ul>
 * <li>commandName: a String which describes the application
 *                  command to be executed as a result of this event
 *                  e.g. &quot;login&quot;, &quot;place-order&quot;, etc.
 *                  This will contain the value of the commandName
 *                  property of the WCommand component where the command
 *                  event originated.
 * </ul>
 * @see FormListener
 */
public class FormEvent extends FacesEvent {

    /**
     * The initialization event type.  This event occurs when
     * a WForm component is initialized in its defined scope.
     */
    public static final int INIT = 0;

    /**
     * The control-added event type. This event occurs when
     * a form control component is added to a WForm's hierarchy.
     */
    public static final int CONTROL_ADDED = 1;

    /**
     * The control-removed event type. This event occurs when
     * a form control component is removed from a WForm's hierarchy.
     */
    public static final int CONTROL_REMOVED = 2;

    /**
     * The exit event type. This event occurs when
     * a WForm has been exited by the user, usually because
     * either a submit or cancel command has executed.
     */
    public static final int EXIT = 3;

    private int type;
    private String formControlName;

    /**
     * Creates a form event
     * @param request the ServletRequest object where this event was generated
     * @param sourceName a String containing the name of the form component
     *        where this event originated
     * @param type an integer value indicating the type of form event which
     *        occurred
     * @param formControlName a String containing the name of the child
     *        component which was added or removed, or null if this is
     *        an INIT or EXIT event type
     * @throws NullPointerException if sourceName is null
     * @throws IllegalParameterException if type isn't either INIT, 
     *         CONTROL_ADDED, CONTROL_REMOVED, or EXIT
     */
    public FormEvent(ServletRequest request, String sourceName, int type,
			String formControlName) {
	super(request, sourceName);
	this.type = type;
	this.formControlName = formControlName;
    }

    /**
     * Returns the type of form event, one of:
     * <ul>
     * <li>INIT
     * <li>CONTROL_ADDED
     * <li>CONTROL_REMOVED
     * <li>EXIT
     * </ul>
     * @return an integer value indicating the type of form event
     */
    public int getType() {
	return type;
    }

    /**
     * Returns the form control which was either added in a
     * CONTROL_ADDED event or removed in a CONTROL_REMOVED event.
     * @return WComponent object corresponding to the form control
     *         which was added or removed, or null if the event type
     *         is INIT or EXIT
     */
    public WComponent getFormControl() {
        // return ObjectTable.get(getRequest(), formControlName);
	return null;
    }

}
