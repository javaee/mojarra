package com.sun.faces.facelets.tag.jsf;

import javax.faces.event.ComponentSystemEvent;
import javax.faces.component.UIComponent;

/**
 * Event fired by the CompositeComponentTagHandler to notify interested
 * parties that it's template and implementation children have completed
 * processing.
 */
public class CompositeChildrenProcessedEvent extends ComponentSystemEvent {


    public CompositeChildrenProcessedEvent(UIComponent component) {
        super(component);
    }
    
}
