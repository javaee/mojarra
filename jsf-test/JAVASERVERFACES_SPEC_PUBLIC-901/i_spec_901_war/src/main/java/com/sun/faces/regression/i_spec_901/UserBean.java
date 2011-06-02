package com.sun.faces.regression.i_spec_901;

import java.util.Date;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.validator.ValidatorException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

@ManagedBean(name="bean")
@SessionScoped
public class UserBean {
    
    protected String firstName;
    protected String lastName;
    protected Date dob;
    protected String sex;
    protected String email;
    protected String serviceLevel = "medium";
    
    protected ActionListenerImpl a, b, c;
    
    public UserBean() {
        a = new ActionListenerImpl("a called ");
        b = new ActionListenerImpl("b called ");
        c = new ActionListenerImpl("c called ");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }
    
    public void validateEmail(FacesContext context, UIComponent toValidate,
            Object value) throws ValidatorException {
        String emailStr = (String) value;
        if (-1 == emailStr.indexOf("@")) {
            FacesMessage message = new FacesMessage("Invalid email address");
            throw new ValidatorException(message);
        }
    }

    public String addConfirmedUser() {
        // This method would call a database or other service and add the 
        // confirmed user information.
        // For now, we just place an informative message in request scope
        FacesMessage doneMessage = 
                new FacesMessage("Successfully added new user");
        FacesContext.getCurrentInstance().addMessage(null, doneMessage);
        return "done";
    }

    public ActionListener getLoginEventListener() {
        return a;
    }

    public ActionListener getLoginEventListener2() {
        return b;
    }

    public ActionListener cancelEventListener(ActionEvent e) {
        return c;
    }
    
    private void appendMessage(String message) {
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
    
    private class ActionListenerImpl implements ActionListener {
        
        private String message;
        
        private ActionListenerImpl(String message) {
            this.message = message;
        }

        @Override
        public void processAction(ActionEvent event) throws AbortProcessingException {
            UserBean.this.appendMessage(message);

        }
        
        
    }
}

