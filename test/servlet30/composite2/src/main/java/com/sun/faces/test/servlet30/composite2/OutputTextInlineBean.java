/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.faces.test.servlet30.composite2;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "outputTextInlineBean")
@RequestScoped
public class OutputTextInlineBean {

    public String getData() {
        return "myData";
    }
}
