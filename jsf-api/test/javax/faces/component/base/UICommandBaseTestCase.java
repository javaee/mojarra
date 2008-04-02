/*
 * $Id: UICommandBaseTestCase.java,v 1.13 2003/09/09 20:51:26 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;
import javax.faces.TestUtil;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UICommandBase}.</p>
 */

public class UICommandBaseTestCase extends ValueHolderTestCaseBase {


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
        assertTrue(command.broadcast(event, PhaseId.APPLY_REQUEST_VALUES));
        assertTrue(command.broadcast(event, PhaseId.PROCESS_VALIDATIONS));
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
        assertTrue(command.broadcast(event, PhaseId.PROCESS_VALIDATIONS));
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
        assertTrue(command.broadcast(event, PhaseId.RESTORE_VIEW));
        assertTrue(command.broadcast(event, PhaseId.APPLY_REQUEST_VALUES));
        assertTrue(command.broadcast(event, PhaseId.PROCESS_VALIDATIONS));
        assertTrue(command.broadcast(event, PhaseId.UPDATE_MODEL_VALUES));
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

        /* PENDING(craigmcc) - listeners are no longer accessible
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
        */

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

    public void testStateHolder() throws Exception {

        UIComponent testParent = new TestComponentNamingContainer("root");
	UICommandSub
	    preSave = null,
	    postSave = null;
	Object state = null;

	// test page with no attributes
	testParent.getChildren().clear();
	preSave = new UICommandSub();
	preSave.setId("command");
	preSave.setRendererType(null); // necessary: we have no renderkit
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UICommandSub();
	postSave.setId("command");
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(null != postSave.getListeners());
	// make sure the default action listener has been added on restore
	List [] lister = (List []) postSave.getListeners();
	assertTrue(lister[PhaseId.INVOKE_APPLICATION.getOrdinal()].get(0) == 
		   facesContext.getApplication().getActionListener());
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test page with action and actionRef
	testParent.getChildren().clear();
	preSave = new UICommandSub();
	preSave.setId("command");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setAction("action");
	preSave.setActionRef("actionRef");
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UICommandSub();
	postSave.setId("command");
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test page with action and actionRef, and listeners
	testParent.getChildren().clear();
	preSave = new UICommandSub();
	preSave.setId("command");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setAction("action");
	preSave.setActionRef("actionRef");
	testParent.getChildren().add(preSave);
	preSave.addActionListener(new TestActionListener("ANY",
							 PhaseId.ANY_PHASE));
	preSave.addActionListener(new TestActionListener("APR0",
							 PhaseId.APPLY_REQUEST_VALUES));
	preSave.addActionListener(new TestActionListener("APR1",
							 PhaseId.APPLY_REQUEST_VALUES));
	preSave.addActionListener(new TestActionListener("UMV0",
							 PhaseId.UPDATE_MODEL_VALUES));
	preSave.addActionListener(new TestActionListener("UMV1",
							 PhaseId.UPDATE_MODEL_VALUES));
	preSave.addActionListener(new TestActionListener("UMV2",
							 PhaseId.UPDATE_MODEL_VALUES));
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UICommandSub();
	postSave.setId("command");
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test page with converter, value, and valueRef
	testParent.getChildren().clear();
	preSave = new UICommandSub();
	preSave.setId("valueHolder");
	preSave.setRendererType(null); // necessary: we have no renderkit
        preSave.setValue("valueString");
	preSave.setValueRef("valueRefString");
	preSave.setConverter(new StateSavingConverter("testCase State"));
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UICommandSub();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));


    }

    public void testImmediate() throws Exception {
	List [] listeners = null;
	UICommandSub command = new UICommandSub();
	assertTrue(!command.isImmediate());

	// if there is a change in the immediate flag, from false to
	// true, the default action listener should be replaced with an
	// instance of WrapperActionListener.
	command.setImmediate(true);
	assertTrue(command.isImmediate());
	listeners = command.getListeners();
	// we should have one listener for APPLY_REQUEST_VALUES
	assertTrue(1 == 
		   ((List)listeners[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()]).size());
	// we should have no listeners for INVOKE_APPLICATION
	assertTrue(0 == 
		   ((List)listeners[PhaseId.INVOKE_APPLICATION.getOrdinal()]).size());
	
	// if there is a change in the immediate flag, from true to
	// false, the default action listener should be restored.
	command.setImmediate(false);
	assertTrue(!command.isImmediate());
	listeners = command.getListeners();
	// we should have one listener for INVOKE_APPLICATION
	assertTrue(1 == 
		   ((List)listeners[PhaseId.INVOKE_APPLICATION.getOrdinal()]).size());
	// we should have no listeners for APPLY_REQUEST_VALUES
	assertTrue(0 == 
		   ((List)listeners[PhaseId.APPLY_REQUEST_VALUES.getOrdinal()]).size());

    }


    // -------------------------------------------------------- Support Methods

    boolean propertiesAreEqual(FacesContext context,
			       UIComponent comp1,
			       UIComponent comp2) {
	UICommandSub 
	    command1 = (UICommandSub) comp1,
	    command2 = (UICommandSub) comp2;
	if (super.propertiesAreEqual(context, comp1, comp2)) {
            if (command1.isImmediate() != command2.isImmediate()) {
                return false;
            }
	    // if their not both null, or not the same string
	    if (!TestUtil.equalsWithNulls(command1.getAction(),
					  command2.getAction())) {
		return false;
	    }
	    // if their not both null, or not the same string
	    if (!TestUtil.equalsWithNulls(command1.getActionRef(),
					  command2.getActionRef())) {
		return false;
	    }
	    
	}
	return listenersAreEqual(context, command1, command2);
    }

    protected boolean listenersAreEqual(FacesContext context,
					UICommandSub comp1,
					UICommandSub comp2) {
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
		    result = list1[i].get(j).equals(list2[i].get(j));
		    if (!result) {
			return false;
		    }
		}
	    }
	}
	return true;
    }

    public static class UICommandSub extends UICommandBase {
	public List[] getListeners() { 
	    return listeners;
	}
    }

}
