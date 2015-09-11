package com.sun.faces.test.javaee8.cdi;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.HeaderValuesMap;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class InjectHeaderValuesMapBean {
    
    @Inject @HeaderValuesMap
    private Map<String, String[]> headerValuesMap;

    public String getHeaderValue() {
        return (String) headerValuesMap.get("foo")[0];
    }
    
}
