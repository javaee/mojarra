/*
 * $Id: UIOutputTestCase.java,v 1.1 2002/06/04 02:31:07 craigmcc Exp $
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
 * <p>Test case for the <strong>javax.faces.UIOutput</strong>
 * concrete class.</p>
 */

public class UIOutputTestCase extends UIComponentTestCase {


    // ----------------------------------------------------- Instance Variables


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIOutputTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        component = new UIOutput();
        component.setComponentId("test");
        attributes = new String[]
            { "componentId", "rendersChildren" };

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UIOutputTestCase.class));

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

        assertEquals("componentType", UIOutput.TYPE,
                     component.getComponentType());

    }


    /**
     * [3.1.7] Attribute/Property Transparency
     */
    public void testAttributePropertyTransparency() {

        super.testAttributePropertyTransparency();
        UIOutput output = (UIOutput) component;

        // outputName
        assertNull("text1", output.getText());
        assertNull("text2", output.getAttribute("value"));
        output.setText("foo");
        assertEquals("text3", "foo", output.getText());
        assertEquals("text4", "foo",
                     (String) output.getAttribute("value"));
        output.setAttribute("value", "bar");
        assertEquals("text5", "bar", output.getText());
        assertEquals("text6", "bar",
                     (String) output.getAttribute("value"));
        output.setAttribute("value", null);
        assertNull("text7", output.getText());
        assertNull("text8", output.getAttribute("value"));

    }


}
