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
     * RELEASE_PENDING (edburns,roger) document
     * @param source
     */
    public SystemEvent(Object source) {
        super(source);
    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return <code>true</code> if this {@link FacesListener} is an instance
     * of a listener class that this event supports.  Typically, this will be
     * accomplished by an "instanceof" check on the listener class.</p>
     *
     * RELEASE_PENDING (edburns,rogerk) I'm not sure it's important to include
     *  the instanceof information.  Let the implementation deal with it.
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
