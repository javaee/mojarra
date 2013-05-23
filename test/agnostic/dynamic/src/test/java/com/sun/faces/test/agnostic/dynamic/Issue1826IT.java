/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU General
 * Public License Version 2 only ("GPL") or the Common Development and
 * Distribution License("CDDL") (collectively, the "License"). You may not use
 * this file except in compliance with the License. You can obtain a copy of the
 * License at https://glassfish.dev.java.net/public/CDDLGPL_1_1.html or
 * packager/legal/LICENSE.txt. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception: Oracle designates this particular file as subject to
 * the "Classpath" exception as provided by Oracle in the GPL Version 2 section
 * of the License file that accompanied this code.
 *
 * Modifications: If applicable, add the following below the License Header,
 * with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s): If you wish your version of this file to be governed by only
 * the CDDL or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution under the
 * [CDDL or GPL Version 2] license." If you don't indicate a single choice of
 * license, a recipient has the option to distribute your version of this file
 * under either the CDDL, the GPL Version 2 or to extend the choice of license
 * to its licensees as provided above. However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright holder.
 */
package com.sun.faces.test.agnostic.dynamic;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class Issue1826IT {

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
    public void testAddComponent() throws Exception {

        /*
         * Make sure the added component is contained within the outer component. 
         */
        HtmlPage page = webClient.getPage(webUrl + "faces/add.xhtml");
        assertTrue(page.asXml().indexOf("encodeBegin") < page.asXml().indexOf(" Dynamically added child"));
        assertTrue(page.asXml().indexOf(" Dynamically added child") < page.asXml().indexOf("encodeEnd"));

        /**
         * After clicking make sure the added component is still in its proper place.
         */
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("button");
        page = button.click();
        assertTrue(page.asXml().indexOf("encodeBegin") < page.asXml().indexOf(" Dynamically added child"));
        assertTrue(page.asXml().indexOf(" Dynamically added child") < page.asXml().indexOf("encodeEnd"));
    }

    @Test
    public void testStable() throws Exception {

         String inputValue1 = "value=" + '"' + "1" + '"';
         String inputValue2 = "value=" + '"' + "2" + '"';
         String idText3 = "id=" + '"' + "text3" + '"';

        /*
         * Make sure the three dynamically added input components are in their proper place. 
         */
        HtmlPage page = webClient.getPage(webUrl + "faces/stable.xhtml");
        assertTrue(page.asXml().indexOf("encodeBegin") < page.asXml().indexOf(inputValue1));
        assertTrue(page.asXml().indexOf(inputValue1) < page.asXml().indexOf(inputValue2));
        assertTrue(page.asXml().indexOf(inputValue2) < page.asXml().indexOf(idText3));
        assertTrue(page.asXml().indexOf("text3") < page.asXml().indexOf("encodeEnd"));

        /**
         * After clicking make sure the added component is still in its proper place.
         * Also verify the validation required error message appears as the third input
         * component has required attribute set to true.
         */
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("button");
        page = button.click();
        assertTrue(page.asXml().contains("text3: Validation Error: Value is required."));
        assertTrue(page.asXml().indexOf("encodeBegin") < page.asXml().indexOf(inputValue1));
        assertTrue(page.asXml().indexOf(inputValue1) < page.asXml().indexOf(inputValue2));
        assertTrue(page.asXml().indexOf(inputValue2) < page.asXml().indexOf(idText3));
        assertTrue(page.asXml().indexOf("text3") < page.asXml().indexOf("encodeEnd"));
    }

    @Test
    public void testTable() throws Exception {

        /*
         * Make sure the dynamically added table and table values are in place.
         */
        HtmlPage page = webClient.getPage(webUrl + "faces/table.xhtml");
        assertTrue(page.asXml().indexOf("encodeBegin") < page.asXml().indexOf("Foo"));
        assertTrue(page.asXml().indexOf("Foo") < page.asXml().indexOf("Bar"));
        assertTrue(page.asXml().indexOf("Bar") < page.asXml().indexOf("Baz"));
        assertTrue(page.asXml().indexOf("Baz") < page.asXml().indexOf("encodeEnd"));

        /**
         * After clicking make sure the added component is still in its proper place.
         * Also verify the validation required error message appears as the third input
         * component has required attribute set to true.
         */
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("button");
        page = button.click();
        assertTrue(page.asXml().indexOf("encodeBegin") < page.asXml().indexOf("Foo"));
        assertTrue(page.asXml().indexOf("Foo") < page.asXml().indexOf("Bar"));
        assertTrue(page.asXml().indexOf("Bar") < page.asXml().indexOf("Baz"));
        assertTrue(page.asXml().indexOf("Baz") < page.asXml().indexOf("encodeEnd"));
    }

    @Ignore
    @Test
    public void testRecursive() throws Exception {
        String str = "Component::encodeBegin" + "\n" + "Dynamically added child" + "\n" +
            "Component::encodeBegin" + "\n" + "Dynamically added child" + "\n" +
            "Component::encodeEnd" + "\n" + "Component::encodeEnd";

        /*
         * Make sure the added component and nested component is in the proper place. 
         */
        HtmlPage page = webClient.getPage(webUrl + "faces/recursive.xhtml");
        assertTrue(page.asText().contains(str));

        /**
         * After clicking make sure the added component and nested component is still in 
         * its proper place.
         */
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("button");
        page = button.click();
        assertTrue(page.asText().contains(str));
    }


}
