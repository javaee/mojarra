/*
 * $Id: TestCreateRequestTreePhase.java,v 1.3 2002/06/20 01:34:25 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestCreateRequestTreePhase.java

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

import com.sun.faces.ServletFacesTestCase;

/**
 *
 *  <B>TestCreateRequestTreePhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestCreateRequestTreePhase.java,v 1.3 2002/06/20 01:34:25 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestCreateRequestTreePhase extends ServletFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI_XUL = "/Faces_Basic.xul";

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

    public TestCreateRequestTreePhase() {
	super("TestCreateRequestTreePhase");
    }

    public TestCreateRequestTreePhase(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//

public void beginExecute(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI_XUL, null);
    //theRequest.addParameter("tree", TEST_URI_XUL);
    System.setProperty(FactoryFinder.TREE_FACTORY,
			"com.sun.faces.tree.XmlTreeFactoryImpl");
}

public void beginExecteDefaultRequestTree(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI_XUL, null);
    //theRequest.addParameter("tree", TEST_URI_XUL);
    System.setProperty(FactoryFinder.TREE_FACTORY,
			"com.sun.faces.tree.XmlTreeFactoryImpl");
}


public void testExecute()
{
    testExecteDefaultRequestTree();

    UIComponent root = null;

    assertTrue(getFacesContext().getRequestTree().getTreeId().equals(TEST_URI_XUL));
    root = getFacesContext().getRequestTree().getRoot();
    assertTrue(null != root.findComponent("./basicForm/appleQuantity"));
    assertTrue(null != root.findComponent("./basicForm/login"));
    assertTrue(null != root.findComponent("./basicForm/login2"));
    assertTrue(null != root.findComponent("./basicForm"));
}

public void testExecteDefaultRequestTree()
{
    Phase createTree = new CreateRequestTreePhase(null, 
						  Lifecycle.CREATE_REQUEST_TREE_PHASE);
    int result = -1;

    try {
	result = createTree.execute(getFacesContext());
    }
    catch (Throwable e) {
	assertTrue(false);
    }
    assertTrue(Phase.GOTO_NEXT == result);

    assertTrue(null != getFacesContext().getRequestTree());
    assertTrue(getFacesContext().getRequestTree() == 
	       getFacesContext().getResponseTree());
    assertTrue(null != getFacesContext().getRequestTree().getRenderKit());
    assertTrue(null != getFacesContext().getRequestTree().getRoot());
    assertTrue(null != getFacesContext().getLocale());
}




} // end of class TestCreateRequestTreePhase
