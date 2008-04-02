/*
 * $Id: TestCarDemo.java,v 1.3 2004/01/29 15:51:23 eburns Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestCarDemo extends HtmlUnitTestCase {

    // Log instance for this class
    private static final Log log = LogFactory.getLog(TestCarDemo.class);

    public TestCarDemo() {
    }

    protected ResourceBundle resources = null;


    // PENDING: find a way to cause the WebClient's Accept_Charset
    // header to be set so we can test the locale calculation algorithm.

    public void testCarDemo() throws Exception {

	// for each of the language links run the test
	HtmlPage page = (HtmlPage) getInitialPage();
	List buttons = getAllElementsOfGivenClass(page, null, 
						  HtmlSubmitInput.class);
	HtmlSubmitInput button = null;
	int i = 0;
	Locale [] locales = {
	    Locale.ENGLISH,
	    Locale.GERMAN,
	    Locale.FRENCH,
	    new Locale("es", "")
	};

	for (i = 0; i < locales.length; i++) {
	    resources = ResourceBundle.getBundle("carstore.bundles.Resources",
						 locales[i]);
	    button = (HtmlSubmitInput) buttons.get(i);
	    if (log.isTraceEnabled()) {
		log.trace("Running test for language: " + button.asText());
	    }
	    doStoreFront((HtmlPage) button.click());
	}
    }
    
    public void doStoreFront(HtmlPage storeFront) throws Exception {
	HtmlForm form = null;
	HtmlInput input = null;
	String moreButton = null;
	Iterator iter = null;

	assertNotNull(storeFront);
	// check that there are four buttons
	List forms = storeFront.getAllForms();
	assertEquals(1, forms.size());
	
	form = (HtmlForm) forms.get(0);

	List inputs = getAllElementsOfGivenClass(form, null, HtmlInput.class);
	iter = inputs.iterator();
	moreButton = resources.getString("moreButton");
	while (iter.hasNext()) {
	    input = (HtmlInput) iter.next();
	    if (input instanceof HtmlSubmitInput) {
		assertTrue(-1 != 
			   input.asText().trim().indexOf(moreButton.trim()));
		if (log.isTraceEnabled()) {
		    log.trace("Button text of " + moreButton + " confirmed.");
		}
		doCarDetail((HtmlPage) input.click());
	    }
	}
	
    }

    public void doCarDetail(HtmlPage carDetail) {

	assertNotNull(carDetail);
    }
    
} // end of class DemoTest01
    
