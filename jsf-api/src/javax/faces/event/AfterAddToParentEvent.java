/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.event;

import java.util.Map;
import javax.faces.component.UIComponent;

/**
 *
 * <div class="changed_added_2_0">
 *
 * <p>When an instance of this event is passed to {@link
 * SystemEventListener#processEvent} or {@link
 * ComponentSystemEventListener#processEvent}, the listener implementation
 * may assume that the <code>source</code> of this event instance is the
 * {@link UIComponent} instance that was just added to its parent and
 * that it is safe to call {@link UIComponent#getParent}, {@link
 * UIComponent#getClientId}, and other methods that depend upon the
 * component instance being added into the view.</p>
 *
 * <p>The implementation must guarantee that {@link
 * javax.faces.application.Application#publishEvent} is called,
 * immediately after any <code>UIComponent</code> instance is added to
 * the view hierarchy <strong>except</strong> in the case where {@link
 * javax.faces.render.ResponseStateManager#isPostback} returns
 * <code>true</code> <strong>at the same time as</strong> {@link
 * javax.faces.context.FacesContext#getCurrentPhaseId} returns {@link
 * javax.faces.event.PhaseId#RESTORE_VIEW}.  When both of those
 * conditions are met, {@link
 * javax.faces.application.Application#publishEvent} must not be called.</p>
 * 
 * </div>
 *
 * @since 2.0
 */
public class AfterAddToParentEvent extends ComponentSystemEvent {

    private static final long serialVersionUID = -5706460518363094948L;

    /**
     * Namespaced key to mark this component having been added
     * to its parent.
     */
    private static final String ADDED_ATTRIBUTE_KEY = "javax.faces.private.ADDED";


    // ------------------------------------------------------------ Constructors


    /**
     * <p class="changed_added_2_0">Instantiate a new
     * <code>AfterAddToParentEvent</code> that indicates the argument
     * <code>component</code> was just added to the view.</p>

     * @param component the <code>UIComponent</code> that has just been
     * added to the view.
     *
     * @throws <code>NullPointerException</code> if the argument is <code>null</code>.
     */
    public AfterAddToParentEvent(UIComponent component) {
        super(component);
    }


    // --------------------------------------- Methods from ComponentSystemEvent

    /**
     * <p class="changed_added_2_0">Returns <code>true</code> if and
     * only if the argument <code>listener</code> is an instance of
     * {@link SystemEventListener}.</p>
     * @param listener
     */
    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        Map<String,Object> attributes = getComponent().getAttributes();
        boolean result = false;
        if (!attributes.containsKey(ADDED_ATTRIBUTE_KEY)) {
            result = (listener instanceof SystemEventListener);
            attributes.put(ADDED_ATTRIBUTE_KEY, Boolean.TRUE);
        }
        return result;
    }
    

}
