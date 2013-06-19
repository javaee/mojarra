package com.sun.faces.test.agnostic.facelets.viewAction;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;


/**
 * @author <a href="http://community.jboss.org/people/bleathem)">Brian Leathem</a>
 */
@RequestScoped
@ManagedBean
public class ViewActionTestBean {
    
    @ManagedProperty(value="#{facesContext}")
    private FacesContext context;
            
    public String action() {
        return "result";
    }
    
    public String pageA() {
        getContext().getExternalContext().getRequestMap().put("message", "pageA action");
        return "pageA";
    }

    public String pageAExplicitRedirect() {
        getContext().getExternalContext().getRequestMap().put("message", "pageA explicit redirect");
        return "pageAExplicitRedirect";
    }

    public String returnEmpty() {
        getContext().getExternalContext().getRequestMap().put("message", "pageA empty");
        return "";
    }

    public String returnNull() {
        getContext().getExternalContext().getRequestMap().put("message", "pageA null");
        return null;
    }

    /**
     * @return the context
     */
    public FacesContext getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(FacesContext context) {
        this.context = context;
    }
    

}
