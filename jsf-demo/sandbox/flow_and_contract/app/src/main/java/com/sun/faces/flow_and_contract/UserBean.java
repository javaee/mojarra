package com.sun.faces.flow_and_contract;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class UserBean implements Serializable {
    
    private boolean navToggle = true;

    public boolean isNavToggle() {
        return navToggle;
    }

    public void setNavToggle(boolean navToggle) {
        this.navToggle = navToggle;
    }
    
    public String getContract() {
        String result = "leftNav";
        if (navToggle) {
            result = "topNav";
        }
        return result;
    }
    
    
}

