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
        // MY_TEST_PARAMETER set in web.xml, should be available
        return initParameterMap.get("MY_TEST_PARAMETER");
    }
    
}
