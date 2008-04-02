/*
 * $Id: UIViewRootBaseTestCase.java,v 1.2 2003/09/09 20:51:28 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.util.Iterator;
import javax.faces.event.FacesEvent;
import javax.faces.validator.Validator;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.faces.mock.MockExternalContext;
import javax.faces.mock.MockFacesContext;
import javax.faces.mock.MockHttpServletRequest;
import javax.faces.mock.MockHttpServletResponse;
import javax.faces.mock.MockLifecycle;
import javax.faces.mock.MockServletContext;
import javax.faces.TestUtil;

/**
 * <p>Test case for the <strong>javax.faces.base.UIViewRootBase</strong>
 * concrete class.</p>
 */

public class UIViewRootBaseTestCase extends UIComponentBaseTestCase {


    // ----------------------------------------------------- Instance Variables

    protected FacesContext context = null;

    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIViewRootBaseTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UIViewRootBaseTestCase.class));

    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {

        component = null;

    }


    // ------------------------------------------------ Individual Test Methods

    public void testStateHolder() {
        UIComponent testParent = new TestComponentNamingContainer("root");
	UIViewRootBase
	    preSave = null,
	    postSave = null;
	Object state = null;

	// test page with viewId and no renderKitId
	testParent.getChildren().clear();
	preSave = new UIViewRootBase();
	preSave.setViewId("viewId");
	preSave.setId("page");
	preSave.setRendererType(null); // necessary: we have no renderkit
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIViewRootBase();
	postSave.setId("newViewId");
	testParent.getChildren().add(postSave);
	try {
	    postSave.restoreState(facesContext, state);
	}
	catch (Throwable e) {
	    assertTrue(false);
	}
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test page with viewId and renderKitId
	testParent.getChildren().clear();
	preSave = new UIViewRootBase();
	preSave.setViewId("viewId");
	preSave.setId("page");
	preSave.setRenderKitId("renderKitId");
	preSave.setRendererType(null); // necessary: we have no renderkit
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIViewRootBase();
	postSave.setId("newViewId1");
	testParent.getChildren().add(postSave);
	try {
	    postSave.restoreState(facesContext, state);
	}
	catch (Throwable e) {
	    assertTrue(false);
	}
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

    }

    // -------------------------------------------------------- Support Methods

    boolean propertiesAreEqual(FacesContext context,
			       UIComponent comp1,
			       UIComponent comp2) {
	if (super.propertiesAreEqual(context, comp1, comp2)) {
	    UIViewRootBase 
		page1 = (UIViewRootBase) comp1,
		page2 = (UIViewRootBase) comp2;
	    // if their not both null, or not the same string
	    if (!TestUtil.equalsWithNulls(page1.getViewId(),
					  page2.getViewId())) {
		return false;
	    }
	    // if their not both null, or not the same string
	    if (!TestUtil.equalsWithNulls(page1.getRenderKitId(),
					  page2.getRenderKitId())) {
		return false;
	    }
	}
	return true;
    }

}
