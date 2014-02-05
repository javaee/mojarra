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

package com.sun.faces.facelets;


import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test cases for Facelets functionality
 */
public class ImplicitFacetTestCase extends HtmlUnitFacesTestCase {


    // --------------------------------------------------------------- Test Init


    public ImplicitFacetTestCase() {
        this("FaceletsTestCase");
    }


    public ImplicitFacetTestCase(String name) {
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
        return (new TestSuite(ImplicitFacetTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------------ Test Methods


    /*
     * Added for issue 917.
     */
    public void testUIRepeat() throws Exception {

        HtmlPage page = getPage("/faces/facelets/implicitFacet01.xhtml") ;
        
        String text = page.asText();
        
        assertTrue(text.matches("(?s).*Implicit\\s*facet\\s*01\\s*id:.*Child\\s*01\\s*of\\s*facet\\s*01\\s*id:\\s*output01.\\s*Child\\s*02\\s*of\\s*facet\\s*01\\s*id:\\s*output02.\\s*Child\\s*03\\s*of\\s*facet\\s*01\\s*id:\\s*output03.*"));
        assertTrue(-1 != text.indexOf("Implicit facet 01 id: panelGroup01. Child 01 of facet 01 id: output07. Child 02 of facet 01 id: output08. Child 03 of facet 01 id: output09."));

        HtmlSubmitInput input = (HtmlSubmitInput) getInputContainingGivenId(page, "command");
        page = input.click();

        text = page.asText();

        assertTrue(text.matches("(?s).*Implicit\\s*facet\\s*01\\s*id:.*Child\\s*01\\s*of\\s*facet\\s*01\\s*id:\\s*output01.\\s*Child\\s*02\\s*of\\s*facet\\s*01\\s*id:\\s*output02.\\s*Child\\s*03\\s*of\\s*facet\\s*01\\s*id:\\s*output03.*"));
        assertTrue(-1 != text.indexOf("Implicit facet 01 id: panelGroup01. Child 01 of facet 01 id: output07. Child 02 of facet 01 id: output08. Child 03 of facet 01 id: output09."));


    }

    /*
     * Added for issue 1726.
     */
    public void testPostBack() throws Exception {

        HtmlPage page = getPage("/faces/facelets/issue1726.xhtml") ;
        
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("button");
        page = button.click();
        String text = page.asText();
        assert(!text.contains("javax.faces.component.UIPanel"));
    }

    public void testConditionalImplicitFacetChild1727() throws Exception {
        HtmlPage page = getPage("/faces/facelets/issue1727-facet-conditional.xhtml");
        HtmlCheckBoxInput checkbox = (HtmlCheckBoxInput) page.getElementById("checkbox");
        checkbox.setChecked(false);
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("button");
        page = button.click();
        String text = page.asText();
        assertTrue(text.matches("(?s).*|Not in if 01|\\s|Not in if 02|.*"));

        checkbox = (HtmlCheckBoxInput) page.getElementById("checkbox");
        checkbox.setChecked(true);
        button = (HtmlSubmitInput) page.getElementById("button");
        page = button.click();
        text = page.asText();
        assertTrue(text.matches("(?s).*|Not in if 01|\\s|Not in if 02|\\s|In if 01|\\s|In if 02|.*"));

        checkbox = (HtmlCheckBoxInput) page.getElementById("checkbox");
        checkbox.setChecked(false);
        button = (HtmlSubmitInput) page.getElementById("button");
        page = button.click();
        text = page.asText();
        assertTrue(text.matches("(?s).*|Not in if 01|\\s|Not in if 02|.*"));

    }

    /*
     * Added for Issue 2066
     * Tests h:column "rowHeader" tag attribute.
     */
    public void testColumnRowHeader() throws Exception {
         HtmlPage page = getPage("/faces/facelets/issue1726.xhtml");
         String xml = page.asXml();
         assertTrue(xml.contains("<th scope=\"row\">"));
    }
}
