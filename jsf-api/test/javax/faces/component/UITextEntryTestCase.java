/*
 * $Id: UITextEntryTestCase.java,v 1.1 2002/06/04 02:31:08 craigmcc Exp $
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
 * <p>Test case for the <strong>javax.faces.UITextEntry</strong>
 * concrete class.</p>
 */

public class UITextEntryTestCase extends UIComponentTestCase {


    // ----------------------------------------------------- Instance Variables


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UITextEntryTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        component = new UITextEntry();
        component.setComponentId("test");
        attributes = new String[]
            { "componentId", "rendersChildren" };

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UITextEntryTestCase.class));

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

        assertEquals("componentType", UITextEntry.TYPE,
                     component.getComponentType());

    }


    /**
     * [3.1.7] Attribute/Property Transparency
     */
    public void testAttributePropertyTransparency() {

        super.testAttributePropertyTransparency();
        UITextEntry textEntry = (UITextEntry) component;

        // outputName
        assertNull("text1", textEntry.getText());
        assertNull("text2", textEntry.getAttribute("value"));
        textEntry.setText("foo");
        assertEquals("text3", "foo", textEntry.getText());
        assertEquals("text4", "foo",
                     (String) textEntry.getAttribute("value"));
        textEntry.setAttribute("value", "bar");
        assertEquals("text5", "bar", textEntry.getText());
        assertEquals("text6", "bar",
                     (String) textEntry.getAttribute("value"));
        textEntry.setAttribute("value", null);
        assertNull("text7", textEntry.getText());
        assertNull("text8", textEntry.getAttribute("value"));

    }


}
