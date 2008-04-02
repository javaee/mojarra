/*
 * $Id: ValueHolderTestCaseBase.java,v 1.9 2004/02/04 23:38:50 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.LongConverter;
import javax.faces.convert.NumberConverter;
import javax.faces.convert.ShortConverter;
import javax.faces.TestUtil;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link ValueHolder}.  Any test case for a
 * component class that implements {@link ValueHolder} should
 * extend this class.</p>
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

        assertEquals(vh.getConverter(),
                     (String) component.getAttributes().get("converter"));
        vh.setConverter(new LongConverter());
        assertNotNull((Converter) component.getAttributes().get("converter"));
        assertTrue(component.getAttributes().get("converter")
                   instanceof LongConverter);
        vh.setConverter(null);
        assertNull(component.getAttributes().get("converter"));
        component.getAttributes().put("converter", new ShortConverter());
        assertNotNull(vh.getConverter());
        assertTrue(vh.getConverter() instanceof ShortConverter);
        component.getAttributes().put("converter", null);
        assertNull(vh.getConverter());

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
        assertNull("no converter", vh.getConverter());

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

        // converter
        vh.setConverter(new LongConverter());
        assertTrue("expected converter",
                   vh.getConverter() instanceof LongConverter);
        vh.setConverter(null);
        assertNull("erased converter", vh.getConverter());
    }


    // --------------------------------------------------------- Support Methods


    // Check that the properties of the NumberConverters are equal
    protected void checkNumberConverter(NumberConverter nc1,
                                        NumberConverter nc2) {

        assertEquals(nc1.getCurrencyCode(), nc2.getCurrencyCode());
        assertEquals(nc1.getCurrencySymbol(), nc2.getCurrencySymbol());
        assertEquals(nc1.isGroupingUsed(), nc2.isGroupingUsed());
        assertEquals(nc1.isIntegerOnly(), nc2.isIntegerOnly());
        assertEquals(nc1.getMaxFractionDigits(), nc2.getMaxFractionDigits());
        assertEquals(nc1.getMaxIntegerDigits(), nc2.getMaxIntegerDigits());
        assertEquals(nc1.getMinFractionDigits(), nc2.getMinFractionDigits());
        assertEquals(nc1.getMinIntegerDigits(), nc2.getMinIntegerDigits());
        assertEquals(nc1.getLocale(), nc2.getLocale());
        assertEquals(nc1.getPattern(), nc2.getPattern());
        assertEquals(nc1.getType(), nc2.getType());

    }


    // Check that the properties on the specified components are equal
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {

        super.checkProperties(comp1, comp2);
        ValueHolder vh1 = (ValueHolder) comp1;
        ValueHolder vh2 = (ValueHolder) comp2;
        assertEquals(vh1.getValue(), vh2.getValue());
        checkNumberConverter((NumberConverter) vh1.getConverter(),
                             (NumberConverter) vh2.getConverter());
    }


    // Populate a pristine component to be used in state holder tests
    protected void populateComponent(UIComponent component) {

        super.populateComponent(component);
        ValueHolder vh = (ValueHolder) component;
        vh.setValue("component value");
        vh.setConverter(createNumberConverter());

    }


    // Create and configure a NumberConverter
    protected NumberConverter createNumberConverter() {

        NumberConverter nc = new NumberConverter();
        nc.setCurrencyCode("USD");
        nc.setCurrencySymbol("$");
        nc.setGroupingUsed(false);
        nc.setIntegerOnly(true);
        nc.setMaxFractionDigits(2);
        nc.setMaxIntegerDigits(10);
        nc.setMinFractionDigits(2);
        nc.setMinIntegerDigits(5);
        nc.setType("currency");
        return (nc);

    }


    protected void checkNumberConverters(NumberConverter nc1,
                               NumberConverter nc2) {

        assertNotNull(nc1);
        assertNotNull(nc2);
        assertEquals(nc1.getCurrencyCode(), nc2.getCurrencyCode());
        assertEquals(nc1.getCurrencySymbol(), nc2.getCurrencySymbol());
        assertEquals(nc1.isGroupingUsed(), nc2.isGroupingUsed());
        assertEquals(nc1.isIntegerOnly(), nc2.isIntegerOnly());
        assertEquals(nc1.getMaxFractionDigits(), nc2.getMaxFractionDigits());
        assertEquals(nc1.getMaxIntegerDigits(), nc2.getMaxIntegerDigits());
        assertEquals(nc1.getMinFractionDigits(), nc2.getMinFractionDigits());
        assertEquals(nc1.getMinIntegerDigits(), nc2.getMinIntegerDigits());
        assertEquals(nc1.getLocale(), nc2.getLocale());
        assertEquals(nc1.getPattern(), nc2.getPattern());
        assertEquals(nc1.getType(), nc2.getType());

    }


}
