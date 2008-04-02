/*
 * $Id: UIComponentBaseTestCase.java,v 1.1 2002/12/03 01:04:59 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.RequestEvent;
import javax.faces.event.RequestEventHandler;
import javax.faces.validator.Validator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.faces.mock.MockFacesContext;
import javax.faces.mock.MockHttpServletRequest;
import javax.faces.mock.MockServletContext;


/**
 * <p>Test case for the <strong>javax.faces.component.UIComponentBase</strong>
 * concrete class.</p>
 */

public class UIComponentBaseTestCase extends UIComponentTestCase {


    // ----------------------------------------------------- Instance Variables


    /**
     * The mock FacesContext to use in our tests.
     */
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
        context = new MockFacesContext();
        ((MockFacesContext) context).setServletContext
            (new MockServletContext());
        ((MockFacesContext) context).setServletRequest
            (new MockHttpServletRequest());

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
        component.addFacet(facet1);
        component.addFacet(facet2);
        component.addFacet(facet3);
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

        // Enqueue a single RequestEvent for each component
        context.addRequestEvent(component, new RequestEvent(component));
        context.addRequestEvent(facet1, new RequestEvent(component));
        context.addRequestEvent(facet2, new RequestEvent(component));
        context.addRequestEvent(facet3, new RequestEvent(component));
        context.addRequestEvent(child1, new RequestEvent(component));
        context.addRequestEvent(child2, new RequestEvent(component));
        context.addRequestEvent(child3, new RequestEvent(component));
        context.addRequestEvent(child2a, new RequestEvent(component));
        context.addRequestEvent(child2b, new RequestEvent(component));

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

        // Test processEvents()
        TestComponent.trace(null);
        component.processEvents(context);
        assertEquals("processEvents",
                     lifecycleTrace("pE", "e"),
                     TestComponent.trace());

        // Test processValidators()
        TestComponent.trace(null);
        component.processValidators(context);
        assertEquals("processValidators",
                     lifecycleTrace("pV", "v"),
                     TestComponent.trace());

        // Test processUpdates()
        TestComponent.trace(null);
        component.processUpdates(context);
        assertEquals("processUpdates",
                     lifecycleTrace("pU", "u"),
                     TestComponent.trace());

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
            sb.append("/" + cmethod + "-" + name);
        }

        // Append the calls for each child
        Iterator kids = component.getChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            lifecycleTrace(lmethod, cmethod, kid, sb);
        }

        // Append the call for this component's component method
        sb.append("/" + cmethod + "-" + id);

    }


}
