/*
 * $Id: TestFacesContextImpl.java,v 1.5 2002/06/12 18:24:06 rkitain Exp $
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
import com.sun.faces.tree.XmlTreeImpl;

import javax.faces.component.UICommand;
import javax.faces.component.UIForm;

import javax.faces.event.FacesEvent;
import javax.faces.event.CommandEvent;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.tree.Tree;
import javax.faces.FacesException;
import javax.faces.context.MessageList;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseStream;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;
import com.sun.faces.RIConstants;
import javax.faces.render.RenderKit;

import java.util.Locale;
import java.util.Iterator;

/**
 *
 *  <B>TestFacesContextImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestFacesContextImpl.java,v 1.5 2002/06/12 18:24:06 rkitain Exp $
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
    
public void setUp() {
    
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
    
    RenderKit renderKit = new HtmlBasicRenderKit();
    sc.setAttribute(RIConstants.DEFAULT_RENDER_KIT, renderKit);
}    

public void tearDown() {
    config.getServletContext().removeAttribute(RIConstants.DEFAULT_RENDER_KIT);
}    

public void testAccessors() 
{
    boolean result = false;
    ServletRequest req = null;
    ServletResponse resp = null;
    ServletContext sc = null;
    
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
    
    
    HttpSession session = facesContext.getHttpSession();
    result = null != session;
    System.out.println("Testing getHttpSession: " + result);
    assertTrue(result);
    
    Lifecycle lc = facesContext.getLifecycle();
    result = null != lc;
    System.out.println("Testing getLifeCyle: " + result);
    assertTrue(result);
    
    Locale locale = facesContext.getLocale();
    result = null != locale;
    System.out.println("Testing getLocale: " + result);
    assertTrue(result);
    
    MessageList ml = facesContext.getMessageList();
    result = null != ml;
    System.out.println("Testing getMessageList: " + result);
    assertTrue(result);
    
    facesContext.setRequestTree( new XmlTreeImpl(config.getServletContext(),
                new UIForm(),"treeId"));
    Tree requestTree = facesContext.getRequestTree();
    result = null != requestTree;
    System.out.println("Testing getRequestTree: " + result);
    assertTrue(result);

    boolean exceptionThrown = false; 
    System.out.println("Testing setRequestTree IllegalStateException..."); 
    try {
        facesContext.setRequestTree( new XmlTreeImpl(config.getServletContext(),
            new UIForm(),"treeId"));
    } catch (IllegalStateException e) {
        exceptionThrown = true;
    }
    assertTrue(exceptionThrown);

    Tree responseTree = facesContext.getResponseTree();
    result = null != responseTree;
    System.out.println("Testing getResponseTree: " + result);
    assertTrue(result);
    
    facesContext.addApplicationEvent(new CommandEvent(new UICommand(), 
            "cmdName"));
    System.out.println("Testing addApplicationEvent: " );

    ResponseStream responseStream = new ResponseStream() {
	    public void write(int b) {}
	};
    facesContext.setResponseStream(responseStream);
    result = responseStream == facesContext.getResponseStream();
    assertTrue(result);
    System.out.println("Testing responseStream: " + result);

    ResponseWriter responseWriter = new ResponseWriter() {
	    public void close() {}
	    public void flush() {}
	    public void write(char[] cbuf, int off, int len) {}
	};
    facesContext.setResponseWriter(responseWriter);
    result = responseWriter == facesContext.getResponseWriter();
    assertTrue(result);
    System.out.println("Testing responseWriter: " + result);
   
    Iterator it = facesContext.getApplicationEvents();
    result = null != it;
    System.out.println("Testing getApplicationEvent: " + result);
    assertTrue(result);
    
    FacesEvent event = (FacesEvent) it.next();
    assertTrue(event instanceof CommandEvent);
    
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
