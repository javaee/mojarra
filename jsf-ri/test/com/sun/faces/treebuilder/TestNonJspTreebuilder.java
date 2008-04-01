/*
 * $Id: TestNonJspTreebuilder.java,v 1.1 2002/04/25 22:42:21 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestNonJspTreebuilder.java

package com.sun.faces.treebuilder;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;

import javax.faces.Constants;
import javax.faces.UIComponent;
import javax.faces.FacesContext;
import javax.faces.ObjectManager;
import javax.faces.AbstractFactory;
import javax.faces.TreeNavigator;
import javax.faces.UIPage;


import org.xml.sax.Attributes;

import com.sun.faces.FacesTestCase;
import com.sun.faces.ParamBlockingRequestWrapper;

import org.apache.cactus.WebRequest;

import com.sun.faces.treebuilder.TreeEngine;
import com.sun.faces.treebuilder.NonJspTreeEngineImpl;
import com.sun.faces.treebuilder.TreeBuilder;
import com.sun.faces.treebuilder.BuildComponentFromTag;
import com.sun.faces.util.Util;

import java.util.List;
import java.util.Iterator;

/**
 *
 *  Exercise NonJspTreeBuilder and NonJspTreeEngine
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestNonJspTreebuilder.java,v 1.1 2002/04/25 22:42:21 eburns Exp $
 * 
 * @see	com.sun.faces.TreeNavigator
 * @see	com.sun.faces.TreeEngine
 *
 */

public class TestNonJspTreebuilder extends FacesTestCase
{
//
// Protected Constants
//

// PENDING(rogerk) change these to be our XML file
public static final String TEST_URI = "/Faces_Basic.uiml";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

private BuildComponentFromTag componentBuilder = null;

//
// Constructors and Initializers    
//

    public TestNonJspTreebuilder() {super("TestNonJspTreebuilder");}
    public TestNonJspTreebuilder(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//


public void setUp() {
    super.setUp();
    // Install the NonJspTreeEngineFactory
    ObjectManager om = facesContext.getObjectManager();
    assertTrue(null != om);
    AbstractFactory abstractFactory = (AbstractFactory)
	om.get(Constants.REF_ABSTRACTFACTORY);
    assertTrue(null != abstractFactory);

    abstractFactory.addFactoryForFacesName(new com.sun.faces.treebuilder.NonJspTreeEngineImpl.Factory(), 
					   Constants.REF_TREEENGINE);
}

public void tearDown() {
    super.tearDown();
}

public void beginTree(WebRequest theRequest) {
    theRequest.setURL("localhost:8080", null, null, TEST_URI,null);
}

public void testTree() {
    ObjectManager om = facesContext.getObjectManager();
    assertTrue(null != om);
    AbstractFactory abstractFactory = (AbstractFactory)
	om.get(Constants.REF_ABSTRACTFACTORY);
    assertTrue(null != abstractFactory);

    TreeEngine treeEngine = (TreeEngine)
	abstractFactory.newInstance(Constants.REF_TREEENGINE, 
				    config.getServletContext());
    assertTrue(null != treeEngine);

    UIPage root = new UIPage();

    TreeNavigator treeNav = treeEngine.getTreeForURI(facesContext,
						     root, TEST_URI);
    assertTrue(null != treeNav);

    // Write some code that traverses the tree, given a priori knowledge
    // of how it should look, making sure it conforms to how we expect
    // it to look.


}

public void beginFactory(WebRequest theRequest) {
    theRequest.setURL("localhost:8080", null, null, TEST_URI,null);
}

public void testFactory() {
    ObjectManager om = facesContext.getObjectManager();
    assertTrue(null != om);
    AbstractFactory abstractFactory = (AbstractFactory)
	om.get(Constants.REF_ABSTRACTFACTORY);
    assertTrue(null != abstractFactory);

    TreeEngine treeEngine = (TreeEngine)
	abstractFactory.newInstance(Constants.REF_TREEENGINE, 
				    config.getServletContext());
    assertTrue(null != treeEngine);

    assertTrue(treeEngine instanceof NonJspTreeEngineImpl);
}

} // end of class TestNonJspTreebuilder
