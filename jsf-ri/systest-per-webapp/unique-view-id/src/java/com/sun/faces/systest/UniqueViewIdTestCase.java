/*
 * $Id: UniqueViewIdTestCase.java,v 1.1 2004/06/01 17:06:29 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.faces.component.NamingContainer;


/**
 * <p>Make sure that only unique view ids are saved in the session</p>
 */

public class UniqueViewIdTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public UniqueViewIdTestCase(String name) {
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
        return (new TestSuite(UniqueViewIdTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------ Instance Variables



    // ------------------------------------------------- Individual Test Methods

    /**
     *
     * <p>See that clicking the re-submit button 20 times doesn't
     * increment the view counter.</p>
     */

    public void testReSubmitDoesNotIncrementCounter() throws Exception {
	HtmlPage page = getPage("/faces/test.jsp");
	List list;
	HtmlSubmitInput button = null;
	for (int i = 0; i < 20; i++) {
	    list = getAllElementsOfGivenClass(page, null, 
					      HtmlSubmitInput.class); 
	    button = (HtmlSubmitInput) list.get(0);
	    page = (HtmlPage) button.click();
	}
	assertTrue(-1 != 
		   page.asText().indexOf("Number of views in session is 1."));
    }

    /**
     *
     * <p>See that clicking to a new page and back again does increment
     * the view counter.</p>
     */

    public void testNewPageDoesIncrementCounter() throws Exception {
	HtmlPage page = getPage("/faces/test.jsp");
	List list;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	HtmlSubmitInput button = (HtmlSubmitInput) list.get(1);
	page = (HtmlPage) button.click();

	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();

	assertTrue(-1 != 
		   page.asText().indexOf("Number of views in session is 2."));

    }


}
