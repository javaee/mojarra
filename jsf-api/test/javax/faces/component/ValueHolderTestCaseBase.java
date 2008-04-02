/*
 * $Id: ValueHolderTestCaseBase.java,v 1.1 2003/09/25 07:46:13 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.LongConverter;
import javax.faces.convert.NumberConverter;
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
        component = new UIOutput();
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
                     (String) component.getAttributes().get("converter"));
        valueHolder.setConverter(new LongConverter());
        assertNotNull((Converter) component.getAttributes().get("converter"));
        assertTrue
            (component.getAttributes().get("converter") instanceof LongConverter);
        valueHolder.setConverter(null);
        assertNull(component.getAttributes().get("converter"));
        component.getAttributes().put("converter", new ShortConverter());
        assertNotNull(valueHolder.getConverter());
        assertTrue(valueHolder.getConverter() instanceof ShortConverter);
        component.getAttributes().put("converter", null);
        assertNull(valueHolder.getConverter());

        assertEquals(valueHolder.getValue(),
                     (String) component.getAttributes().get("value"));
        valueHolder.setValue("foo");
        assertEquals("foo", (String) component.getAttributes().get("value"));
        valueHolder.setValue(null);
        assertNull((String) component.getAttributes().get("value"));
        component.getAttributes().put("value", "bar");
        assertEquals("bar", valueHolder.getValue());
        component.getAttributes().put("value", null);
        assertNull(valueHolder.getValue());

        assertEquals(valueHolder.getValueRef(),
                     (String) component.getAttributes().get("valueRef"));
        valueHolder.setValueRef("foo");
        assertEquals("foo", (String) component.getAttributes().get("valueRef"));
        valueHolder.setValueRef(null);
        assertNull((String) component.getAttributes().get("valueRef"));
        component.getAttributes().put("valueRef", "bar");
        assertEquals("bar", valueHolder.getValueRef());
        component.getAttributes().put("valueRef", null);
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
	UIOutput
	    preSave = null,
	    postSave = null;
	Object state = null;

	// test component with no properties
	testParent.getChildren().clear();
	preSave = new UIOutput();
	preSave.setId("valueHolder");
	preSave.setRendererType(null); // necessary: we have no renderkit
	testParent.getChildren().add(preSave);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIOutput();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test component with valueRef
	testParent.getChildren().clear();
	preSave = new UIOutput();
	preSave.setId("valueHolder");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setValueRef("valueRefString");
	testParent.getChildren().add(preSave);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIOutput();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test component with valueRef and converter
	testParent.getChildren().clear();
	preSave = new UIOutput();
	preSave.setId("valueHolder");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setValueRef("valueRefString");
	preSave.setConverter(new StateSavingConverter("testCase State"));
	testParent.getChildren().add(preSave);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIOutput();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

    }


    // Special save/restore test for components implementing ValueHolderBase
    public void testValueHolderBase() throws Exception {

        UIComponent testParent = new TestComponentNamingContainer("root");
        ValueHolder
            preSave = null,
            postSave = null;
        Object state = null;

        // Create and populate test component
        preSave = createValueHolder();
        preSave.setConverter(createNumberConverter());
        preSave.setValue("foo");
        preSave.setValueRef("bar.baz");

        // Save and restore state
        testParent.getChildren().clear();
        testParent.getChildren().add(preSave);
        state = ((StateHolder) preSave).saveState(facesContext);
        testParent.getChildren().clear();
        postSave = createValueHolder();
        testParent.getChildren().add(postSave);
        ((StateHolder) postSave).restoreState(facesContext, state);

        // Validate the results
        checkValueHolders(preSave, postSave);
        checkNumberConverters((NumberConverter) preSave.getConverter(),
                              (NumberConverter) postSave.getConverter());

    }


    protected NumberConverter createNumberConverter() {

        NumberConverter nc = new NumberConverter();
        nc.setCurrencyCode("USD");
        nc.setCurrencySymbol("$");
        nc.setGroupingUsed(false);
        nc.setIntegerOnly(true);
        nc.setMaxFractionDigits(2);
        nc.setMaxIntegerDigits(10);
        nc.setMinFractionDigits(2);
        nc.setMinIntegerDigits(5);
        nc.setType("currency");
        return (nc);

    }


    // Create and return a new component of the type being tested
    // (must implement ValueHolder and StateHolder, and return
    // null for rendererType)
    protected ValueHolder createValueHolder() {

        UIComponent component = new UIOutput();
        component.setRendererType(null);
        return ((ValueHolder) component);

    }


    protected void checkNumberConverters(NumberConverter nc1,
                               NumberConverter nc2) {

        assertNotNull(nc1);
        assertNotNull(nc2);
        assertEquals(nc1.getCurrencyCode(), nc2.getCurrencyCode());
        assertEquals(nc1.getCurrencySymbol(), nc2.getCurrencySymbol());
        assertEquals(nc1.isGroupingUsed(), nc2.isGroupingUsed());
        assertEquals(nc1.isIntegerOnly(), nc2.isIntegerOnly());
        assertEquals(nc1.getMaxFractionDigits(), nc2.getMaxFractionDigits());
        assertEquals(nc1.getMaxIntegerDigits(), nc2.getMaxIntegerDigits());
        assertEquals(nc1.getMinFractionDigits(), nc2.getMinFractionDigits());
        assertEquals(nc1.getMinIntegerDigits(), nc2.getMinIntegerDigits());
        assertEquals(nc1.getParseLocale(), nc2.getParseLocale());
        assertEquals(nc1.getPattern(), nc2.getPattern());
        assertEquals(nc1.getType(), nc2.getType());

    }


    protected void checkValueHolders(ValueHolder vh1,
                                     ValueHolder vh2) {

        assertNotNull(vh1);
        assertNotNull(vh2);
        assertEquals(vh1.getValue(), vh2.getValue());
        assertEquals(vh1.getValueRef(), vh2.getValueRef());

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
    
        public void setComponent(UIComponent yourComponent) {
	    // we don't keep a back reference to our component, but if we
	    // did, here is where we'd restore it.
	}

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
