/*
 * $Id: UISelectItemsTestCase.java,v 1.4 2003/10/09 22:58:14 craigmcc Exp $
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
import javax.faces.component.UISelectItems;
import javax.faces.component.ValueHolder;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UISelectItems}.</p>
 */

public class UISelectItemsTestCase extends ValueHolderTestCaseBase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UISelectItemsTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UISelectItems();
        expectedRendererType = null;
    }

    
    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UISelectItemsTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test a pristine UISelectItems instance
    public void testPristine() {

        super.testPristine();
        UISelectItems selectItems = (UISelectItems) component;

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UISelectItems selectItems = (UISelectItems) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UISelectItems selectItems = (UISelectItems) component;


    }


    // --------------------------------------------------------- Support Methods


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UISelectItems();
        component.setRendererType(null);
        return (component);
    }


}
