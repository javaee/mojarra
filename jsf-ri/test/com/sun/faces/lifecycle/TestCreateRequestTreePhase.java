/*
 * $Id: TestCreateRequestTreePhase.java,v 1.1 2002/06/01 00:58:23 eburns Exp $
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

import com.sun.faces.FacesContextTestCase;

/**
 *
 *  <B>TestCreateRequestTreePhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestCreateRequestTreePhase.java,v 1.1 2002/06/01 00:58:23 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestCreateRequestTreePhase extends FacesContextTestCase
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
    theRequest.addParameter("tree", TEST_URI_XUL);
    System.setProperty(FactoryFinder.TREE_FACTORY,
			"com.sun.faces.tree.XmlTreeFactoryImpl");
}

public void testExecute()
{
    testExecteDefaultRequestTree();

    UIComponent root = null;

    assertTrue(facesContext.getRequestTree().getTreeId().equals(TEST_URI_XUL));
    root = facesContext.getRequestTree().getRoot();
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
	result = createTree.execute(facesContext);
    }
    catch (Throwable e) {
	assertTrue(false);
    }
    assertTrue(Phase.GOTO_NEXT == result);

    assertTrue(null != facesContext.getRequestTree());
    assertTrue(facesContext.getRequestTree() == 
	       facesContext.getResponseTree());
    assertTrue(null != facesContext.getRequestTree().getRenderKit());
    assertTrue(null != facesContext.getRequestTree().getRoot());
    assertTrue(null != facesContext.getLocale());
}




} // end of class TestCreateRequestTreePhase
