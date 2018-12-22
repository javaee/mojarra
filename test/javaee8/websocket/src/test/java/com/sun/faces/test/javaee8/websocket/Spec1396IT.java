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
package com.sun.faces.test.javaee8.websocket;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.test.htmlunit.IgnoringIncorrectnessListener;
import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_2_1;
import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_3_1;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;
import static com.sun.faces.test.junit.JsfVersion.JSF_2_3_0_M08;

@RunWith(JsfTestRunner.class)
public class Spec1396IT {

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
    @JsfTest(value=JSF_2_3_0_M08, excludes={WEBLOGIC_12_2_1})
    public void test() throws Exception {
        webClient.setIncorrectnessListener(new IgnoringIncorrectnessListener());

        testEnableWebsocketEndpoint();
        testWebsocket();
    }

    public void testEnableWebsocketEndpoint() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "spec1396EnableWebsocketEndpoint.xhtml");
        assertTrue(page.getHtmlElementById("param").asText().equals("true"));
    }

    public void testWebsocket() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "spec1396.xhtml");
        String pageSource = page.getWebResponse().getContentAsString();
        assertTrue(pageSource.contains("/javax.faces.push/push?"));
        assertTrue(pageSource.contains("/javax.faces.push/user?"));
        assertTrue(pageSource.contains("/javax.faces.push/view?"));

        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("form:button");
        assertTrue(button.asText().equals("push"));

        page = button.click();
        webClient.waitForBackgroundJavaScript(60000);
       
        for (int i=0; i<6; i++) {
            try {
                System.out.println("Wait until WS push - iteration #" + i);
                Thread.sleep(1000); // waitForBackgroundJavaScript doesn't wait until the WS push is arrived.

                assertTrue(page.getHtmlElementById("form:button").asText().equals("pushed!"));
                assertTrue(page.getHtmlElementById("user").asText().equals("pushed!"));
                assertTrue(page.getHtmlElementById("ajaxOutput").asText().equals("pushed!"));
                
                break;
            } catch (Error e) {
                e.printStackTrace();
            }
        }
        
    }

    @After
    public void tearDown() {
        webClient.close();
    }

}
