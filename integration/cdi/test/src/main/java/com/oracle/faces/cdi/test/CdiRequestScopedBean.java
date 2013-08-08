package com.oracle.faces.cdi.test;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("cdiRequestScopedBean")
@RequestScoped
public class CdiRequestScopedBean implements Serializable {

    public String getText() {
        return "Request scoped!";
    }
}
