/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.systest.state;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
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
    
}
