/*
 * $Id: FormEvent.java,v 1.6 2002/03/07 23:44:04 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import javax.servlet.ServletRequest;

/**
 * The class which encapsulates information associated
 * with a form event.  Form events are generated in response
 * to specific phases in a UIForm component's lifecycle.
 * @see FormListener
 */
public class FormEvent extends FacesEvent {

    /**
     * The initialization event type.  This event occurs when
     * a UIForm component is initialized in its defined scope.
     */
    public static final int INIT = 0;

    /**
     * The exit event type. This event occurs when
     * a UIForm component has been exited by the user, usually because
     * either a submit or cancel command has executed.
     */
    public static final int EXIT = 1;

    private int type;

    /**
     * Creates a form event.
     * @param ec EventContext object representing the event-processing 
     *           phase of the request where this event originated
     * @param sourceComponent the component where this event originated
     * @param type an integer value indicating the type of form event which
     *        occurred
     * @throws NullPointerException if sourceComponent is null
     * @throws IllegalParameterException if type isn't either INIT or EXIT
     */
    public FormEvent(EventContext ec, UIComponent sourceComponent, int type) {
	super(ec, sourceComponent);
	this.type = type;
    }

    /**
     * Returns the type of form event, one of:
     * <ul>
     * <li><code>FormEvent.INIT</code>
     * <li><code>FormEvent.EXIT</code>
     * </ul>
     * @return an integer value indicating the type of form event
     */
    public int getType() {
	return type;
    }
}
