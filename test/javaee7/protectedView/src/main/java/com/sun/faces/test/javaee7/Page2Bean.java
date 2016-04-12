package com.sun.faces.test.javaee7;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class Page2Bean implements Serializable {
    
    private String text2 = "Welcome to Page2";
    
    public Page2Bean() {}

    public String getText2() {
        return text2;
    }

    public void setText2 (String text2) {
        this.text2 = text2;
    }
}

