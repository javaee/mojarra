/*
 * $Id: TestCarDemo.java,v 1.4 2004/01/29 16:38:11 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.demotest.cardemo;

import com.gargoylesoftware.htmlunit.html.*;
import com.sun.faces.demotest.HtmlUnitTestCase;

import javax.faces.component.NamingContainer;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Assumptions: the app is localized for four locales, English,
 * German, French, Spanish.</p>
 *
 */

public class TestCarDemo extends HtmlUnitTestCase {

    // Log instance for this class
    private static final Log log = LogFactory.getLog(TestCarDemo.class);

    public TestCarDemo() {
    }

    protected ResourceBundle resources = null;
    protected String [] carBundleNames = {
	"carstore.bundles.Jalopy",
	"carstore.bundles.Luxury",
	"carstore.bundles.Roadster",
	"carstore.bundles.SUV"
    };
    protected ResourceBundle [] carBundles = null;



    // PENDING: find a way to cause the WebClient's Accept_Charset
    // header to be set so we can test the locale calculation algorithm.

    /**
     * <p>Load the main page.  Assumptions: there are exactly four
     * buttons, in a certain order, to select each locale.  For each
     * button, press it, and call doStoreFront() on the result.</p>
     *
     */

    public void testCarDemo() throws Exception {

	// for each of the language links run the test
	HtmlPage page = (HtmlPage) getInitialPage();
	List buttons = getAllElementsOfGivenClass(page, null, 
						  HtmlSubmitInput.class);
	HtmlSubmitInput button = null;
	int i, j = 0;
	Locale [] locales = {
	    Locale.ENGLISH,
	    Locale.GERMAN,
	    Locale.FRENCH,
	    new Locale("es", "")
	};

	for (i = 0; i < locales.length; i++) {
	    resources = ResourceBundle.getBundle("carstore.bundles.Resources",
						 locales[i]);
	    carBundles = 
		new ResourceBundle[carBundleNames.length];
	    for (j = 0; j < carBundleNames.length; j++) {
		carBundles[j] = 
		    ResourceBundle.getBundle(carBundleNames[j], locales[i]);
	    }
	    
	    button = (HtmlSubmitInput) buttons.get(i);
	    if (log.isTraceEnabled()) {
		log.trace("Running test for language: " + button.asText());
	    }
	    doStoreFront((HtmlPage) button.click());
	}
    }

    /**
     *
     * <p>Assumptions: there are exactly four buttons on this page, one
     * for each car model.</p>
     * 
     * <p>Verify that all of the expected cars have their descriptions
     * on the page.</p>
     * 
     *
     * <p>Verify that the text of the "more" button is properly
     * localized.</p>
     *
     *
     */ 
    
    public void doStoreFront(HtmlPage storeFront) throws Exception {
	HtmlSubmitInput button = null;
	HtmlTableDataCell cell = null;
	String 
	    description = null,
	    moreButton = null;
	Iterator iter = null;
	boolean found = false;
	int i;

	assertNotNull(storeFront);

	List
	    cells = getAllElementsOfGivenClass(storeFront, null, 
					       HtmlTableDataCell.class),
	    buttons = getAllElementsOfGivenClass(storeFront, null, 
						 HtmlSubmitInput.class);

	// verify the expected descriptions are present

	for (i = 0; i < carBundles.length; i++) {
	    iter = cells.iterator();
	    description = carBundles[i].getString("description");
	    while (iter.hasNext()) {
		cell = (HtmlTableDataCell) iter.next();
		if (-1 != cell.asText().trim().indexOf(description)) {
		    if (log.isTraceEnabled()) {
			log.trace("Found description " + description + ".");
		    }
		    found = true;
		    break;
		}
	    }
	}
	assertTrue("Did not find description: " + description, found);

	iter = buttons.iterator();
	moreButton = resources.getString("moreButton");
	while (iter.hasNext()) {
	    button = (HtmlSubmitInput) iter.next();
	    assertTrue(-1 != 
		       button.asText().trim().indexOf(moreButton.trim()));
	    if (log.isTraceEnabled()) {
		log.trace("Button text of " + moreButton + " confirmed.");
	    }
	    doCarDetail((HtmlPage) button.click());
	}
	
    }

    public void doCarDetail(HtmlPage carDetail) {

	assertNotNull(carDetail);
    }
    
} // end of class DemoTest01
    
