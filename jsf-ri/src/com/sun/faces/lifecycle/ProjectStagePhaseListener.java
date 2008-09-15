/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */


package com.sun.faces.lifecycle;


import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIMessage;
import javax.faces.component.UIMessages;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlMessage;
import javax.faces.component.html.HtmlMessages;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AfterAddToParentEvent;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import com.sun.faces.util.MessageUtils;

/**
 * <p>
 * When this {@link PhaseListener} is present, the following will occur:
 * <ul>
 *   <li>
 *     If no UIMessage or UIMessages component is present within the view,
 *     a UIMessages component will be added on behalf of the user to display
 *     any unhandled FacesMessages.
 *   </li>
 *   <li>
 *     If a UICommand is present within the view but not nested within a
 *     UIForm, queue a message stating such and that any actions associated
 *     with that command will not be invoked.
 *   </li>
 * </ul>
 * </p>
 *
 * <p>
 * NOTE:  This PhaseListener is installed only when {@link javax.faces.application.ProjectStage} is
 * {@link javax.faces.application.ProjectStage#Development}.
 * </p>
 *
 * @see com.sun.faces.config.listeners.ProjectStagePhaseListenerInstallationListener
 */
public class ProjectStagePhaseListener implements PhaseListener {

    private static final long serialVersionUID = 8281381763781233640L;

    private static final String HAS_MESSAGES =
          ProjectStagePhaseListener.class.getName() + ".HAS_MESSAGES";
    private static final String HAS_COMMAND_NO_FORM =
          ProjectStagePhaseListener.class.getName() + ".HAS_COMMAND_NO_FORM";


    // ------------------------------------------------------------ Constructors

    
    public ProjectStagePhaseListener() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        subscribeSystemEventListener(ctx,
                                     AfterAddToParentEvent.class,
                                     new MessageListener(),
                                     UIMessage.class,
                                     UIMessages.class,
                                     HtmlMessage.class,
                                     HtmlMessages.class);
        subscribeSystemEventListener(ctx,
                                     AfterAddToParentEvent.class,
                                     new CommandListener(),
                                     UICommand.class,
                                     HtmlCommandButton.class,
                                     HtmlCommandLink.class);

    }


    // ---------------------------------------------- Methods from PhaseListener


    public void afterPhase(PhaseEvent event) {

        FacesContext context = event.getFacesContext();
        Map<Object,Object> attributes = context.getAttributes();
        if (!attributes.containsKey(HAS_MESSAGES)) {
            addMessagesComponent(context);

        }
        if (attributes.containsKey(HAS_COMMAND_NO_FORM)) {
            context.addMessage(null, MessageUtils.getExceptionMessage(MessageUtils.COMMAND_NOT_NESTED_WITHIN_FORM_ID));
        }

    }


    public void beforePhase(PhaseEvent event) {
    }


    public PhaseId getPhaseId() {

        return PhaseId.RESTORE_VIEW;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>
     * For source class provided, call {@link Application#subscribeToEvent(Class, Class, javax.faces.event.SystemEventListener)}
     * passing in the provided {@link SystemEvent} class and {@link SystemEventListener}
     * implementation.
     * </p>
     *
     * @param ctx the {@link FacesContext} for the current request
     * @param systemEvent {@link SystemEvent}
     * @param listener {@link SystemEventListener}
     * @param sources one or more sources for the event
     */
    private void subscribeSystemEventListener(FacesContext ctx,
                                              Class<? extends SystemEvent> systemEvent,
                                              SystemEventListener listener,
                                              Class<?>... sources) {

        Application app = ctx.getApplication();
        for (Class<?> source : sources) {
            app.subscribeToEvent(systemEvent, source, listener);
        }

    }

    
    /**
     * <p>
     * Add a stylized UIMessages component to the current UIViewRoot.
     * </p>
     *
     * @param context the {@link FacesContext} for the current request
     */
    private void addMessagesComponent(FacesContext context) {

        Application app = context.getApplication();
        UIViewRoot root = context.getViewRoot();

        UIComponent panel = app.createComponent("javax.faces.Panel");
        panel.setRendererType("javax.faces.Grid");
        UIOutput output = (UIOutput) app.createComponent("javax.faces.Output");
        output.setValue("ProjectStage[Development]: Messages - Add your own message handling to prevent this from appearing.");
        output.getAttributes().put("style", "color: red");
        panel.getAttributes().put("columns", 1);
        panel.getFacets().put("caption", output);
        // Add a component to hold the messages
        UIComponent messages = app.createComponent("javax.faces.Messages");
        messages.setTransient(true);
        Map<String,Object> attrs = messages.getAttributes();
        attrs.put("style", "");
        attrs.put("layout", "table");
        attrs.put("title", "Development Mode Messages");
        attrs.put("style", "font-family: \'Trebuchet MS\', Verdana, Arial, Sans-Serif; font-size: small; color: #339;");
        attrs.put("tooltip", Boolean.TRUE);
        messages.setRendererType("javax.faces.Messages");
        // RELEASE_PENDING (edburns,rlubke) this won't work if people aren't
        // using the body tag.
        panel.getChildren().add(messages);
        root.addComponentResource(context, panel, "body");

    }


    // ---------------------------------------------------------- Nested Classes


    /**
     * Invoked when a UIMessage, UIMessages, HtmlMessage, or HtmlMessages has
     * been added to the tree. When this occurs, we add a flag to the current
     * FacesContext to indicate that the ProjectStagePhaseListener does not need
     * to add a messages component to the view.
     */
    private static class MessageListener implements SystemEventListener {


        // ------------------------------------ Methods from SystemEventListener


        public void processEvent(SystemEvent event)
        throws AbortProcessingException {

            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.getAttributes().put(HAS_MESSAGES, Boolean.TRUE);

        }

        public boolean isListenerForSource(Object source) {

            return (source instanceof UIMessage || source instanceof UIMessages);

        }
        
    }


    /**
     * Invoked when a UICommand, HtmlCommandButton, or HtmlCommandLink has
     * been added to the tree. When this occurs, check to see if the UICommand
     * instance has a UIForm parent.  If it doesn't, add a flag to the
     * FacesContext so that the ProjectStatePhaseListener can queue a message
     * informing the user of their error.
     */
    private static class CommandListener implements SystemEventListener {


        // ------------------------------------ Methods from SystemEventListener


        public void processEvent(SystemEvent event)
        throws AbortProcessingException {

            UICommand command = (UICommand) event.getSource();
            UIComponent parent = command.getParent();
            while (parent != null) {
                if (parent instanceof UIForm) {
                    return;
                }
                parent = parent.getParent();
            }
            
            // no form parent found
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.getAttributes().put(HAS_COMMAND_NO_FORM, Boolean.TRUE);

        }

        
        public boolean isListenerForSource(Object source) {

            return (source instanceof UICommand);

        }
    }


}
