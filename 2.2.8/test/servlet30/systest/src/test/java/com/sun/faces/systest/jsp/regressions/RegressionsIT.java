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
package com.sun.faces.systest.jsp.regressions;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RegressionsIT {

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
    public void testAreaTextRowsAttrTest() throws Exception {
        webClient.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = webClient.getPage(webUrl + "faces/regression/AreaTextRowsAttrTest.jsp");
        assertTrue(Pattern.matches("(?s).*<html>\\s*<head>\\s*<title>\\s*Text\\s*Area\\s*Row\\s*Attribute\\s*Regression\\s*Test\\s*</title>\\s*</head>\\s*<body>\\s*<textarea\\s*name=\".*\"\\s*rows=\"30\">\\s*</textarea>\\s*</body>\\s*</html>.*", page.asXml()));
    }

    @Test
    public void testSelectOneManySizeAttrTest() throws Exception {
        webClient.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = webClient.getPage(webUrl + "faces/regression/SelectOneManySizeAttrTest.jsp");
        assertTrue(Pattern.matches("(?s).*<html>\\s*<head>\\s*<title>\\s*Select.One,Many.ListBox\\s*Size\\s*Attribute\\s*Test\\s*</title>\\s*</head>\\s*<body>\\s*<select\\s*name=\".*\"\\s*size=\"5\">\\s*<option\\s*value=\"val1\">\\s*val1\\s*</option>\\s*</select>\\s*<select\\s*name=\".*\"\\s*multiple=\"multiple\"\\s*size=\"5\">\\s*<option\\s*value=\"val1\">\\s*val1\\s*</option>\\s*</select>\\s*</body>\\s*</html>.*", page.asXml()));
    }
}
