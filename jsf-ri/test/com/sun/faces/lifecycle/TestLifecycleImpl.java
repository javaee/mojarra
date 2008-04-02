/*
 * $Id: TestLifecycleImpl.java,v 1.18 2003/08/28 21:39:22 eburns Exp $
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

import javax.servlet.http.HttpSession;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.component.UIViewRoot;
import javax.faces.component.base.UIViewRootBase;
import javax.faces.component.base.UIInputBase;
import javax.faces.component.base.UIFormBase;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;
import java.util.Iterator;

import com.sun.faces.JspFacesTestCase;

/**
 *
 *  <B>TestLifecycleImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestLifecycleImpl.java,v 1.18 2003/08/28 21:39:22 eburns Exp $
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

public static final String TEST_URI = "/TestLifecycleImpl.html";

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

protected static LifecycleImpl sharedLifecycleImpl = null;
protected static PhaseListenerImpl sharedListener = null;

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

protected PhaseListenerImpl getSharedPhaseListenerImpl() {
    return sharedListener;
}
    
protected void initWebRequest(WebRequest theRequest)
{
    theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
}

public void setUp() {
    RIConstants.IS_UNIT_TEST_MODE = true;
    super.setUp();
    UIViewRoot root = new UIViewRootBase();
    root.setViewId(TEST_URI);
    
    UIFormBase basicForm = new UIFormBase();
    basicForm.setId("basicForm");
    UIInputBase userName = new UIInputBase();
    
    userName.setId("userName");
    root.getChildren().add(basicForm);
    basicForm.getChildren().add(userName);
    
    HttpSession session = (HttpSession) 
        getFacesContext().getExternalContext().getSession(false);
    session.setAttribute(TEST_URI, root);
}

public void beginAnyPhaseWithListenerAndValidationFailure(WebRequest theRequest) {
    initWebRequest(theRequest);
}

public void testAnyPhaseWithListenerAndValidationFailure() {
    LifecycleImpl life = getSharedLifecycleImpl();
    final int [] phaseCalled = new 
	int[LifecycleFactoryImpl.LAST_PHASE + 1];
    int i;
    for (i = 1; i < phaseCalled.length; i++) {
	phaseCalled[i] = 0;
    }

    sharedListener = new PhaseListenerImpl(phaseCalled,  PhaseId.ANY_PHASE, 
					   PhaseId.PROCESS_VALIDATIONS);
    life.addPhaseListener(sharedListener);

    try {
	life.execute(getFacesContext());
    }
    catch (Throwable e) {
	e.printStackTrace();
	assertTrue(e.getMessage(), false);
    }
    
    for (i = 1; i < phaseCalled.length; i++) {
	// i is restore_view, apply_request, process_val, or render_resp
	if (((1 <= i) && (i <= 3)) || (i == 6)) {
	    assertTrue("Expected 2 for phase " + i + ", got " + phaseCalled[i] + ".",
		       
		       phaseCalled[i] == 2);
	}
	else {
	    assertTrue(phaseCalled[i] == 0);
	}
    }
}

public void beginAnyPhaseWithListener(WebRequest theRequest) {
    initWebRequest(theRequest);
}

public void testAnyPhaseWithListener() {
    LifecycleImpl life = getSharedLifecycleImpl();
    final int [] phaseCalled = new 
	int[LifecycleFactoryImpl.LAST_PHASE + 1];
    int i;
    for (i = 1; i < phaseCalled.length; i++) {
	phaseCalled[i] = 0;
    }

    life.removePhaseListener(sharedListener);
    sharedListener = new PhaseListenerImpl(phaseCalled,  PhaseId.ANY_PHASE,
					   null);
    life.addPhaseListener(sharedListener);

    try {
	life.execute(getFacesContext());
    }
    catch (Throwable e) {
	e.printStackTrace();
	assertTrue(e.getMessage(), false);
    }
    
    for (i = 1; i < phaseCalled.length; i++) {
	assertTrue(phaseCalled[i] == 2);
    }
}

public void beginAnyPhaseWithoutListener(WebRequest theRequest) {
    initWebRequest(theRequest);
}

public void testAnyPhaseWithoutListener() {
    assertTrue(null != sharedListener);

    LifecycleImpl life = getSharedLifecycleImpl();
    final int [] phaseCalled = sharedListener.getPhaseCalled();
    int i;

    life.removePhaseListener(sharedListener);

    try {
	life.execute(getFacesContext());
    }
    catch (Throwable e) {
	e.printStackTrace();
	assertTrue(e.getMessage(), false);
    }
    
    // make sure the listener wasn't called
    for (i = 1; i < phaseCalled.length; i++) {
	assertTrue(phaseCalled[i] == 2);
    }

}

public void beginValidateWithListener(WebRequest theRequest) {
    initWebRequest(theRequest);
}

public void testValidateWithListener() {
    LifecycleImpl life = getSharedLifecycleImpl();
    final int [] phaseCalled = new 
	int[LifecycleFactoryImpl.LAST_PHASE + 1];
    int i;
    for (i = 1; i < phaseCalled.length; i++) {
	phaseCalled[i] = 0;
    }

    sharedListener = new PhaseListenerImpl(phaseCalled,  
					   PhaseId.PROCESS_VALIDATIONS, null);
    life.addPhaseListener(sharedListener);

    try {
	life.execute(getFacesContext());
    }
    catch (Throwable e) {
	e.printStackTrace();
	assertTrue(e.getMessage(), false);
    }
    
    for (i = 1; i < phaseCalled.length; i++) {
	if (PhaseId.PROCESS_VALIDATIONS.getOrdinal() == i) {
	    assertTrue(phaseCalled[i] == 2);
	}
	else {
	    assertTrue(phaseCalled[i] == 0);
	}
    }
}

public void beginValidateWithoutListener(WebRequest theRequest) {
    initWebRequest(theRequest);
}

public void testValidateWithoutListener() {
    assertTrue(null != sharedListener);

    LifecycleImpl life = getSharedLifecycleImpl();
    final int [] phaseCalled = sharedListener.getPhaseCalled();
    int i;

    life.removePhaseListener(sharedListener);

    try {
	life.execute(getFacesContext());
    }
    catch (Throwable e) {
	e.printStackTrace();
	assertTrue(e.getMessage(), false);
    }
    
    // make sure the listener wasn't called
    for (i = 1; i < phaseCalled.length; i++) {
	if (PhaseId.PROCESS_VALIDATIONS.getOrdinal() == i) {
	    assertTrue(phaseCalled[i] == 2);
	}
	else {
	    assertTrue(phaseCalled[i] == 0);
	}
    }

}

class PhaseListenerImpl implements PhaseListener {
    int [] phaseCalled = null;
    PhaseId phaseId = null;
    PhaseId messageBeforeThisPhase = null;

    public int [] getPhaseCalled() {
	return phaseCalled;
    }

    public PhaseListenerImpl(int [] newPhaseCalled, PhaseId newPhaseId, PhaseId addErrorMessageBeforeThisPhase) {
	phaseCalled = newPhaseCalled;
	phaseId = newPhaseId;
	messageBeforeThisPhase = addErrorMessageBeforeThisPhase;
    }

    public void afterPhase(PhaseEvent event) {
	phaseCalled[event.getPhaseId().getOrdinal()] = 
	    phaseCalled[event.getPhaseId().getOrdinal()] + 1;
    }
    
    public void beforePhase(PhaseEvent event) {
	phaseCalled[event.getPhaseId().getOrdinal()] = 
	    phaseCalled[event.getPhaseId().getOrdinal()] + 1;
	if (messageBeforeThisPhase == event.getPhaseId()) {
	    FacesContext.getCurrentInstance().addMessage(null,
							 Util.getMessageResources().getMessage(FacesContext.getCurrentInstance(), Util.VALIDATOR_NOT_FOUND_ERROR_MESSAGE_ID));
	}
    }
    
    public PhaseId getPhaseId() {
	return phaseId;
    }
    
}

} // end of class TestLifecycleImpl
