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
package com.sun.faces.test.servlet30.flashBasic;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import org.junit.Test;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;
import com.sun.faces.test.junit.JsfVersion;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;

@RunWith(JsfTestRunner.class)
public class Issue2973IT {

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

    @JsfTest(JsfVersion.JSF_2_2_2)
    @Test
    public void testServerRestartHandledGracefully() throws Exception {

        HtmlPage page = webClient.getPage(webUrl + "faces/issue2973/page1.xhtml") ;
        webClient.getOptions().setRedirectEnabled(true);
        HtmlTextInput textInput = (HtmlTextInput) page.getElementById("input");
        String message = "" + System.currentTimeMillis();
        textInput.setValueAttribute(message);
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("button");
        page = button.click();
        HtmlElement value = (HtmlElement) page.getElementById("response");
        
        assertEquals(message, value.asText());
        
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        boolean assertionValue = false;
        for (int i = 0; i < 3; i++) {
            page = webClient.getPage(webUrl + "faces/issue2973/page1.xhtml") ;
            button = (HtmlSubmitInput) page.getElementById("restart");
            page = button.click();
            Thread.sleep(3000);
            
            textInput = (HtmlTextInput) page.getElementById("input");
            message = "" + System.currentTimeMillis();
            textInput.setValueAttribute(message);
            button = (HtmlSubmitInput) page.getElementById("button");
            page = button.click();
            value = (HtmlElement) page.getElementById("response");
        
            if (null != value) {
                assertionValue = message.equals(value.asText());
            }
            if (assertionValue) {
                break;
            }
        }
        assertTrue(assertionValue);   
        
    }
}
