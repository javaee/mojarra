/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.systest.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author edburns
 */
public class ComponentScopeBean {
    
    private static final String key = "requestMessages";
    
    public List<String> getRequestMessageList() {
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();

        List<String> requestMessages = null;
        if (requestMap.containsKey(key)) {
            requestMessages = (List<String>) requestMap.get(key);
        }
        else {
            requestMessages = new ArrayList<String>();
            requestMap.put(key, requestMessages);
        }
        return requestMessages;
    }
    
    
    
    public Converter getConverter() {
        Converter result = null;
        
        result = new Converter() {

            public Object getAsObject(FacesContext context, UIComponent component, 
                    String value) {
                String message = "converter.getAsObject called. Component from EL: " +
                        context.getApplication().evaluateExpressionGet(context, 
                        "#{component.class.name}", String.class) + " id: " + 
                        context.getApplication().evaluateExpressionGet(context, 
                        "#{component.id}", String.class);
                getRequestMessageList().add(message);
                return value;
            }

            public String getAsString(FacesContext context, UIComponent component, Object value) {
                String message = "converter.getAsObject called. Component from EL: " +
                        context.getApplication().evaluateExpressionGet(context, 
                        "#{component.class.name}", String.class) + " id: " + 
                        context.getApplication().evaluateExpressionGet(context, 
                        "#{component.id}", String.class);
                getRequestMessageList().add(message);
                return (String) value;
            }
            
        };
        
        return result;
    }
    
    public void validator(FacesContext context, UIComponent component, 
            Object value) {
        String message = "validator called. Component from EL: " +
                         context.getApplication().evaluateExpressionGet(context,
                         "#{component.class.name}", String.class) + " id: " +
                         context.getApplication().evaluateExpressionGet(context,
                         "#{component.id}", String.class);
        getRequestMessageList().add(message);
    }
    
    public void valueChangeListener(ValueChangeEvent e) {
        FacesContext context = FacesContext.getCurrentInstance();
        String message = "validator called. Component from EL: " +
                         context.getApplication().evaluateExpressionGet(context,
                         "#{component.class.name}", String.class) + " id: " +
                         context.getApplication().evaluateExpressionGet(context,
                         "#{component.id}", String.class);
        getRequestMessageList().add(message);
    }
    
    public String getRequestMessages() {
        StringBuilder builder = new StringBuilder();
        
        for (String cur : getRequestMessageList()) {
            builder.append("<p>" + cur + "</p>");
        }
        
        return builder.toString();
    }
    
}
