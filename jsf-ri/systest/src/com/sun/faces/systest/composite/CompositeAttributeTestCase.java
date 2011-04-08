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

package com.sun.faces.systest.composite;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import com.gargoylesoftware.htmlunit.html.*;

import java.util.List;

/**
 * Unit tests for Composite Component Attributes
 */
public class CompositeAttributeTestCase extends HtmlUnitFacesTestCase {


    @SuppressWarnings({"UnusedDeclaration"})
    public CompositeAttributeTestCase() {
        this("CompositeAttributeTestCase");
    }

    public CompositeAttributeTestCase(String name) {
        super(name);
    }


    /**
     * Set up instance variables required by this test case.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }


    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(CompositeAttributeTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    @Override
    public void tearDown() {
        super.tearDown();
    }
    

    // -------------------------------------------------------------- Test Cases

    // Tests cc:attribute default=....

    public void testValueExpressionDefaults() throws Exception {

        HtmlPage page = getPage("/faces/composite/defaultAttributeValueExpression.xhtml");
        assertTrue(page.asText().contains("DEFAULT VALUE"));

    }

    /**
     * Test for Issue #1966
     * @throws Exception
     */
    public void testCompositeAttributeDefaults() throws Exception {
        HtmlPage page = getPage("/faces/composite/defaultAttributeValueExpression_1966.xhtml");

        // Test with empty list as items
        HtmlElement dataTable = page.getElementById("WithValueEmptyList:DataTable");
        List<DomText> content = (List<DomText>) dataTable.getByXPath("./tbody/tr/td/text()");
        assertTrue("Table should conain max. 1 empty cell.", content.size() <= 1);
        for (DomText text : content) {
            assertTrue("Cells should be empty", text.asText().length() == 0);
        }

        // Test with null value as items
        dataTable = page.getElementById("WithValueNull:DataTable");
        content = (List<DomText>) dataTable.getByXPath("./tbody/tr/td/text()");
        assertEquals("Table should contain 2 cells", 2, content.size());
        for (int i = 0; i < 2; i++) {
            assertEquals("---Item " + (i + 1) + "---", content.get(i).toString());
        }

        // @Todo change to a test without awt dependencies
        // Test Colors
//        assertElementContentEquals(page,
//                "ColorWithValueFromBean:Output",
//                "---java.awt.Color[r=255,g=175,b=175]---");
//        assertElementContentEquals(page,
//                "ColorWithValueLiteral:Output",
//                "---java.awt.Color[r=80,g=40,b=20]---");
//        assertElementContentEquals(page,
//                "ColorWithValueNone:Output",
//                "---java.awt.Color[r=200,g=100,b=50]---");
//        assertElementContentEquals(page,
//                "ColorWithValueEmpty:Output",
//                "---java.awt.Color[r=200,g=100,b=50]---");
//        assertElementContentEquals(page,
//                "ColorWithValueNull:Output",
//                "---java.awt.Color[r=200,g=100,b=50]---");

    }

    /**
     * Test for Issue #1986
     */
    public void testCompositeAttributeCanBeNull() throws Exception {
        HtmlPage page = getPage("/faces/composite/defaultAttributeValueExpression_1986.xhtml");
        assertElementAttributeEquals(page, "WithValueNull:Input", "value", "");
        assertElementAttributeEquals(page, "WithValueEmpty:Input", "value", "");
    }

    /**
     * Helper to test for the Content of an HTML-Element.
     * This method will assert, that the Element with the provided id exists
     * and that its content is equal to the provided expected content.
     * @param page the page to test
     * @param elementId the id of the element that contains the content to be
     *  tested.
     * @param expected the expected content
     */
    private void assertElementContentEquals(final HtmlPage page,
            final String elementId, final String expected) {
        HtmlElement element = page.getElementById(elementId);
        assertNotNull(element);
        assertEquals("Testing element content of #" + elementId,
                expected, element.getTextContent());
    }


    /**
     * Helper to test for the value of an attribute of a HTML-Element.
     * This method will assert, that the Element with the provided id exists
     * and that the value of the attribute with the provided attributeName is
     * equal to the provided expected content.
     * @param page the page to test
     * @param elementId the id of the element with the attribute to be tested
     * @param attributeName the name of the attribute to be tested
     * @param expected the expected content
     */
    private void assertElementAttributeEquals(final HtmlPage page,
            final String elementId,final String attributeName,
            final String expected) {
        HtmlElement element = page.getElementById(elementId);
        assertNotNull(element);
        assertEquals("Testing attribute '" +  attributeName + "' of #"
                + elementId,
                expected, element.getAttribute(attributeName));
    }
}
