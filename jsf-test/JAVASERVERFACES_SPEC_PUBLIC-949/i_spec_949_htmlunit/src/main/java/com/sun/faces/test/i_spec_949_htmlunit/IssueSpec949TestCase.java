/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.faces.test.i_spec_949_htmlunit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


public class IssueSpec949TestCase extends HtmlUnitFacesTestCase {

    public IssueSpec949TestCase(String name) {
        super(name);
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(IssueSpec949TestCase.class));
    }


    // ------------------------------------------------------------ Test Methods

    public void testWindowId() throws Exception {
        String windowId1, windowId2, window1Session, window2Session;
        windowId1 = doTestAndReturnWindowId(client, "window0");
        windowId2 = doTestAndReturnWindowId(client, "window1");
        assertNotSame(windowId1, windowId2);
        window1Session = windowId1.substring(0, windowId1.indexOf(':'));
        window2Session = windowId2.substring(0, windowId2.indexOf(':'));
        assertEquals(window1Session, window2Session);

    }
    
    public String doTestAndReturnWindowId(WebClient yourClient, String windowName) throws Exception {
        String windowId = null;
        
        HtmlPage page = (HtmlPage) yourClient.openWindow(getURL("/"), windowName).getEnclosedPage();
        HtmlTextInput textField = (HtmlTextInput) page.getElementById("firstName");
        textField.setValueAttribute("ajaxFirstName");
        
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("submitAjax");
        page = button.click();
        Thread.sleep(2000);
        String pageText = page.asText();
        assertTrue(pageText.contains("|ajaxFirstName|"));

        final String windowIdLabel = "WindowId: ";
        int windowIdLabelIndex = pageText.indexOf(windowIdLabel);
        windowId = pageText.substring(windowIdLabelIndex + windowIdLabel.length());
        
        
        textField = (HtmlTextInput) page.getElementById("firstName");
        textField.setValueAttribute("nonAjaxFirstName");
        
        button = (HtmlSubmitInput) page.getElementById("submitNonAjax");
        page = button.click();
        Thread.sleep(2000);
        pageText = page.asText();
        assertTrue(pageText.contains("|nonAjaxFirstName|"));
        
        return windowId;
    }

    

}
