package com.oracle.faces.cdi.test;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class CdiInjectedBean implements Serializable {

    public String getText() {
        return "CDI Injected bean";
    }
}
