/*
 * $Id: UICommandTestCase.java,v 1.20 2003/12/17 15:11:11 rkitain Exp $
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
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.component.ValueHolder;
import javax.faces.event.ActionEvent;
import javax.faces.el.MethodBinding;
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

    private static Class actionListenerSignature[] = { ActionEvent.class };

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


    // Test order of action listener calls with actionListener also
    public void testActionOrder() throws Exception {

        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit =
            renderKitFactory.getRenderKit(RenderKitFactory.DEFAULT_RENDER_KIT);
        renderKit.addRenderer("Button", new ButtonRenderer());
        UIViewRoot root = new UIViewRoot();
        root.getChildren().add(component);
        UICommand command = (UICommand) component;
	MethodBinding binding = facesContext.getApplication().
	    createMethodBinding("#{l3.processAction}", 
				actionListenerSignature);
        command.setId("command");
        command.addActionListener(new TestCommandActionListener("l1"));
        command.addActionListener(new TestCommandActionListener("l2"));
        command.setActionListener(binding);
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
	Application app = facesContext.getApplication();
	MethodBinding methodBinding = null;

        assertEquals(command.getAction(),
                     (MethodBinding) command.getAttributes().get("action"));
        command.setAction(methodBinding =
			  app.createMethodBinding("#{foo.bar}", null));
        assertEquals(methodBinding, 
		     (MethodBinding) command.getAttributes().get("action"));
        command.setAction(null);
        assertNull((MethodBinding) command.getAttributes().get("action"));
	methodBinding = app.createMethodBinding("#{bar.baz}", null);
        command.getAttributes().put("action", methodBinding);
        assertEquals(methodBinding, command.getAction());
        command.getAttributes().put("action", null);
        assertNull(command.getAction());

        assertEquals(command.getActionListener(),
                     (MethodBinding) command.getAttributes().get("actionListener"));
	methodBinding = app.createMethodBinding("#{foo.yoyo}", 
						actionListenerSignature);
        command.setActionListener(methodBinding);
        assertEquals(methodBinding, 
		     (MethodBinding) command.getAttributes().get("actionListener"));
        command.setActionListener(null);
        assertNull((MethodBinding) 
		   command.getAttributes().get("actionListener"));
	methodBinding = app.createMethodBinding("#{foo.buckaroo}", 
						actionListenerSignature);
        command.getAttributes().put("actionListener", methodBinding);
        assertEquals(methodBinding, command.getActionListener());
        command.getAttributes().put("actionListener", null);
        assertNull(command.getActionListener());

    }

    // Test event queuing and broadcasting (any phase listeners)
    public void testEventsGeneric() {

        UICommand command = (UICommand) component;
	command.setRendererType(null);
        ActionEvent event = new ActionEvent(command);

        // Register three listeners
        command.addActionListener
            (new TestActionListener("AP0"));
        command.addActionListener
            (new TestActionListener("AP1"));
        command.addActionListener
            (new TestActionListener("AP2"));

        // Fire events and evaluate results
        TestActionListener.trace(null);
	UIViewRoot root = new UIViewRoot();
	root.getChildren().add(command);
	command.queueEvent(event);
	root.processDecodes(facesContext);
	root.processValidators(facesContext);
	root.processApplication(facesContext);
        assertEquals("/AP0@INVOKE_APPLICATION 5/AP1@INVOKE_APPLICATION 5/AP2@INVOKE_APPLICATION 5",
                     TestActionListener.trace());

    }

    // Test event queuing and broadcasting (mixed phase listeners)
    public void testEventsMixed() {

        UICommand command = (UICommand) component;
	command.setRendererType(null);
        ActionEvent event = new ActionEvent(command);

        // Register three listeners
        command.addActionListener
            (new TestActionListener("ARV"));
        command.addActionListener
            (new TestActionListener("PV"));
        command.addActionListener
            (new TestActionListener("AP"));

        // Fire events and evaluate results
        TestActionListener.trace(null);
	UIViewRoot root = new UIViewRoot();
	root.getChildren().add(command);
	command.queueEvent(event);
	root.processDecodes(facesContext);
	root.processValidators(facesContext);
	root.processApplication(facesContext);
        assertEquals("/ARV@INVOKE_APPLICATION 5/PV@INVOKE_APPLICATION 5/AP@INVOKE_APPLICATION 5",
                     TestActionListener.trace());

    }

    // Test event queuing and broadcasting (mixed phase listeners), with
    // immediate set.
    public void testEventsMixedImmediate() {

        UICommand command = (UICommand) component;
	command.setImmediate(true);
	command.setRendererType(null);
        ActionEvent event = new ActionEvent(command);

        // Register three listeners
        command.addActionListener
            (new TestActionListener("ARV"));
        command.addActionListener
            (new TestActionListener("PV"));
        command.addActionListener
            (new TestActionListener("AP"));

        // Fire events and evaluate results
        TestActionListener.trace(null);
	UIViewRoot root = new UIViewRoot();
	root.getChildren().add(command);
	command.queueEvent(event);
	root.processDecodes(facesContext);
	root.processValidators(facesContext);
	root.processApplication(facesContext);
        assertEquals("/ARV@APPLY_REQUEST_VALUES 2/PV@APPLY_REQUEST_VALUES 2/AP@APPLY_REQUEST_VALUES 2",
                     TestActionListener.trace());

    }


    // Test listener registration and deregistration
    public void testListeners() {

        TestCommand command = new TestCommand();
        TestActionListener listener = null;

        command.addActionListener
            (new TestActionListener("ARV0"));
        command.addActionListener
            (new TestActionListener("ARV1"));
        command.addActionListener
            (new TestActionListener("PV0"));
        command.addActionListener
            (new TestActionListener("PV1"));
        command.addActionListener
            (new TestActionListener("PV2"));

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
        assertNull("no actionListener", command.getActionListener());

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
	Application app = facesContext.getApplication();
	MethodBinding methodBinding = null;

        command.setAction(methodBinding = 
			  app.createMethodBinding("#{foo.bar}", null));
        assertEquals(methodBinding, command.getAction());
        command.setAction(null);
        assertNull(command.getAction());

	methodBinding = app.createMethodBinding("#{foo.yoyo}", 
						actionListenerSignature);
        command.setActionListener(methodBinding);
        assertEquals(methodBinding, command.getActionListener());
        command.setActionListener(null);
        assertNull(command.getActionListener());

    }

    public void testValueBindings() {

	super.testValueBindings();
	UICommand test = (UICommand) component;

	// "immediate" property
	request.setAttribute("foo", Boolean.FALSE);
	boolean initial = test.isImmediate();
	if (initial) {
	    request.setAttribute("foo", Boolean.FALSE);
	} else {
	    request.setAttribute("foo", Boolean.TRUE);
	}
	test.setValueBinding("immediate", application.createValueBinding("#{foo}"));
	assertEquals(!initial, test.isImmediate());
	test.setImmediate(initial);
	assertEquals(initial, test.isImmediate());
	assertNotNull(test.getValueBinding("immediate"));

	// "value" property
	request.setAttribute("foo", "bar");
	test.setValue(null);
	assertNull(test.getValue());
	test.setValueBinding("value", application.createValueBinding("#{foo}"));
	assertNotNull(test.getValueBinding("value"));
	assertEquals("bar", test.getValue());
	test.setValue("baz");
	assertEquals("baz", test.getValue());
	test.setValue(null);
	assertEquals("bar", test.getValue());
	test.setValueBinding("value", null);
	assertNull(test.getValueBinding("value"));
	assertNull(test.getValue());

    }


    // --------------------------------------------------------- Support Methods


    // Check that the properties on the specified components are equal
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {
        super.checkProperties(comp1, comp2);
        UICommand c1 = (UICommand) comp1;
        UICommand c2 = (UICommand) comp2;
        assertEquals(c1.getAction(), c2.getAction());
        assertEquals(c1.getActionListener(), c2.getActionListener());
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
	Application app = facesContext.getApplication();
	MethodBinding methodBinding = null;
	
        c.setAction(methodBinding = app.createMethodBinding("#{foo.bar}", 
							    null));
        c.setActionListener(methodBinding = 
			    app.createMethodBinding("#{baz.bop}", 
						    actionListenerSignature));
    }


    protected boolean listenersAreEqual(FacesContext context,
					UICommandSub comp1,
					UICommandSub comp2) {
	List list1 = comp1.getListeners();
	List list2 = comp2.getListeners();
	// make sure they're either both null or both non-null
	if ((null == list1 && null != list2) ||
	    (null != list1 && null == list2)) {
	    return false;
	}
	if (null == list1) {
	    return true;
	}
	int j = 0, outerLen = list1.size();
	boolean result = true;
	if (outerLen != list2.size()) {
	    return false;
	}
	for (j = 0; j < outerLen; j++) {
	    result = list1.get(j).equals(list2.get(j));
	    if (!result) {
		return false;
	    }
	}
	return true;
    }

    public static class UICommandSub extends UICommand {
	public List getListeners() { 
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
