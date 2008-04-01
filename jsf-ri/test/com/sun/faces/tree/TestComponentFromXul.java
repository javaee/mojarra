/*
 * $Id: TestComponentFromXul.java,v 1.3 2002/06/20 01:34:27 eburns Exp $
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

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.lifecycle.CreateRequestTreePhase;

/**
 *
 *  <B>TestComponentFromXul</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestComponentFromXul.java,v 1.3 2002/06/20 01:34:27 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestComponentFromXul extends ServletFacesTestCase
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
}

public void beginExecteDefaultRequestTree(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI_XUL, null);
}

public void testExecute()
{
    testExecteDefaultRequestTree();

    UIComponent root = null;

    assertTrue(getFacesContext().getRequestTree().getTreeId().equals(TEST_URI_XUL));
    root = getFacesContext().getRequestTree().getRoot();
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
	result = createTree.execute(getFacesContext());
    }
    catch (Throwable e) {
       e.printStackTrace();
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




} // end of class TestComponentFromXul
