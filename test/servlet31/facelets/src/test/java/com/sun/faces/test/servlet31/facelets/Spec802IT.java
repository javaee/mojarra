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
 * https://glassfish.java.net/public/CDDLGPL_1_1.html
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
package com.sun.faces.test.servlet31.facelets;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.test.junit.JsfServerExclude;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;
import com.sun.faces.test.junit.JsfVersion;
import java.io.File;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(JsfTestRunner.class)
public class Spec802IT {

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

    @JsfTest(value = JsfVersion.JSF_2_2_0, excludes = {JsfServerExclude.WEBLOGIC_12_1_3})
    @Test
    public void testFileUpload() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/inputFile.xhtml");
        HtmlTextInput text;

        String basedir = System.getProperty("basedir");
        HtmlFileInput fileInput = (HtmlFileInput) page.getElementById("file");
        fileInput.setValueAttribute(basedir + File.separator + "inputFileSuccess.txt");

        text = (HtmlTextInput) page.getElementById("text");
        String textValue = "" + System.currentTimeMillis();
        text.setText(textValue);

        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("button");

        page = button.click();

        String pageText = page.getBody().asText();
        assertTrue(pageText.contains("JSR-344"));

        pageText = page.getElementById("textOutput").getTextContent();
        assertTrue(pageText.contains(textValue));

        page = webClient.getPage(webUrl + "faces/inputFile.xhtml");

        fileInput = (HtmlFileInput) page.getElementById("file");
        fileInput.setValueAttribute(basedir + File.separator + "inputFileFailure.txt");
        button = (HtmlSubmitInput) page.getElementById("button");

        text = (HtmlTextInput) page.getElementById("text");
        textValue = "" + System.currentTimeMillis();
        text.setText(textValue);

        page = button.click();

        pageText = page.getBody().asText();
        assertFalse(pageText.contains("JSR-344"));
        assertTrue(pageText.contains("Invalid file"));

        pageText = page.getElementById("textOutput").getTextContent();
        assertTrue(!pageText.contains(textValue));
    }

    @JsfTest(value = JsfVersion.JSF_2_2_0, excludes = {JsfServerExclude.WEBLOGIC_12_1_3})
    @Test
    public void testFileUploadMultipleTimes() throws Exception {
        webClient = new WebClient(BrowserVersion.FIREFOX_31);
        HtmlPage page = webClient.getPage(webUrl + "faces/uploadMultipleTimes.xhtml");

        String basedir = System.getProperty("basedir");
        HtmlFileInput fileInput = (HtmlFileInput) page.getElementById("file");
        fileInput.setValueAttribute(basedir + File.separator + "inputFileSuccess.txt");

        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("button");

        page = button.click();

        String pageText = page.getBody().asXml();
        assertTrue(pageText.contains("(?s).*bytes\\s+sent\\s+=\\s+83.*"));

        fileInput = (HtmlFileInput) page.getElementById("file");
        fileInput.setValueAttribute(basedir + File.separator + "inputFileSuccess2.txt");

        button = (HtmlSubmitInput) page.getElementById("button");

        page = button.click();

        pageText = page.getBody().asXml();
        assertTrue(pageText.matches("(?s).*bytes\\s+sent\\s+=\\s+107.*"));

        fileInput = (HtmlFileInput) page.getElementById("file");
        fileInput.setValueAttribute(basedir + File.separator + "inputFileSuccess3.txt");

        button = (HtmlSubmitInput) page.getElementById("button");

        page = button.click();

        pageText = page.getBody().asXml();
        assertTrue(pageText.matches("(?s).*bytes\\s+sent\\s+=\\s+124.*"));
    }

    @JsfTest(value = JsfVersion.JSF_2_2_0, excludes = {JsfServerExclude.WEBLOGIC_12_1_3})
    @Test
    public void testFileUploadNoEncType() throws Exception {
        webClient = new WebClient();
        HtmlPage page = webClient.getPage(webUrl + "faces/inputFileNoEncType.xhtml");
        if (page.asText().contains("ProjectStage.Development")) {
            assertTrue(page.asText().contains(
                    "File upload component requires a form with an enctype of multipart/form-data"));
        }
    }
}
