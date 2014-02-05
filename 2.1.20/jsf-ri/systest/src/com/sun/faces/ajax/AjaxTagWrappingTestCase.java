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

package com.sun.faces.ajax;

import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.*;

public class AjaxTagWrappingTestCase extends HtmlUnitFacesTestCase {

    public AjaxTagWrappingTestCase(String name) {
        super(name);
    }

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
        return (new TestSuite(AjaxTagWrappingTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    /*
       Test each component to see that it behaves correctly when used with an Ajax tag
     */
    public void testAjaxTagWrapping() throws Exception {
        getPage("/faces/ajax/ajaxTagWrapping.xhtml");
        System.out.println("Start ajax tag wrapping test");

        // First we'll check the first page was output correctly
        checkTrue("out1", "0");
        checkTrue("checkedvalue", "false");
        checkTrue("outtext", "");

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("button1");
        lastpage = (HtmlPage) button1.click();

        // Check that the ajax request succeeds
        checkTrue("out1","1");

        HtmlAnchor link1 = (HtmlAnchor) lastpage.getHtmlElementById("link1");
        lastpage = (HtmlPage) link1.click();

        // Check that the ajax request succeeds
        checkTrue("out1","2");

        // Submit the ajax request
        HtmlSubmitInput button2 = (HtmlSubmitInput) lastpage.getHtmlElementById("button2");
        lastpage = (HtmlPage) button2.click();

        // Check that the ajax request succeeds
        checkTrue("out1","3");

        // Check on the text field
        HtmlTextInput intext = ((HtmlTextInput)lastpage.getHtmlElementById("intext"));
        intext.focus();
        intext.type("test");
        intext.blur();

        checkTrue("outtext","test");

        // Check on the text field
        HtmlTextInput intext2 = ((HtmlTextInput)lastpage.getHtmlElementById("intext2"));
        intext2.focus();
        intext2.type("test2");
        intext2.blur();

        checkTrue("outtext","test2");

        // Check on the checkbox
        HtmlCheckBoxInput checked = ((HtmlCheckBoxInput)lastpage.getHtmlElementById("checkbox"));
        lastpage = (HtmlPage)checked.click();

        checkTrue("checkedvalue","true");

        // Check on the select many checkbox
        checked = ((HtmlCheckBoxInput)lastpage.getHtmlElementById("manyCheckbox:0"));
        lastpage = (HtmlPage)checked.click();

        checkTrue("manyCheckedValue","Value: 1");

    }

    public void testReturnFalseOnlyGeneratedOnAjaxInsideActionSourceComponents() throws Exception {
        HtmlPage page = getPage("/faces/ajax/issue1760NestedAjaxCheckboxRender.xhtml");
        
        sampleClickSample(page, "form1CurrentTime:", "checkbox1");
        sampleClickSample(page, "form2CurrentTime:", "checkbox2");
        sampleClickSample(page, "form3CurrentTime:", "button1");
        sampleClickSample(page, "form4CurrentTime:", "link1");

    }

    private void sampleClickSample(HtmlPage page, String timestampPrefix, String clickableElementId) throws Exception {
        String xml = page.asXml();
        verifyContent(xml);

        String timestampBefore = sampleTimestamp(timestampPrefix, xml);
        ClickableElement clickable = (ClickableElement) page.getElementById(clickableElementId);
        page = clickable.click();

        xml = page.asXml();
        verifyContent(xml);
        String timestampAfter = sampleTimestamp(timestampPrefix, xml);

        assertTrue(!timestampBefore.equals(timestampAfter));

    }

    private String sampleTimestamp(String timestampPrefix, String xml) {
        String result = null;
        int prefixLen = timestampPrefix.length();
        int i = xml.indexOf(timestampPrefix);
        int j = xml.indexOf(".", i);
        result = xml.substring(i+prefixLen, j);

        return result;
    }

    private void verifyContent(String xml) throws Exception {
        // Verify that return false is *not* present on ajax request for form1
        // and form2.
        assertTrue(xml.matches("(?s).*<form id=\"form1\".*<input id=\"checkbox1\".*onclick=\".*[^f][^a][^l][^s][^e].*</form.*"));
        assertTrue(xml.matches("(?s).*<form id=\"form2\".*<input id=\"checkbox2\".*onclick=\".*[^f][^a][^l][^s][^e].*</form.*"));

        // Verify that return false *is* present on the ajax request for form3
        // and form4
        assertTrue(xml.matches("(?s).*<form id=\"form3\".*<input.*type=\"submit\".*onclick=\".*return false\".*</form.*"));
        assertTrue(xml.matches("(?s).*<form id=\"form3\".*<a.*onclick=\".*return false\".*</form.*"));

    }

}
