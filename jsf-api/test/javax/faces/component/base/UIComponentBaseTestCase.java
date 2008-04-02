/*
 * $Id: UIComponentBaseTestCase.java,v 1.3 2003/07/27 00:48:30 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentTestCase;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.mock.MockExternalContext;
import javax.faces.mock.MockFacesContext;
import javax.faces.mock.MockHttpServletRequest;
import javax.faces.mock.MockHttpServletResponse;
import javax.faces.mock.MockHttpSession;
import javax.faces.mock.MockLifecycle;
import javax.faces.mock.MockRenderKit;
import javax.faces.mock.MockRenderKitFactory;
import javax.faces.mock.MockServletConfig;
import javax.faces.mock.MockServletContext;
import javax.faces.mock.MockTree;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.Validator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;



/**
 * <p>Base unit tests for all {@link UIComponentBase} subclasses.</p>
 */

public class UIComponentBaseTestCase extends UIComponentTestCase {


    // ------------------------------------------------------ Instance Variables


    // Mock object instances for our tests
    protected MockServletConfig       config = null;
    protected MockExternalContext     externalContext = null;
    protected MockFacesContext        facesContext = null;
    protected MockLifecycle           lifecycle = null;
    protected MockHttpServletRequest  request = null;
    protected MockHttpServletResponse response = null;
    protected MockServletContext      servletContext = null;
    protected MockHttpSession         session = null;


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIComponentBaseTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {

        // Set up Servlet API Objects
        servletContext = new MockServletContext();
        servletContext.addInitParameter("appParamName", "appParamValue");
        servletContext.setAttribute("appScopeName", "appScopeValue");
        config = new MockServletConfig(servletContext);
        session = new MockHttpSession();
        session.setAttribute("sesScopeName", "sesScopeValue");
        request = new MockHttpServletRequest(session);
        request.setAttribute("reqScopeName", "reqScopeValue");
        response = new MockHttpServletResponse();

        // Set up Faces API Objects
        externalContext =
            new MockExternalContext(servletContext, request, response);
        lifecycle = new MockLifecycle();
        facesContext = new MockFacesContext(externalContext, lifecycle);
        facesContext.setTree(new MockTree(new TestComponentNamingContainer()));
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = new MockRenderKit();
        try {
            renderKitFactory.addRenderKit(RenderKitFactory.DEFAULT_RENDER_KIT,
                                          renderKit);
        } catch (IllegalArgumentException e) {
            ;
        }

        // Set up the component under test
        super.setUp();
        component = new TestComponentNamingContainer(expectedId);

    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIComponentBaseTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {

        super.tearDown();
        config = null;
        externalContext = null;
        facesContext = null;
        lifecycle = null;
        request = null;
        response = null;
        servletContext = null;
        session = null;

    }


    // ------------------------------------------------- Individual Test Methods


    // Test lifecycle management methods
    public void testLifecycleManagement() {

        // Establish a component tree with multiple facets and children
        UIComponent facet1 = new TestComponent("f1");
        UIComponent facet2 = new TestComponent("f2");
        UIComponent facet3 = new TestComponent("f3");
        component.getFacets().put("f1", facet1);
        component.getFacets().put("f2", facet2);
        component.getFacets().put("f3", facet3);
        checkFacetCount(component, 3);
        UIComponent child1 = new TestComponent("c1");
        UIComponent child2 = new TestComponent("c2");
        UIComponent child3 = new TestComponent("c3");
        component.getChildren().add(child1);
        component.getChildren().add(child2);
        component.getChildren().add(child3);
        checkChildCount(component, 3);
        UIComponent child2a = new TestComponent("c2a");
        UIComponent child2b = new TestComponent("c2b");
        child2.getChildren().add(child2a);
        child2.getChildren().add(child2b);
        checkChildCount(child2, 2);

        // Enqueue a single FacesEvent for each component
        facesContext.addFacesEvent(new TestEvent(component));
        facesContext.addFacesEvent(new TestEvent(facet1));
        facesContext.addFacesEvent(new TestEvent(facet2));
        facesContext.addFacesEvent(new TestEvent(facet3));
        facesContext.addFacesEvent(new TestEvent(child1));
        facesContext.addFacesEvent(new TestEvent(child2));
        facesContext.addFacesEvent(new TestEvent(child3));
        facesContext.addFacesEvent(new TestEvent(child2a));
        facesContext.addFacesEvent(new TestEvent(child2b));

        // Test processDecodes()
        TestComponent.trace(null);
        try {
            component.processDecodes(facesContext);
        } catch (IOException e) {
            fail("Threw exception: " + e);
        }
        assertEquals("processDecodes",
                     lifecycleTrace("pD", "d"),
                     TestComponent.trace());

        // Test processValidators()
        TestComponent.trace(null);
        component.processValidators(facesContext);
        assertEquals("processValidators",
                     lifecycleTrace("pV", null),
                     TestComponent.trace());

        // Test processUpdates()
        TestComponent.trace(null);
        component.processUpdates(facesContext);
        assertEquals("processUpdates",
                     lifecycleTrace("pU", null),
                     TestComponent.trace());

    }


    // Test removing components from or naming container.
    public void testComponentRemoval() {

        UIComponent testComponent = new TestComponentNamingContainer();
        UIComponent child1 = new TestComponent("child1");
        UIComponent child2 = new TestComponent("child2");
        UIComponent child3 = new TestComponent("child3");
        UIComponent child = null;

        //adding children to naming container
        testComponent.getChildren().add(child1);
        testComponent.getChildren().add(child2);
        testComponent.getChildren().add(child3);

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
        testComponent.getChildren().remove(child1);

        kidItr = testComponent.getFacetsAndChildren();

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child2));

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child3));

        //make sure child is removed from component and naming container
        //pass an index to remove method
        testComponent.getChildren().remove(0);

        kidItr = testComponent.getFacetsAndChildren();

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child3));

        //make sure child is removed from component and naming container
        //remove all children
        testComponent.getChildren().clear();

        kidItr = testComponent.getFacetsAndChildren();
        assertTrue(!kidItr.hasNext());
    }


    // --------------------------------------------------------- Support Methods


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
        String id = component.getId();
        sb.append("/" + lmethod + "-" + id);

        // Append the calls for each facet
        Iterator names = component.getFacets().keySet().iterator();
        while (names.hasNext()) {
            String name = (String) names.next();
            sb.append("/" + lmethod + "-" + name);
	    if (cmethod != null) {
		sb.append("/" + cmethod + "-" + name);
	    }
        }

        // Append the calls for each child
        Iterator kids = component.getChildren().iterator();
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
