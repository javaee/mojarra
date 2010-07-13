package com.sun.faces.systest;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;

@ManagedBean(name = "hello", eager = true)
@ApplicationScoped
public class HelloBean {
    String fname;
    public HelloBean() {
        System.out.println("HelloBean instantiated");
    }
    public String getMessage() {
        return "Hello " + fname + ", Good Morning!";
    }
    public String getFname() {
        return fname;
    }
    public  void setFname(String name) {
        this.fname = name;
    }
}