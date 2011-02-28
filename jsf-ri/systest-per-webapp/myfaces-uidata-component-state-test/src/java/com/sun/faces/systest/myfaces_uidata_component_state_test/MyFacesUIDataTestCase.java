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

package com.sun.faces.systest.myfaces_uidata_component_state_test;

import com.gargoylesoftware.htmlunit.html.*;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

public class MyFacesUIDataTestCase extends HtmlUnitFacesTestCase {

    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public MyFacesUIDataTestCase(String name) {
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
        return (new TestSuite(MyFacesUIDataTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }

    // ------------------------------------------------- Individual Test Methods

    public void testShowProblem() throws Exception {
        HtmlPage page = getPage("/showProblem.jsf");
        assertNoRedText(page);

        HtmlTextInput field = (HtmlTextInput) page.getElementById("form:inputClientId");
        field.setValueAttribute("form:data:2:cities:3:city");
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("form:button");

        page = button.click();

        assertRedTextCount(page, 18);


    }

    public void testSimpleDatatable() throws Exception {
        HtmlPage page = getPage("/simpleDatatable.jsf");
        assertNoRedText(page);

        HtmlTextInput field = (HtmlTextInput) page.getElementById("form:inputClientId");
        field.setValueAttribute("form:data:1:isoCode");
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("form:button");

        page = button.click();

        assertRedTextCount(page, 1);


    }

    public void testNestedDatatable() throws Exception {
        HtmlPage page = getPage("/nestedDatatable.jsf");
        assertNoRedText(page);

        HtmlTextInput field = (HtmlTextInput) page.getElementById("form:inputClientId");
        field.setValueAttribute("form:data:2:cities:3:city");
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("form:button");

        page = button.click();

        assertRedTextCount(page, 1);


    }

    public void testSimpleDatatableCCResource() throws Exception {
        HtmlPage page = getPage("/simpleDatatableCCResource.jsf");
        assertNoRedText(page);

        HtmlTextInput field = (HtmlTextInput) page.getElementById("form:inputClientId");
        field.setValueAttribute("form:data:1:isoCode:text");
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("form:button");

        page = button.click();

        assertRedTextCount(page, 1);


    }

    public void testNestedDatatableCCResource() throws Exception {
        HtmlPage page = getPage("/nestedDatatableCCResource.jsf");
        assertNoRedText(page);

        HtmlTextInput field = (HtmlTextInput) page.getElementById("form:inputClientId");
        field.setValueAttribute("form:data:2:cities:3:city:text");
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("form:button");

        page = button.click();

        assertRedTextCount(page, 1);


    }

    public void testSimpleDatatableCCInsertChildren() throws Exception {
        HtmlPage page = getPage("/simpleDatatableCCInsertChildren.jsf");
        assertNoRedText(page);

        HtmlTextInput field = (HtmlTextInput) page.getElementById("form:inputClientId");
        field.setValueAttribute("form:data:1:text:isoCode");
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("form:button");

        page = button.click();

        assertRedTextCount(page, 1);


    }

    public void testNestedDatatableCCInsertChildren() throws Exception {
        HtmlPage page = getPage("/nestedDatatableCCInsertChildren.jsf");
        assertNoRedText(page);

        HtmlTextInput field = (HtmlTextInput) page.getElementById("form:inputClientId");
        field.setValueAttribute("form:data:2:cities:3:text:city");
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("form:button");

        page = button.click();

        assertRedTextCount(page, 1);


    }

    public void assertNoRedText(HtmlPage page) {
        String xml = page.asXml();
        assertFalse(xml.contains("class=\"redbackground\""));
    }

    public void assertRedTextCount(HtmlPage page, int redCount) {
        String xml = page.asXml();
        int i = 0, j = 0, k = 0;

         while (-1 != j) {
            j = xml.indexOf("class=\"redbackground\"", k);
            if (j == -1) {
                break;
            }
            i++;
            k = j + 1;
        }
        assertEquals(redCount, i);
    }


}
