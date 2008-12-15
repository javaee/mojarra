package com.sun.faces.systest.model.ajax;

import javax.faces.model.ManagedBean;
import javax.faces.model.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name="ajaxtag")
@SessionScoped
public class AjaxTagValuesBean {
    private Integer count = 0;
    private Boolean checked = false;
    private String text = "";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Integer getCount() {
        return count++;
    }

    public void reset(ActionEvent ae) {
        count = 0;
        checked = false;
        text = "";
    }

}