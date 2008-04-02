/*
 * $Id: TestLifecycleImpl.java,v 1.14 2003/03/12 19:53:42 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestLifecycleImpl.java

package com.sun.faces.lifecycle;

import org.apache.cactus.WebRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.FormEvent;
import javax.faces.event.FacesEvent;
import com.sun.faces.RIConstants;
import java.util.Iterator;

import com.sun.faces.JspFacesTestCase;

/**
 *
 *  <B>TestLifecycleImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestLifecycleImpl.java,v 1.14 2003/03/12 19:53:42 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestLifecycleImpl extends JspFacesTestCase
{
//
// Protected Constants
//

public static final String TEST_URI = "/components.jsp";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

protected LifecycleImpl sharedLifecycleImpl = null;

//
// Constructors and Initializers    
//

    public TestLifecycleImpl() {super("TestLifecycleImpl");}
    public TestLifecycleImpl(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//

protected LifecycleImpl getSharedLifecycleImpl()
{
    if (null == sharedLifecycleImpl) {
	sharedLifecycleImpl = new LifecycleImpl();
    }
    return sharedLifecycleImpl;
}

protected void setSharedLifecycleImpl(LifecycleImpl newLife)
{
    sharedLifecycleImpl = newLife;
}
    
protected void initWebRequest(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
}

public void testApplicationHandler()
{
    ApplicationHandler result = null, app = new ApplicationHandler() {
	    public boolean processEvent(FacesContext context, 
					FacesEvent event) 
	    {return true;} 
	}; 
    
    
    LifecycleImpl life = new LifecycleImpl();
    boolean exceptionThrown = false;

    try {
	life.setApplicationHandler(app);
    }
    catch (FacesException e) {
	exceptionThrown = true;
    }
    assertTrue(!exceptionThrown);

    exceptionThrown = false;
    try {
	result = life.getApplicationHandler();
    }
    catch (FacesException e) {
	exceptionThrown = true;
    }
    assertTrue(!exceptionThrown);
    assertTrue(result == app);
}

} // end of class TestLifecycleImpl
