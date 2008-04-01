/*
 * $Id: TestFacesContextImpl.java,v 1.14 2002/08/08 00:46:17 eburns Exp $
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

import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.MessageImpl;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import com.sun.faces.context.FacesContextImpl;
import com.sun.faces.tree.XmlTreeImpl;

import javax.faces.component.UICommand;
import javax.faces.component.UIForm;

import javax.faces.event.FacesEvent;
import javax.faces.event.CommandEvent;
import javax.faces.event.FormEvent;
import javax.faces.tree.Tree;
import javax.faces.FacesException;
import javax.faces.context.ResponseWriter;
import javax.faces.webapp.ServletResponseWriter;
import java.io.PrintWriter;
import javax.faces.context.ResponseStream;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;
import com.sun.faces.RIConstants;
import javax.faces.render.RenderKit;
import java.util.Collections;
import java.util.Locale;
import java.util.Iterator;

import com.sun.faces.ServletFacesTestCase;

/**
 *
 *  <B>TestFacesContextImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestFacesContextImpl.java,v 1.14 2002/08/08 00:46:17 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestFacesContextImpl extends ServletFacesTestCase
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

public void testAccessors() 
{
    boolean result = false;
    boolean exceptionThrown = false;
    ServletRequest req = null;
    ServletResponse resp = null;
    ServletContext sc = null;
    
    req = getFacesContext().getServletRequest();
    result = null != req;
    System.out.println("Testing getRequest: " + result);
    assertTrue(result);

    resp = getFacesContext().getServletResponse();
    result = null != resp;
    System.out.println("Testing getResponse: " + result);
    assertTrue(result);

    sc = getFacesContext().getServletContext();
    result = null != sc;
    System.out.println("Testing getServletContext: " + result);
    assertTrue(result);
    
    
    HttpSession session = getFacesContext().getHttpSession();
    result = null != session;
    System.out.println("Testing getHttpSession: " + result);
    assertTrue(result);
    
    assertTrue(getFacesContext().getApplicationHandler() == null);
    assertTrue(getFacesContext().getViewHandler() != null );
    
    Locale locale = getFacesContext().getLocale();
    result = null != locale;
    System.out.println("Testing getLocale: " + result);
    assertTrue(result);

    getFacesContext().setRequestTree( new XmlTreeImpl(config.getServletContext(),
                new UIForm(),"treeId", ""));
    Tree requestTree = getFacesContext().getRequestTree();
    result = null != requestTree;
    System.out.println("Testing getRequestTree: " + result);
    assertTrue(result);

    exceptionThrown = false; 
    System.out.println("Testing setRequestTree IllegalStateException..."); 
    try {
        getFacesContext().setRequestTree( new XmlTreeImpl(config.getServletContext(),
            new UIForm(),"treeId", ""));
    } catch (IllegalStateException e) {
        exceptionThrown = true;
    }
    assertTrue(exceptionThrown);

    Tree responseTree = getFacesContext().getResponseTree();
    result = null != responseTree;
    System.out.println("Testing getResponseTree: " + result);
    assertTrue(result);
 
    ResponseStream responseStream = new ResponseStream() {
	    public void write(int b) {}
	};
    getFacesContext().setResponseStream(responseStream);
    result = responseStream == getFacesContext().getResponseStream();
    assertTrue(result);
    System.out.println("Testing responseStream: " + result);

    /*ResponseWriter responseWriter = new ResponseWriter() {
	    public void close() {}
	    public void flush() {}
	    public void write(char[] cbuf, int off, int len) {}
          
	}; */
    ResponseWriter responseWriter = null;
    try {
        responseWriter = new 
        ServletResponseWriter((getFacesContext().getServletResponse()).getWriter());
    } catch ( Exception e ) {
        assertTrue(false);
    }    
    getFacesContext().setResponseWriter(responseWriter);
    result = responseWriter == getFacesContext().getResponseWriter();
    assertTrue(result);
    System.out.println("Testing responseWriter: " + result);
}

public void testApplicationEvents() {
    
    assertTrue(getFacesContext().getApplicationEventsCount() == 0);
    
    boolean gotException = false;
    try {
        getFacesContext().addApplicationEvent(null);
    } catch (Exception e ) {
        gotException = true;
    }
    assertTrue(gotException);
    
    System.out.println("Testing addApplicationEvent: " );
    getFacesContext().addApplicationEvent(new CommandEvent(new UICommand(), 
            "cmdName"));
    getFacesContext().addApplicationEvent(new FormEvent(new UIForm(), "basicForm", 
            "cmdName"));
    
    System.out.println("Testing getApplicationEventCount: " );
    int size = getFacesContext().getApplicationEventsCount();
    assertTrue (size ==2 );
    
    Iterator it = getFacesContext().getApplicationEvents();
    FacesEvent event = (FacesEvent) it.next();
    assertTrue(event instanceof CommandEvent);
    
    event = (FacesEvent) it.next();
    assertTrue(event instanceof FormEvent);
}

