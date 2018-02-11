package com.sun.faces.test.javaee8.websocket;

import javax.enterprise.context.RequestScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class Issue4332 {

    @Inject @Push
    private PushContext push;

    public void send(){
        push.send("pushed!");
    }

}