package com.sun.faces.test.javaee8.cdi;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.HeaderMap;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class InjectHeaderMapBean {
    
    @Inject @HeaderMap
    private Map<String, String> headerMap;

    public String getHeaderValue() {
        return (String) headerMap.get("foo");
    }
    
}
