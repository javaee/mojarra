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

public class StandardIT {

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
    public void testMessage01() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/standard/messages01.jsp");
        assertTrue(Pattern.matches("(?s).*<head>\\s*<title>\\s*messages01.jsp\\s*</title>\\s*<style\\s*type=\"text/css\"\\s*media=\"screen\">.*\\.errors.*</style>\\s*</head>\\s*<body>\\s*<ul\\s*class=\"errors\">\\s*<li>\\s*\\{0\\}:\\s*Validation\\s*Error:\\s*Value\\s*is\\s*required.\\s*</li>\\s*</ul>\\s*</body>.*", page.asXml()));
    }

    @Test
    public void testMessage02() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/standard/messages02.jsp");
        assertTrue(Pattern.matches("(?s).*<head>\\s*<style\\s*type=\"text/css\"\\s*media=\"screen\">.*\\.errors.*</style>\\s*</head>\\s*<body>\\s*<table>\\s*<tbody>\\s*<tr>\\s*<td>\\s*<ul\\s*dir=\"LTR\"\\s*style=\"left:\\s*48px;\\s*top:\\s*100px;\\s*position:\\s*absolute\">\\s*<li\\s*style=\"color:\\s*yellow\">\\s*<span\\s*title=\"Informational\\s*Detail\">\\s*Information\\s*Summary\\s*Informational\\s*Detail\\s*</span>\\s*</li>\\s*<li>\\s*<span\\s*title=\"Warning\\s*Detail\">\\s*Warning\\s*Summary\\s*Warning\\s*Detail\\s*</span>\\s*</li>\\s*<li\\s*style=\"color:\\s*red\">\\s*<span\\s*title=\"Error\\s*Detail\">\\s*Error\\s*Summary\\s*Error\\s*Detail\\s*</span>\\s*</li>\\s*<li\\s*style=\"color:\\s*blue\">\\s*<span\\s*title=\"Fatal\\s*Detail\">\\s*Fatal\\s*Summary\\s*Fatal\\s*Detail\\s*</span>\\s*</li>\\s*</ul>\\s*</td>\\s*</tr>\\s*<tr>\\s*<td>\\s*<table\\s*lang=\"en\"\\s*style=\"left:\\s*48px;\\s*top:\\s*200px;\\s*position:\\s*absolute\">\\s*<tbody>\\s*<tr\\s*style=\"color:\\s*yellow\">\\s*<td>\\s*<span\\s*title=\"Informational\\s*Detail\">\\s*Information\\s*Summary\\s*Informational\\s*Detail\\s*</span>\\s*</td>\\s*</tr>\\s*<tr>\\s*<td>\\s*<span\\s*title=\"Warning\\s*Detail\">\\s*Warning\\s*Summary\\s*Warning\\s*Detail\\s*</span>\\s*</td>\\s*</tr>\\s*<tr\\s*style=\"color:\\s*red\">\\s*<td>\\s*<span\\s*title=\"Error\\s*Detail\">\\s*Error\\s*Summary\\s*Error\\s*Detail\\s*</span>\\s*</td>\\s*</tr>\\s*<tr\\s*style=\"color:\\s*blue\">\\s*<td>\\s*<span\\s*title=\"Fatal\\s*Detail\">\\s*Fatal\\s*Summary\\s*Fatal\\s*Detail\\s*</span>\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*</td>\\s*</tr>\\s*<tr>\\s*<td>\\s*<ul\\s*class=\"errors\">\\s*<li>\\s*Information\\s*Summary\\s*Informational\\s*Detail\\s*</li>\\s*<li>\\s*Warning\\s*Summary\\s*Warning\\s*Detail\\s*</li>\\s*<li>\\s*Error\\s*Summary\\s*Error\\s*Detail\\s*</li>\\s*<li>\\s*Fatal\\s*Summary\\s*Fatal\\s*Detail\\s*</li>\\s*</ul>\\s*</td>\\s*</tr>\\s*<tr>\\s*<td>\\s*<table\\s*style=\"left:\\s*48px;\\s*top:\\s*500px;\\s*position:\\s*absolute\">\\s*<tbody>\\s*<tr\\s*style=\"color:\\s*yellow\">\\s*<td>\\s*Information\\s*Summary\\s*Informational\\s*Detail\\s*</td>\\s*</tr>\\s*<tr>\\s*<td>\\s*Warning\\s*Summary\\s*Warning\\s*Detail\\s*</td>\\s*</tr>\\s*<tr\\s*style=\"color:\\s*red\">\\s*<td>\\s*Error\\s*Summary\\s*Error\\s*Detail\\s*</td>\\s*</tr>\\s*<tr\\s*style=\"color:\\s*blue\">\\s*<td>\\s*Fatal\\s*Summary\\s*Fatal\\s*Detail\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*</body>.*", page.asXml()));
    }

    @Test
    public void testComponent01() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/standard/component01.jsp");
        assertTrue(Pattern.matches("(?s).*<html>\\s*<head>\\s*<title>\\s*component01.jsp\\s*</title>\\s*</head>\\s*<body>\\s*<input\\s*id=\"username\"\\s*type=\"text\"\\s*name=\"username\"\\s*maxlength=\"32\"\\s*onkeypress=\"attrValue\"\\s*size=\"20\"/>\\s*</body>\\s*</html>.*", page.asXml()));
    }

    @Test
    public void testAutoComplete() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/standard/autocomplete.jsp");
        assertTrue(Pattern.matches("(?s).*<html>\\s*<head>\\s*<title>\\s*autocomplete.jsp\\s*</title>\\s*</head>\\s*<body>\\s*autocomplete\\s*on\\s*.*\\s*no\\s*attribute\\s*rendered:\\s*<input\\s*id=\"a\"\\s*type=\"password\"\\s*name=\"a\"\\s*value=\"\"/>\\s*autocomplete\\s*off\\s*.*\\s*attribute\\s*rendered:\\s*<input\\s*id=\"b\"\\s*type=\"password\"\\s*name=\"b\"\\s*autocomplete=\"off\"\\s*value=\"\"/>\\s*no\\s*autocomplete\\s*defined\\s*.*\\s*no\\s*attribute\\s*rendered:\\s*<input\\s*id=\"c\"\\s*type=\"password\"\\s*name=\"c\"\\s*value=\"\"/>\\s*autocomplete\\s*on\\s*.*\\s*no\\s*attribute\\s*rendered:\\s*<input\\s*id=\"d\"\\s*type=\"text\"\\s*name=\"d\"/>\\s*autocomplete\\s*off\\s*.*\\s*attribute\\s*rendered:\\s*<input\\s*id=\"e\"\\s*type=\"text\"\\s*name=\"e\"\\s*autocomplete=\"off\"/>\\s*no\\s*autocomplete\\s*defined\\s*.*\\s*no\\s*attribute\\s*rendered:\\s*<input\\s*id=\"f\"\\s*type=\"text\"\\s*name=\"f\"/>\\s*</body>\\s*</html>.*", page.asXml()));
    }

    @Test
    public void testDataTableColumnClasses() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/standard/dtablecolumnclasses.jsp");
        assertTrue(Pattern.matches("(?s).*<html>\\s*<head>\\s*<title>\\s*dtablecolumnclasses.jsp\\s*</title>\\s*<style\\s*type=\"text/css\">\\s*\\.b1.*\\.b2.*\\.b3.*\\.b4.*\\.b5.*.b6.*\\.b7.*</style>\\s*</head>\\s*<body>\\s*<table>\\s*<tbody>\\s*<tr>\\s*<td\\s*class=\"b1\">\\s*c1\\s*</td>\\s*<td\\s*class=\"b2\">\\s*c2\\s*</td>\\s*<td>\\s*c3\\s*</td>\\s*<td>\\s*c4\\s*</td>\\s*<td>\\s*c5\\s*</td>\\s*<td>\\s*c6\\s*</td>\\s*</tr>\\s*<tr>\\s*<td\\s*class=\"b1\">\\s*c1_1\\s*</td>\\s*<td\\s*class=\"b2\">\\s*c2_1\\s*</td>\\s*<td>\\s*c3_1\\s*</td>\\s*<td>\\s*c4_1\\s*</td>\\s*<td>\\s*c5_1\\s*</td>\\s*<td>\\s*c6_1\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*<table>\\s*<tbody>\\s*<tr>\\s*<td\\s*class=\"b1\">\\s*c1\\s*</td>\\s*<td\\s*class=\"b2\">\\s*c2\\s*</td>\\s*<td\\s*class=\"b3\">\\s*c3\\s*</td>\\s*<td\\s*class=\"b4\">\\s*c4\\s*</td>\\s*<td>\\s*c5\\s*</td>\\s*<td>\\s*c6\\s*</td>\\s*</tr>\\s*<tr>\\s*<td\\s*class=\"b1\">\\s*c1_1\\s*</td>\\s*<td\\s*class=\"b2\">\\s*c2_1\\s*</td>\\s*<td\\s*class=\"b3\">\\s*c3_1\\s*</td>\\s*<td\\s*class=\"b4\">\\s*c4_1\\s*</td>\\s*<td>\\s*c5_1\\s*</td>\\s*<td>\\s*c6_1\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*<table>\\s*<tbody>\\s*<tr>\\s*<td\\s*class=\"b1\">\\s*c1\\s*</td>\\s*<td\\s*class=\"b2\">\\s*c2\\s*</td>\\s*<td\\s*class=\"b3\">\\s*c3\\s*</td>\\s*<td>\\s*c4\\s*</td>\\s*<td>\\s*c5\\s*</td>\\s*<td>\\s*c6\\s*</td>\\s*</tr>\\s*<tr>\\s*<td\\s*class=\"b1\">\\s*c1_1\\s*</td>\\s*<td\\s*class=\"b2\">\\s*c2_1\\s*</td>\\s*<td\\s*class=\"b3\">\\s*c3_1\\s*</td>\\s*<td>\\s*c4_1\\s*</td>\\s*<td>\\s*c5_1\\s*</td>\\s*<td>\\s*c6_1\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*<table>\\s*<tbody>\\s*<tr>\\s*<td\\s*class=\"b1\">\\s*c1\\s*</td>\\s*<td>\\s*c2\\s*</td>\\s*<td>\\s*c3\\s*</td>\\s*<td>\\s*c4\\s*</td>\\s*<td>\\s*c5\\s*</td>\\s*<td>\\s*c6\\s*</td>\\s*</tr>\\s*<tr>\\s*<td\\s*class=\"b1\">\\s*c1_1\\s*</td>\\s*<td>\\s*c2_1\\s*</td>\\s*<td>\\s*c3_1\\s*</td>\\s*<td>\\s*c4_1\\s*</td>\\s*<td>\\s*c5_1\\s*</td>\\s*<td>\\s*c6_1\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*<table>\\s*<tbody>\\s*<tr>\\s*<td>\\s*c1\\s*</td>\\s*<td>\\s*c2\\s*</td>\\s*<td>\\s*c3\\s*</td>\\s*<td>\\s*c4\\s*</td>\\s*<td>\\s*c5\\s*</td>\\s*<td>\\s*c6\\s*</td>\\s*</tr>\\s*<tr>\\s*<td>\\s*c1_1\\s*</td>\\s*<td>\\s*c2_1\\s*</td>\\s*<td>\\s*c3_1\\s*</td>\\s*<td>\\s*c4_1\\s*</td>\\s*<td>\\s*c5_1\\s*</td>\\s*<td>\\s*c6_1\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*<table>\\s*<tbody>\\s*<tr>\\s*<td\\s*class=\"b1\">\\s*c1\\s*</td>\\s*<td\\s*class=\"b2\">\\s*c2\\s*</td>\\s*<td\\s*class=\"b3\">\\s*c3\\s*</td>\\s*<td\\s*class=\"b4\">\\s*c4\\s*</td>\\s*<td\\s*class=\"b5\">\\s*c5\\s*</td>\\s*<td\\s*class=\"b6\">\\s*c6\\s*</td>\\s*</tr>\\s*<tr>\\s*<td\\s*class=\"b1\">\\s*c1_1\\s*</td>\\s*<td\\s*class=\"b2\">\\s*c2_1\\s*</td>\\s*<td\\s*class=\"b3\">\\s*c3_1\\s*</td>\\s*<td\\s*class=\"b4\">\\s*c4_1\\s*</td>\\s*<td\\s*class=\"b5\">\\s*c5_1\\s*</td>\\s*<td\\s*class=\"b6\">\\s*c6_1\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*<table>\\s*<tbody>\\s*<tr>\\s*<td\\s*class=\"b1\">\\s*c1\\s*</td>\\s*<td\\s*class=\"b2\">\\s*c2\\s*</td>\\s*<td\\s*class=\"b3\">\\s*c3\\s*</td>\\s*<td\\s*class=\"b4\">\\s*c4\\s*</td>\\s*<td>\\s*c5\\s*</td>\\s*<td>\\s*c6\\s*</td>\\s*</tr>\\s*<tr>\\s*<td\\s*class=\"b1\">\\s*c1_1\\s*</td>\\s*<td\\s*class=\"b2\">\\s*c2_1\\s*</td>\\s*<td\\s*class=\"b3\">\\s*c3_1\\s*</td>\\s*<td\\s*class=\"b4\">\\s*c4_1\\s*</td>\\s*<td>\\s*c5_1\\s*</td>\\s*<td>\\s*c6_1\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*<table>\\s*<tbody>\\s*<tr>\\s*<td>\\s*c1\\s*</td>\\s*<td\\s*class=\"b2\">\\s*c2\\s*</td>\\s*<td>\\s*c3\\s*</td>\\s*<td>\\s*c4\\s*</td>\\s*<td\\s*class=\"b4\">\\s*c5\\s*</td>\\s*<td\\s*class=\"b5\">\\s*c6\\s*</td>\\s*</tr>\\s*<tr>\\s*<td>\\s*c1_1\\s*</td>\\s*<td\\s*class=\"b2\">\\s*c2_1\\s*</td>\\s*<td>\\s*c3_1\\s*</td>\\s*<td>\\s*c4_1\\s*</td>\\s*<td\\s*class=\"b4\">\\s*c5_1\\s*</td>\\s*<td\\s*class=\"b5\">\\s*c6_1\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*</body>\\s*</html>.*", page.asXml()));
    }
}
