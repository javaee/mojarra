/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2015 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.faces.composite;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CompositeComponentsIT {

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
    @Ignore
    public void testNesting05() throws Exception {

        HtmlPage page = webClient.getPage(webUrl + "faces/composite/nesting05.xhtml");
        HtmlSubmitInput submit = (HtmlSubmitInput) page.getHtmlElementById("nesting6:nesting7:form1:command");
        page = submit.click();
        assertTrue(page.asText().contains("Action invoked"));

        page = webClient.getPage(webUrl + "faces/composite/nesting05.xhtml");
        submit = (HtmlSubmitInput) page.getHtmlElementById("nesting6:nesting7:form2:command2");
        page = submit.click();
        assertTrue(page.asText().contains("ActionListener invoked"));

        page = webClient.getPage(webUrl + "faces/composite/nesting05.xhtml");
        submit = (HtmlSubmitInput) page.getHtmlElementById("nesting6:nesting7:form3:command3");
        page = submit.click();
        assertTrue(page.asText().contains("Custom action invoked"));

        page = webClient.getPage(webUrl + "faces/composite/nesting05.xhtml");
        submit = (HtmlSubmitInput) page.getHtmlElementById("nesting6:nesting7:form4:command");
        HtmlTextInput text = (HtmlTextInput) page.getHtmlElementById("nesting6:nesting7:form4:input");
        text.setValueAttribute("foo");
        page = submit.click();
        assertTrue(page.asText().contains("validator invoked"));

        page = webClient.getPage(webUrl + "faces/composite/nesting05.xhtml");
        submit = (HtmlSubmitInput) page.getHtmlElementById("nesting6:nesting7:form5:command");
        page = submit.click();
        assertTrue(page.asText().contains("ValueChange invoked"));
    }

    /**
     * Added for issue 1255.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    @Ignore
    public void testNesting08() throws Exception {

        HtmlPage page = webClient.getPage(webUrl + "faces/composite/nesting06.xhtml");
        HtmlSubmitInput submit = (HtmlSubmitInput) page.getHtmlElementById("nesting10:nesting6:nesting7:form1:command");
        page = submit.click();
        assertTrue(page.asText().contains("Action invoked"));

        page = webClient.getPage(webUrl + "faces/composite/nesting06.xhtml");
        submit = (HtmlSubmitInput) page.getHtmlElementById("nesting10:nesting6:nesting7:form2:command2");
        page = submit.click();
        assertTrue(page.asText().contains("ActionListener invoked"));

        page = webClient.getPage(webUrl + "faces/composite/nesting06.xhtml");
        submit = (HtmlSubmitInput) page.getHtmlElementById("nesting10:nesting6:nesting7:form3:command3");
        page = submit.click();
        assertTrue(page.asText().contains("Custom action invoked"));

        page = webClient.getPage(webUrl + "faces/composite/nesting06.xhtml");
        submit = (HtmlSubmitInput) page.getHtmlElementById("nesting10:nesting6:nesting7:form4:command");
        HtmlTextInput text = (HtmlTextInput) page.getHtmlElementById("nesting10:nesting6:nesting7:form4:input");
        text.setValueAttribute("foo");
        page = submit.click();
        assertTrue(page.asText().contains("validator invoked"));

        page = webClient.getPage(webUrl + "faces/composite/nesting06.xhtml");
        submit = (HtmlSubmitInput) page.getHtmlElementById("nesting10:nesting6:nesting7:form5:command");
        page = submit.click();
        assertTrue(page.asText().contains("ValueChange invoked"));
    }

    /**
     * <p>
     * Maps Validator to inputText within composite/validator1.xhtml using only
     * the name attribute.
     * </p>
     */
    @Test
    @Ignore
    public void testValidator1() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/composite/attachedvalidator.xhtml");
        validateValidatorMessagePresent(page,
                "form:s1",
                "form:validator1:input");
    }

    private void validateValidatorMessagePresent(HtmlPage page, String commandId, String inputId)
            throws Exception {
        page = pushButton(page, commandId);
        validateMessage(page, "Validator Invoked", inputId);
    }

    private void validateMessage(HtmlPage page,
            String messagePrefix,
            String messageSuffix) {

        List<HtmlUnorderedList> list = page.getBody().getHtmlElementsByTagName("ul");
        HtmlUnorderedList ulist = list.get(0);
        assertEquals("messages", ulist.getId());
        int count = 0;
        String message = (messagePrefix + " : " + messageSuffix);
        for (HtmlElement e : ulist.getAllHtmlChildElements()) {
            if (count > 1) {
                fail("Expected only one message to be displayed");
            }
            count++;
            assertTrue(e instanceof HtmlListItem);
            assertEquals(message, message, e.asText());
        }

        if (list.size() == 2) {
            ulist = list.get(1);
            for (HtmlElement e : ulist.getAllHtmlChildElements()) {
                fail("Messages have been redisplayed");
            }
        }
    }

    private HtmlPage pushButton(HtmlPage page, String commandId) throws Exception {
        HtmlSubmitInput input = (HtmlSubmitInput) page.getHtmlElementById(commandId);
        assertNotNull(input);
        return (HtmlPage) input.click();
    }
}
