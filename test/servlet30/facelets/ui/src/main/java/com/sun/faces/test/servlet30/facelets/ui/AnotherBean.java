package com.sun.faces.test.servlet30.facelets.ui;

import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;

/**
 * Created by xinyuan.zhang on 9/18/17.
 */

@ManagedBean
@RequestScoped
public class AnotherBean {


    public String name;


    public AnotherBean(String name) {
        this.name = name;
    }

    public AnotherBean() {
    }

    public String getName() {
        this.name="anotherBean";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
