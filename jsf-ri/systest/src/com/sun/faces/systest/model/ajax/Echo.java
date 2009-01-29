package com.sun.faces.systest.model.ajax;

import javax.faces.model.ManagedBean;
import javax.faces.model.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "ajaxecho")
@SessionScoped
public class Echo {
    String str = "";

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void reset(ActionEvent ae) {
        str = "";
    }

}
