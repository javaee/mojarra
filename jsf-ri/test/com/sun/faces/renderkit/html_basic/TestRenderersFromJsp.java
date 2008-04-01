/*
 * $Id: TestRenderersFromJsp.java,v 1.6 2002/06/26 21:25:10 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderersFromJsp.java

package com.sun.faces.renderkit.html_basic;

import org.apache.cactus.WebRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.Phase;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.FormEvent;
import javax.faces.event.CommandEvent;

import java.util.Iterator;
import java.util.ArrayList;

import com.sun.faces.JspFacesTestCase;

import com.sun.faces.RIConstants;
import com.sun.faces.lifecycle.LifecycleImpl;

import javax.servlet.http.HttpServletResponse;

/**
 *
 *  <B>TestRenderersFromJsp</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderersFromJsp.java,v 1.6 2002/06/26 21:25:10 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRenderersFromJsp extends JspFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI_XUL = "/TestRenderersFromJsp.xul";
public static final String TEST_URI = "/TestRenderersFromJsp.jsp";

public String getExpectedOutputFilename() {
    return "TestRenderersFromJsp_correct";
}

public static final String ignore[] = {
    "        <FORM METHOD=\"post\" ACTION=\"/test/faces;jsessionid=D31568262822D1F579F1B4D228176342?action=form&name=basicForm&tree=/TestRenderersFromJsp.xul\">",
    "	    <!-- <a href=\"/test/faces;jsessionid=D31568262822D1F579F1B4D228176342?action=command&name=loginButton&tree=/TestRenderersFromJsp.xul\">loginButton</a> -->"
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

    public TestRenderersFromJsp() {super("TestRenderersFromJsp");}
    public TestRenderersFromJsp(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//

protected void initWebRequest(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI_XUL, null);
   // theRequest.addParameter("tree", TEST_URI_XUL);
    theRequest.addParameter(RIConstants.INITIAL_REQUEST_NAME, 
			    RIConstants.INITIAL_REQUEST_VALUE);
}

public void beginFromJsp(WebRequest theRequest)
{
    initWebRequest(theRequest);
}

public void testFromJsp()
{
    System.setProperty(RIConstants.DISABLE_RENDERERS, "");
    boolean result = false;
    LifecycleImpl life = new LifecycleImpl();

    try {
	life.execute(getFacesContext());
    }
    catch (Throwable e) {
	e.printStackTrace();
	assertTrue(e.getMessage(), false);
    }

    assertTrue(verifyExpectedOutput());
}



} // end of class TestRenderersFromJsp
