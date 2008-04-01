/*
 * $Id: TestApplyRequestValuesPhase.java,v 1.3 2002/06/18 18:23:23 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestApplyRequestValuesPhase.java

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

import com.sun.faces.FacesContextTestCase;

/**
 *
 *  <B>TestApplyRequestValuesPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestApplyRequestValuesPhase.java,v 1.3 2002/06/18 18:23:23 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestApplyRequestValuesPhase extends FacesContextTestCase
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

    public TestApplyRequestValuesPhase() {
	super("TestApplyRequestValuesPhase");
    }

    public TestApplyRequestValuesPhase(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//

public void beginCallback(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI_XUL, null);
    //theRequest.addParameter("tree", TEST_URI_XUL);
    theRequest.addParameter("/basicForm/userName", "jerry");
}

public void testCallback()
{
    int rc = Phase.GOTO_NEXT;
    UIComponent root = null;
    UITextEntry userName = null;
    String value = null;
    Phase 
	applyValues = new ApplyRequestValuesPhase(null, 
					Lifecycle.APPLY_REQUEST_VALUES_PHASE), 
	createTree = new CreateRequestTreePhase(null, 
				       Lifecycle.CREATE_REQUEST_TREE_PHASE);
    rc = createTree.execute(facesContext);
    assertTrue(Phase.GOTO_NEXT == rc);
    rc = applyValues.execute(facesContext);
    assertTrue(Phase.GOTO_NEXT == rc);
    
    root = facesContext.getRequestTree().getRoot();
    try {
	userName = (UITextEntry) root.findComponent("./basicForm/userName");
    }
    catch (Throwable e) {
	System.out.println(e.getMessage());
	assertTrue("Can't find userName in tree", false);
    }
    assertTrue(null != userName);
    assertTrue(null != (value = (String) userName.getValue()));
    assertTrue(value.equals("jerry"));
}




} // end of class TestApplyRequestValuesPhase
