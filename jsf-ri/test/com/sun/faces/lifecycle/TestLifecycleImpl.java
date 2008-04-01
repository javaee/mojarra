/*
 * $Id: TestLifecycleImpl.java,v 1.2 2002/06/01 00:58:23 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestLifecycleImpl.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.PhaseListener;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import java.util.Iterator;

import com.sun.faces.FacesContextTestCase;

/**
 *
 *  <B>TestLifecycleImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestLifecycleImpl.java,v 1.2 2002/06/01 00:58:23 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestLifecycleImpl extends FacesContextTestCase
{
//
// Protected Constants
//

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

    public TestLifecycleImpl() {super("TestLifecycleImpl");}
    public TestLifecycleImpl(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//

public void testApplicationHandler()
{
    ApplicationHandler app = null;

    LifecycleImpl life = new LifecycleImpl();
    boolean exceptionThrown = false;

    try {
	life.setApplicationHandler(app);
    }
    catch (FacesException e) {
	exceptionThrown = true;
    }
    assertTrue(exceptionThrown);

    exceptionThrown = false;
    try {
	life.getApplicationHandler();
    }
    catch (FacesException e) {
	exceptionThrown = true;
    }
    assertTrue(exceptionThrown);
}

public void testExtraPhases()
{
    LifecycleFactoryImpl factory = new LifecycleFactoryImpl();
    Lifecycle life = null;
    boolean exceptionThrown = false;
    String empty = "empty";
    final String beforeRender = "beforeRender";
    final String afterCreateRequest = "afterCreateRequest";
    System.setProperty(beforeRender, empty);
    System.setProperty(afterCreateRequest, empty);

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

    assertTrue(empty != System.getProperty(beforeRender));
    assertTrue(empty != System.getProperty(afterCreateRequest));
}

public void testPhaseListenersBasic()
{
    LifecycleImpl life = new LifecycleImpl();

    String empty = "empty";
    final String enteringCalled = "enteringCalled";
    final String exitingCalled = "exitingCalled";
    System.setProperty(enteringCalled, empty);
    System.setProperty(exitingCalled, empty);

    PhaseListener listener = new PhaseListener() {
	    public void entering(FacesContext context, int phaseId, 
				 Phase phase) {
		System.setProperty(enteringCalled, enteringCalled);
	    }

	    public void exiting(FacesContext context, int phaseId,
				Phase phase, int stateChange) {
		System.setProperty(exitingCalled, exitingCalled);
	    }
	};
    
    life.addPhaseListener(listener);

    life.execute(facesContext);

    // verify the listeners are called
    assertTrue(empty != System.getProperty(enteringCalled));
    assertTrue(empty != System.getProperty(exitingCalled));
    
    life.removePhaseListener(listener);

    System.setProperty(enteringCalled, empty);
    System.setProperty(exitingCalled, empty);

    life.execute(facesContext);

    // verify the listeners are not called
    assertTrue(empty == System.getProperty(enteringCalled));
    assertTrue(empty == System.getProperty(exitingCalled));
}

public void testPhaseListenersAdvanced()
{
    LifecycleFactoryImpl factory = new LifecycleFactoryImpl();
    Lifecycle life = null;

    boolean exceptionThrown = false;
    String empty = "empty";
    final String enteringCalled = "enteringCalled";
    final String exitingCalled = "exitingCalled";
    System.setProperty(enteringCalled, empty);
    System.setProperty(exitingCalled, empty);

    PhaseListener listener = new PhaseListener() {
	    public void entering(FacesContext context, int phaseId, 
				 Phase phase) {
		System.setProperty(enteringCalled, enteringCalled);
	    }

	    public void exiting(FacesContext context, int phaseId,
				Phase phase, int stateChange) {
		System.setProperty(exitingCalled, exitingCalled);
	    }
	};

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
    life = factory.createLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    assertTrue(null != life);
    
    life.addPhaseListener(listener);

    life.execute(facesContext);

    // verify the listeners are called
    assertTrue(empty != System.getProperty(enteringCalled));
    assertTrue(empty != System.getProperty(exitingCalled));
    
    life.removePhaseListener(listener);

    System.setProperty(enteringCalled, empty);
    System.setProperty(exitingCalled, empty);

    life.execute(facesContext);

    // verify the listeners are not called
    assertTrue(empty == System.getProperty(enteringCalled));
    assertTrue(empty == System.getProperty(exitingCalled));
}


} // end of class TestLifecycleImpl
