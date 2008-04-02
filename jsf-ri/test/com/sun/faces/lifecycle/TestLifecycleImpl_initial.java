/*
 * $Id: TestLifecycleImpl_initial.java,v 1.21 2004/01/16 21:31:07 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestLifecycleImpl_initial.java

package com.sun.faces.lifecycle;

import com.sun.faces.JspFacesTestCase;
import org.apache.cactus.WebRequest;

import javax.faces.FacesException;

/**
 *
 *  <B>TestLifecycleImpl_initial</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestLifecycleImpl_initial.java,v 1.21 2004/01/16 21:31:07 craigmcc Exp $ 
 */

public class TestLifecycleImpl_initial extends JspFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI = "/greeting.jsp";

public String getExpectedOutputFilename() {
    return "TestLifecycleImpl_initial_correct";
}

public static final String ignore[] = {
    "    <form id=\"helloForm\" method=\"post\" action=\"/test/faces/greeting.jsp\">"
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

    public TestLifecycleImpl_initial() {super("TestLifecycleImpl_initial");}
    public TestLifecycleImpl_initial(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//


protected void initWebRequest(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", "/test", "/faces", TEST_URI, null);
}

public void beginExecuteInitial(WebRequest theRequest)
{
    initWebRequest(theRequest);
}

public void testExecuteInitial()
{
    boolean result = false;
    LifecycleImpl life = new LifecycleImpl();

    try {
	life.execute(getFacesContext());
	life.render(getFacesContext());
    }
    catch (FacesException e) {
	System.err.println("Root Cause: " + e.getCause());
	if (null != e.getCause()) {
	    e.getCause().printStackTrace();
	}
	else {
	    e.printStackTrace();
	}
	
	assertTrue(e.getMessage(), false);
    }
    
    assertTrue(verifyExpectedOutput());

}



} // end of class TestLifecycleImpl_initial
