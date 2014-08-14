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

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;

@ManagedBean
public class DynamicStateBean2 {

    private String value;

    public DynamicStateBean2() {
        value = "default value";
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void render() {

        // <h:form id="form">
        //   <h:panelGroup id="viewPanel"/>
        // </h:form>

        UIComponent viewPanel = FacesContext
              .getCurrentInstance()
              .getViewRoot()
              .findComponent("form:viewPanel");

        viewPanel.getChildren().clear();

        UIComponent childPanel = new HtmlPanelGroup();
        childPanel.setId("childPanel");
        viewPanel.getChildren().add(childPanel);

        // Add a textinput to the inner most panel with a
        // binding to the the value property of this bean.
        //  ...
        //  <h:panelGroup id="viewPanel">
        //      <h:panelGroup id="childPanel"/>
        //        <h:textInput value="#{render.value}"/>
        //      </h:panelGroup>
        //  </h:panelGroup>
        //  ...

        UIComponent textInput = new HtmlInputText();
        textInput.setId("textInput");
        textInput.setValueExpression("value", FacesContext.getCurrentInstance()
              .getApplication().getExpressionFactory().createValueExpression(
              FacesContext.getCurrentInstance().getELContext(),
              "#{dynamicStateBean2.value}", Object.class));

        childPanel.getChildren().add(textInput);
    }

    public void render2() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        UIComponent viewPanel = ctx.getViewRoot().findComponent("form:viewPanel");

        viewPanel.getChildren().clear();

        HtmlCommandButton btn = (HtmlCommandButton)
              ctx.getApplication().createComponent(HtmlCommandButton.COMPONENT_TYPE);
        btn.setValue("dynamically added button");

        // Using a non-generated identifier will not cause
        // the exception to be thrown.

        //btn.setId("btn");

        viewPanel.getChildren().add(btn);

    }
    
}
