/*
 * $Id: UIComponentBaseTestCase.java,v 1.39 2007/04/27 22:00:14 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

package javax.faces.component;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.el.ValueBinding;
import javax.el.ValueExpression;
import com.sun.faces.mock.MockApplication;
import com.sun.faces.mock.MockExternalContext;
import com.sun.faces.mock.MockFacesContext;
import com.sun.faces.mock.MockHttpServletRequest;
import com.sun.faces.mock.MockHttpServletResponse;
import com.sun.faces.mock.MockHttpSession;
import com.sun.faces.mock.MockLifecycle;
import com.sun.faces.mock.MockRenderKit;
import com.sun.faces.mock.MockServletConfig;
import com.sun.faces.mock.MockServletContext;
import com.sun.faces.mock.MockValueBinding;
import java.util.HashMap;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.component.UIComponentTestCase;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.ValidatorException;
import javax.faces.event.AbortProcessingException;
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
				 "com.sun.faces.mock.MockApplicationFactory");
	FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
				 "com.sun.faces.mock.MockRenderKitFactory");

        externalContext =
            new MockExternalContext(servletContext, request, response);
        lifecycle = new MockLifecycle();
        facesContext = new MockFacesContext(externalContext, lifecycle);
        ApplicationFactory applicationFactory = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = (MockApplication) applicationFactory.getApplication();
        facesContext.setApplication(application);
	UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	root.setViewId("/viewId");
        facesContext.setViewRoot(root);
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = new MockRenderKit();
        try {
            renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT,
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

    public void testChildrenAndFacetsWithNullGetParent() throws Exception {
	TestComponent child = new TestComponent() {
		public UIComponent getParent() {
		    return null;
		}
	    };
	component.getChildren().add(child);
	assertNull(component.getChildren().get(0).getParent());
	TestComponent facet = new TestComponent() {
		public UIComponent getParent() {
		    return null;
		}
	    };
	component.getFacets().put("nullParent", facet);
	assertNull(component.getFacets().get("nullParent").getParent());
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
        Object result = test.getAttributes().get("childCount");
	test.getAttributes().clear();
	assertNull(test.getAttributes().get("baz"));
	test.setValueBinding("baz", application.createValueBinding("#{foo}"));
	assertEquals("bar", test.getAttributes().get("baz"));
	test.getAttributes().put("baz", "bop");
	assertEquals("bop", test.getAttributes().get("baz"));
	test.getAttributes().remove("baz");
	assertEquals("bar", test.getAttributes().get("baz"));
	test.setValueBinding("baz", null);
	assertNull(test.getAttributes().get("baz"));

        // "id" property
        try {
            test.setValueBinding("id",
                                 application.createValueBinding("#{foo}"));
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        }

        // "parent" property
        try {
            test.setValueBinding("parent",
                                 application.createValueBinding("#{foo}"));
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        }

	// "rendered" property
	request.setAttribute("foo", Boolean.FALSE);
	boolean initial = test.isRendered();
	if (initial) {
	    request.setAttribute("foo", Boolean.FALSE);
	} else {
	    request.setAttribute("foo", Boolean.TRUE);
	}
	test.setValueBinding("rendered", application.createValueBinding("#{foo}"));
	assertEquals(!initial, test.isRendered());
	test.setRendered(initial);
	assertEquals(initial, test.isRendered());
	assertNotNull(test.getValueBinding("rendered"));

	// "rendererType" property
	request.setAttribute("foo", "bar");
	test.setRendererType(null);
	assertNull(test.getRendererType());
	test.setValueBinding("rendererType", application.createValueBinding("#{foo}"));
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

    public void testValueExpressions() throws Exception {

	UIComponentBase test = (UIComponentBase) component;

	// generic attributes
	request.setAttribute("foo", "bar");
	test.getAttributes().clear();
	assertNull(test.getAttributes().get("baz"));
	test.setValueExpression("baz", application.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{foo}", String.class));
	assertEquals("bar", test.getAttributes().get("baz"));
	test.getAttributes().put("baz", "bop");
	assertEquals("bop", test.getAttributes().get("baz"));
	test.getAttributes().remove("baz");
	assertEquals("bar", test.getAttributes().get("baz"));
	test.setValueExpression("baz", null);
	assertNull(test.getAttributes().get("baz"));

        // "id" property
        try {
            test.setValueExpression("id",
                                 application.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{foo}", String.class));
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        }

        // "parent" property
        try {
            test.setValueExpression("parent",
                                 application.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{foo}", UIComponent.class));
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected response
        }

	// "rendered" property
	request.setAttribute("foo", Boolean.FALSE);
	test.setValueExpression("rendered", null);
	boolean initial = test.isRendered();
	if (initial) {
	    request.setAttribute("foo", Boolean.FALSE);
	} else {
	    request.setAttribute("foo", Boolean.TRUE);
	}
	test.setValueExpression("rendered", application.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{foo}", Boolean.class));
	assertEquals(!initial, test.isRendered());
	test.setRendered(initial);
	assertEquals(initial, test.isRendered());
	assertNotNull(test.getValueExpression("rendered"));

	// "rendererType" property
	request.setAttribute("foo", "bar");
	test.setRendererType(null);
	assertNull(test.getRendererType());
	test.setValueExpression("rendererType", application.getExpressionFactory().createValueExpression(facesContext.getELContext(),"#{foo}", String.class));
	assertNotNull(test.getValueExpression("rendererType"));
	assertEquals("bar", test.getRendererType());
	test.setRendererType("baz");
	assertEquals("baz", test.getRendererType());
	test.setRendererType(null);
	assertEquals("bar", test.getRendererType());
	test.setValueExpression("rendererType", null);
	assertNull(test.getValueExpression("rendererType"));
	assertNull(test.getRendererType());

    }

    public void testValueExpressionValueBindingIdempotency() throws Exception{

	UIComponentBase test = (UIComponentBase) component;

	request.setAttribute("foo", "bar");
	test.getAttributes().clear();
	assertNull(test.getAttributes().get("baz"));
	ValueBinding binding = null;
	ValueExpression expression = null;
	
	binding = application.createValueBinding("#{foo}");
	test.setValueBinding("baz", binding);
	expression = test.getValueExpression("baz");
	
	assertEquals(binding.getExpressionString(), 
		     expression.getExpressionString());
	test.setValueBinding("baz", null);

	expression = application.getExpressionFactory().createValueExpression(facesContext.getELContext(),"#{foo}", String.class);
	test.setValueExpression("baz", expression);
	binding = test.getValueBinding("baz");
	assertEquals(binding.getExpressionString(), 
		     expression.getExpressionString());
	test.setValueBinding("baz", null);
	
    }

    public void testMethodBindingAdapterBaseException() throws Exception {
	IllegalThreadStateException itse = new IllegalThreadStateException("The root cause!");
	AbortProcessingException ape = new AbortProcessingException(itse);
	InvocationTargetException ite1 = new InvocationTargetException(ape);
	InvocationTargetException ite2 = new InvocationTargetException(ite1);
	InvocationTargetException ite3 = new InvocationTargetException(ite2);
	MethodBindingValueChangeListener mbvcl = 
	    new MethodBindingValueChangeListener();
	Throwable expected = 
	    mbvcl.getExpectedCause(AbortProcessingException.class, ite3);
	assertEquals(expected, ape);

	ValidatorException ve = new ValidatorException(new FacesMessage(), 
						       itse);
	ite1 = new InvocationTargetException(ve);
	ite2 = new InvocationTargetException(ite1);
	ite3 = new InvocationTargetException(ite2);

	MethodBindingValidator mbv = new MethodBindingValidator();
	expected = 
	    mbv.getExpectedCause(ValidatorException.class, ite3);
	assertEquals(expected, ve);	
    }


    // --------------------------------------------------------- support Methods


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
        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
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
        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
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
        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
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
        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
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
        component.setId("componentId");
        component.getClientId(facesContext); // Forces evaluation
        component.setRendered(false);
        component.setRendererType(null); // Since we have no renderers

	component.setValueBinding("baz",
				  application.createValueBinding("baz.value"));
	component.setValueBinding("bop",
				  application.createValueBinding("bop.value"));

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

    public void testGetFacetsAndChildren() {

        UIComponent testComponent = new TestComponent();
        UIComponent child1 = new TestComponent("child1");
        UIComponent child2 = new TestComponent("child2");
        UIComponent child3 = new TestComponent("child3");
        UIComponent facet1 = new TestComponent("facet1");
        UIComponent facet2 = new TestComponent("facet2");
        UIComponent facet3 = new TestComponent("facet3");

	testComponent.getChildren().add(child1);
        testComponent.getChildren().add(child2);
        testComponent.getChildren().add(child3);
        testComponent.getFacets().put("facet1", facet1);
        testComponent.getFacets().put("facet2", facet2);
        testComponent.getFacets().put("facet3", facet3);        

        Iterator iter = testComponent.getFacetsAndChildren();
        Object cur = null;
        boolean exceptionThrown = false;
        assertTrue(iter.hasNext());
        
        try {
            iter.remove();
        }
        catch (UnsupportedOperationException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        // facets are returned in an undefined order.
        cur = iter.next();
        assertTrue(cur == facet1 || cur == facet2 || cur == facet3);
        cur = iter.next();
        assertTrue(cur == facet1 || cur == facet2 || cur == facet3);
        cur = iter.next();
        assertTrue(cur == facet1 || cur == facet2 || cur == facet3);
        
        // followed by components, in the order added
        cur = iter.next();
        assertTrue(cur == child1);
        cur = iter.next();
        assertTrue(cur == child2);
        cur = iter.next();
        assertTrue(cur == child3);
        
        assertTrue(!iter.hasNext());
        
    }
        
    private Object foundComponent = null;
    
    /**
     *
     * <p>Build a tree with the following layout.</p>
     * <code><pre>
     * root: id: root
     * 
     *   form1: id: form1
     * 
     *     panel1: id: panel
     * 
     *       input1: id: input1
     * 
     *       input2: id: input2
     * 
     *   form2: id: form2
     * 
     *     panel2: id: panel
     * 
     *       input3: id: input1
     * 
     *       input4: id: input2
     * </pre></code>
     * 
     * @return a Map<String, UIComponent>.  The key is the string before the
     * first : in the above layout.  The value is the component instance.
     * Note that the keys in the map are <b>not</b> the ids.
     */    
    
    private Map<String, UIComponent> setupInvokeOnComponentTree() {
        UIViewRoot root = new UIViewRoot();
        UIForm form1 = new UIForm();
        UIPanel panel1 = new UIPanel();
        UIInput input1 = new UIInput();
        UIInput input2 = new UIInput();
        UIForm form2 = new UIForm();
        UIPanel panel2 = new UIPanel();
        UIInput input3 = new UIInput();
        UIInput input4 = new UIInput();

        root.setId("root");
        form1.setId("form1");
        panel1.setId("panel");
        input1.setId("input1");
        input2.setId("input2");

        form2.setId("form2");
        panel2.setId("panel");
        input3.setId("input1");
        input4.setId("input2");
        
        root.getChildren().add(form1);
        form1.getChildren().add(panel1);
        panel1.getChildren().add(input1);
        panel1.getChildren().add(input2);

        root.getChildren().add(form2);
        form2.getChildren().add(panel2);
        panel2.getChildren().add(input3);
        panel2.getChildren().add(input4);
        Map<String, UIComponent> result = new HashMap<String,UIComponent>();
        result.put("root", root);
        result.put("form1", form1);
        result.put("panel1", panel1);
        result.put("input1", input1);
        result.put("input2", input2);
        result.put("form2", form2);
        result.put("panel2", panel2);
        result.put("input3", input3);
        result.put("input4", input4);

        return result;
    }
    
        
    public void testInvokeOnComponentPositive() throws Exception {

        Map<String, UIComponent> tree = setupInvokeOnComponentTree();
        
        UIViewRoot root = (UIViewRoot) tree.get("root");
        UIInput input1 = (UIInput) tree.get("input1");

        foundComponent = null;
        boolean result = false;

        result = root.invokeOnComponent(facesContext, 
                input1.getClientId(facesContext),
                new ContextCallback() {
            public void invokeContextCallback(FacesContext context, UIComponent component) {
                foundComponent = component;
            }});
        assertEquals(input1, foundComponent);
        assertTrue(result);

    }
    
    public void testInvokeOnComponentNegative() throws Exception {
        Map<String, UIComponent> tree = setupInvokeOnComponentTree();
        
        UIViewRoot root = (UIViewRoot) tree.get("root");
        UIInput input4 = (UIInput) tree.get("input4");

        foundComponent = null;
        boolean result = false;
        boolean exceptionThrown = false;

	// Negative case 0, null pointers
	exceptionThrown = false;
	FacesContext nullContext = null;
	ContextCallback nullCallback = null;
	try {
	    root.invokeOnComponent(nullContext, "form:input7", 
				   nullCallback);
	}
	catch (NullPointerException npe) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

 	exceptionThrown = false;
	try {
	    root.invokeOnComponent(facesContext, null, 
				   nullCallback);
	}
	catch (NullPointerException npe) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

 	exceptionThrown = false;
	try {
	    root.invokeOnComponent(nullContext, null, 
				   nullCallback);
	}
	catch (NullPointerException npe) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

        // Negative case 1, not found component.
        result = root.invokeOnComponent(facesContext, 
                "form:input7",
                new ContextCallback() {
            public void invokeContextCallback(FacesContext context, UIComponent component) {
                foundComponent = component;
            }});
        assertNull(foundComponent);
        assertTrue(!result);
        
        // Negative case 2A, callback throws exception with found component
        foundComponent = null;
        result = false;
        exceptionThrown = false;
        try {
            result = root.invokeOnComponent(facesContext,
                    "form2:input2",
                    new ContextCallback() {
                public void invokeContextCallback(FacesContext context, UIComponent component) {
                    foundComponent = component;
                    // When else am I going to get the chance to throw this exception?
                    throw new IllegalStateException();
                }});
        } catch (FacesException e) {
            assertTrue(e.getCause() instanceof IllegalStateException);
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        assertEquals(foundComponent, input4);
        assertTrue(!result);
        
        // Negative case 2B, callback throws exception with not found component
        foundComponent = null;
        result = false;
        exceptionThrown = false;
        try {
            result = root.invokeOnComponent(facesContext,
                    "form2:input6",
                    new ContextCallback() {
                public void invokeContextCallback(FacesContext context, UIComponent component) {
                    foundComponent = component;
                    // When else am I going to get the chance to throw this exception?
                    throw new IllegalStateException();
                }});
        } catch (FacesException e) {
            assertTrue(e.getCause() instanceof IllegalStateException);
            exceptionThrown = true;
        }
        assertTrue(!exceptionThrown);
        assertNull(foundComponent);
        assertTrue(!result);
        
        
    }
    
    public void testInvokeOnComponentWithPrependId() throws Exception {
        Map<String, UIComponent> tree = setupInvokeOnComponentTree();
        
        UIViewRoot root = (UIViewRoot) tree.get("root");
        UIForm truePrependIdForm = (UIForm) tree.get("form1");
        UIForm falsePrependIdForm = (UIForm) tree.get("form2");
        UIInput truePrependIdInput = (UIInput) tree.get("input2");
        UIInput falsePrependIdInput = (UIInput) tree.get("input3");
        
        truePrependIdForm.setPrependId(true);
        falsePrependIdForm.setPrependId(false);

        foundComponent = null;
        boolean result = false;
        boolean exceptionThrown = false;

        // Case 1, positive find with prependId == true
        result = root.invokeOnComponent(facesContext, 
                "form1:input2",
                new ContextCallback() {
            public void invokeContextCallback(FacesContext context, UIComponent component) {
                foundComponent = component;
            }});
        assertEquals(truePrependIdInput, foundComponent);
        assertTrue(result);
        
        // Case 2, negative find with prependId == true
        foundComponent = null;
        result = false;
        
        result = root.invokeOnComponent(facesContext, 
                "form9:input5",
                new ContextCallback() {
            public void invokeContextCallback(FacesContext context, UIComponent component) {
                foundComponent = component;
            }});
        assertNull(foundComponent);
        assertTrue(!result);
        
        // Case 3, exception positive find with prependId == true
        foundComponent = null;
        result = false;
        exceptionThrown = false;
        try {

            
            result = root.invokeOnComponent(facesContext, 
                    "form1:input2",
                    new ContextCallback() {
                public void invokeContextCallback(FacesContext context, UIComponent component) {
                    foundComponent = component;
                    throw new IllegalStateException();
                }});
        } 
        catch (FacesException e) {
            assertTrue(e.getCause() instanceof IllegalStateException);
            exceptionThrown = true;
        }
        assertEquals(truePrependIdInput, foundComponent);
        assertTrue(!result);
        assertTrue(exceptionThrown);

        // Case 4, exception negative find with prependId == true
        foundComponent = null;
        result = false;
        exceptionThrown = false;
        try {

            
            result = root.invokeOnComponent(facesContext, 
                    "formFozzy:inputKermit",
                    new ContextCallback() {
                public void invokeContextCallback(FacesContext context, UIComponent component) {
                    foundComponent = component;
                    throw new IllegalStateException();
                }});
        } 
        catch (FacesException e) {
            assertTrue(e.getCause() instanceof IllegalStateException);
            exceptionThrown = true;
        }
        assertNull(foundComponent);
        assertTrue(!result);
        assertTrue(!exceptionThrown);
        
        // Case 5, positive find with prependId == false
        result = root.invokeOnComponent(facesContext, 
                "input1",
                new ContextCallback() {
            public void invokeContextCallback(FacesContext context, UIComponent component) {
                foundComponent = component;
            }});
        assertEquals(falsePrependIdInput, foundComponent);
        assertTrue(result);

        // Case 6, negative find with prependId == false
        foundComponent = null;
        result = false;
        
        result = root.invokeOnComponent(facesContext, 
                "input99",
                new ContextCallback() {
            public void invokeContextCallback(FacesContext context, UIComponent component) {
                foundComponent = component;
            }});
        assertNull(foundComponent);
        assertTrue(!result);
        
        // Case 3, exception positive find with prependId == false
        foundComponent = null;
        result = false;
        exceptionThrown = false;
        try {

            
            result = root.invokeOnComponent(facesContext, 
                    "input1",
                    new ContextCallback() {
                public void invokeContextCallback(FacesContext context, UIComponent component) {
                    foundComponent = component;
                    throw new IllegalStateException();
                }});
        } 
        catch (FacesException e) {
            assertTrue(e.getCause() instanceof IllegalStateException);
            exceptionThrown = true;
        }
        assertEquals(falsePrependIdInput, foundComponent);
        assertTrue(!result);
        assertTrue(exceptionThrown);

        // Case 4, exception negative find with prependId == false
        foundComponent = null;
        result = false;
        exceptionThrown = false;
        try {

            
            result = root.invokeOnComponent(facesContext, 
                    "inputKermit",
                    new ContextCallback() {
                public void invokeContextCallback(FacesContext context, UIComponent component) {
                    foundComponent = component;
                    throw new IllegalStateException();
                }});
        } 
        catch (FacesException e) {
            assertTrue(e.getCause() instanceof IllegalStateException);
            exceptionThrown = true;
        }
        assertNull(foundComponent);
        assertTrue(!result);
        assertTrue(!exceptionThrown);

    
    }

    
    

}
