/*
 * $Id: TestFacesContext.java,v 1.1 2002/04/05 19:41:21 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestFacesContext.java

package com.sun.faces;

import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;

import javax.faces.ObjectManager;
import javax.faces.EventQueue;
import javax.faces.ClientCapabilities;
import javax.faces.EventDispatcher;
import javax.faces.ValueChangeEvent;
import javax.faces.CommandEvent;
import javax.faces.UITextEntry;
import javax.faces.UICommand;
import javax.faces.UIForm;
import javax.faces.ObjectAccessor;
import javax.faces.NavigationHandler;
import javax.faces.Constants;
import com.sun.faces.NavigationMapImpl;

/**
 *
 *  <B>TestFacesContext</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestFacesContext.java,v 1.1 2002/04/05 19:41:21 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestFacesContext extends FacesTestCase
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

    public TestFacesContext() {super("TestFacesContext");}
    public TestFacesContext(String name) {super(name);}
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
    ServletContext sc = null;

    caps = facesContext.getClientCapabilities();
    result = null == caps; // PENDING(edburns): should not be null
    System.out.println("Testing getEventQueue: " + result);
    assertTrue(result);

    req = facesContext.getRequest();
    result = null != req;
    System.out.println("Testing getRequest: " + result);
    assertTrue(result);

    resp = facesContext.getResponse();
    result = null != resp;
    System.out.println("Testing getResponse: " + result);
    assertTrue(result);

    sc = facesContext.getServletContext();
    result = null != sc;
    System.out.println("Testing getServletContext: " + result);
    assertTrue(result);
    
    eq = facesContext.getEventQueue();
    result = null != eq;
    System.out.println("Testing getEventQueue: " + result);
    assertTrue(result);

    objectManager = facesContext.getObjectManager();
    result = null != objectManager;
    System.out.println("Testing getObjectManager: " + result);
    assertTrue(result);

    UICommand uiCommand = new UICommand();
    uiCommand.setId("name");
    objectManager.put( request, "name", uiCommand);

    UITextEntry input = new UITextEntry();
    input.setId("source");
    objectManager.put( request, "source", input);

    ValueChangeEvent valueChange = new ValueChangeEvent(facesContext, input, 
							"value");
    eventDispatcher = facesContext.getEventDispatcher(valueChange);
    result = null != eventDispatcher;
    System.out.println("Testing getEventDispatcher for valueChange: " + 
		       result);
    assertTrue(result);
    result = eventDispatcher instanceof UITextEntry;
    System.out.println("Testing getEventDispatcher for valueChange: isA " +
		       "UITextEntry: " + result);
    assertTrue(result);
    
    CommandEvent command = new CommandEvent (facesContext, uiCommand, "value");
    eventDispatcher = facesContext.getEventDispatcher(command);
    result = null != eventDispatcher;
    System.out.println("Testing getEventDispatcher for command: " + 
		       result);
    assertTrue(result);
    result = eventDispatcher instanceof UICommand;
    System.out.println("Testing getEventDispatcher for command: isA " +
		       "UICommand: " + result);
    assertTrue(result);

    // PENDING ( visvan ) couldn't test getNavigationHandler() because
    // formID could not set as one of the parameters. Using setAttribute()
    // doesn't help because getParameter() returns null. There is no
    // setParameter method. Is there any other way to do this ??

    /* To test getNavigationHandler, create an instance of UIForm
    // navigationMap and put in objectManager.

    UIForm form_obj = new UIForm();
    form_obj.setId("basicForm");
    objectManager.put(request, "basicForm", form_obj);
    request.setAttribute(Constants.REF_UIFORMID, "basicForm");

    NavigationMapImpl navMap = new NavigationMapImpl();
    objectManager.put(request, "navMap", navMap);
    form_obj.setNavigationMapId("navMap");

    String form_id = (String) request.getParameter(Constants.REF_UIFORMID);
    System.out.println("FORMID " + form_id);

    nav = facesContext.getNavigationHandler();
    result = null != nav;
    System.out.println("Testing getNavigationHandler: " + result);
    assertTrue(result); */
    
    objectAccessor = facesContext.getObjectAccessor();
    result = null != objectAccessor; 
    System.out.println("Testing getObjectAccessor: " + result);
    assertTrue(result);
}

} // end of class TestFacesContext
