/*
 * $Id: MethodRefTestCase.java,v 1.1 2003/10/31 18:42:23 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.methodref;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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
        return (new TestSuite(MethodRefTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test dynamically adding and removing components
    public void testSimpleActionRef() throws Exception {
	HtmlForm form;
	HtmlSubmitInput submit;
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
        submit = (HtmlSubmitInput)
	    form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
                                "button2");
	input = (HtmlTextInput) 
	    form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
				"input");
	assertTrue("Input does not have expected value",
		   -1 != input.asText().indexOf("button1 was pressed"));
	
	// press button2
	page = (HtmlPage) submit.click();
	form = getFormById(page, "form");
	input = (HtmlTextInput) 
	    form.getInputByName("form" + NamingContainer.SEPARATOR_CHAR +
				"input");
	assertTrue("Input does not have expected value",
		   -1 != input.asText().indexOf("button2 was pressed"));

	
    }


}
