/*
 * $Id: TestCoreTagsVBEnabled.java,v 1.10 2006/03/29 22:39:48 rlubke Exp $
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

package com.sun.faces.taglib.jsf_core;

import java.util.Iterator;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;

import com.sun.faces.cactus.JspFacesTestCase;
import com.sun.faces.lifecycle.ApplyRequestValuesPhase;
import com.sun.faces.lifecycle.Phase;
import com.sun.faces.lifecycle.ProcessValidationsPhase;
import com.sun.faces.lifecycle.RenderResponsePhase;
import com.sun.faces.util.Util;

import org.apache.cactus.WebRequest;

/**
 * <B>TestValidatorTags</B> is a class ...
 * <p/>
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestCoreTagsVBEnabled.java,v 1.10 2006/03/29 22:39:48 rlubke Exp $
 */

public class TestCoreTagsVBEnabled extends JspFacesTestCase {


    public static final String DOUBLERANGE_ID = "validatorForm" +
        NamingContainer.SEPARATOR_CHAR +
        "doubleRange";
    public static final String DOUBLERANGE_VALUE = "1500";
    public static final String INTRANGE_ID = "validatorForm" +
        NamingContainer.SEPARATOR_CHAR +
        "intRange";
    public static final String INTRANGE_VALUE = "NorthAmerica";
    public static final String LONGRANGE_ID = "validatorForm" +
        NamingContainer.SEPARATOR_CHAR +
        "longRange";
    public static final String LONGRANGE_VALUE = "115";
    public static final String TEST_URI = "/TestCoreTagVBEnabled.jsp";


    // ------------------------------------------------------------ Constructors


    public TestCoreTagsVBEnabled() {

        super("TestValidatorTags");

    }


    public TestCoreTagsVBEnabled(String name) {

        super(name);

    }


    // ---------------------------------------------- Methods From FacesTestCase


    public boolean sendResponseToFile() {

        return false;

    }


    // ---------------------------------------------------------- Public Methods


    public void beginValidators(WebRequest theRequest) {

        theRequest.setURL("localhost:8080", "/test", "/faces", TEST_URI, null);
        theRequest.addParameter(LONGRANGE_ID, LONGRANGE_VALUE);
        theRequest.addParameter(INTRANGE_ID, INTRANGE_VALUE);
        theRequest.addParameter(DOUBLERANGE_ID, DOUBLERANGE_VALUE);
        theRequest.addParameter("validatorForm", "validatorForm");

    }


    public void setUp() {

        Util.setUnitTestModeEnabled(true);
        super.setUp();
        (getFacesContext().getExternalContext().getRequestMap()).put("intMin",
                                                                     new Integer(
                                                                         1));
        (getFacesContext().getExternalContext().getRequestMap()).put("intMax",
                                                                     new Integer(
                                                                         10));
        (getFacesContext().getExternalContext().getRequestMap()).put("longMin",
                                                                     new Long(
                                                                         100));
        (getFacesContext().getExternalContext().getRequestMap()).put("longMax",
                                                                     new Long(
                                                                         110));
        (getFacesContext().getExternalContext().getRequestMap()).put(
            "doubleMin", new Double(1.0));
        (getFacesContext().getExternalContext().getRequestMap()).put(
            "doubleMax", new Double(10.0));

    }


    public void testValidators() {

        System.out.println("Test VBEnabled attributes on core Validator tags");
        // Verify the parmeters are as expected
        String paramVal = (String) (getFacesContext().getExternalContext()
            .getRequestParameterMap()).get(LONGRANGE_ID);
        assertTrue(LONGRANGE_VALUE.equals(paramVal));

        String paramVal1 = (String) (getFacesContext().getExternalContext()
            .getRequestParameterMap()).get(DOUBLERANGE_ID);
        assertTrue(DOUBLERANGE_VALUE.equals(paramVal1));

        String paramVal2 = (String) (getFacesContext().getExternalContext()
            .getRequestParameterMap()).get(INTRANGE_ID);
        assertTrue(INTRANGE_VALUE.equals(paramVal2));

        boolean result = false;
        String value = null;
        Phase
            renderResponse = new RenderResponsePhase(),
            processValidations = new ProcessValidationsPhase(),
            applyRequestValues = new ApplyRequestValuesPhase();

        UIViewRoot page = Util.getViewHandler(getFacesContext()).createView(getFacesContext(), null);
        page.setViewId(TEST_URI);
        getFacesContext().setViewRoot(page);

        // This builds the tree, and usefaces saves it in the session
        renderResponse.execute(getFacesContext());
        assertTrue(!(getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        // This causes the components to be set to valid
        applyRequestValues.execute(getFacesContext());
        assertTrue(!(getFacesContext().getRenderResponse()) &&
                   !(getFacesContext().getResponseComplete()));

        // process the validations
        processValidations.execute(getFacesContext());
        // We know there are validation errors on the page
        assertTrue(getFacesContext().getRenderResponse());

        System.out.println("Verifying results...");
        // verify the messages have been added correctly.
        UIComponent comp = null;
        Iterator messages = null;

        assertTrue(null != (messages = getFacesContext().getMessages()));
        assertTrue(messages.hasNext());

        // check the messages for each component in the page
        assertTrue(null !=
                   (comp =
                    getFacesContext().getViewRoot().findComponent(LONGRANGE_ID)));
        assertTrue(
            null !=
            (messages =
             getFacesContext().getMessages(comp.getClientId(getFacesContext()))));
        assertTrue(messages.hasNext());

        assertTrue(null !=
                   (comp =
                    getFacesContext().getViewRoot().findComponent(INTRANGE_ID)));
        assertTrue(
            null !=
            (messages =
             getFacesContext().getMessages(comp.getClientId(getFacesContext()))));
        assertTrue(messages.hasNext());

        assertTrue(null !=
                   (comp =
                    getFacesContext().getViewRoot().findComponent(
                        DOUBLERANGE_ID)));
        assertTrue(
            null !=
            (messages =
             getFacesContext().getMessages(comp.getClientId(getFacesContext()))));
        assertTrue(messages.hasNext());


        Util.setUnitTestModeEnabled(false);

    }

} // end of class TestValidatorTags
