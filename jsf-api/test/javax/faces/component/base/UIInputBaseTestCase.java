/*
 * $Id: UIInputBaseTestCase.java,v 1.6 2003/08/22 14:03:26 eburns Exp $
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
import javax.faces.validator.StringRangeValidator;


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

    public void testStateHolder() {
        UIComponent testParent = new TestComponentNamingContainer("root");
	UIInputSub
	    preSave = null,
	    postSave = null;
	Object state = null;

	// test input with no attributes
	testParent.getChildren().clear();
	preSave = new UIInputSub();
	preSave.setId("input");
	preSave.setRendererType(null); // necessary: we have no renderkit
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIInputSub();
	postSave.setId("input");
	testParent.getChildren().add(postSave);
	try {
	    postSave.restoreState(facesContext, state);
	}
	catch (Throwable e) {
	    assertTrue(false);
	}
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test input with previous and valid
	testParent.getChildren().clear();
	preSave = new UIInputSub();
	preSave.setId("input");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setPrevious("previous");
	preSave.setValid(false);
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIInputSub();
	postSave.setId("input");
	testParent.getChildren().add(postSave);
	try {
	    postSave.restoreState(facesContext, state);
	}
	catch (Throwable e) {
	    assertTrue(false);
	}
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test input with previous, valid, required and listeners
	testParent.getChildren().clear();
	preSave = new UIInputSub();
	preSave.setId("input");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setPrevious("previous");
	preSave.setValid(false);
	preSave.setRequired(true);
	testParent.getChildren().add(preSave);
	preSave.addValueChangedListener(new TestValueChangedListener("ANY",
								     PhaseId.ANY_PHASE));
	preSave.addValueChangedListener(new TestValueChangedListener("APR0",
								     PhaseId.APPLY_REQUEST_VALUES));
	preSave.addValueChangedListener(new TestValueChangedListener("APR1",
								     PhaseId.APPLY_REQUEST_VALUES));
	preSave.addValueChangedListener(new TestValueChangedListener("UMV0",
								     PhaseId.UPDATE_MODEL_VALUES));
	preSave.addValueChangedListener(new TestValueChangedListener("UMV1",
								     PhaseId.UPDATE_MODEL_VALUES));
	preSave.addValueChangedListener(new TestValueChangedListener("UMV2",
								     PhaseId.UPDATE_MODEL_VALUES));
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIInputSub();
	postSave.setId("input");
	testParent.getChildren().add(postSave);
	try {
	    postSave.restoreState(facesContext, state);
	}
	catch (Throwable e) {
	    assertTrue(false);
	}
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));
	assertTrue(listenersAreEqual(facesContext, preSave, postSave));

	// test input with previous, valid, required, listeners and validators
	testParent.getChildren().clear();
	preSave = new UIInputSub();
	preSave.setId("input");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setPrevious("previous");
	preSave.setValid(false);
	preSave.setRequired(true);
	testParent.getChildren().add(preSave);
	preSave.addValueChangedListener(new TestValueChangedListener("ANY",
								     PhaseId.ANY_PHASE));
	preSave.addValueChangedListener(new TestValueChangedListener("APR0",
								     PhaseId.APPLY_REQUEST_VALUES));
	preSave.addValueChangedListener(new TestValueChangedListener("APR1",
								     PhaseId.APPLY_REQUEST_VALUES));
	preSave.addValueChangedListener(new TestValueChangedListener("UMV0",
								     PhaseId.UPDATE_MODEL_VALUES));
	preSave.addValueChangedListener(new TestValueChangedListener("UMV1",
								     PhaseId.UPDATE_MODEL_VALUES));
	preSave.addValueChangedListener(new TestValueChangedListener("UMV2",
								     PhaseId.UPDATE_MODEL_VALUES));
	preSave.addValidator(new TestValidator("buckaroo"));
	DoubleRangeValidator doubleVal = new DoubleRangeValidator();
	doubleVal.setMinimum(3.14);
	doubleVal.setMaximum(6.02);
	preSave.addValidator(doubleVal);
	LengthValidator lengthVal = new LengthValidator();
	lengthVal.setMinimum(3);
	lengthVal.setMaximum(6);
	preSave.addValidator(lengthVal);
	LongRangeValidator longVal = new LongRangeValidator();
	longVal.setMinimum(3);
	longVal.setMaximum(6);
	preSave.addValidator(longVal);
	StringRangeValidator stringVal = new StringRangeValidator();
	stringVal.setMinimum("a");
	stringVal.setMaximum("z");
	preSave.addValidator(stringVal);

	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIInputSub();
	postSave.setId("input");
	testParent.getChildren().add(postSave);
	try {
	    postSave.restoreState(facesContext, state);
	}
	catch (Throwable e) {
	    assertTrue(false);
	}
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));
	assertTrue(listenersAreEqual(facesContext, preSave, postSave));
	assertTrue(validatorsAreEqual(facesContext, preSave, postSave));
    }    
 
    boolean propertiesAreEqual(FacesContext context,
			       UIComponent comp1,
			       UIComponent comp2) {
	UIInputSub 
	    input1 = (UIInputSub) comp1,
	    input2 = (UIInputSub) comp2;
	if (super.propertiesAreEqual(context, comp1, comp2)) {
	    // if their not both null, or not the same string
	    if (!((null == input1.getPrevious() && 
		   null == input2.getPrevious()) ||
		(input1.getPrevious().equals(input2.getPrevious())))) {
		return false;
	    }	 
	    if (input1.isRequired() != input2.isRequired()) {
		return false;
	    }
	    if (input1.isValid() != input2.isValid()) {
		return false;
	    }
	    
	}
	return true;
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

    public static class UIInputSub extends UIInputBase {
	public List[] getListeners() { 
	    return listeners;
	}
	public List getValidators() {
	    return validators;
	}
    }


}
