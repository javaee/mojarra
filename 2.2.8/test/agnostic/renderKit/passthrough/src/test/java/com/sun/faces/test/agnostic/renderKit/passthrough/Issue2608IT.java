/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.test.agnostic.renderKit.passthrough;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

public class Issue2608IT {
    
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

    // This tests the following markup:
    // <h:inputText id="input1" type="text" p:placeholder="Enter text here" />
    //
    @Test
    public void testInputTextPlaceholder() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/input1.xhtml");
        HtmlElement input = page.getHtmlElementById("input1");
        String xml = input.asXml();
        assertTrue(xml.contains("<input"));
        assertTrue(xml.contains("id=\"" + "input1" + "\""));
        assertTrue(xml.contains("type=\"" + "text" + "\""));
        assertTrue(xml.contains("name=\"" + "input1" + "\""));
        assertTrue(xml.contains("placeholder=\"" + "Enter text here" + "\""));
    } 

    // This tests the following markup:
    // <h:inputText id="input2" type="text" p:autocomplete="on" />
    //
    @Test
    public void testInputTextAutocomplete() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/input1.xhtml");
        HtmlElement input = page.getHtmlElementById("input2");
        String xml = input.asXml();
        assertTrue(xml.contains("<input"));
        assertTrue(xml.contains("id=\"" + "input2" + "\""));
        assertTrue(xml.contains("type=\"" + "text" + "\""));
        assertTrue(xml.contains("name=\"" + "input2" + "\""));
        assertTrue(xml.contains("autocomplete=\"" + "on" + "\""));
    } 

    // This tests the following markup:
    // <h:inputText id="input3" type="text" p:autofocus="autofocus" />
    //
    @Test
    public void testInputTextAutofocus() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/input1.xhtml");
        HtmlElement input = page.getHtmlElementById("input3");
        String xml = input.asXml();
        assertTrue(xml.contains("<input"));
        assertTrue(xml.contains("id=\"" + "input3" + "\""));
        assertTrue(xml.contains("type=\"" + "text" + "\""));
        assertTrue(xml.contains("name=\"" + "input3" + "\""));
        assertTrue(xml.contains("autofocus=\"" + "autofocus" + "\""));
    } 

    // This tests the following markup:
    // <h:inputText id="input4" type="text" p:list="mydatalist" />
    //
    @Test
    public void testInputTextList() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/input1.xhtml");
        HtmlElement input = page.getHtmlElementById("input4");
        String xml = input.asXml();
        assertTrue(xml.contains("<input"));
        assertTrue(xml.contains("id=\"" + "input4" + "\""));
        assertTrue(xml.contains("type=\"" + "text" + "\""));
        assertTrue(xml.contains("name=\"" + "input4" + "\""));
        assertTrue(xml.contains("list=\"" + "mydatalist" + "\""));
    } 

    // This tests the following markup:
    // <h:inputText id="input5" type="text" p:pattern="[A-Za-z]{3}" />
    //
    @Test
    public void testInputTextPattern() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/input1.xhtml");
        HtmlElement input = page.getHtmlElementById("input5");
        String xml = input.asXml();
        assertTrue(xml.contains("<input"));
        assertTrue(xml.contains("id=\"" + "input5" + "\""));
        assertTrue(xml.contains("type=\"" + "text" + "\""));
        assertTrue(xml.contains("name=\"" + "input5" + "\""));
        assertTrue(xml.contains("pattern=\"" + "[A-Za-z]{3}" + "\""));
    } 

    // This tests the following markup:
    // <h:inputText id="input6" type="text" p:required="required" />
    //
    @Test
    public void testInputTextRequired() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/input1.xhtml");
        HtmlElement input = page.getHtmlElementById("input6");
        String xml = input.asXml();
        assertTrue(xml.contains("<input"));
        assertTrue(xml.contains("id=\"" + "input6" + "\""));
        assertTrue(xml.contains("type=\"" + "text" + "\""));
        assertTrue(xml.contains("name=\"" + "input6" + "\""));
        assertTrue(xml.contains("required=\"" + "required" + "\""));
    } 

    // This tests the following markup:
    // <h:inputText id="input7" type="text" p:dirname="input7.dir" />
    //
    @Test
    public void testInputTextDirname() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/input1.xhtml");
        HtmlElement input = page.getHtmlElementById("input7");
        String xml = input.asXml();
        assertTrue(xml.contains("<input"));
        assertTrue(xml.contains("id=\"" + "input7" + "\""));
        assertTrue(xml.contains("type=\"" + "text" + "\""));
        assertTrue(xml.contains("name=\"" + "input7" + "\""));
        assertTrue(xml.contains("dirname=\"" + "input7.dir" + "\""));
    } 
}
