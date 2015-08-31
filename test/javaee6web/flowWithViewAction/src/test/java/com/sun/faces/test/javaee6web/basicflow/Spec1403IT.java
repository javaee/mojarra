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
package com.sun.faces.test.javaee6web.basicflow;

import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import static org.junit.Assert.assertTrue;

public class Spec1403IT {
    /**
     * Stores the web URL.
     */
    private String webUrl;
    /**
     * Stores the web client.
     */
    private WebClient webClient;

    /**
     * Setup before testing.
     * 
     * @throws Exception when a serious error occurs.
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     * Cleanup after testing.
     * 
     * @throws Exception when a serious error occurs.
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Setup before testing.
     */
    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
    }

    /**
     * Tear down after testing.
     */
    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    @Test
    public void testFlowEntryExit() throws Exception {
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = webClient.getPage(webUrl);

        assertTrue(page.getBody().asText().contains("First page in the flow."));
        assertTrue(page.getBody().asText().contains("flowA"));

        HtmlButtonInput button = (HtmlButtonInput) page.getElementById("flowA-next");
        page = button.click();
        
        String pageText = page.getBody().asText();
        assertTrue(!pageText.contains("flowA"));
        assertTrue(pageText.contains("Gracefully handle ContextNotActive"));
        
    }

    @Test
    public void testNestedFlowEntryExit() throws Exception {
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = webClient.getPage(webUrl);

        assertTrue(page.getBody().asText().contains("First page in the flow."));
        assertTrue(page.getBody().asText().contains("flowA"));

        HtmlButtonInput button = (HtmlButtonInput) page.getElementById("flowB");
        page = button.click();

        assertTrue(page.getBody().asText().contains("called from flowA"));
        assertTrue(page.getBody().asText().contains("flowB"));

        button = (HtmlButtonInput) page.getElementById("flowB-next");
        page = button.click();

        assertTrue(page.getBody().asText().contains("First page in the flow."));
        assertTrue(page.getBody().asText().contains("flowA"));
        
        button = (HtmlButtonInput) page.getElementById("flowA-next");
        page = button.click();

        String pageText = page.getBody().asText();
        assertTrue(!pageText.contains("flowA"));
        assertTrue(pageText.contains("Gracefully handle ContextNotActive"));
        
    }
    
    
}
