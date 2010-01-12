/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// TestProcessValidationsPhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.cactus.ServletFacesTestCase;
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
