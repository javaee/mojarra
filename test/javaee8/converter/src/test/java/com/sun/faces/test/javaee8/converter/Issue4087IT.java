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
package com.sun.faces.test.javaee8.converter;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class Issue4087IT {

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
    public void testJavaTimeTypes() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/issue4087.xhtml");
        
        HtmlTextInput input1 = (HtmlTextInput)page.getHtmlElementById("localDateTime1");
        input1.setValueAttribute("Sep 30, 2015 4:14:43 PM");
        
        HtmlTextInput input2 = (HtmlTextInput)page.getHtmlElementById("localDateTime2");
        input2.setValueAttribute("Sep 30, 2015 4:14:43 PM");

        HtmlTextInput input3 = (HtmlTextInput)page.getHtmlElementById("localTime1");
        input3.setValueAttribute("4:14:43 PM");
        
        HtmlTextInput input4 = (HtmlTextInput)page.getHtmlElementById("localTime2");
        input4.setValueAttribute("4:14:43 PM");
        
        HtmlSubmitInput submit = (HtmlSubmitInput)page.getHtmlElementById("submit");
        HtmlPage page1 = submit.click();
        
        HtmlSpan time1Output = (HtmlSpan)page1.getHtmlElementById("localDateTimeValue1");
        assertTrue(time1Output.getTextContent().contains("Sep 30, 2015 4:14 PM"));
        
        HtmlSpan time2Output = (HtmlSpan)page1.getHtmlElementById("localDateTimeValue2");
        assertTrue(time2Output.getTextContent().contains("Sep 30, 2015 4:14 PM"));

        HtmlSpan time3Output = (HtmlSpan)page1.getHtmlElementById("localTimeValue1");
        assertTrue(time3Output.getTextContent().contains("4:14:43 PM"));
        
        HtmlSpan time4Output = (HtmlSpan)page1.getHtmlElementById("localTimeValue2");
        assertTrue(time4Output.getTextContent().contains("4:14 PM"));
    }
    
}
