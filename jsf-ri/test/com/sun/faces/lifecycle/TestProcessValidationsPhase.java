/*
 * $Id: TestProcessValidationsPhase.java,v 1.18 2003/08/13 21:06:42 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestProcessValidationsPhase.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.validator.Validator;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.lifecycle.Phase;
import java.io.IOException;

import java.util.Iterator;

/**
 *
 *  <B>TestProcessValidationsPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestProcessValidationsPhase.java,v 1.18 2003/08/13 21:06:42 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestProcessValidationsPhase extends ServletFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI = "/components.jsp";

public static final String DID_VALIDATE = "didValidate";
public static UIInput userName = null;

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

    public TestProcessValidationsPhase() {
	super("TestProcessValidationsPhase");
    }

    public TestProcessValidationsPhase(String name) {
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
    theRequest.addParameter("userName", "jerry");
}

public void testCallback()
{
    UIComponent root = null;
    userName = null;
    String value = null;
    Phase 
        reconstituteTree = new ReconstituteComponentTreePhase(),
	applyValues = new ApplyRequestValuesPhase(), 
	processValidations = new ProcessValidationsPhase();

    try {
        reconstituteTree.execute(getFacesContext());
    }
    catch (Throwable e) {
        e.printStackTrace();
        assertTrue(false);
    }
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));
    assertTrue(null != getFacesContext().getTree());

    root = getFacesContext().getTree().getRoot();
    UIForm basicForm = new UIForm();
    basicForm.setComponentId("basicForm");
    UIInput userName1 = new UIInput();
    userName1.setComponentId("userName");
    root.addChild(basicForm);
    basicForm.addChild(userName1);

    // clear the property
    System.setProperty(DID_VALIDATE, EMPTY);

    try {
	userName = (UIInput) root.findComponent("userName");
    }
    catch (Throwable e) {
	System.out.println(e.getMessage());
	assertTrue("Can't find userName in tree", false);
    }

    // add the validator
    Validator validator = new Validator() {
        public Iterator getAttributeNames() {
            return null;
        }

        public void validate(FacesContext context, UIInput component){
            assertTrue(component == userName);
            System.setProperty(DID_VALIDATE, DID_VALIDATE);
	    return;
        }
    };
    userName.addValidator(validator);

    assertTrue(userName.isValid());

    applyValues.execute(getFacesContext());
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));
    
    processValidations.execute(getFacesContext());
    assertTrue(!System.getProperty(DID_VALIDATE).equals(EMPTY));
    assertTrue(userName.isValid());
    
    System.setProperty(DID_VALIDATE, EMPTY);
}

} // end of class TestProcessValidationsPhase
