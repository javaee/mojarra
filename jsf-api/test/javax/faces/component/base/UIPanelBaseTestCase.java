/*
 * $Id: UIPanelBaseTestCase.java,v 1.3 2003/08/30 00:31:42 craigmcc Exp $
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

public class UIPanelBaseTestCase extends ValueHolderTestCaseBase {


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


    // Test saving and restoring state
    public void testStateHolder() throws Exception {

        UIComponent testParent = new TestComponentNamingContainer("root");
	UIPanel
	    preSave = null,
	    postSave = null;
	Object state = null;

	// test component with no properties
	testParent.getChildren().clear();
	preSave = new UIPanelBase();
	preSave.setId("panel");
	preSave.setRendererType(null); // necessary: we have no renderkit
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIPanelBase();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test component with valueRef
	testParent.getChildren().clear();
	preSave = new UIPanelBase();
	preSave.setId("panel");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setValueRef("valueRefString");
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIPanelBase();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test component with valueRef and converter
	testParent.getChildren().clear();
	preSave = new UIPanelBase();
	preSave.setId("panel");
	preSave.setRendererType(null); // necessary: we have no renderkit
	preSave.setValueRef("valueRefString");
	preSave.setConverter(new StateSavingConverter("testCase State"));
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIPanelBase();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

    }


}
