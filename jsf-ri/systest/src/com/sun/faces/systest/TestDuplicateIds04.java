/*
 * $Id: TestDuplicateIds04.java,v 1.1 2004/05/04 19:19:24 eburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
n */

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
 * <p>Verify that required validation occurrs for Select* components.</p>
 */

public class TestDuplicateIds04 extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public TestDuplicateIds04(String name) {
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
        return (new TestSuite(TestDuplicateIds04.class));
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
     * <p>Verify that the required validator works for SelectOne</p>
     */

    public void testSelectOneNoValue() throws Exception {
	HtmlPage page = getPage("/faces/duplicateIds04.jsp");
	List list = getAllElementsOfGivenClass(page, null, 
					       HtmlSubmitInput.class); 
	HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
	client.setThrowExceptionOnFailingStatusCode(false);
	page = (HtmlPage) button.click();
	assertTrue(-1 != page.asText().indexOf("_id0:_id2"));
	assertTrue(-1 != page.asText().indexOf("Duplicate"));
	client.setThrowExceptionOnFailingStatusCode(true);
    }

    /**
     *
     * <p>Verify that the required validator works for SelectMany</p>
     */

    public void testSelectManyNoValue() throws Exception {
	HtmlPage page = getPage("/faces/jsp/selectManyNoValue.jsp");
	List list = getAllElementsOfGivenClass(page, null, 
					       HtmlSubmitInput.class); 
	HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();
	assertTrue(-1 != page.asText().indexOf("equired"));
	
    }

    /**
     *
     * <p>Verify that the conversion error works for SelectMany</p>
     */

    public void testSelectManyMismatchValue() throws Exception {
	HtmlPage page = getPage("/faces/jsp/selectManyMismatchValue.jsp");
	List list = getAllElementsOfGivenClass(page, null, 
					       HtmlSubmitInput.class); 
	HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
	list = getAllElementsOfGivenClass(page, null, HtmlSelect.class);
	HtmlSelect options = (HtmlSelect) list.get(0);
	String chosen [] = {"one", "three"};
	options.fakeSelectedAttribute(chosen);
	page = (HtmlPage) button.click();
	assertTrue(-1 != page.asText().indexOf("one three"));
	assertTrue(-1 != page.asText().indexOf("#{test3.selection}"));
	
    }


    /**
     * On SelectMany, test that the membership test works and doesn't
     * produce spurious ValueChangeEvent instances.
     */
    public void testSelectManyInvalidValue() throws Exception {
	HtmlPage page = getPage("/faces/jsp/selectManyInvalidValue.jsp");
	List list = getAllElementsOfGivenClass(page, null, 
					       HtmlSubmitInput.class); 
	HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
	list = getAllElementsOfGivenClass(page, null, HtmlSelect.class);
	HtmlSelect options = (HtmlSelect) list.get(0);
	Random random = new Random(4143);
	String str = new String((new Float(random.nextFloat())).toString());

	String chosen [] = {
	    (new Float(random.nextFloat())).toString(),
	    (new Float(random.nextFloat())).toString()
	};
	options.fakeSelectedAttribute(chosen);
	page = (HtmlPage) button.click();
        ResourceBundle messages = ResourceBundle.getBundle(
            "javax.faces.Messages");
        String message = messages.getString("javax.faces.component.UISelectMany.INVALID");
	// it does have a validation message
        assertTrue(-1 != page.asText().indexOf(message));
	// it does not have a value change message
	assertTrue(-1 == page.asText().indexOf("value changed"));
    }


    /**
     * run doInvalidTest on UISelectOne
     */

    public void testSelectOneInvalidValue() throws Exception {
	HtmlPage page = getPage("/faces/jsp/selectOneInvalidValue.jsp");

	List list = getAllElementsOfGivenClass(page, null, 
					       HtmlSubmitInput.class); 
	HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlRadioButtonInput.class);
	HtmlRadioButtonInput radio = (HtmlRadioButtonInput) list.get(0);
	radio.setChecked(true);
	page = (HtmlPage) button.click();
        ResourceBundle messages = ResourceBundle.getBundle("javax.faces.Messages");
        String message = messages.getString("javax.faces.component.UIInput.REQUIRED");
	// it does not have a validation error
	assertTrue(-1 == page.asText().indexOf(message));
    }
	





}
