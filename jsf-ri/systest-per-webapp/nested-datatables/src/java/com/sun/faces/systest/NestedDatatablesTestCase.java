/*
 * $Id: NestedDatatablesTestCase.java,v 1.5 2006/03/29 23:04:28 rlubke Exp $
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

    public void testInputFieldUpdate2() throws Exception {
	HtmlPage page = getPage("/faces/nested.jsp");
	List list;
	int i;
	char c, max;

	HtmlSubmitInput button = null;
	HtmlTextInput input = null;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlTextInput.class); 
	// 
	// submit values 1 thru list.size();
	// 
	for (i = 0; i < list.size(); i++) {
	    ((HtmlTextInput)list.get(i)).setValueAttribute("" + i);
	}

	// find and press the "reload" button
	page = pressReloadButton(page);

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
	
	// find and press the "reload" button
	page = pressReloadButton(page);

	list = getAllElementsOfGivenClass(page, null, 
					  HtmlTextInput.class); 
	i = 0;
	// verify they are correctly updated
	for (c = 'a'; c < max; c++) {
	    assertEquals("" + c, 
			 ((HtmlTextInput)list.get(i++)).getValueAttribute());
	}

	// add some ports
       
	// press first add port button
	page = pressAddPortButton(page, 0);

	// change the port number
	input = (HtmlTextInput) getNthInputContainingGivenId(page, 
							     "portNumber", 0);
	input.setValueAttribute("12");

	page = pressReloadButton(page);

	// verify that it is updated correctly.
	input = (HtmlTextInput) getNthInputContainingGivenId(page, 
							     "portNumber", 0);
	assertEquals("12", input.getValueAttribute());

	// press second add port button
	page = pressAddPortButton(page, 1);

	// verify that the last port input in the page doesn't the value
	input = (HtmlTextInput) getNthFromLastInputContainingGivenId(page, 
							     "portNumber", 0);
	assertTrue(-1 == input.getValueAttribute().indexOf("12"));

    }

    private HtmlPage pressReloadButton(HtmlPage page) throws Exception {
	HtmlSubmitInput button = null;
	
	button = (HtmlSubmitInput)getInputContainingGivenId(page, 
							    "reload");
	page = (HtmlPage) button.click();
	return page;
    }
    
    private HtmlPage pressAddPortButton(HtmlPage page, 
					int whichButton) throws Exception {
	HtmlSubmitInput button = null;
	
	button = (HtmlSubmitInput)getNthInputContainingGivenId(page, 
							       "add-port",
							       whichButton);
	page = (HtmlPage) button.click();
	return page;
    }
    
}

