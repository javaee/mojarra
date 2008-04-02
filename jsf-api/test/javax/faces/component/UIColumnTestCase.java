/*
 * $Id: UIColumnTestCase.java,v 1.7 2003/10/09 22:58:11 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.context.FacesContext;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIColumn}.</p>
 */

public class UIColumnTestCase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIColumnTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIColumn();
        expectedId = null;
        expectedRendererType = null;
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIColumnTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test attribute-property transparency
    public void testAttributesTransparency() {

        super.testAttributesTransparency();
        UIColumn column = (UIColumn) component;

    }


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine UIColumn instance
    public void testPristine() {

        super.testPristine();
        UIColumn column = (UIColumn) component;

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIColumn column = (UIColumn) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIColumn column = (UIColumn) component;

    }


    // --------------------------------------------------------- Support Methods


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UIColumn();
        component.setRendererType(null);
        return (component);
    }


}
