/*
 * $Id: ViewRootPhaseListenerTestCase.java,v 1.1 2004/12/08 15:10:26 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.jsptest;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.faces.component.NamingContainer;


/**
 * <p>Make sure that only unique view ids are saved in the session</p>
 */

public class ViewRootPhaseListenerTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ViewRootPhaseListenerTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


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
        return (new TestSuite(ViewRootPhaseListenerTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------ Instance Variables



    // ------------------------------------------------- Individual Test Methods

    public void testViewTagListeners() throws Exception {
	HtmlPage page = getPage("/faces/viewTagListeners.jsp");
	
	assertTrue(-1 != page.asText().indexOf("beforePhaseEvent: beforePhase: RENDER_RESPONSE 6."));
	assertTrue(-1 != page.asText().indexOf("afterPhaseEvent: ."));

	List list;

	HtmlSubmitInput button = null;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();

	assertTrue(-1 != page.asText().indexOf("beforePhaseEvent: beforePhase: APPLY_REQUEST_VALUES 2 beforePhase: PROCESS_VALIDATIONS 3 beforePhase: UPDATE_MODEL_VALUES 4 beforePhase: INVOKE_APPLICATION 5 beforePhase: RENDER_RESPONSE 6."));
	assertTrue(-1 != page.asText().indexOf("afterPhaseEvent: afterPhase: APPLY_REQUEST_VALUES 2 afterPhase: PROCESS_VALIDATIONS 3 afterPhase: UPDATE_MODEL_VALUES 4 afterPhase: INVOKE_APPLICATION 5."));


    }

    public void testListenerTagListenersType() throws Exception {
	HtmlPage page = getPage("/faces/listenerTagListenersType.jsp");
	doTestListenerTagListeners(page);
    }

    public void testListenerTagListenersBinding() throws Exception {
	HtmlPage page = getPage("/faces/listenerTagListenersBinding.jsp");
	doTestListenerTagListeners(page);
    }

    public void testListenerTagListenersBindingType() throws Exception {
	HtmlPage page = getPage("/faces/listenerTagListenersBindingType.jsp");
	doTestListenerTagListeners(page);
    }

    public void doTestListenerTagListeners(HtmlPage page) throws Exception {
	
	assertTrue(-1 != page.asText().indexOf("beforePhaseEvent: ."));
	assertTrue(-1 != page.asText().indexOf("afterPhaseEvent: ."));

	List list;

	HtmlSubmitInput button = null;
	list = getAllElementsOfGivenClass(page, null, 
					  HtmlSubmitInput.class); 
	button = (HtmlSubmitInput) list.get(0);
	page = (HtmlPage) button.click();

	assertTrue(-1 != page.asText().indexOf("beforePhaseEvent: beforePhase: APPLY_REQUEST_VALUES 2 beforePhase: PROCESS_VALIDATIONS 3 beforePhase: UPDATE_MODEL_VALUES 4 beforePhase: INVOKE_APPLICATION 5 beforePhase: RENDER_RESPONSE 6."));
	assertTrue(-1 != page.asText().indexOf("afterPhaseEvent: afterPhase: APPLY_REQUEST_VALUES 2 afterPhase: PROCESS_VALIDATIONS 3 afterPhase: UPDATE_MODEL_VALUES 4 afterPhase: INVOKE_APPLICATION 5."));


    }
    
}

