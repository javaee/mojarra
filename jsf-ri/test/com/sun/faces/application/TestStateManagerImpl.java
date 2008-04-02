/*
 * $Id: TestStateManagerImpl.java,v 1.10 2004/04/07 17:52:44 rkitain Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.application;

import com.sun.faces.RIConstants;
import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.util.Util;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;


/**
 * This class tests the <code>StateManagerImpl</code> class
 * functionality.
 */
public class TestStateManagerImpl extends ServletFacesTestCase {


    //
    // Constructors/Initializers
    //
    public TestStateManagerImpl() {
        super("TestStateManagerImpl");
    }


    public TestStateManagerImpl(String name) {
        super(name);
    }
    
    //
    // Test Methods
    //
    
    // Verify saveSerializedView() throws IllegalStateException
    // if duplicate component id's are detected on non-transient 
    // components.
    public void testDuplicateIdDetectionServer() throws Exception {

        FacesContext context = getFacesContext();
        UIViewRoot root = null;

        UIComponent comp1 = null;

        UIComponent comp2 = null;

        UIComponent comp3 = null;

        UIComponent facet1 = null;

        UIComponent facet2 = null;
        
        // construct a view
        root = Util.getViewHandler(getFacesContext()).createView(context, null); 
        root.setViewId("/test");
        root.setId("root");

        comp1 = new UIInput();
        comp1.setId("comp1");

        comp2 = new UIOutput();
        comp2.setId("comp2");

        comp3 = new UIGraphic();
        comp3.setId("comp3");

        facet1 = new UIOutput();
        facet1.setId("comp4");

        facet2 = new UIOutput();
        facet2.setId("comp2");

        comp2.getFacets().put("facet1", facet1);
        comp2.getFacets().put("facet2", facet2);

        root.getChildren().add(comp1);
        root.getChildren().add(comp2);
        root.getChildren().add(comp3);

        HttpSession session =
            (HttpSession) context.getExternalContext().getSession(false);
        session.setAttribute("/test", root);


        context.setViewRoot(root);

        StateManagerImpl stateManager = (StateManagerImpl) context.getApplication()
            .getStateManager();

        boolean exceptionThrown = false;
        try {
            stateManager.saveSerializedView(context);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        
        // multiple componentns with a null ID should not
        // trigger an exception
        // construct a view
        root = Util.getViewHandler(getFacesContext()).createView(context, null); 
        root.setViewId("/test");
        root.setId("root");

        comp1 = new UIInput();
        comp1.setId("comp1");

        comp2 = new UIOutput();
        comp2.setId(null);

        comp3 = new UIGraphic();
        comp3.setId(null);

        facet1 = new UIOutput();
        facet1.setId("comp4");

        facet2 = new UIOutput();
        facet2.setId("comp2");

        comp2.getFacets().put("facet1", facet1);
        comp2.getFacets().put("facet2", facet2);

        root.getChildren().add(comp1);
        root.getChildren().add(comp2);
        root.getChildren().add(comp3);

        session.setAttribute("/test", root);

        context.setViewRoot(root);

        exceptionThrown = false;
        try {
            stateManager.saveSerializedView(context);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        assertTrue(!exceptionThrown);
        
        // transient components with duplicate ids should 
        // trigger an error condition
        // construct a view
        root = Util.getViewHandler(getFacesContext()).createView(context, null); 
        root.setViewId("/test");
        root.setId("root");

        comp1 = new UIInput();
        comp1.setId("comp1");
        comp1.setTransient(true);

        comp2 = new UIOutput();
        comp2.setId("comp1");
        comp2.setTransient(true);

        comp3 = new UIGraphic();
        comp3.setId("comp3");

        facet1 = new UIOutput();
        facet1.setId("comp4");

        facet2 = new UIOutput();
        facet2.setId("comp2");

        comp2.getFacets().put("facet1", facet1);
        comp2.getFacets().put("facet2", facet2);

        root.getChildren().add(comp1);
        root.getChildren().add(comp2);
        root.getChildren().add(comp3);

        session.setAttribute("/test", root);

        context.setViewRoot(root);

        exceptionThrown = false;
        try {
            stateManager.saveSerializedView(context);
        } catch (IllegalStateException ise) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }


    public void testDuplicateIdDetectionClient() throws Exception {
        StateManagerImpl wrapper =
            new StateManagerImpl() {
                public boolean isSavingStateInClient(FacesContext context) {
                    return true;
                }
            };
        getFacesContext().getApplication().setStateManager(wrapper);
        testDuplicateIdDetectionServer();
    }


    public void testRemoveViewFromSession() {
        ArrayList viewList = new ArrayList(10);
        FacesContext context = getFacesContext();
        UIViewRoot newViewRoot = Util.getViewHandler(getFacesContext()).createView(context, null);
        newViewRoot.setViewId("viewId");
        context.setViewRoot(newViewRoot);

        HttpSession session =
            (HttpSession) context.getExternalContext().getSession(false);
        for (int i = 0; i < 21; ++i) {
            String viewId = "viewId" + i;
            viewList.add(viewId);
            UIViewRoot viewRoot = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
            viewRoot.setViewId(viewId);
            session.setAttribute(viewId, viewRoot);
        }
        session.setAttribute("com.sun.faces.VIEW_LIST", viewList);

        StateManagerImpl stateManager = new StateManagerImpl();
        stateManager.restoreView(context, "viewId2",
                                 RenderKitFactory.HTML_BASIC_RENDER_KIT);

        viewList = (ArrayList) session.getAttribute(RIConstants.FACES_PREFIX
                                                    + "VIEW_LIST");
        assertTrue(viewList.size() == 20);
        assertTrue(!(viewList.contains("viewId0")));
        assertTrue((session.getAttribute("viewId0")) == null);
    }

}
