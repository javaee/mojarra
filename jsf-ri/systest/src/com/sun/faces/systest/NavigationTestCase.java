/*
 * $Id: NavigationTestCase.java,v 1.2 2004/02/04 23:42:33 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import java.net.URL;
import java.util.Iterator;
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
	}
	catch (FailingHttpStatusCodeException fhsce) {
	    assertEquals("Didn't get expected redirect", 
			 fhsce.getStatusCode(), 302);
	    exceptionThrown = true;
	}
	assertTrue("Didn't get expected redirect", exceptionThrown);
    }


}
