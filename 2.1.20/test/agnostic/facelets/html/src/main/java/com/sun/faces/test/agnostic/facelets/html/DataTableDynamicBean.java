/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.agnostic.facelets.html;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.el.ValueExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

@SessionScoped
@ManagedBean(name = "dataTableDynamicBean")
public class DataTableDynamicBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String getTitle() {
        return "Can not add table dynamically";
    }

    public String getAddValue() {
        return "Add Datatable";
    }

    public void addTable() {
        FacesContext fc = FacesContext.getCurrentInstance();
        UIViewRoot root = fc.getViewRoot();
        UIComponent container = root.findComponent("form:dtcontainer");
        container.getChildren().add(creatTable(fc));
    }

    public HtmlDataTable creatTable(FacesContext fc) {
        HtmlDataTable table = new HtmlDataTable();
        ValueExpression ve = fc.getApplication().getExpressionFactory()
                .createValueExpression(fc.getELContext(), "#{dataTableDynamicBean.testStrings}", Object.class);
        table.setId("table");
        table.setValueExpression("value", ve);
        table.setVar("str");

        UINamingContainer nc = new UINamingContainer();
        nc.setId("nc");

        HtmlPanelGroup ncPanel = new HtmlPanelGroup();
        ncPanel.setId("ncpanel");

        HtmlOutputText text = new HtmlOutputText();
        text.setId("strv");
        ValueExpression textve = fc.getApplication().getExpressionFactory()
                .createValueExpression(fc.getELContext(), "#{str}", Object.class);
        text.setValueExpression("value", textve);
        ncPanel.getChildren().add(text);

        nc.getChildren().add(ncPanel);

        HtmlPanelGroup panel = new HtmlPanelGroup();
        panel.getChildren().add(nc);

        HtmlColumn column = new HtmlColumn();
        column.getChildren().add(panel);
        table.getChildren().add(column);
        return table;
    }

    public List<String> getTestStrings() {
        String vs[] = {"one", "two", "three", "four"};
        return Arrays.asList(vs);
    }
}
