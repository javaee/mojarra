package com.sun.faces.test.agnostic.composite;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;

@ManagedBean
@SessionScoped
public class Issue1943Bean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String groupToAdd = "select one";

    public void valueChange(ValueChangeEvent vce) throws AbortProcessingException {
        System.err.println("VALUECHANGE CALLED!!!");
        FacesMessage fMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "valueChange Called...", null);
        FacesContext.getCurrentInstance().addMessage(null, fMsg);

    }

    public String getGroupToAdd() {
        return groupToAdd;
    }

    public void setGroupToAdd(String groupToAdd) {
        this.groupToAdd = groupToAdd;
    }

    public String removeGroup() {
        System.err.println("REMOVEGROUP CALLED!!!");
        FacesMessage fMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "removeGroup Called...", null);
        FacesContext.getCurrentInstance().addMessage(null, fMsg);
        return null;
    }
}
