package com.oracle.faces.cdi.test;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("cdiSessionScopedBean")
@SessionScoped
public class CdiSessionScopedBean implements Serializable {

    public String getText() {
        return "Session scoped!";
    }
}
