package com.oracle.faces.cdi.test;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "cdiBean")
@ConversationScoped
public class CdiBean implements Serializable {

    @Inject
    private CdiInjectedBean injectedBean;
    
    @Inject
    private Conversation conversation;
    
    @PostConstruct
    public void init() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }
    
    public Conversation getConversation() {
        return this.conversation;
    }

    public CdiInjectedBean getInjectedBean() {
        return this.injectedBean;
    }
    
    public String getText() {
        return "CDI is active!";
    }
    
    public String getUrl() {
        return FacesContext.getCurrentInstance().getExternalContext().encodeActionURL("cdi.xhtml");
    }
    
    @PreDestroy
    public void destroy() {
        System.out.println("Destroy!");
    }
}
