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

// TestTreeVisit.java

package com.sun.faces.component.visit;

import com.sun.faces.application.view.FaceletPartialStateManagementStrategy;
import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.util.Util;

import java.util.*;
import java.util.logging.Logger;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ScalarDataModel;
import javax.faces.view.StateManagementStrategy;


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

        String result = builder.toString().trim();
        assertEquals(result, "form:panel0:data:3:output0 form:panel1:data:0:output0");

    }


    /**
     * Added for issue https://javaserverfaces.dev.java.net/issues/show_bug.cgi?id=1483
     */
    public void testFacetVisits() throws Exception {

        UIData data = new UIData();
        DataModel m = new ArrayDataModel<String>(new String[] {"a", "b"});
        data.setValue(m);
        data.setId("table");
        UIOutput tableFacet = new UIOutput();
        tableFacet.setId("tableFacet");
        data.getFacets().put("header", tableFacet);
        UIColumn c1 = new UIColumn();
        c1.setId("column1");
        UIOutput column1Facet = new UIOutput();
        column1Facet.setId("column1Facet");
        c1.getFacets().put("header", column1Facet);
        UIOutput column1Data = new UIOutput();
        column1Data.setId("column1Data");
        c1.getChildren().add(column1Data);
        data.getChildren().add(c1);

        getFacesContext().setCurrentPhaseId(PhaseId.RENDER_RESPONSE);
        
        final List<String> visitedIds = new ArrayList<String>();
        data.visitTree(VisitContext.createVisitContext(getFacesContext(),
                                                       null,
                                                       null),
                       new VisitCallback() {
                           public VisitResult visit(VisitContext context,
                                                    UIComponent target) {
                               visitedIds
                                     .add(target.getClientId(context.getFacesContext()));
                               return VisitResult.ACCEPT;
                           }
                       });
        
        String[] expectedIds = { "table",
                                 "table:tableFacet",
                                 "table:column1Facet",
                                 "table:column1",
                                 "table:0:column1Data",
                                 "table:1:column1Data" };

        Logger.getAnonymousLogger().info("** Visited IDs: " + visitedIds);

        assertEquals("Expected number of vists: " + expectedIds.length + ", actual number of visits: " + visitedIds.size(),
                     expectedIds.length,
                     visitedIds.size());

        for (String id : expectedIds) {
            assertTrue("ID: " + id + " not visited.", visitedIds.contains(id));
        }

    }

    // Tests UIData visiting with VisitHint.SKIP_ITERATION set.
    // Each child of UIData should be visited once.    
    public void testUIDataSkipIterationVisit() throws Exception {

        UIData data = new UIData();
        DataModel m = new ArrayDataModel<String>(new String[] {"a", "b"});
        data.setValue(m);
        data.setId("table");
        UIOutput tableFacet = new UIOutput();
        tableFacet.setId("tableFacet");
        data.getFacets().put("header", tableFacet);
        UIColumn c1 = new UIColumn();
        c1.setId("column1");
        UIOutput column1Facet = new UIOutput();
        column1Facet.setId("column1Facet");
        c1.getFacets().put("header", column1Facet);
        UIOutput column1Data = new UIOutput();
        column1Data.setId("column1Data");
        c1.getChildren().add(column1Data);
        data.getChildren().add(c1);

        final List<String> visitedIds = new ArrayList<String>();
        Set<VisitHint> hints = EnumSet.of(VisitHint.SKIP_ITERATION);
        data.visitTree(VisitContext.createVisitContext(getFacesContext(),
                                                       null,
                                                       hints),
                       new VisitCallback() {
                           public VisitResult visit(VisitContext context,
                                                    UIComponent target) {
                               visitedIds
                                     .add(target.getClientId(context.getFacesContext()));
                               return VisitResult.ACCEPT;
                           }
                       });

        String[] expectedIds = { "table",
                                 "table:tableFacet",
                                 "table:column1",
                                 "table:column1Facet",
                                 "table:column1Data" };


        Logger.getAnonymousLogger().info("VISITED IDS:"+visitedIds);

        assertEquals("Expected number of vists: " + expectedIds.length + ", actual number of visits: " + visitedIds.size(),
                     expectedIds.length,
                     visitedIds.size());

        for (String id : expectedIds) {
            assertTrue("ID: " + id + " not visited.", visitedIds.contains(id));
        }
    }

    public void testUIDataSkipIterationSaveRestoreStateVisit() throws Exception {
        FacesContext context = getFacesContext();
        List<String> visitedIds = new ArrayList<String>();
        UIViewRoot root = Util.getViewHandler(context).createView(context, null);
        root.setViewId("/root.xhtml");
        context.setViewRoot(root);

        HtmlForm form = new HtmlForm();
        form.setId("form");
        root.getChildren().add(form);

        UIDataState data = new UIDataState(visitedIds);
        DataModel m = new ArrayDataModel<String>(new String[] {"a", "b"});
        data.setValue(m);
        data.setId("table");
        UIOutputState tableFacet = new UIOutputState(visitedIds);
        tableFacet.setId("tableFacet");
        data.getFacets().put("header", tableFacet);
        UIColumnState c1 = new UIColumnState(visitedIds);
        c1.setId("column1");
        UIOutputState column1Facet = new UIOutputState(visitedIds);
        column1Facet.setId("column1Facet");
        c1.getFacets().put("header", column1Facet);
        UIOutputState column1Data = new UIOutputState(visitedIds);
        column1Data.setId("column1Data");
        c1.getChildren().add(column1Data);
        data.getChildren().add(c1);
        form.getChildren().add(data);
        FaceletPartialStateManagementStrategy strategy = new FaceletPartialStateManagementStrategy();

        String[] expectedIds = { "SAVE:form:table",
                                 "SAVE:form:table:tableFacet",
                                 "SAVE:form:table:column1",
                                 "SAVE:form:table:column1Facet",
                                 "SAVE:form:table:column1Data" };

        Object[] state = (Object[])strategy.saveView(context);

        Logger.getAnonymousLogger().info("VISITED IDS:"+visitedIds);

        assertEquals("Expected number of vists: " + expectedIds.length + ", actual number of visits: " + visitedIds.size(),
                     expectedIds.length,
                     visitedIds.size());

        for (String id : expectedIds) {
            assertTrue("ID: " + id + " not visited.", visitedIds.contains(id));
        }

        expectedIds = new String[]{"RESTORE:form:table",
                "RESTORE:form:table:tableFacet",
                "RESTORE:form:table:column1",
                "RESTORE:form:table:column1Facet",
                "RESTORE:form:table:column1Data"};

        visitedIds = new ArrayList<String>();
        data.setVisitedIds(visitedIds);
        tableFacet.setVisitedIds(visitedIds);
        c1.setVisitedIds(visitedIds);
        column1Facet.setVisitedIds(visitedIds);
        column1Data.setVisitedIds(visitedIds);

        final Map<String, Object> stateMap = (Map<String,Object>) state[1];

        Set<VisitHint> hints = EnumSet.of(VisitHint.SKIP_ITERATION);
        root.visitTree(VisitContext.createVisitContext(context, null,hints),
            new VisitCallback() {
                public VisitResult visit(VisitContext context, UIComponent target) {
                    String cid = target.getClientId(context.getFacesContext());
                    Object stateObj = stateMap.get(cid);
                    target.restoreState(context.getFacesContext(), stateObj);
                    return VisitResult.ACCEPT;
                }
        });



        Logger.getAnonymousLogger().info("VISITED IDS:"+visitedIds);

        assertEquals("Expected number of vists: " + expectedIds.length + ", actual number of visits: " + visitedIds.size(),
                     expectedIds.length,
                     visitedIds.size());

        for (String id : expectedIds) {
            assertTrue("ID: " + id + " not visited.", visitedIds.contains(id));
        }

    }

    public void testUIDataSkipIterationPostRestoreEventVisit() throws Exception {
        FacesContext context = getFacesContext();
        List<String> visitedIds = new ArrayList<String>();
        UIViewRoot root = Util.getViewHandler(context).createView(context, null);
        root.setViewId("/root.xhtml");
        context.setViewRoot(root);

        HtmlForm form = new HtmlForm();
        form.setId("form");
        root.getChildren().add(form);

        UIDataState data = new UIDataState(visitedIds);
        DataModel m = new ArrayDataModel<String>(new String[] {"a", "b"});
        data.setValue(m);
        data.setId("table");
        UIOutputState tableFacet = new UIOutputState(visitedIds);
        tableFacet.setId("tableFacet");
        data.getFacets().put("header", tableFacet);
        UIColumnState c1 = new UIColumnState(visitedIds);
        c1.setId("column1");
        UIOutputState column1Facet = new UIOutputState(visitedIds);
        column1Facet.setId("column1Facet");
        c1.getFacets().put("header", column1Facet);
        UIOutputState column1Data = new UIOutputState(visitedIds);
        column1Data.setId("column1Data");
        c1.getChildren().add(column1Data);
        data.getChildren().add(c1);
        form.getChildren().add(data);

        final PostRestoreStateEvent postRestoreStateEvent = new PostRestoreStateEvent(root);

        String[] expectedIds = {"EVENT:form:table",
                "EVENT:form:table:tableFacet",
                "EVENT:form:table:column1",
                "EVENT:form:table:column1Facet",
                "EVENT:form:table:column1Data"};


        Set<VisitHint> hints = EnumSet.of(VisitHint.SKIP_ITERATION);
        VisitContext visitContext = VisitContext.createVisitContext(context, null, hints);
        root.visitTree(visitContext, new VisitCallback() {
            public VisitResult visit(VisitContext context, UIComponent target) {
                postRestoreStateEvent.setComponent(target);
                target.processEvent(postRestoreStateEvent);
                return VisitResult.ACCEPT;
            }
        });

        Logger.getAnonymousLogger().info("VISITED IDS:"+visitedIds);

        assertEquals("Expected number of vists: " + expectedIds.length + ", actual number of visits: " + visitedIds.size(),
                     expectedIds.length,
                     visitedIds.size());

        for (String id : expectedIds) {
            assertTrue("ID: " + id + " not visited.", visitedIds.contains(id));
        }
        
    }

    private class UIDataState extends UIData
        implements ComponentSystemEventListener {
        private List visitedIds = null;
        public UIDataState(List visitedIds) {
            this.visitedIds = visitedIds;
        }
        public Object saveState(FacesContext context) {
            visitedIds.add("SAVE:"+getClientId(context));
            return super.saveState(context);
        }
        public void restoreState(FacesContext context, Object state) {
            visitedIds.add("RESTORE:"+getClientId(context));
            super.restoreState(context, state);
        }
        public void setVisitedIds(List<String>visitedIds) {
            this.visitedIds = visitedIds;
        }
        public List<String> getVisitedIds() {
            return visitedIds;
        }
        public void processEvent( ComponentSystemEvent event )
            throws AbortProcessingException {
            FacesContext context = FacesContext.getCurrentInstance();
            visitedIds.add("EVENT:"+getClientId(context));
        }
        public boolean isListenerForSource( Object source ) {
            return ( source instanceof UIViewRoot );
	    }

    }

    private class UIOutputState extends UIOutput
        implements ComponentSystemEventListener {
        private List<String> visitedIds = null;
        public UIOutputState(List visitedIds) {
            this.visitedIds = visitedIds;
        }
        public Object saveState(FacesContext context) {
            visitedIds.add("SAVE:"+getClientId(context));
            return super.saveState(context);
        }
        public void restoreState(FacesContext context, Object state) {
            visitedIds.add("RESTORE:"+getClientId(context));
            super.restoreState(context, state);
        }
        public void setVisitedIds(List<String>visitedIds) {
            this.visitedIds = visitedIds;
        }
        public List<String> getVisitedIds() {
            return visitedIds;
        }
        public void processEvent( ComponentSystemEvent event )
            throws AbortProcessingException {
            FacesContext context = FacesContext.getCurrentInstance();
            visitedIds.add("EVENT:"+getClientId(context));
        }
        public boolean isListenerForSource( Object source ) {
            return ( source instanceof UIViewRoot );
	    }

    }

    private class UIColumnState extends UIColumn
        implements ComponentSystemEventListener {
        private List visitedIds = null;
        private ComponentSystemEvent event;

        public UIColumnState(List visitedIds) {
            this.visitedIds = visitedIds;
        }
        public Object saveState(FacesContext context) {
            visitedIds.add("SAVE:"+getClientId(context));
            return super.saveState(context);
        }
        public void restoreState(FacesContext context, Object state) {
            visitedIds.add("RESTORE:"+getClientId(context));
            super.restoreState(context, state);
        }
        public void setVisitedIds(List<String>visitedIds) {
            this.visitedIds = visitedIds;
        }
        public List<String> getVisitedIds() {
            return visitedIds;
        }
        @Override
        public void processEvent( ComponentSystemEvent event )
            throws AbortProcessingException {
            FacesContext context = FacesContext.getCurrentInstance();
            visitedIds.add("EVENT:"+getClientId(context));
        }
        public boolean isListenerForSource( Object source ) {
            return ( source instanceof UIViewRoot );
	    }

    }


// PENDING make sure UIData and UIRepeat are tested.

} // end of class TestTreeVisit
