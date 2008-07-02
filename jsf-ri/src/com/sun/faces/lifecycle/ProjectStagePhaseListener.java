/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.lifecycle;

import java.util.Map;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author edburns
 */
public class ProjectStagePhaseListener implements PhaseListener {
    
    public static final String VIEW_HAS_MESSAGE_OR_MESSAGES_ELEMENT =
            "com.sun.faces.lifecycle.ProjectStageHasMessages";
    public static final String VIEW_HAS_FORM_ELEMENT =
            "com.sun.faces.lifecycle.ProjectStageHasForm";

    public void afterPhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        Application app = context.getApplication();
        if (ProjectStage.Development == app.getProjectStage()) {
            if (!context.getAttributes().containsKey(VIEW_HAS_MESSAGE_OR_MESSAGES_ELEMENT)) {
                UIViewRoot root = context.getViewRoot();
                
                // Add a component to style the error messages
                UIOutput messagesStyle = (UIOutput) app.createComponent("javax.faces.Output");
                messagesStyle.setRendererType("javax.faces.Text");
                Map<String, Object> attrs = messagesStyle.getAttributes();
                attrs.put("escape", false);
                messagesStyle.setValue("<style type=\"text/css\">.ProjectStageDevelopment { font-family: \'Trebuchet MS\', Verdana, Arial, Sans-Serif; font-size: small; color: #339; }</style>");
                root.addComponentResource(context, messagesStyle, "head");
                
                // Add a component to hold the messages
                UIComponent messages = app.createComponent("javax.faces.Messages");
                attrs = messages.getAttributes();
                attrs.put("style", "");
                attrs.put("layout", "table");
                attrs.put("title", "Development Mode Messages");
                attrs.put("styleClass", "ProjectStageDevelopment");
                attrs.put("tooltip", Boolean.TRUE);
                messages.setRendererType("javax.faces.Messages");
                root.addComponentResource(context, messages, "body");
            }
            if (!context.getAttributes().containsKey(VIEW_HAS_FORM_ELEMENT)) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Warning: This page has no form element.", ""));

            }

        }
    }

    public void beforePhase(PhaseEvent arg0) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
    
    

}
