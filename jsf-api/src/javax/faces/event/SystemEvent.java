/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.event;

import java.util.EventObject;

/**
 * <p><strong class="changed_added_2_0">SystemEvent</strong> is the base
 * class for non-application specific events that can be fired by
 * arbitrary objects.</p>
 *
 * @since 2.0
 */
public abstract class SystemEvent extends EventObject {

    private static final long serialVersionUID = 2696415667461888462L;


    // ------------------------------------------------------------ Constructors


    /**
     * <p class="changed_added_2_0">Pass the argument
     * <code>source</code> to the superclass constructor.</p>

     * @param source the <code>source</code> reference to be
     * passed to the superclass constructor.
     *
     * @throws <code>NullPointerException</code> if the argument is
     * <code>null</code>.
     */
    public SystemEvent(Object source) {
        super(source);
    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return <code>true</code> if this {@link FacesListener} is an
     * instance of a the appropriate listener class that this event
     * supports.</p>
     *
     * @param listener {@link FacesListener} to evaluate
     */
    public boolean isAppropriateListener(FacesListener listener) {

        return (listener instanceof SystemEventListener);

    }


    /**
     * <p>Broadcast this event instance to the specified
     * {@link FacesListener}, by whatever mechanism is appropriate.  Typically,
     * this will be accomplished by calling an event processing method, and
     * passing this instance as a paramter.</p>
     *
     * @param listener {@link FacesListener} to send this {@link FacesEvent} to
     *
     * @throws AbortProcessingException Signal the JavaServer Faces
     *  implementation that no further processing on the current event
     *  should be performed
     */
    public void processListener(FacesListener listener) {

        ((SystemEventListener) listener).processEvent(this);

    }
}
