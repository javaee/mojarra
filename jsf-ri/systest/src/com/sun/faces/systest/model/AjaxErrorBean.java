package com.sun.faces.systest.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="ajaxerror")
@SessionScoped
public class AjaxErrorBean {


    private int number = 0;
    
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) throws AjaxNumberException{
        if (number > 5) {
            throw new AjaxNumberException();
        }
        this.number = number;
    }

}

class AjaxNumberException extends Exception {

}