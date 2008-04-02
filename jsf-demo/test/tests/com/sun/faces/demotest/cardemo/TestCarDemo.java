/*
 * $Id: TestCarDemo.java,v 1.2 2004/01/28 21:45:36 eburns Exp $
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
	HtmlAnchor anchor = null;
	List anchors = page.getAnchors();
	int i = 0;
	Locale [] locales = {
	    Locale.ENGLISH,
	    Locale.GERMAN,
	    Locale.FRENCH,
	    new Locale("es", "")
	};
	String [] links = {
	    "/storeFront.jsf?_id4:NAmerica=_id4:NAmerica",
	    "/storeFront.jsf?_id4:Germany=_id4:Germany",
	    "/storeFront.jsf?_id4:France=_id4:France",
	    "/storeFront.jsf?_id4:SAmerica=_id4:SAmerica",
	};

	for (i = 0; i < locales.length; i++) {
	    resources = ResourceBundle.getBundle("carstore.bundles.Resources",
						 locales[i]);
	    anchor = (HtmlAnchor) anchors.get(i);
	    if (log.isTraceEnabled()) {
		log.trace("Running test for language: " + anchor.asText());
	    }
	    doStoreFront(getPage(links[i]));
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

	List inputs = getAllHtmlInputElements(form, null);
	iter = inputs.iterator();
	moreButton = resources.getString("moreButton");
	while (iter.hasNext()) {
	    input = (HtmlInput) iter.next();
	    if (input instanceof HtmlSubmitInput) {
		// assertEquals(moreButton, input.asText());
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
    
