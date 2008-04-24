/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.event;


/**
 *  <p class="changed_added_2_0">Implementors of this class do not need
 *  an <code>isListenerForSource()</code> method because they are only
 *  installed on specific component instances, therefore the
 *  <code>isListenerForSource()</code> merthod is implicit.  Also, the 
 * {@link #processEvent} method on this interface takes a 
 * {@link ComponentSystemEvent} because the event will always be associated with
 * a {@link javax.faces.component.UIComponent} instance.</p>
 *
 */
public interface ComponentSystemEventListener extends FacesListener {

    /**
     * <p>When called, the listener can assume that any guarantees given
     * in the javadoc for the specific {@link SystemEvent}
     * subclass are true.</p>
     *
     * @param event the <code>ComponentSystemEvent</code> instance that
     * is being processed.
     *
     * @throws AbortProcessingException if lifecycle processing should
     * cease for this request.
     */

    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException;
}
