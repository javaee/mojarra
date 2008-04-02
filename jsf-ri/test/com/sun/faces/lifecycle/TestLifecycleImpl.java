/*
 * $Id: TestLifecycleImpl.java,v 1.31 2004/11/09 04:19:29 jhook Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestLifecycleImpl.java

package com.sun.faces.lifecycle;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;
import org.apache.cactus.WebRequest;

import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

/**
 * <B>TestLifecycleImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestLifecycleImpl.java,v 1.31 2004/11/09 04:19:29 jhook Exp $
 */

public class TestLifecycleImpl extends JspFacesTestCase {

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

    public TestLifecycleImpl() {
        super("TestLifecycleImpl");
    }


    public TestLifecycleImpl(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//

    protected LifecycleImpl getSharedLifecycleImpl() {
        if (null == sharedLifecycleImpl) {
            sharedLifecycleImpl = new LifecycleImpl();
        }
        return sharedLifecycleImpl;
    }


    protected PhaseListenerImpl getSharedPhaseListenerImpl() {
        return sharedListener;
    }


    protected void initWebRequest(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", "/test", "/faces", TEST_URI, null);
    }


    public void setUp() {
        RIConstants.IS_UNIT_TEST_MODE = true;
        super.setUp();
        UIViewRoot root = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        root.setViewId(TEST_URI);

        UIForm basicForm = new UIForm();
        basicForm.setId("basicForm");
        UIInput userName = new UIInput();

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
        final int[] phaseCalled = new
            int[LifecycleFactoryImpl.LAST_PHASE + 1];
        int i;
        for (i = 1; i < phaseCalled.length; i++) {
            phaseCalled[i] = 0;
        }

        sharedListener = new PhaseListenerImpl(phaseCalled, PhaseId.ANY_PHASE,
                                               PhaseId.PROCESS_VALIDATIONS);
        life.addPhaseListener(sharedListener);

        try {
            life.execute(getFacesContext());
            life.render(getFacesContext());
        } catch (Throwable e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }

        for (i = 1; i < phaseCalled.length; i++) {
            // i is restore_view, apply_request, process_val, or render_resp
            if (((1 <= i) && (i <= 3)) || (i == 6)) {
                assertTrue(
                    "Expected 2 for phase " + i + ", got " + phaseCalled[i] +
                    ".",

                    phaseCalled[i] == 2);
            } else {
                assertTrue("For phase: " + PhaseId.VALUES.get(i) +
                           " expected no calls, got " + phaseCalled[i] + ".",
                           phaseCalled[i] == 0);
            }
        }
    }


    public void beginAnyPhaseWithListener(WebRequest theRequest) {
        initWebRequest(theRequest);
    }


    public void testAnyPhaseWithListener() {
        LifecycleImpl life = getSharedLifecycleImpl();
        final int[] phaseCalled = new
            int[LifecycleFactoryImpl.LAST_PHASE + 1];
        int i;
        for (i = 1; i < phaseCalled.length; i++) {
            phaseCalled[i] = 0;
        }

        life.removePhaseListener(sharedListener);
        sharedListener = new PhaseListenerImpl(phaseCalled, PhaseId.ANY_PHASE,
                                               null);
        life.addPhaseListener(sharedListener);

        try {
            life.execute(getFacesContext());
            life.render(getFacesContext());
        } catch (Throwable e) {
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
        final int[] phaseCalled = sharedListener.getPhaseCalled();
        int i;

        life.removePhaseListener(sharedListener);

        try {
            life.execute(getFacesContext());
            life.render(getFacesContext());
        } catch (Throwable e) {
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
        final int[] phaseCalled = new
            int[LifecycleFactoryImpl.LAST_PHASE + 1];
        int i;
        for (i = 1; i < phaseCalled.length; i++) {
            phaseCalled[i] = 0;
        }

        sharedListener = new PhaseListenerImpl(phaseCalled,
                                               PhaseId.PROCESS_VALIDATIONS,
                                               null);
        life.addPhaseListener(sharedListener);

        try {
            life.execute(getFacesContext());
            life.render(getFacesContext());
        } catch (Throwable e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }

        for (i = 1; i < phaseCalled.length; i++) {
            if (PhaseId.PROCESS_VALIDATIONS.getOrdinal() == i) {
                assertTrue(phaseCalled[i] == 2);
            } else {
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
        final int[] phaseCalled = sharedListener.getPhaseCalled();
        int i;

        life.removePhaseListener(sharedListener);

        try {
            life.execute(getFacesContext());
            life.render(getFacesContext());
        } catch (Throwable e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }

        // make sure the listener wasn't called
        for (i = 1; i < phaseCalled.length; i++) {
            if (PhaseId.PROCESS_VALIDATIONS.getOrdinal() == i) {
                assertTrue(phaseCalled[i] == 2);
            } else {
                assertTrue(phaseCalled[i] == 0);
            }
        }

    }

    public void beginBeforeListenerException(WebRequest theRequest) {
        initWebRequest(theRequest);
    }


    public void testBeforeListenerException() {
        assertTrue(null != sharedListener);

        LifecycleImpl life = getSharedLifecycleImpl();
        int [] phaseCalledA = new int[LifecycleFactoryImpl.LAST_PHASE + 1];
	int [] phaseCalledB = new int[LifecycleFactoryImpl.LAST_PHASE + 1];
	int [] phaseCalledC = new int[LifecycleFactoryImpl.LAST_PHASE + 1];
        int i;
        for (i = 1; i < phaseCalledA.length; i++) {
            phaseCalledA[i] = 0;
            phaseCalledB[i] = 0;
            phaseCalledC[i] = 0;
        }


        life.removePhaseListener(sharedListener);

	PhaseListenerImpl 
	    a = new PhaseListenerImpl(phaseCalledA, 
				      PhaseId.APPLY_REQUEST_VALUES,
				      PhaseId.PROCESS_VALIDATIONS),
	    b = new PhaseListenerImpl(phaseCalledB, 
				      PhaseId.APPLY_REQUEST_VALUES,
				      PhaseId.PROCESS_VALIDATIONS),
	    c = new PhaseListenerImpl(phaseCalledC, 
				      PhaseId.APPLY_REQUEST_VALUES,
				      PhaseId.PROCESS_VALIDATIONS);
	b.setThrowExceptionOnBefore(true);
        life.addPhaseListener(a);
        life.addPhaseListener(b);
        life.addPhaseListener(c);

        try {
            life.execute(getFacesContext());
            life.render(getFacesContext());
        } catch (Throwable e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }

	// verify before and after for "a" were called.
	assertEquals(2, 
		     phaseCalledA[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()]);
	// verify before for "b" was called, but the after was not
	assertEquals(2, 
		     phaseCalledB[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()]);
	// verify that neither before nor after for "c" were called
	assertEquals(2, 
		     phaseCalledC[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()]);

        life.removePhaseListener(a);
        life.removePhaseListener(b);
        life.removePhaseListener(c);
    }

    public void beginAfterListenerException(WebRequest theRequest) {
        initWebRequest(theRequest);
    }

    public void testAfterListenerException() {
        assertTrue(null != sharedListener);

        LifecycleImpl life = getSharedLifecycleImpl();
        int [] phaseCalledA = new int[LifecycleFactoryImpl.LAST_PHASE + 1];
	int [] phaseCalledB = new int[LifecycleFactoryImpl.LAST_PHASE + 1];
	int [] phaseCalledC = new int[LifecycleFactoryImpl.LAST_PHASE + 1];
        int i;
        for (i = 1; i < phaseCalledA.length; i++) {
            phaseCalledA[i] = 0;
            phaseCalledB[i] = 0;
            phaseCalledC[i] = 0;
        }


        life.removePhaseListener(sharedListener);

	PhaseListenerImpl 
	    a = new PhaseListenerImpl(phaseCalledA, 
				      PhaseId.APPLY_REQUEST_VALUES,
				      PhaseId.PROCESS_VALIDATIONS),
	    b = new PhaseListenerImpl(phaseCalledB, 
				      PhaseId.APPLY_REQUEST_VALUES,
				      PhaseId.PROCESS_VALIDATIONS),
	    c = new PhaseListenerImpl(phaseCalledC, 
				      PhaseId.APPLY_REQUEST_VALUES,
				      PhaseId.PROCESS_VALIDATIONS);
	b.setThrowExceptionOnAfter(true);
        life.addPhaseListener(a);
        life.addPhaseListener(b);
        life.addPhaseListener(c);

        try {
            life.execute(getFacesContext());
            life.render(getFacesContext());
        } catch (Throwable e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }

	// verify before and after for "a" were called.
	assertEquals(2, 
		     phaseCalledA[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()]);
	// verify before for "b" was called, but the after was not
	assertEquals(2, 
		     phaseCalledB[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()]);
	// verify that neither before nor after for "c" were called
	assertEquals(2, 
		     phaseCalledC[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()]);

        life.removePhaseListener(a);
        life.removePhaseListener(b);
        life.removePhaseListener(c);
    }


    class PhaseListenerImpl implements PhaseListener {

        int[] phaseCalled = null;
        PhaseId phaseId = null;
        PhaseId callRenderResponseBeforeThisPhase = null;


        public int[] getPhaseCalled() {
            return phaseCalled;
        }

	boolean throwExceptionOnBefore = false;
	boolean throwExceptionOnAfter = false;

	public void setThrowExceptionOnBefore(boolean newValue) {
	    throwExceptionOnBefore = newValue;
	}

	public void setThrowExceptionOnAfter(boolean newValue) {
	    throwExceptionOnAfter = newValue;
	}
	    


        public PhaseListenerImpl(int[] newPhaseCalled, PhaseId newPhaseId, PhaseId yourCallRenderResponseBeforeThisPhase) {
            phaseCalled = newPhaseCalled;
            phaseId = newPhaseId;
            callRenderResponseBeforeThisPhase =
                yourCallRenderResponseBeforeThisPhase;
        }

        public void afterPhase(PhaseEvent event) {
            phaseCalled[event.getPhaseId().getOrdinal()] =
                phaseCalled[event.getPhaseId().getOrdinal()] + 1;
	    if (throwExceptionOnAfter) {
		throw new IllegalStateException("throwing exception on after " +
						event.getPhaseId().toString());
	    }
        }

        public void beforePhase(PhaseEvent event) {
            phaseCalled[event.getPhaseId().getOrdinal()] =
                phaseCalled[event.getPhaseId().getOrdinal()] + 1;
            if (callRenderResponseBeforeThisPhase == event.getPhaseId()) {
                FacesContext.getCurrentInstance().renderResponse();
            }
	    if (throwExceptionOnBefore) {
		throw new IllegalStateException("throwing exception on before " +
						event.getPhaseId().toString());
	    }

        }


        public PhaseId getPhaseId() {
            return phaseId;
        }

    }

} // end of class TestLifecycleImpl
