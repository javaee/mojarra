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
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.component.UIMessages;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * <p>
 * When this {@link PhaseListener} is present, the following will occur:
 * <ul>
 *   <li>
 *     If no UIMessage or UIMessages component is present within the view,
 *     a UIMessages component will be added on behalf of the user to display
 *     any unhandled FacesMessages.
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


    // ---------------------------------------------- Methods from PhaseListener


    /**
     * <p>
     * If a <code>UIMessage</code> or <code>UIMessages</code> component is not
     * found within the view, add a <code>UIMessages</code> to the view
     * on behalf of the developer.
     * </p>
     *
     * RELEASE_PENDING this is probably only applicable for views containg
     *  form.  Right now, if a developer has a read-only view (i.e. only
     *  output components, this would cause the messages component to be
     *  added.
     *
     * @see PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
     */
    public void afterPhase(PhaseEvent event) {

        FacesContext context = event.getFacesContext();

        if (!isMessageComponentNeeded(context.getViewRoot())) {
            addMessagesComponent(context);
        }

    }


    /**
     * <p>
     * This is a no-op
     * </p>
     *
     * @see PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     */
    public void beforePhase(PhaseEvent event) {
    }


    /**
     * @return {@link PhaseId#RESTORE_VIEW}
     *
     * @see javax.faces.event.PhaseListener#getPhaseId()
     */
    public PhaseId getPhaseId() {

        return PhaseId.RESTORE_VIEW;

    }


    // --------------------------------------------------------- Private Methods


    
    private boolean isMessageComponentNeeded(UIViewRoot root) {

        for (Iterator<UIComponent> i = root.getFacetsAndChildren(); i.hasNext();) {
            UIComponent c = i.next();
            UIComponent message = findMessageComponent(c);
            if (message != null) {
                return true;
            }
        }
        return false;

    }


    private UIComponent findMessageComponent(UIComponent component) {

        if (component instanceof UIMessage || component instanceof UIMessages) {
            return component;
        }

        for (Iterator<UIComponent> i = component.getFacetsAndChildren(); i.hasNext(); ) {
            UIComponent c = i.next();
            if (c instanceof UIMessage || c instanceof UIMessages) {
                return c;
            }
            UIComponent nc = findMessageComponent(c);
            if (nc != null) {
                return nc;
            }
        }

        return null;

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
        UIOutput caption = (UIOutput) app.createComponent("javax.faces.Output");
        caption.setValue("ProjectStage[Development]: Messages");
        caption.getAttributes().put("style", "color: red");
        panel.getAttributes().put("columns", 1);
        panel.getFacets().put("caption", caption);
        UIOutput footer = (UIOutput) app.createComponent("javax.faces.Output");
        footer.setValue("Add your own message handling to prevent this from appearing.");
        footer.getAttributes().put("style", "color: red");        
        panel.getFacets().put("footer", footer);
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

}
