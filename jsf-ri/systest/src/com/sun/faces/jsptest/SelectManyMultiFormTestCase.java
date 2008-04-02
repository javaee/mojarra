/*
 * $Id: SelectManyMultiFormTestCase.java,v 1.2 2004/02/04 23:42:26 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.jsptest;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
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

public class SelectManyMultiFormTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public SelectManyMultiFormTestCase(String name) {
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
        return (new TestSuite(SelectManyMultiFormTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    public void testMultiForm() throws Exception {
	HtmlForm form;
	HtmlSubmitInput submit;
	HtmlAnchor link;
	HtmlTextInput input;
        HtmlPage page;

	page = getPage("/faces/standard/selectmany01.jsp");
	// verify that the model tier is as expected
	assertTrue(-1 != 
		   page.asText().indexOf("Current model value: 1, 2, ,"));
	form = getFormById(page, "form2");
        submit = (HtmlSubmitInput)
	    form.getInputByName("form2" + NamingContainer.SEPARATOR_CHAR +
                                "doNotModify");
	
	// press button1
	page = (HtmlPage) submit.click();
	// verify that submitting the form does not change the model tier
	assertTrue(-1 != 
		   page.asText().indexOf("Current model value: 1, 2, ,"));
	
    }
}
