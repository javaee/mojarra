package com.sun.faces.systest.model;

import javax.faces.model.ManagedBean;
import javax.faces.model.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name="ajaxrequest")
@SessionScoped
public class AjaxRequestBean {
    private Integer count = 0;

    private String echo = "intial";
    private String echo2 = "echo";

    public String getEcho() {
        return echo;
    }

    public void setEcho(String echo) {
        this.echo = echo;
    }

    public void echoText(ActionEvent ae) {
        echo2 = echo;
    }

    public Integer getCount() {
        return count++;
    }

    public void resetCount(ActionEvent ae) {
        count = 0;
    }

    public String getEcho2() {
        return echo2;
    }

    public void setEcho2(String echo2) {
        this.echo2 = echo2;
    }
}
