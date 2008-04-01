/*
 * $Id: TestRenderersFromJsp.java,v 1.2 2002/06/09 01:49:09 eburns Exp $
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
import javax.faces.lifecycle.PhaseListener;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.FormEvent;
import javax.faces.event.CommandEvent;

import java.util.Iterator;
import java.util.ArrayList;

import com.sun.faces.FacesContextTestCaseJsp;
import com.sun.faces.FileOutputResponseWrapper;
import com.sun.faces.CompareFiles;

import com.sun.faces.RIConstants;
import com.sun.faces.lifecycle.LifecycleImpl;

import javax.servlet.http.HttpServletResponse;

/**
 *
 *  <B>TestRenderersFromJsp</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderersFromJsp.java,v 1.2 2002/06/09 01:49:09 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestRenderersFromJsp extends FacesContextTestCaseJsp
{
//
// Protected Constants
//

public static final String TEST_URI_XUL = "/TestRenderersFromJsp.xul";
public static final String TEST_URI = "/TestRenderersFromJsp.jsp";

public static final String EXPECTED_OUTPUT_FILENAME = 
    FileOutputResponseWrapper.FACES_RESPONSE_ROOT + 
    "TestRenderersFromJsp_correct";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

protected FileOutputResponseWrapper fileResponse = null;

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

public HttpServletResponse getResponse()
{
    if (null == fileResponse) {
	fileResponse = new FileOutputResponseWrapper(super.getResponse());
    }
    return fileResponse;
}

protected void initWebRequest(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    theRequest.addParameter("tree", TEST_URI_XUL);
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
	life.execute(facesContext);
    }
    catch (Throwable e) {
	e.printStackTrace();
	assertTrue(e.getMessage(), false);
    }

    CompareFiles cf = new CompareFiles();
    String errorMessage = "File Comparison failed: diff -u " + 
	FileOutputResponseWrapper.FACES_RESPONSE_FILENAME + " " + 
	EXPECTED_OUTPUT_FILENAME;
    try {
	ArrayList ignoreList = new ArrayList();
	String ignore[] = {
	    "        <FORM METHOD=\"post\" ACTION=\"/test/faces;jsessionid=0875D3A320D4D041C8A492E012D9B497?action=form&name=basicForm&tree=/TestRenderersFromJsp.xul\">",
	    "	    <!-- <a href=\"/test/faces;jsessionid=0875D3A320D4D041C8A492E012D9B497?action=command&name=loginButton&tree=/TestRenderersFromJsp.xul\">loginButton</a> -->"
	};
	for (int i = 0; i < ignore.length; i++) {
	    ignoreList.add(ignore[i]);
	}

	result = 
	    cf.filesIdentical(FileOutputResponseWrapper.FACES_RESPONSE_FILENAME,
			      EXPECTED_OUTPUT_FILENAME, ignoreList);
    }
    catch (Throwable e) {
	System.out.println(e.getMessage());
	e.printStackTrace();
	assertTrue(false);
    }

    assertTrue(errorMessage, result);

}



} // end of class TestRenderersFromJsp
