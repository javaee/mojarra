package com.sun.faces.test.javaee8.websocket;

import java.io.Serializable;

import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class Spec1396 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject @Push
    private PushContext push;

    @Inject @Push
    private PushContext user;

    @Inject @Push
    private PushContext view;

    private String ajaxOutput;

    public void send(){
        push.send("pushed!");

        user.send("pushed!", "user");

        ajaxOutput = "pushed!";
        view.send("renderAjaxOutput");
    }

    public String getAjaxOutput() {
        return ajaxOutput;
    }

}