package com.sun.faces.test.servlet30.flashChunckRedirect;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;

@RequestScoped
@ManagedBean(name = "issue2332Bean")
public class Issue2332Bean {

    private Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();

    public void test(ActionEvent ae) {
        flash.put("flashContent", "== Flash value LINK1 ==");
    }

    public String test2() {
        flash.put("flashContent", "== Flash value LINK2 ==");
        return "page2.xhtml";
    }
}
