/*
 * $Id: UICommandBaseTestCase.java,v 1.2 2003/07/26 17:55:21 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UICommandBase}.</p>
 */

public class UICommandBaseTestCase extends UIOutputBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UICommandBaseTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UICommandBase();
        expectedRendererType = "Button";
    }

    
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UICommandBaseTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UICommand command = (UICommand) component;

        assertEquals(command.getAction(),
                     (String) command.getAttribute("action"));
        command.setAction("foo");
        assertEquals("foo", (String) command.getAttribute("action"));
        command.setAction(null);
        assertNull((String) command.getAttribute("action"));
        command.setAttribute("action", "bar");
        assertEquals("bar", command.getAction());
        command.setAttribute("action", null);
        assertNull(command.getAction());

        assertEquals(command.getActionRef(),
                     (String) command.getAttribute("actionRef"));
        command.setActionRef("foo");
        assertEquals("foo", (String) command.getAttribute("actionRef"));
        command.setActionRef(null);
        assertNull((String) command.getAttribute("actionRef"));
        command.setAttribute("actionRef", "bar");
        assertEquals("bar", command.getActionRef());
        command.setAttribute("actionRef", null);
        assertNull(command.getActionRef());

    }


    // Test event queuing and broadcasting (any phase listeners)
    public void testEventsGeneric() {

        UICommand command = (UICommand) component;
        ActionEvent event = new ActionEvent(command);

        // Register three listeners
        command.addActionListener
            (new TestActionListener("AP0", PhaseId.ANY_PHASE));
        command.addActionListener
            (new TestActionListener("AP1", PhaseId.ANY_PHASE));
        command.addActionListener
            (new TestActionListener("AP2", PhaseId.ANY_PHASE));

        // Fire events and evaluate results
        TestActionListener.trace(null);
        assertTrue(!command.broadcast(event, PhaseId.APPLY_REQUEST_VALUES));
        assertTrue(!command.broadcast(event, PhaseId.PROCESS_VALIDATIONS));
        assertTrue(!command.broadcast(event, PhaseId.INVOKE_APPLICATION));
        assertEquals("/AP0/AP1/AP2/AP0/AP1/AP2/AP0/AP1/AP2",
                     TestActionListener.trace());

    }


    // Test event queuing and broadcasting (mixed phase listeners)
    public void testEventsMixed() {

        UICommand command = (UICommand) component;
        ActionEvent event = new ActionEvent(command);

        // Register three listeners
        command.addActionListener
            (new TestActionListener("ARV", PhaseId.APPLY_REQUEST_VALUES));
        command.addActionListener
            (new TestActionListener("PV", PhaseId.PROCESS_VALIDATIONS));
        command.addActionListener
            (new TestActionListener("AP", PhaseId.ANY_PHASE));

        // Fire events and evaluate results
        TestActionListener.trace(null);
        assertTrue(command.broadcast(event, PhaseId.APPLY_REQUEST_VALUES));
        assertTrue(!command.broadcast(event, PhaseId.PROCESS_VALIDATIONS));
        assertTrue(!command.broadcast(event, PhaseId.INVOKE_APPLICATION));
        assertEquals("/AP/ARV/AP/PV/AP",
                     TestActionListener.trace());

    }


    // Test event queuing and broadcasting (specific phase listeners)
    public void testEventsSpecific() {

        UICommand command = (UICommand) component;
        ActionEvent event = new ActionEvent(command);

        // Register five listeners
        command.addActionListener
            (new TestActionListener("ARV0", PhaseId.APPLY_REQUEST_VALUES));
        command.addActionListener
            (new TestActionListener("ARV1", PhaseId.APPLY_REQUEST_VALUES));
        command.addActionListener
            (new TestActionListener("PV0", PhaseId.PROCESS_VALIDATIONS));
        command.addActionListener
            (new TestActionListener("PV1", PhaseId.PROCESS_VALIDATIONS));
        command.addActionListener
            (new TestActionListener("PV2", PhaseId.PROCESS_VALIDATIONS));

        // Fire events and evaluate results
        TestActionListener.trace(null);
        assertTrue(command.broadcast(event, PhaseId.RECONSTITUTE_REQUEST));
        assertTrue(command.broadcast(event, PhaseId.APPLY_REQUEST_VALUES));
        assertTrue(!command.broadcast(event, PhaseId.PROCESS_VALIDATIONS));
        assertTrue(!command.broadcast(event, PhaseId.UPDATE_MODEL_VALUES));
        assertTrue(!command.broadcast(event, PhaseId.INVOKE_APPLICATION));
        assertEquals("/ARV0/ARV1/PV0/PV1/PV2",
                     TestActionListener.trace());

    }


    // Test listener registration and deregistration
    public void testListeners() {

        TestCommandBase command = new TestCommandBase();
        TestActionListener listener = null;
        List lists[] = null;

        command.addActionListener
            (new TestActionListener("ARV0", PhaseId.APPLY_REQUEST_VALUES));
        command.addActionListener
            (new TestActionListener("ARV1", PhaseId.APPLY_REQUEST_VALUES));
        command.addActionListener
            (new TestActionListener("PV0", PhaseId.PROCESS_VALIDATIONS));
        command.addActionListener
            (new TestActionListener("PV1", PhaseId.PROCESS_VALIDATIONS));
        command.addActionListener
            (new TestActionListener("PV2", PhaseId.PROCESS_VALIDATIONS));

        lists = command.getListeners();
        assertEquals(PhaseId.VALUES.size(), lists.length);
        for (int i = 0; i < lists.length; i++) {
            if (i == PhaseId.APPLY_REQUEST_VALUES.getOrdinal()) {
                assertEquals(2, lists[i].size());
                listener = (TestActionListener) lists[i].get(0);
                assertEquals("ARV0", listener.getId());
                listener = (TestActionListener) lists[i].get(1);
                assertEquals("ARV1", listener.getId());
            } else if (i == PhaseId.PROCESS_VALIDATIONS.getOrdinal()) {
                assertEquals(3, lists[i].size());
                listener = (TestActionListener) lists[i].get(0);
                assertEquals("PV0", listener.getId());
                listener = (TestActionListener) lists[i].get(1);
                assertEquals("PV1", listener.getId());
                listener = (TestActionListener) lists[i].get(2);
                assertEquals("PV2", listener.getId());
            } else {
                assertNull(lists[i]);
            }
        }

        command.removeActionListener
            ((ActionListener) lists[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()].get(0));
        command.removeActionListener
            ((ActionListener) lists[PhaseId.PROCESS_VALIDATIONS.getOrdinal()].get(1));

        lists = command.getListeners();
        assertEquals(PhaseId.VALUES.size(), lists.length);
        for (int i = 0; i < lists.length; i++) {
            if (i == PhaseId.APPLY_REQUEST_VALUES.getOrdinal()) {
                assertEquals(1, lists[i].size());
                listener = (TestActionListener) lists[i].get(0);
                assertEquals("ARV1", listener.getId());
            } else if (i == PhaseId.PROCESS_VALIDATIONS.getOrdinal()) {
                assertEquals(2, lists[i].size());
                listener = (TestActionListener) lists[i].get(0);
                assertEquals("PV0", listener.getId());
                listener = (TestActionListener) lists[i].get(1);
                assertEquals("PV2", listener.getId());
            } else {
                assertNull(lists[i]);
            }
        }

    }


    // Test a pristine UICommandBase instance
    public void testPristine() {

        super.testPristine();
        UICommand command = (UICommand) component;

        assertNull("no action", command.getAction());
        assertNull("no actionRef", command.getActionRef());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UICommand command = (UICommand) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UICommand command = (UICommand) component;

        command.setAction("foo");
        assertEquals("foo", command.getAction());
        command.setAction(null);
        assertNull(command.getAction());

        command.setActionRef("foo");
        assertEquals("foo", command.getActionRef());
        command.setActionRef(null);
        assertNull(command.getActionRef());

    }



}
