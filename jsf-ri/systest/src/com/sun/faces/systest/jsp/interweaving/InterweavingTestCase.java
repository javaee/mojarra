
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

package com.sun.faces.systest.jsp.interweaving;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;



public class InterweavingTestCase extends HtmlUnitFacesTestCase {


    public InterweavingTestCase(String name) {
        super(name);
        addExclusion(Container.WLS_12_1_1_NO_CLUSTER, "test07");
        addExclusion(Container.WLS_12_1_1_NO_CLUSTER, "test13");
        addExclusion(Container.WLS_12_1_1_NO_CLUSTER, "test11");
        addExclusion(Container.WLS_12_1_1_NO_CLUSTER, "test12");
    }

    public static Test suite() {
        return (new TestSuite(InterweavingTestCase.class));
    }

    public void test01() throws Exception {

        HtmlPage page = getPage("/faces/interweaving01.jsp");
        assertTrue(page.asText().matches("(?s).*Begin\\s*test\\s*jsp include without verbatim\\s*interweaving\\s*works\\s*well!!\\s*End\\s*test\\s*jsp include without verbatim.*"
));
    }

    public void test02() throws Exception {

        HtmlPage page = getPage("/faces/interweaving02.jsp");
        assertTrue(page.asText().matches("(?s).*Begin\\s*test\\s*jstl import without verbatim\\s*interweaving\\s*works\\s*well!!\\s*End\\s*test\\s*jstl import without verbatim.*"));
    }

    public void test03() throws Exception {

        HtmlPage page = getPage("/faces/interweaving03.jsp");
        assertTrue(page.asXml().matches("(?s).*<table>\\s*<tbody>\\s*<tr>\\s*<td>\\s*Row 1\\s*</td>\\s*</tr>\\s*<tr>\\s*<td>\\s*Row 2\\s*</td>\\s*</tr>\\s*<tr>\\s*<td>\\s*Row 3\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*.*"));
    }

    public void test04() throws Exception {

        HtmlPage page = getPage("/faces/interweaving04.jsp");
        assertTrue(page.asXml().matches("(?s).*\\[First\\]\\[Second\\]\\[Third\\].*"));
    }

    public void test05() throws Exception {

        HtmlPage page = getPage("/faces/interweaving05.jsp");
        assertTrue(page.asText().matches("(?s).*Begin jstl-choose test without id\\[FIRST\\]\\[SECOND\\]End jstl-choose test without id.*"));
    }

    public void test06() throws Exception {

        HtmlPage page = getPage("/faces/interweaving06.jsp");
        assertTrue(page.asXml().matches("(?s).*<body>\\s*<p>\\s*Begin\\s*test\\s*jsp:include\\s*without\\s*subview\\s*and\\s*iterator\\s*tag\\s*in\\s*included\\s*page\\s*</p>\\s*<br/>\\s*<p>\\s*<br/>\\s*Array\\[0\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\".*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*Array\\[1\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\".*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*Array\\[2\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\".*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*Array\\[3\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\".*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*</p>\\s*<p>\\s*Text\\s*from\\s*interweaving06.jsp\\s*</p>\\s*<p/>\\s*End\\s*test\\s*jsp:include\\s*without\\s*subview\\s*and\\s*iterator\\s*tag\\s*in\\s*included\\s*page\\s*<p/>\\s*</body>.*"));
    }

    public void test07() throws Exception {
	/***  20131120 WLS 12.1.1
        HtmlPage page = getPage("/faces/interweaving07.jsp");
        assertTrue(page.asXml().matches("(?s).*\\s*<body>\\s*<p>\\s*Begin\\s*test\\s*&lt;c:import&gt;\\s*with\\s*iterator\\s*tag\\s*in\\s*imported\\s*page\\s*</p>\\s*<br/>\\s*<p>\\s*<br/>\\s*Array\\[0\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\".*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*Array\\[1\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\".*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*Array\\[2\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\".*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*Array\\[3\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\".*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*</p>\\s*<p>\\s*Text\\s*from\\s*interweaving07.jsp\\s*</p>\\s*<p>\\s*End\\s*test\\s*&lt;c:import&gt;\\s*with\\s*iterator\\s*tag\\s*in\\s*imported\\s*page\\s*</p>\\s*</body>.*"));
	***/
    }

    public void test08() throws Exception {

        // Make multiple requests to the same page and ensure the response is 200

        HtmlPage page1 = getPage("/faces/interweaving08.jsp");
        assertTrue(page1.asXml().matches("(?s).*\\s*<body>\\s*<form\\s*id=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"javax.faces.ViewState\".*<table>\\s*<tbody>\\s*<tr>\\s*<td>\\s*ciao\\s*Value\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*</form>\\s*</body>.*"));

        HtmlPage page2 = getPage("/faces/interweaving08.jsp");
        assertTrue(page2.asXml().matches("(?s).*\\s*<body>\\s*<form\\s*id=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"javax.faces.ViewState\".*<table>\\s*<tbody>\\s*<tr>\\s*<td>\\s*ciao\\s*Value\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*</form>\\s*</body>.*"));

        HtmlPage page3 = getPage("/faces/interweaving08.jsp");
        assertTrue(page3.asXml().matches("(?s).*\\s*<body>\\s*<form\\s*id=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"javax.faces.ViewState\".*<table>\\s*<tbody>\\s*<tr>\\s*<td>\\s*ciao\\s*Value\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*</form>\\s*</body>.*"));
    }

