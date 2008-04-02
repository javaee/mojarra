/*
 * $Id: TestReconstituteComponentTreePhase.java,v 1.5 2003/05/21 18:46:29 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestReconstituteComponentTreePhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.RIConstants;
import com.sun.faces.context.FacesContextImpl;
import com.sun.faces.lifecycle.Phase;
import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.tree.SimpleTreeImpl;

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
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.event.PhaseId;
import javax.faces.tree.TreeFactory;
import javax.faces.tree.Tree;
import javax.servlet.http.HttpSession;
import javax.faces.render.RenderKitFactory;

import org.apache.cactus.WebRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

/**
 *
 *  <B>TestReconstituteComponentTreePhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestReconstituteComponentTreePhase.java,v 1.5 2003/05/21 18:46:29 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestReconstituteComponentTreePhase extends ServletFacesTestCase
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

    public TestReconstituteComponentTreePhase() {
	super("TestReconstituteComponentTreePhase");
    }

    public TestReconstituteComponentTreePhase(String name) {
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
    Phase reconstituteTree = new ReconstituteComponentTreePhase();

    try {
        reconstituteTree.execute(getFacesContext());
    }
    catch (Throwable e) {
        e.printStackTrace();
	assertTrue(false);
    }
    assertTrue(!((FacesContextImpl)getFacesContext()).getRenderResponse() &&
        !((FacesContextImpl)getFacesContext()).getResponseComplete());

    assertTrue(null != getFacesContext().getTree());
    assertTrue(RenderKitFactory.DEFAULT_RENDER_KIT == 
            getFacesContext().getTree().getRenderKitId());

    assertTrue(null != getFacesContext().getTree().getRoot());
    assertTrue(null != getFacesContext().getLocale());
    
    UIComponent root = null;

    assertTrue(getFacesContext().getTree().getTreeId().equals(TEST_URI));
    root = getFacesContext().getTree().getRoot();
    assertTrue(root.getChildCount() == 0);
}

public void testReconstituteRequestSubmit()
{
    Tree requestTree = null;
    // precreate tree and set it in session and make sure the tree is
    // restored from session.
   
    UIComponent root = new UINamingContainer() {
        public String getComponentType() { return "root"; }
    };
    
    UIForm basicForm = new UIForm();
    basicForm.setComponentId("basicForm");
    UIInput userName = new UIInput();
    
    userName.setComponentId("userName");
    root.addChild(basicForm);
    basicForm.addChild(userName);
    
    requestTree = new SimpleTreeImpl(getFacesContext(), root, 
            TEST_URI);
    assertTrue(requestTree != null);
    
    HttpSession session = (HttpSession) 
        getFacesContext().getExternalContext().getSession(false);
    session.setAttribute(RIConstants.FACES_TREE, requestTree);
    // set a locale
    Locale locale = new Locale("France", "french");
    session.setAttribute(RIConstants.REQUEST_LOCALE, locale);
    
    Phase reconstituteTree = new ReconstituteComponentTreePhase();

    try {
	reconstituteTree.execute(getFacesContext());
    }
    catch (Throwable e) {
	assertTrue(false);
    }
    assertTrue(!((FacesContextImpl)getFacesContext()).getRenderResponse() &&
        !((FacesContextImpl)getFacesContext()).getResponseComplete());

    assertTrue(null != getFacesContext().getTree());
    assertTrue(RenderKitFactory.DEFAULT_RENDER_KIT == 
            getFacesContext().getTree().getRenderKitId());

    assertTrue(null != getFacesContext().getTree().getRoot());
    assertTrue(locale == getFacesContext().getLocale());
    
    assertTrue(getFacesContext().getTree().getTreeId().equals(TEST_URI));
    root = getFacesContext().getTree().getRoot();
    // components should exist.
    assertTrue(root.getChildCount() == 1);
    assertTrue(userName == root.findComponent("userName"));
    assertTrue(basicForm == root.findComponent("basicForm")); 
}

/**
 * This method will test the <code>registerActionListeners</code> method.
 * It will first create a simple tree consisting of a couple of <code>UICommand</code>
 * components added to a facet;  Then the <code>ReconstituteComponentTree.execute</code>
 * method is run;  And finally, an assertion is done to ensure that default action
 * listeners have been registered on the <code>UICommand</code> components;
 */
public void testRegisterListeners() { 
    Tree requestTree = null;
    // precreate tree and set it in session and make sure the tree is
    // restored from session.
   
    UIComponent root = new UINamingContainer() {
        public String getComponentType() { return "root"; }
    };
    
    UIForm basicForm = new UIForm();
    basicForm.setComponentId("basicForm");
    root.addChild(basicForm);
    UIPanel panel = new UIPanel();
    basicForm.addChild(panel);
    UIPanel commandPanel = new UIPanel();
    commandPanel.setComponentId("commandPanel");
    TestCommand command1 = new TestCommand();
    TestCommand command2 = new TestCommand();
    commandPanel.addChild(command1);
    commandPanel.addChild(command2);
    panel.addFacet("commandPanel", commandPanel);

    requestTree = new SimpleTreeImpl(getFacesContext(), root, 
            TEST_URI);
    assertTrue(requestTree != null);

    getFacesContext().setTree(requestTree);

    HttpSession session = (HttpSession) 
        getFacesContext().getExternalContext().getSession(false);
    session.setAttribute(RIConstants.FACES_TREE, requestTree);

    Phase reconstituteTree = new ReconstituteComponentTreePhase();

    try {
	reconstituteTree.execute(getFacesContext());
    }
    catch (Throwable e) {
	assertTrue(false);
    }
    assertTrue(!((FacesContextImpl)getFacesContext()).getRenderResponse() &&
        !((FacesContextImpl)getFacesContext()).getResponseComplete());

    assertTrue(command1.getDefaultListenerCount() > 0); 
    assertTrue(command2.getDefaultListenerCount() > 0); 

    // Now test with no facets... Listeners should still be registered on UICommand
    // components....
    // 
    root = new UINamingContainer() {
        public String getComponentType() { return "root"; }
    };
    basicForm = new UIForm();
    basicForm.setComponentId("basicForm");
    root.addChild(basicForm);
    command1 = new TestCommand();
    command2 = new TestCommand();
    basicForm.addChild(command1);
    basicForm.addChild(command2);
    requestTree = new SimpleTreeImpl(getFacesContext(), root, 
            TEST_URI);
    assertTrue(requestTree != null);

    getFacesContext().setTree(requestTree);

    session = (HttpSession) 
        getFacesContext().getExternalContext().getSession(false);
    session.setAttribute(RIConstants.FACES_TREE, requestTree);

    reconstituteTree = new ReconstituteComponentTreePhase();

    try {
	reconstituteTree.execute(getFacesContext());
    }
    catch (Throwable e) {
	assertTrue(false);
    }   
    assertTrue(!((FacesContextImpl)getFacesContext()).getRenderResponse() &&
        !((FacesContextImpl)getFacesContext()).getResponseComplete());

    assertTrue(command1.getDefaultListenerCount() > 0); 
    assertTrue(command2.getDefaultListenerCount() > 0); 
}

public static class TestCommand extends UICommand {
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

} // end of class TestReconstituteComponentTreePhase

