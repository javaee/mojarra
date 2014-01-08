package com.sun.faces.test.agnostic.facelets.viewAction;

import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;


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
    
    private static class ActionListenerImpl implements ActionListener  {
        
        private String id;

        private ActionListenerImpl(String id) {
            this.id = id;
        }

        @Override
        public void processAction(ActionEvent event) throws AbortProcessingException {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            
            String message = (String) sessionMap.get("message");
            message = (null == message) ? "" : message + " ";
            message = message + id + " " + event.getComponent().getId();
            sessionMap.put("message", message);
        }
    }
    
    public ActionListener getActionListener01() {
        return new ActionListenerImpl("01");
    }
    
    public ActionListener getActionListener02() {
        return new ActionListenerImpl("02");
    }
    
    public void actionListenerMethod(ActionEvent event) {
        ActionListenerImpl actionListener = new ActionListenerImpl("method");
        actionListener.processAction(event);
    }

}
