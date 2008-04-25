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
     * RELEASE_PENDING (edburns,rogerk) document
     * @param component
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
