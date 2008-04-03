/*
 * $Id: TestFacesContextImpl.java,v 1.57 2008/03/03 05:34:43 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// TestFacesContextImpl.java

package com.sun.faces.context;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.lifecycle.LifecycleImpl;
import com.sun.faces.util.Util;

import javax.faces.application.FacesMessage;
import javax.faces.application.Application;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * <B>TestFacesContextImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestFacesContextImpl.java,v 1.57 2008/03/03 05:34:43 rlubke Exp $
 */

public class TestFacesContextImpl extends ServletFacesTestCase {

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

    public TestFacesContextImpl() {
        super("TestFacesContext");
    }


    public TestFacesContextImpl(String name) {
        super(name);
    }
//
// Class methods
//

//
// Methods from TestCase
//
    public void setUp() {
        super.setUp();
        UIViewRoot viewRoot = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        viewRoot.setViewId("viewId");
        viewRoot.setLocale(Locale.US);
        getFacesContext().setViewRoot(viewRoot);
    }

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


    public void testAccessors() {
        boolean result = false;
        boolean exceptionThrown = false;
        ServletRequest req = null;
        ServletResponse resp = null;
        ServletContext sc = null;

        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        page.setViewId("viewId");
        page.setLocale(Locale.US);
        getFacesContext().setViewRoot(page);
        UIViewRoot root = getFacesContext().getViewRoot();
        result = null != root;
        System.out.println("Testing getViewRoot: " + result);
        assertTrue(result);

        ResponseStream responseStream = new ResponseStream() {
            public void write(int b) {
            }
        };
        getFacesContext().setResponseStream(responseStream);
        result = responseStream == getFacesContext().getResponseStream();
        assertTrue(result);
        System.out.println("Testing responseStream: " + result);

//PENDING(rogerk) JSF_API_20030718 - implement (ResponseWriter related mods..
        ResponseWriter responseWriter = new ResponseWriter() {
            public void close() {
            }


            public void flush() {
            }


            public void write(char[] cbuf, int off, int len) {
            }


            public ResponseWriter cloneWithWriter(Writer writer) {
                return null;
            }


            public void writeText(char text[], int off, int len) {
            }


            public void writeText(char text[]) {
            }


            public void writeText(char text) {
            }


            public void writeText(Object text) {
            }


            public void writeComments(Object text) {
            }


            public void writeComment(Object text) {
            }


            public void writeURIAttribute(String name, Object value) {
            }


            public void writeAttribute(String name, Object value) {
            }


            public void endElement(String name) {
            }


            public void startElement(String name) {
            }


            public void endDocument() {
            }


            public void startDocument() {
            }


            public String getCharacterEncoding() {
                return null;
            }


            public String getContentType() {
                return null;
            }


            public void startElement(String name, UIComponent componentForElement)
                throws IOException {
            }


            public void writeAttribute(String name, Object value, String componentPropertyName)
                throws IOException {
            }


            public void writeURIAttribute(String name, Object value, String componentPropertyName)
                throws IOException {
            }


            public void writeText(Object text, String componentPropertyName)
                throws IOException {
            }
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
        assertTrue(fc != null);

        try {
            fc.addMessage(null, null);
        } catch (NullPointerException fe) {
            gotException = true;
        }
        assertTrue(gotException);
        gotException = false;

        try {
            fc.addMessage(null, null);
        } catch (NullPointerException fe) {
            gotException = true;
        }
        assertTrue(gotException);
        gotException = false;

    }


    public void testMessageMethods() {
        FacesContext fc = getFacesContext();
        assertTrue(fc != null);

        System.out.println("Testing add methods");
        FacesMessage msg1 = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                             "summary1", "detail1");
        fc.addMessage(null, msg1);

        FacesMessage msg2 = new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                             "summary2", "detail2");
        fc.addMessage(null, msg2);

