/*
 * $Id: TestFacesContextImpl.java,v 1.1 2002/05/28 18:20:40 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestFacesContextImpl.java

package com.sun.faces.context;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;
import org.apache.cactus.ServletTestCase;

import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import javax.faces.lifecycle.LifecycleFactory;
import com.sun.faces.context.FacesContextImpl;
import javax.faces.component.UICommand;
import javax.faces.component.FacesEvent;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.tree.Tree;
import javax.faces.FacesException;
import javax.faces.context.MessageList;

import java.util.Locale;
import java.util.Iterator;

/**
 *
 *  <B>TestFacesContextImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestFacesContextImpl.java,v 1.1 2002/05/28 18:20:40 jvisvanathan Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestFacesContextImpl extends ServletTestCase
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
private FacesContextImpl facesContext = null;    

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestFacesContextImpl() {super("TestFacesContext");}
    public TestFacesContextImpl(String name) {super(name);}
//
// Class methods
//

//
// Methods from TestCase
//

//
// General Methods
//
    
public void createFacesContext() {
    
    boolean gotException = false;
    try {
        facesContext = new FacesContextImpl(null,null, null, null);
    } catch ( FacesException fe) {
        gotException = true;
    }
    assertTrue(gotException);
    
    gotException = false;
    ServletContext sc = (request.getSession()).getServletContext();
    try {
        facesContext = new FacesContextImpl(sc,request, response, 
                LifecycleFactory.DEFAULT_LIFECYCLE);
        assertTrue(facesContext != null);
    } catch ( FacesException fe) {
    }
    assertTrue(facesContext != null);
}    

public void testAccessors() 
{
    boolean result = false;
    ServletRequest req = null;
    ServletResponse resp = null;
    ServletContext sc = null;

    createFacesContext();    
    req = facesContext.getServletRequest();
    result = null != req;
    System.out.println("Testing getRequest: " + result);
    assertTrue(result);

    resp = facesContext.getServletResponse();
    result = null != resp;
    System.out.println("Testing getResponse: " + result);
    assertTrue(result);

    sc = facesContext.getServletContext();
    result = null != sc;
    System.out.println("Testing getServletContext: " + result);
    assertTrue(result);
    
    // this method it not implemented yet. Make sure the result fails.
    try {
        Iterator it = facesContext.getApplicationEvents();
        result = false;
        System.out.println("Testing getApplicationEvent: " + result);
    } catch (FacesException fe) {
        result = true;    
    }    
    assertTrue(result);

    HttpSession session = facesContext.getHttpSession();
    result = null != session;
    System.out.println("Testing getHttpSession: " + result);
    assertTrue(result);
    
    // PENDING (visvan) update test case once integrated tests are
    // completed.
    Lifecycle lc = facesContext.getLifecycle();
    result = null != lc;
    System.out.println("Testing getLifeCyle: " + result);
    assertTrue(result == false);
    
    Locale locale = facesContext.getLocale();
    result = null != locale;
    System.out.println("Testing getLocale: " + result);
    assertTrue(result);
    
    MessageList ml = facesContext.getMessageList();
    result = null != ml;
    System.out.println("Testing getMessageList: " + result);
    assertTrue(result);
    
    // PENDING (visvan) update test case once integrated tests are
    // completed.
    Tree requestTree = facesContext.getRequestTree();
    result = null != requestTree;
    System.out.println("Testing getRequestTree: " + result);
    assertTrue(result == false);
    
    // PENDING (visvan) update test case once integrated tests are
    // completed.
    Tree responseTree = facesContext.getResponseTree();
    result = null != responseTree;
    System.out.println("Testing getResponseTree: " + result);
    assertTrue(result== false);
    
    // this method it not implemented yet. Make sure the result fails.
    try {
        facesContext.addApplicationEvent(new FacesEvent(new UICommand()));
        result = false;
        System.out.println("Testing addApplicationEvent: " + result);
    } catch (FacesException fe) {
        result = true;    
    }    
    assertTrue(result);
    
    // this method it not implemented yet. Make sure the result fails.
    try {
        facesContext.release();
        result = false;
        System.out.println("Testing release: " + result);
    } catch (FacesException fe) {
        result = true;    
    }    
    assertTrue(result);
    
    // Unit tests to update and retrieve values from model objects
    // are in TestFacesContextImpl_Model.java
}

} // end of class TestFacesContextImpl
