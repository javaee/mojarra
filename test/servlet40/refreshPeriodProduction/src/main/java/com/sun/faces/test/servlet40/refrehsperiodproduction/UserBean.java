package com.sun.faces.test.servlet40.refrehsperiodproduction;

import com.sun.faces.application.ApplicationAssociate;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class UserBean implements Serializable {
    
    protected String firstName;
    @Inject 
    ExternalContext extContext;
    
    public UserBean() {
    }
    
    @PostConstruct
    private void init() {
        ApplicationAssociate appAss = ApplicationAssociate.getInstance(extContext);
        firstName = "" + appAss.getFaceletFactory().getRefreshPeriod();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}

