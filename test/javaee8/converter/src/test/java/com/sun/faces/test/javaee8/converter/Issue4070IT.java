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
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Issue4070IT {

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
        
        doTestJavaTimeTypes("Sep 30, 2015 4:14:43 PM", "localDateTime", "2015-09-30T16:14:43");
                
        doTestJavaTimeTypes("Sep 30, 2015", "localDate", "2015-09-30");
        
        doTestJavaTimeTypes("4:52:56 PM", "localTime", "16:52:56");
        
        doTestJavaTimeTypes("17:07:19.358-04:00", "offsetTime", "17:07:19.358-04:00");     

        doTestJavaTimeTypes("2015-09-30T17:24:36.529-04:00", "offsetDateTime", "2015-09-30T17:24:36.529-04:00");
        
        doTestJavaTimeTypes("2015-09-30T17:31:42.09-04:00[America/New_York]", "zonedDateTime", "2015-09-30T17:31:42.090-04:00[America/New_York]");        
        
    }
    
    private void doTestJavaTimeTypes(String value, String inputId, String expected) throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/Issue4070Using.xhtml");
        
        HtmlTextInput input;
        HtmlSubmitInput submit;
        HtmlSpan output;

        input = page.getHtmlElementById(inputId);
        input.setValueAttribute(value);
        submit = page.getHtmlElementById("submit");
        page = submit.click();
        output = page.getHtmlElementById(inputId + "Value");
        assertEquals(expected, output.getTextContent());
    }
    
    @Test
    public void testInputOutputDiffer() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/Issue4070InputOutputDiffer.xhtml");
        
        HtmlTextInput input;
        HtmlSubmitInput submit;
        HtmlSpan output;

        input = page.getHtmlElementById("localDate");
        input.setValueAttribute("30.09.2015");
        submit = page.getHtmlElementById("submit");
        page = submit.click();
        output = page.getHtmlElementById("localDateValue");
        assertEquals("30.09.15", output.getTextContent());
        
    }
    
}
