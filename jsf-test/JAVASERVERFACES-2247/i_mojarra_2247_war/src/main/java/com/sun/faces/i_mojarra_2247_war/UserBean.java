package com.sun.faces.i_mojarra_2247_war;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class UserBean implements Serializable {
    
    private String value; // +getter+setter

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public String submit() {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true&amp;includeViewParams=true";
    }    
    
}

