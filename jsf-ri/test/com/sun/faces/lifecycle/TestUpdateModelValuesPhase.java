/*
 * $Id: TestUpdateModelValuesPhase.java,v 1.5 2002/06/18 05:02:27 rkitain Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestUpdateModelValuesPhase.java

package com.sun.faces.lifecycle;

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
import javax.faces.component.UITextEntry;
import javax.faces.tree.Tree;

import com.sun.faces.FacesContextTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.tree.XmlTreeImpl;

import java.io.IOException;

import java.util.Iterator;

/**
 *
 *  <B>TestUpdateModelValuesPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUpdateModelValuesPhase.java,v 1.5 2002/06/18 05:02:27 rkitain Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestUpdateModelValuesPhase extends FacesContextTestCase
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
    UITextEntry userName = null;
    UITextEntry userName1 = null;
    UITextEntry userName2 = null;
    Tree tree = null;
    TestBean testBean = new TestBean();
    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);
    String value = null;
    Phase 
	updateModelValues = new UpdateModelValuesPhase(null, 
				       Lifecycle.UPDATE_MODEL_VALUES_PHASE);
form = new UIForm();
form.setComponentId("form");
    userName = new UITextEntry();
    userName.setComponentId("userName");
    userName.setValue("one");
    userName.setModelReference("${TestBean.one}");
form.addChild(userName);
userName1 = new UITextEntry();
userName1.setComponentId("userName1");
userName1.setValue("one");
userName1.setModelReference("${TestBean.one}");
form.addChild(userName1);
userName2 = new UITextEntry();
userName2.setComponentId("userName2");
userName2.setValue("one");
userName2.setModelReference("${TestBean.one}");
form.addChild(userName2);

//    tree = new XmlTreeImpl(config.getServletContext(), userName, 
//			   "updateModel.xul", "");
    tree = new XmlTreeImpl(config.getServletContext(), form, 
			   "updateModel.xul", "");
    facesContext.setRequestTree(tree);

    rc = updateModelValues.execute(facesContext);
    assertTrue(Phase.GOTO_NEXT == rc);    

    assertTrue(null == userName.getValue());

    assertTrue(testBean.getOne().equals("one"));
    assertTrue(0 == facesContext.getMessageList().size());
}

public void testUpdateFailed()
{
    int rc = Phase.GOTO_NEXT;
UIForm form = null;
    UITextEntry userName = null;
UITextEntry userName1 = null;
UITextEntry userName2 = null;
    Tree tree = null;
    String value = null;
    Phase 
	updateModelValues = new UpdateModelValuesPhase(null, 
				       Lifecycle.UPDATE_MODEL_VALUES_PHASE);
form = new UIForm();
form.setComponentId("form");
    userName = new UITextEntry();
    userName.setComponentId("userName");
    userName.setValue("one");
    userName.setModelReference("${UserBean.one}");
form.addChild(userName);
userName1 = new UITextEntry();
userName1.setComponentId("userName1");
userName1.setValue("one");
userName1.setModelReference("${TestBean.one}");
form.addChild(userName1);
userName2 = new UITextEntry();
userName2.setComponentId("userName2");
userName2.setValue("one");
userName2.setModelReference("${TestBean.one}");
form.addChild(userName2);

//    tree = new XmlTreeImpl(config.getServletContext(), userName, 
//			   "updateModel.xul", "");
    tree = new XmlTreeImpl(config.getServletContext(), form,
                           "updateModel.xul", "");
    facesContext.setRequestTree(tree);

    // This stage will go to render, since there was at least one error
    // during component updates... 
    rc = updateModelValues.execute(facesContext);
    assertTrue(Phase.GOTO_RENDER == rc);    

    assertTrue(null != userName.getValue());
    // PENDING (visvan) only validation and conversion erros should be put
    // in messageList. remove this after confirming with Ed/Craig.
     assertTrue(1 == facesContext.getMessageList().size());
    
}

} // end of class TestUpdateModelValuesPhase
