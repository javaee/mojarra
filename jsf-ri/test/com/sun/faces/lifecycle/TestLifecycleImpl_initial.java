/*
 * $Id: TestLifecycleImpl_initial.java,v 1.16 2003/08/29 12:29:26 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestLifecycleImpl_initial.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;


import javax.faces.lifecycle.Lifecycle;
import javax.faces.context.FacesContext;

import com.sun.faces.RIConstants;

import com.sun.faces.JspFacesTestCase;

/**
 *
 *  <B>TestLifecycleImpl_initial</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestLifecycleImpl_initial.java,v 1.16 2003/08/29 12:29:26 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
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
    "    <form id=\"helloForm\" method=\"post\" action=\"/test/faces/greeting.jsp;jsessionid=E8E8BB5494CECF4F0B35AAB328FEBEC0\">"
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
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
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
    }
    catch (Throwable e) {
	e.printStackTrace();
	assertTrue(e.getMessage(), false);
    }
    
    assertTrue(verifyExpectedOutput());

}



} // end of class TestLifecycleImpl_initial
