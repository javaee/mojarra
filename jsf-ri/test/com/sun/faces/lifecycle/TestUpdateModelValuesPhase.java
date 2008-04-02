/*
 * $Id: TestUpdateModelValuesPhase.java,v 1.17 2003/02/20 22:49:57 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestUpdateModelValuesPhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.context.FacesContextImpl;

import org.apache.cactus.WebRequest;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.tree.Tree;
import com.sun.faces.RIConstants;
import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.tree.SimpleTreeImpl;

import java.io.IOException;

import java.util.Iterator;

/**
 *
 *  <B>TestUpdateModelValuesPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUpdateModelValuesPhase.java,v 1.17 2003/02/20 22:49:57 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestUpdateModelValuesPhase extends ServletFacesTestCase
{
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestUpdateModelValuesPhase() {
	super("TestUpdateModelValuesPhase");
    }

    public TestUpdateModelValuesPhase(String name) {
	super(name);
    }

//
// Class methods
//

//
// General Methods
//

public void testUpdateNormal()
{
    int rc = Phase.GOTO_NEXT;
    UIForm form = null;
    TestUIInput userName = null;
    TestUIInput userName1 = null;
    TestUIInput userName2 = null;
    Tree tree = null;
    TestBean testBean = (TestBean)
	(getFacesContext().getHttpSession()).getAttribute("TestBean");
    String value = null;
    Phase 
	updateModelValues = new UpdateModelValuesPhase(null, 
				       RIConstants.UPDATE_MODEL_VALUES_PHASE);
    form = new UIForm();
    form.setComponentId("form");
    form.setValid(true);
    userName = new TestUIInput();
    userName.setComponentId("userName");
    userName.setValue("one");
    userName.setModelReference("${TestBean.one}");
    userName.testSetValid(true);
    form.addChild(userName);
    userName1 = new TestUIInput();
    userName1.setComponentId("userName1");
    userName1.setValue("one");
    userName1.setModelReference("${TestBean.one}");
    userName1.testSetValid(true);
    form.addChild(userName1);
    userName2 = new TestUIInput();
    userName2.setComponentId("userName2");
    userName2.setValue("one");
    userName2.setModelReference("${TestBean.one}");
    userName2.testSetValid(true);
    form.addChild(userName2);

    tree = new SimpleTreeImpl(getFacesContext(), form, 
			   "updateModel.xul");
    getFacesContext().setTree(tree);
    rc = updateModelValues.execute(getFacesContext());
    assertTrue(Phase.GOTO_NEXT == rc);    

    assertTrue(null == userName.getValue());

    assertTrue(testBean.getOne().equals("one"));
    assertTrue(false == (getFacesContext().getMessages().hasNext()));
}

public void testUpdateFailed()
{
    int rc = Phase.GOTO_NEXT;
    UIForm form = null;
    TestUIInput userName = null;
    TestUIInput userName1 = null;
    TestUIInput userName2 = null;
    Tree tree = null;
    String value = null;
    Phase 
	updateModelValues = new UpdateModelValuesPhase(null, 
				       RIConstants.UPDATE_MODEL_VALUES_PHASE);
    form = new UIForm();
    form.setComponentId("form");
    userName = new TestUIInput();
    userName.setComponentId("userName");
    userName.setValue("one");
    userName.testSetValid(true);
    userName.setModelReference("${UserBean.one}");
    form.addChild(userName);
    userName1 = new TestUIInput();
    userName1.setComponentId("userName1");
    userName1.setValue("one");
    userName1.testSetValid(true);
    userName1.setModelReference("${TestBean.one}");
    form.addChild(userName1);
    userName2 = new TestUIInput();
    userName2.setComponentId("userName2");
    userName2.setValue("one");
    userName2.setModelReference("${TestBean.one}");
    userName2.testSetValid(true);
    form.addChild(userName2);

    tree = new SimpleTreeImpl(getFacesContext(), form,
                           "updateModel.xul");
    getFacesContext().setTree(tree);

    // This stage will go to render, since there was at least one error
    // during component updates... 
    rc = updateModelValues.execute(getFacesContext());
    assertTrue(Phase.GOTO_RENDER == rc);    

    assertTrue(null != userName.getValue());
    assertTrue(true == (getFacesContext().getMessages().hasNext()));
    
}

public static class TestUIInput extends UIInput {

    public void testSetValid(boolean validState) {
	this.setValid(validState);
    }

}

} // end of class TestUpdateModelValuesPhase
