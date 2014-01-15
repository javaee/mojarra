package com.sun.faces.test.agnostic.facelets.core;

import javax.validation.constraints.Size;

public class ValidateBeanDisabledFoo {

    @Size(min = 1)
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
