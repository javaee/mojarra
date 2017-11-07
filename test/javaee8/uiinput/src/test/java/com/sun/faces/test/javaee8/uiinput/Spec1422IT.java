/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.test.javaee8.uiinput;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.sun.faces.test.htmlunit.IgnoringIncorrectnessListener;
import com.sun.faces.test.junit.JsfTestRunner;

@RunWith(JsfTestRunner.class)
public class Spec1422IT {

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
    public void testSpec1422() throws Exception {
        webClient.setIncorrectnessListener(new IgnoringIncorrectnessListener());

        HtmlPage page;
        HtmlCheckBoxInput item1;
        HtmlCheckBoxInput item2;
        HtmlCheckBoxInput item3;
        HtmlCheckBoxInput number1;
        HtmlCheckBoxInput number2;
        HtmlCheckBoxInput number3;
        HtmlCheckBoxInput number4;
        HtmlCheckBoxInput number5;
        HtmlCheckBoxInput number6;
        HtmlCheckBoxInput number7;
        HtmlSubmitInput button;

        page = webClient.getPage(webUrl + "spec1422.xhtml");
        assertTrue(page.getHtmlElementById("form:result").asText().isEmpty());

        item1 = (HtmlCheckBoxInput) page.getHtmlElementById("form:items:0");
        item2 = (HtmlCheckBoxInput) page.getHtmlElementById("form:items:1");
        item3 = (HtmlCheckBoxInput) page.getHtmlElementById("form:items:2");
        number1 = (HtmlCheckBoxInput) page.getHtmlElementById("form:numbers:0");
        number2 = (HtmlCheckBoxInput) page.getHtmlElementById("form:numbers:1");
        number3 = (HtmlCheckBoxInput) page.getHtmlElementById("form:numbers:2");
        number4 = (HtmlCheckBoxInput) page.getHtmlElementById("form:numbers:3");
        number5 = (HtmlCheckBoxInput) page.getHtmlElementById("form:numbers:4");
        number6 = (HtmlCheckBoxInput) page.getHtmlElementById("form:numbers:5");
        number7 = (HtmlCheckBoxInput) page.getHtmlElementById("form:numbers:6");
        button = (HtmlSubmitInput) page.getHtmlElementById("form:button");
        item1.setChecked(true);
        item2.setChecked(true);
        item3.setChecked(true);
        number1.setChecked(true);
        number2.setChecked(true);
        number3.setChecked(true);
        number4.setChecked(true);
        number5.setChecked(true);
        number6.setChecked(true);
        number7.setChecked(true);
        page = button.click();
        assertTrue(page.getHtmlElementById("form:result").asText().equals("[ONE, TWO, THREE][null, 1, 2, 3, 4.5, 6.7, 8.9]"));
    }

    @After
    public void tearDown() {
        webClient.close();
    }

}
