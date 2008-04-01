/*
 * $Id: TestEventContext.java,v 1.2 2002/01/12 01:41:17 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestEventContext.java

package com.sun.faces;

import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;

import javax.faces.ObjectManager;
import javax.faces.EventQueue;
import javax.faces.ClientCapabilities;
import javax.faces.EventDispatcher;
import javax.faces.ValueChangeEvent;
import javax.faces.CommandEvent;
import javax.faces.ValueChangeDispatcher;
import javax.faces.CommandDispatcher;
import javax.faces.ObjectAccessor;
import javax.faces.NavigationHandler;

/**
 *
 *  <B>TestEventContext</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestEventContext.java,v 1.2 2002/01/12 01:41:17 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestEventContext extends FacesTestCase
{
//
// Protected Constants
//

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

    public TestEventContext() {super("TestEventContext");}
    public TestEventContext(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//

public void testAccessors() 
{
    boolean result = false;
    EventQueue eq = null;
    EventDispatcher eventDispatcher = null;
    ServletRequest req = null;
    ServletResponse resp = null;
    ObjectManager objectManager = null;
    ClientCapabilities caps = null;
    NavigationHandler nav = null;
    ObjectAccessor objectAccessor = null;

    caps = eventContext.getClientCapabilities();
    result = null == caps; // PENDING(edburns): should not be null
    System.out.println("Testing getEventQueue: " + result);
    assertTrue(result);
    
    req = eventContext.getRequest();
    result = null != req;
    System.out.println("Testing getRequest: " + result);
    assertTrue(result);

    resp = eventContext.getResponse();
    result = null != resp;
    System.out.println("Testing getResponse: " + result);
    assertTrue(result);

    eq = eventContext.getEventQueue();
    result = null != eq;
    System.out.println("Testing getEventQueue: " + result);
    assertTrue(result);

    ValueChangeEvent valueChange = new ValueChangeEvent(request, "source", 
							"modelRef", "value");
    eventDispatcher = eventContext.getEventDispatcher(valueChange);
    result = null != eventDispatcher;
    System.out.println("Testing getEventDispatcher for valueChange: " + 
		       result);
    assertTrue(result);
    result = eventDispatcher instanceof ValueChangeDispatcher;
    System.out.println("Testing getEventDispatcher for valueChange: isA " +
		       "ValueChangeDispatcher: " + result);
    assertTrue(result);
    
    CommandEvent command = new CommandEvent(request, "name", 
					    "value");
    eventDispatcher = eventContext.getEventDispatcher(command);
    result = null != eventDispatcher;
    System.out.println("Testing getEventDispatcher for command: " + 
		       result);
    assertTrue(result);
    result = eventDispatcher instanceof CommandDispatcher;
    System.out.println("Testing getEventDispatcher for command: isA " +
		       "CommandDispatcher: " + result);
    assertTrue(result);

    nav = eventContext.getNavigationHandler();
    result = null == nav; // PENDING(edburns): should not be null
    System.out.println("Testing getNavigationHandler: " + result);
    assertTrue(result);
    
    objectManager = eventContext.getObjectManager();
    result = null != objectManager;
    System.out.println("Testing getObjectManager: " + result);
    assertTrue(result);

    objectAccessor = eventContext.getObjectAccessor();
    result = null != objectAccessor; 
    System.out.println("Testing getObjectAccessor: " + result);
    assertTrue(result);
}

} // end of class TestEventContext
