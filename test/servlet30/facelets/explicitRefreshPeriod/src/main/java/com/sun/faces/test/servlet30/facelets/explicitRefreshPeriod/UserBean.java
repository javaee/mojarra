package com.sun.faces.test.servlet30.facelets.explicitRefreshPeriod;

import com.sun.faces.application.ApplicationAssociate;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class UserBean implements Serializable {
    private static final long serialVersionUID = 8120165093754719188L;
    
    protected String firstName;
    
    ExternalContext extContext;
    
    public UserBean() {
    }
    
    @PostConstruct
    private void init() {
        extContext = FacesContext.getCurrentInstance().getExternalContext();
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

