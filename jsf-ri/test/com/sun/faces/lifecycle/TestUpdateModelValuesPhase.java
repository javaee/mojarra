/*
 * $Id: TestUpdateModelValuesPhase.java,v 1.44 2007/02/27 23:10:18 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

// TestUpdateModelValuesPhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.cactus.ServletFacesTestCase;
import com.sun.faces.el.ELUtils;
import com.sun.faces.util.Util;

import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import java.util.Locale;

/**
 * <B>TestUpdateModelValuesPhase</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestUpdateModelValuesPhase.java,v 1.44 2007/02/27 23:10:18 rlubke Exp $
 */

public class TestUpdateModelValuesPhase extends ServletFacesTestCase {

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

    public void testUpdateNormal() {
//DebugUtil.waitForDebugger();
        UIForm form = null;
        TestUIInput userName = null;
        TestUIInput userName1 = null;
        TestUIInput userName2 = null;
        com.sun.faces.cactus.TestBean testBean = (com.sun.faces.cactus.TestBean)
            (getFacesContext().getExternalContext().getSessionMap()).get(
                "TestBean");
        String value = null;
        Phase updateModelValues = new UpdateModelValuesPhase();
        form = new UIForm();
        form.setId("form");
        form.setSubmitted(true);
        userName = new TestUIInput();
        userName.setId("userName");
        userName.setValue("one");
        userName.setValueExpression("value",
                                 ELUtils.getValueExpression("#{TestBean.one}"));
        userName.testSetValid(true);
        form.getChildren().add(userName);
        userName1 = new TestUIInput();
        userName1.setId("userName1");
        userName1.setValue("one");
        userName1.setValueExpression("value",
                                  ELUtils.getValueExpression("#{TestBean.one}"));
        userName1.testSetValid(true);
        form.getChildren().add(userName1);
        userName2 = new TestUIInput();
        userName2.setId("userName2");
        userName2.setValue("one");
        userName2.setValueExpression("value",
                                  ELUtils.getValueExpression("#{TestBean.one}"));
        userName2.testSetValid(true);
        form.getChildren().add(userName2);

        UIViewRoot viewRoot = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        viewRoot.setLocale(Locale.US);
        viewRoot.getChildren().add(form);
        viewRoot.setViewId("updateModel.xul");
        getFacesContext().setViewRoot(viewRoot);

        try {
            updateModelValues.execute(getFacesContext());
        } catch (Throwable e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue(!(getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));
        assertTrue(null == userName.getLocalValue());

        assertTrue(testBean.getOne().equals("one"));
        assertTrue(false == (getFacesContext().getMessages().hasNext()));
    }


    public void testUpdateFailed() {
        UIForm form = null;
        TestUIInput userName = null;
        TestUIInput userName1 = null;
        TestUIInput userName2 = null;
        TestUIInput userName3 = null;
        String value = null;
        Phase
            updateModelValues = new UpdateModelValuesPhase();
        form = new UIForm();
        form.setId("form");
        form.setSubmitted(true);
        userName = new TestUIInput();
        userName.setId("userName");
        userName.setValue("one");
        userName.testSetValid(true);
        userName.setValueExpression("value",
                                 ELUtils.getValueExpression("#{TestBean.two}"));
        form.getChildren().add(userName);
        userName1 = new TestUIInput();
        userName1.setId("userName1");
        userName1.setValue("one");
        userName1.testSetValid(true);
        userName1.setValueExpression("value",
                                  ELUtils.getValueExpression("#{TestBean.one}"));
        form.getChildren().add(userName1);
        userName2 = new TestUIInput();
        userName2.setId("userName2");
        userName2.setValue("one");
        userName2.setValueExpression("value",
                                  ELUtils.getValueExpression("#{TestBean.one}"));
        userName2.testSetValid(true);
        form.getChildren().add(userName2);
        userName3 = new TestUIInput();
        userName3.setId("userName3");
        userName3.setValue("four");
        userName3.setValueExpression("value",
                                  ELUtils.getValueExpression("#{TestBean.four}"));
        userName3.testSetValid(true);
        form.getChildren().add(userName3);

        UIViewRoot viewRoot = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        viewRoot.setLocale(Locale.US);
        viewRoot.getChildren().add(form);
        viewRoot.setViewId("updateModel.xul");
        getFacesContext().setViewRoot(viewRoot);

        // This stage will go to render, since there was at least one error
        // during component updates...
        try {
            updateModelValues.execute(getFacesContext());
        } catch (Throwable e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(getFacesContext().getRenderResponse());

        assertTrue(true == (getFacesContext().getMessages().hasNext()));

        //assertions for our default update failed message
        assertTrue(true == (getFacesContext().getMessages("form:userName3").hasNext()));
        java.util.Iterator iter = getFacesContext().getMessages("form:userName3");
        javax.faces.application.FacesMessage msg = null;
        javax.faces.application.FacesMessage expectedMsg = 
            com.sun.faces.util.MessageFactory.getMessage(getFacesContext(), "javax.faces.component.UIInput.UPDATE",
            new Object[] {com.sun.faces.util.MessageFactory.getLabel(getFacesContext(), userName3)}); 
        while (iter.hasNext()) {
            msg = (javax.faces.application.FacesMessage)iter.next();
        }    
        assertTrue(msg.getSummary().equals(expectedMsg.getSummary()));
    }


    public static class TestUIInput extends UIInput {

        public void testSetValid(boolean validState) {
            this.setValid(validState);
        }

    }

} // end of class TestUpdateModelValuesPhase
