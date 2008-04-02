/*
 * $Id: UIInputTestCase.java,v 1.4 2003/01/16 20:48:01 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.util.Iterator;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangedEvent;
import javax.faces.event.ValueChangedListener;
import javax.faces.validator.Validator;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Test case for the <strong>javax.faces.UIInput</strong>
 * concrete class.</p>
 */

public class UIInputTestCase extends UIComponentTestCase {


    // ----------------------------------------------------- Instance Variables


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIInputTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods
// This class is necessary since UIInput isn't a naming container by
// default.
private class UIInputNamingContainer extends UIInput implements NamingContainer {
    private UINamingContainer namingContainer = null;

    UIInputNamingContainer() {
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

        component = new UIInputNamingContainer();
        component.setComponentId("test");
        attributes = new String[]
            { "componentId", "rendersChildren" };

    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UIInputTestCase.class));

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

        assertEquals("componentType", UIInput.TYPE,
                     component.getComponentType());

    }


    /**
     * [3.1.7] Attribute/Property Transparency
     */
    public void testAttributePropertyTransparency() {

        super.testAttributePropertyTransparency();
        UIInput input = (UIInput) component;

    }


    // ----- ValueChangedEvents Tests -----

    public void testValueChangedEvent1() {

        // Add a single listener interested in a single phase
        UIInput input = (UIInput) component;
        ValueChangedEvent event = new ValueChangedEvent(input, "", "");
        TestValueChangedListener listener =
            new TestValueChangedListener(PhaseId.APPLY_REQUEST_VALUES);
        input.addValueChangedListener(listener);

        // Ensure that it is called only once
        assertEquals("Listener has not been called yet",
                     0, listener.getCount());
        input.broadcast(event, PhaseId.RECONSTITUTE_REQUEST);
        input.broadcast(event, PhaseId.APPLY_REQUEST_VALUES);
        input.broadcast(event, PhaseId.PROCESS_VALIDATIONS);
        input.broadcast(event, PhaseId.UPDATE_MODEL_VALUES);
        assertEquals("Listener was called exactly once",
                     1, listener.getCount());

    }


    public void testValueChangedEvent2() {

        // Add a single listener interested in all phases
        UIInput input = (UIInput) component;
        ValueChangedEvent event = new ValueChangedEvent(input, "", "");
        TestValueChangedListener listener =
            new TestValueChangedListener(PhaseId.ANY_PHASE);
        input.addValueChangedListener(listener);

        // Ensure that it is called only once
        assertEquals("Listener has not been called yet",
                     0, listener.getCount());
        input.broadcast(event, PhaseId.RECONSTITUTE_REQUEST);
        input.broadcast(event, PhaseId.APPLY_REQUEST_VALUES);
        input.broadcast(event, PhaseId.PROCESS_VALIDATIONS);
        input.broadcast(event, PhaseId.UPDATE_MODEL_VALUES);
        assertEquals("Listener was called exactly four times",
                     4, listener.getCount());

    }


    public void testValueChangedEvent3() {

        UIInput input = (UIInput) component;
        ValueChangedEvent event = new ValueChangedEvent(input, "", "");

        // No registered listeners at all
        if (input.broadcast(event, PhaseId.RECONSTITUTE_REQUEST)) {
            fail("(1) should have returned false");
        }

        // Retroactively added listener -- too late
        input.addValueChangedListener
                    (new TestValueChangedListener(PhaseId.RECONSTITUTE_REQUEST));
        if (input.broadcast(event, PhaseId.APPLY_REQUEST_VALUES)) {
            fail("(2) should have returned false");
        }

        // Future-phase listener should trigger true return
        input.addValueChangedListener
                    (new TestValueChangedListener(PhaseId.UPDATE_MODEL_VALUES));
        if (!input.broadcast(event, PhaseId.PROCESS_VALIDATIONS)) {
            fail("(3) should have returned true");
        }

        // Last-phase listener should trigger false return
        if (input.broadcast(event, PhaseId.UPDATE_MODEL_VALUES)) {
            fail("(4) should have returned false");
        }

    }


}
