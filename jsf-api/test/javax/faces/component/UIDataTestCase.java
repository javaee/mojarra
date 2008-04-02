/*
 * $Id: UIDataTestCase.java,v 1.11 2003/10/09 19:18:27 craigmcc Exp $
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
import javax.faces.convert.ShortConverter;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIData}.</p>
 */

public class UIDataTestCase extends ValueHolderTestCaseBase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIDataTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIData();
        expectedId = null;
        expectedRendererType = "Table";
        expectedRendersChildren = true;
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIDataTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UIData data = (UIData) component;

        data.setFirst(6);
        assertEquals(data.getFirst(),
                     ((Integer) data.getAttributes().get("first")).intValue());
        data.getAttributes().put("first", new Integer(7));
        assertEquals(data.getFirst(),
                     ((Integer) data.getAttributes().get("first")).intValue());

        data.setRows(10);
        assertEquals(data.getRows(),
                     ((Integer) data.getAttributes().get("rows")).intValue());
        data.getAttributes().put("rows", new Integer(20));
        assertEquals(data.getRows(),
                     ((Integer) data.getAttributes().get("rows")).intValue());

        assertEquals(data.getVar(),
                     (String) data.getAttributes().get("var"));
        data.setVar("foo");
        assertEquals("foo", (String) data.getAttributes().get("var"));
        data.setVar(null);
        assertNull((String) data.getAttributes().get("var"));
        data.getAttributes().put("var", "bar");
        assertEquals("bar", data.getVar());
        data.getAttributes().put("var", null);
        assertNull(data.getVar());

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine UIData instance
    public void testPristine() {

        super.testPristine();
        UIData data = (UIData) component;

        assertEquals("no first", 0, data.getFirst());
        assertEquals("no rows", 0, data.getRows());
        assertNull("no var", data.getVar());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIData data = (UIData) component;

        try {
            data.setFirst(-1);
            fail("Should have thrown IAE");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        try {
            data.setRows(-1);
            fail("Should have thrown IAE");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIData data = (UIData) component;

        data.setFirst(0);
        data.setFirst(11);
        data.setRows(0);
        data.setRows(20);
        data.setVar(null);
        data.setVar("foo");

    }


    // Test saving and restoring state
    public void testStateHolder() throws Exception {

        UIComponent testParent = new TestComponent("root");
	UIData
	    preSave = null,
	    postSave = null;
	Object state = null;

	// test component with no properties
	testParent.getChildren().clear();
	preSave = new UIData();
	preSave.setId("data");
	preSave.setRendererType(null); // necessary: we have no renderkit
	testParent.getChildren().add(preSave);
        preSave.getClientId(facesContext);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIData();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test component with valueRef
	testParent.getChildren().clear();
	preSave = new UIData();
	preSave.setId("data");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setValueRef("valueRefString");
	testParent.getChildren().add(preSave);
        preSave.getClientId(facesContext);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIData();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test component with valueRef
	testParent.getChildren().clear();
	preSave = new UIData();
	preSave.setId("data");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setValueRef("valueRefString");
	testParent.getChildren().add(preSave);
        preSave.getClientId(facesContext);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIData();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test component with UIData specific properties
	testParent.getChildren().clear();
	preSave = new UIData();
	preSave.setId("data");
	preSave.setRendererType(null); // necessary: we have no renderkit
        preSave.setFirst(11);
        preSave.setRows(20);
        preSave.setVar("foo");
	testParent.getChildren().add(preSave);
        preSave.getClientId(facesContext);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIData();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

    }


    protected ValueHolder createValueHolder() {

        UIComponent component = new UIData();
        component.setRendererType(null);
        return ((ValueHolder) component);

    }


    boolean propertiesAreEqual(FacesContext context,
                               UIComponent comp1,
                               UIComponent comp2) {

        if (superPropertiesAreEqual(context, comp1, comp2)) {
	    UIData 
		data1 = (UIData) comp1,
		data2 = (UIData) comp2;
	    // if their not both null, or not the same string
	    if (!((null == data1.getValueRef() && 
		   null == data2.getValueRef()) ||
		(data1.getValueRef().equals(data2.getValueRef())))) {
		return false;
	    }
	    // if their not both null, or not the same string
	    if (!((null == data1.getValue() && 
		   null == data2.getValue()) ||
		(data1.getValue().equals(data2.getValue())))) {
		return false;
	    }
	    // if their not both null, or not the same string
	    if (!((null == data1.getVar() && 
		   null == data2.getVar()) ||
		(data1.getVar().equals(data2.getVar())))) {
		return false;
	    }
            if (!(data1.getFirst() == data2.getFirst()) ||
                !(data1.getRows() == data2.getRows())) {
                return false;
            }
        }
        return (true);

    }


    // PENDING(craigmcc) - copied from ValueHolderTestCaseBase
    // until it is moved back into the javax.faces.component package
    boolean superPropertiesAreEqual(FacesContext context,
			       UIComponent comp1,
			       UIComponent comp2) {
	if (superSuperPropertiesAreEqual(context, comp1, comp2)) {
	    ValueHolder 
		valueHolder1 = (ValueHolder) comp1,
		valueHolder2 = (ValueHolder) comp2;
	    // if their not both null, or not the same string
	    if (!((null == valueHolder1.getValueRef() && 
		   null == valueHolder2.getValueRef()) ||
		(valueHolder1.getValueRef().equals(valueHolder2.getValueRef())))) {
		return false;
	    }
	    // if their not both null, or not the same string
	    if (!((null == valueHolder1.getValue() && 
		   null == valueHolder2.getValue()) ||
		(valueHolder1.getValue().equals(valueHolder2.getValue())))) {
		return false;
	    }
	}
	return true;
    }


    // PENDING(craigmcc) - copied from UIComponentBaseTestCase
    // until it is moved back into the javax.faces.component package
    boolean superSuperPropertiesAreEqual(FacesContext context,
                                         UIComponent comp1,
                                         UIComponent comp2) {
	// if they're not both null, or not the same string
	if (!((null == comp1.getClientId(context) && 
	     null == comp2.getClientId(context)) ||
	    (comp1.getClientId(context).equals(comp2.getClientId(context))))) {
	    return false;
	}
	// if they're not both null, or not the same string
	if (!((null == comp1.getId() && 
	     null == comp2.getId()) ||
	    (comp1.getId().equals(comp2.getId())))) {
	    return false;
	}
	// if they're not both null, or not the same string
	if (!((null == comp1.getComponentRef() && 
	     null == comp2.getComponentRef()) ||
	    (comp1.getComponentRef().equals(comp2.getComponentRef())))) {
	    return false;
	}
	if (comp1.isRendered() != comp2.isRendered()) {
	    return false;
	}
	// if they're not both null, or not the same string
	if (!((null == comp1.getRendererType() && 
	     null == comp2.getRendererType()) ||
	    (comp1.getRendererType().equals(comp2.getRendererType())))) {
	    return false;
	}
	if (comp1.isTransient() != comp2.isTransient()) {
	    return false;
	}
	if (!attributesAreEqual(comp1, comp2)) {
	    return false;
	}
	return true;
    }


}
