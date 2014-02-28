/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package com.sun.faces.jsptest;

import com.gargoylesoftware.htmlunit.html.*;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.*;

/**
 * <p>
 * Test id-ref values in <code>h:message</code> and <code>h:outputLabel</code>
 * tags, and their interaction with <code>c:forEach</code>.
 * </p>
 */

public class IdRefTestCase extends HtmlUnitFacesTestCase {

    // ------------------------------------------------------------
    // Constructors

    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public IdRefTestCase(String name) {
        super(name);
        addExclusion(Container.TOMCAT6, "testIdRefs");
        addExclusion(Container.TOMCAT7, "testIdRefs");
        addExclusion(Container.WLS_10_3_4_NO_CLUSTER, "testIdRefs");
        addExclusion(Container.TOMCAT6, "testIncludedLoopIdRefs");
        addExclusion(Container.TOMCAT7, "testIncludedLoopIdRefs");
        addExclusion(Container.WLS_10_3_4_NO_CLUSTER, "testIncludedLoopIdRefs");
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
        String[] testIds = new String[]{idPrefix + "Int1", idPrefix + "Id1",
                idPrefix + "Id2j_id_1", idPrefix + "Id3j_id_2"};
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
