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
 * https://glassfish.dev.java.net/public/CDDLGPL_1_1.html
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
package com.sun.faces.test.agnostic.renderKit.passthrough;

import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlResetInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.junit.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Issue1111IT {

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
    public void testInputMarkup() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/input.xhtml");
        assertInputDefaults(page);
    }

    private void assertInputDefaults(HtmlPage page) {
        assertInput(page, "inputText", "type" ,"text", "value", "text1");
        assertInput(page, "inputText2", "type" ,"text", "value", "text2");
        assertInput(page, "textField", "type" ,"text", "value", "text1");
        assertInput(page, "emailField", "type" ,"email", "value", "anybody@example.com");
        assertInput(page, "numberField", "type" ,"number", "value", "10", "pattern", "[0-9]*");
        assertInput(page, "checkBox", "type" ,"checkbox");
        HtmlCheckBoxInput checkBoxInput = (HtmlCheckBoxInput) page.getElementById("checkBox");
        assertFalse(checkBoxInput.isChecked());
    }

    @Test
    public void testInputPostback() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/input.xhtml");

        setValue(page, "inputText2", "new text2");
        setValue(page, "textField", "new text1");
        setValue(page, "emailField", "nobody@example.com");
        setValue(page, "numberField", "12");
        HtmlCheckBoxInput checkBoxInput = (HtmlCheckBoxInput) page.getElementById("checkBox");
        checkBoxInput.setChecked(true);

        HtmlResetInput resetButton = (HtmlResetInput) page.getElementById("resetButton");
        page = resetButton.click();

        assertInputDefaults(page);

        setValue(page, "inputText2", "new text2");
        setValue(page, "textField", "new text1");
        setValue(page, "emailField", "nobody@example.com");
        setValue(page, "numberField", "12");
        checkBoxInput.setChecked(true);

        HtmlSubmitInput submitButton = (HtmlSubmitInput) page.getElementById("submitButton");
        page = submitButton.click();

        assertInput(page, "inputText", "type" ,"text", "value", "new text1");
        assertInput(page, "inputText2", "type" ,"text", "value", "new text2");
        assertInput(page, "textField", "type" ,"text", "value", "new text1");
        assertInput(page, "emailField", "type" ,"email", "value", "nobody@example.com");
        assertInput(page, "numberField", "type", "number", "value", "12", "pattern", "[0-9]*");

        checkBoxInput = (HtmlCheckBoxInput) page.getElementById("checkBox");
        assertTrue(checkBoxInput.isChecked());
    }

    private void setValue(HtmlPage page, String id, String value) {
        HtmlTextInput input = (HtmlTextInput) page.getElementById(id);
        input.setValueAttribute(value);
    }

    private void assertInput(HtmlPage page, String id, String... attrs) {
        assertFormElement(page, "input", id, attrs);
    }

    private void assertSelect(HtmlPage page, String id, String... attrs) {
        assertFormElement(page, "select", id, attrs);
    }

    private void assertFormElement(HtmlPage page, String elementName, String id, String... attrs) {
        HtmlElement input = page.getElementById(id);
        String xml = input.asXml();

        assertTrue(xml.contains("<" + elementName));
        assertTrue(xml.contains("id=\"" + id + "\""));
        assertTrue(xml.contains("name=\"" + id + "\""));

        if(attrs == null) {
            return;
        }
        for(int i = 0; i < attrs.length; i++) {
            String name = attrs[i];
            String value = attrs[++i];
            assertTrue(xml.contains(name + "=\"" + value + "\""));
        }
    }
    
    @Test
    public void testCauseError() throws Exception {
        webClient.setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = webClient.getPage(webUrl + "faces/causeError.xhtml");
        String xml = page.getBody().asXml();
        assertTrue(xml.contains("FaceletException"));
        webClient.setThrowExceptionOnFailingStatusCode(true);
    }        
    
    @Test
    public void testSelectMarkup() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/select.xhtml");
        assertSelectAttributes(page);
        assertDefaultSelections(page);
    }

    private void assertDefaultSelections(HtmlPage page) {
        assertSelection(page, "selectOne", "2");
        assertSelection(page, "selectOneSize2", "3");
        assertSelection(page, "selectMany", "4", "6");
    }

    private void assertSelection(HtmlPage page, String id, String... values) {
        HtmlSelect select = (HtmlSelect) page.getElementById(id);
        List<String> valuesAsList = Arrays.asList(values);
        for (HtmlOption option : select.getOptions()) {
            boolean shouldBeSelected = valuesAsList.contains(option.getValueAttribute());

            if(option.isSelected()) {
                assertTrue(shouldBeSelected);
            } else {
                assertFalse(shouldBeSelected);
            }
        }
    }

    private void assertSelectAttributes(HtmlPage page) {
        assertSelect(page, "selectOne", "size" ,"1");
        assertSelect(page, "selectOneSize2", "size" ,"2");
        assertSelect(page, "selectMany", "size", "7", "multiple", "multiple");
    }

    @Test
    public void testSelectPostback() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/select.xhtml");

        select(page, "selectOne", "3");
        select(page, "selectOneSize2", "5");
        select(page, "selectMany", "1", "2");

        HtmlResetInput resetButton = (HtmlResetInput) page.getElementById("resetButton");
        page = resetButton.click();

        assertSelectAttributes(page);
        assertDefaultSelections(page);

        select(page, "selectOne", "3");
        select(page, "selectOneSize2", "5");
        select(page, "selectMany", "1", "2");

        HtmlSubmitInput submitButton = (HtmlSubmitInput) page.getElementById("submitButton");
        page = submitButton.click();

        assertSelectAttributes(page);

        assertSelection(page, "selectOne", "3");
        assertSelection(page, "selectOneSize2", "5");
        assertSelection(page, "selectMany", "1", "2");
    }

    private void select(HtmlPage page, String id, String... values) {
        HtmlSelect select = (HtmlSelect) page.getElementById(id);
        List<String> valuesAsList = Arrays.asList(values);

        for (HtmlOption option : select.getOptions()) {
            option.setSelected(valuesAsList.contains(option.getValueAttribute()));
        }
    }

    @Test
    public void testTextareaMarkup() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/textarea.xhtml");
        assertFormElement(page, "textarea", "textarea", "autofocus", "autofocus");
        HtmlTextArea textarea = (HtmlTextArea) page.getElementById("textarea");
        assertEquals(textarea.getText(), "Long text");
    }

    @Test
    public void testTextareaPostback() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/textarea.xhtml");
        HtmlTextArea textarea = (HtmlTextArea) page.getElementById("textarea");
        textarea.setText("Very long text");

        HtmlSubmitInput submitButton = (HtmlSubmitInput) page.getElementById("submitButton");
        page = submitButton.click();

        assertFormElement(page, "textarea", "textarea", "autofocus", "autofocus");

        textarea = (HtmlTextArea) page.getElementById("textarea");
        assertEquals("Very long text", textarea.getText());
    }

    @Test
    public void testButton() throws Exception {
        HtmlPage page = webClient.getPage(webUrl + "faces/button.xhtml");
        assertFormElement(page, "button", "fancyButton1");
        assertFormElement(page, "button", "fancyButton2");
        String lastAction = page.getElementById("lastAction").getTextContent();
        assertEquals("", lastAction);

        page = page.getElementById("fancyButton1").click();

        lastAction = page.getElementById("lastAction").getTextContent();
        assertEquals("action1", lastAction);

        page = page.getElementById("fancyButton2").click();

        lastAction = page.getElementById("lastAction").getTextContent();
        assertEquals("action2", lastAction);
    }

}