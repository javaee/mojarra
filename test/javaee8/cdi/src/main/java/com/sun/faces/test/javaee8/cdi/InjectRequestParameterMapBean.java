package com.sun.faces.test.javaee8.cdi;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.RequestParameterMap;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class InjectRequestParameterMapBean {
    
    @Inject @RequestParameterMap
    private Map<String, String> requestParameterMap;

    public String getRequestParameterValue() {
        return requestParameterMap.get("foo");
    }
    
}
