/*
 * $Id: ReplaceStateManagerTestCase.java,v 1.1 2005/03/18 22:12:51 edburns Exp $
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
 * <p>Make sure that an application that replaces the ApplicationFactory
 * but uses the decorator pattern to allow the existing ApplicationImpl
 * to do the bulk of the requests works.</p>
 */

public class ReplaceStateManagerTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ReplaceStateManagerTestCase(String name) {
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
        return (new TestSuite(ReplaceStateManagerTestCase.class));
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
     * <p>Verify that the bean is successfully resolved</p>
     */

    public void testReplaceStateManager() throws Exception {
	HtmlPage page = getPage("/faces/test.jsp");
	assertTrue(-1 != page.asText().indexOf("New String Value"));
	assertTrue(-1 != page.asText().indexOf("com.sun.faces.systest.NewStateManager"));
	assertTrue(-1 != page.asText().indexOf("com.sun.faces.systest.NewViewHandler"));
	assertTrue(-1 != page.asText().indexOf("com.sun.faces.systest.NewApplication"));
	
	List list;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlTextInput.class); 
	((HtmlTextInput)list.get(0)).setValueAttribute("text from client");
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();
	
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlTextInput.class); 
	assertEquals("text from client", 
		     ((HtmlTextInput)list.get(0)).asText());
	
    }

}
