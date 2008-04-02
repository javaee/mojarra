/*
 * $Id: TestFacesContextImpl.java,v 1.39 2003/10/21 16:42:02 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestFacesContextImpl.java

package com.sun.faces.context;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.lifecycle.LifecycleImpl;

import javax.faces.application.Message;
import javax.faces.application.MessageImpl;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
/**
 *
 *  <B>TestFacesContextImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestFacesContextImpl.java,v 1.39 2003/10/21 16:42:02 eburns Exp $
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

public void testConstructor() {
    ExternalContextImpl ecImpl = 
        new ExternalContextImpl(getConfig().getServletContext(),
            getRequest(), getResponse());
    LifecycleImpl lifeImpl = new LifecycleImpl();
    try {
        FacesContextImpl fImpl = new FacesContextImpl(null, null);
	assertTrue(false);
    } catch (NullPointerException npe) {
        assertTrue(true);
    }
    try {
        FacesContextImpl fImpl = new FacesContextImpl(ecImpl, null);
	assertTrue(false);
    } catch (NullPointerException npe) {
        assertTrue(true);
    }
    try {
        FacesContextImpl fImpl = new FacesContextImpl(null, lifeImpl);
	assertTrue(false);
    } catch (NullPointerException npe) {
        assertTrue(true);
    }
    try {
        FacesContextImpl fImpl = new FacesContextImpl(ecImpl, lifeImpl);
	assertTrue(true);
    } catch (Exception e) {
        assertTrue(false);
    }
}
        
public void testAccessors() 
{
    boolean result = false;
    boolean exceptionThrown = false;
    ServletRequest req = null;
    ServletResponse resp = null;
    ServletContext sc = null;
        
    UIViewRoot page = new UIViewRoot();
    page.setViewId("viewId");
    getFacesContext().setViewRoot(page);
    UIViewRoot root = getFacesContext().getViewRoot();
    result = null != root;
    System.out.println("Testing getViewRoot: " + result);
    assertTrue(result);

    ResponseStream responseStream = new ResponseStream() {
	    public void write(int b) {}
	};
    getFacesContext().setResponseStream(responseStream);
    result = responseStream == getFacesContext().getResponseStream();
    assertTrue(result);
    System.out.println("Testing responseStream: " + result);

//PENDING(rogerk) JSF_API_20030718 - implement (ResponseWriter related mods..
    ResponseWriter responseWriter = new ResponseWriter() {
	    public void close() {}
	    public void flush() {}
	    public void write(char[] cbuf, int off, int len) {}
            public ResponseWriter cloneWithWriter(Writer writer) {return null;}
            public void writeText(char text[], int off, int len) {}
            public void writeText(char text[]) {}
            public void writeText(char text) {}
            public void writeText(Object text) {}
            public void writeComments(Object text) {}
            public void writeComment(Object text) {}
            public void writeURIAttribute(String name, Object value) {}
            public void writeAttribute(String name, Object value) {}
            public void endElement(String name) {}
            public void startElement(String name) {}
            public void endDocument() {}
            public void startDocument() {}
            public String getCharacterEncoding() {return null;}       
            public String getContentType() { return null; }
            public void startElement(String name, UIComponent componentForElement) throws IOException {}
            public void writeAttribute(String name, Object value, String componentPropertyName) throws IOException {}
            public void writeURIAttribute(String name, Object value, String componentPropertyName) throws IOException {}
            public void writeText(Object text, String componentPropertyName) throws IOException {}
	};
/*    ResponseWriter responseWriter = null;
    try {
        responseWriter = getFacesContext().getResponseWriter();
    } catch ( Exception e ) {
        assertTrue(false);
    }    
*/
    getFacesContext().setResponseWriter(responseWriter);
    result = responseWriter == getFacesContext().getResponseWriter();
    assertTrue(result);
    System.out.println("Testing responseWriter: " + result);

    // test null response writer exception //
    try {
        getFacesContext().setResponseWriter(null);
    } catch (Exception e) {
        if (-1 == e.getMessage().indexOf("esponseWriter")) {
            assertTrue(false);
        }
    }
}


public void testRenderingControls() {
    System.out.println("Testing renderResponse()");
    getFacesContext().renderResponse();
    assertTrue(getFacesContext().getRenderResponse());
    System.out.println("Testing responseComplete()");
    getFacesContext().responseComplete();
    assertTrue(getFacesContext().getResponseComplete());
}

public void testCurrentInstance() {
    System.out.println("Testing getCurrentInstance()");
    FacesContext context = getFacesContext();
    assertTrue(context == FacesContext.getCurrentInstance());
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
    fc.addMessage(command.getClientId(fc), msg3);
    
    Message msg4 = new MessageImpl (1, "summary4", "detail4");
    fc.addMessage(command.getClientId(fc), msg4);
    
    System.out.println("Testing get methods");
    assertTrue ( fc.getMaximumSeverity() == 4 );
    
    Iterator it = fc.getMessages();
    while ( it.hasNext() ) {
       Message result = (Message) it.next();
       assertTrue ( result.equals(msg1) || result.equals(msg2) || 
           result.equals(msg3) || result.equals(msg4));
    }   
    
    it = null;
    it = fc.getMessages(command.getClientId(fc));
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

public void testGetApplication() {
    FacesContext fc = getFacesContext();
    assertTrue( fc != null);

    assertTrue(null != fc.getApplication());
}    

public void testRelease() {
    System.out.println("Testing release method");
    FacesContext context = getFacesContext();
    context.release();
    boolean exceptionThrown = false;
    try {
        context.getViewRoot();
    } catch (IllegalStateException ise) {
        exceptionThrown = true;
    }
    assertTrue(exceptionThrown);
    
    exceptionThrown = false;
    try {
        context.getResponseStream();
    } catch (IllegalStateException ise) {
        exceptionThrown = true;
    }
    
    exceptionThrown = false;
    try {
        context.getResponseWriter();
    } catch (IllegalStateException ise) {
        exceptionThrown = true;
    }
    assertTrue(exceptionThrown);        
    
    // remainder of FacesContext methods are tested in TCK       
}


// Unit tests to update and retrieve values from model objects
// are in TestFacesContextImpl_Model.java
} // end of class TestFacesContextImpl
