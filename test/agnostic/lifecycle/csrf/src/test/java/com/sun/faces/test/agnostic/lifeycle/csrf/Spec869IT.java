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
 * https://glassfish.dev.java.net/public/CDDLGPL_1_1.html
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
package com.sun.faces.test.agnostic.lifeycle.csrf;

import org.junit.Ignore;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


import static org.junit.Assert.assertTrue;

public class Spec869IT {

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
    public void testSimpleCSRF() throws Exception {
        HtmlPage page = webClient.getPage(webUrl);
        HtmlButtonInput button = (HtmlButtonInput) page.getElementById("button");
        page = button.click();
        
        String pageText = page.getBody().asText();
        assertTrue(pageText.contains("protected view"));
        
        webClient.setThrowExceptionOnFailingStatusCode(false);
        page = webClient.getPage(webUrl + "faces/i_spec_869_war_protected.xhtml");
        pageText = page.getBody().asText();
        
        assertTrue(pageText.contains("javax.faces.application.ProtectedViewException"));
        
    }

    // Tests a request with an invalid referer header request parameter.
    @Test
    @Ignore
    public void testBadRefererCSRF() throws Exception {
        webClient.removeRequestHeader("Referer");
        webClient.addRequestHeader("Referer", "foobar");
        webClient.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = webClient.getPage(webUrl);
        HtmlButtonInput button = (HtmlButtonInput) page.getElementById("button");
        page = button.click();
        String pageText = page.getBody().asText();
        assertTrue(pageText.contains("javax.faces.application.ProtectedViewException"));
    }

    // Tests a request with an invalid origin header request parameter.
    @Test
    @Ignore
    public void testBadOriginCSRF() throws Exception {
        webClient.removeRequestHeader("Origin");
        webClient.addRequestHeader("Origin", "foobar");
        webClient.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = webClient.getPage(webUrl);
        HtmlButtonInput button = (HtmlButtonInput) page.getElementById("button");
        page = button.click();
        String pageText = page.getBody().asText();
        assertTrue(pageText.contains("javax.faces.application.ProtectedViewException"));
    }

    // Tests a request with a valid referer header request parameter.
    // In this case the referer is an unprotected page, but it is the originating
    // page in this webapp (for the protected page).
    @Test
    public void testGoodRefererCSRF() throws Exception {
        webClient.removeRequestHeader("Referer");
        webClient.addRequestHeader("Referer", "i_spec_869_war.xhtml");
        webClient.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = webClient.getPage(webUrl);
        HtmlButtonInput button = (HtmlButtonInput) page.getElementById("button");
        page = button.click();
        String pageText = page.getBody().asText();
        assertTrue(!pageText.contains("javax.faces.application.ProtectedViewException"));
    }

    // Tests a request with a valid origin header request parameter.
    // In this case the origin is an unprotected page, but it is the originating
    // page in this webapp (for the protected page).
    @Test
    public void testGoodOriginCSRF() throws Exception {
        webClient.removeRequestHeader("Origin");
        webClient.addRequestHeader("Origin", "i_spec_869_war.xhtml");
        webClient.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = webClient.getPage(webUrl);
        HtmlButtonInput button = (HtmlButtonInput) page.getElementById("button");
        page = button.click();
        String pageText = page.getBody().asText();
        assertTrue(!pageText.contains("javax.faces.application.ProtectedViewException"));
    }

}
