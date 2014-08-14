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

package com.sun.faces.test.agnostic.renderKit.passthrough;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlFieldSet;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;

public class Issue2606IT {
    
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
    public void testArticle() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/article.xhtml");
        HtmlElement article1 = page.getHtmlElementById("article1");
        String xml = article1.asXml();
        assertTrue(xml.contains("<article"));
        assertTrue(xml.contains("id=\"" + "article1" + "\""));

        HtmlElement article2 = page.getHtmlElementById("article2");
        xml = article2.asXml();
        assertTrue(xml.contains("<article"));
        assertTrue(xml.contains("id=\"" + "article2" + "\""));

        webClient.setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(60000);
        page = (HtmlPage)article2.mouseOver();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("article2 Event: begin"));
        assertTrue(page.asXml().contains("article2 Event: complete"));
        assertTrue(page.asXml().contains("article2 Event: success"));
    } 

    @Test
    public void testAside() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/aside.xhtml");
        HtmlElement aside1 = page.getHtmlElementById("aside1");
        String xml = aside1.asXml();
        assertTrue(xml.contains("<aside"));
        assertTrue(xml.contains("id=\"" + "aside1" + "\""));

        HtmlElement aside2 = page.getHtmlElementById("aside2");
        xml = aside2.asXml();
        assertTrue(xml.contains("<aside"));
        assertTrue(xml.contains("id=\"" + "aside2" + "\""));

        webClient.setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(60000);
        page = (HtmlPage)aside2.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("aside2 Event: begin"));
        assertTrue(page.asXml().contains("aside2 Event: complete"));
        assertTrue(page.asXml().contains("aside2 Event: success"));
    }

    @Test
    public void testNav() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/nav.xhtml");
        HtmlElement nav1 = page.getHtmlElementById("nav1");
        String xml = nav1.asXml();
        assertTrue(xml.contains("<nav"));
        assertTrue(xml.contains("id=\"" + "nav1" + "\""));

        HtmlElement nav2 = page.getHtmlElementById("nav2");
        xml = nav2.asXml();
        assertTrue(xml.contains("<nav"));
        assertTrue(xml.contains("id=\"" + "nav2" + "\""));

        webClient.setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(60000);
        page = (HtmlPage)nav2.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("nav2 Event: begin"));
        assertTrue(page.asXml().contains("nav2 Event: complete"));
        assertTrue(page.asXml().contains("nav2 Event: success"));
    }

    @Test
    public void testSection() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/section.xhtml");
        HtmlElement section1 = page.getHtmlElementById("section1");
        String xml = section1.asXml();
        assertTrue(xml.contains("<section"));
        assertTrue(xml.contains("id=\"" + "section1" + "\""));

        HtmlElement section2 = page.getHtmlElementById("section2");
        xml = section2.asXml();
        assertTrue(xml.contains("<section"));
        assertTrue(xml.contains("id=\"" + "section2" + "\""));

        webClient.setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(60000);
        page = (HtmlPage)section2.mouseOver();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("section2 Event: begin"));
        assertTrue(page.asXml().contains("section2 Event: complete"));
        assertTrue(page.asXml().contains("section2 Event: success"));
    }

    @Test
    public void testHeaders() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/h1h2h3h4h5h6.xhtml");
        HtmlElement h1 = page.getHtmlElementById("header1");
        String xml = h1.asXml();
        assertTrue(xml.contains("<h1"));
        assertTrue(xml.contains("id=\"" + "header1" + "\""));

        HtmlElement h2 = page.getHtmlElementById("header2");
        xml = h2.asXml();
        assertTrue(xml.contains("<h2"));
        assertTrue(xml.contains("id=\"" + "header2" + "\""));

        webClient.setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(60000);
        page = (HtmlPage)h2.mouseOver();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("header2 Event: begin"));
        assertTrue(page.asXml().contains("header2 Event: complete"));
        assertTrue(page.asXml().contains("header2 Event: success"));

        HtmlElement h3 = page.getHtmlElementById("header3");
        xml = h3.asXml();
        assertTrue(xml.contains("<h3"));
        assertTrue(xml.contains("id=\"" + "header3" + "\""));

        HtmlElement h4 = page.getHtmlElementById("header4");
        xml = h4.asXml();
        assertTrue(xml.contains("<h4"));
        assertTrue(xml.contains("id=\"" + "header4" + "\""));

        HtmlElement h5 = page.getHtmlElementById("header5");
        xml = h5.asXml();
        assertTrue(xml.contains("<h5"));
        assertTrue(xml.contains("id=\"" + "header5" + "\""));

        HtmlElement h6 = page.getHtmlElementById("header6");
        xml = h6.asXml();
        assertTrue(xml.contains("<h6"));
        assertTrue(xml.contains("id=\"" + "header6" + "\""));
    }

    @Test
    public void testHeaderGroup() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "/faces/hgroup.xhtml");
        HtmlElement hgroup = page.getHtmlElementById("hgroup");
        String xml = hgroup.asXml();
        assertTrue(xml.contains("<hgroup"));
        assertTrue(xml.contains("id=\"" + "hgroup" + "\""));
    }

}
