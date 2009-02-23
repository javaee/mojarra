package com.corejsf;

import javax.faces.event.ActionEvent;
import javax.faces.model.ManagedBean;

@ManagedBean(name = "user", eager=true)

public class UserBean {

    public UserBean() {
        System.out.println("ctor called");
    }
    
  public void loginActionListener(ActionEvent e) {
    System.out.println("logging in...");
  }  
}