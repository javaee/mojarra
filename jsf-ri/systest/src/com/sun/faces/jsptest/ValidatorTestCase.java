/*
 * $Id: ValidatorTestCase.java,v 1.4 2005/04/26 16:41:39 jayashri Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.jsptest;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;

import javax.faces.component.NamingContainer;

/**
 * <p>Test that invalid values don't cause valueChangeEvents to occur.</p>
 */

public class ValidatorTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ValidatorTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ValidatorTestCase.class));
    }

    // ------------------------------------------------- Individual Test Methods
    public void testValidator() throws Exception {
	HtmlPage page = getPage("/faces/validator02.jsp");
	List list;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlTextInput.class); 

	// set the initial value to be 1 for all input fields
	((HtmlTextInput)list.get(0)).setValueAttribute("1111111111");
	((HtmlTextInput)list.get(1)).setValueAttribute("1111111111");
	((HtmlTextInput)list.get(2)).setValueAttribute("1111111111");
	((HtmlTextInput)list.get(3)).setValueAttribute("1111111111");
	((HtmlTextInput)list.get(4)).setValueAttribute("1111111111");
	((HtmlTextInput)list.get(5)).setValueAttribute("1111111111");
	((HtmlTextInput)list.get(6)).setValueAttribute("1111111111");

	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();
	assertTrue(-1 != page.asText().indexOf("text1 was validated"));
	assertTrue(-1 != page.asText().indexOf("text2 was validated"));
	assertTrue(-1 != page.asText().indexOf("text3 was validated"));
	assertTrue(-1 != page.asText().indexOf("text4 was validated"));
        String str = "allowable maximum of "+'"'+"2"+'"';
	assertTrue(-1 != page.asText().indexOf(str));
	assertTrue(-1 != page.asText().indexOf("allowable maximum of '5'"));
	assertTrue(-1 != page.asText().indexOf("allowable maximum of '4'"));

    }

    public void testValidatorMessages() throws Exception {
        HtmlPage page = getPage("/faces/validator03.jsp");
        List list;
        list = getAllElementsOfGivenClass(page, null,
                                          HtmlTextInput.class);
                                                                                         
        // set the initial value to be "1" for all input fields
        for (int i=0; i< list.size(); i++) {
            ((HtmlTextInput)list.get(i)).setValueAttribute("1");
        }
                                                                                         
        list = getAllElementsOfGivenClass(page, null,
                                          HtmlSubmitInput.class);
        HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        assertTrue(-1 != page.asText().indexOf("_id1:dr1: Validation Error: Specified attribute is not between the expected values of 2 and 5."));
        assertTrue(-1 != page.asText().indexOf("DoubleRange2: Validation Error: Specified attribute is not between the expected values of 2 and 5."));
        assertTrue(-1 != page.asText().indexOf("_id1:l1: Validation Error: Value is less than allowable minimum of '2'"));
        assertTrue(-1 != page.asText().indexOf("Length2: Validation Error: Value is less than allowable minimum of '2'"));
        assertTrue(-1 != page.asText().indexOf("id1:lr1: Validation Error: Specified attribute is not between the expected values of 2 and 5."));
        assertTrue(-1 != page.asText().indexOf("LongRange2: Validation Error: Specified attribute is not between the expected values of 2 and 5."));
    }

}
