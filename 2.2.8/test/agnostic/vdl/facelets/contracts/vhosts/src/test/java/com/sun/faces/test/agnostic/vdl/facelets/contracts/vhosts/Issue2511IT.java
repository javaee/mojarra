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
package com.sun.faces.test.agnostic.vdl.facelets.contracts.vhosts;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;
import com.sun.faces.test.junit.JsfVersion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import org.junit.runner.RunWith;

@RunWith(JsfTestRunner.class)
public class Issue2511IT {

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
    public void testResources() throws Exception {
        checkCss("defaultHost", null);
        checkCss("host1", "host1");
        checkCss("host2", null);
    }

    @Test
    public void testDefaultTemplate() throws Exception {
        webClient.removeRequestHeader("Host");
        HtmlPage page = webClient.getPage(webUrl + "faces/index.xhtml");
        DomElement footer = page.getElementById("footer");
        assertThat(footer, nullValue());
        DomElement content = page.getElementById("content");
        assertThat(content, notNullValue());
        assertThat(content.getTextContent().trim(), containsString("main content"));
    }

    @Test
    public void testAnotherTemplate() throws Exception {
        webClient.addRequestHeader("Host", "host2");
        HtmlPage page = webClient.getPage(webUrl + "faces/index.xhtml");
        HtmlElement host2content = page.getHtmlElementById("host2content");
        assertThat(host2content, notNullValue());
        HtmlElement footer = page.getHtmlElementById("footer");
        assertThat(footer, notNullValue());
        assertThat(footer.getTextContent().trim(), is("footer info"));
    }

    @Test
    public void testFalsePositive() throws Exception {
        webClient.addRequestHeader("Host", "host3");
        HtmlPage page = webClient.getPage(webUrl + "faces/index.xhtml");
        HtmlElement content = page.getHtmlElementById("content");
        assertThat(content, notNullValue());
        assertThat(content.getTextContent().trim(), is("false positive"));
    }

    @Test
    public void testInclude() throws Exception {
        webClient.removeRequestHeader("Host");
        HtmlPage page = webClient.getPage(webUrl + "faces/index.xhtml");
        DomElement header = page.getElementById("header");
        assertThat(header, notNullValue());
        assertThat(header.getTextContent().trim(), is("header content"));
        webClient.addRequestHeader("Host", "host1");
        page = webClient.getPage(webUrl + "faces/index.xhtml");
        header = page.getElementById("header");
        assertThat(header, nullValue());
    }

    @Test
    public void testExtension() throws Exception {
        webClient.addRequestHeader("Host", "host4");
        HtmlPage page = webClient.getPage(webUrl + "faces/index.xhtml");
        HtmlElement content = page.getHtmlElementById("host2content");
        assertThat(content, notNullValue());
        assertThat(content.getTextContent().trim(), is("host4 content"));
        HtmlElement footer = page.getHtmlElementById("footer");
        assertThat(footer, notNullValue());
        assertThat(footer.getTextContent().trim(), is(""));
    }

    @JsfTest(JsfVersion.JSF_2_2_1)
    @Test
    public void testCompositeComponent() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/index.xhtml");

        HtmlElement template = page.getHtmlElementById("ccTemplate");
        assertThat(template, notNullValue());
        assertThat(template.getTextContent().trim(), is("lib/2_3/template.xhtml"));

        HtmlElement content = page.getHtmlElementById("ccContent");
        assertThat(content, notNullValue());
        assertThat(content.getTextContent().trim(), containsString("lib/2_3/cc.xhtml"));

        webClient.addRequestHeader("Host", "host1");
        page = webClient.getPage(webUrl + "faces/index.xhtml");

        content = page.getHtmlElementById("ccContent");
        assertThat(content, notNullValue());
        assertThat(content.getTextContent().trim(), containsString("host1/lib/cc.xhtml"));

    }

    private void checkCss(String host, String contract) throws IOException {
        webClient.addRequestHeader("Host", host);
        HtmlPage page = webClient.getPage(webUrl + "faces/index.xhtml");
        String titleText = page.getTitleText();
        assertThat(titleText, is(host));
        DomNodeList<DomElement> linkElements = page.getElementsByTagName("link");
        for (DomElement linkElement : linkElements) {
            HtmlLink link = (HtmlLink) linkElement;
            if(contract == null) {
                assertThat(link.getHrefAttribute(), not(containsString("con=")));
            } else {
                assertThat(link.getHrefAttribute(), containsString("con=" + contract));
            }
        }
    }
}
