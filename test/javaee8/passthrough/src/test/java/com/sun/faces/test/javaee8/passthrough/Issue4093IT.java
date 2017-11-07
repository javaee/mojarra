/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.faces.test.javaee8.passthrough;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.test.junit.JsfTestRunner;
import static org.junit.Assert.assertFalse;

@RunWith(JsfTestRunner.class)
public class Issue4093IT {

    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(120000);
    }

    @Test
    public void testSpec4093RequiredWithoutPassthrough() throws Exception {

        HtmlPage page = webClient.getPage(webUrl + "issue4093.xhtml");
        HtmlTextInput input = (HtmlTextInput) page.getElementById("requiredwithoutpassthrough:value");
        input.setAttribute("value", "");


	HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("requiredwithoutpassthrough:submit");

        page = button.click();

        String output = page.asText();

        assertTrue(output.contains("requiredwithoutpassthrough:value: Validation Error: Value is required."));

    }
    
    @Test
    public void testSpec4093RequiredWithPassthrough() throws Exception {

        HtmlPage page = webClient.getPage(webUrl + "issue4093.xhtml");
        HtmlTextInput input = (HtmlTextInput) page.getElementById("requiredwithpassthrough:value");
        input.setAttribute("value", "");


	HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("requiredwithpassthrough:submit");

        page = button.click();

        String output = page.asText();

        assertFalse(output.contains("Please fill out this field"));

    }
    
    @Test
    public void testSpec4093ValidateWithoutPassthrough() throws Exception {

        HtmlPage page = webClient.getPage(webUrl + "issue4093.xhtml");
        HtmlTextInput input = (HtmlTextInput) page.getElementById("validatewithoutpassthrough:value");
        input.setAttribute("value", "");


	HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("validatewithoutpassthrough:submit");

        page = button.click();

        String output = page.asText();

        assertTrue(output.contains("validatewithoutpassthrough:value: Validation Error: Value is required."));

    }
    
    /**
     * This test should yield no JSF message response, as the inputText component
     * is using passthrough to HTML.
     * @throws Exception 
     */
    @Test
    public void testSpec4093ValidateWithPassthrough() throws Exception {

        HtmlPage page = webClient.getPage(webUrl + "issue4093.xhtml");
        HtmlTextInput input = (HtmlTextInput) page.getElementById("validatewithpassthrough:value");
        input.setAttribute("value", "");


	HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("validatewithpassthrough:submit");

        page = button.click();

        String output = page.asText();

        assertFalse(output.contains("Please fill out this field"));

    }
    
    /**
     * This test should yield no JSF message response, as the inputText component
     * is using passthrough to HTML.
     * @throws Exception 
     */
    @Test
    public void testSpec4093ValidateWithPassthroughId() throws Exception {

        HtmlPage page = webClient.getPage(webUrl + "issue4093.xhtml");
        HtmlTextInput input = (HtmlTextInput) page.getElementById("validatewithpassthrough:value");
        input.setAttribute("value", "");


	HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("validatewithpassthrough:submit");

        page = button.click();

        String output = page.asText();

        assertFalse(output.contains("Please fill out this field"));

    }

    @After
    public void tearDown() {
        webClient.close();
    }

}
