/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.systest.model;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

/**
 *
 */
public class EventTagBean {
    
    
    public void beforeViewRender(ComponentSystemEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        
        context.getExternalContext().getRequestMap().put("preRenderComponentMessage", 
                event.getComponent().getClass() + " pre-render");
    }


    public void beforeEncode(ComponentSystemEvent event) {
        UIOutput output = (UIOutput)event.getComponent();
        output.setValue("The '" + event.getClass().getName() + "' event fired!");
    }
    
    public void beforeEncodeNoArg() {
        FacesContext context = FacesContext.getCurrentInstance();
        UIOutput output = (UIOutput) context.getViewRoot().findComponent("form:noArgTest");
//        UIOutput output = (UIOutput)event.getComponent();
        output.setValue("The no-arg event fired!");
    }

    public void postValidate(ComponentSystemEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        final UIForm form = (UIForm) event.getComponent();
        final String [] clientIds = { "lesser", "greater" };
        final int [] values = new int[2];
        final boolean [] hasValues = new boolean[2];
        final List<FacesMessage> toAdd = new ArrayList<FacesMessage>();
        
        // Traverse the form and suck out the individual values
        for (int i = 0; i < clientIds.length; i++) {
            final int finalI = i;
            form.invokeOnComponent(context, clientIds[i], new ContextCallback() {

                public void invokeContextCallback(FacesContext context, UIComponent target) {
                    Object value = ((ValueHolder) target).getValue();
                    try {
                        if (null != value) {
                            values[finalI] = Integer.parseInt(value.toString());
                            hasValues[finalI] = true;
                        } else {
                            hasValues[finalI] = false;
                            FacesMessage msg = new FacesMessage(clientIds[finalI] +
                                    " must have a value");
                            toAdd.add(msg);
                        }
                    } catch (NumberFormatException nfe) {
                        FacesMessage msg = new FacesMessage("unable to parse the number for field " + 
                                clientIds[finalI]);
                        toAdd.add(msg);
                    }

                }
            });
        }

        // case one, ensure both fields have a value
        if (!hasValues[0] || !hasValues[1]) {
            FacesMessage msg = new FacesMessage("both fields must have a value");
            toAdd.add(msg);
        } else {
            // case two, ensure lesser is lesser than greater
            if (!(values[0] < values[1])) {
                FacesMessage msg = new FacesMessage("lesser must be lesser than greater");
                toAdd.add(msg);
            }
        }
        
        // If we have any messages
        if (!toAdd.isEmpty()) {
            // add them so the user sees the message
            String formClientId = form.getClientId(context);
            for (FacesMessage cur : toAdd) {
                context.addMessage(formClientId, cur);
            }
            // skip remaining lifecycle phases
            context.renderResponse();
        }
    }
    
}
