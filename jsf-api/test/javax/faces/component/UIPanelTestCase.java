/*
 * $Id: UIPanelTestCase.java,v 1.16 2004/01/27 20:30:09 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link UIPanel}.</p>
 */

public class UIPanelTestCase extends UIComponentBaseTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIPanelTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() {
        super.setUp();
        component = new UIPanel();
        expectedFamily = UIPanel.COMPONENT_FAMILY;
        expectedId = null;
        expectedRendererType = null;
        expectedRendersChildren = false;
    }


    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(UIPanelTestCase.class));
    }


    // Tear down instance variables required by ths test case
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods

    // Suppress lifecycle tests since we do not have a renderer
    public void testLifecycleManagement() {
    }

    // Test a pristine UIPanel instance
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


    public void testValueBindings() {

	super.testValueBindings();
	UIPanel test = (UIPanel) component;


    }


    // --------------------------------------------------------- Support Methods


    // Create a pristine component of the type to be used in state holder tests
    protected UIComponent createComponent() {
        UIComponent component = new UIPanel();
        component.setRendererType(null);
        return (component);
    }


}
