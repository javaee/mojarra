/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.systest.model;

import javax.faces.event.SystemEvent;

/**
 *
 * @author edburns
 */
public class TestBeanEventClass extends SystemEvent {

    public TestBeanEventClass(TestBean source) {
        super(source);
    }

}