public void testRequestEventsNull()
{
    boolean exceptionThrown = false;
    int count = 0;
    Iterator iter = null;

    System.out.println("Testing getRequestEventsCount() == 0");
    count =  getFacesContext().getRequestEventsCount();
    assertTrue(0 == count);
    
    System.out.println("Testing getRequestEventsCount(null) throws NullPointerException");
    try {
	count = getFacesContext().getRequestEventsCount(null);
    }
    catch (NullPointerException e) {
	exceptionThrown = true;
    }
    assertTrue(exceptionThrown);
    
    exceptionThrown = false;
    System.out.println("Testing getRequestEvents(null) throws NullPointerException");
    try {
	iter = getFacesContext().getRequestEvents(null);
    }
    catch (NullPointerException e) {
	exceptionThrown = true;
    }
    assertTrue(exceptionThrown);

    exceptionThrown = false;
    System.out.println("Testing addRequestEvent(null, null) throws NullPointerException");
    try {
	getFacesContext().addRequestEvent(null, null);
    }
    catch (NullPointerException e) {
	exceptionThrown = true;
    }
    assertTrue(exceptionThrown);
}

public void testRequestEvents()
{
    int count = 0;
    Iterator iter = null;
    UIForm 
	source1 = new UIForm(),
	source2 = new UIForm();
    FacesEvent 
	testEvent = null,
	event1 = new FacesEvent(source1),
	event2 = new FacesEvent(source2);


    System.out.println("Testing addRequestEvent(source1, event1)");
    getFacesContext().addRequestEvent(source1, event1);
    count = getFacesContext().getRequestEventsCount(source1);
    assertTrue(1 == count);

    count = getFacesContext().getRequestEventsCount(source2);
    assertTrue(0 == count);

    System.out.println("Testing addRequestEvent(source2, event2)");
    getFacesContext().addRequestEvent(source2, event2);
    count = getFacesContext().getRequestEventsCount(source1);
    assertTrue(1 == count);

    count = getFacesContext().getRequestEventsCount(source2);
    assertTrue(1 == count);

    count = getFacesContext().getRequestEventsCount();
    assertTrue(2 == count);

    System.out.println("Testing getRequestEvents(source1)");
    iter = getFacesContext().getRequestEvents(source1);
    assertTrue(iter.hasNext());

    while (iter.hasNext()) {
	testEvent = (FacesEvent) iter.next();
	assertTrue(testEvent == event1);
	assertTrue(testEvent.getComponent() == source1);
    }

    System.out.println("Testing getRequestEvents(source2)");
    iter = getFacesContext().getRequestEvents(source2);
    assertTrue(iter.hasNext());

    while (iter.hasNext()) {
	testEvent = (FacesEvent) iter.next();
	assertTrue(testEvent == event2);
	assertTrue(testEvent.getComponent() == source2);
    }

}

public void testMessageMethodsNull() {
    boolean gotException = false;
    
    FacesContext fc = getFacesContext();
    assertTrue( fc != null);
    
    try {
        fc.addMessage(null,null);
    } catch ( NullPointerException fe) {
        gotException = true;
    }
    assertTrue(gotException);
    gotException = false;
    
    try {
        fc.addMessage(null, null);
    } catch ( NullPointerException fe) {
        gotException = true;
    }
    assertTrue(gotException);
    gotException = false;
    
}

public void testMessageMethods() {
    FacesContext fc = getFacesContext();
    assertTrue( fc != null);
    
    System.out.println("Testing add methods");
    Message msg1 = new MessageImpl (2, "summary1", "detail1");
    fc.addMessage(null, msg1);
    
    Message msg2 = new MessageImpl (3, "summary2", "detail2");
    fc.addMessage(null, msg2);
    
    UICommand command = new UICommand();
    Message msg3 = new MessageImpl (4, "summary3", "detail3");
    fc.addMessage(command, msg3);
    
    Message msg4 = new MessageImpl (1, "summary4", "detail4");
    fc.addMessage(command, msg4);
    
    System.out.println("Testing get methods");
    assertTrue ( fc.getMaximumSeverity() == 4 );
    
    Iterator it = fc.getMessages();
    while ( it.hasNext() ) {
       Message result = (Message) it.next();
       assertTrue ( result.equals(msg1) || result.equals(msg2) || 
           result.equals(msg3) || result.equals(msg4));
    }   
    
    it = null;
    it = fc.getMessages(command);
    while ( it.hasNext() ) {
       Message result = (Message) it.next();
       assertTrue (result.equals(msg3) || result.equals(msg4));
    }
    
    it = null;
    it = fc.getMessages(null);
    while ( it.hasNext() ) {
       Message result = (Message) it.next();
       //System.out.println("summary " + result.getSummary());
       assertTrue ( result.equals(msg1) || result.equals(msg2));
    }
}    

public void testRelease() {
    System.out.println("Testing release method");
    getFacesContext().release();
    
    assertTrue(getFacesContext().getServletContext() == null);
    assertTrue(getFacesContext().getServletRequest() == null);
    assertTrue(getFacesContext().getServletResponse() == null);
    assertTrue(getFacesContext().getHttpSession() == null);
    assertTrue(getFacesContext().getLocale() == null);
    assertTrue(getFacesContext().getRequestTree() == null);
    assertTrue(getFacesContext().getResponseTree() == null);
    assertTrue(getFacesContext().getResponseStream() == null);
    assertTrue(getFacesContext().getResponseWriter() == null);
    Iterator it = getFacesContext().getApplicationEvents();
    assertTrue( !it.hasNext());
    assertTrue(getFacesContext().getRequestEventsCount() == 0);
    assertTrue(getFacesContext().getViewHandler() == null);
    assertTrue(getFacesContext().getApplicationHandler() == null);
}
// Unit tests to update and retrieve values from model objects
// are in TestFacesContextImpl_Model.java
} // end of class TestFacesContextImpl
