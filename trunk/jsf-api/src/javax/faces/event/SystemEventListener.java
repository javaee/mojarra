/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.event;


/**
 * <p class="changed_added_2_0">By implementing this class, an object
 * indicates that it is a listener for one or more kinds of {@link
 * SystemEvent}s.  The exact type of event that will cause the
 * implementing class's {@link #processEvent} method to be called is
 * indicated by the <code>facesEventClass</code> argument passed when
 * the listener is installed using {@link
 * javax.faces.application.Application#subscribeToEvent}.</p>
 *
 * @since 2.0
 */
public interface SystemEventListener extends FacesListener {


    /**
     * <p>When called, the listener can assume that any guarantees given
     * in the javadoc for the specific {@link SystemEvent}
     * subclass are true.</p>
     *
     * @param event the <code>SystemEvent</code> instance that
     * is being processed.
     *
     * @throws AbortProcessingException if lifecycle processing should
     * cease for this request.
     */
    public void processEvent(SystemEvent event) throws AbortProcessingException;

    /**
     * <p>This method must return <code>true</code> if and only if this
     * listener instance is interested in receiving events from the
     * instance referenced by the <code>source</code> parameter.</p>
     *
     * @param source the source that is inquiring about the
     * appropriateness of sending an event to this listener instance.  
     */ 
    public boolean isListenerForSource(Object source);

}
