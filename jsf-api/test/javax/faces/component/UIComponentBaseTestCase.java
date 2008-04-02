/*
 * $Id: UIComponentBaseTestCase.java,v 1.19 2003/11/07 18:55:37 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.FacesEvent;
import javax.faces.mock.MockApplication;
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
import javax.faces.mock.MockValueBinding;
import javax.faces.TestUtil;
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
    protected MockApplication         application = null;
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
	FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
				 "javax.faces.mock.MockApplicationFactory");
	FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
				 "javax.faces.mock.MockRenderKitFactory");

        externalContext =
            new MockExternalContext(servletContext, request, response);
        lifecycle = new MockLifecycle();
        facesContext = new MockFacesContext(externalContext, lifecycle);
	UIViewRoot root = new UIViewRoot();
	root.setViewId("/viewId");
        facesContext.setViewRoot(root);
        ApplicationFactory applicationFactory = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = (MockApplication) applicationFactory.getApplication();
        facesContext.setApplication(application);
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
        component = new TestComponent(expectedId);

    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIComponentBaseTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {

        super.tearDown();
        application = null;
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

        checkLifecycleParentRendered();
        checkLifecycleParentUnrendered();
        checkLifecycleSelfRendered();
        checkLifecycleSelfUnrendered();


    }


    // Test recursive adding and removing child trees with ids
    public void testChildrenRecursive() {

        // Create the components we will need
        UIComponent testComponent = new TestComponent();
        UIComponent child1 = new TestComponent("child1");
        UIComponent child2 = new TestComponent("child2");
        UIComponent child3 = new TestComponent("child3");

        // Prepare ancestry tree before adding to base component
        child1.getChildren().add(child2);
        child2.getChildren().add(child3);

        // Verify that no child ids are visible yet
        assertNull(testComponent.findComponent("child1"));
        assertNull(testComponent.findComponent("child2"));
        assertNull(testComponent.findComponent("child3"));

        // Add the entire tree
        testComponent.getChildren().add(child1);

        // Verify that all named children get added
        assertEquals(child1, testComponent.findComponent("child1"));
        assertEquals(child2, testComponent.findComponent("child2"));
        assertEquals(child3, testComponent.findComponent("child3"));

        // Remove the entire tree
        testComponent.getChildren().remove(child1);

        // Verify that child ids are no longer visible
        assertNull(testComponent.findComponent("child1"));
        assertNull(testComponent.findComponent("child2"));
        assertNull(testComponent.findComponent("child3"));

    }


    // Test reconnecting a child or facet to a different component
    public void testComponentReconnect() {

        UIComponent parent1 = new TestComponent();
        UIComponent parent2 = new TestComponent();

        // Reconnect an existing child as a child
        checkChildCount(parent1, 0);
        checkChildCount(parent2, 0);
        parent1.getChildren().add(component);
        checkChildCount(parent1, 1);
        checkChildCount(parent2, 0);
        checkChildPresent(parent1, component, 0);
        parent2.getChildren().add(component);
        checkChildCount(parent1, 0);
        checkChildCount(parent2, 1);
        checkChildPresent(parent2, component, 0);
        parent2.getChildren().clear();
        checkChildCount(parent1, 0);
        checkChildCount(parent2, 0);

        // Reconnect an existing child as a facet
        checkChildCount(parent1, 0);
        checkFacetCount(parent2, 0);
        parent1.getChildren().add(component);
        checkChildCount(parent1, 1);
        checkFacetCount(parent2, 0);
        checkChildPresent(parent1, component, 0);
        parent2.getFacets().put("facet", component);
        checkChildCount(parent1, 0);
        checkFacetCount(parent2, 1);
        checkFacetPresent(parent2, "facet", component);
        parent2.getFacets().clear();
        checkChildCount(parent1, 0);
        checkFacetCount(parent2, 0);

        // Reconnect an existing facet as a child
        checkFacetCount(parent1, 0);
        checkChildCount(parent2, 0);
        parent1.getFacets().put("facet", component);
        checkFacetCount(parent1, 1);
        checkChildCount(parent2, 0);
        checkFacetPresent(parent1, "facet", component);
        parent2.getChildren().add(component);
        checkFacetCount(parent1, 0);
        checkChildCount(parent2, 1);
        checkChildPresent(parent2, component, 0);
        parent2.getChildren().clear();
        checkFacetCount(parent1, 0);
        checkChildCount(parent2, 0);

    }


    // Test removing components from our naming container.
    public void testComponentRemoval() {

        UIComponent testComponent = new TestComponent();
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


    public void testStateHolder() throws Exception {

        // Set up the components we will need
        UIComponent parent = new TestComponent("root");
	UIComponent preSave = createComponent();
        UIComponent facet1 = createComponent();
        facet1.setId("facet1");
        preSave.getFacets().put("facet1 key", facet1);
        UIComponent facet2 = createComponent();
        facet2.setId("facet2");
        preSave.getFacets().put("facet2 key", facet2);
        populateComponent(preSave);
        parent.getChildren().add(preSave);
        UIComponent postSave = createComponent();

        // Save and restore state and compare the results
        Object state = preSave.saveState(facesContext);
        assertNotNull(state);
        postSave.restoreState(facesContext, state);
        checkComponents(preSave, postSave);
	checkValueBindings(preSave, postSave);

    }


    public void testValueBindings() {

	UIComponentBase test = (UIComponentBase) component;

	// generic attributes
	request.setAttribute("foo", "bar");
	test.getAttributes().clear();
	assertNull(test.getAttributes().get("baz"));
	test.setValueBinding("baz", application.getValueBinding("#{foo}"));
	assertEquals("bar", test.getAttributes().get("baz"));
	test.getAttributes().put("baz", "bop");
	assertEquals("bop", test.getAttributes().get("baz"));
	test.getAttributes().remove("baz");
	assertEquals("bar", test.getAttributes().get("baz"));
	test.setValueBinding("baz", null);
	assertNull(test.getAttributes().get("baz"));

	// "id" property
	request.setAttribute("foo", "bar");
	test.setId(null);
	assertNull(test.getId());
	test.setValueBinding("id", application.getValueBinding("#{foo}"));
	assertNotNull(test.getValueBinding("id"));
	assertEquals("bar", test.getId());
	test.setId("baz");
	assertEquals("baz", test.getId());
	test.setId(null);
	assertEquals("bar", test.getId());
	test.setValueBinding("id", null);
	assertNull(test.getValueBinding("id"));
	assertNull(test.getId());

	// "rendered" property
	request.setAttribute("foo", Boolean.FALSE);
	boolean initial = test.isRendered();
	if (initial) {
	    request.setAttribute("foo", Boolean.FALSE);
	} else {
	    request.setAttribute("foo", Boolean.TRUE);
	}
	test.setValueBinding("rendered", application.getValueBinding("#{foo}"));
	assertEquals(!initial, test.isRendered());
	test.setRendered(initial);
	assertEquals(initial, test.isRendered());
	assertNotNull(test.getValueBinding("rendered"));

	// "rendererType" property
	request.setAttribute("foo", "bar");
	test.setRendererType(null);
	assertNull(test.getRendererType());
	test.setValueBinding("rendererType", application.getValueBinding("#{foo}"));
	assertNotNull(test.getValueBinding("rendererType"));
	assertEquals("bar", test.getRendererType());
	test.setRendererType("baz");
	assertEquals("baz", test.getRendererType());
	test.setRendererType(null);
	assertEquals("bar", test.getRendererType());
	test.setValueBinding("rendererType", null);
	assertNull(test.getValueBinding("rendererType"));
	assertNull(test.getRendererType());

    }


    // --------------------------------------------------------- Support Methods


    // Check that the attributes on the specified components are equal
    protected void checkAttributes(UIComponent comp1, UIComponent comp2) {
        assertEquals(comp1.getAttributes(), comp2.getAttributes());
    }


    // Check that the specified components are equal
    protected void checkComponents(UIComponent comp1, UIComponent comp2) {
        checkAttributes(comp1, comp2);
        // checkFacets(comp1, comp2); // Not saved and restored by component
        checkProperties(comp1, comp2);
    }


    // Check lifecycle processing when parent "rendered" property is "true"
    private void checkLifecycleParentRendered() {

        // Put our component under test in a tree under a UIViewRoot
        component.getAttributes().clear();
        component.getChildren().clear();
        component.getFacets().clear();
        component.setRendered(true);
        UIViewRoot root = new UIViewRoot();
        UIPanel panel = new UIPanel();
        panel.setRendered(true);
        root.getChildren().add(panel);
        panel.getChildren().add(component);

        // Establish a view with multiple facets and children
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
        component.queueEvent(new TestEvent(component));
        component.queueEvent(new TestEvent(facet1));
        component.queueEvent(new TestEvent(facet2));
        component.queueEvent(new TestEvent(facet3));
        component.queueEvent(new TestEvent(child1));
        component.queueEvent(new TestEvent(child2));
        component.queueEvent(new TestEvent(child3));
        component.queueEvent(new TestEvent(child2a));
        component.queueEvent(new TestEvent(child2b));

        // Test processDecodes()
        TestComponent.trace(null);
	component.processDecodes(facesContext);
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


    // Check lifecycle processing when parent "rendered" property is "false"
    private void checkLifecycleParentUnrendered() {

        // Put our component under test in a tree under a UIViewRoot
        component.getAttributes().clear();
        component.getChildren().clear();
        component.getFacets().clear();
        component.setRendered(true);
        UIViewRoot root = new UIViewRoot();
        UIPanel panel = new UIPanel();
        panel.setRendered(false);
        root.getChildren().add(panel);
        panel.getChildren().add(component);

        // Establish a view with multiple facets and children
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
        component.queueEvent(new TestEvent(component));
        component.queueEvent(new TestEvent(facet1));
        component.queueEvent(new TestEvent(facet2));
        component.queueEvent(new TestEvent(facet3));
        component.queueEvent(new TestEvent(child1));
        component.queueEvent(new TestEvent(child2));
        component.queueEvent(new TestEvent(child3));
        component.queueEvent(new TestEvent(child2a));
        component.queueEvent(new TestEvent(child2b));

        // Test processDecodes()
        TestComponent.trace(null);
	component.processDecodes(facesContext);
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


    // Check lifecycle processing when our "rendered" property is "true"
    private void checkLifecycleSelfRendered() {

        // Put our component under test in a tree under a UIViewRoot
        component.getAttributes().clear();
        component.getChildren().clear();
        component.getFacets().clear();
        component.setRendered(true);
        UIViewRoot root = new UIViewRoot();
        root.getChildren().add(component);

        // Establish a view with multiple facets and children
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
        component.queueEvent(new TestEvent(component));
        component.queueEvent(new TestEvent(facet1));
        component.queueEvent(new TestEvent(facet2));
        component.queueEvent(new TestEvent(facet3));
        component.queueEvent(new TestEvent(child1));
        component.queueEvent(new TestEvent(child2));
        component.queueEvent(new TestEvent(child3));
        component.queueEvent(new TestEvent(child2a));
        component.queueEvent(new TestEvent(child2b));

        // Test processDecodes()
        TestComponent.trace(null);
	component.processDecodes(facesContext);
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


    // Check lifecycle processing when our "rendered" property is "false"
    private void checkLifecycleSelfUnrendered() {

        // Put our component under test in a tree under a UIViewRoot
        component.getAttributes().clear();
        component.getChildren().clear();
        component.getFacets().clear();
        component.setRendered(false);
        UIViewRoot root = new UIViewRoot();
        root.getChildren().add(component);

        // Establish a view with multiple facets and children
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
        component.queueEvent(new TestEvent(component));
        component.queueEvent(new TestEvent(facet1));
        component.queueEvent(new TestEvent(facet2));
        component.queueEvent(new TestEvent(facet3));
        component.queueEvent(new TestEvent(child1));
        component.queueEvent(new TestEvent(child2));
        component.queueEvent(new TestEvent(child3));
        component.queueEvent(new TestEvent(child2a));
        component.queueEvent(new TestEvent(child2b));

        // Test processDecodes()
        TestComponent.trace(null);
	component.processDecodes(facesContext);
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


    // Check that the properties on the specified components are equal
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {
        assertEquals(comp1.getComponentRef(), comp2.getComponentRef());
        assertEquals(comp1.getClientId(facesContext),
                     comp2.getClientId(facesContext));
        assertEquals(comp1.getId(), comp2.getId());
        assertEquals(comp1.isRendered(), comp2.isRendered());
        assertEquals(comp1.getRendererType(), comp2.getRendererType());
        assertEquals(comp1.getRendersChildren(), comp2.getRendersChildren());
    }


    // Check that the configured ValueBindings got restored
    protected void checkValueBindings(UIComponent comp1, UIComponent comp2) {

	ValueBinding vb1, vb2;

	vb1 = comp1.getValueBinding("baz");
	vb2 = comp2.getValueBinding("baz");
	assertEquals(((MockValueBinding) vb1).ref(),
		     ((MockValueBinding) vb2).ref());

	vb1 = comp1.getValueBinding("bop");
	vb2 = comp2.getValueBinding("bop");
	assertEquals(((MockValueBinding) vb1).ref(),
		     ((MockValueBinding) vb2).ref());

    }


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        return (new TestComponent());
    }


    // Populate a pristine component to be used in state holder tests
    protected void populateComponent(UIComponent component) {

        component.getAttributes().put("foo", "foo value");
        component.getAttributes().put("bar", "bar value");
        component.setComponentRef("foo.bar");
        component.setId("componentId");
        component.getClientId(facesContext); // Forces evaluation
        component.setRendered(false);
        component.setRendererType(null); // Since we have no renderers

	component.setValueBinding("baz",
				  application.getValueBinding("baz.value"));
	component.setValueBinding("bop",
				  application.getValueBinding("bop.value"));

    }


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
        if (!component.isRendered()) {
            return;
        }

        // Append the calls for each facet
        Iterator names = component.getFacets().keySet().iterator();
        while (names.hasNext()) {
            String name = (String) names.next();
            sb.append("/" + lmethod + "-" + name);
	    if ((cmethod != null) &&
                ((UIComponent) component.getFacets().get(name)).isRendered()) {
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
	if ((cmethod != null) && component.isRendered()) {
	    sb.append("/" + cmethod + "-" + id);
	}

    }


}