    public void test09() throws Exception {

        // Make multiple requests to the same page and ensure the response is 200

        HtmlPage page1 = getPage("/faces/interweaving09.jsp");
        assertTrue(page1.asXml().matches("(?s).*\\s*<body>\\s*<form\\s*id=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"javax.faces.ViewState\".*<table>\\s*<tbody>\\s*<tr>\\s*<td>\\s*Value\\s*ciao\\s*Value\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*</form>\\s*</body>.*"));

        HtmlPage page2 = getPage("/faces/interweaving09.jsp");
        assertTrue(page2.asXml().matches("(?s).*\\s*<body>\\s*<form\\s*id=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"javax.faces.ViewState\".*<table>\\s*<tbody>\\s*<tr>\\s*<td>\\s*Value\\s*ciao\\s*Value\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*</form>\\s*</body>.*"));

        HtmlPage page3 = getPage("/faces/interweaving09.jsp");
        assertTrue(page3.asXml().matches("(?s).*\\s*<body>\\s*<form\\s*id=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"javax.faces.ViewState\".*<table>\\s*<tbody>\\s*<tr>\\s*<td>\\s*Value\\s*ciao\\s*Value\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*</form>\\s*</body>.*"));
    }

    public void test10() throws Exception {

        // Make multiple requests to the same page and ensure the response is 200

        HtmlPage page1 = getPage("/faces/interweaving10.jsp");
        assertTrue(page1.asXml().matches("(?s).*\\s*<body>\\s*<form\\s*id=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"javax.faces.ViewState\".*<table>\\s*<tbody>\\s*<tr>\\s*<td>\\s*Value\\s*ciao\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*</form>\\s*</body>.*"));

        HtmlPage page2 = getPage("/faces/interweaving10.jsp");
        assertTrue(page2.asXml().matches("(?s).*\\s*<body>\\s*<form\\s*id=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"javax.faces.ViewState\".*<table>\\s*<tbody>\\s*<tr>\\s*<td>\\s*Value\\s*ciao\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*</form>\\s*</body>.*"));

        HtmlPage page3 = getPage("/faces/interweaving10.jsp");
        assertTrue(page3.asXml().matches("(?s).*\\s*<body>\\s*<form\\s*id=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"form\".*<input\\s*type=\"hidden\"\\s*name=\"javax.faces.ViewState\".*<table>\\s*<tbody>\\s*<tr>\\s*<td>\\s*Value\\s*ciao\\s*</td>\\s*</tr>\\s*</tbody>\\s*</table>\\s*</form>\\s*</body>.*"));
    }

    public void test11() throws Exception {

        HtmlPage page = getPage("/faces/interweaving11.jsp");
        assertTrue(page.asXml().matches("(?s).*\\s*<body>\\s*<form.*<input\\s*type=\"hidden\".*<input\\s*type=\"hidden\"\\s*name=\"javax.faces.ViewState\".*<script.*jsf.js.*<a\\s*href.*>\\s*one\\s*</a>\\s*<a\\s*href.*>\\s*two\\s*</a>\\s*<a\\s*href.*>\\s*three\\s*</a>\\s*</form>\\s*</body>.*"));
    }

    public void test12() throws Exception {

        HtmlPage page = getPage("/faces/interweaving12.jsp");
        assertTrue(page.asXml().matches("(?s).*\\s*<body>\\s*<form.*<input\\s*type=\"hidden\".*<input\\s*type=\"hidden\"\\s*name=\"javax.faces.ViewState\".*this should be before the button\\s*<input.*type=\"submit\".*value=\"commandButton 1\"\\s*/>\\s*</form>\\s*</body>.*"));
    }

    public void test13() throws Exception {

        HtmlPage page = getPage("/faces/include-import-interweaving.jsp");
        assertTrue(page.asXml().matches("(?s).*\\s*<body.*<form.*<input\\s*type=\"hidden\".*<input\\s*type=\"hidden\"\\s*name=\"javax.faces.ViewState\".*Outer Template Text followed by\\s*<span\\s*id=\"form:outerComponent\">\\s*outer component\\s*</span>.*Subview on the outside only with jsp:include:.*Subview 1: Inner template text followed by\\s*<span\\s*id=\"form:subview01:innerComponent1\">\\s*inner component 1\\s*</span>.*Subview 1: Inner template 2 text followed by\\s*<span\\s*id=\"form:subview01:innerComponent2\">\\s*inner component 2\\s*</span>.*Subview on the inside only with jsp:include:.*Subview 2: Inner template text followed by\\s*<span\\s*id=\"form:innerSubView:innerComponent3\">\\s*inner component 3\\s*</span>.*Subview on the outside and inside with jsp:include:.*Template Text before the include.*Subview 2: Inner template text followed by\\s*<span\\s*id=\"form:subview03:innerSubView2:innerComponent4\">\\s*inner component 4\\s*</span>.*Subview on the outside only with c:import:.*Subview 4: Inner template text followed by\\s*<span\\s*id=\"form:outerSubview3:innerComponent5\">\\s*inner component 5\\s*</span>.*Subview 4: Inner template 2 text followed by\\s*<span\\s*id=\"form:outerSubview3:innerComponent6\">\\s*inner component 6\\s*</span>.*Subview on the inside only with c:import:.*Subview 5: Inner template text followed by\\s*<span\\s*id=\"form:innerSubView4:innerComponent7\">\\s*inner component 7\\s*</span>.*Subview on the outside and inside with c:import:.*Template Text on the outside.*Subview 6: Inner template text followed by\\s*<span\\s*id=\"form:subview05:innerSubView5:innerComponent8\">\\s*inner component 8\\s*</span>.*</form>\\s*</body>.*"));
    }
}
