package com.sun.faces.test.servlet30.facelets.ui;

import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;

/**
 * Created by xinyuan.zhang on 9/18/17.
 */

@ManagedBean
@RequestScoped
public class OneBean {

    public OneBean(String val) {
        this.val = val;
    }

    public OneBean() {
    }

    private String val;


    public String getVal() {
        this.val="OneBean";
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
