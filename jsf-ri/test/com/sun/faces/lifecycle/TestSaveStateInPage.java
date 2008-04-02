/*
 * $Id: TestSaveStateInPage.java,v 1.28 2004/04/07 17:52:56 rkitain Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestSaveStateInPage.java

package com.sun.faces.lifecycle;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.application.StateManagerImpl;
import com.sun.faces.application.ViewHandlerImpl;
import com.sun.faces.util.TreeStructure;
import com.sun.faces.util.Util;
import org.apache.cactus.WebRequest;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;

import java.util.Map;


/**
 * <B>TestSaveStateInPage</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestSaveStateInPage.java,v 1.28 2004/04/07 17:52:56 rkitain Exp $
 */

public class TestSaveStateInPage extends JspFacesTestCase {

//
// Protected Constants
//

    public static final String TEST_URI = "/greeting.jsp";


    public String getExpectedOutputFilename() {
        return "SaveState_correct";
    }


    public static final String ignore[] = {
        "<form id=\"helloForm\" method=\"post\" action=\"/test/faces/greeting.jsp;jsessionid=09AF72F7E5EA209865AFFAB72D0F7B33\">"
    };


    public String[] getLinesToIgnore() {
        return ignore;
    }


    public boolean sendResponseToFile() {
        return true;
    }

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

    public TestSaveStateInPage() {
        super("TestRenderResponsePhase");
    }


    public TestSaveStateInPage(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//


    public void beginSaveStateInPage(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    }


    public void testSaveStateInPage() {

        boolean result = false;
        UIComponentBase root = null;
        String value = null;
        Phase renderResponse = new RenderResponsePhase();
        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        page.setId("root");
        page.setViewId(TEST_URI);
        getFacesContext().setViewRoot(page);

        renderResponse.execute(getFacesContext());
        assertTrue(!(getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        assertTrue(verifyExpectedOutput());
    }


    public void testSaveStateInClient() {
        // PENDING (visvan) add test case to make sure no state is saved when
        // root is marked transient.
        // precreate tree and set it in session and make sure the tree is
        // restored from session.
        getFacesContext().setViewRoot(null);
        UIViewRoot root = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        root.setViewId(TEST_URI);

        UIForm basicForm = new UIForm();
        basicForm.setId("basicForm");
        UIInput userName = new UIInput();

        userName.setId("userName");
        userName.setTransient(true);
        root.getChildren().add(basicForm);
        basicForm.getChildren().add(userName);

        UIPanel panel1 = new UIPanel();
        panel1.setId("panel1");
        basicForm.getChildren().add(panel1);

        UIInput userName1 = new UIInput();
        userName1.setId("userName1");
        userName1.setTransient(true);
        panel1.getChildren().add(userName1);

        UIInput userName2 = new UIInput();
        userName2.setId("userName2");
        panel1.getChildren().add(userName2);

        UIInput userName3 = new UIInput();
        userName3.setTransient(true);
        panel1.getFacets().put("userName3", userName3);

        UIInput userName4 = new UIInput();
        panel1.getFacets().put("userName4", userName4);

        getFacesContext().setViewRoot(root);

        ViewHandlerImpl viewHandler = new ViewHandlerImpl();
        StateManagerImpl stateManager = new StateManagerImpl();

        TreeStructure structRoot =
            new TreeStructure(((UIComponent) getFacesContext().getViewRoot()));
        stateManager.buildTreeStructureToSave(getFacesContext(),
                                              ((UIComponent) root),
                                              structRoot, null);

        // make sure restored tree structure is correct
        UIViewRoot viewRoot = (UIViewRoot) structRoot.createComponent();
        assertTrue(null != viewRoot);
        stateManager.restoreComponentTreeStructure(structRoot,
                                                   ((UIComponent) viewRoot));

        UIComponent component = (UIComponent) viewRoot.getChildren().get(0);
        assertTrue(component instanceof UIForm);
        assertTrue(component.getId().equals("basicForm"));

        UIForm uiform = (UIForm) component;
        component = (UIComponent) uiform.getChildren().get(0);
        assertTrue(component instanceof UIPanel);
        assertTrue(component.getId().equals("panel1"));

        UIPanel uipanel = (UIPanel) component;
        component = (UIComponent) uipanel.getChildren().get(0);
        assertTrue(component instanceof UIInput);
        assertTrue(component.getId().equals("userName2"));

        // make sure that the transient property is not persisted as well as the
        // namespace is preserved.
        basicForm = (UIForm) viewRoot.findComponent("basicForm");
        assertTrue(basicForm != null);

        userName = (UIInput) basicForm.findComponent("userName");
        assertTrue(userName == null);

        panel1 = (UIPanel) basicForm.findComponent("panel1");
        assertTrue(panel1 != null);

        userName1 = (UIInput) panel1.findComponent("userName1");
        assertTrue(userName1 == null);

        userName2 = (UIInput) panel1.findComponent("userName2");
        assertTrue(userName2 != null);

        // make sure facets work correctly when marked transient.
        Map facetList = panel1.getFacets();
        assertTrue(!(facetList.containsKey("userName3")));
        assertTrue(facetList.containsKey("userName4"));
    }


} // end of class TestRenderResponsePhase
