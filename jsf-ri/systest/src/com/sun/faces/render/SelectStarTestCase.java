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

package com.sun.faces.render;

import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.*;

import java.util.List;

public class SelectStarTestCase extends AbstractTestCase {

    public SelectStarTestCase(String name) {
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
        return (new TestSuite(SelectStarTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testSelectStarXhtml() throws Exception {
        String failMsg;

        getPage("/faces/render/selectStarNoSelection.xhtml");
        System.out.println("Start select star test case - facelets");

        // Check SelectManyListbox
        HtmlSelect selectOneList = (HtmlSelect) lastpage.getHtmlElementById("selectOneListbox");
        List<HtmlOption> selectOneListOptions = selectOneList.getOptions();
        failMsg = "Wrong number of options for SelectManyListbox, expected 5, got "+selectOneListOptions.size();
        assertTrue(failMsg,selectOneListOptions.size() == 5);
        HtmlOption selectOption = selectOneListOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectOneListOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectOneListbox
        HtmlSelect selectOneMenu = (HtmlSelect) lastpage.getHtmlElementById("selectOneMenu");
        List<HtmlOption> selectOneMenuOptions = selectOneMenu.getOptions();
        assertTrue(selectOneMenuOptions.size() == 5);
        selectOption = selectOneMenuOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectOneMenuOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectOneRadio
        HtmlInput selectOneRadio0 = (HtmlInput) lastpage.getHtmlElementById("selectOneRadio:0");
        assertTrue(selectOneRadio0.getValueAttribute().equals("noSelection"));
        HtmlInput selectOneRadio1 = (HtmlInput) lastpage.getHtmlElementById("selectOneRadio:1");
        assertTrue(selectOneRadio1.getValueAttribute().equals("Apple"));

        // Check SelectManyCheckbox
        HtmlInput selectManyCheckbox0 = (HtmlInput) lastpage.getHtmlElementById("selectManyCheckbox:0");
        assertTrue(selectManyCheckbox0.getValueAttribute().equals("noSelection"));
        HtmlInput selectManyCheckbox1 = (HtmlInput) lastpage.getHtmlElementById("selectManyCheckbox:1");
        assertTrue(selectManyCheckbox1.getValueAttribute().equals("Apple"));

        // Check SelectManyListbox
        HtmlSelect selectManyListbox = (HtmlSelect) lastpage.getHtmlElementById("selectManyListbox");
        List<HtmlOption> selectManyListOptions = selectManyListbox.getOptions();
        assertTrue(selectManyListOptions.size() == 5);
        selectOption = selectManyListOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectManyListOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectManyMenu
        HtmlSelect selectManyMenu = (HtmlSelect) lastpage.getHtmlElementById("selectManyMenu");
        List<HtmlOption> selectManyMenuOptions = selectManyMenu.getOptions();
        assertTrue(selectManyMenuOptions.size() == 5);
        selectOption = selectManyMenuOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectManyMenuOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));
    }

    public void testSelectStarJspx() throws Exception {
        String failMsg;

        getPage("/faces/render/selectStarNoSelection.jspx");
        System.out.println("Start select star test case - jspx");

        // Check SelectManyListbox
        HtmlSelect selectOneList = (HtmlSelect) lastpage.getHtmlElementById("selectOneListbox");
        List<HtmlOption> selectOneListOptions = selectOneList.getOptions();
        failMsg = "Wrong number of options for SelectManyListbox, expected 5, got "+selectOneListOptions.size();
        assertTrue(failMsg,selectOneListOptions.size() == 5);
        HtmlOption selectOption = selectOneListOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectOneListOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectOneListbox
        HtmlSelect selectOneMenu = (HtmlSelect) lastpage.getHtmlElementById("selectOneMenu");
        List<HtmlOption> selectOneMenuOptions = selectOneMenu.getOptions();
        assertTrue(selectOneMenuOptions.size() == 5);
        selectOption = selectOneMenuOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectOneMenuOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectOneRadio
        HtmlInput selectOneRadio0 = (HtmlInput) lastpage.getHtmlElementById("selectOneRadio:0");
        assertTrue(selectOneRadio0.getValueAttribute().equals("noSelection"));
        HtmlInput selectOneRadio1 = (HtmlInput) lastpage.getHtmlElementById("selectOneRadio:1");
        assertTrue(selectOneRadio1.getValueAttribute().equals("Apple"));

        // Check SelectManyCheckbox
        HtmlInput selectManyCheckbox0 = (HtmlInput) lastpage.getHtmlElementById("selectManyCheckbox:0");
        assertTrue(selectManyCheckbox0.getValueAttribute().equals("noSelection"));
        HtmlInput selectManyCheckbox1 = (HtmlInput) lastpage.getHtmlElementById("selectManyCheckbox:1");
        assertTrue(selectManyCheckbox1.getValueAttribute().equals("Apple"));

        // Check SelectManyListbox
        HtmlSelect selectManyListbox = (HtmlSelect) lastpage.getHtmlElementById("selectManyListbox");
        List<HtmlOption> selectManyListOptions = selectManyListbox.getOptions();
        assertTrue(selectManyListOptions.size() == 5);
        selectOption = selectManyListOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectManyListOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectManyMenu
        HtmlSelect selectManyMenu = (HtmlSelect) lastpage.getHtmlElementById("selectManyMenu");
        List<HtmlOption> selectManyMenuOptions = selectManyMenu.getOptions();
        assertTrue(selectManyMenuOptions.size() == 5);
        selectOption = selectManyMenuOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("noSelection"));
        assertTrue(selectOption.asText().equals("No selection"));
        selectOption = selectManyMenuOptions.get(1);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

    }

