/*
 * $Id: TestUpdateModelValuesPhase.java,v 1.2 2002/06/07 00:01:14 eburns Exp $
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
 * @version $Id: TestUpdateModelValuesPhase.java,v 1.2 2002/06/07 00:01:14 eburns Exp $
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
    UITextEntry userName = null;
    Tree tree = null;
    TestBean testBean = new TestBean();
    (facesContext.getHttpSession()).setAttribute("TestBean", testBean);
    String value = null;
    Phase 
	updateModelValues = new UpdateModelValuesPhase(null, 
				       Lifecycle.UPDATE_MODEL_VALUES_PHASE);
    userName = new UITextEntry();
    userName.setComponentId("userName");
    userName.setValue("one");
    userName.setModel("${TestBean.one}");
    tree = new XmlTreeImpl(config.getServletContext(), userName, 
			   "updateModel.xul");
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
    UITextEntry userName = null;
    Tree tree = null;
    String value = null;
    Phase 
	updateModelValues = new UpdateModelValuesPhase(null, 
				       Lifecycle.UPDATE_MODEL_VALUES_PHASE);
    userName = new UITextEntry();
    userName.setComponentId("userName");
    userName.setValue("one");
    userName.setModel("${UserBean.one}");
    tree = new XmlTreeImpl(config.getServletContext(), userName, 
			   "updateModel.xul");
    facesContext.setRequestTree(tree);

    // This stage will go straight to render, since there is no model
    // called UserBean.
    rc = updateModelValues.execute(facesContext);
    assertTrue(Phase.GOTO_RENDER == rc);    

    assertTrue(null != userName.getValue());

    assertTrue(1 == facesContext.getMessageList().size());
    
}

} // end of class TestUpdateModelValuesPhase
