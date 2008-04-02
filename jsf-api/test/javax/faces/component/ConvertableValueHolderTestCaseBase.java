/*
 * $Id: ConvertableValueHolderTestCaseBase.java,v 1.2 2003/10/09 22:58:10 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
 * <p>Unit tests for {@link ConvertableValueHolder}.  Any test case for a
 * component class that implements {@link ConvertableValueHolder} should
 * extend this class.</p>
 */

public abstract class ConvertableValueHolderTestCaseBase
    extends ValueHolderTestCaseBase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ConvertableValueHolderTestCaseBase(String name) {
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
        return (new TestSuite(ConvertableValueHolderTestCaseBase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        ConvertableValueHolder cvh = (ConvertableValueHolder) component;

        assertEquals(cvh.getConverter(),
                     (String) component.getAttributes().get("converter"));
        cvh.setConverter(new LongConverter());
        assertNotNull((Converter) component.getAttributes().get("converter"));
        assertTrue(component.getAttributes().get("converter")
                   instanceof LongConverter);
        cvh.setConverter(null);
        assertNull(component.getAttributes().get("converter"));
        component.getAttributes().put("converter", new ShortConverter());
        assertNotNull(cvh.getConverter());
        assertTrue(cvh.getConverter() instanceof ShortConverter);
        component.getAttributes().put("converter", null);
        assertNull(cvh.getConverter());

        assertEquals(cvh.isValid(), true);
        assertEquals(cvh.isValid(),
                     ((Boolean) component.getAttributes().get("valid")).
                     booleanValue());
        cvh.setValid(false);
        assertEquals(cvh.isValid(),
                     ((Boolean) component.getAttributes().get("valid")).
                     booleanValue());
        component.getAttributes().put("valid", Boolean.TRUE);
        assertEquals(cvh.isValid(),
                     ((Boolean) component.getAttributes().get("valid")).
                     booleanValue());

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine ConvertableValueHolderBase instance
    public void testPristine() {

        super.testPristine();
        ConvertableValueHolder cvh = (ConvertableValueHolder) component;

        // Validate properties
        assertNull("no converter", cvh.getConverter());
        assertTrue("is valid", cvh.isValid());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        ConvertableValueHolder cvh = (ConvertableValueHolder) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        ConvertableValueHolder cvh = (ConvertableValueHolder) component;

        // converter
        cvh.setConverter(new LongConverter());
        assertTrue("expected converter",
                   cvh.getConverter() instanceof LongConverter);
        cvh.setConverter(null);
        assertNull("erased converter", cvh.getConverter());

        cvh.setValid(false);
        assertTrue(!cvh.isValid());
        cvh.setValid(true);
        assertTrue(cvh.isValid());

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
        assertEquals(nc1.getParseLocale(), nc2.getParseLocale());
        assertEquals(nc1.getPattern(), nc2.getPattern());
        assertEquals(nc1.getType(), nc2.getType());

    }


    // Check that the properties on the specified components are equal
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {

        super.checkProperties(comp1, comp2);
        ConvertableValueHolder cvh1 = (ConvertableValueHolder) comp1;
        ConvertableValueHolder cvh2 = (ConvertableValueHolder) comp2;
        checkNumberConverter((NumberConverter) cvh1.getConverter(),
                             (NumberConverter) cvh2.getConverter());
        assertEquals(cvh1.isValid(), cvh2.isValid());

    }


    // Populate a pristine component to be used in state holder tests
    protected void populateComponent(UIComponent component) {

        super.populateComponent(component);
        ConvertableValueHolder cvh = (ConvertableValueHolder) component;
        cvh.setConverter(createNumberConverter());
        cvh.setValid(false);

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
        assertEquals(nc1.getParseLocale(), nc2.getParseLocale());
        assertEquals(nc1.getPattern(), nc2.getPattern());
        assertEquals(nc1.getType(), nc2.getType());

    }


}
