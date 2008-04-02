/*
 * $Id: UIPageBaseTestCase.java,v 1.2 2003/07/29 16:38:05 eburns Exp $
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
import javax.faces.component.UIPage;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.faces.mock.MockExternalContext;
import javax.faces.mock.MockFacesContext;
import javax.faces.mock.MockHttpServletRequest;
import javax.faces.mock.MockHttpServletResponse;
import javax.faces.mock.MockLifecycle;
import javax.faces.mock.MockServletContext;

/**
 * <p>Test case for the <strong>javax.faces.base.UIPageBase</strong>
 * concrete class.</p>
 */

public class UIPageBaseTestCase extends UIComponentBaseTestCase {


    // ----------------------------------------------------- Instance Variables

    protected FacesContext context = null;

    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UIPageBaseTestCase(String name) {
        super(name);
    }


    // -------------------------------------------------- Overall Test Methods

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {

        return (new TestSuite(UIPageBaseTestCase.class));

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
	UIPageBase
	    preSave = null,
	    postSave = null;
	Object state = null;

	// test page with treeId and no renderKitId
	testParent.getChildren().clear();
	preSave = new UIPageBase();
	preSave.setTreeId("treeId");
	preSave.setId("page");
	preSave.setRendererType(null); // necessary: we have no renderkit
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIPageBase();
	postSave.setId("newTreeId");
	testParent.getChildren().add(postSave);
	try {
	    postSave.restoreState(facesContext, state);
	}
	catch (Throwable e) {
	    assertTrue(false);
	}
	assertTrue(propertiesAreEqual(facesContext, preSave, postSave));

	// test page with treeId and renderKitId
	testParent.getChildren().clear();
	preSave = new UIPageBase();
	preSave.setTreeId("treeId");
	preSave.setId("page");
	preSave.setRenderKitId("renderKitId");
	preSave.setRendererType(null); // necessary: we have no renderkit
	testParent.getChildren().add(preSave);
	state = preSave.getState(facesContext);
	assertTrue(null != state);
	testParent.getChildren().clear();
	
	postSave = new UIPageBase();
	postSave.setId("newTreeId1");
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

    protected boolean propertiesAreEqual(FacesContext context,
					 UIComponent comp1,
					 UIComponent comp2) {
	if (super.propertiesAreEqual(context, comp1, comp2)) {
	    UIPageBase 
		page1 = (UIPageBase) comp1,
		page2 = (UIPageBase) comp2;
	    // if their not both null, or not the same string
	    if (!((null == page1.getTreeId() && 
		   null == page2.getTreeId()) ||
		(page1.getTreeId().equals(page2.getTreeId())))) {
		return false;
	    }
	    // if their not both null, or not the same string
	    if (!((null == page1.getRenderKitId() && 
		   null == page2.getRenderKitId()) ||
		(page1.getRenderKitId().equals(page2.getRenderKitId())))) {
		return false;
	    }
	}
	return true;
    }

}
