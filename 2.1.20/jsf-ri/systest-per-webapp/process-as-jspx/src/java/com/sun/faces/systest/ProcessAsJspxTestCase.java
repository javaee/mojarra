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

package com.sun.faces.systest;


import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Pattern;
import junit.framework.Test;
import junit.framework.TestSuite;


public class ProcessAsJspxTestCase extends HtmlUnitFacesTestCase {

    private final static Pattern XmlDeclaration = Pattern.compile("(?s)^<\\?xml(\\s)*version=.*\\?>.*");
    private final static Pattern XmlDoctype = Pattern.compile("(?s).*<!DOCTYPE.*>.*");
    private final static Pattern XmlPI = Pattern.compile("(?s).*<\\?xml-stylesheet.*\\?>.*");
    private final static Pattern CDATASection = Pattern.compile("(?s).*<!\\[CDATA\\[ .*\\]\\]>.*");
    private final static Pattern Comment = Pattern.compile("(?s).*<!--.*-->.*");
    private final static Pattern EscapedText = Pattern.compile("(?s).*&amp;lt;context-param&amp;gt;.*");
    private final static Pattern NotEscapedText = Pattern.compile("(?s).*&lt;context-param&gt;.*");


    public ProcessAsJspxTestCase(String name) {
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
        return (new TestSuite(ProcessAsJspxTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }
    
    private String getRawMarkup(String path) throws Exception {
        URL url = getURL(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder builder = new StringBuilder();
        String cur;
        while (null != (cur = reader.readLine())) {
            builder.append(cur);
        }

        String xml = builder.toString();
        return xml;
    }
    
    // ------------------------------------------------------------ Test Methods
    
    public void testProcessAsXhtml() throws Exception {

        String xml = getRawMarkup("/faces/xhtmlview.xhtml");
        assertTrue(XmlDoctype.matcher(xml).matches());
        assertTrue(XmlDeclaration.matcher(xml).matches());
        assertTrue(XmlPI.matcher(xml).matches());
        assertTrue(CDATASection.matcher(xml).matches());
        assertTrue(EscapedText.matcher(xml).matches());
        assertTrue(Comment.matcher(xml).matches());
    }

    public void testProcessAsXml() throws Exception {

        String xml = getRawMarkup("/faces/xmlview.view.xml");
        assertFalse(XmlDoctype.matcher(xml).matches());
        assertFalse(XmlDeclaration.matcher(xml).matches());
        assertFalse(XmlPI.matcher(xml).matches());
        assertFalse(CDATASection.matcher(xml).matches());
        assertTrue(EscapedText.matcher(xml).matches());
        assertFalse(Comment.matcher(xml).matches());
    }

    public void testProcessAsXmlWithDifferentXmlnsCases() throws Exception {

        String xml = getRawMarkup("/faces/xmlviewWithHtmlRoot.view.xml");
        assertTrue(xml.matches("(?s).*<html.*xmlns=\"http://www.w3.org/1999/xhtml\">.*"));
        xml = getRawMarkup("/faces/xmlviewWithHtmlRootAndXmlnsOnHeadAndBody.view.xml");
        assertTrue(xml.matches("(?s).*<html>.*<head.*xmlns=\"http://www.w3.org/1999/xhtml\">.*<body.*xmlns=\"http://www.w3.org/1999/xhtml\">.*"));
    }

    public void testProcessAsXmlWithDoctype() throws Exception {

        String xml = getRawMarkup("/faces/xmlviewWithDoctype.view.xml");
        assertTrue(xml.matches("(?s).*<!DOCTYPE.*html.*PUBLIC.*\"-//W3C//DTD.*XHTML.*1.0.*Transitional//EN\".*\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html.*xmlns=\"http://www.w3.org/1999/xhtml\".*"));
    }

    public void testProcessAsXmlMathML() throws Exception {

        String xml = getRawMarkup("/faces/mathmlview.view.xml");
        assertTrue(xml.matches("(?s).*<html.*xmlns=\"http://www.w3.org/1999/xhtml\">.*<head>.*<title>.*Raw.*XML.*View.*with.*MathML</title>.*</head>.*<body>.*<p>.*<math.*xmlns=\"http://www.w3.org/1998/Math/MathML\">.*<msup>.*<msqrt>.*<mrow>.*<mi>.*a</mi>.*<mo>.*\\+</mo>.*<mi>.*b</mi>.*</mrow>.*</msqrt>.*<mn>.*27</mn>.*</msup>.*</math>.*</p>.*</body>.*</html>.*"));

        Page page = client.getPage(getURL("/faces/mathmlview.view.xml"));
        WebResponse response = page.getWebResponse();
        assertEquals("Content-type should be text/xml", "text/xml", response.getContentType());

    }

    public void testProcessAsJspx() throws Exception {

        String xml = getRawMarkup("/faces/jspxview.jspx");
        assertFalse(XmlDoctype.matcher(xml).matches());
        assertFalse(XmlDeclaration.matcher(xml).matches());
        assertFalse(XmlPI.matcher(xml).matches());
        assertFalse(CDATASection.matcher(xml).matches());
        assertTrue(xml.matches("(?s).*<p>This\\s+is\\s+CDATA\\s+content</p>.*"));
        assertTrue(NotEscapedText.matcher(xml).matches());
        assertFalse(Comment.matcher(xml).matches());
    }

}
