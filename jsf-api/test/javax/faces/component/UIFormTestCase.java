/*
 * $Id: UIFormTestCase.java,v 1.8 2004/01/27 20:30:08 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIForm}.</p>
 */

public class UIFormTestCase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIFormTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIForm();
        expectedFamily = UIForm.COMPONENT_FAMILY;
        expectedId = null;
        expectedRendererType = "javax.faces.Form";
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIFormTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }


    // Test a pristine UIForm instance
    public void testPristine() {

        super.testPristine();
        UIForm form = (UIForm) component;

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIForm form = (UIForm) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIForm form = (UIForm) component;

    }



}
