/*
 * $Id: TestReconstituteRequestTreePhase.java,v 1.3 2002/08/02 19:32:11 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestReconstituteRequestTreePhase.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIComponent;
import javax.faces.component.UITextEntry;
import javax.faces.component.UIForm;
import javax.faces.component.UIComponentBase;
import com.sun.faces.tree.SimpleTreeImpl;
import javax.faces.tree.TreeFactory;
import javax.faces.tree.Tree;
import com.sun.faces.RIConstants;

import com.sun.faces.ServletFacesTestCase;
import javax.servlet.http.HttpSession;
import javax.faces.render.RenderKitFactory;
import java.util.Locale;

/**
 *
 *  <B>TestReconstituteRequestTreePhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestReconstituteRequestTreePhase.java,v 1.3 2002/08/02 19:32:11 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestReconstituteRequestTreePhase extends ServletFacesTestCase
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

    public TestReconstituteRequestTreePhase() {
	super("TestReconstituteRequestTreePhase");
    }

    public TestReconstituteRequestTreePhase(String name) {
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
    Phase reconstituteTree = new ReconstituteRequestTreePhase(null, 
			Lifecycle.RECONSTITUTE_REQUEST_TREE_PHASE);
    int result = -1;

    try {
	result = reconstituteTree.execute(getFacesContext());
    }
    catch (Throwable e) {
        e.printStackTrace();
	assertTrue(false);
    }
    assertTrue(Phase.GOTO_NEXT == result);

    assertTrue(null != getFacesContext().getRequestTree());
    assertTrue(getFacesContext().getRequestTree() == 
	       getFacesContext().getResponseTree());
    assertTrue(RenderKitFactory.DEFAULT_RENDER_KIT == 
            getFacesContext().getRequestTree().getRenderKitId());

    assertTrue(null != getFacesContext().getRequestTree().getRoot());
    assertTrue(null != getFacesContext().getLocale());
    
    UIComponent root = null;

    assertTrue(getFacesContext().getRequestTree().getTreeId().equals(TEST_URI));
    root = getFacesContext().getRequestTree().getRoot();
    assertTrue(root.getChildCount() == 0);
}

public void testReconstituteRequestSubmit()
{
    Tree requestTree = null;
    // precreate tree and set it in session and make sure the tree is
    // restored from session.
   
    UIComponent root = new UIComponentBase() {
        public String getComponentType() { return "root"; }
    };
    
    UIForm basicForm = new UIForm();
    basicForm.setComponentId("basicForm");
    UITextEntry userName = new UITextEntry();
    
    userName.setComponentId("userName");
    root.addChild(basicForm);
    basicForm.addChild(userName);
    
    requestTree = new SimpleTreeImpl(getFacesContext().getServletContext(), root, 
            TEST_URI);
    assertTrue(requestTree != null);
    
    HttpSession session = getFacesContext().getHttpSession();
    session.setAttribute(RIConstants.FACES_TREE, requestTree);
    // set a locale
    Locale locale = new Locale("France", "french");
    session.setAttribute(RIConstants.REQUEST_LOCALE, locale);
    
    Phase reconstituteTree = new ReconstituteRequestTreePhase(null, 
			Lifecycle.RECONSTITUTE_REQUEST_TREE_PHASE);
    int result = -1;

    try {
	result = reconstituteTree.execute(getFacesContext());
    }
    catch (Throwable e) {
	assertTrue(false);
    }
    assertTrue(Phase.GOTO_NEXT == result);

    assertTrue(null != getFacesContext().getRequestTree());
    assertTrue(getFacesContext().getRequestTree() == 
	       getFacesContext().getResponseTree());
    assertTrue(RenderKitFactory.DEFAULT_RENDER_KIT == 
            getFacesContext().getRequestTree().getRenderKitId());

    assertTrue(null != getFacesContext().getRequestTree().getRoot());
    assertTrue(locale == getFacesContext().getLocale());
    
    assertTrue(getFacesContext().getRequestTree().getTreeId().equals(TEST_URI));
    root = getFacesContext().getRequestTree().getRoot();
    // components should exist.
    assertTrue(root.getChildCount() == 1);
    assertTrue(userName == root.findComponent("/basicForm/userName"));
    assertTrue(basicForm == root.findComponent("/basicForm")); 
}




} // end of class TestReconstituteRequestTreePhase
