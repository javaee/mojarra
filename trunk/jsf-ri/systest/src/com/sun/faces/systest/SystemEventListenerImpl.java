/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.systest;

import com.sun.faces.systest.model.TestBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

/**
 *
 * @author edburns
 */
public class SystemEventListenerImpl implements SystemEventListener {

    public boolean isListenerForSource(Object component) {
        return component instanceof TestBean;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
	FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("eventFromBean",
									 "eventFromBean: source: " + 
                                                                         event.getSource().getClass().getName());
    }

}
