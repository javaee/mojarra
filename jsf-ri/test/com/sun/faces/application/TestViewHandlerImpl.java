/* 
 * $Id: TestViewHandlerImpl.java,v 1.5 2003/10/13 18:08:51 rlubke Exp $ 
 */ 


/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


// TestViewHandlerImpl.java 


package com.sun.faces.application; 


import com.sun.faces.JspFacesTestCase;
import com.sun.faces.context.ExternalContextImpl;
import com.sun.faces.context.FacesContextImpl;
import org.apache.cactus.WebRequest;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map; 


/** 
 * 
 * <B>TestViewHandlerImpl</B> is a class ... 
 * 
 * <B>Lifetime And Scope</B> <P> 
 * 
 * @version $Id: TestViewHandlerImpl.java,v 1.5 2003/10/13 18:08:51 rlubke Exp $  
 */ 


public class TestViewHandlerImpl extends JspFacesTestCase 
{ 
// 
// Protected Constants 
// 


public static final String TEST_URI = "/greeting.jsp"; 

public String getExpectedOutputFilename() { 
    return "TestViewHandlerImpl_correct"; 
} 


public static final String ignore[] = {
    "    <form id=\"helloForm\" method=\"post\" action=\"/test/faces/greeting.jsp;jsessionid=99D4FBE19D2B3AB5804FB24B9D0DBCF7\">"
};
     
public String [] getLinesToIgnore() { 
    return ignore; 
} 


public boolean sendResponseToFile() 
{ 
    return true; 
} 


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


    public TestViewHandlerImpl() { 
        super("TestViewHandlerImpl"); 
    } 


    public TestViewHandlerImpl(String name) { 
        super(name); 
    } 


// 
// Class methods 
// 


// 
// General Methods 
// 



public void beginRender(WebRequest theRequest) 
{ 
    theRequest.setURL("localhost:8080", "/test", "/faces", TEST_URI, null); 
}
    
    public void beginRender2(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", "/test", "/somepath/greeting.jsf", null, null);
    }
    
   public void testGetViewIdPath() {
       
       LifecycleFactory factory = (LifecycleFactory) 
           FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
       Lifecycle lifecycle = 
           factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
       
       // wrap the request so we can control the return value of getServletPath
       TestRequest testRequest = new TestRequest(request);       
       
       ExternalContext extContext = new ExternalContextImpl(config.getServletContext(),
                                                         testRequest,
                                                         response);
       FacesContext facesContext = new FacesContextImpl(extContext, lifecycle);
       
       // Spoof the mappings so we can properly test the different possible
       // values
       List mappings = new ArrayList();
       // create the same "massaged" mappings that WebXMLParser creates.
       mappings.add("/faces");
       mappings.add(".jsf");
       mappings.add("/*");
       ViewHandlerImpl handler = new ViewHandlerImpl();
       handler.setFacesMapping(mappings);
       
       
       // if getServletPath() returns "" then the viewId path returned should
       // be the same as what was passed.
       testRequest.setServletPath("");
       testRequest.setAttribute("com.sun.faces.INVOCATION_PATH", null);
       String path = handler.getViewIdPath(facesContext, "/test.jsp");
       System.out.println("VIEW ID PATH 1: " + path);
       assertTrue(path.equals("/test.jsp"));
       
       // if getServletPath() returns a path prefix, then the viewId path
       // returned must have that path prefixed.
       testRequest.setServletPath("/faces");
       testRequest.setPathInfo("/path/test.jsp");
       testRequest.setAttribute("com.sun.faces.INVOCATION_PATH", null);
       path = handler.getViewIdPath(facesContext, "/path/test.jsp");
       System.out.println("VIEW ID PATH 2: " + path);
       assertTrue(path.equals("/faces/path/test.jsp"));
       
       
       // if getServletPath() returns a path indicating extension mapping
       // and the viewId passed has no extension, append the extension
       // to the provided viewId
       testRequest.setServletPath("/path/firstRequest.jsf");
       testRequest.setPathInfo(null);
       testRequest.setAttribute("com.sun.faces.INVOCATION_PATH", null);
       path = handler.getViewIdPath(facesContext, "/path/test");
       System.out.println("VIEW ID PATH 3: " + path);
       assertTrue(path.equals("/path/test.jsf"));
       
       // if getServletPath() returns a path indicating extension mapping
       // and the viewId passed has an extension, replace the extension with
       // the extension defined in the deployment descriptor
       testRequest.setServletPath("/path/firstRequest.jsf");
       testRequest.setPathInfo(null);
       testRequest.setAttribute("com.sun.faces.INVOCATION_PATH", null);
       path = handler.getViewIdPath(facesContext, "/path/t.est.jsp");
       System.out.println("VIEW ID PATH 4: " + path);
       assertTrue(path.equals("/path/t.est.jsf"));
       
       // if path info is null, the impl must check to see if 
       // there is an exact match on the servlet path, if so, return
       // the servlet path
       testRequest.setServletPath("/faces");
       testRequest.setPathInfo(null);
       testRequest.setAttribute("com.sun.faces.INVOCATION_PATH", null);
       path = handler.getViewIdPath(facesContext, "/path/t.est");
       System.out.println("VIEW ID PATH 5: " + path);
       assertTrue(path.equals("/faces/path/t.est"));
                     
    }
    
