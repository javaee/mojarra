/*
 * $Id: MethodRefTestCase.java,v 1.13 2006/03/29 22:38:48 rlubke Exp $
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

package com.sun.faces.methodref;


import javax.faces.component.NamingContainer;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/** <p>Test Case for JSP Interoperability.</p> */

public class MethodRefTestCase extends AbstractTestCase {

    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public MethodRefTestCase(String name) {

        super(name);

    }

    // ---------------------------------------------------------- Public Methods


    /** Return the tests included in this test suite. */
    public static Test suite() {

        return (new TestSuite(MethodRefTestCase.class));

    }


    /** Set up instance variables required by this test case. */
    public void setUp() throws Exception {

        super.setUp();

    }


    /** Tear down instance variables required by this test case. */
    public void tearDown() {

        super.tearDown();

    }

    // ------------------------------------------------- Individual Test Methods

    public void testActionAndActionListener() throws Exception {

        HtmlForm form;
        HtmlSubmitInput submit;
        HtmlAnchor link;
        HtmlTextInput input;
        HtmlPage page;

        page = getPage("/faces/methodref01.jsp");
        form = getFormById(page, "form");
        submit = (HtmlSubmitInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "button1");

        // press button1
        page = (HtmlPage) submit.click();
        form = getFormById(page, "form");
        input = (HtmlTextInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "buttonStatus");
        assertTrue("Input does not have expected value",
                   -1 != input.asText().indexOf("button1 was pressed"));
        link = (HtmlAnchor) page.getAnchors().get(0);

        // press button2
        page = (HtmlPage) link.click();
        assertNotNull(page);
        form = getFormById(page, "form");
        input = (HtmlTextInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "buttonStatus");
        assertTrue("Input does not have expected value",
                   -1 != input.asText().indexOf("button2 was pressed"));
        submit = (HtmlSubmitInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "button3");

        // press button3
        page = (HtmlPage) submit.click();
        form = getFormById(page, "form");
        input = (HtmlTextInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "buttonStatus");
        assertTrue("Input does not have expected value",
                   -1 != input.asText().indexOf("button3 was pressed"));

    }


    public void testValidatorReference() throws Exception {

        HtmlForm form;
        HtmlSubmitInput submit;
        HtmlAnchor link;
        HtmlTextInput input;
        HtmlPage page;

        page = getPage("/faces/methodref01.jsp");
        form = getFormById(page, "form");
        submit = (HtmlSubmitInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "validate");

        // press the button with no value, see that no value appears in
        // the "toValidate" textField.
        page = (HtmlPage) submit.click();
        form = getFormById(page, "form");
        input = (HtmlTextInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "toValidate");
        int fieldLen = input.asText().length();
        assertTrue("Input does not have expected value", 0 == fieldLen);

        // fill in an incorrect value, see that still no value appears
        // in the text field.
        input = (HtmlTextInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "toValidate");
        input.setValueAttribute("aoeuaoeu");
        submit = (HtmlSubmitInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "validate");
        page = (HtmlPage) submit.click();
        form = getFormById(page, "form");
        input = (HtmlTextInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "toValidate");
        fieldLen = input.asText().length();
        assertTrue("Input does not have expected value", 8 == fieldLen);

        // fill in the correct value, see that finally we have a value
        input = (HtmlTextInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "toValidate");
        input.setValueAttribute("batman");
        submit = (HtmlSubmitInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "validate");
        page = (HtmlPage) submit.click();
        form = getFormById(page, "form");
        input = (HtmlTextInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "toValidate");
        assertEquals("Input does not have expected value",
                     "batman", input.asText());

    }


    public void testValueChangeListenerByReference() throws Exception {

        HtmlForm form;
        HtmlSubmitInput submit;
        HtmlAnchor link;
        HtmlTextInput input;
        HtmlPage page;

        page = getPage("/faces/methodref01.jsp");
        form = getFormById(page, "form");
        submit = (HtmlSubmitInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "changeValue");

        // fill in a value, see we have a value
        input = (HtmlTextInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "toChange");
        input.setValueAttribute("batman");
        page = (HtmlPage) submit.click();
        form = getFormById(page, "form");
        input = (HtmlTextInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "toChange");
        assertEquals("Input does not have expected value",
                     "batman", input.getOnBlurAttribute());

    }


    /** Test case for bug 5030555 */
    public void testValueChangeListenerWithBinding() throws Exception {

        HtmlForm form;
        HtmlSubmitInput submit;
        HtmlAnchor link;
        HtmlTextInput input;
        HtmlPage page;

        page = getPage("/faces/binding01.jsp");
        form = getFormById(page, "form");
        submit = (HtmlSubmitInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "changeValue");

        // fill in a value, see we have a value
        input = (HtmlTextInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "toChange");
        input.setValueAttribute("binding works!!");
        page = (HtmlPage) submit.click();
        form = getFormById(page, "form");
        input = (HtmlTextInput)
              form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                  "toChange");
        assertEquals("Input does not have expected value",
                     "binding works!!", input.getOnBlurAttribute());

    }

}