        UICommand command = new UICommand();
        FacesMessage msg3 = new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                             "summary3", "detail3");
        fc.addMessage(command.getClientId(fc), msg3);

        FacesMessage msg4 = new FacesMessage(FacesMessage.SEVERITY_WARN,
                                             "summary4", "detail4");
        fc.addMessage(command.getClientId(fc), msg4);

        System.out.println("Testing get methods");
        assertTrue(fc.getMaximumSeverity() == FacesMessage.SEVERITY_FATAL);

        List controlList = new ArrayList();
        controlList.add(msg1);
        controlList.add(msg2);
        controlList.add(msg3);
        controlList.add(msg4);
        Iterator it = fc.getMessages();
        for (int i = 0, size = controlList.size(); i < size; i++) {
            assertTrue(controlList.get(i).equals(it.next()));
        }

        controlList.clear();
        controlList.add(msg3);
        controlList.add(msg4);
        it = fc.getMessages(command.getClientId(fc));
        for (int i = 0, size = controlList.size(); i < size; i++) {
            assertTrue(controlList.get(i).equals(it.next()));
        }

        controlList.clear();
        controlList.add(msg1);
        controlList.add(msg2);
        it = fc.getMessages(null);
        for (int i = 0, size = controlList.size(); i < size; i++) {
            assertTrue(controlList.get(i).equals(it.next()));
        }
        
    }

    public void testGetMaxServerity1() {
        FacesContext f = getFacesContext();
        FacesMessage msg1 = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "");
        FacesMessage msg2 = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "");

        f.addMessage(null, msg2);
        f.addMessage(null, msg1);

        assertTrue(FacesMessage.SEVERITY_WARN.equals(f.getMaximumSeverity()));
    }

     public void testGetMaxServerity2() {
        FacesContext f = getFacesContext();
        FacesMessage msg1 = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "");
        FacesMessage msg2 = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "");
        FacesMessage msg3 = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "");

        f.addMessage(null, msg2);
        f.addMessage(null, msg1);
        f.addMessage(null, msg3);

        assertTrue(FacesMessage.SEVERITY_ERROR.equals(f.getMaximumSeverity()));
    }

    public void testGetMaxSeverity3() throws Exception {
        FacesContext f = getFacesContext();
        Iterator<FacesMessage> messages = f.getMessages();
        assertTrue(!messages.hasNext());

        FacesMessage msg1 = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "");
        FacesMessage msg2 = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "");
        FacesMessage msg3 = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "");
        FacesMessage msg4 = new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "");
        f.addMessage(null, msg2);
        f.addMessage(null, msg1);
        f.addMessage(null, msg3);

        messages = f.getMessages();
        assertTrue(messages.hasNext());
        while (messages.hasNext()) {
            messages.next();
            messages.remove();
        }
        assertTrue(f.getMaximumSeverity() == null);

        f.addMessage("id1", msg1);
        f.addMessage("id3", msg1);
        f.addMessage("id3", msg3);
        f.addMessage("id3", msg1);
        f.addMessage(null, msg4);
        assertTrue(f.getMaximumSeverity() == FacesMessage.SEVERITY_FATAL);

        for (Iterator<FacesMessage> i = f.getMessages(); i.hasNext(); ) {
            FacesMessage m = i.next();
            if (m.getSeverity() == FacesMessage.SEVERITY_FATAL) {
                i.remove();
            }
        }
        assertTrue(f.getMaximumSeverity() == FacesMessage.SEVERITY_ERROR);

        for (Iterator<FacesMessage> i = f.getMessages(); i.hasNext(); ) {
            FacesMessage m = i.next();
            if (m.getSeverity() == FacesMessage.SEVERITY_ERROR) {
                i.remove();
            }
        }
        assertTrue(f.getMaximumSeverity() == FacesMessage.SEVERITY_INFO);

        for (Iterator<FacesMessage> i = f.getMessages(); i.hasNext(); ) {
            FacesMessage m = i.next();
            if (m.getSeverity() == FacesMessage.SEVERITY_INFO) {
                i.remove();
            }
        }
        assertTrue(f.getMaximumSeverity() == null);


    }


    public void testGetMessagesWithIdsIteratorRemove() throws Exception {
        FacesContext f = getFacesContext();
        Iterator<FacesMessage> messages = f.getMessages();
        assertTrue(!messages.hasNext());

        FacesMessage msg1 = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "");
        FacesMessage msg2 = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "");
        FacesMessage msg3 = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "");
        FacesMessage msg4 = new FacesMessage(FacesMessage.SEVERITY_FATAL, "", "");
        f.addMessage("id1", msg1);
        f.addMessage("id3", msg2);
        f.addMessage("id3", msg3);
        f.addMessage("id3", msg4);
        f.addMessage("id2", msg1);

        for (Iterator<String> i = f.getClientIdsWithMessages();
              i.hasNext();) {
            String id = i.next();
            if ("id3".equals(id)) {
                i.remove();
            }
        }

        assertTrue(!f.getMessages("id3").hasNext());
        assertTrue(f.getMaximumSeverity() == FacesMessage.SEVERITY_INFO);

        for (Iterator<String> i = f.getClientIdsWithMessages();
              i.hasNext();) {
            i.next();
            i.remove();
        }

        assertTrue(f.getMaximumSeverity() == null);
    }

    public void testGetMessagesCustomIteratorExceptions() throws Exception {
        // we use a custom iterator for iterating over all messages.
        // ensure the proper exceptions are thrown by next() and remove()
        FacesContext f = getFacesContext();
        f.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "", ""));

        // next should throw NoSuchElementException after the second call to next()
        Iterator i = f.getMessages();
        i.next();
        try {
            i.next();
            assertTrue(false);
        } catch (NoSuchElementException nsee) { }

        // remove should throw an IllegalStateException if called without having
        // called next()
        i = f.getMessages();
        try {
            i.remove();
            assertTrue(false);
        } catch (IllegalStateException ise) { }               

    }


    public void testGetApplication() {
        FacesContext fc = getFacesContext();
        assertTrue(fc != null);

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
    
    
    public void testGetRenderKid() {
        FacesContext orig = FacesContext.getCurrentInstance();
        FacesContext context = new FacesContextImpl(orig.getExternalContext(),
                                                    new LifecycleImpl());
        Application application = context.getApplication();
        UIViewRoot root = (UIViewRoot) 
              application.createComponent(UIViewRoot.COMPONENT_TYPE);
        
        // if no UIViewRoot then null should be returned
        assertTrue(context.getRenderKit() == null);
        
        // if UIViewRoot is present but has no RenderKitID, null
        // should be rendered
        context.setViewRoot(root);
        assertTrue(context.getRenderKit() == null);
        
        // UIViewRoot is present, and has an ID for a non existent
        // RenderKit - null should be returned
        root.setRenderKitId("nosuchkit");
        assertTrue(context.getRenderKit() == null);
        
        // UIViewRoot with valid RenderKit id should return a RenderKit
        root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
        assertTrue(context.getRenderKit() != null);
        
    }


// Unit tests to update and retrieve values from model objects
// are in TestFacesContextImpl_Model.java
} // end of class TestFacesContextImpl
