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
package com.sun.faces.test.weblogic.wls1214.elBackwardCompatible;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import static org.junit.Assert.assertTrue;

import static com.sun.faces.test.junit.JsfServerExclude.*;
import static com.sun.faces.test.junit.JsfVersion.JSF_2_2_8_03;
import com.sun.faces.test.junit.JsfTest;

public class Issue3454IT {
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
    @JsfTest(value = JSF_2_2_8_03, excludes = { 
        GLASSFISH_4_0,
        GLASSFISH_3_1_2_2,
        TOMCAT_7_0_35,
        WEBLOGIC_12_1_1,
        WEBLOGIC_12_1_2,
        WEBLOGIC_12_1_3
    })
    public void testBackwardCompatibleFlag() throws Exception {
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        HtmlPage page = webClient.getPage(webUrl + "faces/elBackwardCompatible.jspx");
        String pageXml = page.asXml();

        assertTrue(pageXml.contains("table:0:input"));
        assertTrue(pageXml.contains("table:1:input"));
        assertTrue(pageXml.contains("table:2:input"));        

        page = webClient.getPage(webUrl + "faces/elBackwardCompatible.jspx?isBackwardCompatible22=true&forceSetFlag=true");
        pageXml = page.asXml();

        assertTrue(pageXml.contains("table:0:input"));
        assertTrue(pageXml.contains("table:1:input"));
        assertTrue(pageXml.contains("table:2:input"));        
        
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        page = webClient.getPage(webUrl + "faces/elBackwardCompatible.jspx?isBackwardCompatible22=false&forceSetFlag=true");
        pageXml = page.asXml();

        assertTrue(!pageXml.contains("table:0:input"));
        assertTrue(!pageXml.contains("table:1:input"));
        assertTrue(!pageXml.contains("table:2:input"));        
        
    }
}
