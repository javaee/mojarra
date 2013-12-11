package com.sun.faces.test.webprofile.facelets.core;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class ValidateRequiredBean implements Serializable {

    private static final long serialVersionUID = -8114872795707646008L;
    private String text1;
    private String text2;

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }
}
