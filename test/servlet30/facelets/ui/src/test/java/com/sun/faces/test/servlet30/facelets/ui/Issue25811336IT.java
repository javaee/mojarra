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
package com.sun.faces.test.servlet30.facelets.ui;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by xinyuan.zhang on 4/10/17.
 */
public class Issue25811336IT {

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
    public void testValueAfterValidation() throws Exception {

        HtmlPage page = webClient.getPage(webUrl + "faces/Sample.xhtml");

        HtmlInput input0=page.getHtmlElementById("form:repeat:0:input");
        HtmlInput input1=page.getHtmlElementById("form:repeat:1:input");

        assertTrue(input0.asText().contains("aaa"));
        assertTrue(input1.asText().contains("bbb"));

        input0.setAttribute("value","cccc");
        input1.setAttribute("value","ddd");

        HtmlSubmitInput submit = (HtmlSubmitInput) page.getHtmlElementById("form:submit");
        HtmlPage page2 =(HtmlPage) submit.click();
        webClient.waitForBackgroundJavaScript(60000);

        HtmlInput input2=page2.getHtmlElementById("form:repeat:0:input");
        HtmlInput input3=page2.getHtmlElementById("form:repeat:1:input");

        assertTrue(input2.asText().contains("cccc"));
        assertTrue(input3.asText().contains("ddd"));
        assertTrue(page2.asText().contains("size must be between 0 and 3"));

        input2.setAttribute("value","ee");
        input3.setAttribute("value","ff");


        HtmlSubmitInput submit2 = (HtmlSubmitInput) page2.getHtmlElementById("form:submit");

        HtmlPage page3 =(HtmlPage) submit2.click();
        webClient.waitForBackgroundJavaScript(60000);
        HtmlInput input4=page2.getHtmlElementById("form:repeat:0:input");
        HtmlInput input5=page2.getHtmlElementById("form:repeat:1:input");

        System.out.println("**************************************");
        System.out.println(page3.asText());
        System.out.println("**************************************");
        assertTrue(input4.asText().contains("ee"));
        assertTrue(input5.asText().contains("ff"));
        assertTrue(!page3.asText().contains("size must be between 0 and 3"));



    }

}
