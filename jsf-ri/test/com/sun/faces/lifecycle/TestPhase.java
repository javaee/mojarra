/*
 * $Id: TestPhase.java,v 1.15 2004/04/07 17:52:53 rkitain Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestPhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.ServletFacesTestCase;
import com.sun.faces.util.Util;
import org.apache.cactus.WebRequest;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;


/**
 * <B>TestPhase</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestPhase.java,v 1.15 2004/04/07 17:52:53 rkitain Exp $
 */

public class TestPhase extends ServletFacesTestCase {

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

    public TestPhase() {
        super("TestPhase");
    }


    public TestPhase(String name) {
        super(name);
    }

//
// Class methods
//

//
// General Methods
//

    public void beginExecute(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
        theRequest.addParameter(
            "basicForm" + NamingContainer.SEPARATOR_CHAR + "userName", "jerry");
    }


    public void testExecute() {

        Phase restoreView = new RestoreViewPhase();
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
        UIComponent root = getFacesContext().getViewRoot();
        UIForm basicForm = new UIForm();
        basicForm.setId("basicForm");
        UIInput userName = new UIInput();
        userName.setId("userName");
        root.getChildren().add(basicForm);
        basicForm.getChildren().add(userName);

        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        page.getChildren().add(basicForm);
        page.setViewId("root");
        getFacesContext().setViewRoot(page);

        Phase applyValues = new ApplyRequestValuesPhase();

        try {
            applyValues.execute(getFacesContext());
        } catch (Throwable e) {
            System.out.println("Throwable: " + e.getMessage());
            e.printStackTrace();
            assertTrue(false);
        }
        assertTrue((getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));
    }

} // end of class TestPhase
