/*
 * $Id: UIGraphicBaseTestCase.java,v 1.2 2003/07/26 17:55:22 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIGraphicBase}.</p>
 */

public class UIGraphicBaseTestCase extends UIOutputBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIGraphicBaseTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIGraphicBase();
        expectedRendererType = "Image";
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIGraphicBaseTestCase.class));
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
                     (String) graphic.getAttribute("URL"));
        graphic.setURL("foo");
        assertEquals("foo", (String) graphic.getAttribute("URL"));
        graphic.setURL(null);
        assertNull((String) graphic.getAttribute("URL"));
        graphic.setAttribute("URL", "bar");
        assertEquals("bar", graphic.getURL());
        graphic.setAttribute("URL", null);
        assertNull(graphic.getURL());

    }


    // Test a pristine UIGraphicBase instance
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

        // Test transparency between "value" and "URL" properties
        graphic.setURL("foo");
        assertEquals("foo", (String) graphic.getValue());
        graphic.setURL(null);
        assertNull(graphic.getValue());
        graphic.setValue("bar");
        assertEquals("bar", graphic.getURL());
        graphic.setValue(null);
        assertNull(graphic.getURL());

    }



}
