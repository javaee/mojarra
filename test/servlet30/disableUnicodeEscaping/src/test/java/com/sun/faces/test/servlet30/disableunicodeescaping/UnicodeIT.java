/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.servlet30.disableunicodeescaping;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_1_4;
import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_2_1;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;
import static com.sun.faces.test.junit.JsfVersion.JSF_2_3_0_M03;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JsfTestRunner.class)
public class UnicodeIT {

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

    @JsfTest(value = JSF_2_3_0_M03, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    @Test
    public void testUnicodeEscapingTrue() throws Exception {
        webClient = new WebClient();
        webClient.addRequestHeader("Accept-Encoding", "UTF-8");
        HtmlPage page = webClient.getPage(webUrl + "faces/indexUTF.jsp?escape=true");
        assertTrue(
                "Title should contain the unicode characters '\u1234' and '\u00c4'.",
                page.getWebResponse().getContentAsString().contains("a\u1234a")
                && !page.getWebResponse().getContentAsString().contains("a&#4660;a")
                && page.getWebResponse().getContentAsString().contains("b\u00c4b")
                && !page.getWebResponse().getContentAsString().contains("b&Auml;b"));

        webClient = new WebClient();
        webClient.addRequestHeader("Accept-Encoding", "US-ASCII");
        page = webClient.getPage(webUrl + "faces/indexUSASCII.jsp?escape=true");
        assertTrue(
                "Title should contain the unicode characters replaced by ?.",
                !page.getWebResponse().getContentAsString().contains("a\u1234a")
                && page.getWebResponse().getContentAsString().contains("a?a")
                && !page.getWebResponse().getContentAsString().contains("b\u00c4b")
                && page.getWebResponse().getContentAsString().contains("b?b"));

        webClient = new WebClient();
        webClient.addRequestHeader("Accept-Encoding", "ISO-8859-1");
        page = webClient.getPage(webUrl + "faces/indexISO8859_1.jsp?escape=true");
        assertTrue(
                "Title should contain the unicode character replaced by ? but the correct iso character.",
                !page.getWebResponse().getContentAsString().contains("a\u1234a")
                && page.getWebResponse().getContentAsString().contains("a?a")
                && page.getWebResponse().getContentAsString().contains("b\u00c4b")
                && !page.getWebResponse().getContentAsString().contains("b&Auml;b"));
    }

    @Test
    public void testUnicodeEscapingFalse() throws Exception {
        webClient = new WebClient();
        webClient.addRequestHeader("Accept-Encoding", "UTF-8");
        HtmlPage page = webClient.getPage(webUrl + "faces/indexUTF.jsp?escape=false");
        assertTrue(
                "Title should contain the escaped unicode characters only.",
                !page.getWebResponse().getContentAsString().contains("a\u1234a")
                && page.getWebResponse().getContentAsString().contains("a&#4660;a")
                && !page.getWebResponse().getContentAsString().contains("b\u00c4b")
                && page.getWebResponse().getContentAsString().contains("b&Auml;b"));

        webClient = new WebClient();
        webClient.addRequestHeader("Accept-Encoding", "US-ASCII");
        page = webClient.getPage(webUrl + "faces/indexUSASCII.jsp?escape=false");
        assertTrue(
                "Title should contain the escaped unicode characters only.",
                !page.getWebResponse().getContentAsString().contains("a\u1234a")
                && page.getWebResponse().getContentAsString().contains("a&#4660;a")
                && !page.getWebResponse().getContentAsString().contains("b\u00c4b")
                && page.getWebResponse().getContentAsString().contains("b&Auml;b"));

        webClient = new WebClient();
        webClient.addRequestHeader("Accept-Encoding", "ISO-8859-1");
        page = webClient.getPage(webUrl + "faces/indexISO8859_1.jsp?escape=false");
        assertTrue(
                "Title should contain the escaped unicode characters only.",
                !page.getWebResponse().getContentAsString().contains("a\u1234a")
                && page.getWebResponse().getContentAsString().contains("a&#4660;a")
                && !page.getWebResponse().getContentAsString().contains("b\u00c4b")
                && page.getWebResponse().getContentAsString().contains("b&Auml;b"));
    }

    @JsfTest(value = JSF_2_3_0_M03, excludes = {WEBLOGIC_12_1_4, WEBLOGIC_12_2_1})
    @Test
    public void testUnicodeEscapingAuto() throws Exception {
        webClient = new WebClient();
        webClient.addRequestHeader("Accept-Encoding", "UTF-8");
        HtmlPage page = webClient.getPage(webUrl + "faces/indexUTF.jsp?escape=auto");
        assertTrue(
                "Title should contain the unicode characters '\u1234' and '\u00c4'.",
                page.getWebResponse().getContentAsString().contains("a\u1234a")
                && !page.getWebResponse().getContentAsString().contains("a&#4660;a")
                && page.getWebResponse().getContentAsString().contains("b\u00c4b")
                && !page.getWebResponse().getContentAsString().contains("b&Auml;b"));

        webClient = new WebClient();
        webClient.addRequestHeader("Accept-Encoding", "US-ASCII");
        page = webClient.getPage(webUrl + "faces/indexUSASCII.jsp?escape=auto");
        assertTrue(
                "Title should contain the escaped entity '&#4660;' and the escaped umlaut a.",
                !page.getWebResponse().getContentAsString().contains("a\u1234a")
                && page.getWebResponse().getContentAsString().contains("a&#4660;a")
                && !page.getWebResponse().getContentAsString().contains("b\u00c4b")
                && page.getWebResponse().getContentAsString().contains("b&Auml;b"));

        webClient = new WebClient();
        webClient.addRequestHeader("Accept-Encoding", "ISO-8859-1");
        page = webClient.getPage(webUrl + "faces/indexISO8859_1.jsp?escape=auto");
        assertTrue(
                "Title should contain the escaped entity '&#4660;' and the correct iso character.",
                !page.getWebResponse().getContentAsString().contains("a\u1234a")
                && page.getWebResponse().getContentAsString().contains("a&#4660;a")
                && page.getWebResponse().getContentAsString().contains("b\u00c4b")
                && !page.getWebResponse().getContentAsString().contains("b&Auml;b"));
    }
}
