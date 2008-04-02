/*
 * $Id: TestReconstituteComponentTreePhase.java,v 1.1 2003/03/11 05:37:59 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestReconstituteComponentTreePhase.java

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
import javax.faces.component.UIInput;
import javax.faces.component.UIForm;
import javax.faces.component.UINamingContainer;
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
 *  <B>TestReconstituteComponentTreePhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestReconstituteComponentTreePhase.java,v 1.1 2003/03/11 05:37:59 rkitain Exp $
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
    Phase reconstituteTree = new ReconstituteComponentTreePhase(null, 
			RIConstants.RECONSTITUTE_COMPONENT_TREE_PHASE);
    int result = -1;

    try {
	result = reconstituteTree.execute(getFacesContext());
    }
    catch (Throwable e) {
        e.printStackTrace();
	assertTrue(false);
    }
    assertTrue(Phase.GOTO_NEXT == result);

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
    
    HttpSession session = getFacesContext().getHttpSession();
    session.setAttribute(RIConstants.FACES_TREE, requestTree);
    // set a locale
    Locale locale = new Locale("France", "french");
    session.setAttribute(RIConstants.REQUEST_LOCALE, locale);
    
    Phase reconstituteTree = new ReconstituteComponentTreePhase(null, 
			RIConstants.RECONSTITUTE_COMPONENT_TREE_PHASE);
    int result = -1;

    try {
	result = reconstituteTree.execute(getFacesContext());
    }
    catch (Throwable e) {
	assertTrue(false);
    }
    assertTrue(Phase.GOTO_NEXT == result);

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




} // end of class TestReconstituteComponentTreePhase
