/*
 * $Id: TestFacesContextImpl.java,v 1.26 2003/07/08 15:38:45 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestFacesContextImpl.java

package com.sun.faces.context;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;

import javax.faces.context.FacesContext;
import javax.faces.application.Message;
import javax.faces.application.MessageImpl;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import com.sun.faces.context.FacesContextImpl;
import com.sun.faces.tree.SimpleTreeImpl;

import javax.faces.component.UICommand;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;

import javax.faces.event.FacesEvent;
import javax.faces.tree.Tree;
import javax.faces.FacesException;
import javax.faces.context.ResponseWriter;
import javax.faces.webapp.ServletResponseWriter;
import java.io.PrintWriter;
import javax.faces.context.ResponseStream;
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
 * @version $Id: TestFacesContextImpl.java,v 1.26 2003/07/08 15:38:45 eburns Exp $
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
    
    assertTrue(((FacesContextImpl)getFacesContext()).getViewHandler() != null );
    
    Locale locale = getFacesContext().getLocale();
    result = null != locale;
    System.out.println("Testing getLocale: " + result);
    assertTrue(result);

    getFacesContext().setTree( new SimpleTreeImpl(getFacesContext(),
                new UIForm(),"treeId"));
    Tree tree = getFacesContext().getTree();
    result = null != tree;
    System.out.println("Testing getTree: " + result);
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
        ServletResponseWriter(((ServletResponse)getFacesContext().getExternalContext().
            getResponse()).getWriter());
    } catch ( Exception e ) {
        assertTrue(false);
    }    
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

public void testFacesEventsNull()
{
    boolean exceptionThrown = false;
    int count = 0;
    Iterator iter = null;

    exceptionThrown = false;
    System.out.println("Testing addFacesEvent(null) throws NullPointerException");
    try {
	getFacesContext().addFacesEvent(null);
    }
    catch (NullPointerException e) {
	exceptionThrown = true;
    }
    assertTrue(exceptionThrown);
}

public void testFacesEvents()
{
    int count = 0;
    Iterator iter = null;
    UIInput source1 = new UIInput();
    UICommand source2 = new UICommand();
    FacesEvent 
	event1 = new FacesEvent(source1),
	event2 = new FacesEvent(source2);

    System.out.println("Testing addFacesEvent(event1)");
    getFacesContext().addFacesEvent(event1);
    assertTrue((count = getCount()) == 1);

    System.out.println("Testing addFacesEvent(event2)");
    getFacesContext().addFacesEvent(event2);
    assertTrue((count = getCount()) == 2);

    System.out.println("Testing getFacesEvents()");
    iter = getFacesContext().getFacesEvents();
    assertTrue(iter.hasNext());

}

public void testRenderingControls() {
    System.out.println("Testing renderResponse()");
    getFacesContext().renderResponse();
    assertTrue(((FacesContextImpl)getFacesContext()).getRenderResponse());
    System.out.println("Testing responseComplete()");
    getFacesContext().responseComplete();
    assertTrue(((FacesContextImpl)getFacesContext()).getResponseComplete());
}

public void testCurrentInstance() {
    System.out.println("Testing getCurrentInstance()");
    FacesContext context = getFacesContext();
    assertTrue(context == context.getCurrentInstance());
}

private int getCount() {
    Iterator iter = getFacesContext().getFacesEvents();
    int count = 0;
    while (iter.hasNext()) {
        iter.next();
        count++;
    }
    return count;
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

public void testGetApplication() {
    FacesContext fc = getFacesContext();
    assertTrue( fc != null);

    assertTrue(null != fc.getApplication());
}    

public void testRelease() {
    System.out.println("Testing release method");
    getFacesContext().release();
    assertTrue(getFacesContext().getLocale() == null);
    assertTrue(getFacesContext().getTree() == null);
    assertTrue(getFacesContext().getResponseStream() == null);
    assertTrue(getFacesContext().getResponseWriter() == null);
    assertTrue(((FacesContextImpl)getFacesContext()).getViewHandler() == null);
}


// Unit tests to update and retrieve values from model objects
// are in TestFacesContextImpl_Model.java
} // end of class TestFacesContextImpl
