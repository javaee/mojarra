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

package com.sun.faces.test.servlet30.ajax;

import static java.lang.System.getProperty;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class Issue2682IT {

    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = getProperty("integration.url");
        webClient = new WebClient();
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
    }

    @After
    public void tearDown() {
        webClient.close();
    }

    @Test
    public void testIssue2682() throws Exception {
        
        // DebugHelper debugHelper = new DebugHelper(webClient, "issue2682.xhtml");
        
        HtmlPage page = webClient.getPage(webUrl + "issue2682.xhtml");
        System.out.println("\n\n\n ****************************" + webClient.getCookieManager().getCookies());
        
        assertTrue(page.asText().contains("Check Box Status: false"));
        
        HtmlCheckBoxInput cbox = (HtmlCheckBoxInput) page.getElementById("form:cbox");
        
        HtmlPage page1 = cbox.click();
        webClient.waitForBackgroundJavaScript(120000);
        
        // debugHelper.print(page1, "After cbox click");
        
        assertTrue(page.asText().contains("Check Box Status: true"));
        
        HtmlSubmitInput input = (HtmlSubmitInput) page1.getHtmlElementById("form:button");
        
        page1 = input.click();
        
        // debugHelper.print(page1, "After button click");
        
        assertTrue(page1.asText().contains("Check Box Status: true"));
        
        cbox = (HtmlCheckBoxInput) page1.getHtmlElementById("form:cbox");
        page1 = cbox.click();
        webClient.waitForBackgroundJavaScript(120000);
        assertTrue(page1.asText().contains("Check Box Status: false"));
        
        input = (HtmlSubmitInput) page1.getHtmlElementById("form:button");
        page1 = input.click();
        webClient.waitForBackgroundJavaScript(120000);
        assertTrue(page1.asText().contains("Check Box Status: false"));
    }
    
   
}
