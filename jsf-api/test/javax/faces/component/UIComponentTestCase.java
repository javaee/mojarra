/*
 * $Id: UIComponentTestCase.java,v 1.24 2003/02/14 00:40:11 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import java.util.Collections;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.validator.Validator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.faces.mock.MockFacesContext;
import javax.faces.mock.MockHttpServletRequest;
import javax.faces.mock.MockServletContext;


/**
 * <p>Base unit tests for all UIComponent classes.</p>
 */

public class UIComponentTestCase extends TestCase {


    // ----------------------------------------------------- Instance Variables


    /**
     * The component to be tested for each test.
     */
    protected UIComponent component = null;


    /**
     * The names of the attributes that should be found in an unmodified
     * instance of this component.
     */
    protected String attributes[] = null;


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIComponentTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        component = new TestComponentNamingContainer();
        attributes = new String[]
            { "componentId" };

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UIComponentTestCase.class));

    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        component = null;

    }


    // ------------------------------------------------ Individual Test Methods


    /**
     * [3.1.3] Negative tests for <code>addChild()</code>.
     */
    public void testAddChildNegative() {
        // Child components we will need
        UIForm form1 = new UIForm();
        form1.setComponentId("form1");
        UIForm form1a = new UIForm();
        form1a.setComponentId("form1"); // Duplicate id
        UIForm form2 = new UIForm();
        form2.setComponentId("form2");

        // Add first child explicitly
        component.addChild(form1);

        // Null child - simple
        try {
            component.addChild(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }

        // Null child - indexed
        try {
            component.addChild(0, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }

        // Duplicate component id - simple
        try {
            component.addChild(form1a);
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            ; // Expected result
        }

        // Duplicate component id - indexed
        try {
            component.addChild(0, form1a);
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            ; // Expected result
        }

        // Index out of bounds - low
        try {
            component.addChild(-1, form2);
            fail("Should have thrown IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            ; // Expected result
        }

        // Index out of bounds - high
        try {
            component.addChild(2, form2);
            fail("Should have thrown IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            ; // Expected result
        }

    }

    public void testAddChildPositive() {

        // Child components we will need
        UIForm form1 = new UIForm();
        form1.setComponentId("form1");
        UIForm form1a = new UIForm();
        form1a.setComponentId("form1"); // Duplicate id
        UIForm form2 = new UIForm();
        form2.setComponentId("form2");

        // Add first child explicitly
        component.addChild(form1);
	boolean exceptionThrown = false; // expected result

        // No component id - simple
        try {
            component.addChild(new UIForm());
        } catch (IllegalArgumentException e) {
	    exceptionThrown = true;
        }
	assertTrue(!exceptionThrown);

	exceptionThrown = false; // expected result
        // No component id - indexed
        try {
            component.addChild(0, new UIForm());
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }
	assertTrue(!exceptionThrown);

	// Make sure we can rename the component, and the clientId
	// changes.
	FacesContext context = new MockFacesContext();
	String form1ClientId = form1.getClientId(context);
	form1.setComponentId("form30");
	assertTrue(!form1ClientId.equals(form1.getClientId(context)));
	    
    }


    /**
     * [3.1.7] Attribute/Property Transparency.
     */
    public void testAttributePropertyTransparency() {

        // componentId
        assertNotNull("componentId1",component.getComponentId());
        assertNotNull("componentId2",
                     (String) component.getAttribute("componentId"));
        assertEquals("componentId12", component.getComponentId(),
                     (String) component.getAttribute("componentId"));
        component.setComponentId("foo");
        assertEquals("componentId3", "foo", component.getComponentId());
        assertEquals("componentId4", "foo",
                     (String) component.getAttribute("componentId"));
        component.setAttribute("componentId", "bar");
        assertEquals("componentId5", "bar", component.getComponentId());
        assertEquals("componentId6", "bar",
                     (String) component.getAttribute("componentId"));
        component.setAttribute("componentId", null);
        assertNull("componentId7", component.getComponentId());
        assertNull("componentId8", component.getAttribute("componentId"));

        // converter
        assertNull("conv1", component.getConverter());
        assertNull("conv2", component.getAttribute("converter"));
        component.setConverter("foo");
        assertEquals("conv3", "foo", component.getConverter());
        assertEquals("conv4", "foo",
                     (String) component.getAttribute("converter"));
        component.setAttribute("converter", "bar");
        assertEquals("conv5", "bar", component.getConverter());
        assertEquals("conv6", "bar",
                     (String) component.getAttribute("converter"));
        component.setAttribute("converter", null);
        assertNull("conv7", component.getConverter());
        assertNull("conv8", component.getAttribute("converter"));

        // modelReference
        assertNull("model1", component.getModelReference());
        assertNull("model2", component.getAttribute("modelReference"));
        component.setModelReference("${foo}");
        assertEquals("model3", "${foo}", component.getModelReference());
        assertEquals("model4", "${foo}",
                     (String) component.getAttribute("modelReference"));
        component.setAttribute("modelReference", "${bar}");
        assertEquals("model5", "${bar}", component.getModelReference());
        assertEquals("model6", "${bar}",
                     (String) component.getAttribute("modelReference"));
        component.setAttribute("modelReference", null);
        assertNull("model7", component.getModelReference());
        assertNull("model8", component.getAttribute("modelReference"));

        // parent
        assertNull("parent1", component.getParent());
        assertNull("parent2", component.getAttribute("parent"));
        UIComponent test1 = new TestComponent("test1");
        component.addChild(test1);
        assertNotNull("parent3", test1.getParent());
        assertNotNull("parent4", test1.getAttribute("parent"));
        assertEquals("parent5", test1.getParent(),
                     (UIComponent) test1.getAttribute("parent"));

        // rendererType
        assertNull("rendererType1", component.getRendererType());
        assertNull("rendererType2", component.getAttribute("rendererType"));
        component.setRendererType("foo");
        assertEquals("rendererType3", "foo", component.getRendererType());
        assertEquals("rendererType4", "foo",
                     (String) component.getAttribute("rendererType"));
        component.setAttribute("rendererType", "bar");
        assertEquals("rendererType5", "bar", component.getRendererType());
        assertEquals("rendererType6", "bar",
                     (String) component.getAttribute("rendererType"));
        component.setAttribute("rendererType", null);
        assertNull("rendererType7", component.getRendererType());
        assertNull("rendererType8", component.getAttribute("rendererType"));

        // rendersChildren and rendersSelf
        assertEquals("rendersChildren1", component.getRendersChildren(),
                     ((Boolean) component.getAttribute("rendersChildren")).booleanValue());
        assertEquals("rendersSelf1", component.getRendersSelf(),
                     ((Boolean) component.getAttribute("rendersSelf")).booleanValue());

        // value
        assertNull("value1", component.getValue());
        assertNull("value2", component.getAttribute("value"));
        component.setValue("foo");
        assertEquals("value3", "foo", component.getValue());
        assertEquals("value4", "foo",
                     (String) component.getAttribute("value"));
        component.setAttribute("value", "bar");
        assertEquals("value5", "bar", component.getValue());
        assertEquals("value6", "bar",
                     (String) component.getAttribute("value"));
        component.setAttribute("value", null);
        assertNull("value7", component.getValue());
        assertNull("value8", component.getAttribute("value"));

    }


    /**
     * [3.1.3] Component Tree Manipulation.
     */
    public void testComponentTreeManipulation() {

	FacesContext context = new MockFacesContext();

        UIComponent test1 = new TestComponent("test1");
        UIComponent test2 = new TestComponent("test2");
        UIComponent test3 = new TestComponent("test3");

        // Review initial conditions
        assertEquals("No children yet", 0, component.getChildCount());
        assertTrue("test1 not a child", !component.containsChild(test1));
        assertTrue("test2 not a child", !component.containsChild(test2));
        assertTrue("test3 not a child", !component.containsChild(test3));
        checkChildCount(component, 0);
        checkChildCount(test1, 0);
        checkChildCount(test2, 0);
        checkChildCount(test3, 0);

        // Add "test2" component as the only child
        component.addChild(test2);
        checkChildCount(component, 1);
        assertTrue("test1 not a child", !component.containsChild(test1));
        assertTrue("test2 is a child", component.containsChild(test2));
        assertTrue("test3 not a child", !component.containsChild(test3));
        assertEquals("test2 clientSideId", "test2", 
		     test2.getClientId(context));

        // Insert "test1" component in front of "test2" component
        component.addChild(0, test1);
        checkChildCount(component, 2);
        assertTrue("test1 is a child", component.containsChild(test1));
        assertTrue("test2 is a child", component.containsChild(test2));
        assertTrue("test3 not a child", !component.containsChild(test3));
        assertEquals("test1 clientSideId", "test1", 
		     test1.getClientId(context));

        // Add "test3" component as child of "test2" component
        test2.addChild(test3);
        checkChildCount(component, 2);
        checkChildCount(test1, 0);
        checkChildCount(test2, 1);
        checkChildCount(test3, 0);
        assertTrue("test1 is a child", component.containsChild(test1));
        assertTrue("test2 is a child", component.containsChild(test2));
        assertTrue("test3 not a child", !component.containsChild(test3));
        assertTrue("test3 is a child", test2.containsChild(test3));
        assertEquals("test3 clientSideId", "test3",
		     test3.getClientId(context));

        // Verify the correct children are present in the correct order
        Iterator kids = component.getChildren();
        if (!kids.hasNext()) {
            fail("Less than one child present");
        }
        UIComponent child1 = (UIComponent) kids.next();
        assertEquals("test1 is first child", test1, child1);
        if (!kids.hasNext()) {
            fail("Less than two children present");
        }
        UIComponent child2 = (UIComponent) kids.next();
        assertEquals("test2 is second child", test2, child2);
        assertTrue("Exactly two children", !kids.hasNext());
        assertEquals("test1 by index", test1, component.getChild(0));
        assertEquals("test2 by index", test2, component.getChild(1));

        // Remove children tests
        test2.removeChild(0);
        checkChildCount(test2, 0);
        component.removeChild(test1);
        checkChildCount(component, 1);

        // Clear children and review conditions
        component.clearChildren();
        checkChildCount(component, 0);

    }


    /**
     * [3.1.3] Component Tree Navigation with invalid findComponent arguments.
     */
    public void testComponentTreeNavigationInvalid() {

        UIComponent test1 = new TestComponent("test1");
        UIComponent test2 = new TestComponent("test2");
        UIComponent test3 = new TestComponent("test3");
        component.addChild(test1);
        component.addChild(test2);
        test2.addChild(test3);

	assertTrue(null == component.findComponent("test4"));

	assertTrue(null == test2.findComponent("test3//"));

        try {
            component.findComponent(null);
            fail("findComponent should throw NPE");
        } catch (NullPointerException e) {
            ; // Expected result
        }


    }


    /**
     * [3.1.3] Component Tree Navigation with valid findComponent() arguments.
     */
    public void testComponentTreeNavigationValid() {

        UIComponent test1 = new TestComponent("test1");
        UIComponent test2 = new TestComponent("test2");
        UIComponent test3 = new TestComponent("test3");
        component.addChild(test1);
        component.addChild(test2);
        test2.addChild(test3);

        // Can a component find itself?
        assertEquals("component find self", component,
                     component.findComponent(component.getComponentId()));
        assertEquals("test1 find self", test1,
                     test1.findComponent(test1.getComponentId()));
        assertEquals("test2 find self", test2,
                     test2.findComponent(test2.getComponentId()));
        assertEquals("test3 find self", test3,
                     test3.findComponent(test3.getComponentId()));

        // Can a component find its parent?
        assertEquals("test1 find parent", component,
                     test1.findComponent(test1.getParent().getComponentId()));
        assertEquals("test2 find parent", component,
                     test2.findComponent(test2.getParent().getComponentId()));
        assertEquals("test3 find parent", test2,
                     test3.findComponent(test3.getParent().getComponentId()));

        // Can a component find its child by name?
        assertEquals("component find test1", test1,
                     component.findComponent("test1"));
        assertEquals("component find test2", test2,
                     component.findComponent("test2"));
        assertEquals("test2 find test3", test3,
                     test2.findComponent("test3"));
        assertEquals("component find test3", test3,
                     component.findComponent("test3"));

    }


    /**
     * [3.1.1] Component Type.
     */
    public void testComponentType() {

        assertEquals("componentType", "TestComponent",
                     component.getComponentType());

    }


    /**
     * [3.1.12] Facet manipulation.
     */
    public void testFacetManipulation() {

        UIComponent test1 = new TestComponent("test1");
        UIComponent test2 = new TestComponent("test2");
        UIComponent test3 = new TestComponent("test3");
        UIComponent test3dup = new TestComponent("test3"); // Same component id
        UIComponent facet = null;

        // Review initial conditions
        checkFacetCount(component, 0);

        // Add facets one at a time and check the count
        component.addFacet("test1", test1);
	assertEquals("facet.getAttribute(\"facetParent\") returned", 
		     test1.getAttribute(UIComponent.FACET_PARENT_ATTR), 
		     component);
        checkFacetCount(component, 1);
        component.addFacet("test2", test2);
	assertEquals("facet.getAttribute(\"facetParent\") returned", 
		     test2.getAttribute(UIComponent.FACET_PARENT_ATTR), 
		     component);
        checkFacetCount(component, 2);
        component.addFacet("test3", test3);
	assertEquals("facet.getAttribute(\"facetParent\") returned", 
		     test3.getAttribute(UIComponent.FACET_PARENT_ATTR), 
		     component);
        checkFacetCount(component, 3);

        // All added facets must be individually retrievable
        facet = component.getFacet("test1");
        assertEquals("test1 returned", test1, facet);
        facet = component.getFacet("test2");
        assertEquals("test2 returned", test2, facet);
        facet = component.getFacet("test3");
        assertEquals("test3 returned", test3, facet);

        // Replace an existing facet

        component.addFacet("test3", test3dup);
	assertEquals("facet.getAttribute(\"facetParent\") returned", 
		     test3dup.getAttribute(UIComponent.FACET_PARENT_ATTR), 
		     component);
	// Note that this doesn't throw any exception
        checkFacetCount(component, 3);
        facet = component.getFacet("test1");
        assertEquals("test1 returned", test1, facet);
        facet = component.getFacet("test2");
        assertEquals("test2 returned", test2, facet);
        facet = component.getFacet("test3");
        assertEquals("test3dup returned", test3dup, facet);

        // Remove a facet
        component.removeFacet("test2");
	assertEquals("facet.getAttribute(\"facetParent\") returned", 
		     test2.getAttribute(UIComponent.FACET_PARENT_ATTR), 
		     null);
        checkFacetCount(component, 2);
        facet = component.getFacet("test1");
        assertEquals("test1 returned", test1, facet);
        facet = component.getFacet("test2");
        assertNull("test2 not returned", facet);
        facet = component.getFacet("test3");
        assertEquals("test3dup returned", test3dup, facet);

        // Clear all facets
        component.clearFacets();
        checkFacetCount(component, 0);
        facet = component.getFacet("test1");
        assertNull("test1 not returned", facet);
        facet = component.getFacet("test2");
        assertNull("test2 not returned", facet);
        facet = component.getFacet("test3");
        assertNull("test3 not returned", facet);

    }


    /**
     * [3.1] Invalid setter arguments.
     */
    public void testInvalidSetters() {


        // [3.1.2] setComponentId()
	boolean exceptionThrown = false;
        try {
            component.setComponentId(null);
        } catch (NullPointerException e) {
	    exceptionThrown = true;
        }
	assertTrue(!exceptionThrown);
        /** FIXME - checking valid characters not yet implemented
        try {
            component.setComponentId("*");
            fail("setComponentId did not throw IAE");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }
        */

        // [3.1.7] setAttribute()
        try {
            component.setAttribute(null, "bar");
            fail("setAttribute did not throw NPE");
        } catch (NullPointerException e) {
            ; // Expected result
        }
        try {
            component.setAttribute("componentType", "foo"); // Read-only prop
            fail("setAttribute did not throw IAE");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        try {
            component.setAttribute("rendersChildren", Boolean.FALSE); // Read-only prop
            fail("setAttribute did not throw IAE");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        try {
            component.setAttribute("rendersSelf", Boolean.FALSE); // Read-only prop
            fail("setAttribute did not throw IAE");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        // [3.1.9] addValidator()
        try {
            component.addValidator(null);
            fail("addValidator did not throw NPE");
        } catch (NullPointerException e) {
            ; // Expected result
        }

    }


    /**
     * [3.1.10] Renders Children.
     */
    public void testRendersChildren() {

        assertTrue("rendersChildren", !component.getRendersChildren());

    }


    /**
     * [3.1.11] Renders Self.
     */
    public void testRendersSelf() {

        if ((component instanceof UIParameter) ||
            (component instanceof UISelectItem) ||
            (component instanceof UISelectItems)) {
            assertTrue("rendersSelf", component.getRendersSelf());
        } else {
            assertTrue("rendersSelf", !component.getRendersSelf());
        }

    }


    /**
     * [3.1] Test the state of an unmodified test component.
     */
    public void testUnmodifiedComponent() {

        // [3.1.2] Component Identifiers
        String componentId = component.getComponentId();
        assertEquals("componentId", "test", componentId);

        // [3.1.3] Component Tree Manipulation
        assertNull("parent", component.getParent());
        checkChildCount(component, 0);

        // [3.1.4] Component Tree Navigation
        UIComponent result = null;
        result = component.findComponent(component.getComponentId());
        assertTrue("Can find self", result == component);

        // [3.1.5] Model Object References
        assertNull("modelReference", component.getModelReference());

        // [3.1.6] Local Values
        assertNull("value", component.getValue());
        // FIXME - assertNull("currentValue", component.currentValue(context));

        // [3.1.7] Generic Attributes
        Iterator names = component.getAttributeNames();
        while (names.hasNext()) {
            String name = (String) names.next();
            boolean found = false;
            for (int i = 0; i < attributes.length; i++) {
                if (name.equals(attributes[i])) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                fail("Invalid attribute name '" + name + "' found");
            }
        }

        // [3.1.9] Validation Processing
        assertTrue("valid", !component.isValid());
        checkValidatorCount(component, 0);

        // [3.1.10] Decoding and Encoding
        assertNull("rendererType", component.getRendererType());

    }



    /**
     * [3.1.9] Validator Queue.
     */
    public void testValidatorQueue() {

        checkValidatorCount(component, 0);
        component.addValidator(new TestValidator());
        checkValidatorCount(component, 1);
        component.addValidator(new TestValidator());
        checkValidatorCount(component, 2);
        component.clearValidators();
        checkValidatorCount(component, 0);

    }


    // -------------------------------------------------------- Support Methods


    /**
     * Validate that the specified number of children are present.
     *
     * @param component Component being tested
     * @param count Expected number of children
     */
    protected void checkChildCount(UIComponent component, int count) {

        assertEquals("childCount", count, component.getChildCount());
        int results = 0;
        Iterator kids = component.getChildren();
        assertNotNull("children", kids);
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            results++;
        }
        assertEquals("child count", count, results);

    }


    /**
     * Validate that the specified number of facets are present.
     *
     * @param component Component being tested
     * @param count Expected number of facets
     */
    protected void checkFacetCount(UIComponent component, int count) {

        int results = 0;
        Iterator names = component.getFacetNames();
        assertNotNull("children", names);
        while (names.hasNext()) {
            String name = (String) names.next();
            results++;
        }
        assertEquals("facet count", count, results);

    }


    /**
     * Validate that the specified number of validators are present.
     *
     * @param component Component being tested
     * @param count Expected number of validators
     */
    protected void checkValidatorCount(UIComponent component, int count) {

        int results = 0;
        Iterator validators = component.getValidators();
        assertNotNull("validators", validators);
        while (validators.hasNext()) {
            Validator validator = (Validator) validators.next();
            results++;
        }
        assertEquals("validator count", count, results);

    }
    
    /**
     * Test <code>getFacetsAndChildren()</code> method. Make sure the 
     * facets are returned first followed by children in the order 
     * they are stored in the child list.
     */
    public void testGetChildrenAndFacets() {

        UIComponent testComponent = new TestComponentNamingContainer();
        UIComponent facet1 = new TestComponent("facet1");
        UIComponent facet2 = new TestComponent("facet2");
        UIComponent facet3 = new TestComponent("facet3");

        UIComponent child1 = new TestComponent("child1");
        UIComponent child2 = new TestComponent("child2");
        UIComponent child3 = new TestComponent("child3");
        UIComponent child = null;

        // Review initial conditions.
        // make sure the itertaor is empty before any children or facet is
        // added.
        Iterator kidItr = null;
        kidItr = testComponent.getFacetsAndChildren();
        assertTrue((kidItr.hasNext()) == false);

        // Add facets and children one at a time.
        testComponent.addFacet("facet1", facet1);
	assertEquals("facet.getAttribute(\"facetParent\") returned", 
		     facet1.getAttribute(UIComponent.FACET_PARENT_ATTR), 
		     testComponent);
        testComponent.addChild(child1);
        testComponent.addFacet("facet2", facet2);
	assertEquals("facet.getAttribute(\"facetParent\") returned", 
		     facet2.getAttribute(UIComponent.FACET_PARENT_ATTR), 
		     testComponent);
        testComponent.addChild(child2);
        testComponent.addFacet("facet3", facet3);
	assertEquals("facet.getAttribute(\"facetParent\") returned", 
		     facet3.getAttribute(UIComponent.FACET_PARENT_ATTR), 
		     testComponent);
        testComponent.addChild(child3);

        // make sure the facets and children are returned in the correct order.
        kidItr = testComponent.getFacetsAndChildren();
        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(facet3) || child.equals(facet2) ||
                child.equals(facet1));

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(facet3) || child.equals(facet2) ||
                child.equals(facet1));

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(facet3) || child.equals(facet2) ||
                child.equals(facet1));

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child1));

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child2));

        child = (UIComponent) kidItr.next();
        assertTrue(child.equals(child3));
    }


}
