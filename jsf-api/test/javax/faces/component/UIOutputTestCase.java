/*
 * $Id: UIOutputTestCase.java,v 1.13 2003/11/07 01:23:57 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.StateHolder;
import javax.faces.component.ValueHolder;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.LongConverter;
import javax.faces.convert.ShortConverter;
import javax.faces.TestUtil;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIOutput}.</p>
 */

public class UIOutputTestCase extends ConvertibleValueHolderTestCaseBase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIOutputTestCase(String name) {
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
        return (new TestSuite(UIOutputTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UIOutput output = (UIOutput) component;

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine UIOutput instance
    public void testPristine() {

        super.testPristine();
        UIOutput output = (UIOutput) component;

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIOutput output = (UIOutput) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIOutput output = (UIOutput) component;

    }


    public void testValueBindings() {

	super.testValueBindings();
	UIOutput test = (UIOutput) component;

	request.setAttribute("foo", new LongConverter());
	test.setConverter(new ShortConverter());
	assertNotNull(test.getConverter());
	assertTrue(test.getConverter() instanceof ShortConverter);
	test.setValueBinding("converter", application.getValueBinding("#{foo}"));
	assertNotNull(test.getConverter());
	assertTrue(test.getConverter() instanceof LongConverter);
	test.setConverter(new ShortConverter());
	assertNotNull(test.getConverter());
	assertTrue(test.getConverter() instanceof ShortConverter);
	assertNull(test.getValueBinding("converter"));
	test.setConverter(null);
	assertNull(test.getConverter());
	assertNull(test.getValueBinding("converter"));

    }


    // --------------------------------------------------------- Support Methods


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UIOutput();
        component.setRendererType(null);
        return (component);
    }


}
