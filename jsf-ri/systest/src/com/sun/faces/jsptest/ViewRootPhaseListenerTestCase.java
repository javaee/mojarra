/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.jsptest;


import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.List;


/**
 * <p>Make sure that only unique view ids are saved in the session</p>
 */

public class ViewRootPhaseListenerTestCase extends HtmlUnitFacesTestCase {

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

}

