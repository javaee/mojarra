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
package com.sun.faces.test.agnostic.renderKit.passthrough;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.util.Arrays;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

public class Issue2605IT {

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
    public void testFieldset() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/fieldset.xhtml");
        HtmlElement elem = page.getHtmlElementById("fieldset1");
        String xml = elem.asXml();
        assertTrue(xml.contains("<fieldset"));
        assertTrue(xml.contains("id=\"" + "fieldset1" + "\""));

        elem = page.getHtmlElementById("fieldset2");
        xml = elem.asXml();
        assertTrue(xml.contains("<fieldset"));
        assertTrue(xml.contains("id=\"" + "fieldset2" + "\""));
        assertTrue(xml.contains("disabled=\"" + "disabled" + "\""));

        elem = page.getHtmlElementById("fieldset3");
        xml = elem.asXml();
        assertTrue(xml.contains("<fieldset"));
        assertTrue(xml.contains("id=\"" + "fieldset3" + "\""));
        assertTrue(xml.contains("form=\"" + "form" + "\""));
        assertTrue(xml.contains("name=\"" + "myfieldset" + "\""));
    }

    @Test
    public void testMeter() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/meter.xhtml");
        HtmlElement elem = page.getHtmlElementById("meter1");
        String xml = elem.asXml();
        assertTrue(xml.contains("<meter"));
        assertTrue(xml.contains("id=\"" + "meter1" + "\""));
        assertTrue(xml.contains("min=\"" + "200" + "\""));
        assertTrue(xml.contains("max=\"" + "500" + "\""));
        assertTrue(xml.contains("value=\"" + "350" + "\""));

        elem = page.getHtmlElementById("meter2");
        xml = elem.asXml();
        assertTrue(xml.contains("<meter"));
        assertTrue(xml.contains("id=\"" + "meter2" + "\""));
        assertTrue(xml.contains("min=\"" + "100" + "\""));
        assertTrue(xml.contains("max=\"" + "500" + "\""));
        assertTrue(xml.contains("value=\"" + "350" + "\""));
    }

    @Test
    public void testLabel() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/label.xhtml");
        HtmlElement elem = page.getHtmlElementById("label1");
        String xml = elem.asXml();
        assertTrue(xml.contains("<label"));
        assertTrue(xml.contains("id=\"" + "label1" + "\""));
        assertTrue(xml.contains("form=\"" + "form" + "\""));
        assertTrue(xml.contains("for=\"" + "input1" + "\""));
    }

    @Test
    public void testDataList() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/datalist.xhtml");
        HtmlElement elem = page.getHtmlElementById("colors");
        String xml = elem.asXml();
        assertTrue(xml.contains("<datalist"));
        assertTrue(xml.contains("id=\"" + "colors" + "\""));
        WebResponse resp = page.getWebResponse();
        String text = resp.getContentAsString();
        assertTrue(text.contains("<option id="+'"'+"r"+'"'+" value="+'"'+"red"+'"'));
        assertTrue(text.contains("<option id="+'"'+"b"+'"'+" value="+'"'+"blue"+'"'));
        assertTrue(text.contains("<option id="+'"'+"g"+'"'+" value="+'"'+"green"+'"'));
    }

    @Test
    public void testOutput() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/output.xhtml");
        HtmlElement elem = page.getHtmlElementById("output1");
        String xml = elem.asXml();
        assertTrue(xml.contains("<output"));
        assertTrue(xml.contains("id=\"" + "output1" + "\""));
    }

    @Test
    public void testKeygen() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/keygen.xhtml");
        HtmlElement elem = page.getHtmlElementById("keygen1");
        String xml = elem.asXml();
        assertTrue(xml.contains("<keygen"));
        assertTrue(xml.contains("id=\"" + "keygen1" + "\""));
    }
}
