/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.systest.model;

import javax.faces.component.UIOutput;
import javax.faces.event.ComponentSystemEvent;

/**
 *
 */
public class EventTagBean {

    public void beforeEncode(ComponentSystemEvent event) {
        UIOutput output = (UIOutput)event.getComponent();
        output.setValue("The '" + event.getClass().getName() + "' event fired!");
    }
}
