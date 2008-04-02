/*
 * $Id: UISelectManyBaseTestCase.java,v 1.4 2003/09/19 00:57:17 craigmcc Exp $
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
import javax.faces.component.UISelectMany;
import javax.faces.component.ValueHolder;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UISelectManyBase}.</p>
 */

public class UISelectManyBaseTestCase extends UIInputBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UISelectManyBaseTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UISelectManyBase();
        expectedRendererType = "Listbox";
    }

    
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UISelectManyBaseTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test the compareValues() method
    public void testCompareValues() {

        TestSelectManyBase selectMany = new TestSelectManyBase();
        Object values1a[] = new Object[] { "foo", "bar", "baz" };
        Object values1b[] = new Object[] { "foo", "baz", "bar" };
        Object values1c[] = new Object[] { "baz", "foo", "bar" };
        Object values2[] = new Object[] { "foo", "bar" };
        Object values3[] = new Object[] { "foo", "bar", "baz", "bop" };
        Object values4[] = null;

        assertTrue(!selectMany.compareValues(values1a, values1a));
        assertTrue(!selectMany.compareValues(values1a, values1b));
        assertTrue(!selectMany.compareValues(values1a, values1c));
        assertTrue(!selectMany.compareValues(values2, values2));
        assertTrue(!selectMany.compareValues(values3, values3));
        assertTrue(!selectMany.compareValues(values4, values4));

        assertTrue(selectMany.compareValues(values1a, values2));
        assertTrue(selectMany.compareValues(values1a, values3));
        assertTrue(selectMany.compareValues(values1a, values4));
        assertTrue(selectMany.compareValues(values2, values3));
        assertTrue(selectMany.compareValues(values2, values4));
        assertTrue(selectMany.compareValues(values4, values1a));
        assertTrue(selectMany.compareValues(values4, values2));
        assertTrue(selectMany.compareValues(values4, values3));

    }


    // Test a pristine UISelectManyBase instance
    public void testPristine() {

        super.testPristine();
        UISelectMany selectMany = (UISelectMany) component;

        assertNull("no selectedValues", selectMany.getSelectedValues());

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UISelectMany selectMany = (UISelectMany) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UISelectMany selectMany = (UISelectMany) component;

        Object values[] = new Object[] { "foo", "bar" };

        selectMany.setSelectedValues(values);
        assertEquals(values, selectMany.getSelectedValues());
        assertEquals(values, (Object[]) selectMany.getValue());
        selectMany.setSelectedValues(null);
        assertNull(selectMany.getSelectedValues());
        assertNull(selectMany.getValue());

        // Test transparency between "value" and "selectedValues" properties
        selectMany.setValue(values);
        assertEquals(values, selectMany.getSelectedValues());
        assertEquals(values, (Object[]) selectMany.getValue());
        selectMany.setValue(null);
        assertNull(selectMany.getSelectedValues());
        assertNull(selectMany.getValue());

    }



    // Test validation of value against the valid list
    public void testValidation() throws Exception {

        // Add valid options to the component under test
        UISelectMany selectMany = (UISelectMany) component;
        selectMany.getChildren().add(new UISelectItemSub("foo", null, null));
        selectMany.getChildren().add(new UISelectItemSub("bar", null, null));
        selectMany.getChildren().add(new UISelectItemSub("baz", null, null));

        // Validate two values that are on the list
        selectMany.setValid(true);
        selectMany.setValue(new Object[] { "foo", "baz" });
        selectMany.validate(facesContext);
        assertTrue(selectMany.isValid());

        // Validate one value on the list and one not on the list
        selectMany.setValid(true);
        selectMany.setValue(new Object[] { "bar", "bop"});
        selectMany.validate(facesContext);
        assertTrue(!selectMany.isValid());

    }


    protected ValueHolder createValueHolder() {

        UIComponent component = new UISelectManyBase();
        component.setRendererType(null);
        return ((ValueHolder) component);

    }


}
