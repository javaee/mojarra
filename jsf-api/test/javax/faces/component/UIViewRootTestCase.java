/*
 * $Id: UIViewRootTestCase.java,v 1.17 2004/11/18 14:11:05 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.faces.FactoryFinder;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseListener;
import javax.faces.event.PhaseId;
import javax.faces.validator.Validator;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.event.PhaseId;
import javax.faces.el.ValueBinding;
import javax.faces.el.MethodBinding;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.faces.mock.MockExternalContext;
import javax.faces.mock.MockFacesContext;
import javax.faces.mock.MockHttpServletRequest;
import javax.faces.mock.MockHttpServletResponse;
import javax.faces.mock.MockLifecycle;
import javax.faces.mock.MockServletContext;
import javax.faces.TestUtil;

/**
 * <p>Test case for the <strong>javax.faces.UIViewRoot</strong>
 * concrete class.</p>
 */

public class UIViewRootTestCase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIViewRootTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UIViewRootTestCase.class));

    }

    public static String FACTORIES[][] = {
	{ FactoryFinder.APPLICATION_FACTORY, 
	  "javax.faces.mock.MockApplicationFactory"
	},
	{ FactoryFinder.FACES_CONTEXT_FACTORY, 
	  "javax.faces.mock.MockFacesContextFactory"
	},
	{ FactoryFinder.LIFECYCLE_FACTORY, 
	  "javax.faces.mock.MockLifecycleFactory"
	},
	{ FactoryFinder.RENDER_KIT_FACTORY, 
	  "javax.faces.mock.MockRenderKitFactory"
	}
    };

    public void setUp() {
	super.setUp();
	for (int i = 0, len = FACTORIES.length; i < len; i++) {
	    System.getProperties().remove(FACTORIES[i][0]);
	}

	FactoryFinder.releaseFactories();
	int len, i = 0;

	// simulate the "faces implementation specific" part
	for (i = 0, len = FACTORIES.length; i < len; i++) {
	    FactoryFinder.setFactory(FACTORIES[i][0],
				     FACTORIES[i][1]);
	}

	
    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        component = null;

    }


    // ------------------------------------------------- Individual Test Methods


    // Test AbortProcessingException support
    public void testAbortProcessingException() {

        // Register three listeners, with the second one set to abort
        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
        root.addFacesListener
            (new TestListener("a", false));
        root.addFacesListener
            (new TestListener("b", true));
        root.addFacesListener
            (new TestListener("c", false));

        // Queue two event and check the results
        TestListener.trace(null);
        TestEvent event1 = new TestEvent(root, "1");
        event1.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        root.queueEvent(event1);
        TestEvent event2 = new TestEvent(root, "2");
        event2.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        root.queueEvent(event2);
        root.processDecodes(facesContext);
        assertEquals("/a/1/b/1/a/2/b/2", TestListener.trace());

    }


    // Test event queuing and dequeuing during broadcasting
    public void testEventBroadcasting() {

        // Register a listener that will conditionally queue a new event
        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
        root.addFacesListener
            (new TestListener("t", "2", "4"));
        TestListener.trace(null);

        // Queue some events, including the trigger one
	TestEvent event = new TestEvent(root, "1");
	event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        root.queueEvent(event);
	event = new TestEvent(root, "2");
	event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        root.queueEvent(event);
	event = new TestEvent(root, "3");
	event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        root.queueEvent(event);

        // Simulate the Apply Request Values phase
        root.processDecodes(facesContext);

        // Validate the results (expect 4th event to also be queued)
        String expected = "/t/1/t/2/t/3/t/4";
        assertEquals(expected, TestListener.trace());

    }


    // Test event queuing and broadcasting
    public void testEventQueuing() {

        // Check for correct ifecycle management processing of event broadcast
        checkEventQueueing(PhaseId.APPLY_REQUEST_VALUES);
        checkEventQueueing(PhaseId.PROCESS_VALIDATIONS);
        checkEventQueueing(PhaseId.UPDATE_MODEL_VALUES);
        checkEventQueueing(PhaseId.INVOKE_APPLICATION);
        checkEventQueueing(PhaseId.ANY_PHASE);

    }

    public void testLocaleFromVB() throws Exception {
	UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	ValueBinding binding = application.createValueBinding("locale");
	request.setAttribute("locale", Locale.CHINESE);
	assertEquals(Locale.getDefault(), root.getLocale());
	root.setValueBinding("locale", binding);
	assertEquals(Locale.CHINESE, root.getLocale());
	root.setLocale(Locale.CANADA_FRENCH);
	assertEquals(Locale.CANADA_FRENCH, root.getLocale());
    }

    public void testUninitializedInstance() throws Exception {
	UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	assertEquals(javax.faces.render.RenderKitFactory.HTML_BASIC_RENDER_KIT,
		     root.getRenderKitId());
	assertEquals(Locale.getDefault(), root.getLocale());
	
    }

    public void testPhaseMethBinding() throws Exception {
	UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	doTestPhaseMethodBinding(root);
    }

    public void testPhaseListener() throws Exception {
	UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	doTestPhaseListener(root);
    }

    public void testPhaseMethodBindingAndListener() throws Exception {
	UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	doTestPhaseMethodBindingAndListener(root);
    }


    public void testPhaseMethBindingState() throws Exception {
	UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	Object state = root.saveState(facesContext);
	root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	root.restoreState(facesContext, state);

	doTestPhaseMethodBinding(root);
    }

    public void testPhaseListenerState() throws Exception {
	UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	Object state = root.saveState(facesContext);
	root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	root.restoreState(facesContext, state);

	doTestPhaseListener(root);
    }

    public void testPhaseMethodBindingAndListenerState() throws Exception {
	UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	Object state = root.saveState(facesContext);
	root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	root.restoreState(facesContext, state);

	doTestPhaseMethodBindingAndListener(root);
    }


	
    public void doTestPhaseMethodBinding(UIViewRoot root) throws Exception {
	doTestPhaseMethodBindingWithPhaseId(root, 
					    PhaseId.APPLY_REQUEST_VALUES);
	doTestPhaseMethodBindingWithPhaseId(root, PhaseId.PROCESS_VALIDATIONS);
	doTestPhaseMethodBindingWithPhaseId(root, PhaseId.UPDATE_MODEL_VALUES);
	doTestPhaseMethodBindingWithPhaseId(root, PhaseId.INVOKE_APPLICATION);
	doTestPhaseMethodBindingWithPhaseId(root, PhaseId.RENDER_RESPONSE);
	
    }

    public void doTestPhaseMethodBindingWithPhaseId(UIViewRoot root, 
						    PhaseId phaseId) throws Exception {
	PhaseListenerBean phaseListenerBean = new PhaseListenerBean(phaseId);
	facesContext.getExternalContext().getRequestMap().put("bean",
							    phaseListenerBean);
	Class [] args = new Class [] { PhaseEvent.class };
	MethodBinding 
	    beforeBinding = facesContext.getApplication().createMethodBinding("#{bean.beforePhase}", args),
	    afterBinding = facesContext.getApplication().createMethodBinding("#{bean.afterPhase}", args);
	root.setBeforePhaseListener(beforeBinding);
	root.setAfterPhaseListener(afterBinding);

	callRightLifecycleMethodGivenPhaseId(root, phaseId);

	assertTrue(phaseListenerBean.isBeforePhaseCalled());
	assertTrue(phaseListenerBean.isAfterPhaseCalled());
	
	
    }


    public void doTestPhaseListener(UIViewRoot root) throws Exception {
	doTestPhaseListenerWithPhaseId(root, 
					    PhaseId.APPLY_REQUEST_VALUES);
	doTestPhaseListenerWithPhaseId(root, PhaseId.PROCESS_VALIDATIONS);
	doTestPhaseListenerWithPhaseId(root, PhaseId.UPDATE_MODEL_VALUES);
	doTestPhaseListenerWithPhaseId(root, PhaseId.INVOKE_APPLICATION);
	doTestPhaseListenerWithPhaseId(root, PhaseId.RENDER_RESPONSE);

    }

    public void doTestPhaseListenerWithPhaseId(UIViewRoot root,
					       PhaseId phaseId) throws Exception {
	PhaseListenerBean phaseListener = new PhaseListenerBean(phaseId);
	root.addPhaseListener(phaseListener);

	callRightLifecycleMethodGivenPhaseId(root, phaseId);

	assertTrue(phaseListener.isBeforePhaseCalled());
	assertTrue(phaseListener.isAfterPhaseCalled());
	
	
    }


    public void doTestPhaseMethodBindingAndListener(UIViewRoot root) throws Exception {
	doTestPhaseMethodBindingAndListenerWithPhaseId(root, 
						       PhaseId.APPLY_REQUEST_VALUES);
	doTestPhaseMethodBindingAndListenerWithPhaseId(root, 
						       PhaseId.PROCESS_VALIDATIONS);
	doTestPhaseMethodBindingAndListenerWithPhaseId(root, 
						       PhaseId.UPDATE_MODEL_VALUES);
	doTestPhaseMethodBindingAndListenerWithPhaseId(root, 
						       PhaseId.INVOKE_APPLICATION);
	doTestPhaseMethodBindingAndListenerWithPhaseId(root, 
						       PhaseId.RENDER_RESPONSE);

    }

    public void doTestPhaseMethodBindingAndListenerWithPhaseId(UIViewRoot root,
							       PhaseId phaseId) throws Exception {
	PhaseListenerBean phaseListener = new PhaseListenerBean(phaseId);
	PhaseListenerBean phaseListenerBean = new PhaseListenerBean(phaseId);
	facesContext.getExternalContext().getRequestMap().put("bean",
							    phaseListenerBean);
	Class [] args = new Class [] { PhaseEvent.class };
	MethodBinding 
	    beforeBinding = facesContext.getApplication().createMethodBinding("#{bean.beforePhase}", args),
	    afterBinding = facesContext.getApplication().createMethodBinding("#{bean.afterPhase}", args);
	root.setBeforePhaseListener(beforeBinding);
	root.setAfterPhaseListener(afterBinding);
	root.addPhaseListener(phaseListener);

	callRightLifecycleMethodGivenPhaseId(root, phaseId);

	assertTrue(phaseListenerBean.isBeforePhaseCalled());
	assertTrue(phaseListenerBean.isAfterPhaseCalled());
	assertTrue(phaseListener.isBeforePhaseCalled());
	assertTrue(phaseListener.isAfterPhaseCalled());
	
    }

    private void callRightLifecycleMethodGivenPhaseId(UIViewRoot root,
						      PhaseId phaseId) throws Exception {
	if (phaseId.getOrdinal() == PhaseId.APPLY_REQUEST_VALUES.getOrdinal()) {
	    root.processDecodes(facesContext);
	}
	else if(phaseId.getOrdinal() == PhaseId.PROCESS_VALIDATIONS.getOrdinal()) {
	    root.processValidators(facesContext);
	} else if(phaseId.getOrdinal() == PhaseId.UPDATE_MODEL_VALUES.getOrdinal()) {
	    root.processUpdates(facesContext);
	} else if(phaseId.getOrdinal() == PhaseId.INVOKE_APPLICATION.getOrdinal()) {
	    root.processApplication(facesContext);
	} else if(phaseId.getOrdinal() == PhaseId.RENDER_RESPONSE.getOrdinal()) {
	    root.encodeBegin(facesContext);
	    root.encodeEnd(facesContext);
	}
    }

    // --------------------------------------------------------- Support Methods


    private void checkEventQueueing(PhaseId phaseId) {

        // NOTE:  Current semantics for ANY_PHASE listeners is that
        // the event should be delivered exactly once, so the existence
        // of such a listener does not cause the event to remain queued.
        // Therefore, the expected string is the same as for any
        // phase-specific listener, and it should get matched after
        // Apply Request Values processing since that is first phase
        // for which events are fired

        // Register an event listener for the specified phase id
        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	TestEvent event = null;
        TestListener listener = new TestListener("t");
        root.addFacesListener(listener);

        // Queue some events to be processed
	event = new TestEvent(root, "1");
	event.setPhaseId(phaseId);
        root.queueEvent(event);
	event = new TestEvent(root, "2");
	event.setPhaseId(phaseId);
        root.queueEvent(event);
        String expected = "/t/1/t/2";

        // Fire off the relevant lifecycle methods and check expected results
        TestListener.trace(null);
        assertEquals("", TestListener.trace());
        root.processDecodes(facesContext);
        if (PhaseId.APPLY_REQUEST_VALUES.equals(phaseId) ||
            PhaseId.ANY_PHASE.equals(phaseId)) {
            assertEquals(expected, TestListener.trace());
        } else {
            assertEquals("", TestListener.trace());
        }
        root.processValidators(facesContext);
        if (PhaseId.PROCESS_VALIDATIONS.equals(phaseId) ||
            PhaseId.APPLY_REQUEST_VALUES.equals(phaseId) ||
            PhaseId.APPLY_REQUEST_VALUES.equals(phaseId) ||
            PhaseId.ANY_PHASE.equals(phaseId)) {
            assertEquals(expected, TestListener.trace());
        } else {
            assertEquals("", TestListener.trace());
        }
        root.processUpdates(facesContext);
        if (PhaseId.UPDATE_MODEL_VALUES.equals(phaseId) ||
            PhaseId.PROCESS_VALIDATIONS.equals(phaseId) ||
            PhaseId.APPLY_REQUEST_VALUES.equals(phaseId) ||
            PhaseId.ANY_PHASE.equals(phaseId)) {
            assertEquals(expected, TestListener.trace());
        } else {
            assertEquals("", TestListener.trace());
        }
        root.processApplication(facesContext);
        assertEquals(expected, TestListener.trace());

    }


    // Check that the properties on the specified components are equal
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {

        super.checkProperties(comp1, comp2);
        UIViewRoot vr1 = (UIViewRoot) comp1;
        UIViewRoot vr2 = (UIViewRoot) comp2;
        assertEquals(vr2.getRenderKitId(), vr2.getRenderKitId());
        assertEquals(vr1.getViewId(), vr2.getViewId());
        assertEquals(vr1.getLocale(), vr2.getLocale());

    }


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UIViewRoot();
        component.setRendererType(null);
        return (component);
    }


    // Populate a pristine component to be used in state holder tests
    protected void populateComponent(UIComponent component) {
        super.populateComponent(component);
        UIViewRoot vr = (UIViewRoot) component;
        vr.setRenderKitId("foo");
        vr.setViewId("bar");
        vr.setLocale(new Locale("fr", "FR"));
    }

    public static class PhaseListenerBean extends Object implements PhaseListener {
	private boolean beforePhaseCalled = false;
	private boolean afterPhaseCalled = false;
	private PhaseId phaseId = null;

	public PhaseListenerBean(PhaseId phaseId) {
	    this.phaseId = phaseId;
	}

	public boolean isBeforePhaseCalled() {
	    return beforePhaseCalled;
	}
	
	public boolean isAfterPhaseCalled() {
	    return afterPhaseCalled;
	}
	
	public void beforePhase(PhaseEvent e) {
	    beforePhaseCalled = true;
	}
	    
	public void afterPhase(PhaseEvent e) {
	    afterPhaseCalled = true;
	}

	public PhaseId getPhaseId() { return phaseId; }
	    
    }


}
