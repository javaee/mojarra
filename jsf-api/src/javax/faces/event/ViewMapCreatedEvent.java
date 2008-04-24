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
 * {javax.faces.application.Application#publishEvent} when the view map
 * is first created.  This must happen on the first time a call is made
 * to {@link UIViewRoot#getViewMap} on a <code>UIViewRoot</code>
 * instance.  The source for this event is the
 * <code>UIViewRoot<code>.</p>

 * </div>
 */
public class ViewMapCreatedEvent extends ComponentSystemEvent {

    public ViewMapCreatedEvent(UIViewRoot component) {
        super(component);
    }

}
