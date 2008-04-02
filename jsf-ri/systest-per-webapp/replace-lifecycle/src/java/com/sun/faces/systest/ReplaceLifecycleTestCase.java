/*
 * $Id: ReplaceLifecycleTestCase.java,v 1.4 2006/03/29 22:39:23 rlubke Exp $
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


import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Replace the LifecycleFactory with an impl that knows about a new
 * LifecycleId that is passed to the app as a ServletContext init
 * parameter.  Make sure that a phaseListener still gets called with a
 * replaced lifecycle id.</p>
 */

public class ReplaceLifecycleTestCase extends AbstractTestCase {

    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ReplaceLifecycleTestCase(String name) {

        super(name);

    }

    // ---------------------------------------------------------- Public Methods


    /** Return the tests included in this test suite. */
    public static Test suite() {

        return (new TestSuite(ReplaceLifecycleTestCase.class));

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


    public void testAlternateLifecycle() throws Exception {

        HtmlPage page = getPage("/alternate/test2.jsp");
        assertTrue(-1 != page.asText().indexOf("beforePhase"));
        assertTrue(-1 != page.asText().indexOf("AlternateLifecycle"));
        page = getPage("/faces/test2.jsp");
        assertTrue(-1 != page.asText().indexOf("beforePhase"));
        assertTrue(-1 != page.asText().indexOf("NewLifecycle"));

    }

    // ------------------------------------------------------ Instance Variables

    // ------------------------------------------------- Individual Test Methods

    /** <p>Verify that the bean is successfully resolved</p> */

    public void testReplaceLifecycle() throws Exception {

        HtmlPage page = getPage("/faces/test.jsp");
        assertTrue(-1 != page.asText().indexOf("beforePhase"));

    }

}
