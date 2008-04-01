/*
 * $Id: UIFormTestCase.java,v 1.1 2002/06/04 02:31:07 craigmcc Exp $
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
 * <p>Test case for the <strong>javax.faces.UIForm</strong>
 * concrete class.</p>
 */

public class UIFormTestCase extends UIComponentTestCase {


    // ----------------------------------------------------- Instance Variables


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIFormTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        component = new UIForm();
        component.setComponentId("test");
        attributes = new String[]
            { "componentId", "rendersChildren" };

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UIFormTestCase.class));

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

        assertEquals("componentType", UIForm.TYPE,
                     component.getComponentType());

    }


    /**
     * [3.1.7] Attribute/Property Transparency
     */
    public void testAttributePropertyTransparency() {

        super.testAttributePropertyTransparency();
        UIForm form = (UIForm) component;

        // formName
        assertNull("formName1", form.getFormName());
        assertNull("formName2", form.getAttribute("value"));
        form.setFormName("foo");
        assertEquals("formName3", "foo", form.getFormName());
        assertEquals("formName4", "foo",
                     (String) form.getAttribute("value"));
        form.setAttribute("value", "bar");
        assertEquals("formName5", "bar", form.getFormName());
        assertEquals("formName6", "bar",
                     (String) form.getAttribute("value"));
        form.setAttribute("value", null);
        assertNull("formName7", form.getFormName());
        assertNull("formName8", form.getAttribute("value"));

    }


}
