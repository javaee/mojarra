/*
 * $Id: UIOutputBaseTestCase.java,v 1.12 2003/09/22 19:03:46 eburns Exp $
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
 * <p>Unit tests for {@link UIOutputBase}.</p>
 */

public class UIOutputBaseTestCase extends ValueHolderTestCaseBase {


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

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine UIOutputBase instance
    public void testPristine() {

        super.testPristine();
        UIOutput output = (UIOutput) component;

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

    }


    // Test saving and restoring state
    public void testStateHolder() throws Exception {

        UIComponent testParent = new TestComponentNamingContainer("root");
	UIOutput
	    preSave = null,
	    postSave = null;
	Object state = null;

	// test component with no properties
	testParent.getChildren().clear();
	preSave = new UIOutputBase();
	preSave.setId("output");
	preSave.setRendererType(null); // necessary: we have no renderkit
	testParent.getChildren().add(preSave);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIOutputBase();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test component with valueRef and value
	testParent.getChildren().clear();
	preSave = new UIOutputBase();
	preSave.setId("output");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setValueRef("valueRefString");
	preSave.setValue(new Integer(1));
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
	preSave.setId("output");
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



    protected ValueHolder createValueHolder() {

        UIComponent component = new UIOutputBase();
        component.setRendererType(null);
        return ((ValueHolder) component);

    }


    boolean propertiesAreEqual(FacesContext context,
			       UIComponent comp1,
			       UIComponent comp2) {
	if (super.propertiesAreEqual(context, comp1, comp2)) {
	    UIOutput 
		output1 = (UIOutput) comp1,
		output2 = (UIOutput) comp2;
	    // if their not both null, or not the same string
	    if (!TestUtil.equalsWithNulls(output1.getValueRef(),
					  output2.getValueRef())) {
		return false;
	    }
	    // if their not both null, or not the same string
	    if (!TestUtil.equalsWithNulls(output1.getValue(),
					  output2.getValue())) {
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
