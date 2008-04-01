/*
 * $Id: UISelectBooleanTestCase.java,v 1.2 2002/08/04 23:27:27 craigmcc Exp $
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
 * <p>Test case for the <strong>javax.faces.UISelectBoolean</strong>
 * concrete class.</p>
 */

public class UISelectBooleanTestCase extends UIInputTestCase {


    // ----------------------------------------------------- Instance Variables


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UISelectBooleanTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        component = new UISelectBoolean();
        component.setComponentId("test");
        attributes = new String[]
            { "componentId", "rendersChildren", "value" };

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UISelectBooleanTestCase.class));

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

        assertEquals("componentType", UISelectBoolean.TYPE,
                     component.getComponentType());

    }


    /**
     * [3.1.7] Attribute/Property Transparency
     */
    public void testAttributePropertyTransparency() {

        UISelectBoolean selectBoolean = (UISelectBoolean) component;

        // Test "selected" property
        assertTrue("selected1", !selectBoolean.isSelected());
        assertTrue("selected2",
                   !((Boolean) selectBoolean.getAttribute("value")).booleanValue());
        selectBoolean.setSelected(true);
        assertTrue("selected3", selectBoolean.isSelected());
        assertTrue("selected4",
                   ((Boolean) selectBoolean.getAttribute("value")).booleanValue());
        selectBoolean.setAttribute("value", Boolean.FALSE);
        assertTrue("selected5", !selectBoolean.isSelected());
        assertTrue("selected6",
                   !((Boolean) selectBoolean.getAttribute("value")).booleanValue());

        // Perform standard tests
        component.setValue(null);
        super.testAttributePropertyTransparency();

    }


}
