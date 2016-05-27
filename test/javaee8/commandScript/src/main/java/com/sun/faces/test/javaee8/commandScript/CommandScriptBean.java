package com.sun.faces.test.javaee8.commandScript;

import javax.enterprise.inject.Model;

@Model
public class CommandScriptBean {

    private String result;
    
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    
}