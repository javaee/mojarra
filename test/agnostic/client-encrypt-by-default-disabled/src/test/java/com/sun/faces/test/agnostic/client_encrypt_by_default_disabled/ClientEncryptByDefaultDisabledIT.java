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
	"H4sICPY0vlYAA3BheWxvYWQub3V0AJVUTUwTQRR+XagISGhrRBOCITFGJbpr1AOhB0USpElBIkiIvTi003Zhd3adnV22oCR6kIMXDjXEi3LAk2hiPHswHkxESfSi4aQmRk000YPxqDO7S9mKvz1sX3fm+973vvf6lj5B1KJwyLKJTHFew1kmI0IMhphqELm7EqaIY2S9qA+RnIbp6cdfm7ctz9yUQErDFh3rY5iOIM3GFoPm9DhykGIzVVP6kZlMQy0rmZhB3D/QECkoPRqyrKRrWhcAIAJN4kQWEJlDXArbvRfiakXZIDXc0us9q+1PYj3LEkTSECky2B0iDW4qG+SKROdgBoBX22HQgoxMlC1iOWvoukEs/q0JoCpiHZlyGk2VuA7ytnzpeilWrhHJ6vIoywxaYrA/zSkUn0IJKJQQhTJMEbHyBtX9zBS6/pIzbxPBbck9RaQSnAsRHHw0/3Hv1HwtLzgDTWrohFt9IPN/UmwaQP4kJgy5P9Lx5eWRHdckANfknRLjkvznYvgvhggLEY46c/Fu6W7Za1+9unaDQSLUxpNj45yLy3UoxNfn4JRNmKpjWP/8n7diKiYwDamZff99Yvr8SqcENRmIqt20wD3dmtkoJQ2Naj9mRSM3gHRcrXaIUZUUkhl+ZRBRpA/zWec0icwvpp37H9D7JQX0cyujN2LWPm3NZYlBQwGzoGAOSoRBPtmtlhd3lp+eqXQGGNRzjK/SFsO+UzBxC2PrUF/rwufFzqnkveMe1PGuev+OFg/WKmBmJeS8m1TPud+R+kVUtcUjbQ1If6o58Ov285F3H9qmT6zpjzA4zAw7W2xXmG5yx1RM2BBfP3gIORwwyIueNGguRXA+L3rrYJm5fHJqsYuzFW0REezyMsetqvFJEYYLmCbeLCx+uzjLex5JQdQRO8utKmfAFsvs8tLVtsbyqysVeRUyb0n1IavIF0S0bvXBw5azz2pA6oUGzUC5Xm9JpKCeFSm2ioaWc82jxzxbYpObxdNzyOUWJkIWOphSNVc926KGrh8WRjW6owUAAA==";

    @Test
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

	assertTrue(-1 != page.asText().indexOf("org.apache.commons.collections.map.LazyMap"));
    }
}
