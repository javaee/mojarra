/*
 * $Id: UICommandTestCase.java,v 1.1 2002/06/04 02:31:07 craigmcc Exp $
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
 * <p>Test case for the <strong>javax.faces.UICommand</strong>
 * concrete class.</p>
 */

public class UICommandTestCase extends UIComponentTestCase {


    // ----------------------------------------------------- Instance Variables


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UICommandTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() {

        component = new UICommand();
        component.setComponentId("test");
        attributes = new String[]
            { "componentId", "rendersChildren" };

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UICommandTestCase.class));

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

        assertEquals("componentType", UICommand.TYPE,
                     component.getComponentType());

    }


    /**
     * [3.1.7] Attribute/Property Transparency
     */
    public void testAttributePropertyTransparency() {

        super.testAttributePropertyTransparency();
        UICommand command = (UICommand) component;

        // commandName
        assertNull("commandName1", command.getCommandName());
        assertNull("commandName2", command.getAttribute("value"));
        command.setCommandName("foo");
        assertEquals("commandName3", "foo", command.getCommandName());
        assertEquals("commandName4", "foo",
                     (String) command.getAttribute("value"));
        command.setAttribute("value", "bar");
        assertEquals("commandName5", "bar", command.getCommandName());
        assertEquals("commandName6", "bar",
                     (String) command.getAttribute("value"));
        command.setAttribute("value", null);
        assertNull("commandName7", command.getCommandName());
        assertNull("commandName8", command.getAttribute("value"));

    }


}
