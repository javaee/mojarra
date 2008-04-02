/*
 * $Id: TestRestoreViewPhase.java,v 1.24 2005/07/21 13:46:30 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.lifecycle;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.util.Util;

import org.apache.cactus.WebRequest;

import javax.faces.application.ViewExpiredException;
import javax.faces.component.UICommand;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;

import javax.servlet.http.HttpSession;

import java.util.Locale;


/**
 * <B>TestReconstituteComponentTreePhase</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRestoreViewPhase.java,v 1.24 2005/07/21 13:46:30 rogerk Exp $
 */

public class TestRestoreViewPhase extends ServletFacesTestCase {

//
// Protected Constants
//

    public static final String TEST_URI = "/components.jsp";

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

    public TestRestoreViewPhase() {
        super("TestRestoreViewPhase");
    }


    public TestRestoreViewPhase(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//

    public void beginReconstituteRequestSubmit(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
	theRequest.addParameter("javax.faces.ViewState",
				"_id1:_id2");
    }


    public void beginRegisterListeners(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
	theRequest.addParameter("javax.faces.ViewState",
				"_id1:_id2");
    }

    public void testReconstituteRequestSubmit() {

        // precreate tree and set it in session and make sure the tree is
        // restored from session.
	
	FacesContext context = getFacesContext();
        UIViewRoot root = Util.getViewHandler(context).createView(context, 
								  null);
        root.setViewId(TEST_URI);
	context.setViewRoot(root);
	

        UIForm basicForm = new UIForm();
        basicForm.setId("basicForm");
        UIInput userName = new UIInput();

        userName.setId("userName");
        root.getChildren().add(basicForm);
        basicForm.getChildren().add(userName);

        Locale locale = new Locale("France", "french");
        root.setLocale(locale);

	// here we do what the StateManager does to save the state in
	// the server.
	Util.getStateManager(context).saveSerializedView(context);
	context.setViewRoot(null);

        Phase restoreView = new RestoreViewPhase();

        try {
            restoreView.execute(getFacesContext());
        } catch (Throwable e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue(!(getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        assertTrue(null != getFacesContext().getViewRoot());
        assertTrue(RenderKitFactory.HTML_BASIC_RENDER_KIT ==
                   getFacesContext().getViewRoot().getRenderKitId());

        assertTrue(locale == getFacesContext().getViewRoot().getLocale());

        assertTrue(
            getFacesContext().getViewRoot().getViewId().equals(TEST_URI));
        root = getFacesContext().getViewRoot();
        // components should exist.
        assertTrue(root.getChildCount() == 1);
        assertTrue(basicForm.getId().equals(root.findComponent("basicForm").getId()));
	assertTrue(userName.getId().equals(basicForm.findComponent("userName").getId()));
        getFacesContext().setViewRoot(null);
    }

    /**
     * This method will test the <code>registerActionListeners</code> method.
     * It will first create a simple tree consisting of a couple of <code>UICommand</code>
     * components added to a facet;  Then the <code>ReconstituteComponentTree.execute</code>
     * method is run;  And finally, an assertion is done to ensure that default action
     * listeners have been registered on the <code>UICommand</code> components;
     */
    public void testRegisterListeners() {

        // precreate tree and set it in session and make sure the tree is
        // restored from session.
	FacesContext context = getFacesContext();

        UIViewRoot root = Util.getViewHandler(context).createView(context, 
								  null);
        root.setViewId(TEST_URI);
	context.setViewRoot(root);

        UIForm basicForm = new UIForm();
        basicForm.setId("basicForm");
        root.getChildren().add(basicForm);
        UIPanel panel = new UIPanel();
        basicForm.getChildren().add(panel);
        UIPanel commandPanel = new UIPanel();
        commandPanel.setId("commandPanel");
        UICommand command1 = new UICommand();
        UICommand command2 = new UICommand();
        commandPanel.getChildren().add(command1);
        commandPanel.getChildren().add(command2);
        panel.getFacets().put("commandPanel", commandPanel);

	// here we do what the StateManager does to save the state in
	// the server.
	Util.getStateManager(context).saveSerializedView(context);
	context.setViewRoot(null);

        Phase restoreView = new RestoreViewPhase();

        try {
            restoreView.execute(context);
        } catch (Throwable e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue(!(context.getRenderResponse()) &&
                   !(context.getResponseComplete()));
        assertTrue(context.getViewRoot() != null);

        // Now test with no facets... Listeners should still be registered on UICommand
        // components....
        //
        context.setViewRoot(null);

        root = Util.getViewHandler(context).createView(context, null);
        root.setViewId(TEST_URI);
	context.setViewRoot(root);

        basicForm = new UIForm();
        basicForm.setId("basicForm");
        root.getChildren().add(basicForm);
        command1 = new UICommand();
        command2 = new UICommand();
        basicForm.getChildren().add(command1);
        basicForm.getChildren().add(command2);

	// here we do what the StateManager does to save the state in
	// the server.
	com.sun.faces.application.TestStateManagerImpl.resetStateManagerRequestIdSerialNumber(context);
	Util.getStateManager(context).saveSerializedView(context);
	context.setViewRoot(null);

        restoreView = new RestoreViewPhase();

        try {
            restoreView.execute(context);
        } catch (Throwable e) {
            assertTrue(false);
        }
        assertTrue(!(context.getRenderResponse()) &&
                   !(context.getResponseComplete()));

        context.setViewRoot(null);
    }

    public void beginRestoreViewExpired(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
        theRequest.addParameter("javax.faces.ViewState",
                                "_id1:_id2");
    }

    public void testRestoreViewExpired() {
        // precreate tree and set it in session and make sure the tree is
        // restored from session.
                                                                                                                        
        FacesContext context = getFacesContext();
        UIViewRoot root = Util.getViewHandler(context).createView(context,
                                                                  null);
        root.setViewId(TEST_URI);
        context.setViewRoot(root);
                                                                                                                        
                                                                                                                        
        UIForm basicForm = new UIForm();
        basicForm.setId("basicForm");
        UIInput userName = new UIInput();
                                                                                                                        
        userName.setId("userName");
        root.getChildren().add(basicForm);
        basicForm.getChildren().add(userName);
                                                                                                                        
        Locale locale = new Locale("France", "french");
        root.setLocale(locale);
                                                                                                                        
        // here we do what the StateManager does to save the state in
        // the server.
        Util.getStateManager(context).saveSerializedView(context);
        context.setViewRoot(null);
                                                                                                                        
        // invalidate the session before we attempt to restore
        ((HttpSession)context.getExternalContext().getSession(true)).invalidate();
                                                                                                                        
        Phase restoreView = new RestoreViewPhase();
                                                                                                                        
        boolean exceptionThrown = false;
        try {
            restoreView.execute(context);
        } catch (ViewExpiredException e) {
            exceptionThrown = true;
            assertTrue(e.getViewId().equals(TEST_URI));
            String expected = "viewId:"+e.getViewId()+" - View "+e.getViewId()+" could not be restored.";
            assertTrue(e.getMessage().equals(expected));
        }
        assertTrue(exceptionThrown);
    }



} // end of class TestRestoreViewPhase

