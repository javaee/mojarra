/*
 * $Id: SelectComponentValueTestCase.java,v 1.8 2006/03/29 23:03:56 rlubke Exp $
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

package com.sun.faces.jsptest;


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

public class SelectComponentValueTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public SelectComponentValueTestCase(String name) {
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
        return (new TestSuite(SelectComponentValueTestCase.class));
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
	HtmlPage page = getPage("/faces/jsp/selectOneNoValue.jsp");
	List list = getAllElementsOfGivenClass(page, null, 
					       HtmlSubmitInput.class); 
	HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();
	assertTrue(-1 != page.asText().indexOf("equired"));
	
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
        assertTrue(-1 != page.asText().indexOf("Validation Error"));
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
	
    public void testSelectOneTypeInt() throws Exception {
        HtmlPage page = getPage("/faces/jsp/selectOneTypeInt.jsp");
        List list = getAllElementsOfGivenClass(page, null,
                                               HtmlSubmitInput.class);
        HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
        list = getAllElementsOfGivenClass(page, null, HtmlSelect.class);
        HtmlSelect options = (HtmlSelect) list.get(0);
                                                                                                    
        String chosen = "2"; 
        options.fakeSelectedAttribute(chosen);
        page = (HtmlPage) button.click();
        ResourceBundle messages = ResourceBundle.getBundle(
            "javax.faces.Messages");
        String message = messages.getString("javax.faces.component.UISelectMany.INVALID");
        // it does not have a validation message
        assertTrue(-1 == page.asText().indexOf("Validation Error"));
    }

    // Verifies original selection from model is unchanged
    // when button with immediate="true" is pressed.
    //
    public void testSelectOneRadioTypeInteger() throws Exception {
        HtmlPage page = getPage("/faces/jsp/selectOneRadioTypeInteger.jsp");
        HtmlSubmitInput nonImmediateButton = null, immediateButton = null;
        HtmlRadioButtonInput radio1 = null, radio2 = null, radio3 = null;
        List submitList = getAllElementsOfGivenClass(page, null, HtmlSubmitInput.class);
        List radioList = getAllElementsOfGivenClass(page, null, HtmlRadioButtonInput.class);
        radio1 = (HtmlRadioButtonInput) radioList.get(0);
        radio2 = (HtmlRadioButtonInput) radioList.get(1);
        radio3 = (HtmlRadioButtonInput) radioList.get(2);
        for (int i = 0; i < submitList.size(); i++) {
            HtmlSubmitInput result = (HtmlSubmitInput) submitList.get(i);
            if (-1 != result.getIdAttribute().indexOf("nonImmediate")) {
                nonImmediateButton = result;
            } else {
                immediateButton = result;
            }
        }
        assertTrue(-1 != page.asText().indexOf("Model Selection:2"));

        // now press the immediate button and verify that our original
        // selection is there...
        page = (HtmlPage) immediateButton.click();
        assertTrue(-1 != page.asText().indexOf("Model Selection:2"));

        // now click the first radio option...
        radio1.setChecked(true);
        // now press the immediate button and verify that our original
        // selection is there...
        page = (HtmlPage) immediateButton.click();
        assertTrue(-1 != page.asText().indexOf("Model Selection:2"));

        // now click the first radio option...
        radio1.setChecked(true);
        // now press the non immediate button and verify that our new
        // selection is there...
        page = (HtmlPage) nonImmediateButton.click();
        assertTrue(-1 != page.asText().indexOf("Model Selection:1"));
    }
    
    public void testSelectOneRadioTypeInt() throws Exception {
        HtmlPage page = getPage("/faces/jsp/selectOneRadioTypeInt.jsp");
        HtmlSubmitInput nonImmediateButton = null, immediateButton = null;
        HtmlRadioButtonInput radio1 = null, radio2 = null, radio3 = null;
        List submitList = getAllElementsOfGivenClass(page, null, HtmlSubmitInput.class);
        List radioList = getAllElementsOfGivenClass(page, null, HtmlRadioButtonInput.class);
        radio1 = (HtmlRadioButtonInput) radioList.get(0);
        radio2 = (HtmlRadioButtonInput) radioList.get(1);
        radio3 = (HtmlRadioButtonInput) radioList.get(2);
        for (int i = 0; i < submitList.size(); i++) {
            HtmlSubmitInput result = (HtmlSubmitInput) submitList.get(i);
            if (-1 != result.getIdAttribute().indexOf("nonImmediate")) {
                nonImmediateButton = result;
            } else {
                immediateButton = result;
            }
        }
        assertTrue(-1 != page.asText().indexOf("Model Selection:3"));

        // now press the immediate button and verify that our original
        // selection is there...
        page = (HtmlPage) immediateButton.click();
        assertTrue(-1 != page.asText().indexOf("Model Selection:3"));

        // now click the first radio option...
        radio1.setChecked(true);
        // now press the immediate button and verify that our original
        // selection is there...
        page = (HtmlPage) immediateButton.click();
        assertTrue(-1 != page.asText().indexOf("Model Selection:3"));

        // now click the first radio option...
        radio1.setChecked(true);
        // now press the non immediate button and verify that our new
        // selection is there...
        page = (HtmlPage) nonImmediateButton.click();
        assertTrue(-1 != page.asText().indexOf("Model Selection:1"));
    }

    public void testSelectManyTypeInts() throws Exception {
        HtmlPage page = getPage("/faces/jsp/selectManyTypeInts.jsp");
        List list = getAllElementsOfGivenClass(page, null,
                                               HtmlSubmitInput.class);
        HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
        list = getAllElementsOfGivenClass(page, null, HtmlSelect.class);
        HtmlSelect options = (HtmlSelect) list.get(0);
                                                                                                    
        String chosen [] = {"2", "3"};
        options.fakeSelectedAttribute(chosen);
        page = (HtmlPage) button.click();
        ResourceBundle messages = ResourceBundle.getBundle(
            "javax.faces.Messages");
        String message = messages.getString("javax.faces.component.UISelectMany.INVALID");
        // it does not have a validation message
        assertTrue(-1 == page.asText().indexOf("Validation Error"));
    }





}
