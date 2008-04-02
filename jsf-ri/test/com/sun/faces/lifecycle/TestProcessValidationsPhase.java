/*
 * $Id: TestProcessValidationsPhase.java,v 1.33 2005/03/15 20:37:40 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestProcessValidationsPhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.ServletFacesTestCase;
import org.apache.cactus.WebRequest;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;

import java.util.Iterator;

/**
 * <B>TestProcessValidationsPhase</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestProcessValidationsPhase.java,v 1.33 2005/03/15 20:37:40 edburns Exp $
 */

public class TestProcessValidationsPhase extends ServletFacesTestCase {

//
// Protected Constants
//

    public static final String TEST_URI = "/components.jsp";

    public static final String DID_VALIDATE = "didValidate";
    public static UIInput userName = null;

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

    public TestProcessValidationsPhase() {
        super("TestProcessValidationsPhase");
    }


    public TestProcessValidationsPhase(String name) {
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
        theRequest.addParameter("basicForm", "basicForm");
    }


    public void testCallback() {
        UIComponent root = null;
        userName = null;
        String value = null;
        Phase
            applyValues = new ApplyRequestValuesPhase(),
            processValidations = new ProcessValidationsPhase();

	root = getFacesContext().getApplication().getViewHandler().createView(getFacesContext(), TEST_URI);
	getFacesContext().setViewRoot((UIViewRoot) root);
	getFacesContext().renderResponse();

        assertTrue((getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));
        assertTrue(null != getFacesContext().getViewRoot());

        root = getFacesContext().getViewRoot();
        UIForm basicForm = new UIForm();
        basicForm.setId("basicForm");
        UIInput userName1 = new UIInput();
        userName1.setId("userName");
        root.getChildren().add(basicForm);
        basicForm.getChildren().add(userName1);

        // clear the property
        System.setProperty(DID_VALIDATE, EMPTY);

        try {
            userName =
                (UIInput) root.findComponent(
                    "basicForm" + NamingContainer.SEPARATOR_CHAR + "userName");
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            assertTrue("Can't find userName in tree", false);
        }

        // add the validator
        Validator validator = new Validator() {
            public Iterator getAttributeNames() {
                return null;
            }


            public void validate(FacesContext context, UIComponent component, Object value) {
                assertTrue(component == userName);
                System.setProperty(DID_VALIDATE, DID_VALIDATE);
                return;
            }
        };
        userName.addValidator(validator);

        assertTrue(userName.isValid());

        applyValues.execute(getFacesContext());
        assertTrue((getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        processValidations.execute(getFacesContext());
        assertTrue(!System.getProperty(DID_VALIDATE).equals(EMPTY));
        assertTrue(userName.isValid());
        assertTrue(null == userName.getSubmittedValue());
        assertTrue("jerry".equals(userName.getValue()));
        System.setProperty(DID_VALIDATE, EMPTY);
    }

} // end of class TestProcessValidationsPhase
