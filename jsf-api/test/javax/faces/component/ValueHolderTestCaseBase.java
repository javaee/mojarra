/*
 * $Id: ValueHolderTestCaseBase.java,v 1.4 2003/10/09 22:58:15 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.TestUtil;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link ValueHolder}.  Any test case for a component
 * class that implements {@link ValueHolder} should extend this class.</p>
 */

public abstract class ValueHolderTestCaseBase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ValueHolderTestCaseBase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIOutput();
        expectedId = null;
        expectedRendererType = "Text";
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(ValueHolderTestCaseBase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        ValueHolder vh = (ValueHolder) component;

        assertEquals(vh.getValue(),
                     (String) component.getAttributes().get("value"));
        vh.setValue("foo");
        assertEquals("foo", (String) component.getAttributes().get("value"));
        vh.setValue(null);
        assertNull((String) component.getAttributes().get("value"));
        component.getAttributes().put("value", "bar");
        assertEquals("bar", vh.getValue());
        component.getAttributes().put("value", null);
        assertNull(vh.getValue());

        assertEquals(vh.getValueRef(),
                     (String) component.getAttributes().get("valueRef"));
        vh.setValueRef("foo");
        assertEquals("foo", (String) component.getAttributes().get("valueRef"));
        vh.setValueRef(null);
        assertNull((String) component.getAttributes().get("valueRef"));
        component.getAttributes().put("valueRef", "bar");
        assertEquals("bar", vh.getValueRef());
        component.getAttributes().put("valueRef", null);
        assertNull(vh.getValueRef());

    }


    // Test currentValue method
    public void testCurrentValue() {

        // Validate initial conditions
        ValueHolder vh = (ValueHolder) component;
        assertNull(vh.getValue());
        assertNull(vh.getValueRef());

        // Retrieve a local value
        vh.setValue("localValue");
        assertEquals("localValue", vh.currentValue(facesContext));
        vh.setValue(null);

        // Retrieve an application initialization parameter
        /* PENDING(craigmcc) - MockExternalContext support
        vh.setValueRef("initParam.appParamName");
        assertEquals("appParamValue", vh.currentValue(facesContext));
        assertNull(vh.getValue());
        vh.setValueRef(null);
        */

        // Retrieve an application scope attribute
        /* PENDING(craigmcc) - MockExternalContext support
        vh.setValueRef("applicationScope.appScopeName");
        assertEquals("appScopeValue", vh.currentValue(facesContext));
        assertNull(vh.getValue());
        vh.setValueRef(null);
        */

        // Retrieve a request scope attribute
        /* PENDING(craigmcc) - MockExternalContext support
        vh.setValueRef("requestScope.reqScopeName");
        assertEquals("reqScopeValue", vh.currentValue(facesContext));
        assertNull(vh.getValue());
        vh.setValueRef(null);
        */

        // Retrieve a session scope attribute
        /* PENDING(craigmcc) - MockExternalContext support
        vh.setValueRef("sessionScope.sesScopeName");
        assertEquals("sesScopeValue", vh.currentValue(facesContext));
        assertNull(vh.getValue());
        vh.setValueRef(null);
        */

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine ValueHolderBase instance
    public void testPristine() {

        super.testPristine();
        ValueHolder vh = (ValueHolder) component;

        // Validate properties
        assertNull("no value", vh.getValue());
        assertNull("no valueRef", vh.getValueRef());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        ValueHolder vh = (ValueHolder) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        ValueHolder vh = (ValueHolder) component;

        // value
        vh.setValue("foo.bar");
        assertEquals("expected value",
                     "foo.bar", vh.getValue());
        vh.setValue(null);
        assertNull("erased value", vh.getValue());

        // valueRef
        vh.setValueRef("customer.name");
        assertEquals("expected valueRef",
                     "customer.name", vh.getValueRef());
        vh.setValueRef(null);
        assertNull("erased valueRef", vh.getValueRef());

    }


    // --------------------------------------------------------- Support Methods


    // Check that the properties on the specified components are equal
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {

        super.checkProperties(comp1, comp2);
        ValueHolder vh1 = (ValueHolder) comp1;
        ValueHolder vh2 = (ValueHolder) comp2;
        assertEquals(vh1.getValue(), vh2.getValue());
        assertEquals(vh1.getValueRef(), vh2.getValueRef());

    }


    // Populate a pristine component to be used in state holder tests
    protected void populateComponent(UIComponent component) {

        super.populateComponent(component);
        ValueHolder vh = (ValueHolder) component;
        vh.setValue("component value");
        vh.setValueRef("component.value.ref");

    }


}
