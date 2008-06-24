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
     * <p class="changed_added_2_0">Instantiate a new
     * <code>ViewMapCreatedEvent</code> that indicates the argument
     * <code>root</code> was just associated with its view map.</p>

     * @param root the <code>UIViewRoot</code> for which a view map has
     * just been created.
     *
     * @throws <code>NullPointerException</code> if the argument is <code>null</code>.
     */
    public ViewMapCreatedEvent(UIViewRoot root) {
        super(root);
    }

}
