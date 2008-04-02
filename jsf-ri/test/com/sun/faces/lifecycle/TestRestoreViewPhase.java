/*
 * $Id: TestRestoreViewPhase.java,v 1.14 2004/01/27 21:06:05 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.lifecycle;

import com.sun.faces.RIConstants;
import com.sun.faces.lifecycle.Phase;
import com.sun.faces.ServletFacesTestCase;

import java.util.List;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.event.PhaseId;

import javax.servlet.http.HttpSession;
import javax.faces.render.RenderKitFactory;

import org.apache.cactus.WebRequest;

import com.sun.faces.util.Util;


/**
 *
 *  <B>TestReconstituteComponentTreePhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRestoreViewPhase.java,v 1.14 2004/01/27 21:06:05 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRestoreViewPhase extends ServletFacesTestCase
{
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

public void beginReconstituteRequestInitial(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
}

public void beginReconstituteRequestSubmit(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
}  

public void beginRegisterListeners(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
} 

public void testReconstituteRequestInitial()
{
    Phase restoreView = new RestoreViewPhase();

    try {
        restoreView.execute(getFacesContext());
    }
    catch (Throwable e) {
        e.printStackTrace();
	assertTrue(false);
    }
    assertTrue((getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    assertTrue(null != getFacesContext().getViewRoot());
    assertTrue(RenderKitFactory.HTML_BASIC_RENDER_KIT == 
            getFacesContext().getViewRoot().getRenderKitId());

    assertTrue(null != getFacesContext().getViewRoot().getLocale());
   
    UIViewRoot root = null;

    assertTrue(getFacesContext().getViewRoot().getViewId().equals(TEST_URI));
    root = getFacesContext().getViewRoot();
    assertTrue((root.getChildren().isEmpty()) == true);
    getFacesContext().setViewRoot(null);
}

public void testReconstituteRequestSubmit()
{
    
    // precreate tree and set it in session and make sure the tree is
    // restored from session.
   
    UIViewRoot root = new UIViewRoot();
    root.setViewId(TEST_URI);
    
    UIForm basicForm = new UIForm();
    basicForm.setId("basicForm");
    UIInput userName = new UIInput();
    
    userName.setId("userName");
    root.getChildren().add(basicForm);
    basicForm.getChildren().add(userName);
    
    HttpSession session = (HttpSession) 
        getFacesContext().getExternalContext().getSession(false);
    session.setAttribute(TEST_URI, root);
    // set a locale
    Locale locale = new Locale("France", "french");
    root.setLocale(locale);
    
    Phase restoreView = new RestoreViewPhase();

    try {
	restoreView.execute(getFacesContext());
    }
    catch (Throwable e) {
        e.printStackTrace();
	assertTrue(false);
    }
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    assertTrue(null != getFacesContext().getViewRoot());
    assertTrue(RenderKitFactory.HTML_BASIC_RENDER_KIT == 
            getFacesContext().getViewRoot().getRenderKitId());

    assertTrue(locale == getFacesContext().getViewRoot().getLocale());
    
    assertTrue(getFacesContext().getViewRoot().getViewId().equals(TEST_URI));
    root = getFacesContext().getViewRoot();
    // components should exist.
    assertTrue(root.getChildCount() == 1);
    assertTrue(basicForm == root.findComponent("basicForm")); 
    assertTrue(userName == basicForm.findComponent("userName"));
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
   
    UIViewRoot root = new UIViewRoot();
    root.setViewId(TEST_URI);
    
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

    HttpSession session = (HttpSession) 
        getFacesContext().getExternalContext().getSession(false);
    session.setAttribute(TEST_URI, root);

    Phase restoreView = new RestoreViewPhase();

    try {
	restoreView.execute(getFacesContext());
    }
    catch (Throwable e) {
        e.printStackTrace();
	assertTrue(false);
    }
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));
    assertTrue(getFacesContext().getViewRoot() != null);

    // Now test with no facets... Listeners should still be registered on UICommand
    // components....
    // 
    getFacesContext().setViewRoot(null);
    
    root = new UIViewRoot();
    root.setViewId(TEST_URI);
    
    basicForm = new UIForm();
    basicForm.setId("basicForm");
    root.getChildren().add(basicForm);
    command1 = new UICommand();
    command2 = new UICommand();
    basicForm.getChildren().add(command1);
    basicForm.getChildren().add(command2);
    
    session = (HttpSession) 
        getFacesContext().getExternalContext().getSession(false);
    session.setAttribute(TEST_URI, root);

    restoreView = new RestoreViewPhase();

    try {
	restoreView.execute(getFacesContext());
    }
    catch (Throwable e) {
	assertTrue(false);
    }   
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    getFacesContext().setViewRoot(null);
}


} // end of class TestRestoreViewPhase

