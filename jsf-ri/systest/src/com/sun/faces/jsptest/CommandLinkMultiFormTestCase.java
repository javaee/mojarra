/*
 * $Id: CommandLinkMultiFormTestCase.java,v 1.4 2006/03/07 08:29:25 srinivas635 Exp $
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
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;

import javax.faces.component.NamingContainer;


/**
 * <p>Test Case for JSP Interoperability.</p>
 */

public class CommandLinkMultiFormTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public CommandLinkMultiFormTestCase(String name) {
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
        return (new TestSuite(CommandLinkMultiFormTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    public void testMultiForm() throws Exception {
        HtmlForm form1, form2;
        HtmlAnchor link1, link2, link3;
        HtmlTextInput input;
        HtmlPage page, page1;
        HtmlHiddenInput hidden1, hidden2;

        page = getPage("/faces/taglib/commandLink_multiform_test.jsp");
        // press all command links..
        List forms = page.getForms();
        form1 = (HtmlForm)forms.get(0);
        form2 = (HtmlForm)forms.get(1);
        
        // links within the first form
        hidden1 = (HtmlHiddenInput)form1.getInputByName("form01:_idcl");
        assertNotNull(hidden1);
        hidden1.setValueAttribute("form01:Link1");
        page1 = (HtmlPage)form1.submit();
        assertTrue(-1 != page1.asText().indexOf("Thank you"));
        hidden1.setValueAttribute("form01:Link2");
        page1 = (HtmlPage)form1.submit();
        assertTrue(-1 != page1.asText().indexOf("Thank you"));

        // links within second form
        hidden2 = (HtmlHiddenInput)form2.getInputByName("form02:_idcl");
        assertNotNull(hidden2);
        hidden2.setValueAttribute("form02:Link3");
        page1 = (HtmlPage)form1.submit();
        assertTrue(-1 != page1.asText().indexOf("Thank you"));
        hidden2.setValueAttribute("form02:Link4");
        page1 = (HtmlPage)form1.submit();
        assertTrue(-1 != page1.asText().indexOf("Thank you"));
    }
}
