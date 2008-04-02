/*
 * $Id: TestComponents.java,v 1.2 2003/09/05 21:28:11 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.demotest.components;

import com.gargoylesoftware.htmlunit.html.*;
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
	mapPage = accessAppAndGetImageMapPage();
  
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
    
