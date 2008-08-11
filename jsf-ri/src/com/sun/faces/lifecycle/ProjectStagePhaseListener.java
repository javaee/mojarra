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

import com.sun.faces.util.Util;
import com.sun.faces.util.Util.TreeTraversalCallback;

import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * RELEASE_PENDING (rlubke,driscoll) docs
 */
public class ProjectStagePhaseListener implements PhaseListener {

    private static final long serialVersionUID = 8281381763781233640L;

    public static final String VIEW_HAS_MESSAGE_OR_MESSAGES_ELEMENT =
          "com.sun.faces.lifecycle.ProjectStageHasMessages";


    // ---------------------------------------------- Methods from PhaseListener


    public void afterPhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        Application app = context.getApplication();
        UIViewRoot root = context.getViewRoot();
        if (!context.getAttributes()
              .containsKey(VIEW_HAS_MESSAGE_OR_MESSAGES_ELEMENT)) {

            // Add a component to style the error messages
            UIOutput messagesStyle =
                  (UIOutput) app.createComponent("javax.faces.Output");
            messagesStyle.setTransient(true);
            messagesStyle.setRendererType("javax.faces.Text");
            Map<String, Object> attrs = messagesStyle.getAttributes();
            attrs.put("escape", false);
            messagesStyle
                  .setValue("<style type=\"text/css\">.ProjectStageDevelopment { font-family: \'Trebuchet MS\', Verdana, Arial, Sans-Serif; font-size: small; color: #339; }</style>");
            root.addComponentResource(context, messagesStyle, "head");

            // Add a component to hold the messages
            UIComponent messages = app.createComponent("javax.faces.Messages");
            messages.setTransient(true);
            attrs = messages.getAttributes();
            attrs.put("style", "");
            attrs.put("layout", "table");
            attrs.put("title", "Development Mode Messages");
            attrs.put("styleClass", "ProjectStageDevelopment");
            attrs.put("tooltip", Boolean.TRUE);
            messages.setRendererType("javax.faces.Messages");
            // RELEASE_PENDING (edburns,rlubke) this won't work if people aren't
            // using the body tag.
            root.addComponentResource(context, messages, "body");
        }
        // scan for a form component
        final Boolean[] result = new Boolean[1];
        result[0] = Boolean.FALSE;

        Util.prefixViewTraversal(context, root, new TreeTraversalCallback() {

            public boolean takeActionOnNode(FacesContext context,
                                            UIComponent curNode)
                  throws FacesException {
                if (curNode instanceof UIForm) {
                    result[0] = Boolean.TRUE;
                    return false;
                }
                return true;
            }
        });
        if (!result[0].booleanValue()) {
            context
                  .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                                     "Warning: This page has no form element.", ""));
        }

    }


    public void beforePhase(PhaseEvent event) {
    }


    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }


}
