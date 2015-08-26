package com.sun.faces.test.servlet30.composite2;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;

@ManagedBean
@RequestScoped
public class UserBean implements Serializable {
    
    public UserBean() {}

    private UIComponent nonComposite;

    public UIComponent getNonComposite() {
        return nonComposite;
    }

    public void setNonComposite(UIComponent boundComponent) {
        this.nonComposite = boundComponent;
    }
    
    
    private UIComponent composite;

    public UIComponent getComposite() {
        return composite;
    }

    public void setComposite(UIComponent composite) {
        this.composite = composite;
    }
    
}

