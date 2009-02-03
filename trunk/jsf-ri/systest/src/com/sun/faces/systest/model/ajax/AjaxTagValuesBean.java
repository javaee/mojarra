package com.sun.faces.systest.model.ajax;

import javax.faces.model.ManagedBean;
import javax.faces.model.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Arrays;

@ManagedBean(name="ajaxtag")
@SessionScoped
public class AjaxTagValuesBean {
    private Integer count = 0;
    private Boolean checked = false;
    private String text = "";
    private String[] outArray = { "out1", ":form2:out2", ":out3" };
    private Collection<String> outSet = new LinkedHashSet<String>(Arrays.asList(outArray));
    private String render = "out1";
    
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

    public Collection<String> getRenderList() {
        return outSet;
    }

    public String getRenderOne() {
        return render;
    }

}