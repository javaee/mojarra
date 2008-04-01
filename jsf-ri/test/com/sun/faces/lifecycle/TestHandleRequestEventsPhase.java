/*
 * $Id: TestHandleRequestEventsPhase.java,v 1.5 2002/06/18 18:23:24 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestHandleRequestEventsPhase.java

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
import javax.faces.event.FacesEvent;

import com.sun.faces.FacesContextTestCase;

import java.io.IOException;

/**
 *
 *  <B>TestHandleRequestEventsPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestHandleRequestEventsPhase.java,v 1.5 2002/06/18 18:23:24 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestHandleRequestEventsPhase extends FacesContextTestCase
{
//
// Protected Constants
//

public static final String TEST_URI_XUL = "/Faces_Basic.xul";

public static final String DID_DECODE = "didDecode";
public static final String DID_EVENT = "didEvent";
public static final String EMPTY = "empty";

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

    public TestHandleRequestEventsPhase() {
	super("TestHandleRequestEventsPhase");
    }

    public TestHandleRequestEventsPhase(String name) {
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
   // theRequest.addParameter("tree", TEST_URI_XUL);
    theRequest.addParameter("/valueChange", "jerry");
}

public void testCallback()
{
    int rc = Phase.GOTO_NEXT;
    UIComponent root = null;
    UIValueChangeTextEntry valueChange = null;
    String value = null;
    Phase 
	applyValues = new ApplyRequestValuesPhase(null, 
					Lifecycle.APPLY_REQUEST_VALUES_PHASE), 
	createTree = new CreateRequestTreePhase(null, 
				       Lifecycle.CREATE_REQUEST_TREE_PHASE),
	handleEvents = new HandleRequestEventsPhase(null, 
                                      Lifecycle.HANDLE_REQUEST_EVENTS_PHASE);
    rc = createTree.execute(facesContext);
    assertTrue(Phase.GOTO_NEXT == rc);

    // clear the property
    System.setProperty(DID_DECODE, EMPTY);
    System.setProperty(DID_EVENT, EMPTY);

    // Stick in our new component
    valueChange = new UIValueChangeTextEntry();
    valueChange.setComponentId("valueChange");

    root = facesContext.getRequestTree().getRoot();
    root.addChild(valueChange);

    rc = applyValues.execute(facesContext);
    assertTrue(Phase.GOTO_NEXT == rc);
    assertTrue(!System.getProperty(DID_DECODE).equals(EMPTY));
    
    rc = handleEvents.execute(facesContext);
    assertTrue(Phase.GOTO_NEXT == rc);
    assertTrue(!System.getProperty(DID_EVENT).equals(EMPTY));

    System.setProperty(DID_DECODE, EMPTY);
    System.setProperty(DID_EVENT, EMPTY);
}

/**

* Enqueue a value change event

*/

public static class UIValueChangeTextEntry extends UITextEntry
{

public void decode(FacesContext context) throws IOException 
{
    System.setProperty(DID_DECODE, DID_DECODE);
    if (context == null) {
	throw new NullPointerException();
    }
    String newValue =
	context.getServletRequest().getParameter(getCompoundId());
    boolean valueChanged = false;
    // if the new value is different from the old value
    if ((null == this.getValue() && null != newValue) ||
	(null != this.getValue() && null == newValue)) {
	valueChanged = true;
    }
    if ((null != this.getValue()) &&
	!((String)this.getValue()).equals(newValue)) {
	valueChanged = true;
    }
    if (valueChanged) {
	this.addEvent(new FacesEvent(this));
    }
	
    setValue(newValue);
    
}

public boolean event(FacesContext context, FacesEvent event) 
{
    assertTrue(event.getSource() == this);
    System.setProperty(DID_EVENT, DID_EVENT);
    return true;
}



} // end of class UIValueChangeTextEntry


} // end of class TestHandleRequestEventsPhase
