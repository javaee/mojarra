/*
 * $Id: ValueHolderTestCaseBase.java,v 1.3 2003/09/15 20:17:40 eburns Exp $
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
import javax.faces.component.ValueHolder;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.LongConverter;
import javax.faces.convert.ShortConverter;
import javax.faces.TestUtil;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link ValueHolder}.  Any test case for a component
 * class that implements {@link ValueHolder} should extend this class.</p>
 */

public abstract class ValueHolderTestCaseBase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ValueHolderTestCaseBase(String name) {
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
        return (new TestSuite(ValueHolderTestCaseBase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        ValueHolder valueHolder = (ValueHolder) component;

        assertEquals(valueHolder.getConverter(),
                     (String) valueHolder.getAttribute("converter"));
        valueHolder.setConverter(new LongConverter());
        assertNotNull((Converter) valueHolder.getAttribute("converter"));
        assertTrue
            (valueHolder.getAttribute("converter") instanceof LongConverter);
        valueHolder.setConverter(null);
        assertNull(valueHolder.getAttribute("converter"));
        valueHolder.setAttribute("converter", new ShortConverter());
        assertNotNull(valueHolder.getConverter());
        assertTrue(valueHolder.getConverter() instanceof ShortConverter);
        valueHolder.setAttribute("converter", null);
        assertNull(valueHolder.getConverter());

        assertEquals(valueHolder.getValue(),
                     (String) valueHolder.getAttribute("value"));
        valueHolder.setValue("foo");
        assertEquals("foo", (String) valueHolder.getAttribute("value"));
        valueHolder.setValue(null);
        assertNull((String) valueHolder.getAttribute("value"));
        valueHolder.setAttribute("value", "bar");
        assertEquals("bar", valueHolder.getValue());
        valueHolder.setAttribute("value", null);
        assertNull(valueHolder.getValue());

        assertEquals(valueHolder.getValueRef(),
                     (String) valueHolder.getAttribute("valueRef"));
        valueHolder.setValueRef("foo");
        assertEquals("foo", (String) valueHolder.getAttribute("valueRef"));
        valueHolder.setValueRef(null);
        assertNull((String) valueHolder.getAttribute("valueRef"));
        valueHolder.setAttribute("valueRef", "bar");
        assertEquals("bar", valueHolder.getValueRef());
        valueHolder.setAttribute("valueRef", null);
        assertNull(valueHolder.getValueRef());

    }


    // Test currentValue method
    public void testCurrentValue() {

        // Validate initial conditions
        ValueHolder valueHolder = (ValueHolder) component;
        assertNull(valueHolder.getConverter());
        assertNull(valueHolder.getValue());
        assertNull(valueHolder.getValueRef());

        // Retrieve a local value
        valueHolder.setValue("localValue");
        assertEquals("localValue", valueHolder.currentValue(facesContext));
        valueHolder.setValue(null);

        // Retrieve an application initialization parameter
        /* PENDING(craigmcc) - MockExternalContext support
        valueHolder.setValueRef("initParam.appParamName");
        assertEquals("appParamValue", valueHolder.currentValue(facesContext));
        assertNull(valueHolder.getValue());
        valueHolder.setValueRef(null);
        */

        // Retrieve an application scope attribute
        /* PENDING(craigmcc) - MockExternalContext support
        valueHolder.setValueRef("applicationScope.appScopeName");
        assertEquals("appScopeValue", valueHolder.currentValue(facesContext));
        assertNull(valueHolder.getValue());
        valueHolder.setValueRef(null);
        */

        // Retrieve a request scope attribute
        /* PENDING(craigmcc) - MockExternalContext support
        valueHolder.setValueRef("requestScope.reqScopeName");
        assertEquals("reqScopeValue", valueHolder.currentValue(facesContext));
        assertNull(valueHolder.getValue());
        valueHolder.setValueRef(null);
        */

        // Retrieve a session scope attribute
        /* PENDING(craigmcc) - MockExternalContext support
        valueHolder.setValueRef("sessionScope.sesScopeName");
        assertEquals("sesScopeValue", valueHolder.currentValue(facesContext));
        assertNull(valueHolder.getValue());
        valueHolder.setValueRef(null);
        */

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine ValueHolderBase instance
    public void testPristine() {

        super.testPristine();
        ValueHolder valueHolder = (ValueHolder) component;

        // Validate properties
        assertNull("no converter", valueHolder.getConverter());
        assertNull("no value", valueHolder.getValue());
        assertNull("no valueRef", valueHolder.getValueRef());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        ValueHolder valueHolder = (ValueHolder) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        ValueHolder valueHolder = (ValueHolder) component;

        // converter
        valueHolder.setConverter(new LongConverter());
        assertTrue("expected converter",
                   valueHolder.getConverter() instanceof LongConverter);
        valueHolder.setConverter(null);
        assertNull("erased converter", valueHolder.getConverter());

        // value
        valueHolder.setValue("foo.bar");
        assertEquals("expected value",
                     "foo.bar", valueHolder.getValue());
        valueHolder.setValue(null);
        assertNull("erased value", valueHolder.getValue());

        // valueRef
        valueHolder.setValueRef("customer.name");
        assertEquals("expected valueRef",
                     "customer.name", valueHolder.getValueRef());
        valueHolder.setValueRef(null);
        assertNull("erased valueRef", valueHolder.getValueRef());

    }

    public void testStateHolder() throws Exception {

        UIComponent testParent = new TestComponentNamingContainer("root");
	UIOutputBase
	    preSave = null,
	    postSave = null;
	Object state = null;

	// test component with no properties
	testParent.getChildren().clear();
	preSave = new UIOutputBase();
	preSave.setId("valueHolder");
	preSave.setRendererType(null); // necessary: we have no renderkit
	testParent.getChildren().add(preSave);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIOutputBase();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test component with valueRef
	testParent.getChildren().clear();
	preSave = new UIOutputBase();
	preSave.setId("valueHolder");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setValueRef("valueRefString");
	testParent.getChildren().add(preSave);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIOutputBase();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test component with valueRef and converter
	testParent.getChildren().clear();
	preSave = new UIOutputBase();
	preSave.setId("valueHolder");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setValueRef("valueRefString");
	preSave.setConverter(new StateSavingConverter("testCase State"));
	testParent.getChildren().add(preSave);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIOutputBase();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

    }

    boolean propertiesAreEqual(FacesContext context,
			       UIComponent comp1,
			       UIComponent comp2) {
	if (super.propertiesAreEqual(context, comp1, comp2)) {
	    ValueHolder 
		valueHolder1 = (ValueHolder) comp1,
		valueHolder2 = (ValueHolder) comp2;
	    // if their not both null, or not the same string
	    if (!TestUtil.equalsWithNulls(valueHolder1.getValueRef(),
					  valueHolder2.getValueRef())) {
		return false;
	    }
	    // if their not both null, or not the same string
	    if (!TestUtil.equalsWithNulls(valueHolder1.getValue(),
					  valueHolder2.getValue())) {
		return false;
	    }
            // Are they the same class?
            Converter conv1 = valueHolder1.getConverter();
            Converter conv2 = valueHolder2.getConverter();
            if ((conv1 != null) && (conv2 == null)) {
                return false;
            } else if ((conv1 == null) && (conv2 != null)) {
                return false;
            } else if ((conv1 != null) && (conv2 != null) &&
                       !conv1.equals(conv2)) {
                return false;
            }
	}
	return true;
    }

public static class StateSavingConverter extends Object
    implements Converter, StateHolder {

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

	public Object saveState(FacesContext context) {
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
