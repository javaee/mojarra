/*
 * $Id: TestHandleRequestEventsPhase.java,v 1.10 2002/09/11 20:02:31 edburns Exp $
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
import javax.faces.component.UIInput;
import javax.faces.event.FacesEvent;

import com.sun.faces.ServletFacesTestCase;

import java.io.IOException;

/**
 *
 *  <B>TestHandleRequestEventsPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestHandleRequestEventsPhase.java,v 1.10 2002/09/11 20:02:31 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestHandleRequestEventsPhase extends ServletFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI = "/components.jsp";

public static final String DID_DECODE1 = "didDecode1";
public static final String DID_EVENT1 = "didEvent1";
public static final String DID_DECODE2 = "didDecode2";
public static final String DID_EVENT2 = "didEvent2";

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
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    theRequest.addParameter("/valueChange1", "jerry");
    theRequest.addParameter("/valueChange2", "robert");
}

// This method will build a tree, create two components, add them
// to the tree, apply the new values from the request to these
// components and invoke HandleRequestEventsPhase.
// Since the "processEvent" method for each component returns "true",
// the expected outcome is that the events for both components will
// be processed, and the next phase will be "render".
// 
public void testCallback()
{
    // 1. Set the root of the tree ...
    //
    Phase reconstituteTree = new ReconstituteRequestTreePhase(null,
                        Lifecycle.RECONSTITUTE_REQUEST_TREE_PHASE);
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

    int rc = Phase.GOTO_NEXT;
    UIValueChangeTextEntry1 valueChange1 = null;
    UIValueChangeTextEntry2 valueChange2 = null;
    String value = null;
    Phase 
	applyValues = new ApplyRequestValuesPhase(null, 
					Lifecycle.APPLY_REQUEST_VALUES_PHASE), 
	handleEvents = new HandleRequestEventsPhase(null, 
                                      Lifecycle.HANDLE_REQUEST_EVENTS_PHASE);
    // clear the property
    System.setProperty(DID_DECODE1, EMPTY);
    System.setProperty(DID_EVENT1, EMPTY);
    System.setProperty(DID_DECODE2, EMPTY);
    System.setProperty(DID_EVENT2, EMPTY);

    // Stick in our new components
    valueChange1 = new UIValueChangeTextEntry1();
    valueChange1.setComponentId("valueChange1");
    valueChange2 = new UIValueChangeTextEntry2();
    valueChange2.setComponentId("valueChange2");

    UIComponent root = getFacesContext().getRequestTree().getRoot();
    root.addChild(valueChange1);
    root.addChild(valueChange2);

    // 2. Apply the new values from the request to the components in
    //    the tree.
    //
    rc = applyValues.execute(getFacesContext());
    assertTrue(Phase.GOTO_NEXT == rc);
    assertTrue(!System.getProperty(DID_DECODE1).equals(EMPTY));
    assertTrue(!System.getProperty(DID_DECODE2).equals(EMPTY));
    
    // 3. Handle request event
    //    The expected outcome: both events get processed (for each
    //    component), and the next phase is "render".
    //
    rc = handleEvents.execute(getFacesContext());
    assertTrue(Phase.GOTO_RENDER == rc);
    assertTrue(!System.getProperty(DID_EVENT1).equals(EMPTY));
    assertTrue(!System.getProperty(DID_EVENT2).equals(EMPTY));

    System.setProperty(DID_DECODE1, EMPTY);
    System.setProperty(DID_EVENT1, EMPTY);
    System.setProperty(DID_DECODE2, EMPTY);
    System.setProperty(DID_EVENT2, EMPTY);
}

/**
* Enqueue value change events
*/

public static class UIValueChangeTextEntry1 extends UIInput
{

public boolean decode(FacesContext context) throws IOException 
{
    System.setProperty(DID_DECODE1, DID_DECODE1);
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
	context.addRequestEvent(this,new FacesEvent(this));
    }
	
    setValue(newValue);
    return true;
}

public boolean processEvent(FacesContext context, FacesEvent event) 
{
    assertTrue(event.getSource() == this);
    System.setProperty(DID_EVENT1, DID_EVENT1);
    return true;
}
} // end of class UIValueChangeTextEntry1

public static class UIValueChangeTextEntry2 extends UIInput
{

public boolean decode(FacesContext context) throws IOException
{
    System.setProperty(DID_DECODE2, DID_DECODE2);
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
        context.addRequestEvent(this,new FacesEvent(this));
    }

    setValue(newValue);
    return true;
}

public boolean processEvent(FacesContext context, FacesEvent event)
{
    assertTrue(event.getSource() == this);
    System.setProperty(DID_EVENT2, DID_EVENT2);
    return true;
}
} // end of class UIValueChangeTextEntry2

} // end of class TestHandleRequestEventsPhase
