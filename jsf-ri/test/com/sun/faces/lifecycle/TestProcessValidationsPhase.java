/*
 * $Id: TestProcessValidationsPhase.java,v 1.5 2002/07/12 23:58:46 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UITextEntry;
import javax.faces.validator.Validator;
import javax.faces.component.AttributeDescriptor;

import com.sun.faces.ServletFacesTestCase;

import java.io.IOException;

import java.util.Iterator;

/**
 *
 *  <B>TestProcessValidationsPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestProcessValidationsPhase.java,v 1.5 2002/07/12 23:58:46 rkitain Exp $
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
public static UITextEntry userName = null;

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
    theRequest.addParameter("/root/basicForm/userName", "jerry");
}

public void testCallback()
{
    int rc = Phase.GOTO_NEXT;
    UIComponent root = null;
    userName = null;
    String value = null;
    Phase 
        reconstituteTree = new ReconstituteRequestTreePhase(null,
            Lifecycle.RECONSTITUTE_REQUEST_TREE_PHASE),
	applyValues = new ApplyRequestValuesPhase(null, 
            Lifecycle.APPLY_REQUEST_VALUES_PHASE), 
	handleEvents = new HandleRequestEventsPhase(null, 
            Lifecycle.HANDLE_REQUEST_EVENTS_PHASE),
	processValidations = new ProcessValidationsPhase(null, 
            Lifecycle.PROCESS_VALIDATIONS_PHASE);

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

    root = getFacesContext().getRequestTree().getRoot();
    UIForm basicForm = new UIForm();
    basicForm.setComponentId("basicForm");
    UITextEntry userName1 = new UITextEntry();
    userName1.setComponentId("userName");
    root.addChild(basicForm);
    basicForm.addChild(userName1);

    // clear the property
    System.setProperty(DID_VALIDATE, EMPTY);

    try {
	userName = (UITextEntry) root.findComponent("./basicForm/userName");
    }
    catch (Throwable e) {
	System.out.println(e.getMessage());
	assertTrue("Can't find userName in tree", false);
    }

    // add the validator
    Validator validator = new Validator() {
        public AttributeDescriptor getAttributeDescriptor(String name) {
            return null;
        }
        public Iterator getAttributeNames() {
            return null;
        }

        public void validate(FacesContext context, UIComponent component){
            assertTrue(component == userName);
            System.setProperty(DID_VALIDATE, DID_VALIDATE);
        }
    };
    userName.addValidator(validator);

    rc = applyValues.execute(getFacesContext());
    assertTrue(Phase.GOTO_NEXT == rc);
    
    rc = handleEvents.execute(getFacesContext());
    assertTrue(Phase.GOTO_NEXT == rc);

    rc = processValidations.execute(getFacesContext());
    assertTrue(!System.getProperty(DID_VALIDATE).equals(EMPTY));
    
    System.setProperty(DID_VALIDATE, EMPTY);
}

} // end of class TestProcessValidationsPhase
