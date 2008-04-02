/*
 * $Id: UIParameterTestCase.java,v 1.2 2002/12/17 23:30:59 eburns Exp $
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
 * <p>Test case for the <strong>javax.faces.UIParameter</strong>
 * concrete class.</p>
 */

public class UIParameterTestCase extends UIOutputTestCase {


    // ----------------------------------------------------- Instance Variables


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIParameterTestCase(String name) {
        super(name);
    }


    // --------------------------------------------------- Overall Test Methods
// This class is necessary since UIParameter isn't a naming container by
// default.
private class UIParameterNamingContainer extends UIParameter implements NamingContainer {
    private UINamingContainer namingContainer = null;

    UIParameterNamingContainer() {
	namingContainer = new UINamingContainer();
    }

    public void addComponentToNamespace(UIComponent namedComponent) {
	namingContainer.addComponentToNamespace(namedComponent);
    }
    public void removeComponentFromNamespace(UIComponent namedComponent) {
	namingContainer.removeComponentFromNamespace(namedComponent);
    } 
    public UIComponent findComponentInNamespace(String name) {
	return namingContainer.findComponentInNamespace(name);
    }

    public String generateClientId() {
	return namingContainer.generateClientId();
    }
}



    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        component = new UIParameterNamingContainer();
        component.setComponentId("test");
        attributes = new String[]
            { "componentId", "rendersChildren" };

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UIParameterTestCase.class));

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

        assertEquals("componentType", UIParameter.TYPE,
                     component.getComponentType());

    }


    /**
     * [3.1.7] Attribute/Property Transparency
     */
    public void testAttributePropertyTransparency() {

        super.testAttributePropertyTransparency();
        UIParameter parameter = (UIParameter) component;

        // name
        assertNull("name1", parameter.getName());
        assertNull("name2", parameter.getAttribute("name"));
        parameter.setName("foo");
        assertEquals("name3", "foo", parameter.getName());
        assertEquals("name4", "foo",
                     (String) parameter.getAttribute("name"));
        parameter.setAttribute("name", "bar");
        assertEquals("name5", "bar", parameter.getName());
        assertEquals("name6", "bar",
                     (String) parameter.getAttribute("name"));
        parameter.setAttribute("name", null);
        assertNull("name7", parameter.getName());
        assertNull("name8", parameter.getAttribute("name"));

    }


    /**
     * [3.1.10] Renders Children.
     */
    public void testRendersChildren() {

        assertTrue("rendersChildren", !component.getRendersChildren());

    }


}
