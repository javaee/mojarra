/*
 * $Id: UICommandTestCase.java,v 1.3 2003/01/16 20:24:24 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.Iterator;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
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

// This class is necessary since UICommand isn't a naming container by
// default.
private class UICommandNamingContainer extends UICommand implements NamingContainer {
    private UINamingContainer namingContainer = null;

    UICommandNamingContainer() {
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
        component = new UICommandNamingContainer();
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


    // ----- ActionEvents Tests -----

    public void testActionEvent1() {

        // Add a single listener interested in a single phase
        UICommand command = (UICommand) component;
        ActionEvent event = new ActionEvent(command, "");
        TestActionListener listener =
            new TestActionListener(PhaseId.APPLY_REQUEST_VALUES);
        command.addActionListener(listener);

        // Ensure that it is called only once
        assertEquals("Listener has not been called yet",
                     0, listener.getCount());
        command.broadcast(event, PhaseId.RECONSTITUTE_REQUEST);
        command.broadcast(event, PhaseId.APPLY_REQUEST_VALUES);
        command.broadcast(event, PhaseId.PROCESS_VALIDATIONS);
        command.broadcast(event, PhaseId.UPDATE_MODEL_VALUES);
        assertEquals("Listener was called exactly once",
                     1, listener.getCount());

    }


    public void testActionEvent2() {

        // Add a single listener interested in all phases
        UICommand command = (UICommand) component;
        ActionEvent event = new ActionEvent(command, "");
        TestActionListener listener =
            new TestActionListener(PhaseId.ANY_PHASE);
        command.addActionListener(listener);

        // Ensure that it is called only once
        assertEquals("Listener has not been called yet",
                     0, listener.getCount());
        command.broadcast(event, PhaseId.RECONSTITUTE_REQUEST);
        command.broadcast(event, PhaseId.APPLY_REQUEST_VALUES);
        command.broadcast(event, PhaseId.PROCESS_VALIDATIONS);
        command.broadcast(event, PhaseId.UPDATE_MODEL_VALUES);
        assertEquals("Listener was called exactly four times",
                     4, listener.getCount());

    }


    public void testActionEvent3() {

        UICommand command = (UICommand) component;
        ActionEvent event = new ActionEvent(command, "");

        // No registered listeners at all
        if (command.broadcast(event, PhaseId.RECONSTITUTE_REQUEST)) {
            fail("(1) should have returned false");
        }

        // Retroactively added listener -- too late
        command.addActionListener
                    (new TestActionListener(PhaseId.RECONSTITUTE_REQUEST));
        if (command.broadcast(event, PhaseId.APPLY_REQUEST_VALUES)) {
            fail("(2) should have returned false");
        }

        // Future-phase listener should trigger true return
        command.addActionListener
                    (new TestActionListener(PhaseId.UPDATE_MODEL_VALUES));
        if (!command.broadcast(event, PhaseId.PROCESS_VALIDATIONS)) {
            fail("(3) should have returned true");
        }

        // Last-phase listener should trigger false return
        if (command.broadcast(event, PhaseId.UPDATE_MODEL_VALUES)) {
            fail("(4) should have returned false");
        }

    }


}
