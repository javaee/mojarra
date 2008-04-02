/*
 * $Id: UISelectOneTestCase.java,v 1.18 2004/01/27 20:30:10 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UISelectOne}.</p>
 */

public class UISelectOneTestCase extends UIInputTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UISelectOneTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UISelectOne();
        expectedFamily = UISelectOne.COMPONENT_FAMILY;
        expectedRendererType = "javax.faces.Menu";
    }

    
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UISelectOneTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test a pristine UISelectOne instance
    public void testPristine() {

        super.testPristine();
        UISelectOne selectOne = (UISelectOne) component;

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UISelectOne selectOne = (UISelectOne) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UISelectOne selectOne = (UISelectOne) component;

    }


    // Test validation of value against the valid list
    public void testValidation() throws Exception {

        // Put our component under test in a tree under a UIViewRoot
        UIViewRoot root = new UIViewRoot();
        root.getChildren().add(component);

        // Add valid options to the component under test
        UISelectOne selectOne = (UISelectOne) component;
        selectOne.getChildren().add(new UISelectItemSub("foo", null, null));
        selectOne.getChildren().add(new UISelectItemSub("bar", null, null));
        selectOne.getChildren().add(new UISelectItemSub("baz", null, null));

        // Validate a value that is on the list
        selectOne.setValid(true);
        selectOne.setSubmittedValue("bar");
        selectOne.setRendererType(null); // We don't have any renderers
        selectOne.validate(facesContext);
        assertTrue(selectOne.isValid());

        // Validate a value that is not on the list
        selectOne.setValid(true);
        selectOne.setSubmittedValue("bop");
        selectOne.validate(facesContext);
        assertTrue(!selectOne.isValid());

    }


    // Test validation of a required field
    public void testValidateRequired() throws Exception {

        UIViewRoot root = new UIViewRoot();
        root.getChildren().add(component);
        UISelectOne selectOne = (UISelectOne) component;
        selectOne.getChildren().add(new UISelectItemSub("foo", null, null));
        selectOne.getChildren().add(new UISelectItemSub("bar", null, null));
        selectOne.getChildren().add(new UISelectItemSub("baz", null, null));
        selectOne.setRequired(true);
        checkMessages(0);

        selectOne.setValid(true);
        selectOne.setSubmittedValue("foo");
        selectOne.validate(facesContext);
        checkMessages(0);
        assertTrue(selectOne.isValid());

        selectOne.setValid(true);
        selectOne.setSubmittedValue("");
        selectOne.validate(facesContext);
        checkMessages(1);
        assertTrue(!selectOne.isValid());

        selectOne.setValid(true);
        selectOne.setSubmittedValue(null);
        // awiner: see UIInputTestCase
        selectOne.validate(facesContext);
        checkMessages(1);
        assertTrue(selectOne.isValid());

    }


    // Test that appropriate properties are value binding enabled
    public void testValueBindings() {

	super.testValueBindings();
	UISelectOne test = (UISelectOne) component;

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


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UISelectOne();
        component.setRendererType(null);
        return (component);
    }


    protected void setupNewValue(UIInput input) {

        input.setSubmittedValue("foo");
        UISelectItem si = new UISelectItem();
        si.setItemValue("foo");
        si.setItemLabel("foo label");
        input.getChildren().add(si);

    }


}
