package com.sun.faces.test.agnostic.facelets.ui;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class Repeat3ClickBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<String> list = Arrays.asList(new String[]{"One", "Two", "Three"});
    private boolean hide = true;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public void toggle() {
        hide = !hide;
    }
}
