/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.event;

import javax.faces.component.UIViewRoot;

/**
 *
 * <p class="changed_added_2_0">When an instance of this event is passed
 * to {@link SystemEventListener#processEvent} or {@link
 * ComponentSystemEventListener#processEvent}, the listener
 * implementation may assume that the <code>source</code> of this event
 * instance is the {@link UIViewRoot} instance that is about to be
 * rendered.</p>
 *
 * @since 2.0
 */
public class PreRenderViewEvent extends ComponentSystemEvent {


    // ------------------------------------------------------------ Constructors


    /**

     * <p class="changed_added_2_0">Instantiate a new
     * <code>PreRenderComponentEvent</code> that indicates the argument
     * <code>root</code> is about to be rendered.</p>

     * @param root the <code>UIViewRoot</code> that is about to be
     * rendered.

     * @throws <code>IllegalArgumentException</code> if the argument is <code>null</code>.
     */
    public PreRenderViewEvent(UIViewRoot root) {
        super(root);
    }

}
