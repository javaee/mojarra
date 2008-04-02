/*
 * $Id: UIInputTestCase.java,v 1.18 2003/10/27 04:10:09 craigmcc Exp $
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
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
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

        assertEquals(input.getValidateRef(),
                     (String) input.getAttributes().get("validateRef"));
        input.setValidateRef("foo");
        assertEquals("foo", (String) input.getAttributes().get("validateRef"));
        input.setValidateRef(null);
        assertNull((String) input.getAttributes().get("validateRef"));
        input.getAttributes().put("validateRef", "bar");
        assertEquals("bar", input.getValidateRef());
        input.getAttributes().put("validateRef", null);
        assertNull(input.getValidateRef());

        assertEquals(input.getValueChangeListenerRef(),
                     (String) input.getAttributes().get("valueChangeListenerRef"));
        input.setValueChangeListenerRef("foo");
        assertEquals("foo", (String) input.getAttributes().get("valueChangeListenerRef"));
        input.setValueChangeListenerRef(null);
        assertNull((String) input.getAttributes().get("valueChangeListenerRef"));
        input.getAttributes().put("valueChangeListenerRef", "bar");
        assertEquals("bar", input.getValueChangeListenerRef());
        input.getAttributes().put("valueChangeListenerRef", null);
        assertNull(input.getValueChangeListenerRef());

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
        ValueChangeEvent event = new ValueChangeEvent(input, null, null);

        // Register three listeners
        input.addValueChangeListener
            (new TestValueChangeListener("AP0", PhaseId.ANY_PHASE));
        input.addValueChangeListener
            (new TestValueChangeListener("AP1", PhaseId.ANY_PHASE));
        input.addValueChangeListener
            (new TestValueChangeListener("AP2", PhaseId.ANY_PHASE));

        // Fire events and evaluate results
        TestValueChangeListener.trace(null);
        assertTrue(!input.broadcast(event, PhaseId.APPLY_REQUEST_VALUES));
        assertTrue(!input.broadcast(event, PhaseId.PROCESS_VALIDATIONS));
        assertTrue(!input.broadcast(event, PhaseId.INVOKE_APPLICATION));
        assertEquals("/AP0/AP1/AP2",
                     TestValueChangeListener.trace());

    }


    // Test event queuing and broadcasting (mixed phase listeners)
    public void testEventsMixed() {

        UIInput input = (UIInput) component;
        ValueChangeEvent event = new ValueChangeEvent(input, null, null);

        // Register three listeners
        input.addValueChangeListener
            (new TestValueChangeListener("ARV", PhaseId.APPLY_REQUEST_VALUES));
        input.addValueChangeListener
            (new TestValueChangeListener("PV", PhaseId.PROCESS_VALIDATIONS));
        input.addValueChangeListener
            (new TestValueChangeListener("AP", PhaseId.ANY_PHASE));

        // Fire events and evaluate results
        TestValueChangeListener.trace(null);
        assertTrue(input.broadcast(event, PhaseId.APPLY_REQUEST_VALUES));
        assertTrue(!input.broadcast(event, PhaseId.PROCESS_VALIDATIONS));
        assertTrue(!input.broadcast(event, PhaseId.INVOKE_APPLICATION));
        assertEquals("/AP/ARV/PV",
                     TestValueChangeListener.trace());

    }


    // Test event queuing and broadcasting (specific phase listeners)
    public void testEventsSpecific() {

        UIInput input = (UIInput) component;
        ValueChangeEvent event = new ValueChangeEvent(input, null, null);

        // Register five listeners
        input.addValueChangeListener
            (new TestValueChangeListener("ARV0", PhaseId.APPLY_REQUEST_VALUES));
        input.addValueChangeListener
            (new TestValueChangeListener("ARV1", PhaseId.APPLY_REQUEST_VALUES));
        input.addValueChangeListener
            (new TestValueChangeListener("PV0", PhaseId.PROCESS_VALIDATIONS));
        input.addValueChangeListener
            (new TestValueChangeListener("PV1", PhaseId.PROCESS_VALIDATIONS));
        input.addValueChangeListener
            (new TestValueChangeListener("PV2", PhaseId.PROCESS_VALIDATIONS));

        // Fire events and evaluate results
        TestValueChangeListener.trace(null);
        assertTrue(input.broadcast(event, PhaseId.RESTORE_VIEW));
        assertTrue(input.broadcast(event, PhaseId.APPLY_REQUEST_VALUES));
        assertTrue(!input.broadcast(event, PhaseId.PROCESS_VALIDATIONS));
        assertTrue(!input.broadcast(event, PhaseId.UPDATE_MODEL_VALUES));
        assertTrue(!input.broadcast(event, PhaseId.INVOKE_APPLICATION));
        assertEquals("/ARV0/ARV1/PV0/PV1/PV2",
                     TestValueChangeListener.trace());

    }


    // Test listener registration and deregistration
    public void testListeners() {

        TestInput input = new TestInput();
        TestValueChangeListener listener = null;
        List lists[] = null;

        input.addValueChangeListener
            (new TestValueChangeListener("ARV0", PhaseId.APPLY_REQUEST_VALUES));
        input.addValueChangeListener
            (new TestValueChangeListener("ARV1", PhaseId.APPLY_REQUEST_VALUES));
        input.addValueChangeListener
            (new TestValueChangeListener("PV0", PhaseId.PROCESS_VALIDATIONS));
        input.addValueChangeListener
            (new TestValueChangeListener("PV1", PhaseId.PROCESS_VALIDATIONS));
        input.addValueChangeListener
            (new TestValueChangeListener("PV2", PhaseId.PROCESS_VALIDATIONS));

        /* PENDING(craigmcc) - listeners are no longer accessible
        lists = input.getListeners();
        assertEquals(PhaseId.VALUES.size(), lists.length);
        for (int i = 0; i < lists.length; i++) {
            if (i == PhaseId.APPLY_REQUEST_VALUES.getOrdinal()) {
                assertEquals(2, lists[i].size());
                listener = (TestValueChangeListener) lists[i].get(0);
                assertEquals("ARV0", listener.getId());
                listener = (TestValueChangeListener) lists[i].get(1);
                assertEquals("ARV1", listener.getId());
            } else if (i == PhaseId.PROCESS_VALIDATIONS.getOrdinal()) {
                assertEquals(3, lists[i].size());
                listener = (TestValueChangeListener) lists[i].get(0);
                assertEquals("PV0", listener.getId());
                listener = (TestValueChangeListener) lists[i].get(1);
                assertEquals("PV1", listener.getId());
                listener = (TestValueChangeListener) lists[i].get(2);
                assertEquals("PV2", listener.getId());
            } else {
                assertNull(lists[i]);
            }
        }

        input.removeValueChangeListener
            ((ValueChangeListener) lists[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()].get(0));
        input.removeValueChangeListener
            ((ValueChangeListener) lists[PhaseId.PROCESS_VALIDATIONS.getOrdinal()].get(1));

        lists = input.getListeners();
        assertEquals(PhaseId.VALUES.size(), lists.length);
        for (int i = 0; i < lists.length; i++) {
            if (i == PhaseId.APPLY_REQUEST_VALUES.getOrdinal()) {
                assertEquals(1, lists[i].size());
                listener = (TestValueChangeListener) lists[i].get(0);
                assertEquals("ARV1", listener.getId());
            } else if (i == PhaseId.PROCESS_VALIDATIONS.getOrdinal()) {
                assertEquals(2, lists[i].size());
                listener = (TestValueChangeListener) lists[i].get(0);
                assertEquals("PV0", listener.getId());
                listener = (TestValueChangeListener) lists[i].get(1);
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
        assertNull("no validateRef", input.getValidateRef());
        assertNull("no valueChangeListenerRef", input.getValueChangeListenerRef());

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

        input.setValidateRef("foo");
        assertEquals("foo", input.getValidateRef());
        input.setValidateRef(null);
        assertNull(input.getValidateRef());

        input.setValueChangeListenerRef("foo");
        assertEquals("foo", input.getValueChangeListenerRef());
        input.setValueChangeListenerRef(null);
        assertNull(input.getValueChangeListenerRef());

    }


    // Test order of validator calls with validateRef also
    public void testValidateOrder() throws Exception {

        UIViewRoot root = new UIViewRoot();
        root.getChildren().add(component);
        UIInput input = (UIInput) component;
        input.addValidator(new TestInputValidator("v1"));
        input.addValidator(new TestInputValidator("v2"));
        input.setValidateRef("v3.validate");
        request.setAttribute("v3", new TestInputValidator("v3"));
        TestInputValidator.trace(null);
        setupNewValue(input);
        root.processValidators(facesContext);
        assertEquals("/v1/v2/v3", TestInputValidator.trace());

    }


    // Test order of value change calls with valueChangeListenerRef also
    public void testValueChangeOrder() throws Exception {

        UIViewRoot root = new UIViewRoot();
        root.getChildren().add(component);
        UIInput input = (UIInput) component;
        input.addValueChangeListener(new TestInputValueChangeListener("l1"));
        input.addValueChangeListener(new TestInputValueChangeListener("l2"));
        input.setValueChangeListenerRef("l3.processValueChange");
        request.setAttribute("l3", new TestInputValueChangeListener("l3"));
        TestInputValueChangeListener.trace(null);
        setupNewValue(input);
        root.processValidators(facesContext);
        assertEquals("/l1/l2/l3", TestInputValueChangeListener.trace());

    }


    // --------------------------------------------------------- Support Methods


    // Check that the properties on the specified components are equal
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {
        super.checkProperties(comp1, comp2);
        UIInput i1 = (UIInput) comp1;
        UIInput i2 = (UIInput) comp2;
        assertEquals(i1.getPrevious(), i2.getPrevious());
        assertEquals(i1.isRequired(), i2.isRequired());
        assertEquals(i1.getValidateRef(), i2.getValidateRef());
        assertEquals(i1.getValueChangeListenerRef(), i2.getValueChangeListenerRef());
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
        i.setValidateRef("foo.bar");
        i.setValueChangeListenerRef("baz.bop");
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
		    result = ((TestValueChangeListener)list1[i].get(j)).isEqual(list2[i].get(j));
		    if (!result) {
			return false;
		    }
		}
	    }
	}
	return true;
    }


    protected void setupNewValue(UIInput input) {

        input.setValue("foo");

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
