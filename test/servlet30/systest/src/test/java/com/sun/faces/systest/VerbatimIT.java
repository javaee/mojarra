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
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VerbatimIT {

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
    public void testVerbatimTest() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/verbatim_test.jsp");
        assertTrue(Pattern.matches("(?s).*\\s*<html>\\s*<head>\\s*<title>\\s*Test\\s*of\\s*the\\s*Verbatim\\s*Tag\\s*</title>\\s*</head>\\s*<body>\\s*<h1>\\s*Test\\s*of\\s*the\\s*Verbatim\\s*Tag\\s*</h1>\\s*<p>\\s*\\[DEFAULT\\]\\s*This\\s*text\\s*<b>\\s*has\\s*angle\\s*brackets\\s*</b>\\s*.\\s*The\\s*angle\\s*brackets\\s*MUST\\s*NOT\\s*be\\s*escaped.\\s*</p>\\s*<p>\\s*\\[FALSE\\]\\s*This\\s*text\\s*<b>\\s*has\\s*angle\\s*brackets\\s*</b>\\s*.\\s*The\\s*angle\\s*brackets\\s*MUST\\s*NOT\\s*be\\s*escaped.\\s*</p>\\s*<p>\\s*\\[TRUE\\]\\s*This\\s*text\\s*&lt;b&gt;has\\s*angle\\s*brackets&lt;/b&gt;.\\s*The\\s*angle\\s*brackets\\s*MUST\\s*be\\s*escaped.\\s*</p>\\s*<p>\\s*This\\s*text\\s*is\\s*rendered.\\s*</p>\\s*</body>\\s*</html>.*", page.asXml()));
    }

    @Test
    public void testEscapeTest() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/escape_test.jsp");
        assertTrue(Pattern.matches("(?s).*<html>\\s*<head>\\s*<title>\\s*Test\\s*of\\s*outputText\\s*Escaping\\s*</title>\\s*</head>\\s*<body>\\s*<h1>\\s*Test\\s*of\\s*outputText\\s*Escaping\\s*</h1>\\s*<p>\\s*\\[DEFAULT\\]\\s*This\\s*text\\s*&lt;b&gt;has\\s*angle\\s*brackets&lt;/b&gt;.\\s*The\\s*angle\\s*brackets\\s*MUST\\s*be\\s*escaped.\\s*</p>\\s*<p>\\s*\\[FALSE\\]\\s*This\\s*text\\s*<b>\\s*has\\s*angle\\s*brackets\\s*</b>\\s*.\\s*The\\s*angle\\s*brackets\\s*MUST\\s*NOT\\s*be\\s*escaped.\\s*</p>\\s*<p>\\s*\\[TRUE\\]\\s*This\\s*text\\s*&lt;b&gt;has\\s*angle\\s*brackets&lt;/b&gt;.\\s*The\\s*angle\\s*brackets\\s*MUST\\s*be\\s*escaped.\\s*</p>\\s*</body>.*</html>.*", page.asXml()));
    }
}
