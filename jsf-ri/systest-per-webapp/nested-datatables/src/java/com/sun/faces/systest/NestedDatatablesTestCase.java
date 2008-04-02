/*
 * $Id: NestedDatatablesTestCase.java,v 1.1 2004/06/17 20:13:33 eburns Exp $
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

public class NestedDatatablesTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public NestedDatatablesTestCase(String name) {
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
        return (new TestSuite(NestedDatatablesTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------ Instance Variables



    // ------------------------------------------------- Individual Test Methods

    public void testInputFieldUpdate() throws Exception {
	HtmlPage page = getPage("/faces/test.jsp");
	List list;
	int i;
	char c, max;

	HtmlTextInput input = null;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlTextInput.class); 
	// 
	// submit values 1 thru list.size();
	// 
	for (i = 0; i < list.size(); i++) {
	    ((HtmlTextInput)list.get(i)).setValueAttribute("" + i);
	}

	HtmlSubmitInput button = null;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();

	list = getAllElementsOfGivenClass(page, null, 
					  HtmlTextInput.class); 
	// verify they are correctly updated
	for (i = 0; i < list.size(); i++) {
	    assertEquals("" + i, 
			 ((HtmlTextInput)list.get(i)).getValueAttribute());
	}

	// 
	// submit values a thru (a + list.size())
	// 

	max = (char) ('a' + (char) list.size());
	i = 0;

	for (c = 'a'; c < max; c++) {
	    ((HtmlTextInput)list.get(i++)).setValueAttribute("" + c);
	}
	
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();

	list = getAllElementsOfGivenClass(page, null, 
					  HtmlTextInput.class); 

	i = 0;
	// verify they are correctly updated
	for (c = 'a'; c < max; c++) {
	    assertEquals("" + c, 
			 ((HtmlTextInput)list.get(i++)).getValueAttribute());
	}


    }

}
