package com.sun.faces.test.javaee8.cdi;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.FlowMap;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class InjectFlowMapBeanX {
    
    @Inject @FlowMap
    private Map<Object, Object> flowMap;

    public void initFoo() {
        flowMap.put("foo", "barx");
    }

    public String getFoo() {
        return (String) flowMap.get("foo");
    }

}
