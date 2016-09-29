/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2016 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDLGPL_1_1.html
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
package com.sun.faces.test.javaee8.uiinput;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.sun.faces.test.htmlunit.IgnoringIncorrectnessListener;
import com.sun.faces.test.junit.JsfTestRunner;

@RunWith(JsfTestRunner.class)
public class Spec329IT {

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
    public void testSpec329() throws Exception {
        webClient.setIncorrectnessListener(new IgnoringIncorrectnessListener());

        HtmlPage page = webClient.getPage(webUrl + "spec329.xhtml");
        assertTrue(page.getHtmlElementById("messages").asText().isEmpty());
        assertTrue(page.getHtmlElementById("inDataTableWithEntityList:selectedItem").asText().isEmpty());
        assertTrue(page.getHtmlElementById("inRepeatWithSelectItemList:selectedItem").asText().isEmpty());
        assertTrue(page.getHtmlElementById("multipleRadioButtonsWithStaticItemsInFirstRadio:selectedItem").asText().isEmpty());
        assertTrue(page.getHtmlElementById("multipleRadioButtonsWithStaticItemsInEachRadio:selectedItem").asText().isEmpty());
        assertTrue(page.getHtmlElementById("multipleRadioButtonsWithSelectItemList:selectedItem").asText().isEmpty());

        page = ((HtmlSubmitInput) page.getHtmlElementById("inDataTableWithEntityList:button")).click();
        assertTrue(page.getHtmlElementById("messages").asText().equals("required")); // It should appear only once!
        
        HtmlRadioButtonInput inDataTableWithEntityListRadio = (HtmlRadioButtonInput) page.getHtmlElementById("inDataTableWithEntityList:table:1:radio");
        inDataTableWithEntityListRadio.setChecked(true);
        page = ((HtmlSubmitInput) page.getHtmlElementById("inDataTableWithEntityList:button")).click();
        assertTrue(page.getHtmlElementById("messages").asText().isEmpty());
        assertTrue(page.getHtmlElementById("inDataTableWithEntityList:selectedItem").asText().equals("two"));

        HtmlRadioButtonInput inRepeatWithSelectItemListRadio = (HtmlRadioButtonInput) page.getHtmlElementById("inRepeatWithSelectItemList:repeat:1:radio");
        inRepeatWithSelectItemListRadio.setChecked(true);
        page = ((HtmlSubmitInput) page.getHtmlElementById("inRepeatWithSelectItemList:button")).click();
        assertTrue(page.getHtmlElementById("messages").asText().isEmpty());
        assertTrue(page.getHtmlElementById("inRepeatWithSelectItemList:selectedItem").asText().equals("value2"));

        HtmlRadioButtonInput multipleRadioButtonsWithStaticItemsInFirstRadio = (HtmlRadioButtonInput) page.getHtmlElementById("multipleRadioButtonsWithStaticItemsInFirstRadio:radio2");
        multipleRadioButtonsWithStaticItemsInFirstRadio.setChecked(true);
        page = ((HtmlSubmitInput) page.getHtmlElementById("multipleRadioButtonsWithStaticItemsInFirstRadio:button")).click();
        assertTrue(page.getHtmlElementById("messages").asText().isEmpty());
        assertTrue(page.getHtmlElementById("multipleRadioButtonsWithStaticItemsInFirstRadio:selectedItem").asText().equals("two"));

        HtmlRadioButtonInput multipleRadioButtonsWithStaticItemsInEachRadio = (HtmlRadioButtonInput) page.getHtmlElementById("multipleRadioButtonsWithStaticItemsInEachRadio:radio2");
        multipleRadioButtonsWithStaticItemsInEachRadio.setChecked(true);
        page = ((HtmlSubmitInput) page.getHtmlElementById("multipleRadioButtonsWithStaticItemsInEachRadio:button")).click();
        assertTrue(page.getHtmlElementById("messages").asText().isEmpty());
        assertTrue(page.getHtmlElementById("multipleRadioButtonsWithStaticItemsInEachRadio:selectedItem").asText().equals("two"));

        HtmlRadioButtonInput multipleRadioButtonsWithSelectItemListRadio = (HtmlRadioButtonInput) page.getHtmlElementById("multipleRadioButtonsWithSelectItemList:radio2");
        multipleRadioButtonsWithSelectItemListRadio.setChecked(true);
        page = ((HtmlSubmitInput) page.getHtmlElementById("multipleRadioButtonsWithSelectItemList:button")).click();
        assertTrue(page.getHtmlElementById("messages").asText().isEmpty());
        assertTrue(page.getHtmlElementById("multipleRadioButtonsWithSelectItemList:selectedItem").asText().equals("value2"));
    }

    @After
    public void tearDown() {
        webClient.close();
    }

}