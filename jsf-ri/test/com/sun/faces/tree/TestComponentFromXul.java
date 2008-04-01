/*
 * $Id: TestComponentFromXul.java,v 1.2 2002/06/18 18:23:28 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestComponentFromXul.java

package com.sun.faces.tree;

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
import com.sun.faces.lifecycle.CreateRequestTreePhase;

/**
 *
 *  <B>TestComponentFromXul</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestComponentFromXul.java,v 1.2 2002/06/18 18:23:28 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestComponentFromXul extends FacesContextTestCase
{
//
// Protected Constants
//

public static final String TEST_URI_XUL = "/Faces_Basic_Component.xul";

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

    public TestComponentFromXul() {
	super("TestComponentFromXul");
    }

    public TestComponentFromXul(String name) {
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

    assertTrue(facesContext.getRequestTree().getTreeId().equals(TEST_URI_XUL));
    root = facesContext.getRequestTree().getRoot();
    assertTrue(null != root.findComponent("./basicForm/myOutput"));

    assertTrue(null != root.findComponent("./basicForm"));

    assertTrue(root.findComponent("./basicForm/myOutput") instanceof UIComponent);
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
       e.printStackTrace();
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




} // end of class TestComponentFromXul
