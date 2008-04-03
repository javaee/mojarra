/*
 * $Id: SelectComponentValueTestCase.java,v 1.3 2007/04/27 22:02:14 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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
