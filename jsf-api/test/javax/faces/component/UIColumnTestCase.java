/*
 * $Id: UIColumnTestCase.java,v 1.3 2003/09/15 20:17:34 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.base.TestComponentNamingContainer;
import javax.faces.component.base.UIComponentBaseTestCase;
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


    // Test saving and restoring state
    public void testStateHolder() throws Exception {

        UIComponent testParent = new TestComponentNamingContainer("root");
	UIColumn
	    preSave = null,
	    postSave = null;
	Object state = null;

	// test component with no properties
	testParent.getChildren().clear();
	preSave = new UIColumn();
	preSave.setId("column");
	preSave.setRendererType(null); // necessary: we have no renderkit
	testParent.getChildren().add(preSave);
	state = preSave.saveState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIColumn();
	testParent.getChildren().add(postSave);
        postSave.restoreState(facesContext, state);
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

    }


    // PENDING(craigmcc) - copied from UIComponentBaseTestCase
    // until it is moved back into the javax.faces.component package
    boolean propertiesAreEqual(FacesContext context,
			       UIComponent comp1,
			       UIComponent comp2) {
	// if they're not both null, or not the same string
	if (!((null == comp1.getClientId(context) && 
	     null == comp2.getClientId(context)) ||
	    (comp1.getClientId(context).equals(comp2.getClientId(context))))) {
	    return false;
	}
	// if they're not both null, or not the same string
	if (!((null == comp1.getId() && 
	     null == comp2.getId()) ||
	    (comp1.getId().equals(comp2.getId())))) {
	    return false;
	}
	// if they're not both null, or not the same string
	if (!((null == comp1.getComponentRef() && 
	     null == comp2.getComponentRef()) ||
	    (comp1.getComponentRef().equals(comp2.getComponentRef())))) {
	    return false;
	}
	if (comp1.isRendered() != comp2.isRendered()) {
	    return false;
	}
	// if they're not both null, or not the same string
	if (!((null == comp1.getRendererType() && 
	     null == comp2.getRendererType()) ||
	    (comp1.getRendererType().equals(comp2.getRendererType())))) {
	    return false;
	}
	if (comp1.isTransient() != comp2.isTransient()) {
	    return false;
	}
	if (!attributesAreEqual(comp1, comp2)) {
	    return false;
	}
	return true;
    }


}
