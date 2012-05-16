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

package com.sun.faces.systest;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


public class InvalidMappingTestCase extends HtmlUnitFacesTestCase {

    public InvalidMappingTestCase(String name) {
        super(name);
    }

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
        return (new TestSuite(InvalidMappingTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }
    
    
    // ------------------------------------------------------------ Test Methods
    
    public void testInvalidMapping() throws Exception {
        WebClient client = new WebClient();
        client.setThrowExceptionOnFailingStatusCode(false);
        client.setTimeout(0);                
        HtmlPage page = (HtmlPage) getPage("/test.jsp", client);
        // this assert will fail on machines with system locale other then en-us: 
        //assertTrue(page.asText(), page.asText().contains("The FacesServlet cannot have a url-pattern of /*"));

        // therefore test fo language independent parts of the expected message only:
        assertEquals(500, page.getWebResponse().getStatusCode());
        String pageText = page.asText();
        int pos = pageText.indexOf("javax.servlet.ServletException");
        assertTrue(pos > 0);  // pageText contains "javax.servlet.ServletException"
        pos += "javax.servlet.ServletException".length();
        int end = pageText.indexOf("root cause");
        String exceptionMsg = pageText.substring(pos, end);
        // now exceptionMsg must contain the exception message of ServletException, this msg must contain
        // "FacesServlet" once, "url-pattern" twice and "/*" once.
        assertEquals(exceptionMsg, 2, exceptionMsg.split("FacesServlet").length); // split must result in two parts
        assertEquals(exceptionMsg, 3, exceptionMsg.split("url-pattern").length); // split must result in three parts
        assertEquals(exceptionMsg, 2, exceptionMsg.split("/\\*").length); // split must result in three parts
        
    }
}
