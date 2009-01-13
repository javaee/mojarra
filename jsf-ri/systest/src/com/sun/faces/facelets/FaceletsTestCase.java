/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.facelets;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test cases for Facelets functionality
 */
public class FaceletsTestCase extends AbstractTestCase {


    // --------------------------------------------------------------- Test Init


    public FaceletsTestCase() {
        this("FaceletsTestCase");
    }


    public FaceletsTestCase(String name) {
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
        return (new TestSuite(FaceletsTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------------ Test Methods


    /**
     * Added for issue 917.
     */
    public void testSetPropertyActionListener1() throws Exception {

        HtmlPage page = getPage("/faces/facelets/setpropertyactionlistener1.xhtml") ;

        // verify the output is initially null
        List<HtmlSpan> output = new ArrayList<HtmlSpan>(1);
        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
        assertTrue(!output.isEmpty());
        HtmlSpan span = output.get(0);
        assertTrue("Current Name: ".equals(span.asText()));

        // click the commandLink with a nested setPropertyActionListener
        // to cause the name attribute in the session to be populated.
        HtmlElement clink = page.getHtmlElementById("form:s1");
        assertNotNull(clink);
        page = (HtmlPage) ((HtmlSubmitInput) clink).click();
        output.clear();
        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
        assertTrue(!output.isEmpty());
        span = output.get(0);
        assertTrue("Current Name: Mojarra".equals(span.asText()));

        // click the commandButton with a nested setPropertyActionListener
        // to cause the name attribute in the session to be cleared
        HtmlElement cbutton = page.getHtmlElementById("form:s2");
        assertNotNull(clink);
        page = (HtmlPage) ((HtmlAnchor) cbutton).click();
        output.clear();
        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
        assertTrue(!output.isEmpty());
        span = output.get(0);
        assertTrue("Current Name: ".equals(span.asText()));
        
    }


    /**
     * Added for issue 909.
     */
    public void testTagSourceFromDtdDocument() throws Exception {

        HtmlPage page = getPage("/faces/facelets/sourcefromdtdconfig.xhtml") ;

        // verify the output is initially null
        List<HtmlSpan> output = new ArrayList<HtmlSpan>(1);
        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
        assertTrue(!output.isEmpty());
        HtmlSpan span = output.get(0);
        assertTrue("Hello!".equals(span.asText()));
        assertTrue("color:red".equals(span.getStyleAttribute()));

    }


    /**
     * Verify #{component} and #{compositeComponent} expressions evaluate
     * at build time.
     * @throws Exception
     */
    public void testComponentELAtBuildTime() throws Exception {

        HtmlPage page = getPage("/faces/facelets/componentELAtBuildTime.xhtml") ;

        // verify the output is initially null
        List<HtmlSpan> output = new ArrayList<HtmlSpan>(4);
        getAllElementsOfGivenClass(page, output, HtmlSpan.class);
        assertTrue(output.size() == 3);
        for (HtmlSpan span : output) {
            assertTrue("PASSED".equals(span.asText()));
        }

    }
}
