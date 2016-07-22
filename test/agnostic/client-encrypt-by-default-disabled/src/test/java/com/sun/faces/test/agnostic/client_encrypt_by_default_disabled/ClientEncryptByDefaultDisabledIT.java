/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2016 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.agnostic.client_encrypt_by_default_disabled;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Verify a simple facelet page.
 * 
 */
public class ClientEncryptByDefaultDisabledIT {
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

    /*
     * From: Jake Evans
     *  Here are the steps to reproduce the issue we're seeing.
     *
     * Steps to build payload:
     * 1. download ysoserial-0.0.3-all.jar
     * 2. create payload: java -jar ysoserial-0.0.3-all.jar CommonsCollections1 'touch /tmp/ClientStateSavingPasswordIneffective.txt' > payload.out
     * 3. gzip payload: gzip -f payload.out
     * 4. base64-encode payload: base64 -w 0 payload.out.gz > payload.out.b64
     */

    public static final String touchTmpClientStateSavingPasswordIneffectiveTxt = 
"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";


    @Ignore @Test
    public void testClientStateEncrypted() throws Exception {
        HtmlPage page = webClient.getPage(webUrl);

        WebClient client = page.getWebClient();
        webClient.setThrowExceptionOnFailingStatusCode(false);


	HtmlHiddenInput stateField = (HtmlHiddenInput) page.getHtmlElementById("javax.faces.ViewState");
	stateField.setValueAttribute(touchTmpClientStateSavingPasswordIneffectiveTxt);
	HtmlTextInput textField = (HtmlTextInput) page.getHtmlElementById("userNo");
	textField.setValueAttribute("5");

	HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("submit");

	page = (HtmlPage) button.click();

        assertTrue(-1 != page.asText().indexOf("Guess The Number"));
    }
}
