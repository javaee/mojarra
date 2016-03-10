package com.sun.faces.test.javaee8.websocket;

import javax.enterprise.inject.Model;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;

@Model
public class PushBean {

    @Inject @Push
    private PushContext push;
    
    @Inject @Push
    private PushContext user;
    
    @Inject @Push
    private PushContext view;
    
    public void send(){
        push.send("pushed!");
        user.send("pushed!", "user");
        view.send("pushed!");
    }
    
}