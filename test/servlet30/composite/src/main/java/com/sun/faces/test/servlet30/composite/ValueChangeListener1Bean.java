/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.faces.test.servlet30.composite;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name = "valueChangeListener1Bean")
public class ValueChangeListener1Bean implements Serializable {

    public void valueChange(ValueChangeEvent event)
            throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIOutput output = (UIOutput) context.getViewRoot().findComponent("form:cc_value:out");
        output.setValue("VALUECHANGE CALLED");
    }
}
