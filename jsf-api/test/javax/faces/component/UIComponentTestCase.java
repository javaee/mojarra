/*
 * $Id: UIComponentTestCase.java,v 1.30 2003/04/29 18:51:51 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.Introspector;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
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
     * The names of the attributes that should be found in an unmodified
     * instance of this component.
     */
    protected String attributes[] = null;


    /**
     * The component to be tested for each test.
     */
    protected UIComponent component = null;


    /**
     * The default rendererType that we expect in a new component instance.
     */
    protected String rendererType = null;


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
        attributes = new String[0];

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
        UIForm form3 = new UIForm();
        form3.setComponentId("form3");

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

        // Add the same child twice
        try {
            component.addChild(form3);
            component.addChild(form3);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalStateException e) {
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


        // Empty string component id
	exceptionThrown = false; // expected result
        try {
	    UIForm emptyString = new UIForm();
	    emptyString.setComponentId("");
            component.addChild(emptyString);
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }
	assertTrue(!exceptionThrown);
	
	// Make sure we can rename the component, and the clientId
	// changes.

        /**
         * PENDING - comment out because we don't initialize the
         * RenderKit so getClientId() will not work
	FacesContext context = new MockFacesContext();
	String form1ClientId = form1.getClientId(context);
	form1.setComponentId("form30");
	assertTrue(!form1ClientId.equals(form1.getClientId(context)));
         */
	    
    }

    public void testComponentIdValidityNegative() {
	UIForm form = new UIForm();

        try {
            form.setComponentId(" startsWithSpace");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            form.setComponentId(" endsWithSpace");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            form.setComponentId("hasInvalidChars[");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            form.setComponentId("hasInvalidChars.");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testComponentIdValidityPositive() {
	UIForm form = new UIForm();

        try {
            form.setComponentId("allchars");
        } catch (Throwable e) {
            fail("Should not throw anything");
        }

        try {
            form.setComponentId("chars090and212numbers1212");
        } catch (Throwable e) {
            fail("Should not throw anything");
        }

        try {
            form.setComponentId("chars-dashes");
        } catch (Throwable e) {
            fail("Should not throw anything");
        }

        try {
            form.setComponentId("chars_underscores");
        } catch (Throwable e) {
            fail("Should not throw anything");
        }

        try {
            form.setComponentId("chars090numbers--dashes__underscores_");
        } catch (Throwable e) {
            fail("Should not throw anything");
        }

    }    /**
     * [3.1.7] Attribute/Property Transparency.
     */

    public void testAttributePropertyTransparency() {

        // Removed because we undid the transparency requirement

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
        assertTrue("facet test1 added",
                   test1 == component.getFacet("test1"));
	assertTrue("facet test1 parent",
                   component == test1.getParent());
        checkFacetCount(component, 1);
        component.addFacet("test2", test2);
        assertTrue("facet test2 added",
                   test2 == component.getFacet("test2"));
	assertTrue("facet test2 parent",
                   component == test2.getParent());
        checkFacetCount(component, 2);
        component.addFacet("test3", test3);
        assertTrue("facet test3 added",
                   test3 == component.getFacet("test3"));
	assertTrue("facet test3 parent",
                   component == test3.getParent());
        checkFacetCount(component, 3);

        // Replace an existing facet
        component.addFacet("test3", test3dup);
        assertTrue("facet test3 replaced",
                   test3dup == component.getFacet("test3"));
        assertTrue("facet test3 new parent",
                   component == test3dup.getParent());
        assertTrue("facet test3 old parent",
                   null == test3.getParent());
        checkFacetCount(component, 3);

	// Note that this doesn't throw any exception
        facet = component.getFacet("test1");
        assertEquals("test1 returned", test1, facet);
        facet = component.getFacet("test2");
        assertEquals("test2 returned", test2, facet);
        facet = component.getFacet("test3");
        assertEquals("test3dup returned", test3dup, facet);

        // Remove a facet
        component.removeFacet("test2");
        assertTrue("facet test2 no parent",
                   null == test2.getParent());
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
     * Test <code>getFacetsAndChildren()</code> method. Make sure the 
     * facets are returned first followed by children in the order 
     * they are stored in the child list.
     */
    public void testGetFacetsAndChildren() {

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
        testComponent.addChild(child1);
        testComponent.addFacet("facet2", facet2);
        testComponent.addChild(child2);
        testComponent.addFacet("facet3", facet3);
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


    /**
     * Test results returned by introspecting the component class.
     */
    public void testIntrospection() throws Exception {

        Class clazz = null;
        Method method = null;

        BeanInfo binfo = Introspector.getBeanInfo(component.getClass());
        assertNotNull(binfo);
        BeanDescriptor bdesc = binfo.getBeanDescriptor();
        assertNotNull(bdesc);
        System.out.println();
        System.out.println("BeanDescriptor Contents:");
        System.out.println("        beanClass=" +
                           bdesc.getBeanClass().getName());
        Enumeration anames = bdesc.attributeNames();
        while (anames.hasMoreElements()) {
            String name = (String) anames.nextElement();
            System.out.println("    attributeName=" + name +
                               ", value=" + bdesc.getValue(name));
        }
        System.out.println("      displayName=" + bdesc.getDisplayName());
        System.out.println("           expert=" + bdesc.isExpert());
        System.out.println("           hidden=" + bdesc.isHidden());
        System.out.println("             name=" + bdesc.getName());
        System.out.println("        preferred=" + bdesc.isPreferred());
        System.out.println(" shortDescription=" + bdesc.getShortDescription());
        System.out.println("------------------------");
        EventSetDescriptor edescs[] = binfo.getEventSetDescriptors();
        if (edescs == null) {
            edescs = new EventSetDescriptor[0];
        }
        for (int i = 0; i < edescs.length; i++) {
            EventSetDescriptor edesc = edescs[i];
            System.out.println("EventSetDescriptor Contents:");
            method = edesc.getAddListenerMethod();
            if (method != null) {
                System.out.println("    addListenerMethod=" +
                                   method.getName());
            }
            System.out.println("          displayName=" +
                               edesc.getDisplayName());
            System.out.println("               expert=" + edesc.isExpert());
	    Method methods []  = edesc.getListenerMethods();
            if (methods != null) {
		if ((method = methods[0]) != null) {
		    System.out.println("    getListenerMethod=" +
				       method.getName());
		}
            }
            System.out.println("               hidden=" + bdesc.isHidden());
            System.out.println("    inDefaultEventSet=" +
                               edesc.isInDefaultEventSet());
            clazz = edesc.getListenerType();
            if (clazz != null) {
                System.out.println("         listenerType=" +
                                   clazz.getName());
            }
            System.out.println("            preferred=" + bdesc.isPreferred());
            method = edesc.getRemoveListenerMethod();
            if (method != null) {
                System.out.println(" removeListenerMethod=" +
                                   method.getName());
            }
            System.out.println("     shortDescription=" +
                               bdesc.getShortDescription());
            System.out.println("              unicast=" +
                               edesc.isUnicast());
            System.out.println("----------------------------");
        }

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
        /* Comment out because transparency removed
        try {
            component.setAttribute("componentType", "foo"); // Read-only prop
            fail("setAttribute did not throw IAE");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }
        */

        /* Comment out because transparency removed
        try {
            component.setAttribute("rendersChildren", Boolean.FALSE); // Read-only prop
            fail("setAttribute did not throw IAE");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }
        */

        /* Comment out because transparency removed
        try {
            component.setAttribute("rendersSelf", Boolean.FALSE); // Read-only prop
            fail("setAttribute did not throw IAE");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }
        */

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
        assertEquals("componentId correct", "test", componentId);

        // [3.1.3] Component Tree Manipulation
        assertNull("parent null", component.getParent());
        checkChildCount(component, 0);

        // [3.1.4] Component Tree Navigation
        UIComponent result = null;
        result = component.findComponent(component.getComponentId());
        assertTrue("Can find self", result == component);

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
        assertTrue("valid true", !component.isValid());
        checkValidatorCount(component, 0);

        // Renderer Type
        if (rendererType == null) {
            assertNull("rendererType null", component.getRendererType());
        } else {
            assertEquals("rendererType correct",
                         rendererType, component.getRendererType());
        }

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

    
}
