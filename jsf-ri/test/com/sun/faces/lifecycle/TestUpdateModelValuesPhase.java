/*
 * $Id: TestUpdateModelValuesPhase.java,v 1.24 2003/08/22 16:51:47 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
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
import javax.faces.lifecycle.Lifecycle;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPage;
import javax.faces.component.base.UIFormBase;
import javax.faces.component.base.UIPageBase;
import javax.faces.component.base.UIInputBase;

import com.sun.faces.lifecycle.Phase;
import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.TestBean;
import com.sun.faces.util.Util;

import java.io.IOException;

import java.util.Iterator;

import com.sun.faces.util.DebugUtil;

/**
 *
 *  <B>TestUpdateModelValuesPhase</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUpdateModelValuesPhase.java,v 1.24 2003/08/22 16:51:47 eburns Exp $
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
//DebugUtil.waitForDebugger();
    UIForm form = null;
    TestUIInput userName = null;
    TestUIInput userName1 = null;
    TestUIInput userName2 = null;    
    TestBean testBean = (TestBean)
	(getFacesContext().getExternalContext().getSessionMap()).get("TestBean");
    String value = null;
    Phase updateModelValues = new UpdateModelValuesPhase();
    form = new UIFormBase();
    form.setId("form");
    userName = new TestUIInput();
    userName.setId("userName");
    userName.setValue("one");
    userName.setValueRef("TestBean.one");
    userName.testSetValid(true);
    form.getChildren().add(userName);
    userName1 = new TestUIInput();
    userName1.setId("userName1");
    userName1.setValue("one");
    userName1.setValueRef("TestBean.one");
    userName1.testSetValid(true);
    form.getChildren().add(userName1);
    userName2 = new TestUIInput();
    userName2.setId("userName2");
    userName2.setValue("one");
    userName2.setValueRef("TestBean.one");
    userName2.testSetValid(true);
    form.getChildren().add(userName2);

    UIPage page = new UIPageBase();
    page.setViewId("updateModel.xul");
    getFacesContext().setViewRoot(page);
    
    updateModelValues.execute(getFacesContext());
    assertTrue(!(getFacesContext().getRenderResponse()) &&
        !(getFacesContext().getResponseComplete()));

    assertTrue(null == userName.getValue());



    assertTrue(testBean.getOne().equals("one"));
    assertTrue(false == (getFacesContext().getMessages().hasNext()));
}

public void testUpdateFailed()
{
    UIForm form = null;
    TestUIInput userName = null;
    TestUIInput userName1 = null;
    TestUIInput userName2 = null;    
    String value = null;
    Phase 
	updateModelValues = new UpdateModelValuesPhase();
    form = new UIFormBase();
    form.setId("form");
    userName = new TestUIInput();
    userName.setId("userName");
    userName.setValue("one");
    userName.testSetValid(true);
    userName.setValueRef("TestBean.two");
    form.getChildren().add(userName);
    userName1 = new TestUIInput();
    userName1.setId("userName1");
    userName1.setValue("one");
    userName1.testSetValid(true);
    userName1.setValueRef("TestBean.one");
    form.getChildren().add(userName1);
    userName2 = new TestUIInput();
    userName2.setId("userName2");
    userName2.setValue("one");
    userName2.setValueRef("TestBean.one");
    userName2.testSetValid(true);
    form.getChildren().add(userName2);

    UIPage page = new UIPageBase();
    page.setViewId("updateModel.xul");
    getFacesContext().setViewRoot(page);

    // This stage will go to render, since there was at least one error
    // during component updates... 
    updateModelValues.execute(getFacesContext());
    assertTrue(getFacesContext().getRenderResponse());

    assertTrue(true == (getFacesContext().getMessages().hasNext()));
    
}

public static class TestUIInput extends UIInputBase {

    public void testSetValid(boolean validState) {
	this.setValid(validState);
    }

}

} // end of class TestUpdateModelValuesPhase
