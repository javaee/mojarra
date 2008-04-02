/*
 * $Id: TestRenderResponsePhase.java,v 1.74 2003/12/17 15:15:27 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderResponsePhase.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;
import org.apache.cactus.JspTestCase;

import com.sun.faces.util.Util;


import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIViewRoot;
import javax.faces.validator.Validator;

import com.sun.faces.lifecycle.Phase;
import com.sun.faces.JspFacesTestCase;
import com.sun.faces.FileOutputResponseWrapper;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;
import com.sun.faces.CompareFiles;

import com.sun.faces.TestBean;

import java.io.IOException;

import java.util.Iterator;
import java.util.ArrayList;


import javax.servlet.jsp.PageContext;

/**
 *
 *  <B>TestRenderResponsePhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderResponsePhase.java,v 1.74 2003/12/17 15:15:27 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRenderResponsePhase extends JspFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI = "/TestRenderResponsePhase.jsp";

public String getExpectedOutputFilename() {
    return "RenderResponse_correct";
}

public static final String ignore[] = {
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

    public TestRenderResponsePhase() {
	super("TestRenderResponsePhase");
    }

    public TestRenderResponsePhase(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//


public void beginHtmlBasicRenderKit(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", "/test", "/faces", TEST_URI, null);
}

public void testHtmlBasicRenderKit()
{    
    
    
    boolean result = false;    
    String value = null;
    LifecycleImpl lifecycle = new LifecycleImpl();
    Phase renderResponse = new RenderResponsePhase();    
    UIViewRoot page = new UIViewRoot();
    page.setId("root");
    page.setViewId(TEST_URI);
    getFacesContext().setViewRoot(page);
    
    try {
	renderResponse.execute(getFacesContext());
    }
    catch (FacesException fe) {
	System.out.println(fe.getMessage());
	if (null != fe.getCause()) {
	    fe.getCause().printStackTrace();
	}
	else {
	    fe.printStackTrace();
	}
    }
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    assertTrue(verifyExpectedOutput());
}

} // end of class TestRenderResponsePhase
