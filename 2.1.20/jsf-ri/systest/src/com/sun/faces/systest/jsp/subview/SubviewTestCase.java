
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

package com.sun.faces.systest.jsp.subview;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;



public class SubviewTestCase extends HtmlUnitFacesTestCase {


    public SubviewTestCase(String name) {
        super(name);
    }

    public static Test suite() {
        return (new TestSuite(SubviewTestCase.class));
    }

    public void test01() throws Exception {

        HtmlPage page = getPage("/faces/subview01.jsp");
        assertTrue(page.asXml().matches("(?s).*<body>\\s*Begin\\s*test\\s*&lt;c:import&gt;\\s*with\\s*subview\\s*tag\\s*in\\s*imported\\s*page\\s*<p>\\s*foo01\\s*</p>\\s*<p>\\s*subview01\\s*</p>\\s*<p>\\s*bar01\\s*</p>\\s*<p>\\s*End\\s*test\\s*&lt;c:import&gt;\\s*with\\s*subview\\s*tag\\s*in\\s*imported\\s*page\\s*</p>\\s*</body>.*"));
    }

    public void test02() throws Exception {

        HtmlPage page = getPage("/faces/subview02.jsp");
        assertTrue(page.asXml().matches("(?s).*<body>\\s*<p>\\s*Begin\\s*test\\s*&lt;c:import&gt;\\s*with\\s*subview\\s*tag\\s*in\\s*importing\\s*page\\s*</p>\\s*<p>\\s*foo02\\s*</p>\\s*<p>\\s*subview02\\s*</p>\\s*<p>\\s*bar02\\s*</p>\\s*<p>\\s*End\\s*test\\s*&lt;c:import&gt;\\s*with\\s*subview\\s*tag\\s*in\\s*importing\\s*page\\s*</p>\\s*</body>.*"));
    }

    public void test03() throws Exception {

        HtmlPage page = getPage("/faces/subview03.jsp");
        assertTrue(page.asXml().matches("(?s).*<body>\\s*<p>\\s*Begin\\s*test\\s*&lt;c:include&gt;\\s*with\\s*subview\\s*tag\\s*in\\s*included\\s*page\\s*</p>\\s*<p>\\s*foo01\\s*</p>\\s*<p>\\s*subview03\\s*</p>\\s*<p>\\s*bar01\\s*</p>\\s*<p>\\s*End\\s*test\\s*&lt;c:include&gt;\\s*with\\s*subview\\s*tag\\s*in\\s*included\\s*page\\s*</p>\\s*</body>.*"));
    }

    public void test04() throws Exception {

        HtmlPage page = getPage("/faces/subview04.jsp");
        assertTrue(page.asXml().matches("(?s).*\\s*<body>\\s*<p>\\s*\\[A\\]\\s*</p>\\s*<p>\\s*Begin\\s*test\\s*&lt;c:include&gt;\\s*with\\s*subview\\s*tag\\s*in\\s*including\\s*page\\s*</p>\\s*<p>\\s*bar01\\s*</p>\\s*<p>\\s*subview04\\s*</p>\\s*<p>\\s*bar02\\s*</p>\\s*<p>\\s*End\\s*test\\s*&lt;c:include&gt;\\s*with\\s*subview\\s*tag\\s*in\\s*including\\s*page\\s*</p>\\s*</body>.*"));
    }

    public void test05() throws Exception {

        HtmlPage page = getPage("/faces/subview05.jsp");
        assertTrue(page.asXml().matches("(?s).*<body>\\s*<p>\\s*Begin\\s*test\\s*jsp:include\\s*with\\s*subview\\s*and\\s*iterator\\s*tag\\s*in\\s*included\\s*page\\s*</p>\\s*<br/>\\s*<p>\\s*<br/>\\s*Array\\[0\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\"subviewInner:.*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*Array\\[1\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\"subviewInner:.*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*Array\\[2\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\"subviewInner:.*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*Array\\[3\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\"subviewInner:.*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*</p>\\s*<p>\\s*Text\\s*from\\s*subview05.jsp\\s*</p>\\s*<p/>\\s*End\\s*test\\s*jsp:include\\s*with\\s*subview\\s*and\\s*iterator\\s*tag\\s*in\\s*included\\s*page\\s*<p/>\\s*</body>.*"));
    }

    public void test06() throws Exception {

        HtmlPage page = getPage("/faces/subview06.jsp");
        assertTrue(page.asXml().matches("(?s).*<body>\\s*<p>\\s*Begin\\s*test\\s*&lt;c:import&gt;\\s*with\\s*iterator\\s*tag\\s*in\\s*imported\\s*page\\s*</p>\\s*<br/>\\s*<p>\\s*<br/>\\s*Array\\[0\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\"subviewOuter:subviewInner:.*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*Array\\[1\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\"subviewOuter:subviewInner:.*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*Array\\[2\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\"subviewOuter:subviewInner:.*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*Array\\[3\\]:\\s*This\\s*component\\s*has\\s*no\\s*ID\\s*<br/>\\s*<input\\s*type=\"text\"\\s*name=\"subviewOuter:subviewInner:.*\"\\s*value=\"This\\s*component\\s*has\\s*no\\s*ID\\s*\"/>\\s*<br/>\\s*</p>\\s*<p>\\s*Text\\s*from\\s*subview06.jsp\\s*</p>\\s*<p>\\s*End\\s*test\\s*&lt;c:import&gt;\\s*with\\s*iterator\\s*tag\\s*in\\s*imported\\s*page\\s*</p>.*"));
    }
}
