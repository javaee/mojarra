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
package com.sun.faces.test.javaee8.ajax;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.test.htmlunit.IgnoringIncorrectnessListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class Spec790IT {

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
    public void testSpec790() throws Exception {
        webClient.setIncorrectnessListener(new IgnoringIncorrectnessListener());

        HtmlPage page = webClient.getPage(webUrl + "spec790.xhtml");
        HtmlForm form1 = (HtmlForm) page.getHtmlElementById("form1");
        HtmlInput form1ViewState = (HtmlInput) form1.getInputByName("javax.faces.ViewState");
        HtmlForm form2 = (HtmlForm) page.getHtmlElementById("form2");
        HtmlInput form2ViewState = (HtmlInput) form2.getInputByName("javax.faces.ViewState");
        assertTrue(!form1ViewState.getValueAttribute().isEmpty());
        assertTrue(!form2ViewState.getValueAttribute().isEmpty());

        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("form1:button");
        page = button.click();
        webClient.waitForBackgroundJavaScript(60000);
        form1 = (HtmlForm) page.getHtmlElementById("form1");
        form1ViewState = (HtmlInput) form1.getInputByName("javax.faces.ViewState");
        form2 = (HtmlForm) page.getHtmlElementById("form2");
        form2ViewState = (HtmlInput) form2.getInputByName("javax.faces.ViewState");
        assertTrue(!form1ViewState.getValueAttribute().isEmpty());
        assertTrue(!form2ViewState.getValueAttribute().isEmpty());

        button = (HtmlSubmitInput) page.getHtmlElementById("form2:button");
        page = button.click();
        webClient.waitForBackgroundJavaScript(60000);
        form1 = (HtmlForm) page.getHtmlElementById("form1");
        form1ViewState = (HtmlInput) form1.getInputByName("javax.faces.ViewState");
        form2 = (HtmlForm) page.getHtmlElementById("form2");
        form2ViewState = (HtmlInput) form2.getInputByName("javax.faces.ViewState");
        assertTrue(!form1ViewState.getValueAttribute().isEmpty());
        assertTrue(!form2ViewState.getValueAttribute().isEmpty());
    }

    @Test
    @Ignore // fails due to https://sourceforge.net/p/htmlunit/bugs/1815 TODO enable when HtmlUnit 2.24 is final
    public void testSpec790AjaxNavigation() throws Exception {
        webClient.setIncorrectnessListener(new IgnoringIncorrectnessListener());

        HtmlPage page = webClient.getPage(webUrl + "spec790AjaxNavigation.xhtml");
        HtmlForm form = (HtmlForm) page.getHtmlElementById("form");
        HtmlInput formViewState = (HtmlInput) form.getInputByName("javax.faces.ViewState");
        assertTrue(!formViewState.getValueAttribute().isEmpty());

        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("form:button");
        page = button.click();
        webClient.waitForBackgroundJavaScript(60000);
        HtmlForm form1 = (HtmlForm) page.getHtmlElementById("form1");
        HtmlInput form1ViewState = (HtmlInput) form1.getInputByName("javax.faces.ViewState");
        HtmlForm form2 = (HtmlForm) page.getHtmlElementById("form2");
        HtmlInput form2ViewState = (HtmlInput) form2.getInputByName("javax.faces.ViewState");
        assertTrue(!form1ViewState.getValueAttribute().isEmpty());
        assertTrue(!form2ViewState.getValueAttribute().isEmpty());
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

}