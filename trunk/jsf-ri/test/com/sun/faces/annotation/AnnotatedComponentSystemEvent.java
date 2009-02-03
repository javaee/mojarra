/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.annotation;

import javax.faces.component.UIComponent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.NamedEvent;

/**
 *
 */
@NamedEvent
public class AnnotatedComponentSystemEvent extends ComponentSystemEvent {
    public AnnotatedComponentSystemEvent(UIComponent component) {
        super(component);
    }

}
