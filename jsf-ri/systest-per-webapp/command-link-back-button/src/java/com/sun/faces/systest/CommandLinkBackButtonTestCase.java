/*
 * $Id: CommandLinkBackButtonTestCase.java,v 1.1 2004/11/12 18:00:27 jayashri Exp $
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
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
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
 * <p>Make sure no extra baggage from a previously clicked commandLink
 * is submitted along with a button press.</p>
 */

public class CommandLinkBackButtonTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public CommandLinkBackButtonTestCase(String name) {
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
        return (new TestSuite(CommandLinkBackButtonTestCase.class));
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

	HtmlAnchor link = null;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlAnchor.class); 
	link = (HtmlAnchor) list.get(0);

	// due to a bug in HtmlUnit 1.2.3, we can't just click the link:
	// page = (HtmlPage) link.click();
	// therefore, we have to hack around it.
	list = page.getAllForms();
	HtmlHiddenInput hidden = (HtmlHiddenInput) ((HtmlForm)list.get(0)).getInputByName("form:_idcl");
        assertTrue( hidden != null);
	hidden.setValueAttribute("form:link");
	page = (HtmlPage) ((HtmlForm)list.get(0)).submit();
	
	// make sure the page contains evidence that the hidden field
	// *did* receive a value.
        // PENDING (visvan) this doesn't work due to bug in HTMLUnit 1.2.3
	// assertTrue(-1 != page.asText().indexOf("form:link"));
	
	page = getPage("/faces/test.jsp");
	HtmlSubmitInput button = null;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();
        
	// make sure the page contains evidence that the hidden field
	// *did not* receive a value.
	assertTrue(-1 == page.asText().indexOf("form:link"));

    }
    
}

