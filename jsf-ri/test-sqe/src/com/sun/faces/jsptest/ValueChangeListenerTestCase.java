/*
 * $Id: ValueChangeListenerTestCase.java,v 1.2 2005/08/22 22:11:32 ofung Exp $
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

import java.util.List;

import com.sun.faces.htmlunit.AbstractTestCase;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Test that invalid values don't cause valueChangeEvents to occur.</p>
 */

public class ValueChangeListenerTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ValueChangeListenerTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ValueChangeListenerTestCase.class));
    }

    // ------------------------------------------------- Individual Test Methods
    public void testValueChangeListener() throws Exception {
	HtmlPage page = getPage("/faces/valueChangeListener.jsp");
	List list;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlTextInput.class); 

	// set the initial value to be 1 for both fields
	((HtmlTextInput)list.get(0)).setValueAttribute("1");
	((HtmlTextInput)list.get(1)).setValueAttribute("1");

	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();

	assertTrue(-1 != 
	   page.asText().indexOf("Received valueChangeEvent for textA"));

	assertTrue(-1 != 
	   page.asText().indexOf("Received valueChangeEvent for textB"));

	// re-submit the form, make sure no valueChangeEvents are fired
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();
	
	assertTrue(-1 == 
	   page.asText().indexOf("Received valueChangeEvent for textA"));

	assertTrue(-1 == 
	   page.asText().indexOf("Received valueChangeEvent for textB"));

	// give invalid values to one field and make sure no
	// valueChangeEvents are fired.
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlTextInput.class); 
	
	((HtmlTextInput)list.get(1)).setValueAttribute("-123");

	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();
	
	assertTrue(-1 == 
	   page.asText().indexOf("Received valueChangeEvent for textA"));

	assertTrue(-1 == 
	   page.asText().indexOf("Received valueChangeEvent for textB"));

	assertTrue(-1 != 
	   page.asText().indexOf("Validation Error"));

	// make sure dir and lang are passed through as expected for
	// message and messages
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSpan.class); 

	boolean 
	    hasMessageContent = false, // do we have the h:message
				       // content we're looking for
	    hasMessagesContent = false; // do we have the h:messages
					// content we're looking for.
	HtmlSpan span = null;
        HtmlUnorderedList ulist = null;

	for (int i = 0; i < list.size(); i++) {
	    span = (HtmlSpan) list.get(i);
	    if (-1 != span.asXml().indexOf("dir=\"RTL\"")
                && span.asXml().indexOf("lang=\"de\"") != -1) {
                hasMessageContent = true;
            }
        }
        list = getAllElementsOfGivenClass(page, null, HtmlUnorderedList.class);
        for (int i = 0; i < list.size(); i++) {
            ulist = (HtmlUnorderedList) list.get(i);
	    if (-1 != ulist.asXml().indexOf("dir=\"LTR\"")
                && ulist.asXml().indexOf("lang=\"en\"") != -1) {
                hasMessagesContent = true;
            }
	}
	assertTrue(hasMessagesContent && hasMessageContent);
	
    }
}
