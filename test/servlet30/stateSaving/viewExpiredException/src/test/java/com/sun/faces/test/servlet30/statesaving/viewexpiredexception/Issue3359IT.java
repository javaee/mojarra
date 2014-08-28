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
package com.sun.faces.test.servlet30.statesaving.viewexpiredexception;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class Issue3359IT {
    private String webUrl;
    private WebClient webClient;
    
    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    @Test
    public void testIssue3359Fixed() throws Exception {
        HtmlPage page = webClient.getPage(webUrl);
        
        String savingMethod = ((HtmlInput)page.getElementById("helloForm:stateSavingMethod")).getValueAttribute();
        String origStateId =  ((HtmlHiddenInput)page.getElementByName("javax.faces.ViewState")).getValueAttribute();
        
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("helloForm:button"); 
        page = button.click();
        
        HtmlAnchor anchor = (HtmlAnchor)page.getElementById("link");
        page = anchor.click();
        
        button = (HtmlSubmitInput) page.getElementById("helloForm:button");
        HtmlHiddenInput hi = (HtmlHiddenInput)(page.getElementByName("javax.faces.ViewState"));
        hi.setValueAttribute(origStateId);
        page = button.click();
        
        if ( "client".equalsIgnoreCase( savingMethod ) ) {
            assertTrue(page.asXml().indexOf("Go back to the index page") != -1);
        } else {
            assertTrue(page.asXml().indexOf("ViewExpiredException") != -1);
        }
    }

}
