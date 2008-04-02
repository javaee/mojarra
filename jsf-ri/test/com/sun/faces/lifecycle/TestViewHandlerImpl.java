/* 
 * $Id: TestViewHandlerImpl.java,v 1.22 2003/08/27 18:52:06 eburns Exp $ 
 */ 


/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


// TestViewHandlerImpl.java 


package com.sun.faces.lifecycle; 


import org.apache.cactus.WebRequest; 
import org.apache.cactus.JspTestCase; 


import org.mozilla.util.Assert; 
import org.mozilla.util.ParameterCheck; 


import javax.faces.FacesException; 
import javax.faces.FactoryFinder; 
import javax.faces.context.FacesContext; 
import javax.faces.context.FacesContextFactory; 
import javax.faces.lifecycle.Lifecycle; 
import javax.faces.component.base.UIComponentBase;
import javax.faces.component.base.UIViewRootBase;
import javax.faces.validator.Validator; 
import javax.servlet.ServletException;

import com.sun.faces.JspFacesTestCase; 
import com.sun.faces.FileOutputResponseWrapper; 
import com.sun.faces.util.Util; 
import com.sun.faces.CompareFiles; 

import com.sun.faces.TestBean;
import com.sun.faces.application.ViewHandlerImpl;


import java.io.IOException; 


import java.util.Iterator; 
import java.util.ArrayList; 


import javax.servlet.jsp.PageContext; 


/** 
 * 
 * <B>TestViewHandlerImpl</B> is a class ... 
 * 
 * <B>Lifetime And Scope</B> <P> 
 * 
 * @version $Id: TestViewHandlerImpl.java,v 1.22 2003/08/27 18:52:06 eburns Exp $ 
 * 
 * @see Blah 
 * @see Bloo 
 * 
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
    "    <form id=\"helloForm\" method=\"post\" action=\"/test/faces/greeting.jsp;jsessionid=1424497F85FE32ABC7CB0FD02F1BE03E\">"
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
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null); 
} 


public void testRender() 
{ 
    boolean result = false; 
    UIComponentBase root = null; 
    String value = null; 
    UIViewRootBase newView = new UIViewRootBase();
    newView.setViewId(TEST_URI);
    getFacesContext().setViewRoot(newView);

    try { 
        ViewHandlerImpl viewHandler = new ViewHandlerImpl(); 
        viewHandler.renderView(getFacesContext()); 
    } catch (IOException e) { 
        System.out.println("ViewHandler IOException:"+e); 
    } catch (FacesException fe) { 
        System.out.println("ViewHandler FacesException: "+fe); 
    }

    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    assertTrue(verifyExpectedOutput()); 
} 


} // end of class TestViewHandlerImpl 
