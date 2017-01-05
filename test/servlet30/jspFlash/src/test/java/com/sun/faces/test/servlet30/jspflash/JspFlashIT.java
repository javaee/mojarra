/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package com.sun.faces.test.servlet30.jspflash;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_1_4;
import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_2_1;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;
import com.sun.faces.test.junit.JsfVersion;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JsfTestRunner.class)
public class JspFlashIT {

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

    @JsfTest(value = JsfVersion.JSF_2_3_0_M02, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    @Test
    public void testFlash() throws Exception {
        // Get the first page
        HtmlPage page = webClient.getPage(webUrl + "home-flash.jsf");
        String pageText = page.asXml();
        // (?s) is an "embedded flag expression" for the "DOTALL" operator.
        // It says, "let . match any character including line terminators."
        // Because page.asXml() returns a big string with lots of \r\n chars
        // in it, we need (?s).
        // the page contains a table tag with a frame attribute whose value is hsides.

        // the page contains the following span, with the following id, with no contents
        assertTrue(pageText.matches("(?s)(?m).*<span.*id=\"fooValueId\">\\s*</span>.*"));

        // Click the reload button
        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("reload");
        page = (HtmlPage) button.click();
        pageText = page.asXml();
        // verify that fooValue is there, indicating that it's been stored in the flash
        assertTrue(pageText.matches("(?s)(?m).*<span.*id=\"fooValueId\">\\s*fooValue\\s*</span>.*"));

        // Get the first page, again
        page = webClient.getPage(webUrl + "home-flash.jsf");

        // fill in "addMessage" in the textBox
        HtmlTextInput text = (HtmlTextInput) page.getHtmlElementById("inputText");
        text.setValueAttribute("addMessage");

        // go to the next page
        button = (HtmlSubmitInput) page.getHtmlElementById("next");
        page = (HtmlPage) button.click();
        pageText = page.asXml();
        // See that it has fooValue
        assertTrue(pageText.matches("(?s)(?m).*<span.*id=\"flash2FooValueId\">\\s*fooValue\\s*</span>.*"));
        // See that it has barValue
        assertTrue(pageText.matches("(?s)(?m).*<span.*id=\"flash2BarValueId\">\\s*barValue\\s*</span>.*"));
        // See that it has the message
        assertTrue(-1 != pageText.indexOf("test that this persists across the redirect"));

        // click the reload button
        button = (HtmlSubmitInput) page.getHtmlElementById("reload");
        page = (HtmlPage) button.click();
        pageText = page.asXml();

        // See that it doesn't have the message
        assertTrue(-1 == pageText.indexOf("test that this persists across the redirect"));

        // Click the back button
        button = (HtmlSubmitInput) page.getHtmlElementById("back");
        page = (HtmlPage) button.click();
        pageText = page.asXml();

        // Click the next button
        button = (HtmlSubmitInput) page.getHtmlElementById("next");
        page = (HtmlPage) button.click();
        pageText = page.asXml();

        // See that the page does not have the message
        assertTrue(-1 == pageText.indexOf("test that this persists across the redirect"));

        // Click the next button
        button = (HtmlSubmitInput) page.getHtmlElementById("next");
        page = (HtmlPage) button.click();
        pageText = page.asXml();

        // See that it has banzai
        assertTrue(pageText.matches("(?s)(?m).*<span.*id=\"flash3NowValueId\">\\s*banzai\\s*</span>.*"));

        // Click the next button
        button = (HtmlSubmitInput) page.getHtmlElementById("next");
        page = (HtmlPage) button.click();
        pageText = page.asXml();

        // See that it still has banzai
        assertTrue(pageText.matches("(?s)(?m).*<span.*id=\"flash4BuckarooValueId\">\\s*banzai\\s*</span>.*"));
    }
}
