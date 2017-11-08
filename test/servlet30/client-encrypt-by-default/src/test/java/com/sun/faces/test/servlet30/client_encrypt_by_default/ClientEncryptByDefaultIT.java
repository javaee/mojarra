/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.test.servlet30.client_encrypt_by_default;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import static com.sun.faces.test.junit.JsfServerExclude.WEBLOGIC_12_2_1;
import com.sun.faces.test.junit.JsfTest;
import com.sun.faces.test.junit.JsfTestRunner;
import static com.sun.faces.test.junit.JsfVersion.JSF_2_3_0_M07;

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 * Verify a simple facelet page.
 *
 */
@RunWith(JsfTestRunner.class)
public class ClientEncryptByDefaultIT {

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
        webClient.close();
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
    public static final String touchTmpClientStateSavingPasswordIneffectiveTxt
            = "H4sIAAAAAAAAAKVYXWgcRRyfXD4uSVObD422NvZsg21ps4lNWzSxSJImzdLLJeQu0bYP6dzdJLfpfmV29j5aGu2DClVBUB+EioJCEVpR+uaDUkSFQkULKogPRR9EsD5IwY8H9T9zu3ub5C57wSXM7ezM/Oc//9/v/zG5cgfV2xR1nIou4iyWVKwvSJPJRZJig698+fRbrdZeNYRQ3kQINVoU9acMTbJsXZrHKWJJ2DRVJYWZYuhSnGFGJrCOFwiVNVPtTlBCYkaa3J3/6OOrfcc+aeFycn2IPxG+W96RAjJNQyc6k2bkWYXkpg2DocbFOSUNf315awkto1DuoFi4q/zCDNNUaRyaMYNqqClDVNXgr+7ix2FpDdobsHjSZqbNEiRf2v7A/5bQ70oYEBL2BUg4RrGZUVKyBnZE4RzOEllbcEUcFiL2BIiQdVeHBtsiNGas1GB/wPIRQ9Ownh62GTN01GDZSU1hrohDQsQjASImiGUJ/QmlBrUezdt8NX9CZd4aTHi83pZliqRK9Bhx37unqGESygrHScFCztMODKPonhKTR3Vb8w+aDDVjxqiStBmoCLwXc22mqNIQpbgQVSyWv3Cr640v8Ju1qEZGdZZylgj61+fqeAuLKpxeOMC4oaYJjQNs9MSNa0devXRzIoRCUdSUUrFlxbBGGGoXvtbLNeyNgzL6wmAUNVuwJi1kMNRZnKEYvXFCFawqZ3FSJYN508xS1FbSeRxbmQls1oe/v/5p5+mva1FoDDWrBk6P4RQzqIyaWIYSKwNK5U2B4H1ggm6//jmSBC+WRmamp0djiblZefSpuenJyYQzm7fbvZWRcisnJ6YmY3ytfDTuzBQ/HZwqwmg1zO+SeQD40MYAnqJKFgzjh5Lv0MrQvSU4ExnMhiiJE+ZtXyu2r3W0yorPZQ7G2x1crZ6gsFSRdo46myjRAf/jCpPTwLXxxER0bngoLo8s8wn7wCNVI4VVAixqLcEYFd+W/2w7fanvr19rUYOMGjMAbQqiZxSFU4atM1rgEjqBKODYRLcg5FrOl0bOJBv8zek3WCmqmMzphbMYKKQXu3nzX3gYQrzXz1CI6OJNNPmSktiCA1gu14QvyTojENrbf3z73T8uvPBYiHtHfRarNsm7hxHzYraWJPT5K693bXrt9otu8qj3ZGfBlNw4m3sXIEEwYL+0aJlieAtDLUUDEpoomATIXpc1lDQqPUDjFVoNG4ZKsH4zQp/95tLfv4FWJ12tTOTLIqX4UhMYgZr4S1sFEvF2m583qynfLKY3M1TP9z7gSyXOp35fbnA+HWReqPeCNnNDrzPnECuFU0G37nU03OO5bJjoKQa2XE31CplsRuY+GkTzJpMSE4CS0/zDMG+OsCIJXJFcjDe4s2TpDWSCpupPWcfJ4PUcAricakwqehp4ZlUJ6aILKUUPufUOUaVZLnQ0D0e3uPfxGuf6jWt3fhjt/KBFJJ4HiudfO/XiM7ePXm7sPhES0zq8aaUZ7zx3Mf77yVtPCHfJ9aCtu87NcBYIXxoGgkuaoiuarZ33u1qxUDNNEVIPV12aBKErjAlWJPrcTLwctlyIh22sBBjaALjhleCGBCKhMuAKRIRqZzaKH/9ZKm9NnF/HmuLMvCl4p5wSks8GHqsekKhQYM3ITm0XZP8ifXmE5DFB0khaWlDmHSDa/MJFlejpOF1JxbpgV6uck9eWlYH6qzhJVEiG3OiRotU3gvZLG0L7pZVo96Hta9C2vW71gMf95qkp7sFQr99GAJOSxlBnSROEZYx0yZ1n3RG4Wt3vXK0UQ1pRzL3/VdOV+Hd3ZDdDhny7UbTDF3hWS+eRp5le+/bUk13ZYuTZ6oWU1XNx+93u4Z+WWMiBx8rtRNvWGMg5CTlfzLerL4ROkXr11uzPv3SdO+ZqXMtW3+OgRMgzaYz3Roodhh4OLPVYWVCqdLjweg7nXGWCCNsK1TLYqmQ1AGDnOgBEFUYoVl/eev6fU+yzuhZH0uXcgyhs2SnQwlp7JKHxe3znYmL6sOToDXGR6B0Pb/efpngL81iZqM4om6t1N9DBYgWV8ATSX92lLtD7hUCGoHpVDToQoSQ9GJkHNvTMY01RCwOR3TGSi4yAbJsWIvFUBkq4pGGc2b0/AqxU5p3ZQsxAxEiqypJNBiOcTT1pkjKo+H8DjMAdS1V0sh4BqtS5Ga5IuaOEYUUVphYAfQ6Vw7xBZx2MilWZO7KJr4jbQC9aELYcqnHg6/Dr4uzv4Tcr8PsPK+niC3URAAA=";

    @JsfTest(value = JSF_2_3_0_M07, excludes = {WEBLOGIC_12_2_1})
    @Test
    public void testClientStateEncrypted() throws Exception {
        HtmlPage page = webClient.getPage(webUrl);

        WebClient client = page.getWebClient();
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        HtmlHiddenInput stateField = (HtmlHiddenInput) page.getHtmlElementById("j_id_id6:javax.faces.ViewState:0");
        stateField.setValueAttribute(touchTmpClientStateSavingPasswordIneffectiveTxt);
        HtmlTextInput textField = (HtmlTextInput) page.getHtmlElementById("userNo");
        textField.setValueAttribute("5");

        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("submit");

        page = (HtmlPage) button.click();

        assertTrue(-1 == page.asText().indexOf("Guess The Number"));
    }
}
