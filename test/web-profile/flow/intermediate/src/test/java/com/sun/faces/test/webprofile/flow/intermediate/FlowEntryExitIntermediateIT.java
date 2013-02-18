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
package com.sun.faces.test.webprofile.flow.intermediate;

import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FlowEntryExitIntermediateIT {
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
    public void testFacesFlowScopeXml() throws Exception {
        performCustomerUpgradeTest("maintain-customer-record");
        performCustomerUpgradeTest("maintain-customer-record");
        performInFlowExplicitNavigationTest("maintain-customer-record");
        performInFlowExplicitNavigationTest("maintain-customer-record");
        
    }
    
    @Test
    public void testFacesFlowScopeJava() throws Exception {
        performCustomerUpgradeTest("maintain-customer-record-java");
        performCustomerUpgradeTest("maintain-customer-record-java");
        performInFlowExplicitNavigationTest("maintain-customer-record-java");
        performInFlowExplicitNavigationTest("maintain-customer-record-java");
        
    }
    private void performCustomerUpgradeTest(String startButton) throws Exception {
        quickEnterExit();
        
        HtmlPage page = webClient.getPage(webUrl);
        
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById(startButton);
        
        page = button.click();
        
        button = (HtmlSubmitInput) page.getElementById("createCustomer");
        page = button.click();
        String pageText = page.asText();
        Pattern pattern = Pattern.compile("(?s).*Customer Id:\\s+([0-9])+.*");
        Matcher matcher = pattern.matcher(pageText);
        assertTrue(matcher.matches());
        String customerId = matcher.group(1);
        assertTrue(pageText.matches("(?s).*Customer is upgraded:\\s+false.*"));
        
        button = (HtmlSubmitInput) page.getElementById("upgrade");
        page = button.click();
        pageText = page.asText();
        matcher = pattern.matcher(pageText);
        assertTrue(matcher.matches());
        String sameCustomerId = matcher.group(1);
        assertTrue(pageText.matches("(?s).*Customer is upgraded:\\s+true.*"));
        assertEquals(customerId, sameCustomerId);
        
        button = (HtmlSubmitInput) page.getElementById("exit");
        page = button.click();
        
        pageText = page.getBody().asText();
        
        assertTrue(pageText.contains("return page"));
        assertTrue(pageText.contains("Finalizer called"));
        
        button = (HtmlSubmitInput) page.getElementById("home");
        page = button.click();

        button = (HtmlSubmitInput) page.getElementById(startButton);
        assertNotNull(button);
        
        
    }
    
    private void performInFlowExplicitNavigationTest(String startButton) throws Exception {
        HtmlPage page = webClient.getPage(webUrl);
        
        HtmlSubmitInput submit = (HtmlSubmitInput) page.getElementById(startButton);
        page = submit.click();
        submit = (HtmlSubmitInput) page.getElementById("createCustomer");
        page = submit.click();
        
        submit = (HtmlSubmitInput) page.getElementById("pageA");
        page = submit.click();
        String pageText = page.asText();
        assertTrue(pageText.contains("explicit in flow nav 01"));
        
        submit = (HtmlSubmitInput) page.getElementById("pageB");
        page = submit.click();
        
        pageText = page.asText();
        assertTrue(pageText.contains("explicit in flow nav 02"));
        
        submit = (HtmlSubmitInput) page.getElementById("pageC_true");
        page = submit.click();
        
        pageText = page.asText();
        assertTrue(pageText.contains("explicit in flow nav 03: if true"));
        
        submit = (HtmlSubmitInput) page.getElementById("pageB");
        page = submit.click();
        
        submit = (HtmlSubmitInput) page.getElementById("pageC_false");
        page = submit.click();
        
        pageText = page.asText();
        assertTrue(pageText.contains("explicit in flow nav 03: if false"));
        
        submit = (HtmlSubmitInput) page.getElementById("pageB");
        page = submit.click();
        
        submit = (HtmlSubmitInput) page.getElementById("pageD_redirect");
        page = submit.click();
        
        pageText = page.asText();
        assertTrue(pageText.contains("explicit in flow nav 04: no params"));

        submit = (HtmlSubmitInput) page.getElementById("pageB");
        page = submit.click();
        
        HtmlButtonInput button = (HtmlButtonInput) page.getElementById("pageD_redirect_params");
        page = button.click();
        
        pageText = page.asText();
        assertTrue(pageText.contains("explicit in flow nav 05: params"));
        assertTrue(pageText.contains("id param: foo"));
        assertTrue(pageText.contains("baz param: bar"));
        
        
    }
    
    
    private void quickEnterExit() throws Exception {
        HtmlPage page = webClient.getPage(webUrl);
        
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("maintain-customer-record");
        
        page = button.click();
        
        String pageText = page.getBody().asText();
        
        assertTrue(pageText.contains("Create customer page"));
        assertTrue(pageText.contains("Initializer called"));
        
        button = (HtmlSubmitInput) page.getElementById("return");
        
        page = button.click();
        
        pageText = page.getBody().asText();
        
        assertTrue(pageText.contains("return page"));
        assertTrue(pageText.contains("Finalizer called"));
        
    }
}
