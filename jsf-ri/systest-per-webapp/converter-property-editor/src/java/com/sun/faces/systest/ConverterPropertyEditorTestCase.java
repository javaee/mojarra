/*
 * $Id: ConverterPropertyEditorTestCase.java,v 1.2 2006/09/14 22:38:42 tony_robertson Exp $
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
 * <p>Make sure that an application that replaces the ApplicationFactory
 * but uses the decorator pattern to allow the existing ApplicationImpl
 * to do the bulk of the requests works.</p>
 */

public class ConverterPropertyEditorTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ConverterPropertyEditorTestCase(String name) {
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
        return (new TestSuite(ConverterPropertyEditorTestCase.class));
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
     * <p>Verify that selectOne conversion has successfully occurred.</p>
     */

    public void testConverterPropertyEditor() throws Exception {
	HtmlPage page;
	List list;
	HtmlSubmitInput button;

	page = getPage("/faces/selectone.jsp");
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();

	assertTrue(-1 != page.asText().indexOf("This selectOneMenu is bound to a list of test.Payment"));

	page = getPage("/faces/selectoneRadio.jsp");
	list = getAllElementsOfGivenClass(page, null, HtmlSubmitInput.class); 
	button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();

	assertTrue(-1 != page.asText().indexOf("This selectOneRadio is bound to a list of test.Payment"));

	page = getPage("/faces/selectmany.jsp");
	list = getAllElementsOfGivenClass(page, null, HtmlSubmitInput.class); 
	button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();

	assertTrue(-1 != page.asText().indexOf("This selectManyMenu is bound to a list of test.Payment"));

        page = getPage("/faces/selectmanyListbox.jsp");
        list = getAllElementsOfGivenClass(page, null, HtmlSubmitInput.class); 
        button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        assertTrue(-1 != page.asText().indexOf("This selectManyListbox is bound to a list of test.Payment"));

        page = getPage("/faces/selectmanyCheckbox.jsp");
        list = getAllElementsOfGivenClass(page, null, HtmlSubmitInput.class); 
        button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        assertTrue(-1 != page.asText().indexOf("This selectManyCheckbox is bound to a list of test.Payment"));

    }

}
