/*
 * $Id: UIInputBaseTestCase.java,v 1.3 2003/07/27 00:48:31 craigmcc Exp $
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
import javax.faces.component.UIInput;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangedEvent;
import javax.faces.event.ValueChangedListener;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIInputBase}.</p>
 */

public class UIInputBaseTestCase extends UIOutputBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIInputBaseTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIInputBase();
        expectedRendererType = "Text";
    }

    
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIInputBaseTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UIInput input = (UIInput) component;

        assertEquals(input.getPrevious(),
                     (String) input.getAttribute("previous"));
        input.setPrevious("foo");
        assertEquals("foo", (String) input.getAttribute("previous"));
        input.setPrevious(null);
        assertNull((String) input.getAttribute("previous"));
        input.setAttribute("previous", "bar");
        assertEquals("bar", input.getPrevious());
        input.setAttribute("previous", null);
        assertNull(input.getPrevious());

        input.setRequired(true);
        assertEquals(Boolean.TRUE,
                     (Boolean) input.getAttribute("required"));
        input.setRequired(false);
        assertEquals(Boolean.FALSE,
                     (Boolean) input.getAttribute("required"));
        input.setAttribute("required", Boolean.TRUE);
        assertTrue(input.isRequired());
        input.setAttribute("required", Boolean.FALSE);
        assertTrue(!input.isRequired());

        input.setValid(false);
        assertEquals(Boolean.FALSE,
                     (Boolean) input.getAttribute("valid"));
        input.setValid(true);
        assertEquals(Boolean.TRUE,
                     (Boolean) input.getAttribute("valid"));
        input.setAttribute("valid", Boolean.FALSE);
        assertTrue(!input.isValid());
        input.setAttribute("valid", Boolean.TRUE);
        assertTrue(input.isValid());

    }


    // Test the compareValues() method
    public void testCompareValues() {

        TestInputBase input = new TestInputBase();
        Object value1a = "foo";
        Object value1b = "foo";
        Object value2 = "bar";
        Object value3 = null;

        assertTrue(!input.compareValues(value1a, value1a));
        assertTrue(!input.compareValues(value1a, value1b));
        assertTrue(!input.compareValues(value1b, value1b));
        assertTrue(!input.compareValues(value2, value2));
        assertTrue(!input.compareValues(value3, value3));

        assertTrue(input.compareValues(value1a, value2));
        assertTrue(input.compareValues(value1a, value3));
        assertTrue(input.compareValues(value2, value3));
        assertTrue(input.compareValues(value3, value2));

    }


    // Test event queuing and broadcasting (any phase listeners)
    public void testEventsGeneric() {

        UIInput input = (UIInput) component;
        ValueChangedEvent event = new ValueChangedEvent(input, null, null);

        // Register three listeners
        input.addValueChangedListener
            (new TestValueChangedListener("AP0", PhaseId.ANY_PHASE));
        input.addValueChangedListener
            (new TestValueChangedListener("AP1", PhaseId.ANY_PHASE));
        input.addValueChangedListener
            (new TestValueChangedListener("AP2", PhaseId.ANY_PHASE));

        // Fire events and evaluate results
        TestValueChangedListener.trace(null);
        assertTrue(!input.broadcast(event, PhaseId.APPLY_REQUEST_VALUES));
        assertTrue(!input.broadcast(event, PhaseId.PROCESS_VALIDATIONS));
        assertTrue(!input.broadcast(event, PhaseId.INVOKE_APPLICATION));
        assertEquals("/AP0/AP1/AP2/AP0/AP1/AP2/AP0/AP1/AP2",
                     TestValueChangedListener.trace());

    }


    // Test event queuing and broadcasting (mixed phase listeners)
    public void testEventsMixed() {

        UIInput input = (UIInput) component;
        ValueChangedEvent event = new ValueChangedEvent(input, null, null);

        // Register three listeners
        input.addValueChangedListener
            (new TestValueChangedListener("ARV", PhaseId.APPLY_REQUEST_VALUES));
        input.addValueChangedListener
            (new TestValueChangedListener("PV", PhaseId.PROCESS_VALIDATIONS));
        input.addValueChangedListener
            (new TestValueChangedListener("AP", PhaseId.ANY_PHASE));

        // Fire events and evaluate results
        TestValueChangedListener.trace(null);
        assertTrue(input.broadcast(event, PhaseId.APPLY_REQUEST_VALUES));
        assertTrue(!input.broadcast(event, PhaseId.PROCESS_VALIDATIONS));
        assertTrue(!input.broadcast(event, PhaseId.INVOKE_APPLICATION));
        assertEquals("/AP/ARV/AP/PV/AP",
                     TestValueChangedListener.trace());

    }


    // Test event queuing and broadcasting (specific phase listeners)
    public void testEventsSpecific() {

        UIInput input = (UIInput) component;
        ValueChangedEvent event = new ValueChangedEvent(input, null, null);

        // Register five listeners
        input.addValueChangedListener
            (new TestValueChangedListener("ARV0", PhaseId.APPLY_REQUEST_VALUES));
        input.addValueChangedListener
            (new TestValueChangedListener("ARV1", PhaseId.APPLY_REQUEST_VALUES));
        input.addValueChangedListener
            (new TestValueChangedListener("PV0", PhaseId.PROCESS_VALIDATIONS));
        input.addValueChangedListener
            (new TestValueChangedListener("PV1", PhaseId.PROCESS_VALIDATIONS));
        input.addValueChangedListener
            (new TestValueChangedListener("PV2", PhaseId.PROCESS_VALIDATIONS));

        // Fire events and evaluate results
        TestValueChangedListener.trace(null);
        assertTrue(input.broadcast(event, PhaseId.RECONSTITUTE_REQUEST));
        assertTrue(input.broadcast(event, PhaseId.APPLY_REQUEST_VALUES));
        assertTrue(!input.broadcast(event, PhaseId.PROCESS_VALIDATIONS));
        assertTrue(!input.broadcast(event, PhaseId.UPDATE_MODEL_VALUES));
        assertTrue(!input.broadcast(event, PhaseId.INVOKE_APPLICATION));
        assertEquals("/ARV0/ARV1/PV0/PV1/PV2",
                     TestValueChangedListener.trace());

    }


    // Test listener registration and deregistration
    public void testListeners() {

        TestInputBase input = new TestInputBase();
        TestValueChangedListener listener = null;
        List lists[] = null;

        input.addValueChangedListener
            (new TestValueChangedListener("ARV0", PhaseId.APPLY_REQUEST_VALUES));
        input.addValueChangedListener
            (new TestValueChangedListener("ARV1", PhaseId.APPLY_REQUEST_VALUES));
        input.addValueChangedListener
            (new TestValueChangedListener("PV0", PhaseId.PROCESS_VALIDATIONS));
        input.addValueChangedListener
            (new TestValueChangedListener("PV1", PhaseId.PROCESS_VALIDATIONS));
        input.addValueChangedListener
            (new TestValueChangedListener("PV2", PhaseId.PROCESS_VALIDATIONS));

        /* PENDING(craigmcc) - listeners are no longer accessible
        lists = input.getListeners();
        assertEquals(PhaseId.VALUES.size(), lists.length);
        for (int i = 0; i < lists.length; i++) {
            if (i == PhaseId.APPLY_REQUEST_VALUES.getOrdinal()) {
                assertEquals(2, lists[i].size());
                listener = (TestValueChangedListener) lists[i].get(0);
                assertEquals("ARV0", listener.getId());
                listener = (TestValueChangedListener) lists[i].get(1);
                assertEquals("ARV1", listener.getId());
            } else if (i == PhaseId.PROCESS_VALIDATIONS.getOrdinal()) {
                assertEquals(3, lists[i].size());
                listener = (TestValueChangedListener) lists[i].get(0);
                assertEquals("PV0", listener.getId());
                listener = (TestValueChangedListener) lists[i].get(1);
                assertEquals("PV1", listener.getId());
                listener = (TestValueChangedListener) lists[i].get(2);
                assertEquals("PV2", listener.getId());
            } else {
                assertNull(lists[i]);
            }
        }

        input.removeValueChangedListener
            ((ValueChangedListener) lists[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()].get(0));
        input.removeValueChangedListener
            ((ValueChangedListener) lists[PhaseId.PROCESS_VALIDATIONS.getOrdinal()].get(1));

        lists = input.getListeners();
        assertEquals(PhaseId.VALUES.size(), lists.length);
        for (int i = 0; i < lists.length; i++) {
            if (i == PhaseId.APPLY_REQUEST_VALUES.getOrdinal()) {
                assertEquals(1, lists[i].size());
                listener = (TestValueChangedListener) lists[i].get(0);
                assertEquals("ARV1", listener.getId());
            } else if (i == PhaseId.PROCESS_VALIDATIONS.getOrdinal()) {
                assertEquals(2, lists[i].size());
                listener = (TestValueChangedListener) lists[i].get(0);
                assertEquals("PV0", listener.getId());
                listener = (TestValueChangedListener) lists[i].get(1);
                assertEquals("PV2", listener.getId());
            } else {
                assertNull(lists[i]);
            }
        }
        */

    }


    // Test a pristine UIInputBase instance
    public void testPristine() {

        super.testPristine();
        UIInput input = (UIInput) component;

        assertNull("no previous", input.getPrevious());
        assertTrue("not required", !input.isRequired());
        assertTrue("is valid", input.isValid());


    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIInput input = (UIInput) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIInput input = (UIInput) component;

        input.setPrevious("foo");
        assertEquals("foo", input.getPrevious());
        input.setPrevious(null);
        assertNull(input.getPrevious());

        input.setRequired(true);
        assertTrue(input.isRequired());
        input.setRequired(false);
        assertTrue(!input.isRequired());

        input.setValid(false);
        assertTrue(!input.isValid());
        input.setValid(true);
        assertTrue(input.isValid());

    }



}
