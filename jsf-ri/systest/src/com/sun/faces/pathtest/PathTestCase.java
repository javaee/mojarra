/*
 * $Id: PathTestCase.java,v 1.10 2007/04/27 22:01:09 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package com.sun.faces.pathtest;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


public class PathTestCase extends AbstractTestCase {

    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public PathTestCase(String name) {
        super(name);
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
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
        return (new TestSuite(PathTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
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


    protected HtmlPage getHtmlPage(String path) throws Exception {
        HtmlPage page = (HtmlPage) client.getPage(getURL(path));

        return (page);

    }


} // end of class PathTestCase

