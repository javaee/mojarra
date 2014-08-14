package com.sun.faces.test.javaee7.beanValidator.methodValidator;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;

@Named
@RequestScoped
public class UserBean implements Serializable {
    
    @Inject
    HelloService hello;
    
    protected String methodValidationValue = "foo";
    protected String valueValidationValue = "foo";
    
    public UserBean() {}

    @FooConstraint
    public String getValueValidationValue() {
        return valueValidationValue;
    }

    public void setValueValidationValue(String ValueValidationValue) {
        this.valueValidationValue = ValueValidationValue;
    }

    public String getMethodValidationValue() {
        return methodValidationValue;
    }

    public void setMethodValidationValue(String firstName) {
        this.methodValidationValue = firstName;
    }
    
    public String getHelloValue() {
        return hello.sayHello(getMethodValidationValue());
    }
    
    public void preRenderViewListener() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        String value = (String) context.getExternalContext().getRequestMap().get("value");
        if (null != value && value.equals("bar")) {
            try {
                hello.sayHello(value);
            } catch (ConstraintViolationException e) {
                FacesMessage m = new FacesMessage(e.getMessage());
                context.addMessage(null, m);
            }
        }
        
    }
    
}

