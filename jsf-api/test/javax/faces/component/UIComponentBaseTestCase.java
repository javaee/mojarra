/*
 * $Id: UIComponentBaseTestCase.java,v 1.8 2003/07/21 18:46:49 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.validator.Validator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.faces.mock.MockExternalContext;
import javax.faces.mock.MockFacesContext;
import javax.faces.mock.MockHttpServletRequest;
import javax.faces.mock.MockHttpServletResponse;
import javax.faces.mock.MockLifecycle;
import javax.faces.mock.MockServletContext;


/**
 * <p>Test case for the <strong>javax.faces.component.UIComponentBase</strong>
 * concrete class.</p>
 */

public class UIComponentBaseTestCase extends UIComponentTestCase {


    // ----------------------------------------------------- Instance Variables


    protected FacesContext context = null;


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIComponentBaseTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        super.setUp();
        MockExternalContext econtext = new MockExternalContext
            (new MockServletContext(),
             new MockHttpServletRequest(),
             new MockHttpServletResponse());
        MockLifecycle lifecycle = new MockLifecycle();
        context = new MockFacesContext(econtext, lifecycle);

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UIComponentBaseTestCase.class));

    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        super.tearDown();
        context = null;

    }


    // ------------------------------------------------ Individual Test Methods


    /**
     * [3.1.x] Lifecycle Management Methods.
     */
    public void testLifecycleManagement() {

        // Establish a component tree with multiple facets and children
        UIComponent facet1 = new TestComponent("f1");
        UIComponent facet2 = new TestComponent("f2");
        UIComponent facet3 = new TestComponent("f3");
        component.addFacet("f1", facet1);
        component.addFacet("f2", facet2);
        component.addFacet("f3", facet3);
        checkFacetCount(component, 3);
        UIComponent child1 = new TestComponent("c1");
        UIComponent child2 = new TestComponent("c2");
        UIComponent child3 = new TestComponent("c3");
        component.addChild(child1);
        component.addChild(child2);
        component.addChild(child3);
        checkChildCount(component, 3);
        UIComponent child2a = new TestComponent("c2a");
        UIComponent child2b = new TestComponent("c2b");
        child2.addChild(child2a);
        child2.addChild(child2b);
        checkChildCount(child2, 2);

        // Enqueue a single FacesEvent for each component
        context.addFacesEvent(new FacesEvent(component));
        context.addFacesEvent(new FacesEvent(facet1));
        context.addFacesEvent(new FacesEvent(facet2));
        context.addFacesEvent(new FacesEvent(facet3));
        context.addFacesEvent(new FacesEvent(child1));
        context.addFacesEvent(new FacesEvent(child2));
        context.addFacesEvent(new FacesEvent(child3));
        context.addFacesEvent(new FacesEvent(child2a));
        context.addFacesEvent(new FacesEvent(child2b));

        // Test processDecodes()
        TestComponent.trace(null);
        try {
            component.processDecodes(context);
        } catch (IOException e) {
            fail("Threw exception: " + e);
        }
        assertEquals("processDecodes",
                     lifecycleTrace("pD", "d"),
                     TestComponent.trace());

        // Test processValidators()
        TestComponent.trace(null);
        component.processValidators(context);
        assertEquals("processValidators",
                     lifecycleTrace("pV", null),
                     TestComponent.trace());

        // Test processUpdates()
        TestComponent.trace(null);
        component.processUpdates(context);
        assertEquals("processUpdates",
                     lifecycleTrace("pU", "u"),
                     TestComponent.trace());

    }

    /*
     * test removing components from naming container
     */
    public void testComponentRemoval() {
        UIComponent testComponent = new TestComponentNamingContainer();
        UIComponent child1 = new TestComponent("child1");
        UIComponent child2 = new TestComponent("child2");
        UIComponent child3 = new TestComponent("child3");
        UIComponent child = null;

        //adding children to naming container
        testComponent.addChild(child1);
        testComponent.addChild(child2);
        testComponent.addChild(child3);

        // make sure children are stored in naming container properly
        Iterator kidItr = null;

        kidItr = testComponent.getFacetsAndChildren();

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child1));

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child2));

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child3));

        //make sure child is removed from component and naming container
        //pass in a component to remove method
        testComponent.removeChild(child1);

        kidItr = testComponent.getFacetsAndChildren();

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child2));

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child3));

        //make sure child is removed from component and naming container
        //pass an index to remove method
        testComponent.removeChild(0);

        kidItr = testComponent.getFacetsAndChildren();

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child3));

        //make sure child is removed from component and naming container
        //remove all children
        testComponent.clearChildren();

        kidItr = testComponent.getFacetsAndChildren();
        assertTrue(!kidItr.hasNext());
    }


    // -------------------------------------------------------- Support Methods


    /**
     * Construct and return a lifecycle method call trace for the specified
     * method names.
     *
     * @param lmethod Name of the lifecycle method under test
     * @param cmethod Name of the component method that corresponds
     */
    protected String lifecycleTrace(String lmethod, String cmethod) {

        StringBuffer sb = new StringBuffer();
        lifecycleTrace(lmethod, cmethod, component, sb);
        return (sb.toString());

    }

    protected void lifecycleTrace(String lmethod, String cmethod,
                                  UIComponent component, StringBuffer sb) {

        // Append the call for this lifecycle method
        String id = component.getComponentId();
        sb.append("/" + lmethod + "-" + id);

        // Append the calls for each facet
        Iterator names = component.getFacetNames();
        while (names.hasNext()) {
            String name = (String) names.next();
            sb.append("/" + lmethod + "-" + name);
	    if (cmethod != null) {
		sb.append("/" + cmethod + "-" + name);
	    }
        }

        // Append the calls for each child
        Iterator kids = component.getChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            lifecycleTrace(lmethod, cmethod, kid, sb);
        }

        // Append the call for this component's component method
	if (cmethod != null) {
	    sb.append("/" + cmethod + "-" + id);
	}

    }


}
