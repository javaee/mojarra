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
package com.sun.faces.test.agnostic.resource.cache.wartest;

import com.gargoylesoftware.htmlunit.CookieManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.Page;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Issue2895IT {

    private String webUrl;
    private WebClient webClient;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

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
    public void testBuilderDefinedFlowWithMethodCall() throws Exception {
        webClient.getCache().clear();
        CookieManager cookieManager = webClient.getCookieManager();
        cookieManager.setCookiesEnabled(true);
        
        HtmlPage page = webClient.getPage(webUrl);
        assertEquals(200, page.getWebResponse().getStatusCode());

        String jsessionid = webClient.getCookieManager().getCookie("JSESSIONID").getValue();
        assertTrue("empty jsessionid sent by server", 0 < jsessionid.length());
        
        String cssUrl = webUrl + "/faces/javax.faces.resource/styles.css";
        Page cssPage = webClient.getPage(cssUrl);
        assertEquals(200, cssPage.getWebResponse().getStatusCode());
        
        /**  Unable to get this working with version of HtmlUnit 
         * in place on 20130716
        
        cssUrl = cssPage.getUrl().toExternalForm();
        
        List<NameValuePair> responseHeaders = page.getWebResponse().getResponseHeaders();
        String ifModifiedSinceValue = null;
        for (NameValuePair cur: responseHeaders) {
            if (cur.getName().equalsIgnoreCase("Last-Modified") || cur.getName().equalsIgnoreCase("Date")) {
                ifModifiedSinceValue = cur.getValue();
            }
        }
        assertTrue("empty Last-Modified", 0 < ifModifiedSinceValue.length());
        

        webClient.removeRequestHeader("If-Modified-Since");
        webClient.removeRequestHeader("Cache-Control");

        webClient.addRequestHeader("If-Modified-Since", ifModifiedSinceValue);
        webClient.addRequestHeader(("Cache-Control"), "max-age=0");
        cssPage = webClient.getPage(cssUrl);
        assertEquals(304, cssPage.getWebResponse().getStatusCode());

         * 
         */ 
    
    }
}
