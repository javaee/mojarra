/*
 * $Id: UIGraphicTestCase.java,v 1.14 2003/11/11 01:09:48 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIGraphic}.</p>
 */

public class UIGraphicTestCase extends ValueHolderTestCaseBase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIGraphicTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIGraphic();
        expectedRendererType = "Image";
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIGraphicTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UIGraphic graphic = (UIGraphic) component;

        assertEquals(graphic.getURL(),
                     (String) graphic.getAttributes().get("URL"));
        graphic.setURL("foo");
        assertEquals("foo", (String) graphic.getAttributes().get("URL"));
        graphic.setURL(null);
        assertNull((String) graphic.getAttributes().get("URL"));
        graphic.getAttributes().put("URL", "bar");
        assertEquals("bar", graphic.getURL());
        graphic.getAttributes().put("URL", null);
        assertNull(graphic.getURL());

    }


    // Test a pristine UIGraphic instance
    public void testPristine() {

        super.testPristine();
        UIGraphic graphic = (UIGraphic) component;

        assertNull("no url", graphic.getURL());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIGraphic graphic = (UIGraphic) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIGraphic graphic = (UIGraphic) component;

        // Test transparency between "value" and "Url" properties
        graphic.setURL("foo");
        assertEquals("foo", (String) graphic.getValue());
        graphic.setURL(null);
        assertNull(graphic.getValue());
        graphic.setValue("bar");
        assertEquals("bar", graphic.getURL());
        graphic.setValue(null);
        assertNull(graphic.getURL());

    }


    public void testValueBindings() {

	super.testValueBindings();
	UIGraphic test = (UIGraphic) component;

	// "value" property
	request.setAttribute("foo", "bar");
	test.setValue(null);
	assertNull(test.getValue());
	test.setValueBinding("value", application.getValueBinding("#{foo}"));
	assertNotNull(test.getValueBinding("value"));
	assertEquals("bar", test.getValue());
	test.setValue("baz");
	assertEquals("baz", test.getValue());
	test.setValue(null);
	assertEquals("bar", test.getValue());
	test.setValueBinding("value", null);
	assertNull(test.getValueBinding("value"));
	assertNull(test.getValue());

    }


    // --------------------------------------------------------- Support Methods


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UIGraphic();
        component.setRendererType(null);
        return (component);
    }


}
