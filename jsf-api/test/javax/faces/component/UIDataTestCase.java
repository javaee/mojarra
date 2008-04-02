/*
 * $Id: UIDataTestCase.java,v 1.12 2003/10/09 22:58:12 craigmcc Exp $
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
import javax.faces.convert.ShortConverter;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIData}.</p>
 */

public class UIDataTestCase extends ValueHolderTestCaseBase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIDataTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIData();
        expectedId = null;
        expectedRendererType = "Table";
        expectedRendersChildren = true;
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIDataTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UIData data = (UIData) component;

        data.setFirst(6);
        assertEquals(data.getFirst(),
                     ((Integer) data.getAttributes().get("first")).intValue());
        data.getAttributes().put("first", new Integer(7));
        assertEquals(data.getFirst(),
                     ((Integer) data.getAttributes().get("first")).intValue());

        data.setRows(10);
        assertEquals(data.getRows(),
                     ((Integer) data.getAttributes().get("rows")).intValue());
        data.getAttributes().put("rows", new Integer(20));
        assertEquals(data.getRows(),
                     ((Integer) data.getAttributes().get("rows")).intValue());

        assertEquals(data.getVar(),
                     (String) data.getAttributes().get("var"));
        data.setVar("foo");
        assertEquals("foo", (String) data.getAttributes().get("var"));
        data.setVar(null);
        assertNull((String) data.getAttributes().get("var"));
        data.getAttributes().put("var", "bar");
        assertEquals("bar", data.getVar());
        data.getAttributes().put("var", null);
        assertNull(data.getVar());

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine UIData instance
    public void testPristine() {

        super.testPristine();
        UIData data = (UIData) component;

        assertEquals("no first", 0, data.getFirst());
        assertEquals("no rows", 0, data.getRows());
        assertNull("no var", data.getVar());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIData data = (UIData) component;

        try {
            data.setFirst(-1);
            fail("Should have thrown IAE");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        try {
            data.setRows(-1);
            fail("Should have thrown IAE");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIData data = (UIData) component;

        data.setFirst(0);
        data.setFirst(11);
        data.setRows(0);
        data.setRows(20);
        data.setVar(null);
        data.setVar("foo");

    }


    // --------------------------------------------------------- Support Methods


    // Check that the properties on the specified components are equal
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {

        super.checkProperties(comp1, comp2);
        UIData d1 = (UIData) comp1;
        UIData d2 = (UIData) comp2;
        assertEquals(d1.getFirst(), d2.getFirst());
        assertEquals(d1.getRows(), d2.getRows());
        assertEquals(d1.getVar(), d2.getVar());

    }


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UIData();
        component.setRendererType(null);
        return (component);
    }


    // Populate a pristine component to be used in state holder tests
    protected void populateComponent(UIComponent component) {
        super.populateComponent(component);
        UIData d = (UIData) component;
        d.setFirst(5);
        d.setRows(10);
        d.setVar("foo");
    }


}
