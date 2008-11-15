package com.sun.faces.systest.model;

import javax.faces.model.ManagedBean;
import javax.faces.model.SessionScoped;
import javax.faces.event.ActionEvent;


@ManagedBean(name="ajaxrequest")
@SessionScoped
public class AjaxRequestBean {
    private Integer count = 0;

    public Integer getCount() {
        return count++;
    }

    public void resetCount(ActionEvent ae) {
        count = 0;
    }

}
