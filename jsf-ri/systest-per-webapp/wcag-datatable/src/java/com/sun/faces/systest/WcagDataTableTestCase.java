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

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.faces.component.NamingContainer;


/**
 * <p>Make sure that an application that replaces the ApplicationFactory
 * but uses the decorator pattern to allow the existing ApplicationImpl
 * to do the bulk of the requests works.</p>
 */
public class WcagDataTableTestCase extends HtmlUnitFacesTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public WcagDataTableTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


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
        return (new TestSuite(WcagDataTableTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------ Instance Variables



    // ------------------------------------------------- Individual Test Methods

    /*
     *
     * <p>Verify that the bean is successfully resolved</p>
     */
    public void testReplaceStateManager() throws Exception {
        HtmlPage page = getPage("/faces/index.jsp");
        String pageText = page.asXml();
        //System.out.println(pageText);
        // (?s) is an "embedded flag expression" for the "DOTALL" operator.
        // It says, "let . match any character including line terminators."
        // Because page.asXml() returns a big string with lots of \r\n chars
        // in it, we need (?s).
        // the page contains a table tag with a frame attribute whose value is hsides.
        assertTrue(pageText.matches("(?s).*<table.*frame=.hsides.*>.*"));
        // the page contains a table tag with a rules attribute whose value is groups.
        assertTrue(pageText.matches("(?s).*<table.*rules..groups.*>.*"));
        // the page contains a table tag with a summary attribute whose value is that string.
        assertTrue(pageText.matches("(?s).*<table.*summary..Code page support in different versions of MS Windows.*>.*"));
        // the page contains a table tag followed immediately by the caption element as follows.
        assertTrue(pageText.matches("(?sm).*<table.*>\\s*<caption>.*CODE-PAGE SUPPORT IN MICROSOFT WINDOWS.*</caption>.*"));
        // the page contains a close caption tag followed immediately by a three colgroup tags as follows.
        assertTrue(pageText.matches("(?sm).*</caption>\\s*" +
                "<colgroup align=.center.\\s*/>\\s*" +
                "<colgroup align=.left.\\s*/>\\s*" +
                "<colgroup align=.center.*span=.2.*/>\\s*" +
                "<colgroup align=.center.*span=.3.*/>.*"));

        // A table with a thead, with a tr with a th scope=col
        assertTrue(pageText.matches("(?sm).*<table.*>.*<thead>\\s*" +
                "<tr>\\s*" +
                "<th\\s*scope=.col.*"));
        
        // A table with a tbody, with a tr with a th scope=row
        assertTrue(pageText.matches("(?sm).*<table.*>.*<tbody>.*" +
                "<th\\s*scope=.row.*"));

        // A table with a tbody, with a tr with a th scope=row
        assertTrue(pageText.matches("(?sm).*<table.*>.*<tbody>.*" +
                "</tbody>.*<tbody>.*</tbody>.*</table>.*"));

    }

}
