package com.sun.faces.test.javaee8.cdi;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.RequestMap;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class InjectRequestMapBean {
    
    @Inject @RequestMap
    private Map<String, Object> requestMap;

    public String getRequestValue() {
        return (String) requestMap.get("fooAttribute");
    }
    
}
