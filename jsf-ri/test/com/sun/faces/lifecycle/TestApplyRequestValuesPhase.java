/*
 * $Id: TestApplyRequestValuesPhase.java,v 1.22 2004/01/27 21:06:03 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestApplyRequestValuesPhase.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;

import com.sun.faces.util.Util;


import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;

import com.sun.faces.lifecycle.Phase;
import com.sun.faces.ServletFacesTestCase;

/**
 *
 *  <B>TestApplyRequestValuesPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestApplyRequestValuesPhase.java,v 1.22 2004/01/27 21:06:03 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestApplyRequestValuesPhase extends ServletFacesTestCase
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
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    theRequest.addParameter("basicForm" + NamingContainer.SEPARATOR_CHAR + "userName", "jerry");
}

public void testCallback()
{
    UIComponent root = null;
    String value = null;
    Phase 
        restoreView = new RestoreViewPhase(),
	applyValues = new ApplyRequestValuesPhase();

    // 1. Set the root of the view ...
    //
    try {
        restoreView.execute(getFacesContext());
    }
    catch (Throwable e) {
        e.printStackTrace();
        assertTrue(false);
    }
    assertTrue((getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));
    assertTrue(null != getFacesContext().getViewRoot());

    // 2. Add components to tree
    //
    root = getFacesContext().getViewRoot();
    UIForm basicForm = new UIForm();
    basicForm.setId("basicForm");
    UIInput userName = new UIInput();
    userName.setId("userName");
    root.getChildren().add(basicForm);
    basicForm.getChildren().add(userName);

    // 3. Apply values
    //
    applyValues.execute(getFacesContext());
    assertTrue((getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));
    
    root = getFacesContext().getViewRoot();
    try {
	userName = (UIInput) basicForm.findComponent("userName");
    }
    catch (Throwable e) {
	System.out.println(e.getMessage());
	assertTrue("Can't find userName in tree", false);
    }
    assertTrue(null != userName);
    assertTrue(null != (value = (String) userName.getSubmittedValue()));
    assertTrue(value.equals("jerry"));
}




} // end of class TestApplyRequestValuesPhase
