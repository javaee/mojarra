/*
 * $Id: ListenerTestCase.java,v 1.4 2006/03/29 23:03:56 rlubke Exp $
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

public class ListenerTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ListenerTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ListenerTestCase.class));
    }

    // ------------------------------------------------- Individual Test Methods
    public void testListener() throws Exception {
	HtmlPage page = getPage("/faces/listener.jsp");
	List list;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlTextInput.class); 

	// set the initial value to be 1 for all input fields
	((HtmlTextInput)list.get(0)).setValueAttribute("1");
	((HtmlTextInput)list.get(1)).setValueAttribute("1");
	((HtmlTextInput)list.get(2)).setValueAttribute("1");
	((HtmlTextInput)list.get(3)).setValueAttribute("1");

	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();
	assertTrue(-1 != page.asText().indexOf("text1 value was changed"));

	assertTrue(-1 != page.asText().indexOf("text2 value was changed"));

	assertTrue(-1 != page.asText().indexOf("text3 value was changed"));

	assertTrue(-1 != page.asText().indexOf("text4 value was changed"));

	// re-submit the form, make sure no valueChangeEvents are fired
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();
	
	assertTrue(-1 == page.asText().indexOf("text1 value was changed"));
	assertTrue(-1 == page.asText().indexOf("text2 value was changed"));
	assertTrue(-1 == page.asText().indexOf("text3 value was changed"));
	assertTrue(-1 == page.asText().indexOf("text4 value was changed"));

	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();
	
	assertTrue(-1 != page.asText().indexOf("button1 was pressed"));

	button = (HtmlSubmitInput) list.get(1);
	page = (HtmlPage) button.click();
	
	assertTrue(-1 != page.asText().indexOf("button2 was pressed"));

	button = (HtmlSubmitInput) list.get(2);
	page = (HtmlPage) button.click();

	assertTrue(-1 != page.asText().indexOf("button3 was pressed"));

	button = (HtmlSubmitInput) list.get(3);
	page = (HtmlPage) button.click();

	assertTrue(-1 != page.asText().indexOf("button4 was pressed"));
    }
}
