/*
 * $Id: TestLifecycleImpl.java,v 1.4 2002/06/07 00:01:13 eburns Exp $
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
import javax.faces.lifecycle.PhaseListener;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.FormEvent;
import javax.faces.event.CommandEvent;

import java.util.Iterator;

import com.sun.faces.FacesContextTestCaseJsp;

/**
 *
 *  <B>TestLifecycleImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestLifecycleImpl.java,v 1.4 2002/06/07 00:01:13 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestLifecycleImpl extends FacesContextTestCaseJsp
{
//
// Protected Constants
//

public static final String TEST_URI_XUL = "/Faces_Basic.xul";
public static final String TEST_URI = "/components.jsp";
public static final String ENTERING_CALLED = "ENTERING_CALLED";
public static final String EXITING_CALLED = "EXITING_CALLED";
public static final String EMPTY = "EMPTY";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

protected LifecycleImpl sharedLifecycleImpl = null;
protected PhaseListener sharedPhaseListener = null;

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
    
protected PhaseListener getSharedPhaseListener()
{
    if (null == sharedPhaseListener) {
	sharedPhaseListener = new PhaseListener() {
		public void entering(FacesContext context, int phaseId, 
				     Phase phase) {
		    System.setProperty(ENTERING_CALLED, ENTERING_CALLED);
		}
		
		public void exiting(FacesContext context, int phaseId,
				    Phase phase, int stateChange) {
		    System.setProperty(EXITING_CALLED, EXITING_CALLED);
		}
	    };
    }
    return sharedPhaseListener;
}

protected void initWebRequest(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
    theRequest.addParameter("tree", TEST_URI_XUL);
}

public void testApplicationHandler()
{
    ApplicationHandler result = null, app = new ApplicationHandler() {
	    public void commandEvent(FacesContext context, CommandEvent event){
	    }
	    public void formEvent(FacesContext context, FormEvent event) {}
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
			       Lifecycle.RENDER_RESPONSE_PHASE, 
	       new GenericPhaseImpl(null, 
				    Lifecycle.RENDER_RESPONSE_PHASE) {
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
			      Lifecycle.CREATE_REQUEST_TREE_PHASE, 
	       new GenericPhaseImpl(null, 
				    Lifecycle.CREATE_REQUEST_TREE_PHASE) {
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
    assertTrue(!exceptionThrown);
    
    // Make sure the default instance exists
    life = factory.createLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    assertTrue(null != life);

    life.execute(facesContext);

    assertTrue(EMPTY != System.getProperty(beforeRender));
    assertTrue(EMPTY != System.getProperty(afterCreateRequest));
}


public void beginPhaseListenersBasic_1(WebRequest theRequest)
{
    initWebRequest(theRequest);
}

public void testPhaseListenersBasic_1()
{
    LifecycleImpl life = getSharedLifecycleImpl();
    PhaseListener listener = getSharedPhaseListener();

    System.setProperty(ENTERING_CALLED, EMPTY);
    System.setProperty(EXITING_CALLED, EMPTY);

    life.addPhaseListener(listener);

    life.execute(facesContext);

    // verify the listeners are called
    assertTrue(EMPTY != System.getProperty(ENTERING_CALLED));
    assertTrue(EMPTY != System.getProperty(EXITING_CALLED));
    
}

public void beginPhaseListenersBasic_2(WebRequest theRequest)
{
    initWebRequest(theRequest);
}

public void testPhaseListenersBasic_2()
{
    LifecycleImpl life = getSharedLifecycleImpl();
    PhaseListener listener = getSharedPhaseListener();

    System.setProperty(ENTERING_CALLED, EMPTY);
    System.setProperty(EXITING_CALLED, EMPTY);

    life.removePhaseListener(listener);

    life.execute(facesContext);

    // verify the listeners are not called
    assertTrue(EMPTY.equals(System.getProperty(ENTERING_CALLED)));
    assertTrue(EMPTY.equals(System.getProperty(EXITING_CALLED)));
}

public void beginPhaseListenersAdvanced_1(WebRequest theRequest)
{
    initWebRequest(theRequest);
}

public void testPhaseListenersAdvanced_1()
{
    LifecycleFactoryImpl factory = new LifecycleFactoryImpl();
    LifecycleImpl life = null;
    PhaseListener listener = getSharedPhaseListener();
    boolean exceptionThrown = false;

    System.setProperty(ENTERING_CALLED, EMPTY);
    System.setProperty(EXITING_CALLED, EMPTY);

    try {
	factory.registerAfter(LifecycleFactory.DEFAULT_LIFECYCLE,
			      Lifecycle.CREATE_REQUEST_TREE_PHASE, 
	       new GenericPhaseImpl(null, 
				    Lifecycle.CREATE_REQUEST_TREE_PHASE) {
		   public int execute(FacesContext facesContext) 
		       throws FacesException
		   {
		       return Phase.GOTO_RENDER;
		   }
	       });
    }
    catch (IllegalStateException e) {
	exceptionThrown = true;
    }
    assertTrue(!exceptionThrown);

    // Make sure the default instance exists
    life = (LifecycleImpl) 
	factory.createLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    assertTrue(null != life);
    setSharedLifecycleImpl(life);
    
    life.addPhaseListener(listener);

    life.execute(facesContext);

    // verify the listeners are called
    assertTrue(EMPTY != System.getProperty(ENTERING_CALLED));
    assertTrue(EMPTY != System.getProperty(EXITING_CALLED));
    
}

public void beginPhaseListenersAdvanced_2(WebRequest theRequest)
{
    initWebRequest(theRequest);
}

public void testPhaseListenersAdvanced_2()
{
    LifecycleImpl life = getSharedLifecycleImpl();
    PhaseListener listener = getSharedPhaseListener();

    life.removePhaseListener(listener);

    System.setProperty(ENTERING_CALLED, EMPTY);
    System.setProperty(EXITING_CALLED, EMPTY);

    life.execute(facesContext);

    // verify the listeners are not called
    assertTrue(EMPTY.equals(System.getProperty(ENTERING_CALLED)));
    assertTrue(EMPTY.equals(System.getProperty(EXITING_CALLED)));
}

} // end of class TestLifecycleImpl
