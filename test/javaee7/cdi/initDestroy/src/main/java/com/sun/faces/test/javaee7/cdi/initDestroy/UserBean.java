package com.sun.faces.test.javaee7.cdi.initDestroy;

import java.io.Serializable;
import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;

@Named
@SessionScoped
public class UserBean implements Serializable {
    
    protected String firstName = "Duke";
    protected String lastName = "Java";
    protected Date dob;
    protected String sex = "Unknown";
    protected String email;
    protected String serviceLevel = "medium";
    
    public UserBean() {}

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
    
    
    private String initMessage;

    public String getInitMessage() {
        return initMessage;
    }

    public void setInitMessage(String initMessage) {
        this.initMessage = initMessage;
    }
    
    private String destroyMessage;

    public String getDestroyMessage() {
        return destroyMessage;
    }

    public void setDestroyMessage(String destroyMessage) {
        this.destroyMessage = destroyMessage;
    }
    
    private String initFlowMessage;

    public String getInitFlowMessage() {
        return initFlowMessage;
    }

    public void setInitFlowMessage(String initFlowMessage) {
        this.initFlowMessage = initFlowMessage;
    }
    
    private String destroyFlowMessage;

    public String getDestroyFlowMessage() {
        return destroyFlowMessage;
    }

    public void setDestroyFlowMessage(String destroyFlowMessage) {
        this.destroyFlowMessage = destroyFlowMessage;
    }
    
    private String initViewScopeMesasge;

    public String getInitViewScopeMesasge() {
        return initViewScopeMesasge;
    }

    public void setInitViewScopeMesasge(String initViewScopeMesasge) {
        this.initViewScopeMesasge = initViewScopeMesasge;
    }
    
    private String destroyViewScopeMessage;

    public String getDestroyViewScopeMessage() {
        return destroyViewScopeMessage;
    }

    public void setDestroyViewScopeMessage(String destroyViewScopeMessage) {
        this.destroyViewScopeMessage = destroyViewScopeMessage;
    }
}

