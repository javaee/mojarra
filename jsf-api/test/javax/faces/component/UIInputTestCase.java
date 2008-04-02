/*
 * $Id: UIInputTestCase.java,v 1.17 2003/10/20 03:04:01 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangedEvent;
import javax.faces.event.ValueChangedListener;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;
import javax.faces.TestUtil;


/**
 * <p>Unit tests for {@link UIInput}.</p>
 */

public class UIInputTestCase extends UIOutputTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIInputTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIInput();
        expectedRendererType = "Text";
    }

    
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIInputTestCase.class));
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
                     (String) input.getAttributes().get("previous"));
        input.setPrevious("foo");
        assertEquals("foo", (String) input.getAttributes().get("previous"));
        input.setPrevious(null);
        assertNull((String) input.getAttributes().get("previous"));
        input.getAttributes().put("previous", "bar");
        assertEquals("bar", input.getPrevious());
        input.getAttributes().put("previous", null);
        assertNull(input.getPrevious());

        input.setRequired(true);
        assertEquals(Boolean.TRUE,
                     (Boolean) input.getAttributes().get("required"));
        input.setRequired(false);
        assertEquals(Boolean.FALSE,
                     (Boolean) input.getAttributes().get("required"));
        input.getAttributes().put("required", Boolean.TRUE);
        assertTrue(input.isRequired());
        input.getAttributes().put("required", Boolean.FALSE);
        assertTrue(!input.isRequired());

    }


    // Test the compareValues() method
    public void testCompareValues() {

        TestInput input = new TestInput();
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
        assertEquals("/AP0/AP1/AP2",
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
        assertEquals("/AP/ARV/PV",
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
        assertTrue(input.broadcast(event, PhaseId.RESTORE_VIEW));
        assertTrue(input.broadcast(event, PhaseId.APPLY_REQUEST_VALUES));
        assertTrue(!input.broadcast(event, PhaseId.PROCESS_VALIDATIONS));
        assertTrue(!input.broadcast(event, PhaseId.UPDATE_MODEL_VALUES));
        assertTrue(!input.broadcast(event, PhaseId.INVOKE_APPLICATION));
        assertEquals("/ARV0/ARV1/PV0/PV1/PV2",
                     TestValueChangedListener.trace());

    }


    // Test listener registration and deregistration
    public void testListeners() {

        TestInput input = new TestInput();
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


    // Test a pristine UIInput instance
    public void testPristine() {

        super.testPristine();
        UIInput input = (UIInput) component;

        assertNull("no previous", input.getPrevious());
        assertTrue("not required", !input.isRequired());

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

    }


    // --------------------------------------------------------- Support Methods


    // Check that the properties on the specified components are equal
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {
        super.checkProperties(comp1, comp2);
        UIInput i1 = (UIInput) comp1;
        UIInput i2 = (UIInput) comp2;
        assertEquals(i1.getPrevious(), i2.getPrevious());
        assertEquals(i1.isRequired(), i2.isRequired());
    }


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UIInput();
        component.setRendererType(null);
        return (component);
    }


    // Populate a pristine component to be used in state holder tests
    protected void populateComponent(UIComponent component) {
        super.populateComponent(component);
        UIInput i = (UIInput) component;
        i.setPrevious("previous");
        i.setRequired(true);
    }


    protected boolean listenersAreEqual(FacesContext context,
					UIInputSub comp1,
					UIInputSub comp2) {

	List [] list1 = comp1.getListeners();
	List [] list2 = comp2.getListeners();
	// make sure they're either both null or both non-null
	if ((null == list1 && null != list2) ||
	    (null != list1 && null == list2)) {
	    return false;
	}
	if (null == list1) {
	    return true;
	}
	int i = 0, j = 0, outerLen = list1.length, innerLen = 0;
	boolean result = true;
	if (outerLen != list2.length) {
	    return false;
	}
	for (i = 0; i < outerLen; i++) {
	    if ((null == list1[i] && null != list2[i]) ||
		(null != list1[i] && null == list2[i])) {
		return false;
	    }
	    else if (null != list1[i]) {
		if (list1[i].size() != (innerLen = list2[i].size())) {
		    return false;
		}
		for (j = 0; j < innerLen; j++) {
		    result = ((TestValueChangedListener)list1[i].get(j)).isEqual(list2[i].get(j));
		    if (!result) {
			return false;
		    }
		}
	    }
	}
	return true;
    }

    protected boolean validatorsAreEqual(FacesContext context,
					UIInputSub comp1,
					UIInputSub comp2) {
	Iterator iter = null;
	int i = 0;
	if (!((null == comp1.getValidators() && null == comp2.getValidators())||
	      (null != comp1.getValidators() && null != comp2.getValidators()))) {
	    return false;
	}
	if (null != comp1.getValidators()) {
	    iter = comp1.getValidators().iterator();
	    while (iter.hasNext()) {
		if (!comp2.getValidators().get(i++).equals(iter.next())) {
		    return false;
		}
	    }
	    i = 0;
	    iter = comp2.getValidators().iterator();
	    while (iter.hasNext()) {
		if (!comp1.getValidators().get(i++).equals(iter.next())) {
		    return false;
		}
	    }
	}
	return true;
    }

    public static class UIInputSub extends UIInput {
	public List[] getListeners() { 
	    return listeners;
	}
	public List getValidators() {
	    return validators;
	}
    }


}
