package com.sun.faces.regression.i_spec_762;

import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@RequestScoped
@Named("bean")
public class UserBean {

    void appendMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        StringBuilder builder;
        builder = (StringBuilder) requestMap.get("builder");
        if (null == builder) {
            builder = new StringBuilder();
            requestMap.put("builder", builder);
        }
        builder.append(message);
    }
    
    public String getMessage() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String result = (requestMap.containsKey("builder")) ? ((StringBuilder)requestMap.get("builder")).toString() : "no message";

        return result;
    }
    
    
}

