/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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
package com.sun.faces.test.servlet30.nesteddatatables;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class NestedDatatablesIT {

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
    public void testInputFieldUpdate() throws Exception {
	HtmlPage page = webClient.getPage(webUrl + "faces/test.jsp");

	for (int i = 0; i < 3; i++) {
            HtmlTextInput input = (HtmlTextInput) page.getHtmlElementById("form:outerData:0:innerData:" + i + ":inputText");
            input.setValueAttribute("" + i);
	}
	for (int i = 3; i < 6; i++) {
            HtmlTextInput input = (HtmlTextInput) page.getHtmlElementById("form:outerData:1:innerData:" + (i-3) + ":inputText");
            input.setValueAttribute("" + i);
	}

        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("form:submit");
        page = button.click();
        
	for (int i = 0; i < 3; i++) {
            HtmlTextInput input = (HtmlTextInput) page.getHtmlElementById("form:outerData:0:innerData:" + i + ":inputText");
            assertEquals("" + i, input.getValueAttribute());
	}
	for (int i = 3; i < 6; i++) {
            HtmlTextInput input = (HtmlTextInput) page.getHtmlElementById("form:outerData:1:innerData:" + (i-3) + ":inputText");
            assertEquals("" + i, input.getValueAttribute());
	}

        for (int i = 0; i < 3; i++) {
            HtmlTextInput input = (HtmlTextInput) page.getHtmlElementById("form:outerData:0:innerData:" + i + ":inputText");
            input.setValueAttribute(Character.toString((char) ('a' + i)));
	}
	for (int i = 3; i < 6; i++) {
            HtmlTextInput input = (HtmlTextInput) page.getHtmlElementById("form:outerData:1:innerData:" + (i-3) + ":inputText");
            input.setValueAttribute(Character.toString((char) ('a' + i)));
	}

        button = (HtmlSubmitInput) page.getHtmlElementById("form:submit");
        page = button.click();
        
	for (int i = 0; i < 3; i++) {
            HtmlTextInput input = (HtmlTextInput) page.getHtmlElementById("form:outerData:0:innerData:" + i + ":inputText");
            assertEquals(Character.toString((char) ('a' + i)), input.getValueAttribute());
	}
	for (int i = 3; i < 6; i++) {
            HtmlTextInput input = (HtmlTextInput) page.getHtmlElementById("form:outerData:1:innerData:" + (i-3) + ":inputText");
            assertEquals(Character.toString((char) ('a' + i)), input.getValueAttribute());
	}
    }

    public void testInputFieldUpdate2() throws Exception {
	HtmlPage page = webClient.getPage(webUrl + "faces/nested.jsp");

        HtmlTextInput input1 = page.getHtmlElementById("form:outer:0:inputText");
        input1.setValueAttribute("" + 0);
        HtmlTextInput input2 = page.getHtmlElementById("form:outer:1:inputText");
        input2.setValueAttribute("" + 1);

        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("form:submit");
        page = button.click();

        input1 = page.getHtmlElementById("form:outer:0:inputText");
        assertEquals("" + 0, input1.getValueAttribute());
        input2 = page.getHtmlElementById("form:outer:1:inputText");
        assertEquals("" + 1, input2.getValueAttribute());

        input1 = page.getHtmlElementById("form:outer:0:inputText");
        input1.setValueAttribute(Character.toString((char) ('a' + 0)));
        input2 = page.getHtmlElementById("form:outer:1:inputText");
        input2.setValueAttribute(Character.toString((char) ('a' + 1)));
        
        button = (HtmlSubmitInput) page.getHtmlElementById("form:submit");
        page = button.click();

        input1 = page.getHtmlElementById("form:outer:0:inputText");
        assertEquals(Character.toString((char) ('a' + 0)), input1.getValueAttribute());
        input2 = page.getHtmlElementById("form:outer:1:inputText");
        assertEquals(Character.toString((char) ('a' + 1)), input2.getValueAttribute());

        button = (HtmlSubmitInput) page.getHtmlElementById("form:outer:0:inner:add-port");
        page = button.click();

        HtmlTextInput portNumberInput = (HtmlTextInput) page.getHtmlElementById("form:outer:0:inner:0:portNumber");
        portNumberInput.setValueAttribute("12");

        button = (HtmlSubmitInput) page.getHtmlElementById("form:submit");
        page = button.click();

        portNumberInput = (HtmlTextInput) page.getHtmlElementById("form:outer:0:inner:0:portNumber");
	assertEquals("12", portNumberInput.getValueAttribute());

        button = (HtmlSubmitInput) page.getHtmlElementById("form:outer:1:inner:add-port");
        page = button.click();

        portNumberInput = (HtmlTextInput) page.getHtmlElementById("form:outer:1:inner:0:portNumber");
	assertTrue(-1 == portNumberInput.getValueAttribute().indexOf("12"));
    }
}
