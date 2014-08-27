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
package com.sun.faces.systest;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_1_3;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;
import static com.sun.faces.test.junit.JsfVersion.JSF_2_2_0;
import java.util.regex.Pattern;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JsfTestRunner.class)
public class ManagedBeanIT {

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

    /*
     * Managed Bean Create #1 (No Property Setters) 
     */
    @Test
    public void testManagerBean1() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/managed01.jsp");
        assertTrue(Pattern.matches("(?s).*/managed01.jsp PASSED.*", page.asXml()));
    }

    /*
     * Managed Bean Create #2 (Primitive Property Setters)
     */
    @Test
    public void testManagerBean2() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/managed02.jsp");
        assertTrue(Pattern.matches("(?s).*/managed02.jsp PASSED.*", page.asXml()));
    }

    @JsfTest(value=JSF_2_2_0, excludes = {WEBLOGIC_12_1_3})
    @Test
    public void testManagedBean3() throws Exception {
        webClient.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = webClient.getPage(webUrl + "faces/managed03.jsp");
        assertEquals(500, page.getWebResponse().getStatusCode());
        webClient.setThrowExceptionOnFailingStatusCode(true);
    }

    @JsfTest(value=JSF_2_2_0, excludes = {WEBLOGIC_12_1_3})
    @Test
    public void testManagedBean7() throws Exception {
        webClient.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = webClient.getPage(webUrl + "faces/managed07.jsp");
        assertEquals(500, page.getWebResponse().getStatusCode());
        webClient.setThrowExceptionOnFailingStatusCode(true);
    }

    @Test
    public void testEagerBean() throws Exception {
        webClient.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = webClient.getPage(webUrl + "faces/eagerbean.jsp");
        assertEquals(200, page.getWebResponse().getStatusCode());
        webClient.setThrowExceptionOnFailingStatusCode(true);
    }
}
