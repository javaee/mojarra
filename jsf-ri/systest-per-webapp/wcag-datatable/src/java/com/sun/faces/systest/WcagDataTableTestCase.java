/*
 * $Id: WcagDataTableTestCase.java,v 1.1 2006/09/15 21:48:14 edburns Exp $
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

public class WcagDataTableTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public WcagDataTableTestCase(String name) {
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
        return (new TestSuite(WcagDataTableTestCase.class));
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
     * <p>Verify that the bean is successfully resolved</p>
     */

    public void testReplaceStateManager() throws Exception {
	HtmlPage page = getPage("/faces/index.jsp");
        String pageText = page.asXml();
        // (?s) is an "embedded flag expression" for the "DOTALL" operator.
        // It says, "let . match any character including line terminators."
        // Because page.asXml() returns a big string with lots of \r\n chars
        // in it, we need (?s).
        // the page contains a table tag with a frame attribute whose value is hsides.
        assertTrue(pageText.matches("(?s).*<table.*frame=.hsides.*>.*"));
        // the page contains a table tag with a rules attribute whose value is groups.
        assertTrue(pageText.matches("(?s).*<table.*rules..groups.*>.*"));
        // the page contains a table tag with a summary attribute whose value is that string.
        assertTrue(pageText.matches("(?s).*<table.*summary..Code page support in different versions of MS Windows.*>.*"));
        // the page contains a table tag followed immediately by the caption element as follows.
        assertTrue(pageText.matches("(?sm).*<table.*>[ \r\n\t]*<caption>.*CODE-PAGE SUPPORT IN MICROSOFT WINDOWS.*</caption>.*"));
        // the page contains a close caption tag followed immediately by a three colgroup tags as follows.
        assertTrue(pageText.matches("(?sm).*</caption>[ \r\n\t]*" +
                "<colgroup align=.center.[ \t]*/>[ \r\n\t]*" + 
                "<colgroup align=.left.[ \t]*/>[ \r\n\t]*" +
                "<colgroup.*span=.2.*/>.*"));
        // the page contains a close caption tag followed immediately by a two colgroup tags as follows.
        assertTrue(pageText.matches("(?sm).*</caption>[ \r\n\t]*" +
                "<colgroup align=.center.[ \t]*/>[ \r\n\t]*" + 
                "<colgroup align=.left.[ \t]*/>[ \r\n\t]*" +
                "<colgroup.*align=.center.*/>.*"));
        // The last colgroup in the previous two assertions accounts for the align and span attributes occurring out of order.
        
        // A table with a thead, with a tr with a th scope=col
        assertTrue(pageText.matches("(?sm).*<table.*>.*<thead>[ \r\n\t]*" +
                "<tr>[ \r\n\t]*" + 
                "<th[ \t]*scope=.col.*"));
        
        // A table with a tbody, with a tr with a th scope=row
        assertTrue(pageText.matches("(?sm).*<table.*>.*<tbody>.*" +
                "<th[ \t]*scope=.row.*"));

        // A table with a tbody, with a tr with a th scope=row
        assertTrue(pageText.matches("(?sm).*<table.*>.*<tbody>.*" +
                "</tbody>.*<tbody>.*</tbody>.*</table>.*"));
	
    }

}
