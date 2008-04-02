/*
 * $Id: UIParameterTestCase.java,v 1.7 2003/09/25 07:46:11 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import javax.faces.TestUtil;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIParameter}.</p>
 */

public class UIParameterTestCase extends ValueHolderTestCaseBase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIParameterTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIParameter();
        expectedId = null;
        expectedRendererType = null;
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIParameterTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UIParameter parameter = (UIParameter) component;

        assertEquals(parameter.getName(),
                     (String) parameter.getAttributes().get("name"));
        parameter.setName("foo");
        assertEquals("foo", (String) parameter.getAttributes().get("name"));
        parameter.setName(null);
        assertNull((String) parameter.getAttributes().get("name"));
        parameter.getAttributes().put("name", "bar");
        assertEquals("bar", parameter.getName());
        parameter.getAttributes().put("name", null);
        assertNull(parameter.getName());

    }


    // Test a pristine UIParameter instance
    public void testPristine() {

        super.testPristine();
        UIParameter parameter = (UIParameter) component;

        assertNull("no name", parameter.getName());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIParameter parameter = (UIParameter) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIParameter parameter = (UIParameter) component;

        parameter.setName("foo");
        assertEquals("foo", parameter.getName());
        parameter.setName(null);
        assertNull(parameter.getName());

    }


    // Test saving and restoring state
    public void testStateHolder() throws Exception {

        UIComponent testParent = new TestComponentNamingContainer("root");
	UIParameter
	    preSave = null,
	    postSave = null;
	Object state = null;

	// test component with no properties
	testParent.getChildren().clear();
	preSave = new UIParameter();
	preSave.setId("parameter");
	preSave.setRendererType(null); // necessary: we have no renderkit
	testParent.getChildren().add(preSave);
        preSave.getClientId(facesContext);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIParameter();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test component with valueRef
	testParent.getChildren().clear();
	preSave = new UIParameter();
	preSave.setId("parameter");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setValueRef("valueRefString");
	testParent.getChildren().add(preSave);
        preSave.getClientId(facesContext);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIParameter();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test component with valueRef and converter
	testParent.getChildren().clear();
	preSave = new UIParameter();
	preSave.setId("parameter");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setValueRef("valueRefString");
	preSave.setConverter(new StateSavingConverter("testCase State"));
	testParent.getChildren().add(preSave);
        preSave.getClientId(facesContext);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIParameter();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

    }


    protected ValueHolder createValueHolder() {

        UIComponent component = new UIParameter();
        component.setRendererType(null);
        return ((ValueHolder) component);

    }


    boolean propertiesAreEqual(FacesContext context,
			       UIComponent comp1,
			       UIComponent comp2) {

	UIParameter
	    param1 = (UIParameter) comp1,
	    param2 = (UIParameter) comp2;
	if (super.propertiesAreEqual(context, comp1, comp2)) {
	    // if their not both null, or not the same string
	    if (!TestUtil.equalsWithNulls(param1.getName(),
					  param2.getName())) {
		return false;
	    }
	}
	return true;

    }


}
