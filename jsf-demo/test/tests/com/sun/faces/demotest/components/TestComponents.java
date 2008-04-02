/*
 * $Id: TestComponents.java,v 1.1 2003/09/02 21:33:08 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.demotest.components;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlMap;
import com.gargoylesoftware.htmlunit.html.HtmlArea;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.ScriptResult;
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

        HtmlPage mapPage = null;
	HtmlForm form = null;
	HtmlMap map = null;
	HtmlArea area = null;
	String onClick = null;
	ScriptResult result = null;

	mapPage = accessAppAndGetImageMapPage();
	for (int i = 0, len = welcomeTexts.length; i < len; i++) {
	    form = (HtmlForm) mapPage.getAllForms().get(0);
	    map = (HtmlMap) form.getChildElements().get(0);
	    area = (HtmlArea) map.getChildElements().get(i);
	    onClick = area.getOnClickAttribute();
	    result = mapPage.executeJavaScriptIfPossible(onClick,
							 onClick,
							 false, area);
	    mapPage = (HtmlPage) result.getNewPage();
	    assertTrue(-1 != getImageMapWelcomeText(mapPage).indexOf(welcomeTexts[i]));
	}

    }
    
    private HtmlPage accessAppAndGetImageMapPage() throws Exception {
        
        HtmlPage page = (HtmlPage) getInitialPage();
	HtmlAnchor imagemapAnchor = page.getAnchorByHref("faces/imagemap.jsp");
	assertTrue(null != imagemapAnchor);
	page = (HtmlPage) imagemapAnchor.click();
	assertTrue(null != page);
        
        return page;
    }

    private String getImageMapWelcomeText(HtmlPage page) {
	String result = null;
	// <html>
	HtmlElement element = (HtmlElement) page.getChildElements().get(0);
	// <body>
	element = (HtmlElement) element.getChildElements().get(1);
	// <table>
	HtmlTable table = (HtmlTable) element.getChildElements().get(0);
	result = table.getCellAt(0, 0).asText().trim();
	return result;
    }

	

} // end of class DemoTest01
    
