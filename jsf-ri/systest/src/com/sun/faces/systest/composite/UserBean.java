package com.sun.faces.systest.composite;

import javax.faces.event.ActionListener;
import javax.faces.model.ManagedBean;

@ManagedBean(name = "user", eager=true)

public class UserBean {

    public UserBean() {
        System.out.println("ctor called");
    }
    
  public ActionListener getLoginActionListener() {
      return new LoginActionListener();
  }  
}