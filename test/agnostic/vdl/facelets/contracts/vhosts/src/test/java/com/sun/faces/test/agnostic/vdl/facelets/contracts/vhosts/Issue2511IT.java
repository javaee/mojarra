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
package com.sun.faces.test.agnostic.vdl.facelets.contracts.vhosts;

import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class Issue2511IT {

    private String contextRoot;
    private WebClient webClient;

    @Before
    public void setUp() {
        String webUrl = System.getProperty("integration.url");

        webClient = new WebClient();
        try {
            // the easy way to set the host header with html-unit (works also fine with firefox)
            URL url = new URL(webUrl);
            webClient.setProxyConfig(new ProxyConfig(url.getHost(), url.getPort()));
            contextRoot = url.getPath();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    private String buildUrl(String host, String path) {
        return "http://" + host + contextRoot + path;
    }

    @Test
    public void testResources() throws Exception {
        checkCss("defaultHost", null);
        checkCss("host1", "host1");
        checkCss("host2", null);
    }

    @Test
    public void testDefaultTemplate() throws Exception {
        HtmlPage page = webClient.getPage(buildUrl("defaultHost", "faces/index.xhtml"));
        HtmlElement footer = page.getElementById("footer");
        assertThat(footer, nullValue());

        HtmlElement content = page.getElementById("content");
        assertThat(content, notNullValue());

        assertThat(content.getTextContent().trim(), is("main content"));
    }

    @Test
    public void testAnotherTemplate() throws Exception {
        HtmlPage page = webClient.getPage(buildUrl("host2", "faces/index.xhtml"));
        HtmlElement host2content = page.getElementById("host2content");
        assertThat(host2content, notNullValue());
        HtmlElement footer = page.getElementById("footer");
        assertThat(footer, notNullValue());
        assertThat(footer.getTextContent().trim(), is("footer info"));
    }

    @Test
    public void testFalsePositive() throws Exception {
        HtmlPage page = webClient.getPage(buildUrl("host3", "faces/index.xhtml"));
        HtmlElement content = page.getElementById("content");
        assertThat(content, notNullValue());
        assertThat(content.getTextContent().trim(), is("false positive"));
    }

    @Test
    public void testInclude() throws Exception {
        HtmlPage page = webClient.getPage(buildUrl("defaultHost", "faces/index.xhtml"));
        HtmlElement header = page.getElementById("header");
        assertThat(header, notNullValue());
        assertThat(header.getTextContent().trim(), is("header content"));

        page = webClient.getPage(buildUrl("host1", "faces/index.xhtml"));
        header = page.getElementById("header");
        // host1 has an empty header include!
        assertThat(header, nullValue());
    }

    @Test
    public void testExtension() throws Exception {
        HtmlPage page = webClient.getPage(buildUrl("host4", "faces/index.xhtml"));
        HtmlElement content = page.getElementById("host2content");
        // we use the template of host 2 but insert our content
        assertThat(content, notNullValue());
        assertThat(content.getTextContent().trim(), is("host4 content"));

        // we use the template of host2, but we don't define the footer.
        HtmlElement footer = page.getElementById("footer");
        assertThat(footer, notNullValue());
        assertThat(footer.getTextContent().trim(), is(""));
    }

    private void checkCss(String host, String contract) throws IOException {
        HtmlPage page = webClient.getPage(buildUrl(host, "faces/index.xhtml"));
        String titleText = page.getTitleText();
        assertThat(titleText, is(host));

        DomNodeList<HtmlElement> linkElements = page.getElementsByTagName("link");

        for (HtmlElement linkElement : linkElements) {
            HtmlLink link = (HtmlLink) linkElement;
            // make sure no contract is chosen
            if(contract == null) {
                assertThat(link.getHrefAttribute(), not(containsString("con=")));
            } else {
                assertThat(link.getHrefAttribute(), containsString("con=" + contract));
            }

        }
    }

}
