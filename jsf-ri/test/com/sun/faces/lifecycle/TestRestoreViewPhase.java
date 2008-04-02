/*
 * $Id: TestRestoreViewPhase.java,v 1.2 2003/08/22 17:27:40 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRestoreViewPhase.java

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
import javax.faces.component.base.UINamingContainerBase;
import javax.faces.component.base.UIFormBase;
import javax.faces.component.base.UIInputBase;
import javax.faces.component.base.UIPanelBase;
import javax.faces.component.base.UIViewRootBase;
import javax.faces.component.base.UICommandBase;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpSession;
import javax.faces.render.RenderKitFactory;

import org.apache.cactus.WebRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>TestRestoreViewPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRestoreViewPhase.java,v 1.2 2003/08/22 17:27:40 rlubke Exp $
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
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    assertTrue(null != getFacesContext().getViewRoot());
    assertTrue(RenderKitFactory.DEFAULT_RENDER_KIT == 
            getFacesContext().getViewRoot().getRenderKitId());

    assertTrue(null != getFacesContext().getViewRoot());
    assertTrue(null != getFacesContext().getLocale());
    
    UIComponent root = null;

    assertTrue(getFacesContext().getViewRoot().getViewId().equals(TEST_URI));
    root = getFacesContext().getViewRoot();
    assertTrue(root.getChildren().size() == 0);
}

public void testReconstituteRequestSubmit()
{    
    // precreate tree and set it in session and make sure the tree is
    // restored from session.      
    UIViewRoot root = new UIViewRootBase();
    root.setViewId(TEST_URI);
    
    UIForm basicForm = new UIFormBase();
    basicForm.setId("basicForm");
    UIInput userName = new UIInputBase();
    
    userName.setId("userName");
    root.getChildren().add(basicForm);
    basicForm.getChildren().add(userName);
        
    HttpSession session = (HttpSession) 
        getFacesContext().getExternalContext().getSession(false);
    
    // PENDING (rlubke) Is FACES_VIEW still valid
    session.setAttribute(RIConstants.FACES_VIEW, root);
    // set a locale
    Locale locale = new Locale("France", "french");
    session.setAttribute(RIConstants.REQUEST_LOCALE, locale);
    
    Phase restoreView = new RestoreViewPhase();

    try {
	restoreView.execute(getFacesContext());
    }
    catch (Throwable e) {
	assertTrue(false);
    }
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    assertTrue(null != getFacesContext().getViewRoot());
    assertTrue(RenderKitFactory.DEFAULT_RENDER_KIT == 
            getFacesContext().getViewRoot().getRenderKitId());

    assertTrue(null != getFacesContext().getViewRoot());
    assertTrue(locale == getFacesContext().getLocale());
    
    assertTrue(getFacesContext().getViewRoot().getViewId().equals(TEST_URI));
    root = getFacesContext().getViewRoot();
    // components should exist.
    assertTrue(root.getChildren().size() == 1);
    assertTrue(userName == root.findComponent("userName"));
    assertTrue(basicForm == root.findComponent("basicForm")); 
}

/**
 * This method will test the <code>registerActionListeners</code> method.
 * It will first create a simple view consisting of a couple of <code>UICommand</code>
 * components added to a facet;  Then the <code>RestoreViewPhase.execute</code>
 * method is run;  And finally, an assertion is done to ensure that default action
 * listeners have been registered on the <code>UICommand</code> components;
 */
public void testRegisterListeners() { 
    
    // precreate tree and set it in session and make sure the tree is
    // restored from session.
   
    UIViewRoot root = new UIViewRootBase();
    root.setViewId(TEST_URI);
    
    UIForm basicForm = new UIFormBase();
    basicForm.setId("basicForm");
    root.getChildren().add(basicForm);
    UIPanel panel = new UIPanelBase();
    basicForm.getChildren().add(panel);
    UIPanel commandPanel = new UIPanelBase();
    commandPanel.setId("commandPanel");
    TestCommand command1 = new TestCommand();
    TestCommand command2 = new TestCommand();
    commandPanel.getChildren().add(command1);
    commandPanel.getChildren().add(command2);
    panel.getFacets().put("commandPanel", commandPanel);
    
    getFacesContext().setViewRoot(root);

    HttpSession session = (HttpSession) 
        getFacesContext().getExternalContext().getSession(false);
    // PENDING (rlubke) Is FACES_VIEW still valid
    session.setAttribute(RIConstants.FACES_VIEW, root);

    Phase restoreView = new RestoreViewPhase();

    try {
	restoreView.execute(getFacesContext());
    }
    catch (Throwable e) {
	assertTrue(false);
    }
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    assertTrue(command1.getDefaultListenerCount() > 0); 
    assertTrue(command2.getDefaultListenerCount() > 0); 

    // Now test with no facets... Listeners should still be registered on UICommand
    // components....
    // 
    root = new UIViewRootBase();
    root.setViewId(TEST_URI);
    basicForm = new UIFormBase();
    basicForm.setId("basicForm");
    root.getChildren().add(basicForm);
    command1 = new TestCommand();
    command2 = new TestCommand();
    basicForm.getChildren().add(command1);
    basicForm.getChildren().add(command2);
    
    getFacesContext().setViewRoot(root);

    session = (HttpSession) 
        getFacesContext().getExternalContext().getSession(false);
    // PENDING (rlubke) Is FACES_VIEW still valid
    session.setAttribute(RIConstants.FACES_VIEW, root);

    restoreView = new RestoreViewPhase();
    
    try {
	restoreView.execute(getFacesContext());
    }
    catch (Throwable e) {
	assertTrue(false);
    }   
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    assertTrue(command1.getDefaultListenerCount() > 0); 
    assertTrue(command2.getDefaultListenerCount() > 0); 
}

public static class TestCommand extends UICommandBase {
    public int getDefaultListenerCount() {
	List list = null;
	int ordinal = PhaseId.INVOKE_APPLICATION.getOrdinal();
	if (listeners != null) {
            list = listeners[ordinal]; 
            return list.size();
	} else {
            return 0;
	}
    }
}

} // end of class TestRestoreViewPhase

