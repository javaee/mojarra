/*
 * $Id: ForEachTestCase.java,v 1.1 2005/08/15 19:00:11 jayashri Exp $
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

public class ForEachTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ForEachTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ForEachTestCase.class));
    }

    // ------------------------------------------------- Individual Test Methods
    public void testForEach() throws Exception {
	HtmlPage page = getPage("/faces/forEach01.jsp");
        
        // Make sure values are displayed properly for the initial request 
        //assert outputText values are as expected
        assertTrue(-1 != page.asText().indexOf("output1"));
	assertTrue(-1 != page.asText().indexOf("output2"));
	assertTrue(-1 != page.asText().indexOf("output3"));
        
        //assert inputText without "id" values are as expected
        assertTrue(-1 != page.asText().indexOf("inputText1=input1"));
	assertTrue(-1 != page.asText().indexOf("inputText2=input2"));
	assertTrue(-1 != page.asText().indexOf("inputText3=input3"));
        
        //assert inputText with "id" values are as expected
        assertTrue(-1 != page.asText().indexOf("inputid1"));
	assertTrue(-1 != page.asText().indexOf("inputid2"));
	assertTrue(-1 != page.asText().indexOf("inputid3"));
        
        // Assign new values to input fields, submit the form.
	List list;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlTextInput.class); 
        
	((HtmlTextInput)list.get(0)).setValueAttribute("newValue1");
	((HtmlTextInput)list.get(1)).setValueAttribute("newValue2");
	((HtmlTextInput)list.get(2)).setValueAttribute("newValue3");
        
        ((HtmlTextInput)list.get(3)).setValueAttribute("newValueid1");
	((HtmlTextInput)list.get(4)).setValueAttribute("newValueid2");
	((HtmlTextInput)list.get(5)).setValueAttribute("newValueid3");

	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();
        
        // make sure the values are as expected on post back.
	 assertTrue(-1 != page.asText().indexOf("output1"));
	assertTrue(-1 != page.asText().indexOf("output2"));
	assertTrue(-1 != page.asText().indexOf("output3"));
        
        //assert inputText without "id" values are as expected
        assertTrue(-1 != page.asText().indexOf("inputText1=input1"));
	assertTrue(-1 != page.asText().indexOf("inputText2=input2"));
	assertTrue(-1 != page.asText().indexOf("inputText3=input3"));
        
        assertTrue(-1 != page.asText().indexOf("newValue1"));
	assertTrue(-1 != page.asText().indexOf("newValue2"));
	assertTrue(-1 != page.asText().indexOf("newValue3"));
        
        //assert inputText with "id" values are as expected
        assertTrue(-1 != page.asText().indexOf("inputid1"));
	assertTrue(-1 != page.asText().indexOf("inputid2"));
	assertTrue(-1 != page.asText().indexOf("inputid3"));
        
        assertTrue(-1 != page.asText().indexOf("newValueid1"));
	assertTrue(-1 != page.asText().indexOf("newValueid2"));
	assertTrue(-1 != page.asText().indexOf("newValueid3"));
    }

}
