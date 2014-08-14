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
package com.sun.faces.systest;

import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;

import javax.faces.component.NamingContainer;

import junit.framework.Test;
import junit.framework.TestSuite;

public class Issue2197TestCase extends HtmlUnitFacesTestCase {

    public Issue2197TestCase(String name) {
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
        return (new TestSuite(Issue2197TestCase.class));
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @Override
    public void tearDown() {
        super.tearDown();
    }

    // ------------------------------------------------------------ Test Methods
    public void testConvertNumber() throws Exception {
        HtmlPage page = getPage("/faces/convertNumber.xhtml");
        HtmlTextInput input = (HtmlTextInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "register" +
                                NamingContainer.SEPARATOR_CHAR +
                                "name");
        input.setValueAttribute("Foo");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "button");
        page = (HtmlPage) submit.click();
        assertTrue(page.asText().contains("could not be understood as a percentage"));
    }

    public void testConverter() throws Exception {
        HtmlPage page = getPage("/faces/converter.xhtml");
        HtmlTextInput input = (HtmlTextInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "register" +
                                NamingContainer.SEPARATOR_CHAR +
                                "name");
        input.setValueAttribute("Foo");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "button");
        page = (HtmlPage) submit.click();
        assertTrue(page.asText().contains("must be a number consisting of one or more digits"));
    }

    public void testConvertDateTime() throws Exception {
        HtmlPage page = getPage("/faces/convertDateTime.xhtml");
        HtmlTextInput input = (HtmlTextInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "register" +
                                NamingContainer.SEPARATOR_CHAR +
                                "name");
        input.setValueAttribute("Foo");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "button");
        page = (HtmlPage) submit.click();
        assertTrue(page.asText().contains("could not be understood as a date"));
    }

    public void testValidateLength() throws Exception {
        HtmlPage page = getPage("/faces/validateLength.xhtml");
        HtmlTextInput input = (HtmlTextInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "register" +
                                NamingContainer.SEPARATOR_CHAR +
                                "name");
        input.setValueAttribute("FooFoo");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "button");
        page = (HtmlPage) submit.click();
        assertTrue(page.asText().contains("Length is greater than allowable maximum"));
    }


    public void testActionListener() throws Exception {
        HtmlPage page = getPage("/faces/actionListener.xhtml");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "mybutton" +
                                NamingContainer.SEPARATOR_CHAR +
                                "name");
        page = (HtmlPage) submit.click();
        assertTrue(page.asText().contains("name was pressed"));
    }

    public void testValueChangeListener() throws Exception {
        HtmlPage page = getPage("/faces/valueChangeListener.xhtml");
        HtmlTextInput input = (HtmlTextInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "register" +
                                NamingContainer.SEPARATOR_CHAR +
                                "name");
        input.setValueAttribute("FooFoo");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "button");
        page = (HtmlPage) submit.click();
        assertTrue(page.asText().contains("name value was changed"));
    }

    public void testSetPropertyActionListener() throws Exception {
        HtmlPage page = getPage("/faces/setPropertyActionListener.xhtml");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "mybutton" +
                                NamingContainer.SEPARATOR_CHAR +
                                "name");
        page = (HtmlPage) submit.click();
        assertTrue(page.asText().contains("foo"));
    }

    public void testValidateDoubleRange() throws Exception {
        HtmlPage page = getPage("/faces/validateDoubleRange.xhtml");
        HtmlTextInput input = (HtmlTextInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "register" +
                                NamingContainer.SEPARATOR_CHAR +
                                "name");
        input.setValueAttribute("123456");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "button");
        page = (HtmlPage) submit.click();
        assertTrue(page.asText().contains("Specified attribute is not between the expected values of 2 and 5"));
    }

    public void testValidateLongRange() throws Exception {
        HtmlPage page = getPage("/faces/validateLongRange.xhtml");
        HtmlTextInput input = (HtmlTextInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "register" +
                                NamingContainer.SEPARATOR_CHAR +
                                "name");
        input.setValueAttribute("123456");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "button");
        page = (HtmlPage) submit.click();
        assertTrue(page.asText().contains("Specified attribute is not between the expected values of 2 and 5"));
    }

    public void testValidateRequired() throws Exception {
        HtmlPage page = getPage("/faces/validateRequired.xhtml");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "button");
        page = (HtmlPage) submit.click();
        assertTrue(page.asText().contains("Value is required"));
    }

    public void testValidator() throws Exception {
        HtmlPage page = getPage("/faces/validator.xhtml");
        HtmlTextInput input = (HtmlTextInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "register" +
                                NamingContainer.SEPARATOR_CHAR +
                                "name");
        input.setValueAttribute("123456");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "button");
        page = (HtmlPage) submit.click();
        assertTrue(page.asText().contains("name was validated"));
    }

    public void testValidateRegex() throws Exception {
        HtmlPage page = getPage("/faces/validateRegex.xhtml");
        HtmlTextInput input = (HtmlTextInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "register" +
                                NamingContainer.SEPARATOR_CHAR +
                                "name");
        input.setValueAttribute("$$$$$$$$$$$");
        HtmlSubmitInput submit = (HtmlSubmitInput)
            page.getElementById("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "button");
        page = (HtmlPage) submit.click();
        assertTrue(page.asText().contains("Regex Pattern not matched"));
    }
}
