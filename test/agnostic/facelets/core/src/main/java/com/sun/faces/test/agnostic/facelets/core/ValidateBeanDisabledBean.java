package com.sun.faces.test.agnostic.facelets.core;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "validateBeanDisabledBean")
@RequestScoped
public class ValidateBeanDisabledBean {

    private ValidateBeanDisabledFoo foo = new ValidateBeanDisabledFoo();

    public ValidateBeanDisabledFoo getFoo() {
        return this.foo;
    }

    public void submit() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCCESS! Name set to '" + this.foo.getName() + "'"));
    }
}
