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

package com.sun.faces.test.agnostic.ajax; 

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import static org.junit.Assert.*;

public class Issue2408IT {

    /**
     * Stores the web URL.
     */
    private String webUrl;
    /**
     * Stores the web client.
     */
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


    // ------------------------------------------------------------ Test Methods

    /**
     * This test verifies correct function of SelectManyCheckbox in a Composite
     * Component over Ajax. 
     */
    @Test
    @Ignore
    public void testSelectManyCheckboxInComposite() throws Exception {
        HtmlPage page = webClient.getPage(webUrl+"faces/selectManyCheckboxInComposite.xhtml");
        final List<HtmlCheckBoxInput> checkBoxes = new ArrayList();
        final DomNodeList<HtmlElement> elements = page.getElementsByTagName("input");
        for (HtmlElement elem : elements) {
            if (elem instanceof HtmlCheckBoxInput) {
                checkBoxes.add((HtmlCheckBoxInput)elem);
            }
        }
        HtmlCheckBoxInput cbox1 = checkBoxes.get(0);
        HtmlCheckBoxInput cbox2 = checkBoxes.get(1);
        HtmlCheckBoxInput cbox3 = checkBoxes.get(2);
        // This will ensure JavaScript finishes before evaluating the page.
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: Pending"));

        page = cbox1.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: mcheck-1"));

        page = cbox2.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: mcheck-1 mcheck-2"));

        page = cbox3.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: mcheck-1 mcheck-2 mcheck-3"));
    }

    /**
     * This test verifies correct function of SelectManyCheckbox in a Composite
     * Component over Ajax. The components in the page have ids.
     */
    @Test
    @Ignore
    public void testSelectManyCheckboxIdsInComposite() throws Exception {
        HtmlPage page = webClient.getPage(webUrl+"faces/selectManyCheckboxIdsInComposite.xhtml");
        
        HtmlCheckBoxInput cbox1 = (HtmlCheckBoxInput)page.getElementById("form:compId:cbox:0");
        HtmlCheckBoxInput cbox2 = (HtmlCheckBoxInput)page.getElementById("form:compId:cbox:1");
        HtmlCheckBoxInput cbox3 = (HtmlCheckBoxInput)page.getElementById("form:compId:cbox:2");

        // This will ensure JavaScript finishes before evaluating the page.
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: Pending"));

        page = cbox1.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: mcheck-1"));

        page = cbox2.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: mcheck-1 mcheck-2"));

        page = cbox3.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: mcheck-1 mcheck-2 mcheck-3"));
    }

    /**
     * This test verifies correct function of SelectManyCheckbox Component over Ajax.
     */
    @Test
    @Ignore
    public void testSelectManyCheckboxNoComposite() throws Exception {
        HtmlPage page = webClient.getPage(webUrl+"faces/selectManyCheckboxNoComposite.xhtml");
        final List<HtmlCheckBoxInput> checkBoxes = new ArrayList();
        final DomNodeList<HtmlElement> elements = page.getElementsByTagName("input");
        for (HtmlElement elem : elements) {
            if (elem instanceof HtmlCheckBoxInput) {
                checkBoxes.add((HtmlCheckBoxInput)elem);
            }
        }
        HtmlCheckBoxInput cbox1 = checkBoxes.get(0);
        HtmlCheckBoxInput cbox2 = checkBoxes.get(1);
        HtmlCheckBoxInput cbox3 = checkBoxes.get(2);
        // This will ensure JavaScript finishes before evaluating the page.
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: Pending"));

        page = cbox1.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: mcheck-1"));

        page = cbox2.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: mcheck-1 mcheck-2"));

        page = cbox3.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: mcheck-1 mcheck-2 mcheck-3"));
    }

    /**
     * This test verifies correct function of SelectOneRadio in a Composite
     * Component over Ajax.
     */
    @Test
    @Ignore
    public void testSelectOneRadioInComposite() throws Exception {
        HtmlPage page = webClient.getPage(webUrl+"faces/selectOneRadioInComposite.xhtml");
        final List<HtmlRadioButtonInput> radios = new ArrayList();
        final DomNodeList<HtmlElement> elements = page.getElementsByTagName("input");
        for (HtmlElement elem : elements) {
            if (elem instanceof HtmlRadioButtonInput) {
                radios.add((HtmlRadioButtonInput)elem);
            }
        }
        HtmlRadioButtonInput radio1 = radios.get(0);
        HtmlRadioButtonInput radio2 = radios.get(1);
        HtmlRadioButtonInput radio3 = radios.get(2);

        // This will ensure JavaScript finishes before evaluating the page.
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: Pending"));

        page = radio1.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: radio-1"));

        page = radio2.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: radio-2"));

        page = radio3.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: radio-3"));
    }

    /**
     * This test verifies correct function of SelectOneRadio in a Composite
     * Component over Ajax. The components in the page have ids.
     */
    @Test
    @Ignore
    public void testSelectOneRadioIdsInComposite() throws Exception {
        HtmlPage page = webClient.getPage(webUrl+"faces/selectOneRadioIdsInComposite.xhtml");

        HtmlRadioButtonInput radio1 = (HtmlRadioButtonInput)page.getElementById("form:compId:radio:0");
        HtmlRadioButtonInput radio2 = (HtmlRadioButtonInput)page.getElementById("form:compId:radio:1");
        HtmlRadioButtonInput radio3 = (HtmlRadioButtonInput)page.getElementById("form:compId:radio:2");

        // This will ensure JavaScript finishes before evaluating the page.
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: Pending"));

        page = radio1.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: radio-1"));

        page = radio2.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: radio-2"));

        page = radio3.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: radio-3"));
    }

    /**
     * This test verifies correct function of SelectOneRadio Component over Ajax.
     */
    @Test
    @Ignore
    public void testSelectOneRadioNoComposite() throws Exception {
        HtmlPage page = webClient.getPage(webUrl+"faces/selectOneRadioNoComposite.xhtml");
        final List<HtmlRadioButtonInput> radios = new ArrayList();
        final DomNodeList<HtmlElement> elements = page.getElementsByTagName("input");
        for (HtmlElement elem : elements) {
            if (elem instanceof HtmlRadioButtonInput) {
                radios.add((HtmlRadioButtonInput)elem);
            }
        }
        HtmlRadioButtonInput radio1 = radios.get(0);
        HtmlRadioButtonInput radio2 = radios.get(1);
        HtmlRadioButtonInput radio3 = radios.get(2);

        // This will ensure JavaScript finishes before evaluating the page.
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: Pending"));

        page = radio1.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: radio-1"));

        page = radio2.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: radio-2"));

        page = radio3.click();
        webClient.waitForBackgroundJavaScript(60000);
        assertTrue(page.asXml().contains("Status: radio-3"));
    }

}
