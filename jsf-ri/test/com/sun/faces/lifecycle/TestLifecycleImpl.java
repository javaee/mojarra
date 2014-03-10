/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// TestLifecycleImpl.java

package com.sun.faces.lifecycle;

import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.webapp.PreJsf2ExceptionHandlerFactory;

import com.sun.faces.cactus.JspFacesTestCase;
import com.sun.faces.util.Util;
import com.sun.faces.context.ExceptionHandlerImpl;

import org.apache.cactus.WebRequest;

import java.util.Locale;

/**
 * <B>TestLifecycleImpl</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
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
	theRequest.addParameter("javax.faces.ViewState",
				"H4sIAAAAAAAAAFvzloG1hIElPjPFsAAAhLx/NgwAAAA=");
    }


    public void setUp() {
        super.setUp();
	FacesContext context = getFacesContext();
        UIViewRoot root = Util.getViewHandler(context).createView(context, null);
        root.setLocale(Locale.US);
        root.setViewId(TEST_URI);
	context.setViewRoot(root);
	

        UIForm basicForm = new UIForm();
        basicForm.setId("basicForm");
        UIInput userName = new UIInput();

        userName.setId("userName");
        root.getChildren().add(basicForm);
        basicForm.getChildren().add(userName);

	// here we do what the StateManager does to save the state in
	// the server.
	Util.getStateManager(context).saveSerializedView(context);

    }


    public void beginAnyPhaseWithListenerAndValidationFailure(WebRequest theRequest) {
        initWebRequest(theRequest);
    }


    public void testAnyPhaseWithListenerAndValidationFailure() {
        LifecycleImpl life = getSharedLifecycleImpl();
        final int[] phaseCalled = new
            int[PhaseId.RENDER_RESPONSE.getOrdinal() + 1];
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
//        initWebRequest(theRequest);
    }


    public void testAnyPhaseWithListener() {
//        LifecycleImpl life = getSharedLifecycleImpl();
//        final int[] phaseCalled = new
//            int[PhaseId.RENDER_RESPONSE.getOrdinal() + 1];
//        int i;
//        for (i = 1; i < phaseCalled.length; i++) {
//            phaseCalled[i] = 0;
//        }
//
//        life.removePhaseListener(sharedListener);
//        sharedListener = new PhaseListenerImpl(phaseCalled, PhaseId.ANY_PHASE,
//                                               null);
//        life.addPhaseListener(sharedListener);
//
//        try {
//            life.execute(getFacesContext());
//            life.render(getFacesContext());
//        } catch (Throwable e) {
//            e.printStackTrace();
//            assertTrue(e.getMessage(), false);
//        }
//
//        for (i = 1; i < phaseCalled.length; i++) {
//            assertTrue(phaseCalled[i] == 2);
//        }
    }


    public void beginAnyPhaseWithoutListener(WebRequest theRequest) {
//        initWebRequest(theRequest);
    }


    public void testAnyPhaseWithoutListener() {
//        assertTrue(null != sharedListener);
//
//        LifecycleImpl life = getSharedLifecycleImpl();
//        final int[] phaseCalled = sharedListener.getPhaseCalled();
//        int i;
//
//        life.removePhaseListener(sharedListener);
//
//        try {
//            life.execute(getFacesContext());
//            life.render(getFacesContext());
//        } catch (Throwable e) {
//            e.printStackTrace();
//            assertTrue(e.getMessage(), false);
//        }
//
//        // make sure the listener wasn't called
//        for (i = 1; i < phaseCalled.length; i++) {
//            assertTrue(phaseCalled[i] == 2);
//        }

    }


    public void beginValidateWithListener(WebRequest theRequest) {
//        initWebRequest(theRequest);
    }


    public void testValidateWithListener() {
//        LifecycleImpl life = getSharedLifecycleImpl();
//        final int[] phaseCalled = new
//            int[PhaseId.RENDER_RESPONSE.getOrdinal() + 1];
//        int i;
//        for (i = 1; i < phaseCalled.length; i++) {
//            phaseCalled[i] = 0;
//        }
//
//        sharedListener = new PhaseListenerImpl(phaseCalled,
//                                               PhaseId.PROCESS_VALIDATIONS,
//                                               null);
//        life.addPhaseListener(sharedListener);
//
//        try {
//            life.execute(getFacesContext());
//            life.render(getFacesContext());
//        } catch (Throwable e) {
//            e.printStackTrace();
//            assertTrue(e.getMessage(), false);
//        }
//
//        for (i = 1; i < phaseCalled.length; i++) {
//            if (PhaseId.PROCESS_VALIDATIONS.getOrdinal() == i) {
//                assertTrue(phaseCalled[i] == 2);
//            } else {
//                assertTrue(phaseCalled[i] == 0);
//            }
//        }
    }


    public void beginValidateWithoutListener(WebRequest theRequest) {
//        initWebRequest(theRequest);
    }


    public void testValidateWithoutListener() {
//        assertTrue(null != sharedListener);
//
//        LifecycleImpl life = getSharedLifecycleImpl();
//        final int[] phaseCalled = sharedListener.getPhaseCalled();
//        int i;
//
//        life.removePhaseListener(sharedListener);
//
//        try {
//            life.execute(getFacesContext());
//            life.render(getFacesContext());
//        } catch (Throwable e) {
//            e.printStackTrace();
//            assertTrue(e.getMessage(), false);
//        }
//
//        // make sure the listener wasn't called
//        for (i = 1; i < phaseCalled.length; i++) {
//            if (PhaseId.PROCESS_VALIDATIONS.getOrdinal() == i) {
//                assertTrue(phaseCalled[i] == 2);
//            } else {
//                assertTrue(phaseCalled[i] == 0);
//            }
//        }

    }

    public void beginBeforeListenerExceptionJsf12(WebRequest theRequest) {
        initWebRequest(theRequest);
    }

    public void testBeforeListenerExceptionJsf12() {
        ExceptionHandlerFactory f = new PreJsf2ExceptionHandlerFactory();
        testBeforeListenerException(f.getExceptionHandler(), false);
    }

    public void beginBeforeListenerExceptionJsf20(WebRequest theRequest) {
        initWebRequest(theRequest);
    }

    public void testBeforeListenerExceptionJsf20() {
        testBeforeListenerException(new ExceptionHandlerImpl(), true);
    }


    public void testBeforeListenerException(ExceptionHandler handler, boolean expectException) {
        assertTrue(null != sharedListener);
        getFacesContext().setExceptionHandler(handler);
        LifecycleImpl life = getSharedLifecycleImpl();
        int[] phaseCalledA = new int[PhaseId.RENDER_RESPONSE.getOrdinal() + 1];
        int[] phaseCalledB = new int[PhaseId.RENDER_RESPONSE.getOrdinal() + 1];
        int[] phaseCalledC = new int[PhaseId.RENDER_RESPONSE.getOrdinal() + 1];
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
            if (expectException) {
                assertTrue(false);
            }
        } catch (Throwable e) {
            if (!expectException) {
                assertTrue(false);
                e.printStackTrace();
            }
        }

        // verify before and after for "a" were called.
        assertEquals(2,
                     phaseCalledA[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()]);
        // verify before for "b" was called, but the after was not
        assertEquals(1,
                     phaseCalledB[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()]);
        // verify that neither before nor after for "c" were called
        assertEquals(0,
                     phaseCalledC[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()]);

        life.removePhaseListener(a);
        life.removePhaseListener(b);
        life.removePhaseListener(c);
    }

    public void beginAfterListenerExceptionJsf12(WebRequest theRequest) {
        initWebRequest(theRequest);
    }

    public void testAfterListenerExceptionJsf12() {
        ExceptionHandlerFactory f = new PreJsf2ExceptionHandlerFactory();
        testAfterListenerException(f.getExceptionHandler(), false);
    }

    public void beginAfterListenerExceptionJsf20(WebRequest theRequest) {
        initWebRequest(theRequest);
    }

    public void testAfterListenerExceptionJsf20() {
        testAfterListenerException(new ExceptionHandlerImpl(), true);
    }

    public void testAfterListenerException(ExceptionHandler handler, boolean expectException) {
        assertTrue(null != sharedListener);
        getFacesContext().setExceptionHandler(handler);
        LifecycleImpl life = getSharedLifecycleImpl();
        int[] phaseCalledA = new int[PhaseId.RENDER_RESPONSE.getOrdinal() + 1];
        int[] phaseCalledB = new int[PhaseId.RENDER_RESPONSE.getOrdinal() + 1];
        int[] phaseCalledC = new int[PhaseId.RENDER_RESPONSE.getOrdinal() + 1];
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
            if (expectException) {
                assertTrue(false);
            }
        } catch (Throwable e) {
            if (!expectException) {
                assertTrue(false);
                e.printStackTrace();
            }
        }

        // verify before and after for "a" were called.
        assertEquals(1,
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
