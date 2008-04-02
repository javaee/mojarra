/*
 * $Id: NavigationTestCase.java,v 1.10 2006/03/29 23:03:58 rlubke Exp $
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

package com.sun.faces.systest;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.faces.component.NamingContainer;

/**
 * <p>Test Case for JSP Interoperability.</p>
 */

public class NavigationTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public NavigationTestCase(String name) {
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
        return (new TestSuite(NavigationTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test dynamically adding and removing components
    public void testRedirect() throws Exception {
        client.setRedirectEnabled(false);
        // the navigation-case for this url is set up to cause a redirect
        HtmlPage page = getPage("/faces/redirect.jsp");
        HtmlForm form = getFormById(page, "redirect");
        assertNotNull("form exists", form);
        HtmlSubmitInput submit = (HtmlSubmitInput)
            form.getInputByName("redirect" +
                                NamingContainer.SEPARATOR_CHAR +
                                "submit");
        boolean exceptionThrown = false;
        try {
            page = (HtmlPage) submit.click();
        } catch (FailingHttpStatusCodeException fhsce) {
            assertEquals("Didn't get expected redirect",
                         fhsce.getStatusCode(), 302);
            exceptionThrown = true;
        }
        assertTrue("Didn't get expected redirect", exceptionThrown);
    }


    public void testNavigateWithVerbatim() throws Exception {
        HtmlForm form;
        HtmlSubmitInput submit;
        HtmlPage page, page1;

        page = getPage("/faces/jsp/verbatim-test.jsp");
        form = getFormById(page, "form1");
        assertNotNull("form exists", form);
        submit = (HtmlSubmitInput)
            form.getInputByName("form1" + NamingContainer.SEPARATOR_CHAR +
                                "submit");

        // press the button
	try {
            page1 = (HtmlPage) submit.click();
            assertTrue(-1 != page1.asText().indexOf("Thank you"));
	} catch (Exception e) {
	    e.printStackTrace();
	    assertTrue(false);
	}
    }

    public void testNavigateWithVerbatim_One() throws Exception {
        HtmlForm form;
        HtmlSubmitInput submit;
        HtmlPage page, page1;
                                                                                
        page = getPage("/faces/jsp/verbatim-one-test.jsp");
        form = getFormById(page, "form");
        assertNotNull("form exists", form);
        submit = (HtmlSubmitInput)
            form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                "submit");
                                                                                
        // press the link, return to the same page, and check that
        // output text (header) is still present...
 
        try {
            page1 = (HtmlPage) submit.click();
            assertTrue(-1 != page1.asText().indexOf("this is the header"));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testNavigateWithVerbatim_Two() throws Exception {
        HtmlForm form;
        HtmlSubmitInput submit;
        HtmlPage page, page1;
                                                                               
                                                                               
        page = getPage("/faces/jsp/verbatim-two-test.jsp");
        form = getFormById(page, "form");
        assertNotNull("form exists", form);
        submit = (HtmlSubmitInput)
            form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                "submit");
                                                                               
                                                                               
        // submit the form, return to the same page, and check that
        // output text (header) is still present...
        // and verbatim text is still present...
                                                                               
        try {
            page1 = (HtmlPage) submit.click();
            assertTrue(-1 != page1.asText().indexOf("verbatim one text here"));
            assertTrue(-1 != page1.asText().indexOf("this is the header"));
            assertTrue(-1 != page1.asText().indexOf("verbatim two text here"));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    
    public void testNavigateWithEnum() throws Exception {
        HtmlForm form;
        HtmlSubmitInput submit;
        HtmlPage page, page1;
                                                                               
                                                                               
        page = getPage("/faces/enum01.jsp");
        form = getFormById(page, "form");
        assertNotNull("form exists", form);
        submit = (HtmlSubmitInput)
            form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                "go");
                                                                               
        // submit the form, go to next page, check that the text exists
                                                                               
        try {
            page1 = (HtmlPage) submit.click();
            assertTrue(-1 != page1.asText().indexOf("/hello.jsp PASSED"));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
        page = getPage("/faces/enum01.jsp");
        form = getFormById(page, "form");
        assertNotNull("form exists", form);
        submit = (HtmlSubmitInput)
            form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                "stay");
                                                                               
        // submit the form, stay on same page, check that the text does not exist
                                                                               
        try {
            page1 = (HtmlPage) submit.click();
            assertTrue(-1 == page1.asText().indexOf("/hello.jsp PASSED"));
            assertTrue(-1 != page1.asText().indexOf("stay here"));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        
    }

}
