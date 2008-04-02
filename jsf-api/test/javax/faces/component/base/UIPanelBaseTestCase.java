/*
 * $Id: UIPanelBaseTestCase.java,v 1.2 2003/07/26 17:55:23 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIPanelBase}.</p>
 */

public class UIPanelBaseTestCase extends UIOutputBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIPanelBaseTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIPanelBase();
        expectedId = null;
        expectedRendererType = null;
        expectedRendersChildren = true;
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIPanelBaseTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test a pristine UIPanelBase instance
    public void testPristine() {

        super.testPristine();
        UIPanel panel = (UIPanel) component;

    }


    // Test setting properties to invalid values
    public void testPropertiesInvalid() throws Exception {

        super.testPropertiesInvalid();
        UIPanel panel = (UIPanel) component;

    }


    // Test setting properties to valid values
    public void testPropertiesValid() throws Exception {

        super.testPropertiesValid();
        UIPanel panel = (UIPanel) component;

    }



}
