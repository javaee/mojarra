package com.sun.faces.test.javaee8.cdi;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.RequestParameterValuesMap;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class InjectRequestParameterValuesMapBean {
    
    @Inject @RequestParameterValuesMap
    private Map<String, String[]> requestParameterValuesMap;

    public String getRequestParameterValue0() {
        return requestParameterValuesMap.get("foo")[0];
    }
    
    public String getRequestParameterValue1() {
        return requestParameterValuesMap.get("foo")[1];
    }
    
}
