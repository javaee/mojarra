/*
 * $Id: ConvertableValueHolderTestCaseBase.java,v 1.1 2003/10/09 19:18:21 craigmcc Exp $
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
 * <p>Unit tests for {@link ConvertableValueHolder}.  Any test case for a
 * component class that implements {@link ConvertableValueHolder} should
 * extend this class.</p>
 */

public abstract class ConvertableValueHolderTestCaseBase
    extends ValueHolderTestCaseBase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ConvertableValueHolderTestCaseBase(String name) {
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
        return (new TestSuite(ConvertableValueHolderTestCaseBase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        ConvertableValueHolder valueHolder = (ConvertableValueHolder) component;

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

        assertEquals(valueHolder.isValid(), true);
        assertEquals(valueHolder.isValid(),
                     ((Boolean) component.getAttributes().get("valid")).booleanValue());
        valueHolder.setValid(false);
        assertEquals(valueHolder.isValid(),
                     ((Boolean) component.getAttributes().get("valid")).booleanValue());
        component.getAttributes().put("valid", Boolean.TRUE);
        assertEquals(valueHolder.isValid(),
                     ((Boolean) component.getAttributes().get("valid")).booleanValue());

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine ConvertableValueHolderBase instance
    public void testPristine() {

        super.testPristine();
        ConvertableValueHolder valueHolder = (ConvertableValueHolder) component;

        // Validate properties
        assertNull("no converter", valueHolder.getConverter());
        assertTrue("is valid", valueHolder.isValid());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        ConvertableValueHolder valueHolder = (ConvertableValueHolder) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        ConvertableValueHolder valueHolder = (ConvertableValueHolder) component;

        // converter
        valueHolder.setConverter(new LongConverter());
        assertTrue("expected converter",
                   valueHolder.getConverter() instanceof LongConverter);
        valueHolder.setConverter(null);
        assertNull("erased converter", valueHolder.getConverter());

        valueHolder.setValid(false);
        assertTrue(!valueHolder.isValid());
        valueHolder.setValid(true);
        assertTrue(valueHolder.isValid());

    }

    public void testStateHolder() throws Exception {

        UIComponent testParent = new TestComponent("root");
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


    // Special save/restore test for components implementing ValueHolder
    public void testValueHolder() throws Exception {

        super.testValueHolder();

        UIComponent testParent = new TestComponent("root");
        ConvertableValueHolder
            preSave = null,
            postSave = null;
        Object state = null;

        // Create and populate test component
        preSave = (ConvertableValueHolder) createValueHolder();
        preSave.setConverter(createNumberConverter());
        preSave.setValid(false);
        preSave.setValue("foo");
        preSave.setValueRef("bar.baz");

        // Save and restore state
        testParent.getChildren().clear();
        testParent.getChildren().add(preSave);
        state = ((StateHolder) preSave).saveState(facesContext);
        testParent.getChildren().clear();
        postSave = (ConvertableValueHolder) createValueHolder();
        testParent.getChildren().add(postSave);
        ((StateHolder) postSave).restoreState(facesContext, state);

        // Validate the results
        checkValueHolders(preSave, postSave);
        assertEquals(preSave.isValid(), postSave.isValid());
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


    boolean propertiesAreEqual(FacesContext context,
			       UIComponent comp1,
			       UIComponent comp2) {
	if (super.propertiesAreEqual(context, comp1, comp2)) {
	    ConvertableValueHolder 
		valueHolder1 = (ConvertableValueHolder) comp1,
		valueHolder2 = (ConvertableValueHolder) comp2;

            if (!(valueHolder1.isValid() == valueHolder2.isValid())) {
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
