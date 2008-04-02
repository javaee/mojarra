/*
 * $Id: TestComponents.java,v 1.5 2003/09/16 21:54:40 rkitain Exp $
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
	mapPage = accessAppAndGetPage("faces/imagemap.jsp");
	
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
	HtmlPage page = accessAppAndGetPage("faces/menu.jsp");
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
	HtmlPage page = accessAppAndGetPage("faces/result-set.jsp");
	HtmlAnchor anchor = null;
	assertNotNull(page);
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
	hidden = (HtmlHiddenInput) form.getInputByName("JSPid1:menu" + 
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
	hidden = (HtmlHiddenInput) form.getInputByName("JSPid1:menu" + 
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
	// "File " + treeNum menu choices to disappear, and the "Edit "
	// + treeNum menu choices to appear.
	anchor = page.getFirstAnchorByText("Edit " + treeNum);
	assertNotNull(anchor);
	// simulate link being clicked
	hidden = (HtmlHiddenInput) form.getInputByName("JSPid1:menu" + 
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
    
