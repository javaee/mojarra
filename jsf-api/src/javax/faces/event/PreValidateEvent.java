/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.event;

import javax.faces.component.UIComponent;

/**
 *
 * <p class="changed_added_2_0"></p>
 *
 * @since 2.0
 */
public class PreValidateEvent extends ComponentSystemEvent {


    // ------------------------------------------------------------ Constructors


    /**

     * <p class="changed_added_2_0"></p>

     * @param component the <code>UIComponent</code> that is about to be
     * validated.

     * @throws <code>IllegalArgumentException</code> if the argument is <code>null</code>.
     */
    public PreValidateEvent(UIComponent component) {
        super(component);
    }

}
