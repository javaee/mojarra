/*
 * $Id: TestApplyRequestValuesPhase.java,v 1.27 2004/07/27 19:36:42 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestApplyRequestValuesPhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.ServletFacesTestCase;
import org.apache.cactus.WebRequest;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UICommand;

/**
 * <B>TestApplyRequestValuesPhase</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestApplyRequestValuesPhase.java,v 1.27 2004/07/27 19:36:42 jayashri Exp $
 */

public class TestApplyRequestValuesPhase extends ServletFacesTestCase {

//
// Protected Constants
//

    public static final String TEST_URI = "/components.jsp";

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

    public TestApplyRequestValuesPhase() {
        super("TestApplyRequestValuesPhase");
    }


    public TestApplyRequestValuesPhase(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//

    public void beginCallback(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
        theRequest.addParameter(
            "basicForm" + NamingContainer.SEPARATOR_CHAR + "userName", "jerry");
         theRequest.addParameter(
            "basicForm" + NamingContainer.SEPARATOR_CHAR + "testCmd", "submit");
          theRequest.addParameter(
            "basicForm" + NamingContainer.SEPARATOR_CHAR + "testInt", "10");
        theRequest.addParameter("basicForm", "basicForm");

    }


    public void testCallback() {
        UIComponent root = null;
        String value = null;
        Phase
            restoreView = new RestoreViewPhase(),
            applyValues = new ApplyRequestValuesPhase();

        // 1. Set the root of the view ...
        //
        try {
            restoreView.execute(getFacesContext());
        } catch (Throwable e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue((getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));
        assertTrue(null != getFacesContext().getViewRoot());

        // 2. Add components to tree
        //
        root = getFacesContext().getViewRoot();
        UIForm basicForm = new UIForm();
        basicForm.setId("basicForm");
        UIInput userName = new UIInput();
        userName.setId("userName");
        root.getChildren().add(basicForm);
        basicForm.getChildren().add(userName);

        // 3. Apply values
        //
        applyValues.execute(getFacesContext());
        assertTrue((getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        root = getFacesContext().getViewRoot();
        try {
            userName = (UIInput) basicForm.findComponent("userName");
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            assertTrue("Can't find userName in tree", false);
        }
        assertTrue(null != userName);
        assertTrue(null != (value = (String) userName.getSubmittedValue()));
        assertTrue(value.equals("jerry"));
        
        testImmediate(basicForm);
    }
    
    public void testImmediate(UIForm basicForm) {
        
        Phase
            restoreView = new RestoreViewPhase(),
            applyValues = new ApplyRequestValuesPhase();

        
         // add a UICommand with "immediate" attribute set
        UICommand testCmd = new UICommand();
        testCmd.setId("testCmd");
        testCmd.setImmediate(true);
        basicForm.getChildren().add(testCmd);
        
        //verify immediate attribute works correctly.
        System.out.println("Testing 'immediate' attribute on UIInput and UICommand");
        UIInput testInt = new UIInput();
        testInt.setConverter(new javax.faces.convert.IntegerConverter());
        testInt.setRequired(true);
        testInt.setId("testInt");
        testInt.setImmediate(true);
        basicForm.getChildren().add(testInt); 
        
        // 3. Apply values
        //
        Integer testNumber = new Integer(10);
        applyValues.execute(getFacesContext());
        assertTrue((getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        UIComponent root = getFacesContext().getViewRoot();
        try {
            testInt = (UIInput) basicForm.findComponent("testInt");
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            assertTrue("Can't find testInt in tree", false);
        }
        
        //make sure the value is converted and validated after Apply request 
        // values phase.
        assertTrue(null != testInt);
        assertTrue(null != testInt.getLocalValue());
        assertTrue(testInt.isValid());
        assertTrue(testNumber.equals((Integer) testInt.getValue()));
        testInt.setValue(null);
        
        // immediate "false" on command button but set on UIInput
        testCmd.setImmediate(false);
        applyValues.execute(getFacesContext());
        assertTrue((getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        root = getFacesContext().getViewRoot();
        try {
            testInt = (UIInput) basicForm.findComponent("testInt");
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            assertTrue("Can't find testInt in tree", false);
        }
        
        //make sure the value is converted and validated after Apply request 
        // values phase.
        assertTrue(null != testInt);
        assertTrue(null != testInt.getLocalValue());
        assertTrue(testInt.isValid());
        assertTrue(testNumber.equals((Integer) testInt.getValue()));
        testInt.setValue(null);
        
        // immediate "true" on command and not set on UIInput.
        testInt.setImmediate(false);
        testCmd.setImmediate(true);
        applyValues.execute(getFacesContext());
        assertTrue((getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        root = getFacesContext().getViewRoot();
        try {
            testInt = (UIInput) basicForm.findComponent("testInt");
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            assertTrue("Can't find testInt in tree", false);
        }
        
        //make sure the value is converted and validated after Apply request 
        // values phase.
        assertTrue(null != testInt);
        assertTrue(null == testInt.getValue());
        assertTrue(testInt.isValid());
    }


} // end of class TestApplyRequestValuesPhase
