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
package com.sun.faces.test.agnostic.facelets.core;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Issue2731IT {

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

    /**
     * Test default Stateless page.
     *
     * @throws Exception
     */
    @Test
    public void testDefaultStateless() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/viewTransient.xhtml");
        assertTrue(page.asXml().indexOf("\"stateless\"") != -1);
        
        HtmlElement button = page.getHtmlElementById("form:button");
        page = button.click();
        
        assertTrue(page.asXml().indexOf("\"stateless\"") != -1);
    }


    /**
     * Test stateless AJAX page.
     *
     * @throws Exception
     */
    @Test
    public void testStatelessAjax() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/viewTransientAjax.xhtml");
        assertTrue(page.asXml().indexOf("\"stateless\"") != -1);
        assertTrue(page.asXml().indexOf("[]") != -1);
        
        HtmlElement button = page.getHtmlElementById("form:ajaxButton");
        page = button.click();
        webClient.waitForBackgroundJavaScript(60000);
        
        assertTrue(page.asXml().indexOf("\"stateless\"") != -1);
        assertTrue(page.asXml().indexOf("[ajax]") != -1);

        button = page.getHtmlElementById("form:submitButton");
        page = button.click();

        assertTrue(page.asXml().indexOf("\"stateless\"") != -1);
        assertTrue(page.asXml().indexOf("[non-ajax]") != -1);
    }
    
    /**
     * Test default Stateful page.
     *
     * @throws Exception
     */
    @Test
    public void testDefaultStateful() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/viewTransientFalse.xhtml");
        assertTrue(page.asXml().indexOf("com.sun.faces.StatelessPostback") == -1);
        
        HtmlElement button = page.getHtmlElementById("form:button");
        page = button.click();
        
        assertTrue(page.asXml().indexOf("com.sun.faces.StatelessPostback") == -1);
    }
}
