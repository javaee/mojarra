/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2018 Oracle and/or its affiliates. All rights reserved.
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.test.htmlunit.IgnoringIncorrectnessListener;
import com.sun.faces.test.junit.JsfTestRunner;

@RunWith(JsfTestRunner.class)
public class Issue4330IT {

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
    public void testIssue4330() throws Exception {
        webClient.setIncorrectnessListener(new IgnoringIncorrectnessListener());

        HtmlPage page;
        HtmlRadioButtonInput disabledRadio;
        HtmlCheckBoxInput enabledCheckbox;
        HtmlCheckBoxInput disabledCheckbox;
        HtmlButtonInput hack;
        HtmlSubmitInput submit;

        page = webClient.getPage(webUrl + "issue4330.xhtml");
        assertTrue(page.getHtmlElementById("form:result").asText().isEmpty());

        disabledRadio = page.getHtmlElementById("form:one:1");
        enabledCheckbox = page.getHtmlElementById("form:many:0");
        disabledCheckbox = page.getHtmlElementById("form:many:1");
        hack = page.getHtmlElementById("form:hack");
        submit = page.getHtmlElementById("form:submit");

        assertTrue(disabledRadio.isDisabled());
        assertTrue(disabledCheckbox.isDisabled());

        hack.click();
        webClient.waitForBackgroundJavaScript(1000);

        assertFalse(disabledRadio.isDisabled());
        assertFalse(disabledCheckbox.isDisabled());

        disabledRadio.setChecked(true);
        enabledCheckbox.setChecked(true);
        disabledCheckbox.setChecked(true);

        page = submit.click();
        assertTrue(page.getHtmlElementById("form:result").asText().equals("[enabled]")); // Thus not "disabled[enabled, disabled]"
    }

    @After
    public void tearDown() {
        webClient.close();
    }

}