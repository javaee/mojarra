/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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


// TestTreeVisit.java

package com.sun.faces.component.visit;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.Util;
import java.util.ArrayList;
import java.util.HashSet;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.model.ListDataModel;


public class TestTreeWithUIDataVisit extends ServletFacesTestCase {

//
// Protected Constants
//

// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestTreeWithUIDataVisit() {
        super("TestTreeVisit.java");
    }


    public TestTreeWithUIDataVisit(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//
    private void buildTree() {
	FacesContext context = getFacesContext();
        UIViewRoot root = Util.getViewHandler(context).createView(context, null);
        root.setId("root");
        context.setViewRoot(root);

        HtmlForm form = new HtmlForm();
        form.setId("form");
        root.getChildren().add(form);

        buildPanel(form, "panel0");
        buildPanel(form, "panel1");

    }

    private void buildPanel(HtmlForm form, String panelId) {
        UIComponent
                panel,
                input0,
                input1,
                button0,
                button1,
                column0;
        HtmlDataTable data;
        HtmlOutputText output0;
        ArrayList<String> hobbits = new ArrayList<String>();
        hobbits.add("bilbo");
        hobbits.add("frodo");
        hobbits.add("merry");
        hobbits.add("pippin");
        hobbits.add("lumpy");
        ListDataModel dataModel = new ListDataModel(hobbits);

        panel = new UINamingContainer();
        panel.setId(panelId);
        form.getChildren().add(panel);

        input0 = new HtmlInputText();
        input0.setId("input0");
        panel.getChildren().add(input0);

        input1 = new HtmlInputText();
        input1.setId("input1");
        panel.getChildren().add(input1);

        data = new HtmlDataTable();
        data.setId("data");
        panel.getChildren().add(data);
        data.setValue(dataModel);
        data.setVar("hobbitName");
        String dataId = data.getClientId();

        column0 = new HtmlColumn();
        column0.setId("column0");
        data.getChildren().add(column0);

        output0 = new HtmlOutputText();
        output0.setId("output0");
        output0.setValue(getFacesContext().getApplication().getExpressionFactory().createValueExpression(getFacesContext().getELContext(), "#{hobbitName}", String.class));
        column0.getChildren().add(output0);

        button0 = new HtmlCommandButton();
        button0.setId("button0");
        panel.getChildren().add(button0);

        button1 = new HtmlCommandButton();
        button1.setId("button1");
        panel.getChildren().add(button1);

    }

    public void testSpecificIdTraversal() throws Exception {
        buildTree();
        UIViewRoot root = getFacesContext().getViewRoot();
        final StringBuilder builder = new StringBuilder();

        HashSet ids = new HashSet();
        ids.add("form:panel0:data:3:output0");
        ids.add("form:panel1:data:0:output0");

        // At the point of this visit call the current Phase is RESTORE_VIEW.
        // This will cause the test to fail due to the changes for issue 1310.
        // So we need to switch to a different phase
        getFacesContext().setCurrentPhaseId(PhaseId.RENDER_RESPONSE);
        
        root.visitTree(VisitContext.createVisitContext(getFacesContext(),
                ids, null),
                new VisitCallback() {
                    public VisitResult visit(VisitContext context,
                            UIComponent target) {
                        builder.append(target.getClientId(context.getFacesContext()) + " ");
                        return VisitResult.ACCEPT;
                    }
                });
        System.out.println(builder);
        String result = builder.toString().trim();
        assertEquals(result, "form:panel0:data:3:output0 form:panel1:data:0:output0");

    }


    // PENDING make sure UIData and UIRepeat are tested.

} // end of class TestTreeVisit
