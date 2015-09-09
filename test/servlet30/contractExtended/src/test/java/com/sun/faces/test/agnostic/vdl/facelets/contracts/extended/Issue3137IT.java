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
package com.sun.faces.test.agnostic.vdl.facelets.contracts.extended;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class Issue3137IT {

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
    public void testInitialContractIsJarbase() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/index.xhtml");

        // start with initial jarbase contract
        String text = page.asText();
        assertTrue(text.contains("\"jarbase\" template header"));
        assertTrue(text.contains("from \"jarbase\" subtemplate.xhtml"));
        assertTrue(text.contains("resolved contracts: [jarbase]"));
        assertContractForCss(page, "contract.css", "jarbase");
        assertContractForCss(page, "cssLayout.css", "jarbase");

    }

    @Test
    public void testWarbaseContract() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/index.xhtml");

        // switch to warbase contract
        HtmlSelect selectOne = (HtmlSelect) page.getElementById("selectOne");
        selectOne.setSelectedAttribute("warbase", true);
        HtmlSubmitInput apply = (HtmlSubmitInput) page.getElementById("apply");
        page = apply.click();

        String text = page.asText();
        assertTrue(text.contains("\"warbase\" template header"));
        assertTrue(text.contains("from \"warbase\" subtemplate.xhtml"));
        assertTrue(text.contains("resolved contracts: [warbase]"));
        assertContractForCss(page, "contract.css", "warbase");
        assertContractForCss(page, "cssLayout.css", "warbase");

    }

    @Test
    public void testRedExtendsWarbaseOrJarBaseContract() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/index.xhtml");

        // switch to red,warbase contract
        HtmlSelect selectOne = (HtmlSelect) page.getElementById("selectOne");
        selectOne.setSelectedAttribute("red,warbase", true);
        HtmlSubmitInput apply = (HtmlSubmitInput) page.getElementById("apply");
        page = apply.click();

        String text = page.asText();
        assertTrue(text.contains("\"red\" template header"));
        assertTrue(text.contains("from \"warbase\" subtemplate.xhtml"));
        assertTrue(text.contains("resolved contracts: [red, warbase]"));
        assertContractForCss(page, "contract.css", "red");
        assertContractForCss(page, "cssLayout.css", "warbase");

        // switch to red,jarbase contract
        selectOne = (HtmlSelect) page.getElementById("selectOne");
        selectOne.setSelectedAttribute("red,jarbase", true);
        apply = (HtmlSubmitInput) page.getElementById("apply");
        page = apply.click();

        text = page.asText();
        assertTrue(text.contains("\"red\" template header"));
        assertTrue(text.contains("from \"jarbase\" subtemplate.xhtml"));
        assertTrue(text.contains("resolved contracts: [red, jarbase]"));
        assertContractForCss(page, "contract.css", "red");
        assertContractForCss(page, "cssLayout.css", "jarbase");

    }

    @Test
    public void testBlueExtendsWarbaseOrJarBaseContract() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/index.xhtml");

        // switch to blue,warbase contract
        HtmlSelect selectOne = (HtmlSelect) page.getElementById("selectOne");
        selectOne.setSelectedAttribute("blue,warbase", true);
        HtmlSubmitInput apply = (HtmlSubmitInput) page.getElementById("apply");
        page = apply.click();

        String text = page.asText();
        assertTrue(text.contains("\"blue\" template header"));
        assertTrue(text.contains("from \"warbase\" subtemplate.xhtml"));
        assertTrue(text.contains("resolved contracts: [blue, warbase]"));
        assertContractForCss(page, "contract.css", "blue");
        assertContractForCss(page, "cssLayout.css", "warbase");

        // switch to blue,jarbase contract
        selectOne = (HtmlSelect) page.getElementById("selectOne");
        selectOne.setSelectedAttribute("blue,jarbase", true);
        apply = (HtmlSubmitInput) page.getElementById("apply");
        page = apply.click();

        text = page.asText();
        assertTrue(text.contains("\"blue\" template header"));
        assertTrue(text.contains("from \"jarbase\" subtemplate.xhtml"));
        assertTrue(text.contains("resolved contracts: [blue, jarbase]"));
        assertContractForCss(page, "contract.css", "blue");
        assertContractForCss(page, "cssLayout.css", "jarbase");

    }

    @Test
    public void testGreenExtendsWarbaseOrJarBaseContract() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/index.xhtml");

        // switch to green,warbase contract
        HtmlSelect selectOne = (HtmlSelect) page.getElementById("selectOne");
        selectOne.setSelectedAttribute("green,warbase", true);
        HtmlSubmitInput apply = (HtmlSubmitInput) page.getElementById("apply");
        page = apply.click();

        String text = page.asText();
        assertTrue(text.contains("\"green\" template header"));
        assertTrue(text.contains("from \"warbase\" subtemplate.xhtml"));
        assertTrue(text.contains("resolved contracts: [green, warbase]"));
        assertContractForCss(page, "contract.css", "green");
        assertContractForCss(page, "cssLayout.css", "warbase");

        // switch to green,jarbase contract
        selectOne = (HtmlSelect) page.getElementById("selectOne");
        selectOne.setSelectedAttribute("green,jarbase", true);
        apply = (HtmlSubmitInput) page.getElementById("apply");
        page = apply.click();

        text = page.asText();
        assertTrue(text.contains("\"green\" template header"));
        assertTrue(text.contains("from \"jarbase\" subtemplate.xhtml"));
        assertTrue(text.contains("resolved contracts: [green, jarbase]"));
        assertContractForCss(page, "contract.css", "green");
        assertContractForCss(page, "cssLayout.css", "jarbase");

    }

    @Test
    public void testUserPathCalculatedContracts() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/user/index.xhtml");

        // expect contracts blue,jarbase
        String text = page.asText();
        assertTrue(text.contains("\"blue\" template header"));
        assertTrue(text.contains("from \"jarbase\" subtemplate.xhtml"));
        assertTrue(text.contains("resolved contracts: [blue, jarbase]"));
        assertContractForCss(page, "contract.css", "blue");
        assertContractForCss(page, "cssLayout.css", "jarbase");

    }

    @Test
    public void testRedPathCalculatedContracts() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/red/index.xhtml");

        // expect contracts red,jarbase
        String text = page.asText();
        assertTrue(text.contains("\"red\" template header"));
        assertTrue(text.contains("from \"jarbase\" subtemplate.xhtml"));
        assertTrue(text.contains("resolved contracts: [red, jarbase]"));
        assertContractForCss(page, "contract.css", "red");
        assertContractForCss(page, "cssLayout.css", "jarbase");

    }

    @Test
    public void testGreenPathCalculatedContracts() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/green/index.xhtml");

        // expect contracts green,jarbase
        String text = page.asText();
        assertTrue(text.contains("\"green\" template header"));
        assertTrue(text.contains("from \"jarbase\" subtemplate.xhtml"));
        assertTrue(text.contains("resolved contracts: [green, jarbase]"));
        assertContractForCss(page, "contract.css", "green");
        assertContractForCss(page, "cssLayout.css", "jarbase");

    }

    private void assertContractForCss(HtmlPage page, String resourceName,
            String expectedContract) {
        DomNodeList<DomElement> links = page.getElementsByTagName("link");
        for (DomElement cur : links) {
            HtmlLink link = (HtmlLink) cur;
            String href = link.getHrefAttribute();
            if (href.contains(resourceName)) {
                String query = href.substring(href.indexOf("?") + 1);
                String[] parts = query.split("&");
                for (String part : parts) {
                    String[] kv = part.split("=");
                    if ("con".equals(kv[0])) {
                        assertEquals("examined link href=" + href,
                                expectedContract, kv[1]);
                        return;
                    }
                }
            }
        }
        fail("Could not find link for resource '" + resourceName + "'!");
    }

}
