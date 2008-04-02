/*
 * $Id: ViewRootPhaseListenerTestCase.java,v 1.4 2006/03/29 22:38:47 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.jsptest;


import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/** <p>Make sure that only unique view ids are saved in the session</p> */

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

    // ---------------------------------------------------------- Public Methods


    /** Return the tests included in this test suite. */
    public static Test suite() {

        return (new TestSuite(ViewRootPhaseListenerTestCase.class));

    }


    public void doTestListenerTagListeners(HtmlPage page) throws Exception {

        assertTrue(-1 != page.asText()
              .indexOf("beforePhaseEvent: beforePhase: RENDER_RESPONSE 6."));
        assertTrue(-1 != page.asText().indexOf("afterPhaseEvent: ."));

        List list;

        HtmlSubmitInput button = null;
        list = getAllElementsOfGivenClass(page, null,
                                          HtmlSubmitInput.class);
        button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        assertTrue(-1 != page.asText().indexOf(
              "beforePhaseEvent: beforePhase: APPLY_REQUEST_VALUES 2 beforePhase: PROCESS_VALIDATIONS 3 beforePhase: UPDATE_MODEL_VALUES 4 beforePhase: INVOKE_APPLICATION 5 beforePhase: RENDER_RESPONSE 6."));
        assertTrue(-1 != page.asText().indexOf(
              "afterPhaseEvent: afterPhase: APPLY_REQUEST_VALUES 2 afterPhase: PROCESS_VALIDATIONS 3 afterPhase: UPDATE_MODEL_VALUES 4 afterPhase: INVOKE_APPLICATION 5."));

    }


    /** Set up instance variables required by this test case. */
    public void setUp() throws Exception {

        super.setUp();

    }


    /** Tear down instance variables required by this test case. */
    public void tearDown() {

        super.tearDown();

    }


    public void testListenerTagListenersBinding() throws Exception {

        HtmlPage page = getPage("/faces/listenerTagListenersBinding.jsp");
        doTestListenerTagListeners(page);

    }


    public void testListenerTagListenersBindingType() throws Exception {

        HtmlPage page = getPage("/faces/listenerTagListenersBindingType.jsp");
        doTestListenerTagListeners(page);

    }


    public void testListenerTagListenersType() throws Exception {

        HtmlPage page = getPage("/faces/listenerTagListenersType.jsp");
        doTestListenerTagListeners(page);

    }

    // ------------------------------------------------------ Instance Variables

    // ------------------------------------------------- Individual Test Methods

    public void testViewTagListeners() throws Exception {

        HtmlPage page = getPage("/faces/viewTagListeners.jsp");

        assertTrue(-1 != page.asText()
              .indexOf("beforePhaseEvent: beforePhase: RENDER_RESPONSE 6."));
        assertTrue(-1 != page.asText().indexOf("afterPhaseEvent: ."));

        List list;

        HtmlSubmitInput button = null;
        list = getAllElementsOfGivenClass(page, null,
                                          HtmlSubmitInput.class);
        button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        assertTrue(-1 != page.asText().indexOf(
              "beforePhaseEvent: beforePhase: APPLY_REQUEST_VALUES 2 beforePhase: PROCESS_VALIDATIONS 3 beforePhase: UPDATE_MODEL_VALUES 4 beforePhase: INVOKE_APPLICATION 5 beforePhase: RENDER_RESPONSE 6."));
        assertTrue(-1 != page.asText().indexOf(
              "afterPhaseEvent: afterPhase: APPLY_REQUEST_VALUES 2 afterPhase: PROCESS_VALIDATIONS 3 afterPhase: UPDATE_MODEL_VALUES 4 afterPhase: INVOKE_APPLICATION 5."));

    }

}

