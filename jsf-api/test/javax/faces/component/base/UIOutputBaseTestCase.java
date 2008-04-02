/*
 * $Id: UIOutputBaseTestCase.java,v 1.6 2003/08/21 15:26:07 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.StateHolder;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.LongConverter;
import javax.faces.convert.ShortConverter;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIOutputBase}.</p>
 */

public class UIOutputBaseTestCase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIOutputBaseTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIOutputBase();
        expectedId = null;
        expectedRendererType = "Text";
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIOutputBaseTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UIOutput output = (UIOutput) component;

        assertEquals(output.getConverter(),
                     (String) output.getAttribute("converter"));
        output.setConverter(new LongConverter());
        assertNotNull((Converter) output.getAttribute("converter"));
        assertTrue(output.getAttribute("converter") instanceof LongConverter);
        output.setConverter(null);
        assertNull(output.getAttribute("converter"));
        output.setAttribute("converter", new ShortConverter());
        assertNotNull(output.getConverter());
        assertTrue(output.getConverter() instanceof ShortConverter);
        output.setAttribute("converter", null);
        assertNull(output.getConverter());

        assertEquals(output.getValue(),
                     (String) output.getAttribute("value"));
        output.setValue("foo");
        assertEquals("foo", (String) output.getAttribute("value"));
        output.setValue(null);
        assertNull((String) output.getAttribute("value"));
        output.setAttribute("value", "bar");
        assertEquals("bar", output.getValue());
        output.setAttribute("value", null);
        assertNull(output.getValue());

        assertEquals(output.getValueRef(),
                     (String) output.getAttribute("valueRef"));
        output.setValueRef("foo");
        assertEquals("foo", (String) output.getAttribute("valueRef"));
        output.setValueRef(null);
        assertNull((String) output.getAttribute("valueRef"));
        output.setAttribute("valueRef", "bar");
        assertEquals("bar", output.getValueRef());
        output.setAttribute("valueRef", null);
        assertNull(output.getValueRef());

    }


    // Test currentValue method
    public void testCurrentValue() {

        // Validate initial conditions
        UIOutput output = (UIOutput) component;
        assertNull(output.getConverter());
        assertNull(output.getValue());
        assertNull(output.getValueRef());

        // Retrieve a local value
        output.setValue("localValue");
        assertEquals("localValue", output.currentValue(facesContext));
        output.setValue(null);

        // Retrieve an application initialization parameter
        /* PENDING(craigmcc) - MockExternalContext support
        output.setValueRef("initParam.appParamName");
        assertEquals("appParamValue", output.currentValue(facesContext));
        assertNull(output.getValue());
        output.setValueRef(null);
        */

        // Retrieve an application scope attribute
        /* PENDING(craigmcc) - MockExternalContext support
        output.setValueRef("applicationScope.appScopeName");
        assertEquals("appScopeValue", output.currentValue(facesContext));
        assertNull(output.getValue());
        output.setValueRef(null);
        */

        // Retrieve a request scope attribute
        /* PENDING(craigmcc) - MockExternalContext support
        output.setValueRef("requestScope.reqScopeName");
        assertEquals("reqScopeValue", output.currentValue(facesContext));
        assertNull(output.getValue());
        output.setValueRef(null);
        */

        // Retrieve a session scope attribute
        /* PENDING(craigmcc) - MockExternalContext support
        output.setValueRef("sessionScope.sesScopeName");
        assertEquals("sesScopeValue", output.currentValue(facesContext));
        assertNull(output.getValue());
        output.setValueRef(null);
        */

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine UIOutputBase instance
    public void testPristine() {

        super.testPristine();
        UIOutput output = (UIOutput) component;

        // Validate properties
        assertNull("no converter", output.getConverter());
        assertNull("no value", output.getValue());
        assertNull("no valueRef", output.getValueRef());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIOutput output = (UIOutput) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIOutput output = (UIOutput) component;

        // converter
        output.setConverter(new LongConverter());
        assertTrue("expected converter",
                   output.getConverter() instanceof LongConverter);
        output.setConverter(null);
        assertNull("erased converter", output.getConverter());

        // value
        output.setValue("foo.bar");
        assertEquals("expected value",
                     "foo.bar", output.getValue());
        output.setValue(null);
        assertNull("erased value", output.getValue());

        // valueRef
        output.setValueRef("customer.name");
        assertEquals("expected valueRef",
                     "customer.name", output.getValueRef());
        output.setValueRef(null);
        assertNull("erased valueRef", output.getValueRef());

    }

    public void testStateHolder() {
        UIComponent testParent = new TestComponentNamingContainer("root");
	UIOutput
	    preSave = null,
	    postSave = null;
	Object state = null;

	// test output with no properties
	testParent.getChildren().clear();
	preSave = new UIOutputBase();
	preSave.setId("output");
	preSave.setRendererType(null); // necessary: we have no renderkit
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIOutputBase();
	testParent.getChildren().add(postSave);
	try {
	    postSave.restoreState(facesContext, state);
	}
	catch (Throwable e) {
	    assertTrue(false);
	}
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test output with valueRef
	testParent.getChildren().clear();
	preSave = new UIOutputBase();
	preSave.setId("output");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setValueRef("valueRefString");
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIOutputBase();
	testParent.getChildren().add(postSave);
	try {
	    postSave.restoreState(facesContext, state);
	}
	catch (Throwable e) {
	    assertTrue(false);
	}
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test output with valueRef and converter
	testParent.getChildren().clear();
	preSave = new UIOutputBase();
	preSave.setId("output");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setValueRef("valueRefString");
	preSave.setConverter(new StateSavingConverter("testCase State"));
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIOutputBase();
	testParent.getChildren().add(postSave);
	try {
	    postSave.restoreState(facesContext, state);
	}
	catch (Throwable e) {
	    assertTrue(false);
	}
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));


    }

    boolean propertiesAreEqual(FacesContext context,
			       UIComponent comp1,
			       UIComponent comp2) {
	if (super.propertiesAreEqual(context, comp1, comp2)) {
	    UIOutput 
		output1 = (UIOutput) comp1,
		output2 = (UIOutput) comp2;
	    // if their not both null, or not the same string
	    if (!((null == output1.getValueRef() && 
		   null == output2.getValueRef()) ||
		(output1.getValueRef().equals(output2.getValueRef())))) {
		return false;
	    }
	    // if their not both null, or not the same string
	    if (!((null == output1.getValue() && 
		   null == output2.getValue()) ||
		(output1.getValue().equals(output2.getValue())))) {
		return false;
	    }
	}
	return true;
    }

public static class StateSavingConverter extends Object implements Converter, StateHolder {
	protected String state = null;

	public StateSavingConverter(String newState) {
	    state = newState;
	}

	public StateSavingConverter() {
	}
	
	public Object getAsObject(FacesContext context, UIComponent component,
				  String value) throws ConverterException {
	    return this.getClass().getName();
	}

	public String getAsString(FacesContext context, UIComponent component,
				  Object value) throws ConverterException {
	    return this.getClass().getName();
	}

	public Object getState(FacesContext context) {
	    return state;
	}

	public void restoreState(FacesContext context, Object newState) throws IOException {
	    state = (String) newState;
	}

	public boolean isTransient() { return false; }

	public void setTransient(boolean newTransientValue) {}

	public boolean equals(Object otherObj) {
	    if (!(otherObj instanceof StateSavingConverter)) {
		return false;
	    }
	    StateSavingConverter other = (StateSavingConverter) otherObj;
	    // if not both null, or not the same string
	    if (!((null == this.state && null == other.state) ||
		(this.state.equals(other.state)))) {
		return false;
	    }	 
	    return true;
	}
	    
    }

}
