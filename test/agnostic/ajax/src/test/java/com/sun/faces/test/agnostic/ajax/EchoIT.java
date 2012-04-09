/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package com.sun.faces.test.agnostic.ajax;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

public class EchoIT {
    
    /**
     * Stores the web URL.
     */
    private String webUrl;
    /**
     * Stores the web client.
     */
    private WebClient webClient;

    /**
     * Setup before testing.
     */
    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
        webClient.setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(60000);
    }

    /**
     * Tear down after testing.
     */
    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    @Test
    public void testAjaxEcho() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/echo.xhtml");

        assertTrue(page.getElementById("form1:out1").asXml().indexOf("hello") == -1);
        
        HtmlTextInput in1 = (HtmlTextInput) page.getHtmlElementById("form1:in1");
        in1.type("hello");

        HtmlSubmitInput button1 = (HtmlSubmitInput) page.getHtmlElementById("form1:button1");
        page = (HtmlPage) button1.click();
        webClient.waitForBackgroundJavaScript(60000);

        assertTrue(page.getElementById("form1:out1").asXml().indexOf("hello") != -1);
    }

    public void testAjaxEchoWithStringId() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/echo.xhtml");

        assertTrue(page.getElementById("form1:out1").asXml().indexOf("hello") == -1);

        HtmlTextInput in1 = (HtmlTextInput) page.getHtmlElementById("form1:in1");
        in1.type("hello");

        HtmlSubmitInput button2 = (HtmlSubmitInput) page.getHtmlElementById("form1:button2");
        page = (HtmlPage) button2.click();
        webClient.waitForBackgroundJavaScript(60000);

        assertTrue(page.getElementById("form1:out1").asXml().indexOf("hello") != -1);
    }

    @Test
    public void testAjaxEchoLT() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/echo.xhtml");

        assertTrue(page.getElementById("form1:out1").asText().indexOf("<") == -1);

        HtmlTextInput in1 = (HtmlTextInput) page.getHtmlElementById("form1:in1");
        in1.type("<");

        HtmlSubmitInput button1 = (HtmlSubmitInput) page.getHtmlElementById("form1:button1");
        page = (HtmlPage) button1.click();
        webClient.waitForBackgroundJavaScript(60000);

        assertTrue(page.getElementById("form1:out1").asText().indexOf("<") != -1);
    }

//    /*
//     * Regression test for bug #939
//     */
//    public void testCdataEscape1() throws Exception {
//        getPage("/faces/ajax/ajaxEcho.xhtml");
//
//        // First we'll check the first page was output correctly
//        checkTrue("form1:out1","");
//        checkTrue("form1:in1","");
//
//        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("form1:in1");
//
//        in1.type("]]>");
//
//        // Submit the ajax request
//        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button1");
//        lastpage = (HtmlPage) button1.click();
//
//        // Check that the ajax request succeeds
//        checkTrue("form1:out1","]]>");
//    }
//    public void testCdataEscape2() throws Exception {
//        getPage("/faces/ajax/ajaxEcho.xhtml");
//
//        // First we'll check the first page was output correctly
//        checkTrue("form1:out1","");
//        checkTrue("form1:in1","");
//
//        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("form1:in1");
//
//        in1.type("<!");
//
//        // Submit the ajax request
//        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button1");
//        lastpage = (HtmlPage) button1.click();
//
//        // Check that the ajax request succeeds
//        checkTrue("form1:out1","<!");
//    }
//    public void testCdataEscape3() throws Exception {
//        getPage("/faces/ajax/ajaxEcho.xhtml");
//
//        // First we'll check the first page was output correctly
//        checkTrue("form1:out1","");
//        checkTrue("form1:in1","");
//
//        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("form1:in1");
//
//        in1.type("]");
//
//        // Submit the ajax request
//        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button1");
//        lastpage = (HtmlPage) button1.click();
//
//        // Check that the ajax request succeeds
//        checkTrue("form1:out1","]");
//    }
//    public void testCdataEscape4() throws Exception {
//        getPage("/faces/ajax/ajaxEcho.xhtml");
//
//        // First we'll check the first page was output correctly
//        checkTrue("form1:out1","");
//        checkTrue("form1:in1","");
//
//        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("form1:in1");
//
//        in1.type("]");
//
//        // Submit the ajax request
//        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button1");
//        lastpage = (HtmlPage) button1.click();
//
//        // Check that the ajax request succeeds
//        checkTrue("form1:out1","]");
//    }
//    public void testCdataEscape5() throws Exception {
//        getPage("/faces/ajax/ajaxEcho.xhtml");
//
//        // First we'll check the first page was output correctly
//        checkTrue("form1:out1","");
//        checkTrue("form1:in1","");
//
//        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("form1:in1");
//
//        in1.type("<![CDATA[ ]]>");
//
//        // Submit the ajax request
//        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button1");
//        lastpage = (HtmlPage) button1.click();
//
//        // Check that the ajax request succeeds
//        checkTrue("form1:out1","<![CDATA[ ]]>");
//    }
//
//    // Test for bug #1284
//    public void testCdataEscape6() throws Exception {
//        getPage("/faces/ajax/ajaxEcho.xhtml");
//
//        // First we'll check the first page was output correctly
//        checkTrue("form1:out1","");
//        checkTrue("form1:in1","");
//
//        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("form1:in1");
//
//        in1.type("[");
//
//        // Submit the ajax request
//        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button1");
//        lastpage = (HtmlPage) button1.click();
//
//        // Check that the ajax request succeeds
//        checkTrue("form1:out1","[");
//    }
//    // Test for bug #1284
//    public void testCdataEscape7() throws Exception {
//        getPage("/faces/ajax/ajaxEcho.xhtml");
//
//        // First we'll check the first page was output correctly
//        checkTrue("form1:out1","");
//        checkTrue("form1:in1","");
//
//        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("form1:in1");
//
//        in1.type("var a=[");
//
//        // Submit the ajax request
//        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button1");
//        lastpage = (HtmlPage) button1.click();
//
//        // Check that the ajax request succeeds
//        checkTrue("form1:out1","var a=[");
//    }
//
//    public void testProjectStage() throws Exception {
//        getPage("/faces/ajax/ajaxProjectStage.xhtml");
//
//        // First we'll check the first page was output correctly
//        checkTrue("stage","Development");
//    }
//
//    public void testTextArea() throws Exception {
//        getPage("/faces/ajax/ajaxEchoArea.xhtml");
//
//        // First we'll check the first page was output correctly
//        checkTrue("form1:out1","");
//        checkTrue("form1:in1","");
//
//        HtmlTextArea in1 = (HtmlTextArea) lastpage.getHtmlElementById("form1:in1");
//
//        in1.type("test value");
//
//        // Submit the ajax request
//        HtmlButtonInput button1 = (HtmlButtonInput) lastpage.getHtmlElementById("form1:button1");
//        lastpage = (HtmlPage) button1.click();
//
//        // Check that the ajax request succeeds
//        checkTrue("form1:out1","test value");
//    }


}
