/*
 * $Id: TestRenderResponsePhase.java,v 1.61 2003/08/28 23:29:08 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderResponsePhase.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;
import org.apache.cactus.JspTestCase;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.base.UINamingContainerBase;
import javax.faces.component.base.UIViewRootBase;
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
 * @version $Id: TestRenderResponsePhase.java,v 1.61 2003/08/28 23:29:08 eburns Exp $
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
    "<form id=\"basicForm\" method=\"post\" action=\"/test/faces/TestRenderResponsePhase.jsp;jsessionid=DA4DB0FD65285E5A379B6C39A8F4E48E\" class=\"formClass\" title=\"basicForm\" accept=\"html,wml\">",
    "            <img id=\"basicForm.graphicImage\" src=\"/test/duke.gif;jsessionid=DA4DB0FD65285E5A379B6C39A8F4E48E\" usemap=\"#map1\" ismap> "
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
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
}

public void testHtmlBasicRenderKit()
{    
    
    
    boolean result = false;    
    String value = null;
    LifecycleImpl lifecycle = new LifecycleImpl();
    Phase renderResponse = new RenderResponsePhase(Application.getCurrentInstance());    
    UIViewRoot page = new UIViewRootBase();
    page.setId("root");
    page.setViewId(TEST_URI);
    getFacesContext().setViewRoot(page);

    renderResponse.execute(getFacesContext());
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    assertTrue(verifyExpectedOutput());
}

} // end of class TestRenderResponsePhase
