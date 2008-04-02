/*
 * $Id: UISelectBooleanTestCase.java,v 1.16 2004/01/27 23:10:16 craigmcc Exp $
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
import javax.faces.component.UISelectBoolean;
import javax.faces.el.ValueBinding;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UISelectBoolean}.</p>
 */

public class UISelectBooleanTestCase extends UIInputTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UISelectBooleanTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UISelectBoolean();
        expectedFamily = UISelectBoolean.COMPONENT_FAMILY;
        expectedRendererType = "javax.faces.Checkbox";
    }

    
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UISelectBooleanTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UISelectBoolean selectBoolean = (UISelectBoolean) component;

        selectBoolean.setSelected(false);
        assertEquals(Boolean.FALSE,
                     (Boolean) selectBoolean.getAttributes().get("selected"));
        selectBoolean.setSelected(true);
        assertEquals(Boolean.TRUE,
                     (Boolean) selectBoolean.getAttributes().get("selected"));
        selectBoolean.getAttributes().put("selected", Boolean.FALSE);
        assertTrue(!selectBoolean.isSelected());
        selectBoolean.getAttributes().put("selected", Boolean.TRUE);
        assertTrue(selectBoolean.isSelected());

    }


    // Test a pristine UISelectBoolean instance
    public void testPristine() {

        super.testPristine();
        UISelectBoolean selectBoolean = (UISelectBoolean) component;

        assertTrue("not selected", !selectBoolean.isSelected());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UISelectBoolean selectBoolean = (UISelectBoolean) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UISelectBoolean selectBoolean = (UISelectBoolean) component;

        selectBoolean.setSelected(true);
        assertTrue(selectBoolean.isSelected());
        assertEquals(Boolean.TRUE,
                     (Boolean) selectBoolean.getValue());
        selectBoolean.setSelected(false);
        assertTrue(!selectBoolean.isSelected());
        assertEquals(Boolean.FALSE,
                     (Boolean) selectBoolean.getValue());

        // Test transparency between "value" and "selected" properties
        selectBoolean.setValue(Boolean.TRUE);
        assertTrue(selectBoolean.isSelected());
        selectBoolean.setValue(Boolean.FALSE);
        assertTrue(!selectBoolean.isSelected());
        selectBoolean.setValue(null);
        assertTrue(!selectBoolean.isSelected());

        // Transparency applies to value bindings as well
        assertNull(selectBoolean.getValueBinding("selected"));
        assertNull(selectBoolean.getValueBinding("value"));
        request.setAttribute("foo", Boolean.TRUE);
        ValueBinding vb = application.createValueBinding("#{foo}");
        selectBoolean.setValueBinding("selected", vb);
        assertTrue(vb == selectBoolean.getValueBinding("selected"));
        assertTrue(vb == selectBoolean.getValueBinding("value"));
        selectBoolean.setValueBinding("selected", null);
        assertNull(selectBoolean.getValueBinding("selected"));
        assertNull(selectBoolean.getValueBinding("value"));
        selectBoolean.setValueBinding("value", vb);
        assertTrue(vb == selectBoolean.getValueBinding("selected"));
        assertTrue(vb == selectBoolean.getValueBinding("value"));
        selectBoolean.setValueBinding("selected", null);
        assertNull(selectBoolean.getValueBinding("selected"));
        assertNull(selectBoolean.getValueBinding("value"));

    }


    public void testValueBindings() {

	super.testValueBindings();
	UISelectBoolean test = (UISelectBoolean) component;

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
        UIComponent component = new UISelectBoolean();
        component.setRendererType(null);
        return (component);
    }


}
