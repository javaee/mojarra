/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.event;

import javax.faces.component.UIComponent;

/**
 *
 * <p class="changed_added_2_0">When an instance of this event is passed
 * to {@link SystemEventListener#processEvent} or {@link
 * ComponentSystemEventListener#processEvent}, the listener
 * implementation may assume that the <code>source</code> of this event
 * instance is in a tree that has just had its state restored.</p>
 *
 * @since 2.0
 */
public class AfterRestoreStateEvent extends ComponentSystemEvent {
    
    static final long serialVersionUID = -1007196479122154347L;

    // ------------------------------------------------------------ Constructors


    /**

     * <p class="changed_added_2_0">Instantiate a new
     * <code>AfterRestoreStateEvent</code> that indicates the argument
     * <code>component</code> just had its state restored.</p>

     * @param component the <code>UIComponent</code> whose state was just restored.

     * @throws <code>IllegalArgumentException</code> if the argument is <code>null</code>.
     */
    public AfterRestoreStateEvent(UIComponent component) {
        super(component);
    }
    
    public void setComponent(UIComponent newComponent) {
        this.source = newComponent;
    }


}
