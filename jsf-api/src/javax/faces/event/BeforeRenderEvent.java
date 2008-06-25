/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.event;

import javax.faces.component.UIComponent;

/**
 *
 * <div class="changed_added_2_0">
 *
 * <p>When an instance of this event is passed to {@link
 * SystemEventListener#processEvent} or {@link
 * ComponentSystemEventListener#processEvent}, the listener implementation
 * may assume that the <code>source</code> of this event instance is the
 * {@link UIComponent} instance that is about to be rendered just added
 * to its parent and that it is safe to call {@link
 * UIComponent#getParent}, {@link UIComponent#getClientId}, and other
 * methods that depend upon the component instance being added into the
 * view.</p>
 *
 * </div>
 *
 * @since 2.0
 */
public class BeforeRenderEvent extends ComponentSystemEvent {


    // ------------------------------------------------------------ Constructors


    /**

     * <p class="changed_added_2_0">Instantiate a new
     * <code>BeforeRenderEvent</code> that indicates the argument
     * <code>component</code> is about to be rendered.</p>

     * @param component the <code>UIComponent</code> that is about to be
     * rendered.

     * @throws <code>NullPointerException</code> if the argument is <code>null</code>.
     */
    public BeforeRenderEvent(UIComponent component) {
        super(component);
    }

}
