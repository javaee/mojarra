package com.sun.faces.systest.model;

import javax.faces.model.ManagedBean;
import javax.faces.model.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name="ajaxrequest")
@SessionScoped
public class AjaxRequestBean {
    private Integer count = 0;

    private String echo = "echo";
    private String echo1 = "initial";
    private String echo2 = "initial";
    private String echo3 = "initial";
    private String echo4 = "initial";

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

    public void echo1(ActionEvent ae) {
        echo = echo1;
    }
    public void echo2(ActionEvent ae) {
        echo = echo2;
    }
    public void echo3(ActionEvent ae) {
        echo = echo3;
    }
    public void echo4(ActionEvent ae) {
        echo = echo4;
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
