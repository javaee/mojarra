package com.corejsf;

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