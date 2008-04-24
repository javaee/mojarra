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


 * @author edburns
 */
public class AfterAddToParentEvent extends ComponentSystemEvent {

    public AfterAddToParentEvent(UIComponent component) {
        super(component);
    }
    
    public boolean isAppropriateListener(FacesListener listener) {
        Map<String,Object> attributes = getComponent().getAttributes();
        boolean result = false;
        if (!attributes.containsKey("added")) {
            result = (listener instanceof SystemEventListener);
            attributes.put("added", Boolean.TRUE);
        }
        return result;
    }
    

}
