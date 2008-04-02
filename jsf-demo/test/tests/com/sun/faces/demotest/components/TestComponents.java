/*
 * $Id: TestComponents.java,v 1.11 2003/10/23 20:37:32 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.demotest.components;

import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.sun.faces.demotest.HtmlUnitTestCase;

import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;

public class TestComponents extends HtmlUnitTestCase {


    /*
     * test the image map component
     */
    public void testImageMap() throws Exception {
	String [] welcomeTexts = {
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
	mapPage = accessAppAndGetPage("imagemap.jsf");
	
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
	    assertTrue(-1 != getImageMapWelcomeText(mapPage).indexOf(welcomeTexts[i]));
	}
	
    }

    public void testTree() throws Exception {
	HtmlPage page = accessAppAndGetPage("menu.jsf");
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
	/********************* PENDING(visvan): uncomment this
	HtmlPage page = accessAppAndGetPage("result-set.jsf");
	HtmlAnchor anchor = null;
	assertNotNull(page);
	page = executeResultSet(page, "2", "3");
	//"-1" is action value for "Next"
	page = executeResultSet(page, "5", "-1");
	//"-2" is action value for "Previous"
	page = executeResultSet(page, "10", "-2");
	//stay on same page
	page = executeResultSet(page, "10", "10");
	***********************/
    }

    public void testTabbedPane() throws Exception {
        HtmlPage page = accessAppAndGetPage("tabbedpanes.jsf");
	assertNotNull(page);
	//tab 1
	page= executeTabbedPane(page, "_id0:_id2");
	//tab 2
	page= executeTabbedPane(page, "_id0:_id6");
	//tab3 
	page= executeTabbedPane(page, "_id0:_id21");
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
	}
	catch (ElementNotFoundException e) {
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
	}
	catch (ElementNotFoundException e) {
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

	hidden1 = (HtmlHiddenInput) form.getInputByName("_id1:_id2_curPage");
	assertNotNull(hidden1);
	hidden1.setValueAttribute(currentListNum);

	hidden2 = (HtmlHiddenInput) form.getInputByName("_id1:_id2_action");
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
	HtmlInput button = (HtmlInput)form.getInputByName(buttonName);
	page = (HtmlPage)button.click();
	form = (HtmlForm) page.getAllForms().get(0);     
	button = (HtmlInput)form.getInputByName(buttonName);
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
        for (Iterator i = page.getAllHtmlChildElements(); i.hasNext(); ) {
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
    
