/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.event;

import javax.faces.component.UIViewRoot;

/**
 *
 * <p class="changed_added_2_0">This event must be published by a call
 * to {@link javax.faces.application.Application#publishEvent} when the
 * <code>clear</code> method is called on the map returned from {@link
 * UIViewRoot#getViewMap}.
 *
 * RELEASE_PENDING (edburns) The javadocs for FacesContext.setViewRoot()
 *  already cover this.  I don't think we need to duplicate it here.
 *  Additionally, we need to make sure the navigation section of the
 *  spec document clearly states that the view map is cleared when
 *  navigation to a view ID different from the current view ID regardless
 *  of the navigation being a forward or a redirect.
 * 
 * This must happen when a call is made to
 * {@link javax.faces.context.FacesContext#setViewRoot} and the argument
 * UIViewRoot is different from the current, non-<code>null</code>
 * UIViewRoot.  See {@link javax.faces.context.FacesContext#setViewRoot}
 * for the normative specification of this behavior.</p>
 *
 * @since 2.0
 */
public class PreDestroyViewMapEvent extends ComponentSystemEvent {

    private static final long serialVersionUID = 4470489935758914483L;


    // ------------------------------------------------------------ Constructors


    /**
     * <p class="changed_added_2_0">Instantiate a new
     * <code>ViewMapDestroydEvent</code> that indicates the argument
     * <code>root</code> just had its associated view map destroyed.</p>

     * @param root the <code>UIViewRoot</code> for which the view map has
     * just been destroyed.
     *
     * @throws <code>IllegalArgumentException</code> if the argument is <code>null</code>.
     */
    public PreDestroyViewMapEvent(UIViewRoot root) {
        super(root);
    }

}
