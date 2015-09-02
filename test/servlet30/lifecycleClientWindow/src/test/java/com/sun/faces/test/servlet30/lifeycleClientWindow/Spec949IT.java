/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.test.servlet30.lifeycleClientWindow;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.net.URL;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class Spec949IT {

    private String webUrl;
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(120000);
    }

    @After
    public void tearDown() {
        webClient.closeAllWindows();
    }

    @Test
    public void testClientWindow() throws Exception {
        String clientWindow1, clientWindow2, window1Session, window2Session;
        clientWindow1 = doTestAndReturnClientWindow(webClient, "window0");
        clientWindow2 = doTestAndReturnClientWindow(webClient, "window1");
        assertNotSame(clientWindow1, clientWindow2);
        window1Session = clientWindow1.substring(0, clientWindow1.indexOf(':'));
        window2Session = clientWindow2.substring(0, clientWindow2.indexOf(':'));
        assertEquals(window1Session, window2Session);

    }

    @Test
    public void testDisableClientWindow() throws Exception {
        doTestClientWindowsDifferent("/faces/disableClientWindow.xhtml", "disableClientWindowEL");

        doTestClientWindowsDifferent("/faces/disableClientWindow.xhtml", "disableClientWindowLiteral");

        doTestClientWindowsDifferent("/faces/disableClientWindow.xhtml", "disableClientWindowButtonEL");

        doTestClientWindowsDifferent("/faces/disableClientWindow.xhtml", "disableClientWindowButtonLiteral");
    }
    
    public void doTestClientWindowsDifferent(String path, String id) throws Exception {
        HtmlPage page;
        List<DomElement> clientWindowHiddenFields;
        HtmlElement link;
        String clientWindowBeforeClick, clientWindowAfterClick;
        
        // Click the link and verify the ClientWindow is different on the new page.
        page = webClient.getPage(webUrl + path);
        link = (HtmlElement) page.getElementById(id);
        clientWindowHiddenFields = page.getElementsByName("javax.faces.ClientWindow");
        clientWindowBeforeClick = ((HtmlInput)clientWindowHiddenFields.get(0)).getDefaultValue();
        
        page = link.click();
        clientWindowHiddenFields = page.getElementsByName("javax.faces.ClientWindow");
        clientWindowAfterClick = ((HtmlInput)clientWindowHiddenFields.get(0)).getDefaultValue();
        
        assertNotSame("ClientWindow should not be the same on second page", clientWindowBeforeClick, clientWindowAfterClick);
        
    }
    
    public String doTestAndReturnClientWindow(WebClient yourClient, String windowName) throws Exception {
        String clientWindow;
        
        // 
        // Do some actions on this page
        //
        HtmlPage page = (HtmlPage) yourClient.openWindow(new URL(webUrl), windowName).getEnclosedPage();
        HtmlTextInput textField = (HtmlTextInput) page.getElementById("firstName");
        textField.setValueAttribute("ajaxFirstName");
        
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("submitAjax");
        page = button.click();
        webClient.waitForBackgroundJavaScript(120000);
        String pageText = page.asText();
        assertTrue(pageText.contains("|ajaxFirstName|"));

        final String clientWindowLabel = "ClientWindow: ";
        int clientWindowLabelIndex = pageText.indexOf(clientWindowLabel);
        clientWindow = pageText.substring(clientWindowLabelIndex + clientWindowLabel.length());
        
        
        textField = (HtmlTextInput) page.getElementById("firstName");
        textField.setValueAttribute("nonAjaxFirstName");
        
        button = (HtmlSubmitInput) page.getElementById("submitNonAjax");
        page = button.click();
        webClient.waitForBackgroundJavaScript(120000);
        pageText = page.asText();
        assertTrue(pageText.contains("|nonAjaxFirstName|"));
        
        
        // 
        // visit another page via a commandLink
        //
        HtmlAnchor link = (HtmlAnchor) page.getElementById("commandLink");
        page = link.click();
        button = (HtmlSubmitInput) page.getElementById("getClientWindow");
        page = button.click();
        webClient.waitForBackgroundJavaScript(120000);
        pageText = page.asText();
        clientWindowLabelIndex = pageText.indexOf(clientWindowLabel);
        String newPageClientWindow = pageText.substring(clientWindowLabelIndex + clientWindowLabel.length());
        assertEquals(clientWindow, newPageClientWindow);
        
        // 
        // Go back to the first page
        //
        button = (HtmlSubmitInput) page.getElementById("back");
        page = button.click();
        
        // 
        // visit another page via an h:link
        //
        link = (HtmlAnchor) page.getElementById("outcomeTargetLink");
        page = link.click();
        button = (HtmlSubmitInput) page.getElementById("getClientWindow");
        page = button.click();
        webClient.waitForBackgroundJavaScript(120000);
        pageText = page.asText();
        clientWindowLabelIndex = pageText.indexOf(clientWindowLabel);
        newPageClientWindow = pageText.substring(clientWindowLabelIndex + clientWindowLabel.length());
        assertEquals(clientWindow, newPageClientWindow);
        
        return clientWindow;
    }    
}
