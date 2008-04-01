/*
 * $Id: UIComponentTestCase.java,v 1.5 2002/06/13 17:48:14 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.Iterator;
import javax.faces.event.FacesEvent;
import javax.faces.validator.Validator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Test case for the <strong>javax.faces.UIComponent</strong>
 * concrete class.</p>
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

        component = new TestComponent();
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

        // rendersChildren
        assertEquals("rendersChildren1", component.getRendersChildren(),
                     ((Boolean) component.getAttribute("rendersChildren")).booleanValue());

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
        assertEquals("test2 compoundId", "/test2", test2.getCompoundId());

        // Insert "test1" component in front of "test2" component
        component.addChild(0, test1);
        checkChildCount(component, 2);
        assertTrue("test1 is a child", component.containsChild(test1));
        assertTrue("test2 is a child", component.containsChild(test2));
        assertTrue("test3 not a child", !component.containsChild(test3));
        assertEquals("test1 compoundId", "/test1", test1.getCompoundId());

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
        assertEquals("test3 compoundId", "/test2/test3",
                     test3.getCompoundId());

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

        try {
            component.findComponent("test4");
            fail("findComponent should throw IAE1");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        try {
            test2.findComponent("test3//");
            fail("findComponent should throw IAE2");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

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
                     component.findComponent("."));
        assertEquals("test1 find self", test1,
                     test1.findComponent("."));
        assertEquals("test2 find self", test2,
                     test2.findComponent("."));
        assertEquals("test3 find self", test3,
                     test3.findComponent("."));

        // Can a component find its parent?
        assertEquals("test1 find parent", component,
                     test1.findComponent(".."));
        assertEquals("test2 find parent", component,
                     test2.findComponent(".."));
        assertEquals("test3 find parent", test2,
                     test3.findComponent(".."));

        // Can a component find the root node?
        assertEquals("test1 find root", component,
                     test1.findComponent("/"));
        assertEquals("test2 find root", component,
                     test2.findComponent("/"));
        assertEquals("test3 find root", component,
                     test3.findComponent("/"));

        // Can a component find its child by name?
        assertEquals("component find test1", test1,
                     component.findComponent("test1"));
        assertEquals("component find test2", test2,
                     component.findComponent("test2"));
        assertEquals("test2 find test3", test3,
                     test2.findComponent("test3"));

        // Can a component find its siblings and relatives?
        assertEquals("test1 find test2", test2,
                     test1.findComponent("../test2"));
        assertEquals("test1 find test3", test3,
                     test1.findComponent("../test2/test3"));
        assertEquals("test2 find test1", test1,
                     test2.findComponent("../test1"));
        assertEquals("test3 find test1", test1,
                     test3.findComponent("../../test1"));

        // Can we go up or down two levels?
        assertEquals("component find test3", test3,
                     component.findComponent("test2/test3"));
        assertEquals("test3 find component", component,
                     test3.findComponent("../.."));

        // Can all components use absolute paths?
        assertEquals("component find test1", test1,
                     component.findComponent("/test1"));
        assertEquals("component find test2", test2,
                     component.findComponent("/test2"));
        assertEquals("component find test3", test3,
                     component.findComponent("/test2/test3"));
        assertEquals("test1 find test2", test2,
                     test1.findComponent("/test2"));
        assertEquals("test1 find test3", test3,
                     test1.findComponent("/test2/test3"));
        assertEquals("test2 find test1", test1,
                     test2.findComponent("/test1"));
        assertEquals("test2 find test3", test3,
                     test2.findComponent("/test2/test3"));
        assertEquals("test3 find test1", test1,
                     test3.findComponent("/test1"));
        assertEquals("test3 find test2", test2,
                     test3.findComponent("/test2"));

    }


    /**
     * [3.1.1] Component Type.
     */
    public void testComponentType() {

        assertEquals("componentType", "TestComponent",
                     component.getComponentType());

    }


    /**
     * [3.1] Invalid setter arguments.
     */
    public void testInvalidSetters() {


        // [3.1.2] setComponentId()
        try {
            component.setComponentId(null);
            fail("setComponentId did not throw NPE");
        } catch (NullPointerException e) {
            ; // Expected result
        }
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
            component.setAttribute("compoundId", "/foo/bar"); // Read-only prop
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
     * [3.1] Test the state of an unmodified test component.
     */
    public void testUnmodifiedComponent() {

        // [3.1.2] Component Identifiers
        String componentId = component.getComponentId();
        assertEquals("componentId", "test", componentId);
        String compoundId = component.getCompoundId();
        assertEquals("compoundId", "/", compoundId);

        // [3.1.3] Component Tree Manipulation
        assertNull("parent", component.getParent());
        checkChildCount(component, 0);

        // [3.1.4] Component Tree Navigation
        UIComponent result = null;
        result = component.findComponent(".");
        assertTrue("Can find self", result == component);

        // [3.1.5] Model Object References
        assertNull("modelReference", component.getModelReference());

        // [3.1.6] Local Values
        if (component instanceof UISelectBoolean) {
            assertNotNull("value", component.getValue());
        } else {
            assertNull("value", component.getValue());
        }
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
