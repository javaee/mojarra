/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * [Name of File] [ver.__] [Date]
 *
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.jsptest;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.AbstractTestCase;

/**
 * <p>
 * Test id-ref values in <code>h:message</code> and <code>h:outputLabel</code>
 * tags, and their interaction with <code>c:forEach</code>.
 * </p>
 */

public class IdRefTestCase extends AbstractTestCase {

    // ------------------------------------------------------------
    // Constructors

    /**
         * Construct a new instance of this test case.
         *
         * @param name
         *                Name of the test case
         */
    public IdRefTestCase(String name) {
        super(name);
    }

    // ----------------------------------------------------
    // Overall Test Methods

    /**
         * Return the tests included in this test suite.
         */
    public static Test suite() {
        return (new TestSuite(IdRefTestCase.class));
    }

    private Map mapElementsByAttribute(HtmlElement docElem, String tagName,
            String attName, String filterAtt, String filterValue) {
        Map elems = new TreeMap();
        List tags = docElem.getHtmlElementsByTagName(tagName);
        for (Iterator tagIt = tags.iterator(); tagIt.hasNext();) {
            HtmlElement tag = (HtmlElement) tagIt.next();
            if (filterAtt != null && filterValue != null &&
                !filterValue.equals(tag.getAttributeValue(filterAtt))) {
                continue;
            }
            String attValue = tag.getAttributeValue(attName);
            assertNotNull(attName + " attribute of " + tagName, attValue);
            assertNotSame(attName + " attribute of " + tagName, 0, attValue.length());
            assertFalse("More than one " + tagName + " contains " + attName
                    + "=" + attValue, elems.containsKey(attValue));
            elems.put(attValue, tag);
        }
        return elems;
    }

    private Map mapMessagesById(HtmlElement docElem) {
        Map elems = new TreeMap();
        List tags = docElem.getHtmlElementsByTagName("span");
        for (Iterator tagIt = tags.iterator(); tagIt.hasNext();) {
            HtmlSpan tag = (HtmlSpan) tagIt.next();
            if ("message".equals(tag.getClassAttribute())) {
                String text = tag.asText();
                assertNotSame(
                        "expect validation message to start with component id",
                        -1, text.indexOf(": "));
                String id = text.substring(0, text.indexOf(": ")).trim();
                assertFalse("Duplicate message for input " + id, elems
                        .containsKey(id));
                elems.put(id, tag);
            }
        }
        return elems;
    }

    // -------------------------------------------------
    // Individual Test Methods
    public void testIdRefs() throws Exception {
        HtmlPage page = getPage("/faces/forEach03.jsp");

        // assert every input has a label, and every label refers to an input
        Map inputTagsById = mapElementsByAttribute(page.getDocumentElement(),
                "input", "id", "type", "text");
        Map labelTagsByFor = mapElementsByAttribute(page.getDocumentElement(),
                "label", "for", null, null);
        assertEquals("//label/@for set should be the same as //input/@id set",
                inputTagsById.keySet(), labelTagsByFor.keySet());

        // assign new values to input fields, submit the form.
        String idPrefix = "myform:input";
        String[] testIds = new String[] { idPrefix + "Int1", idPrefix + "Id1",
                idPrefix + "Id2j_id_1", idPrefix + "Id3j_id_2" };
        for (int i = 0; i < testIds.length; ++i) {
            HtmlTextInput input = (HtmlTextInput) inputTagsById.get(testIds[i]);
            input.setValueAttribute("");
        }
        List list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        // make sure every 'value required' validation is present on post back.
        Map messageMap = mapMessagesById(page.getDocumentElement());
        assertEquals("One 'value required' message for each cleared input",
                testIds.length, messageMap.size());
        assertTrue("Only cleared inputs have messages", Arrays.asList(testIds)
                .containsAll(messageMap.keySet()));
        assertTrue("All cleared inputs have messages", messageMap.keySet()
                .containsAll(Arrays.asList(testIds)));
    }
    
    public void testIncludedLoopIdRefs() throws Exception {
        HtmlPage page = getPage("/faces/forEach03.jsp");
        Map inputTagsById = mapElementsByAttribute(page.getDocumentElement(),
                "input", "id", "type", "text");
        String[] testIds = {
              "myform:inputId11",
              "myform:inputId11j_id_1",
              "myform:inputId11j_id_2"
        };
        for (int i = 0; i < testIds.length; i++) {
            HtmlTextInput input = (HtmlTextInput) inputTagsById.get(testIds[i]);
            input.setValueAttribute("");
        }
        List list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();
        Map messageMap = mapMessagesById(page.getDocumentElement());
        assertEquals("One 'value required' message for each cleared input",
                testIds.length, messageMap.size());
        assertTrue("Only cleared inputs have messages", Arrays.asList(testIds)
                .containsAll(messageMap.keySet()));
        assertTrue("All cleared inputs have messages", messageMap.keySet()
                .containsAll(Arrays.asList(testIds)));
    }
    
    public void testIncludeNoLoopIdRef() throws Exception {
         HtmlPage page = getPage("/faces/forEach03.jsp");
        Map inputTagsById = mapElementsByAttribute(page.getDocumentElement(),
                "input", "id", "type", "text");
        String[] testIds = {
              "myform:Short11",              
        };
        for (int i = 0; i < testIds.length; i++) {
            HtmlTextInput input = (HtmlTextInput) inputTagsById.get(testIds[i]);
            input.setValueAttribute("");
        }
         List list = getAllElementsOfGivenClass(page, null,
                HtmlSubmitInput.class);
        HtmlSubmitInput button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();
        Map messageMap = mapMessagesById(page.getDocumentElement());
        assertEquals("One 'value required' message for each cleared input",
                testIds.length, messageMap.size());
        assertTrue("Only cleared inputs have messages", Arrays.asList(testIds)
                .containsAll(messageMap.keySet()));
        assertTrue("All cleared inputs have messages", messageMap.keySet()
                .containsAll(Arrays.asList(testIds)));
    }

}