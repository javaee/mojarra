/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU General
 * Public License Version 2 only ("GPL") or the Common Development and
 * Distribution License("CDDL") (collectively, the "License"). You may not use
 * this file except in compliance with the License. You can obtain a copy of the
 * License at https://glassfish.dev.java.net/public/CDDLGPL_1_1.html or
 * packager/legal/LICENSE.txt. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception: Oracle designates this particular file as subject to
 * the "Classpath" exception as provided by Oracle in the GPL Version 2 section
 * of the License file that accompanied this code.
 *
 * Modifications: If applicable, add the following below the License Header,
 * with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s): If you wish your version of this file to be governed by only
 * the CDDL or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution under the
 * [CDDL or GPL Version 2] license." If you don't indicate a single choice of
 * license, a recipient has the option to distribute your version of this file
 * under either the CDDL, the GPL Version 2 or to extend the choice of license
 * to its licensees as provided above. However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright holder.
 */
package com.sun.faces.test.agnostic.dynamic;

import com.sun.faces.context.StateContext;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * @author Manfred Riem
 */
@ManagedBean
@RequestScoped
public class Issue2395Bean {

    private HtmlPanelGroup panelGroup;

    public HtmlPanelGroup getPanelGroup() {
        return panelGroup;
    }

    public void setPanelGroup(HtmlPanelGroup panelGroup) {
        this.panelGroup = panelGroup;
    }

    public void doAdd(ActionEvent event) {
        HtmlOutputText out = new HtmlOutputText();
        getPanelGroup().getChildren().add(out);
        FacesContext context = FacesContext.getCurrentInstance();
        StateContext stateContext = StateContext.getStateContext(context);
        if (stateContext.getDynamicActions().size() != 1) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, 
                    "Number of dynamic actions should be 1",
                    "Number of dynamic actions should be 1"));
        }
    }
    
    public void doAddRemove(ActionEvent event) {
        HtmlOutputText out = new HtmlOutputText();
        getPanelGroup().getChildren().add(out);
        FacesContext context = FacesContext.getCurrentInstance();
        StateContext stateContext = StateContext.getStateContext(context);
        if (stateContext.getDynamicActions().size() != 1) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, 
                    "Number of dynamic actions should be 1",
                    "Number of dynamic actions should be 1"));
        }

        getPanelGroup().getChildren().remove(out);
        if (!stateContext.getDynamicActions().isEmpty()) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, 
                    "Number of dynamic actions should be 0",
                    "Number of dynamic actions should be 0"));
        }
    }

    public void doAddRemoveAdd(ActionEvent event) {
        HtmlOutputText out = new HtmlOutputText();
        out.setTitle("Issue2395addRemoveAdd");
        getPanelGroup().getChildren().add(out);
        FacesContext context = FacesContext.getCurrentInstance();
        StateContext stateContext = StateContext.getStateContext(context);
        if (stateContext.getDynamicActions().size() != 1) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, 
                    "Number of dynamic actions should be 1",
                    "Number of dynamic actions should be 1"));
        }

        getPanelGroup().getChildren().remove(out);
        if (!stateContext.getDynamicActions().isEmpty()) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, 
                    "Number of dynamic actions should be 0",
                    "Number of dynamic actions should be 0"));
        }

        getPanelGroup().getChildren().add(out);
        if (stateContext.getDynamicActions().size() != 1) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, 
                    "Number of dynamic actions should be 1",
                    "Number of dynamic actions should be 1"));
        }
    }

    public void doRemove(ActionEvent event) {
        getPanelGroup().getChildren().remove(0);
        FacesContext context = FacesContext.getCurrentInstance();
        StateContext stateContext = StateContext.getStateContext(context);
        if (stateContext.getDynamicActions().size() != 1) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, 
                    "Number of dynamic actions should be 1",
                    "Number of dynamic actions should be 1"));
        }
    }
    
    public void doRemoveAdd(ActionEvent event) {
        UIComponent component = getPanelGroup().getChildren().remove(0);
        FacesContext context = FacesContext.getCurrentInstance();
        StateContext stateContext = StateContext.getStateContext(context);
        if (stateContext.getDynamicActions().size() != 1) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, 
                    "Number of dynamic actions should be 1",
                    "Number of dynamic actions should be 1"));
        }
        
        getPanelGroup().getChildren().add(component);
        if (stateContext.getDynamicActions().size() != 2) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, 
                    "Number of dynamic actions should be 2",
                    "Number of dynamic actions should be 2"));
        }
    }
    
    public void doRemoveAddRemove(ActionEvent event) {
        UIComponent component = getPanelGroup().getChildren().remove(0);
        FacesContext context = FacesContext.getCurrentInstance();
        StateContext stateContext = StateContext.getStateContext(context);
        if (stateContext.getDynamicActions().size() != 1) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, 
                    "Number of dynamic actions should be 1",
                    "Number of dynamic actions should be 1"));
        }
        
        getPanelGroup().getChildren().add(component);
        if (stateContext.getDynamicActions().size() != 2) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, 
                    "Number of dynamic actions should be 2",
                    "Number of dynamic actions should be 2"));
        }
        
        getPanelGroup().getChildren().remove(component);
        if (stateContext.getDynamicActions().size() != 1) {
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, 
                    "Number of dynamic actions should be 1",
                    "Number of dynamic actions should be 1"));
        }
    }
}
