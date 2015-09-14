package com.sun.faces.test.javaee8.cdi;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.InitParameterMap;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class InjectInitParameterMapBean {
    
    @Inject @InitParameterMap
    private Map<String, String> initParameterMap;

    public String getInitParameterValue() {
        // javax.faces.ENABLE_CDI_RESOLVER_CHAIN set in web.xml, should be true
        // otherwise injection will not resolve
        return initParameterMap.get("javax.faces.ENABLE_CDI_RESOLVER_CHAIN");
    }
    
}
