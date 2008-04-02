/*
 * $Id: TestComponents.java,v 1.15 2004/05/12 18:47:37 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package com.sun.faces.demotest.components;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.*;
import com.sun.faces.demotest.HtmlUnitTestCase;

import java.util.Iterator;

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

        for (int i = 0, len = welcomeTexts.length; i < len; i++) {
            form = (HtmlForm) mapPage.getAllForms().get(0);

            // commented out until the Javascript interpreter in HtmlUnit
            // can handle form access via index.

            //	    map = (HtmlMap) form.getChildElements().get(0);
            //	    area = (HtmlArea) map.getChildElements().get(i);
            //	    onClick = area.getOnClickAttribute();
            //	    result = mapPage.executeJavaScriptIfPossible(onClick,
            //							 onClick,
            //							 false, area);
            //	    mapPage = (HtmlPage) result.getNewPage();

            // set the value of the hidden field manually and submit the form.
            HtmlHiddenInput hidden =
                (HtmlHiddenInput) form.getInputByName("worldMap_current");
            assertNotNull(hidden);
            hidden.setValueAttribute(lang[i]);
            mapPage = (HtmlPage) form.submit();
            assertTrue(
                -1 != getImageMapWelcomeText(mapPage).indexOf(welcomeTexts[i]));
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
        page = executeTabbedPane(page, "_id0:_id2");
        //tab 2
        page = executeTabbedPane(page, "_id0:_id6");
        //tab3
        page = executeTabbedPane(page, "_id0:_id21");
    }


    protected HtmlPage executeTreeTest(HtmlPage page,
                                       String treeNum) throws Exception {
        HtmlAnchor anchor = null;
        HtmlForm form = (HtmlForm) page.getAllForms().get(0);
        assertNotNull(form);
        HtmlHiddenInput hidden = null;

        // verify that clicking on the "File " + treeNum link causes the
        // menu choices of that menu to disappear.
        anchor = page.getFirstAnchorByText("File " + treeNum);
        assertNotNull(anchor);
        // simulate the link being clicked
        hidden = (HtmlHiddenInput) form.getInputByName("_id0:menu" +
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
        hidden = (HtmlHiddenInput) form.getInputByName("_id0:menu" +
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
        hidden = (HtmlHiddenInput) form.getInputByName("_id0:menu" +
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
        HtmlForm form = (HtmlForm) page.getAllForms().get(0);
        assertNotNull(form);
        HtmlHiddenInput hidden1, hidden2 = null;
        // verify that clicking on the <newListNum> link causes the
        // the correct current page display.
        // simulate the link being clicked

        hidden1 = (HtmlHiddenInput) form.getInputByName("_id0:_id9_curPage");
        assertNotNull(hidden1);
        hidden1.setValueAttribute(currentListNum);

        hidden2 = (HtmlHiddenInput) form.getInputByName("_id0:_id9_action");
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
        HtmlForm form = (HtmlForm) page.getAllForms().get(0);
        assertNotNull(form);
        HtmlInput button = (HtmlInput) form.getInputByName(buttonName);
        page = (HtmlPage) button.click();
        form = (HtmlForm) page.getAllForms().get(0);
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
    
