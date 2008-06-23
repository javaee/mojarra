/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javax.faces.event;

import javax.faces.component.UIComponent;

/**
 *
 * <p><strong class="changed_added_2_0">ComponentSystemEvent</strong> is
 * the base class for {@link SystemEvent}s that are specific to a {@link
 * UIComponent} instance.</p>
 *
 * @since 2.0
 */
public abstract class ComponentSystemEvent extends SystemEvent {

    private static final long serialVersionUID = -4726746661822507506L;

    
    // ------------------------------------------------------------ Constructors


    /**
     * <p class="changed_added_2_0">Pass the argument
     * <code>component</code> to the superclass constructor.</p>

     * @param component the <code>UIComponent</code> reference to be
     * passed to the superclass constructor.
     *
     * @throws <code>NullPointerException</code> if the argument is <code>null</code>.
     */
    public ComponentSystemEvent(UIComponent component) {
        super(component);
    }


    // -------------------------------------------------------------- Properties


    /**
     * @return the source {@link UIComponent} that sent this event.
     */
    public UIComponent getComponent() {

        return ((UIComponent) getSource());

    }    

}
