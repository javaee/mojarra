/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package com.sun.faces.systest;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.faces.component.NamingContainer;

/**
 * <p>Test Case for JSP Interoperability.</p>
 */

public class CSRFTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public CSRFTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        super.setUp();
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(CSRFTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // These tests consist of a "good" page and a "bad" page.
    // The "good" page is one that a typical user uses when
    // running their application.  The "bad" page is one that
    // a hacker puts together to perform CSRF against the 
    // "good" application using the "good" page.  Typically,
    // the "bad" page will have a form that posts back to
    // the "good" page using the necessary request parameters
    // to perform the post (with modified parameter values
    // to manipulate sensitive information).

    // This test attempts to submit from the "bad" page,
    // causing a post to the "good" page.  In this case,
    // the attacker has not supplied the correct request
    // parameter values (namely the javax.faces.ViewState value),
    // so it's expected that JSF will throw the 
    // "ViewExpiredException".

    public void testCSRFBadViewState() throws Exception {

        // User executes the "bad" page ..

        HtmlPage page = getPage("/faces/csrf/badPage.html");
        HtmlForm form = getFormById(page, "form");

        HtmlHiddenInput badamount = (HtmlHiddenInput)
            form.getInputByName("amount");
        badamount.setValueAttribute("100");
        HtmlHiddenInput badaccount = (HtmlHiddenInput)
            form.getInputByName("account");
        badaccount.setValueAttribute("37665");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            form.getInputByName("order");
        try {
            page = (HtmlPage) submit.click();
        } catch (FailingHttpStatusCodeException fhsce) {
            String response = fhsce.getResponse().getContentAsString();
            assertTrue(response.indexOf("Token verification failed") != -1);
        }
    }

    // This test attempts to submit from the "bad" page,
    // causing a post to the "good" page.  In this case,
    // the attacker has supplied the correct request
    // parameter values (namely the javax.faces.ViewState value).
    // However, a token was not specified,
    // so it's expected that JSF will throw a "token verification"
    // exception.

    public void testCSRFGoodViewState() throws Exception {

        HtmlPage page1 = getPage("/faces/csrf/goodPage.xhtml");
        HtmlForm form1 = getFormById(page1, "form");
        HtmlHiddenInput vs= (HtmlHiddenInput)
            form1.getInputByName("javax.faces.ViewState");
        System.out.println("VIEWSTATE:"+vs.getValueAttribute());

        // User executes the "bad" page ..

        HtmlPage page = getPage("/faces/csrf/badPage1.html");
        HtmlForm form = getFormById(page, "form");

        HtmlHiddenInput badamount = (HtmlHiddenInput)
            form.getInputByName("amount");
        badamount.setValueAttribute("100");
        HtmlHiddenInput badaccount = (HtmlHiddenInput)
            form.getInputByName("account");
        badaccount.setValueAttribute("37665");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            form.getInputByName("order");
        try {
            page = (HtmlPage) submit.click();
        } catch (FailingHttpStatusCodeException fhsce) {
            String response = fhsce.getResponse().getContentAsString();
            assertTrue(response.indexOf("Token verification failed") != -1);
        }
    }

    public void testBadTokenAjax() throws Exception {
        HtmlPage page1 = getPage("/faces/csrf/goodPage.xhtml");
        HtmlForm form1 = getFormById(page1, "form");
        HtmlHiddenInput vs= (HtmlHiddenInput)
            form1.getInputByName("javax.faces.ViewState");
        System.out.println("VIEWSTATE:"+vs.getValueAttribute());

        // User executes the "bad" page ..

        HtmlPage page = getPage("/faces/csrf/badPage2.html");
        HtmlForm form = getFormById(page, "form");

        HtmlHiddenInput badamount = (HtmlHiddenInput)
            form.getInputByName("amount");
        badamount.setValueAttribute("100");
        HtmlHiddenInput badaccount = (HtmlHiddenInput)
            form.getInputByName("account");
        badaccount.setValueAttribute("37665");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            form.getInputByName("order");
        try {
            page = (HtmlPage) submit.click();
        } catch (FailingHttpStatusCodeException fhsce) {
            String response = fhsce.getResponse().getContentAsString();
            assertTrue(response.indexOf("Token verification failed") != -1);
        }
    }

}