    public void testSelectStarXhtmlHide() throws Exception {
        String failMsg;

        getPage("/faces/render/selectStarSelectionHideNoSelection.xhtml");
        System.out.println("Start select star test case - facelets");

        // Check SelectManyListbox
        HtmlSelect selectOneList = (HtmlSelect) lastpage.getHtmlElementById("selectOneListbox");
        List<HtmlOption> selectOneListOptions = selectOneList.getOptions();
        failMsg = "Wrong number of options for SelectManyListbox, expected 4, got "+selectOneListOptions.size();
        assertTrue(failMsg,selectOneListOptions.size() == 4);
        HtmlOption selectOption = selectOneListOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectOneListbox
        HtmlSelect selectOneMenu = (HtmlSelect) lastpage.getHtmlElementById("selectOneMenu");
        List<HtmlOption> selectOneMenuOptions = selectOneMenu.getOptions();
        assertTrue(selectOneMenuOptions.size() == 4);
        selectOption = selectOneMenuOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectOneRadio
        HtmlInput selectOneRadio0 = (HtmlInput) lastpage.getHtmlElementById("selectOneRadio:1");
        assertTrue(selectOneRadio0.getValueAttribute().equals("Apple"));

        // Check SelectManyCheckbox
        HtmlInput selectManyCheckbox0 = (HtmlInput) lastpage.getHtmlElementById("selectManyCheckbox:1");
        assertTrue(selectManyCheckbox0.getValueAttribute().equals("Apple"));

        // Check SelectManyListbox
        HtmlSelect selectManyListbox = (HtmlSelect) lastpage.getHtmlElementById("selectManyListbox");
        List<HtmlOption> selectManyListOptions = selectManyListbox.getOptions();
        assertTrue(selectManyListOptions.size() == 4);
        selectOption = selectManyListOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));

        // Check SelectManyMenu
        HtmlSelect selectManyMenu = (HtmlSelect) lastpage.getHtmlElementById("selectManyMenu");
        List<HtmlOption> selectManyMenuOptions = selectManyMenu.getOptions();
        assertTrue(selectManyMenuOptions.size() == 4);
        selectOption = selectManyMenuOptions.get(0);
        assertTrue(selectOption.getValueAttribute().equals("Apple"));
        assertTrue(selectOption.asText().equals("Apple"));
    }


}