/*
 * $Id: TestLifecycleImpl.java,v 1.11 2002/10/07 22:58:01 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
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
import javax.faces.lifecycle.Phase;
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
 * @version $Id: TestLifecycleImpl.java,v 1.11 2002/10/07 22:58:01 jvisvanathan Exp $
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

public void beginExtraPhases(WebRequest theRequest)
{
    initWebRequest(theRequest);
    theRequest.addParameter("/basicForm/shipType", "nextWeek");
    theRequest.addParameter("/basicForm/appleQuantity", "4");
}

public void testExtraPhases()
{
    LifecycleFactoryImpl factory = new LifecycleFactoryImpl();
    Lifecycle life = null;
    boolean exceptionThrown = false;
    final String beforeRender = "beforeRender";
    final String afterCreateRequest = "afterCreateRequest";
    System.setProperty(beforeRender, EMPTY);
    System.setProperty(afterCreateRequest, EMPTY);

    assertTrue(factory != null);

    // try to register before create
    try {
	factory.registerBefore(LifecycleFactory.DEFAULT_LIFECYCLE,
			       RIConstants.RENDER_RESPONSE_PHASE, 
	       new GenericPhaseImpl(null, 
				    RIConstants.RENDER_RESPONSE_PHASE) {
		   public int execute(FacesContext facesContext) 
		       throws FacesException
		   {
		       // PENDING(edburns): verify the facesContext's
		       // phase is the expected phase.
		       System.setProperty(beforeRender, beforeRender);
		       return Phase.GOTO_NEXT;
		   }
		   
	       });
	factory.registerAfter(LifecycleFactory.DEFAULT_LIFECYCLE,
			      RIConstants.RECONSTITUTE_REQUEST_TREE_PHASE, 
	       new GenericPhaseImpl(null, 
				    RIConstants.RECONSTITUTE_REQUEST_TREE_PHASE) {
		   public int execute(FacesContext facesContext) 
		       throws FacesException
		   {
		       // PENDING(edburns): verify the facesContext's
		       // phase is the expected phase.
		       System.setProperty(afterCreateRequest, 
					  afterCreateRequest);
		       return Phase.GOTO_NEXT;
		   }
		   
	       });
	
    }
    catch (IllegalStateException e) {
	exceptionThrown = true;
    }
    catch (UnsupportedOperationException e) {
	exceptionThrown = true;
    }
    assertTrue(exceptionThrown);
    
    // Make sure the default instance exists
    life = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    assertTrue(null != life);

    life.execute(getFacesContext());

    assertTrue(EMPTY == System.getProperty(beforeRender));
    assertTrue(EMPTY == System.getProperty(afterCreateRequest));
}

} // end of class TestLifecycleImpl
