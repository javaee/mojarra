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
package com.sun.faces.test.javaee6.correctscanningtest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class Issue1995IT {

    private String webUrl;
    private WebClient webClient;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /*
     * The webUrl is http://localhost:8080/test-javaee6web-correctScanningWar/
     * and we strip off the / here so we can later use it to write the 2 tests
     * below to point to the 2 web applications in the EAR.
     */
    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webUrl = webUrl.substring(0, webUrl.length() - 1);
        webClient = new WebClient();
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    @Test
    @Ignore
    public void testScanning1() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "1/faces/index.xhtml");
        assertTrue(page.asXml().contains("javax.faces.ViewState"));
        assertTrue(page.asText().matches("(?s).*.war_1\\s+bean:\\s+war1Bean\\s+war_2\\s+bean:\\s+bean:\\s+bar..*"));
    }

    @Test
    @Ignore
    public void testScanning2() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "2/faces/index.xhtml");
        assertTrue(page.asXml().contains("javax.faces.ViewState"));
        assertTrue(page.asText().matches("(?s).*.war_1\\s+bean:\\s+war_2\\s+bean:\\s+war2Bean\\s+bean:\\s+bar..*"));
    }
}
