/*
 * $Id: TestComponents.java,v 1.22 2007/04/27 22:00:48 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.demotest.components;

import java.util.Iterator;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlArea;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlMap;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.sun.faces.demotest.HtmlUnitTestCase;

public class TestComponents extends HtmlUnitTestCase {


    /*
     * test the image map component
     */
    public void testImageMap() throws Exception {
        String[] welcomeTexts = {
              "Welcome",
              "Bienvenido",
              "Tervetuloa",
              "Wilkommen",
              "Bienvenue"
        };

        String[] lang = {
              "NAmerica",
              "SAmerica",
              "Finland",
              "Germany",
              "France"
        };

        HtmlPage mapPage = null;
        HtmlForm form = null;
        HtmlMap map = null;
        HtmlArea area = null;
        String onClick = null;
        ScriptResult result = null;
        mapPage = accessAppAndGetPage("imagemap.faces");
        form = (HtmlForm) mapPage.getForms().get(0);
        map = (HtmlMap) form.getHtmlElementsByTagName("map").get(0);

        for (int i = 0, len = welcomeTexts.length; i < len; i++) {

            area = (HtmlArea) map.getHtmlElementsByTagName("area").get(i);
            onClick = area.getOnClickAttribute();
            result = mapPage.executeJavaScriptIfPossible(onClick,
                                                         onClick,
                                                         false,
                                                         area);
            mapPage = (HtmlPage) result.getNewPage();

            assertTrue(
                  -1 != getImageMapWelcomeText(mapPage)
                        .indexOf(welcomeTexts[i]));
        }

    }


    public void testTree() throws Exception {
        HtmlPage page = accessAppAndGetPage("menu.faces");
        assertNotNull(page);
        page = executeTreeTest(page, "2");
        page = executeTreeTest(page, "3");
        page = executeTreeTest(page, "4");
        // PENDING(): would like to be able to use a regex for
        // getFirstAnchorByText.  That would enable the "img link" case
        // to work.  For now, skip it.
        // page = executeTreeTest(page, "5");
    }


    public void testResultSet() throws Exception {
        HtmlPage page = accessAppAndGetPage("result-set.faces");
        HtmlAnchor anchor = null;
        assertNotNull(page);
        page = executeResultSet(page, "2", "3");
        //"-1" is action value for "Next"
        page = executeResultSet(page, "5", "-1");
        //"-2" is action value for "Previous"
        page = executeResultSet(page, "10", "-2");
        //stay on same page
        page = executeResultSet(page, "10", "10");
    }


    public void testTabbedPane() throws Exception {
        HtmlPage page = accessAppAndGetPage("tabbedpanes.faces");
        assertNotNull(page);
        //tab 1
        page = executeTabbedPane(page, "j_id_id18:j_id_id28");
        //tab 2
        page = executeTabbedPane(page, "j_id_id18:j_id_id46");
        //tab3
        page = executeTabbedPane(page, "j_id_id18:j_id_id28");
    }


    protected HtmlPage executeTreeTest(HtmlPage page,
                                       String treeNum) throws Exception {
        HtmlAnchor anchor = null;
        HtmlForm form = (HtmlForm) page.getForms().get(0);
        assertNotNull(form);
        HtmlHiddenInput hidden = null;

        // verify that clicking on the "File " + treeNum link causes the
        // menu choices of that menu to disappear.
        anchor = page.getFirstAnchorByText("File " + treeNum);
        assertNotNull(anchor);
        // simulate the link being clicked
        hidden = (HtmlHiddenInput) form.getInputByName("j_id_id14:menu" +
                                                       treeNum);
        assertNotNull(hidden);
        hidden.setValueAttribute("/File");
        page = (HtmlPage) form.submit();

        // verify the "File " + treeNum menu disappears
        try {
            anchor = page.getFirstAnchorByText("New " + treeNum);
            assertTrue(false);
        } catch (ElementNotFoundException e) {
            assertTrue(true);
        }

        // verify that clicking on the "File " + treeNum link again,
        // causes the menu to re-appear.
        anchor = page.getFirstAnchorByText("File " + treeNum);
        assertNotNull(anchor);
        // simulate the link being clicked
        hidden = (HtmlHiddenInput) form.getInputByName("j_id_id14:menu" +
                                                       treeNum);
        assertNotNull(hidden);
        hidden.setValueAttribute("/File");
        page = (HtmlPage) form.submit();

        // verify the "File " + treeNum menu re-appears
        anchor = page.getFirstAnchorByText("New " + treeNum);
        assertNotNull(anchor);

        // verify that clicking on the "New " + treeNum link takes you
        // to the right page.
        page = (HtmlPage) anchor.click();
        assertNotNull(page);

        // go back to the tree page
        anchor = page.getFirstAnchorByText("Back");
        assertNotNull(anchor);
        page = (HtmlPage) anchor.click();
        assertNotNull(page);

        // verify that clicking on the "Edit " + treeNum link causes the
        // "File " + treeNum menu choices to clean maindisappear, and the "Edit "
        // + treeNum menu choices to appear.
        anchor = page.getFirstAnchorByText("Edit " + treeNum);
        assertNotNull(anchor);
        // simulate link being clicked
        hidden = (HtmlHiddenInput) form.getInputByName("j_id_id14:menu" +
                                                       treeNum);
        assertNotNull(hidden);
        hidden.setValueAttribute("/Edit");
        page = (HtmlPage) form.submit();

        // verify the expected elements
        try {
            anchor = page.getFirstAnchorByText("Open " + treeNum);
            assertTrue(false);
        } catch (ElementNotFoundException e) {
            assertTrue(true);
        }
        anchor = page.getFirstAnchorByText("Cut " + treeNum);
        assertNotNull(anchor);

        // verify that clicking on the "Cut " + treeNum link takes you
        // to the right page.
        page = (HtmlPage) anchor.click();
        assertNotNull(page);

        // go back to the tree page
        anchor = page.getFirstAnchorByText("Back");
        assertNotNull(anchor);
        page = (HtmlPage) anchor.click();
        assertNotNull(page);

        return page;
    }


    protected HtmlPage executeResultSet(HtmlPage page, String currentListNum,
                                        String newListNum) throws Exception {
        HtmlAnchor anchor = null;
        HtmlForm form = (HtmlForm) page.getForms().get(0);
        assertNotNull(form);
        HtmlHiddenInput hidden1, hidden2 = null;
        // verify that clicking on the <newListNum> link causes the
        // the correct current page display.
        // simulate the link being clicked

        hidden1 = (HtmlHiddenInput) form
              .getInputByName("j_id_id19:j_id_id64_curPage");
        assertNotNull(hidden1);
        hidden1.setValueAttribute(currentListNum);

        hidden2 =
              (HtmlHiddenInput) form.getInputByName("j_id_id19:j_id_id64_action");
        assertNotNull(hidden2);
        hidden2.setValueAttribute(newListNum);
        page = (HtmlPage) form.submit();

        // If we've pressed the "next" link
        if (newListNum.equals("-1")) {
            int newNum = Integer.valueOf(currentListNum).intValue() + 1;
            newListNum = Integer.toString(newNum);
            // If we've pressed the "previous" link
        } else if (newListNum.equals("-2")) {
            int newNum = Integer.valueOf(currentListNum).intValue() - 1;
            newListNum = Integer.toString(newNum);
        }

        try {
            anchor = page.getFirstAnchorByText(newListNum);
            assertTrue(false);
        } catch (ElementNotFoundException e) {
            assertTrue(true);
        }

        return page;
    }


    protected HtmlPage executeTabbedPane(HtmlPage page, String buttonName)
          throws Exception {
        HtmlForm form = (HtmlForm) page.getForms().get(0);
        assertNotNull(form);
        HtmlInput button = (HtmlInput) form.getInputByName(buttonName);
        page = (HtmlPage) button.click();
        form = (HtmlForm) page.getForms().get(0);
        button = (HtmlInput) form.getInputByName(buttonName);
        assertTrue(button.getClassAttribute().equals("tabbed-selected"));
        return page;
    }


    private HtmlPage accessAppAndGetPage(String contextUri) throws Exception {

        HtmlPage page = (HtmlPage) getInitialPage();
        HtmlAnchor imagemapAnchor = page.getAnchorByHref(contextUri);
        assertNotNull(imagemapAnchor);
        page = (HtmlPage) imagemapAnchor.click();
        assertNotNull(page);

        return page;
    }


    private String getImageMapWelcomeText(HtmlPage page) {
        String result = null;
        for (Iterator i = page.getAllHtmlChildElements(); i.hasNext();) {
            HtmlElement element = (HtmlElement) i.next();
            if (element instanceof HtmlTable) {
                HtmlTable table = (HtmlTable) element;
                result = table.getCellAt(0, 0).asText().trim();
                break;
            }
        }
        return result;
    }


} // end of class DemoTest01
    