    public void testGetViewIdExceptionsTest() throws Exception {
        boolean exceptionThrown = false;
        ViewHandler handler = 
            getFacesContext().getApplication().getViewHandler();
        try {
            handler.getViewIdPath(null, "/test.jsp");            
        } catch (NullPointerException npe) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        exceptionThrown = false;
        try {
            handler.getViewIdPath(getFacesContext(), null);
        } catch (NullPointerException npe) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        exceptionThrown = false;        
        try {
            handler.getViewIdPath(getFacesContext(), "test.jsp");            
        } catch (IllegalArgumentException iae) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);                
    }


    
public void testRender() 
{     
    UIViewRoot newView = new UIViewRoot();
    newView.setViewId(TEST_URI);
    getFacesContext().setViewRoot(newView);

    try { 
        ViewHandler viewHandler = 
            getFacesContext().getApplication().getViewHandler(); 
        viewHandler.renderView(getFacesContext(), 
			       getFacesContext().getViewRoot()); 
    } catch (IOException e) { 
        System.out.println("ViewHandler IOException:"+e); 
    } catch (FacesException fe) { 
        System.out.println("ViewHandler FacesException: "+fe); 
    }

    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    assertTrue(verifyExpectedOutput());        
} 
    
    public void testRender2() {
        // Change the viewID to end with .jsf and make sure that
        // the implementation changes .jsf to .jsp and properly dispatches
        // the message.
        UIViewRoot newView = new UIViewRoot();
        newView.setViewId(TEST_URI);
        getFacesContext().setViewRoot(newView); 
        
        newView.setViewId("/faces/greeting.jsf");
        getFacesContext().setViewRoot(newView);
        try {
            ViewHandler viewHandler =
                getFacesContext().getApplication().getViewHandler();
            viewHandler.renderView(getFacesContext(),
                                   getFacesContext().getViewRoot());
        } catch (IOException ioe) {
            System.out.println("ViewHandler IOException: " + ioe);
        } catch (FacesException fe) {
            System.out.println("ViewHandler FacesException: " + fe);
        }
        
        assertTrue(!(getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        assertTrue(verifyExpectedOutput()); 
    }
   
public void testTransient()
{
    
    // precreate tree and set it in session and make sure the tree is
    // restored from session.
    getFacesContext().setViewRoot(null);
    UIViewRoot root = new UIViewRoot();
    root.setViewId(TEST_URI);
    
    UIForm basicForm = new UIForm();
    basicForm.setId("basicForm");
    UIInput userName = new UIInput();
    
    userName.setId("userName");
    userName.setTransient(true);
    root.getChildren().add(basicForm);
    basicForm.getChildren().add(userName);
    
    UIPanel panel1 = new UIPanel();
    panel1.setId("panel1");
    basicForm.getChildren().add(panel1);
    
    UIInput userName1 = new UIInput();
    userName1.setId("userName1");
    userName1.setTransient(true);
    panel1.getChildren().add(userName1);
    
    UIInput userName2 = new UIInput();
    userName2.setId("userName2");
    panel1.getChildren().add(userName2);
    
    UIInput userName3 = new UIInput();
    userName3.setTransient(true);
    panel1.getFacets().put("userName3", userName3);
    
    UIInput userName4 = new UIInput();
    panel1.getFacets().put("userName4",userName4);
    
    HttpSession session = (HttpSession) 
        getFacesContext().getExternalContext().getSession(false);
    session.setAttribute(TEST_URI, root);
    
    getFacesContext().setViewRoot(root);

    ViewHandler viewHandler = 
        getFacesContext().getApplication().getViewHandler(); 
    viewHandler.getStateManager().saveSerializedView(getFacesContext());
   
    // make sure that the transient property is not persisted.
    basicForm = (UIForm)(getFacesContext().getViewRoot()).findComponent("basicForm");
    assertTrue(basicForm != null);
    
    userName = (UIInput)basicForm.findComponent("userName");
    assertTrue(userName == null);
    
    panel1 = (UIPanel)basicForm.findComponent("panel1");
    assertTrue(panel1 != null);
    
    userName1 = (UIInput)panel1.findComponent("userName1");
    assertTrue(userName1 == null);
    
    userName2 = (UIInput)panel1.findComponent("userName2");
    assertTrue(userName2 != null);
    
    // make sure facets work correctly when marked transient.
    Map facetList = panel1.getFacets();
    assertTrue(!(facetList.containsKey("userName3")));
    assertTrue(facetList.containsKey("userName4"));
}        

    private class TestRequest extends HttpServletRequestWrapper {
        
        String servletPath;
        String pathInfo;
        
        public TestRequest(HttpServletRequest request) {
            super(request);
        }

        public String getServletPath() {
            return servletPath;
        }
        
        public void setServletPath(String servletPath) {
            this.servletPath = servletPath;            
        }
        
        public String getPathInfo() {
            return pathInfo;
        }
        
        public void setPathInfo(String pathInfo) {
            this.pathInfo = pathInfo;
        }
    }
    
} // end of class TestViewHandlerImpl 
