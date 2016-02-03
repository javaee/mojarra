/*
 * $Id: UIInputTestCase.java,v 1.38 2007/04/27 22:00:15 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.faces.TestUtil;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.validator.Validator;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.LongRangeValidator;
import javax.faces.application.Application;
import javax.faces.el.MethodBinding;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


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
        expectedFamily = UIInput.COMPONENT_FAMILY;
        expectedRendererType = "javax.faces.Text";
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

        assertEquals(input.getSubmittedValue(),
                     (String) input.getAttributes().get("submittedValue"));
        input.setSubmittedValue("foo");
        assertEquals("foo", (String) input.getAttributes().get("submittedValue"));
        input.setSubmittedValue(null);
        assertNull((String) input.getAttributes().get("submittedValue"));
        input.getAttributes().put("submittedValue", "bar");
        assertEquals("bar", input.getSubmittedValue());
        input.getAttributes().put("submittedValue", null);
        assertNull(input.getSubmittedValue());

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

        assertEquals(input.isValid(), true);
        assertEquals(input.isValid(),
                     ((Boolean) component.getAttributes().get("valid")).
                     booleanValue());
        input.setValid(false);
        assertEquals(input.isValid(),
                     ((Boolean) component.getAttributes().get("valid")).
                     booleanValue());
        component.getAttributes().put("valid", Boolean.TRUE);
        assertEquals(input.isValid(),
                     ((Boolean) component.getAttributes().get("valid")).
                     booleanValue());

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
            (new TestValueChangeListener("AP0"));
        input.addValueChangeListener
            (new TestValueChangeListener("AP1"));
        input.addValueChangeListener
            (new TestValueChangeListener("AP2"));

        // Fire events and evaluate results
        TestValueChangeListener.trace(null);
        input.broadcast(event);
        assertEquals("/AP0/AP1/AP2",
                     TestValueChangeListener.trace());

    }


    // Test event queuing and broadcasting (mixed phase listeners)
    public void testEventsMixed() {

        UIInput input = (UIInput) component;
	input.setRendererType(null);
	UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	root.getChildren().add(input);
        ValueChangeEvent event  = null;
	
        // Register three listeners
        input.addValueChangeListener
            (new TestValueChangeListener("ARV"));
        input.addValueChangeListener
            (new TestValueChangeListener("PV"));
        input.addValueChangeListener
            (new TestValueChangeListener("AP"));

        TestValueChangeListener.trace(null);
	event = new ValueChangeEvent(input, null, null);
	event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
	input.queueEvent(event);

	event = new ValueChangeEvent(input, null, null);
	event.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
	input.queueEvent(event);

	event = new ValueChangeEvent(input, null, null);
	event.setPhaseId(PhaseId.INVOKE_APPLICATION);
	input.queueEvent(event);

        // Fire events and evaluate results
	root.processDecodes(facesContext);
	root.processValidators(facesContext);
	root.processApplication(facesContext);
        assertEquals("/ARV/PV/AP/ARV/PV/AP/ARV/PV/AP",
                     TestValueChangeListener.trace());

    }

    // Test listener registration and deregistration
    public void testListeners() {

        TestInput input = new TestInput();
        TestValueChangeListener listener = null;

        input.addValueChangeListener
            (new TestValueChangeListener("ARV0"));
        input.addValueChangeListener
            (new TestValueChangeListener("ARV1"));
        input.addValueChangeListener
            (new TestValueChangeListener("PV0"));
        input.addValueChangeListener
            (new TestValueChangeListener("PV1"));
        input.addValueChangeListener
            (new TestValueChangeListener("PV2"));

        ValueChangeListener listeners[] = input.getValueChangeListeners();
        assertEquals(5, listeners.length);
        input.removeValueChangeListener(listeners[2]);
        listeners = input.getValueChangeListeners();
        assertEquals(4, listeners.length);

    }

    // Test empty listener list
    public void testEmptyListeners() {

        TestInput input = new TestInput();
        TestValueChangeListener listener = null;

        //No listeners added, should be empty
        ValueChangeListener listeners[] = input.getValueChangeListeners();
	assertEquals(0, listeners.length);

    }

    // Test a pristine UIInput instance
    public void testPristine() {

        super.testPristine();
        UIInput input = (UIInput) component;

        assertNull("no submittedValue", input.getSubmittedValue());
        assertTrue("not required", !input.isRequired());
        assertTrue("is valid", input.isValid());
        assertTrue("is not immediate", !input.isImmediate());
        assertNull("no validatorBinding", input.getValidator());
        assertNull("no valueChangeListener", input.getValueChangeListener());

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

        input.setSubmittedValue("foo");
        assertEquals("foo", input.getSubmittedValue());
        input.setSubmittedValue(null);
        assertNull(input.getSubmittedValue());

        input.setRequired(true);
        assertTrue(input.isRequired());
        input.setRequired(false);
        assertTrue(!input.isRequired());

        input.setValid(false);
        assertTrue(!input.isValid());
        input.setValid(true);
        assertTrue(input.isValid());

        Application app = facesContext.getApplication();
	MethodBinding methodBinding = null;

        input.setValidator(methodBinding = 
			  app.createMethodBinding("#{foo.bar}", null));
        assertEquals(methodBinding, input.getValidator());
        input.setValidator(null);
        assertNull(input.getValidator());

        input.setValueChangeListener(methodBinding = 
	    app.createMethodBinding("#{foo.bar}", null));
        assertEquals(methodBinding, input.getValueChangeListener());
        input.setValueChangeListener(null);
        assertNull(input.getValueChangeListener());

    }


    // Test updating model values
    public void testUpdateModel() throws Exception {

        // Set up test bean as a request attribute
        TestDataBean test = new TestDataBean();
        test.setCommand("old command");
        request.setAttribute("test", test);

        // Point at the "command" property
        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
        root.getChildren().add(component);
        UIInput input = (UIInput) component;
        input.setValueBinding
            ("value",
             application.createValueBinding("#{test.command}"));
        checkMessages(0);

        // Perform update on a valid value
        input.setValid(true);
        input.setValue("new command");
        assertEquals("new command", input.getLocalValue());
        input.updateModel(facesContext);
        assertEquals("new command", test.getCommand());
        assertNull(input.getLocalValue());
        assertTrue(input.isValid());
        checkMessages(0);
	input.resetValue();
	assertNull(input.getLocalValue());
	assertEquals("new command", input.getValue());
	assertNull(input.getSubmittedValue());
        assertTrue(input.isValid());
	assertTrue(!input.isLocalValueSet());

        // Skip update on an invalid value
        input.setValid(false);
        input.setValue("bad command");
        assertEquals("bad command", input.getLocalValue());
        input.updateModel(facesContext);
        assertEquals("new command", test.getCommand());
        assertEquals("bad command", input.getLocalValue());
        assertTrue(!input.isValid());
        checkMessages(0);

        // Log conversion error on update failure
        input.setValid(true);
        input.setValue(new Integer(5));
        assertEquals(new Integer(5), (Integer) input.getLocalValue());
        input.updateModel(facesContext);
        assertEquals("new command", test.getCommand());
        assertEquals(new Integer(5), (Integer) input.getLocalValue());
        assertTrue(!input.isValid());
        checkMessages(1);

        // Perform update on a null value
        input.setValid(true);
        input.setValue(null);
        assertNull(input.getLocalValue());
        input.updateModel(facesContext);
        assertNull(test.getCommand());
        assertNull(input.getLocalValue());
        assertTrue(input.isValid());
        checkMessages(1);

    }


    // Test order of validator calls with validator also
    public void testValidateOrder() throws Exception {

        Class validateParams[] = {FacesContext.class, UIComponent.class,
                                  Object.class};
    
        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
        root.getChildren().add(component);
        UIInput input = (UIInput) component;
        input.addValidator(new TestInputValidator("v1"));
        input.addValidator(new TestInputValidator("v2"));
        Application app = facesContext.getApplication();
	MethodBinding methodBinding = null;

        input.setValidator(methodBinding = 
			  app.createMethodBinding("v3.validate", validateParams));
        assertEquals(methodBinding, input.getValidator());
        request.setAttribute("v3", new TestInputValidator("v3"));
        TestInputValidator.trace(null);
        setupNewValue(input);
        root.processValidators(facesContext);
        assertEquals("/v1/v2/v3", TestInputValidator.trace());

    }


    // Test validation of a required field
    public void testValidateRequired() throws Exception {

        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
        root.getChildren().add(component);
        UIInput input = (UIInput) component;
        input.setRequired(true);
        checkMessages(0);

        input.setValid(true);
        input.setSubmittedValue("foo");
        input.validate(facesContext);
        checkMessages(0);
        assertTrue(input.isValid());

        input.getAttributes().put("label", "mylabel");
        input.setValid(true);
        input.setSubmittedValue("");
        input.validate(facesContext);
        checkMessages(1);
        assertTrue(!input.isValid());
                                                                 
        Iterator messages = facesContext.getMessages();
        while (messages.hasNext()) {
            FacesMessage message = (FacesMessage) messages.next();
            assertTrue(message.getSummary().indexOf("mylabel") >= 0);
        }
        
        input.setValid(true);
        input.setSubmittedValue(null);
        input.validate(facesContext);
        // awiner: this was formerly "checkMessages(2)", but a submitted
        // value of null now explicitly means _do not validate_.
        checkMessages(1);
        // awiner: And this next line flipped as well
        assertTrue(input.isValid());
    }


    // Test that appropriate properties are value binding enabled
    public void testValueBindings() {

	super.testValueBindings();
	UIInput test = (UIInput) component;

	// "required" property
	request.setAttribute("foo", Boolean.FALSE);
	boolean initial = test.isRequired();
	if (initial) {
	    request.setAttribute("foo", Boolean.FALSE);
	} else {
	    request.setAttribute("foo", Boolean.TRUE);
	}
	test.setValueBinding("required", application.createValueBinding("#{foo}"));
	assertEquals(!initial, test.isRequired());
	test.setRequired(initial);
	assertEquals(initial, test.isRequired());
	assertNotNull(test.getValueBinding("required"));

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

	// "immediate" property
	request.setAttribute("foo", Boolean.FALSE);
	boolean initialImmediate = test.isImmediate();
	if (initialImmediate) {
	    request.setAttribute("foo", Boolean.FALSE);
	} else {
	    request.setAttribute("foo", Boolean.TRUE);
	}
	test.setValueBinding("immediate", application.createValueBinding("#{foo}"));
	assertEquals(!initialImmediate, test.isImmediate());
	test.setImmediate(initialImmediate);
	assertEquals(initialImmediate, test.isImmediate());
	assertNotNull(test.getValueBinding("immediate"));
    }


    // Test order of value change calls with valueChangeListener also
    public void testValueChangeOrder() throws Exception {

        Class signature[] = { ValueChangeEvent.class };
	Application app = facesContext.getApplication();
	MethodBinding methodBinding = null;

        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
        root.getChildren().add(component);
        UIInput input = (UIInput) component;
        input.addValueChangeListener(new TestInputValueChangeListener("l1"));
        input.addValueChangeListener(new TestInputValueChangeListener("l2"));
        input.setValueChangeListener(app.createMethodBinding("l3.processValueChange", signature));
        request.setAttribute("l3", new TestInputValueChangeListener("l3"));
        TestInputValueChangeListener.trace(null);
        setupNewValue(input);
        root.processValidators(facesContext);
        assertEquals("/l1/l2/l3", TestInputValueChangeListener.trace());

    }


    // Test order of value change calls with valueChangeListener also
    public void testImmediate() throws Exception {

        Class signature[] = { ValueChangeEvent.class };
	Application app = facesContext.getApplication();
	MethodBinding methodBinding = null;

        UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
        root.getChildren().add(component);
        UIInput input = (UIInput) component;
        input.setImmediate(true);
        input.addValueChangeListener(new TestInputValueChangeListener("l1"));
        input.addValueChangeListener(new TestInputValueChangeListener("l2"));
        input.setValueChangeListener(app.createMethodBinding("l3.processValueChange", signature));
        request.setAttribute("l3", new TestInputValueChangeListener("l3"));
        TestInputValueChangeListener.trace(null);
        setupNewValue(input);
        root.processValidators(facesContext);
        // No ValueChangeEvent should get delivered, because
        // "immediate" processing fires during processDecodes(), not
        // processValidators()
        assertEquals("", TestInputValueChangeListener.trace());

    }

    public void testGetValueChangeListeners() throws Exception {
	UIInput command = (UIInput) component;
	UIViewRoot root = facesContext.getApplication().getViewHandler().createView(facesContext, null);
	root.getChildren().add(command);
	
	TestValueChangeListener 
	    ta1 = new TestValueChangeListener("ta1"),
	    ta2 = new TestValueChangeListener("ta2");

	command.addValueChangeListener(ta1);
	command.addValueChangeListener(ta2);
	ValueChangeListener [] listeners = (ValueChangeListener [])
	    command.getValueChangeListeners();
	assertEquals(2, listeners.length);
	TestValueChangeListener [] taListeners = (TestValueChangeListener [])
	    command.getFacesListeners(TestValueChangeListener.class);
    }


    // --------------------------------------------------------- Support Methods


    // Check that the number of queued messages equals the expected count
    // and that each of them is of severity ERROR
    protected void checkMessages(int expected) {

        int n = 0;
        Iterator messages = facesContext.getMessages();
        while (messages.hasNext()) {
            FacesMessage message = (FacesMessage) messages.next();
            assertEquals("Severity == ERROR",
                         FacesMessage.SEVERITY_ERROR,
                         message.getSeverity());
            n++;
            // System.err.println(message.getSummary());
        }
        assertEquals("expected message count", expected, n);

    }


    // Check that the properties on the specified components are equal
    protected void checkProperties(UIComponent comp1, UIComponent comp2) {
        super.checkProperties(comp1, comp2);
        UIInput i1 = (UIInput) comp1;
        UIInput i2 = (UIInput) comp2;
        // "submittedValue" is not preserved across state-saves
        //        assertEquals(i1.getSubmittedValue(), i2.getSubmittedValue());
        assertEquals(i1.isRequired(), i2.isRequired());
        assertEquals(i1.isValid(), i2.isValid());
        assertEquals(i1.getValidator(), i2.getValidator());
        assertEquals(i1.getValueChangeListener(), i2.getValueChangeListener());
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
        i.setSubmittedValue("submittedValue");
        i.setValid(false);
        i.setRequired(true);
        Application app = facesContext.getApplication();
	MethodBinding methodBinding = null;

        i.setValidator(methodBinding = 
			  app.createMethodBinding("#{foo.bar}", null));
        i.setValueChangeListener(app.createMethodBinding("#{baz.bop}", null));
    }


    protected boolean listenersAreEqual(FacesContext context,
					UIInput comp1,
					UIInput comp2) {

        ValueChangeListener list1[] = comp1.getValueChangeListeners();
        ValueChangeListener list2[] = comp2.getValueChangeListeners();
        assertNotNull(list1);
        assertNotNull(list2);
        assertEquals(list1.length, list2.length);
        for (int i = 0; i < list1.length; i++) {
            assertTrue(list1[i].getClass() == list2[i].getClass());
        }
	return true;

    }


    protected void setupNewValue(UIInput input) {

        input.setSubmittedValue("foo");

    }


    protected boolean validatorsAreEqual(FacesContext context,
					UIInput comp1,
					UIInput comp2) {

        Validator list1[] = comp1.getValidators();
        Validator list2[] = comp2.getValidators();
        assertNotNull(list1);
        assertNotNull(list2);
        assertEquals(list1.length, list2.length);
        for (int i = 0; i < list1.length; i++) {
            assertTrue(list1[i].getClass() == list2[i].getClass());
        }
        return (true);

    }


}
