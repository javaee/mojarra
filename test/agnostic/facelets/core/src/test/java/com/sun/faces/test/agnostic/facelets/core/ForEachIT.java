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
 * https://glassfish.dev.java.net/public/CDDLGPL_1_1.html
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
package com.sun.faces.test.agnostic.facelets.core;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ForEachIT {

    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    @Test
    public void test_false_false_false_false_false_false_false_false_false() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_false_false_false_false.xhtml");

        assertTrue(page.asXml().matches("(?s).*<form.*>.*<input.+type=\"text\".+value=\"Bobby\".+disabled=\"disabled\".*>.*<input.+id=\"literalId\".+type=\"text\".+name=\"literalId\".+value=\"Bobby\".+disabled=\"disabled\".*/>.*<input.+type=\"text\".+value=\"Jerry\".+disabled=\"disabled\".*/>.*<input.+type=\"text\".+value=\"Jerry\".+disabled=\"disabled\".*/>.*<input.+type=\"text\".+value=\"Phil\".+disabled=\"disabled\".*/>.*<input.+type=\"text\".+value=\"Phil\".+disabled=\"disabled\".*/>.*</form>.*"));
        
        HtmlSubmitInput button = page.getHtmlElementById("submit");
        page = button.click();
        
        assertTrue(page.asXml().matches("(?s).*<form.*id=\"form01\".*name=\"form01\".*>.*<input.+type=\"text\".+name=\"j_idt[0-9]+\".+value=\"Bobby\".+disabled=\"disabled\".*/>.*<input.+id=\"literalId\".+type=\"text\".+name=\"literalId\".+value=\"Bobby\".+disabled=\"disabled\".*/>.*<input.+type=\"text\".+name=\"j_idt[0-9]+\".+value=\"Jerry\".+disabled=\"disabled\".*/>.*<input.+type=\"text\".+name=\"j_idt[0-9]+\".+value=\"Jerry\".+disabled=\"disabled\".*/>.*<input.+type=\"text\".+name=\"j_idt[0-9]+\".+value=\"Phil\".+disabled=\"disabled\".*/>.*<input.+type=\"text\".+name=\"j_idt[0-9]+\".+value=\"Phil\".+disabled=\"disabled\".*/>.*<input.+type=\"text\".+name=\"j_idt[0-9]+\".+value=\"Bobby\".+disabled=\"disabled\".*/>.*<input.+type=\"text\".+name=\"j_idt[0-9]+\".+value=\"Bobby\".+disabled=\"disabled\".*/>.*<input.+type=\"text\".+name=\"j_idt[0-9]+\".+value=\"Jerry\".+disabled=\"disabled\".*/>.*<input.+type=\"text\".+name=\"j_idt[0-9]+\".+value=\"Jerry\".+disabled=\"disabled\".*/>.*<input.+type=\"text\".+name=\"j_idt[0-9]+\".+value=\"Phil\".+disabled=\"disabled\".*/>.*<input.+type=\"text\".+name=\"j_idt[0-9]+\".+value=\"Phil\".+disabled=\"disabled\".*/>.*</form>.*"));
        
    }
    
    @Test
    public void test_false_false_false_false_false_false_false_false_true() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_false_false_false_true.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_false_false_false_true_false() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_false_false_true_false.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_false_false_false_true_true() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_false_false_true_true.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_false_false_true_false_false() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_false_true_false_false.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_false_false_true_false_true() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_false_true_false_true.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_false_false_true_true_false() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_false_true_true_false.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_false_false_true_true_true() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/true_true_true_true_true_true_true_true_true.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_false_true_false_false_false() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_true_false_false_false.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_false_true_false_false_true() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_true_false_false_true.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_false_true_false_true_false() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_true_false_true_false.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_false_true_false_true_true() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_true_false_true_true.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_false_true_true_false_false() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_true_true_false_false.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_false_true_true_false_true() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_true_true_false_true.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_false_true_true_true_false() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_true_true_true_false.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_false_true_true_true_true() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_false_true_true_true_true.xhtml");

        
    }


    @Test
    public void test_false_false_false_false_true_true_true_true_false() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_false_true_true_true_true_false.xhtml");

        
    }


    @Test
    public void test_false_false_false_true_false_true_false_true_true() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_false_true_false_true_false_true_true.xhtml");

        
    }


    @Test
    public void test_false_false_true_false_false_false_true_true_false() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_false_true_false_false_false_true_true_false.xhtml");

        
    }


    @Test
    public void test_false_true_false_false_false_true_false_true_false() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/false_true_false_false_false_true_false_true_false.xhtml");

        
    }


    @Test
    public void test_true_false_false_false_false_false_false_false_false() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/true_false_false_false_false_false_false_false_false.xhtml");

        
    }


    @Test
    public void test_true_true_true_true_true_true_true_true_true() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/forEach/index.xhtml");
                
        page = webClient.getPage(webUrl + "faces/forEach/true_true_true_true_true_true_true_true_true.xhtml");

        
    }



}
