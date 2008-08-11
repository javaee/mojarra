/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.faces.lifecycle;

import com.sun.faces.util.Util;
import com.sun.faces.util.Util.TreeTraversalCallback;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
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

    public void afterPhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        Application app = context.getApplication();
        if (ProjectStage.Development == app.getProjectStage()) {
            UIViewRoot root = context.getViewRoot();
            if (!context.getAttributes().containsKey(VIEW_HAS_MESSAGE_OR_MESSAGES_ELEMENT)) {
                
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
            // scan for a form component
            final Boolean [] result = new Boolean[1];
            result[0] = Boolean.FALSE;
            
            Util.prefixViewTraversal(context, root, new TreeTraversalCallback() {

                public boolean takeActionOnNode(FacesContext context, UIComponent curNode) throws FacesException {
                    if (curNode instanceof UIForm) {
                        result[0] = Boolean.TRUE;
                        return false;
                    }
                    return true;
                }
            });
            if (!result[0].booleanValue()) {
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
