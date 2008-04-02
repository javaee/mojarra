/*
 * $Id: CommandLinkBackButtonTestCase.java,v 1.5 2006/03/29 22:39:14 rlubke Exp $
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

package com.sun.faces.systest;


import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Make sure no extra baggage from a previously clicked commandLink
 * is submitted along with a button press.</p>
 */

public class CommandLinkBackButtonTestCase extends AbstractTestCase {

    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public CommandLinkBackButtonTestCase(String name) {

        super(name);

    }

    // ---------------------------------------------------------- Public Methods


    /** Return the tests included in this test suite. */
    public static Test suite() {

        return (new TestSuite(CommandLinkBackButtonTestCase.class));

    }

    // ------------------------------------------------------ Instance Variables

    // ---------------------------------------------------- Overall Test Methods


    /** Set up instance variables required by this test case. */
    public void setUp() throws Exception {

        super.setUp();

    }


    /** Tear down instance variables required by this test case. */
    public void tearDown() {

        super.tearDown();

    }


    public void testInputFieldUpdate() throws Exception {

        HtmlPage page = getPage("/faces/test.jsp");
        List list;

        HtmlAnchor link = null;
        list = getAllElementsOfGivenClass(page, null,
                                          HtmlAnchor.class);
        link = (HtmlAnchor) list.get(0);

        // due to a bug in HtmlUnit 1.2.3, we can't just click the link:
        // page = (HtmlPage) link.click();
        // therefore, we have to hack around it.
        list = page.getForms();
        HtmlHiddenInput hidden = (HtmlHiddenInput) ((HtmlForm) list.get(0))
              .getInputByName("form:j_idcl");
        assertTrue(hidden != null);
        hidden.setValueAttribute("form:link");
        page = (HtmlPage) ((HtmlForm) list.get(0)).submit();

        // make sure the page contains evidence that the hidden field
        // *did* receive a value.
        // PENDING (visvan) this doesn't work due to bug in HTMLUnit 1.2.3
        // assertTrue(-1 != page.asText().indexOf("form:link"));

        page = getPage("/faces/test.jsp");
        HtmlSubmitInput button = null;
        list = getAllElementsOfGivenClass(page, null,
                                          HtmlSubmitInput.class);
        button = (HtmlSubmitInput) list.get(0);
        page = (HtmlPage) button.click();

        // make sure the page contains evidence that the hidden field
        // *did not* receive a value.
        assertTrue(-1 == page.asText().indexOf("form:link"));

    }

}

