/*
 * $Id: UIGraphicTestCase.java,v 1.1 2002/06/04 02:31:07 craigmcc Exp $
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
 * <p>Test case for the <strong>javax.faces.UIGraphic</strong>
 * concrete class.</p>
 */

public class UIGraphicTestCase extends UIComponentTestCase {


    // ----------------------------------------------------- Instance Variables


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIGraphicTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        component = new UIGraphic();
        component.setComponentId("test");
        attributes = new String[]
            { "componentId", "rendersChildren" };

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UIGraphicTestCase.class));

    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        component = null;

    }


    // ------------------------------------------------ Individual Test Methods


    /**
     * [3.1.1] Component Type.
     */
    public void testComponentType() {

        assertEquals("componentType", UIGraphic.TYPE,
                     component.getComponentType());

    }


    /**
     * [3.1.7] Attribute/Property Transparency
     */
    public void testAttributePropertyTransparency() {

        super.testAttributePropertyTransparency();
        UIGraphic graphic = (UIGraphic) component;

        // graphicName
        assertNull("imagePath1", graphic.getImagePath());
        assertNull("imagePath2", graphic.getAttribute("value"));
        graphic.setImagePath("foo");
        assertEquals("imagePath3", "foo", graphic.getImagePath());
        assertEquals("imagePath4", "foo",
                     (String) graphic.getAttribute("value"));
        graphic.setAttribute("value", "bar");
        assertEquals("imagePath5", "bar", graphic.getImagePath());
        assertEquals("imagePath6", "bar",
                     (String) graphic.getAttribute("value"));
        graphic.setAttribute("value", null);
        assertNull("imagePath7", graphic.getImagePath());
        assertNull("imagePath8", graphic.getAttribute("value"));

    }


}
