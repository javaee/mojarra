/*
 * $Id: UICommandTestCase.java,v 1.17 2003/11/07 01:23:55 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.component.ValueHolder;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;
import javax.faces.TestUtil;
import javax.faces.mock.MockExternalContext;
import javax.faces.render.Renderer;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UICommand}.</p>
 */

public class UICommandTestCase extends ValueHolderTestCaseBase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UICommandTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UICommand();
        expectedRendererType = "Button";
    }

    
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UICommandTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test order of action listener calls with actionListenerRef also
    public void testActionOrder() throws Exception {

        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit =
            renderKitFactory.getRenderKit(RenderKitFactory.DEFAULT_RENDER_KIT);
        renderKit.addRenderer("Button", new ButtonRenderer());
        UIViewRoot root = new UIViewRoot();
        root.getChildren().add(component);
        UICommand command = (UICommand) component;
        command.setId("command");
        command.addActionListener(new TestCommandActionListener("l1"));
        command.addActionListener(new TestCommandActionListener("l2"));
        command.setActionListenerRef("l3.processAction");
        command.setImmediate(true);
        request.setAttribute("l3", new TestCommandActionListener("l3"));
        Map map = new HashMap();
        map.put(command.getClientId(facesContext), "");
        MockExternalContext econtext =
            (MockExternalContext) facesContext.getExternalContext();
        econtext.setRequestParameterMap(map);
        TestCommandActionListener.trace(null);
        root.processDecodes(facesContext);
        assertEquals("/l1/l2/l3", TestCommandActionListener.trace());

    }


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UICommand command = (UICommand) component;

        assertEquals(command.getAction(),
                     (String) command.getAttributes().get("action"));
        command.setAction("foo");
        assertEquals("foo", (String) command.getAttributes().get("action"));
        command.setAction(null);
        assertNull((String) command.getAttributes().get("action"));
        command.getAttributes().put("action", "bar");
        assertEquals("bar", command.getAction());
        command.getAttributes().put("action", null);
        assertNull(command.getAction());

        assertEquals(command.getActionListenerRef(),
                     (String) command.getAttributes().get("actionListenerRef"));
        command.setActionListenerRef("foo");
        assertEquals("foo", (String) command.getAttributes().get("actionListenerRef"));
        command.setActionListenerRef(null);
        assertNull((String) command.getAttributes().get("actionListenerRef"));
        command.getAttributes().put("actionListenerRef", "bar");
        assertEquals("bar", command.getActionListenerRef());
        command.getAttributes().put("actionListenerRef", null);
        assertNull(command.getActionListenerRef());

        assertEquals(command.getActionRef(),
                     (String) command.getAttributes().get("actionRef"));
        command.setActionRef("foo");
        assertEquals("foo", (String) command.getAttributes().get("actionRef"));
        command.setActionRef(null);
        assertNull((String) command.getAttributes().get("actionRef"));
        command.getAttributes().put("actionRef", "bar");
        assertEquals("bar", command.getActionRef());
        command.getAttributes().put("actionRef", null);
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
        assertEquals("/AP0/AP1/AP2",
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
        assertEquals("/AP/ARV/PV",
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

        TestCommand command = new TestCommand();
        TestActionListener listener = null;

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

        ActionListener listeners[] = command.getActionListeners();
        assertEquals(6, listeners.length); // Count the default one
        command.removeActionListener(listeners[2]);
        listeners = command.getActionListeners();
        assertEquals(5, listeners.length);

    }


    // Test a pristine UICommand instance
    public void testPristine() {

        super.testPristine();
        UICommand command = (UICommand) component;

        assertNull("no action", command.getAction());
        assertNull("no actionListenerRef", command.getActionListenerRef());
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

        command.setActionListenerRef("foo");
        assertEquals("foo", command.getActionListenerRef());
        command.setActionListenerRef(null);
        assertNull(command.getActionListenerRef());

        command.setActionRef("foo");
        assertEquals("foo", command.getActionRef());
        command.setActionRef(null);
        assertNull(command.getActionRef());

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


    public void testValueBindings() {

	super.testValueBindings();
	UICommand test = (UICommand) component;

	request.setAttribute("foo", "bar");
	test.setAction(null);
	assertNull(test.getAction());
	test.setAction("baz");
	assertEquals("baz", test.getAction());
	test.setValueBinding("action", application.getValueBinding("#{foo}"));
	assertEquals("bar", test.getAction());
	assertNotNull(test.getValueBinding("action"));
	test.setAction("bop");
	assertEquals("bop", test.getAction());
	assertNull(test.getValueBinding("action"));

	request.setAttribute("foo", Boolean.FALSE);
	test.setImmediate(true);
	assertTrue(test.isImmediate());
	test.setValueBinding("immediate", application.getValueBinding("#{foo}"));
	assertTrue(!test.isImmediate());
	assertNotNull(test.getValueBinding("immediate"));
	test.setImmediate(false);
	assertTrue(!test.isImmediate());
	assertNull(test.getValueBinding("immediate"));

    }


    // --------------------------------------------------------- Support Methods


    // Check that the properties on the specified components are equal
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {
        super.checkProperties(comp1, comp2);
        UICommand c1 = (UICommand) comp1;
        UICommand c2 = (UICommand) comp2;
        assertEquals(c1.getAction(), c2.getAction());
        assertEquals(c1.getActionListenerRef(), c2.getActionListenerRef());
        assertEquals(c1.getActionRef(), c2.getActionRef());
    }


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UICommand();
        component.setRendererType(null);
        return (component);
    }


    // Populate a pristine component to be used in state holder tests
    protected void populateComponent(UIComponent component) {
        super.populateComponent(component);
        UICommand c = (UICommand) component;
        c.setAction("foo");
        c.setActionListenerRef("baz.bop");
        c.setActionRef("bar");
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

    public static class UICommandSub extends UICommand {
	public List[] getListeners() { 
	    return listeners;
	}
    }

    // --------------------------------------------------------- Private Classes


    // "Button" Renderer
    class ButtonRenderer extends Renderer {

        public void decode(FacesContext context, UIComponent component) {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

            if (!(component instanceof ActionSource)) {
                return;
            }
            String clientId = component.getClientId(context);
            Map params = context.getExternalContext().getRequestParameterMap();
            if (params.containsKey(clientId)) {
                component.queueEvent(new ActionEvent(component));
            }

        }

        public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

        }

        public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

        }

        public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {

            if ((context == null) || (component == null)) {
                throw new NullPointerException();
            }

        }

    }


}
