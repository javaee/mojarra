/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package com.sun.faces.systest.state;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.validator.ValidatorException;

@ManagedBean
@RequestScoped
public class DynamicStateBean {
    
    public void validateDeletion(FacesContext context, UIComponent comp, Object val) {
        // The button should not be here on postback
        UIComponent button = findButton(context);
        if (null != button) {
            throw new ValidatorException(new FacesMessage("cbutton should not be found"));
        }
        
    }
    
    public void validateAddition(FacesContext context, UIComponent comp, Object val) {
        // The button should not be here on postback
        UIComponent button = findButton(context);
        if (null == button) {
            throw new ValidatorException(new FacesMessage("cbutton should be found"));
        }
        
    }
    public void beforeRenderDeletion(ComponentSystemEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        
        UIComponent 
                buttonParent = null, 
                button = findButton(context);
        if (null != button) {
            buttonParent = button.getParent();
            buttonParent.getChildren().remove(button);
        }
    }

    public void beforeRenderAddition(ComponentSystemEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        
        UIComponent
                form = findForm(context);
        HtmlCommandButton button;
        if (null == (button = (HtmlCommandButton) findButton(context))) {
            button = new HtmlCommandButton();
            button.setId("cbutton");
            button.setValue("added button");
            form.getChildren().add(button);
        }
    }


    public void transientRoot(ActionEvent ae) {
       
        UIComponent button = ae.getComponent();
        UIComponent addto = button.findComponent("addto");

        HtmlPanelGroup transientRoot = new HtmlPanelGroup();
        transientRoot.setTransient(true);
        transientRoot.setId("troot");
        StateComponent text = new StateComponent();
        text.setValue("transient parent");
        text.setId("text");
        HtmlPanelGroup group = new HtmlPanelGroup();
        group.setId("group");
        StateComponent text2 = new StateComponent();
        text2.setValue(" test");
        text2.setId("text2");
        group.getChildren().add(text2);
        transientRoot.getChildren().add(text);
        transientRoot.getChildren().add(group);
        addto.getChildren().add(transientRoot);

    }


    
    
    private UIComponent findButton(FacesContext context) {
        char sep = UINamingContainer.getSeparatorChar(context);
        UIComponent result = null;
                result = context.getViewRoot().findComponent(sep + "form" + 
                sep + "cbutton");
        return result;
    }

    private UIComponent findForm(FacesContext context) {
        char sep = UINamingContainer.getSeparatorChar(context);
        UIComponent result = null;
                result = context.getViewRoot().findComponent(sep + "form");
        return result;
    }


    public static class StateComponent extends HtmlOutputText {


        @Override public Object saveState(FacesContext context) {

            throw new FacesException("saveState(FacesContext) was incorrectly called for component with client ID: "
                                     + this.getClientId(context));
        }

    }
    
}
