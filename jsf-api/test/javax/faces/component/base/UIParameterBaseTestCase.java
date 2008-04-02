/*
 * $Id: UIParameterBaseTestCase.java,v 1.2 2003/07/26 17:55:24 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIParameterBase}.</p>
 */

public class UIParameterBaseTestCase extends UIOutputBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIParameterBaseTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIParameterBase();
        expectedId = null;
        expectedRendererType = null;
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIParameterBaseTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UIParameter parameter = (UIParameter) component;

        assertEquals(parameter.getName(),
                     (String) parameter.getAttribute("name"));
        parameter.setName("foo");
        assertEquals("foo", (String) parameter.getAttribute("name"));
        parameter.setName(null);
        assertNull((String) parameter.getAttribute("name"));
        parameter.setAttribute("name", "bar");
        assertEquals("bar", parameter.getName());
        parameter.setAttribute("name", null);
        assertNull(parameter.getName());

    }


    // Test a pristine UIParameterBase instance
    public void testPristine() {

        super.testPristine();
        UIParameter parameter = (UIParameter) component;

        assertNull("no name", parameter.getName());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIParameter parameter = (UIParameter) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIParameter parameter = (UIParameter) component;

        parameter.setName("foo");
        assertEquals("foo", parameter.getName());
        parameter.setName(null);
        assertNull(parameter.getName());

    }



}
