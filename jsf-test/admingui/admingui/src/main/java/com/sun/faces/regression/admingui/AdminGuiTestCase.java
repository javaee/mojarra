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

package com.sun.faces.regression.admingui;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.host.Event;

import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.httpclient.Cookie;


public class AdminGuiTestCase extends HtmlUnitFacesTestCase {

    public AdminGuiTestCase(String name) {
        super(name);
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(AdminGuiTestCase.class));
    }


    // ------------------------------------------------------------ Test Methods

    public void testDeployExerciseUndeploy() throws Exception {

        HtmlPage page = null;
        HtmlSubmitInput button = null;
        
        client.setThrowExceptionOnFailingStatusCode(false);
        CookieManager cm = client.getCookieManager();
        
        cm.clearCookies();
        try {
            page = getPage("/common/index.jsf?bare=true");
        } catch (Exception e) {
            page = getPage("/common/index.jsf");
        }
        Cookie jSessionID = cm.getCookie("JSESSIONID");
        
        Cookie c1 = new Cookie("", "_common_applications_uploadFrame.jsf", "left:0&top:0&badCookieChars:%28%2C%29%2C%3C%2C%3E%2C@%2C%2C%2C%3B%2C%3A%2C%5C%2C%22%2C/%2C%5B%2C%5D%2C%3F%2C%3D%2C%7B%2C%7D%2C%20%2C%09; treeForm_tree-hi=treeForm:tree:applications; JSESSIONID=" + jSessionID.getValue());
        cm.addCookie(c1);
        
        page = getPage("/common/applications/uploadFrame.jsf?bare=true");
        HtmlRadioButtonInput localPackagedFile = (HtmlRadioButtonInput) page.getElementById("form:sheet1:section1:prop1:fileChooseRdBtn");
        page = localPackagedFile.click();
        
        // Set the war path
        HtmlTextInput textInput = (HtmlTextInput) page.getElementById("form:sheet1:section1:prop1:dirPath");
        String dirPathValue = System.getProperty("warfile");
        System.err.println("DIRPATH:"+dirPathValue);
        textInput.setValueAttribute(dirPathValue);
        
        // Set the appType
        List<HtmlSelect> selects = new ArrayList<HtmlSelect>(1);
        selects = getAllElementsOfGivenClass(page.getDocumentElement(), selects, 
                HtmlSelect.class);
        HtmlSelect type = selects.get(0);
        HtmlOption option = type.getOption(1);
        type.setSelectedAttribute(option, true);
        
        // Set the contextRoot
        textInput = (HtmlTextInput) page.getElementById("form:war:psection:cxp:ctx");
        textInput.setValueAttribute("admingui_test_war");
        
        // Set the appName
        textInput = (HtmlTextInput) page.getElementById("form:appClient:psection:nameProp:appName");
        textInput.setValueAttribute("admingui_test_war");
        textInput.fireEvent(Event.TYPE_CHANGE);

        // Submit the app
        button = (HtmlSubmitInput) 
                page.getElementById("form:title:topButtons:uploadButton");
        button.focus();
        client.setJavaScriptEnabled(false);
        page = button.click();
        client.setJavaScriptEnabled(true);
        
        // Now we visit the deployed app and verify it is successfully deployed
        this.port = 8080;
        client.setThrowExceptionOnFailingStatusCode(true);
        cm.clearCookies();
        Thread.currentThread().sleep(20000L);
        page = getPage("/admingui_test_war/faces/main.xhtml");
        assertTrue(page.asXml().contains("javax.faces.ViewState"));
                
        // Now we undeploy the app using the GUI
        this.port = 4848;
        cm.clearCookies();
        client.setThrowExceptionOnFailingStatusCode(false);

        try {
            page = getPage("/common/index.jsf?bare=true");
        } catch (Exception e) {
            page = getPage("/common/index.jsf");
        }
    
        jSessionID = cm.getCookie("JSESSIONID");
        
        c1 = new Cookie("", "_common_applications_uploadFrame.jsf", "left:0&top:0&badCookieChars:%28%2C%29%2C%3C%2C%3E%2C@%2C%2C%2C%3B%2C%3A%2C%5C%2C%22%2C/%2C%5B%2C%5D%2C%3F%2C%3D%2C%7B%2C%7D%2C%20%2C%09; treeForm_tree-hi=treeForm:tree:applications; JSESSIONID=" + jSessionID.getValue());
        cm.addCookie(c1);
        
        client.setJavaScriptEnabled(false);
        
        page = getPage("/common/applications/applications.jsf?bare=true");
        // In the table of deployed apps, find the one in which we are interested.
        List<HtmlAnchor> links = page.getAnchors();
        HtmlAnchor appLink = null;
        for (HtmlAnchor cur : links) {
            String href = cur.getHrefAttribute();
            String hrefId = cur.getId();
            if (null != href && href.contains("admingui_test_war") &&
                null != hrefId && hrefId.contains(":link") && hrefId.contains(":deployTable")) {
                appLink = cur;
                break;
            }
        }
        String id = appLink.getId();
        String checkboxId = id.replace("col1:link", "col0:select");
        HtmlCheckBoxInput myCheckbox = (HtmlCheckBoxInput) page.getElementById(checkboxId);
        page = (HtmlPage) myCheckbox.setChecked(true);
        
        // remove the app
        button = (HtmlSubmitInput) 
          page.getElementById("propertyForm:deployTable:topActionsGroup1:button1");
        button.removeAttribute("disabled");
        
        page = button.click();
        
    }


}
