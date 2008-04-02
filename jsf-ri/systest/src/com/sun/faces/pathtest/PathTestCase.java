/*
 * $Id: PathTestCase.java,v 1.8 2006/03/29 22:38:48 rlubke Exp $
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

package com.sun.faces.pathtest;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


public class PathTestCase extends AbstractTestCase {

    // ------------------------------------------------------------ Constructors


    public PathTestCase(String name) {

        super(name);

    }

    // ---------------------------------------------------------- Public Methods


    /** Return the tests included in this test suite. */
    public static Test suite() {

        return (new TestSuite(PathTestCase.class));

    }


    /** Set up instance variables required by this test case. */
    public void setUp() throws Exception {

        super.setUp();

    }


    /** Tear down instance variables required by this test case. */
    public void tearDown() {

        super.tearDown();

    }


    /**
     * Verify that if an initial request comes to a FacesSevlet
     * mapped using a prefix path, that the client is redirected
     * to the context root.
     * Additionally verify that after request to a particular view
     * has been made, if a subsequent request is made to the
     * prefix path, context root redirection occurs.
     */
    public void testVerifyPathBehavior() throws Exception {

        final String welcomePage = "WELCOMEPAGE";
        HtmlPage page = getPage("/faces");
        WebResponse response = page.getWebResponse();
        assertTrue(welcomePage.equals(response.getContentAsString().trim()));

        // Ok, now get a page
        HtmlPage textPage = getHtmlPage("/faces/hello.jsp");
        response = textPage.getWebResponse();
        assertTrue("/hello.jsp PASSED".equals(
              response.getContentAsString().trim()));

        page = getPage("/faces");
        response = page.getWebResponse();
        assertTrue("WELCOMEPAGE".equals(response.getContentAsString().trim()));

    }

    // ------------------------------------------------------- Protected Methods


    protected HtmlPage getHtmlPage(String path) throws Exception {

        HtmlPage page = (HtmlPage) client.getPage(getURL(path));

        return (page);

    }

} // end of class PathTestCase

