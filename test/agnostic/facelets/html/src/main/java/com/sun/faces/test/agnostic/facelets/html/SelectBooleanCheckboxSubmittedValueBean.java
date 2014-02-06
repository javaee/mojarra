/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.faces.test.agnostic.facelets.html;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name = "selectBooleanCheckboxSubmittedValueBean")
@SessionScoped
public class SelectBooleanCheckboxSubmittedValueBean implements Serializable {
    
    private Boolean box1;
    private Boolean box2;

    public Boolean getBox1() {
        return box1;
    }

    public Boolean getBox2() {
        return box2;
    }
    
    public void setBox1(Boolean box1) {
        this.box1 = box1;
    }

    public void setBox2(Boolean box2) {
        this.box2 = box2;
    }

    public void valueChange(ValueChangeEvent event) throws AbortProcessingException {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.renderResponse();
    }
}
