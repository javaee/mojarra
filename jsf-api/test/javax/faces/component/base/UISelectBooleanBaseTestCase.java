/*
 * $Id: UISelectBooleanBaseTestCase.java,v 1.3 2003/09/19 00:57:16 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.ValueHolder;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UISelectBooleanBase}.</p>
 */

public class UISelectBooleanBaseTestCase extends UIInputBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UISelectBooleanBaseTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UISelectBooleanBase();
        expectedRendererType = "Checkbox";
    }

    
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UISelectBooleanBaseTestCase.class));
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
                     (Boolean) selectBoolean.getAttribute("selected"));
        selectBoolean.setSelected(true);
        assertEquals(Boolean.TRUE,
                     (Boolean) selectBoolean.getAttribute("selected"));
        selectBoolean.setAttribute("selected", Boolean.FALSE);
        assertTrue(!selectBoolean.isSelected());
        selectBoolean.setAttribute("selected", Boolean.TRUE);
        assertTrue(selectBoolean.isSelected());

    }


    // Test a pristine UISelectBooleanBase instance
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

    }


    protected ValueHolder createValueHolder() {

        UIComponent component = new UISelectBooleanBase();
        component.setRendererType(null);
        return ((ValueHolder) component);

    }




}
