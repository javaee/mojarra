/*
 * $Id: TestProcessValidationsPhase.java,v 1.2 2002/06/03 19:31:09 eburns Exp $
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
import javax.faces.component.UITextEntry;
import javax.faces.validator.Validator;
import javax.faces.component.AttributeDescriptor;

import com.sun.faces.FacesContextTestCase;

import java.io.IOException;

import java.util.Iterator;

/**
 *
 *  <B>TestProcessValidationsPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestProcessValidationsPhase.java,v 1.2 2002/06/03 19:31:09 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestProcessValidationsPhase extends FacesContextTestCase
{
//
// Protected Constants
//

public static final String TEST_URI_XUL = "/Faces_Basic.xul";

public static final String DID_VALIDATE = "didValidate";
public static final String EMPTY = "empty";
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
    theRequest.addParameter("tree", TEST_URI_XUL);
    theRequest.addParameter("/root/basicForm/userName", "jerry");
}

public void testCallback()
{
    int rc = Phase.GOTO_NEXT;
    UIComponent root = null;
    userName = null;
    String value = null;
    Phase 
	applyValues = new ApplyRequestValuesPhase(null, 
					Lifecycle.APPLY_REQUEST_VALUES_PHASE), 
	createTree = new CreateRequestTreePhase(null, 
				       Lifecycle.CREATE_REQUEST_TREE_PHASE),
	handleEvents = new HandleRequestEventsPhase(null, 
                                      Lifecycle.HANDLE_REQUEST_EVENTS_PHASE),
	processValidations = new ProcessValidationsPhase(null, 
                                          Lifecycle.PROCESS_VALIDATIONS_PHASE);
    rc = createTree.execute(facesContext);
    assertTrue(Phase.GOTO_NEXT == rc);

    // clear the property
    System.setProperty(DID_VALIDATE, EMPTY);

    root = facesContext.getRequestTree().getRoot();
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

    rc = applyValues.execute(facesContext);
    assertTrue(Phase.GOTO_NEXT == rc);
    
    rc = handleEvents.execute(facesContext);
    assertTrue(Phase.GOTO_NEXT == rc);

    rc = processValidations.execute(facesContext);
    assertTrue(!System.getProperty(DID_VALIDATE).equals(EMPTY));
    
    System.setProperty(DID_VALIDATE, EMPTY);
}

} // end of class TestProcessValidationsPhase
