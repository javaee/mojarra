package com.oracle.faces.cdi.test;

import java.io.Serializable;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

@Named(value = "conversationBean")
@ConversationScoped
public class ConversationBean implements Serializable {
    
    @Inject Conversation conversation;
    
    public String start() {
        conversation.begin();
        return "conversation2";
    }
    
    public String end() {
        conversation.end();
        return "conversation2";
    }
    
    public String cont() {
        return "conversation2";
    }
    
    public String invalidate() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        return "conversation2";
    }
    
    public boolean isActive() {
        return !conversation.isTransient();
    }
    
    public String getId() {
        return conversation.getId();
    }
}
