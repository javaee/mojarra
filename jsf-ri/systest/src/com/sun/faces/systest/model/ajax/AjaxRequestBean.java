package com.sun.faces.systest.model.ajax;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name="ajaxrequest")
@SessionScoped
public class AjaxRequestBean {
    private Integer count = 0;

    private String echo = "echo";
    private String echo1 = "";
    private String echo2 = "";
    private String echo3 = "";
    private String echo4 = "";

    public String getEcho1() {
        return echo1;
    }

    public void setEcho1(String echo1) {
        this.echo1 = echo1;
    }

    public String getEcho2() {
        return echo2;
    }

    public void setEcho2(String echo2) {
        this.echo2 = echo2;
    }

    public String getEcho3() {
        return echo3;
    }

    public void setEcho3(String echo3) {
        this.echo3 = echo3;
    }

    public String getEcho4() {
        return echo4;
    }

    public void setEcho4(String echo4) {
        this.echo4 = echo4;
    }

    public String getEcho() {
        return echo;
    }

    public void setEcho(String echo) {
        this.echo = echo;
    }

    public void echoValue(ValueChangeEvent event) {
        String str = (String)event.getNewValue();
        echo = str;
    }

    public void resetEcho(ActionEvent ae) {
        echo = "reset";
        echo1 = "reset";
        echo2 = "reset";
        echo3 = "reset";
        echo4 = "reset";
    }

    public Integer getCount() {
        return count++;
    }

    public void resetCount(ActionEvent ae) {
        count = 0;
    }

}
