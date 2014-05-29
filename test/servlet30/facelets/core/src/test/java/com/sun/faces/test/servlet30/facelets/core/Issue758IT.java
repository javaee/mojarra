/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2014 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.servlet30.facelets.core;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;
import com.sun.faces.test.junit.JsfVersion;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JsfTestRunner.class)
public class Issue758IT {

    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
    }

    @Test
    public void testRedirect1a() throws Exception {
        webClient.getOptions().setRedirectEnabled(false);
        HtmlPage page;
        boolean exceptionThrown = false;

        try {
            webClient.getPage(webUrl + "faces/viewActionRedirect1a.xhtml");
        } catch (FailingHttpStatusCodeException ex) {
            assertEquals(302, ex.getStatusCode());
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        webClient.getOptions().setRedirectEnabled(true);
        page = webClient.getPage(webUrl + "faces/viewActionRedirect1a.xhtml");
        assertTrue(page.asText().contains("Result page"));
    }

    @JsfTest(JsfVersion.JSF_2_2_1)
    @Test
    public void testRedirect1b() throws Exception {
        webClient.getOptions().setRedirectEnabled(false);
        HtmlPage page;
        boolean exceptionThrown = false;

        try {
            webClient.getPage(webUrl + "faces/viewActionRedirect1b.xhtml");
        } catch (FailingHttpStatusCodeException ex) {
            assertEquals(302, ex.getStatusCode());
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        webClient.getOptions().setRedirectEnabled(true);
        page = webClient.getPage(webUrl + "faces/viewActionRedirect1b.xhtml");
        assertTrue(page.asText().contains("Result page"));
    }

    @JsfTest(JsfVersion.JSF_2_2_5)
    @Test
    public void testActionListener1a() throws Exception {
        webClient.getOptions().setRedirectEnabled(true);
        HtmlPage page;

        page = webClient.getPage(webUrl + "faces/viewActionActionListener1a.xhtml");
        DomElement e = page.getElementById("result");
        assertTrue(e.asText().contains("1 viewAction1 2 viewAction1"));
    }

    @JsfTest(JsfVersion.JSF_2_2_5)
    @Test
    public void testActionListener1b() throws Exception {
        webClient.getOptions().setRedirectEnabled(true);
        HtmlPage page;

        page = webClient.getPage(webUrl + "faces/viewActionActionListener1b.xhtml");
        DomElement e = page.getElementById("result");
        assertTrue(e.asText().contains("1 viewAction1 2 viewAction1"));
    }

    @JsfTest(JsfVersion.JSF_2_2_5)
    @Test
    public void testActionListener2a() throws Exception {
        webClient.getOptions().setRedirectEnabled(true);
        HtmlPage page;

        page = webClient.getPage(webUrl + "faces/viewActionActionListener2a.xhtml");
        DomElement e = page.getElementById("result");
        assertTrue(e.asText().contains("method viewAction1"));
    }

    @JsfTest(JsfVersion.JSF_2_2_5)
    @Test
    public void testActionListener2b() throws Exception {
        webClient.getOptions().setRedirectEnabled(true);
        HtmlPage page;

        page = webClient.getPage(webUrl + "faces/viewActionActionListener2b.xhtml");
        DomElement e = page.getElementById("result");
        assertTrue(e.asText().contains("method viewAction1"));
    }

    @Test
    public void testActionPageA() throws Exception {
        webClient.getOptions().setRedirectEnabled(false);
        HtmlPage page = null;
        boolean exceptionThrown = false;

        try {
            page = webClient.getPage(webUrl + "faces/viewActionActionPageA.xhtml");
        } catch (FailingHttpStatusCodeException ex) {
            assertEquals(302, ex.getStatusCode());
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(page.asText().contains("pageA action"));
    }

    @Test
    public void testActionEmpty() throws Exception {
        webClient.getOptions().setRedirectEnabled(false);
        HtmlPage page = null;
        boolean exceptionThrown = false;

        try {
            page = webClient.getPage(webUrl + "faces/viewActionActionEmpty.xhtml");
        } catch (FailingHttpStatusCodeException ex) {
            assertEquals(302, ex.getStatusCode());
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(page.asText().contains("pageA empty"));
    }

    @Test
    public void testActionNull() throws Exception {
        webClient.getOptions().setRedirectEnabled(false);
        HtmlPage page = null;
        boolean exceptionThrown = false;

        try {
            page = webClient.getPage(webUrl + "faces/viewActionActionNull.xhtml");
        } catch (FailingHttpStatusCodeException ex) {
            assertEquals(302, ex.getStatusCode());
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertTrue(page.asText().contains("pageA null"));
    }

    @Test
    public void testNegativeIntentionalInfiniteRedirect() throws Exception {
        webClient.getOptions().setRedirectEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        boolean exceptionThrown = false;

        try {
            webClient.getPage(webUrl + "faces/viewActionActionExplicitRedirect.xhtml");
        } catch (FailingHttpStatusCodeException ex) {
            assertEquals(302, ex.getStatusCode());
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        try {
            webClient.getPage(webUrl + "faces/viewActionActionExplicitRedirect.xhtml");
        } catch (FailingHttpStatusCodeException ex) {
            assertEquals(302, ex.getStatusCode());
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
}
