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
 *
 * </div>
 *
 * @since 2.0
 */
public class ViewMapCreatedEvent extends ComponentSystemEvent {

    private static final long serialVersionUID = 8684338297976265379L;


    // ------------------------------------------------------------ Constructors


    /**
     * RELEASE_PENDING (edburns,rogerk) document
     * @param component
     */
    public ViewMapCreatedEvent(UIViewRoot component) {
        super(component);
    }

}
