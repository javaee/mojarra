package com.sun.faces.test.agnostic.ajax;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "legendWithoutIdBean")
@SessionScoped
public class LegendWithoutIdBean {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
