package com.sun.faces.test.javaee8.flash;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

@Model
public class Issue4167 {

    public String keepAndRedirect() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("message", "issue4167");
        return "issue4167?faces-redirect=true";
    }
    
    public String keepAndGet() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.keep("message");
        return (String) flash.get("message");
    }
    
}