/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.event;

import javax.faces.component.UIViewRoot;

/**
 *
 * <div class="changed_added_2_0">
 *
 * <p>This event must be published by a call to
 * {javax.faces.application.Application#publishEvent} when the
 * <code>clear</code> method is called on the map returned from {@link
 * UIViewRoot#getViewMap}.  This must happen when a call is made to
 * {@link javax.faces.context.FacesContext#setViewRoot} and the argument
 * UIViewRoot is different from the current, non-<code>null</code>
 * UIViewRoot.  See {@link javax.faces.context.FacesContext#setViewRoot}
 * for the normative specification of this behavior.</p>

 * </div>
 */
public class ViewMapDestroyedEvent extends ComponentSystemEvent {

    public ViewMapDestroyedEvent(UIViewRoot component) {
        super(component);
    }

}
