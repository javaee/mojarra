/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.facelets;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test cases for Facelets functionality
 */
public class FaceletsTargetNotFoundTestCase extends AbstractTestCase {


    // --------------------------------------------------------------- Test Init


    public FaceletsTargetNotFoundTestCase() {
        this("FaceletsTargetNotFoundTestCase");
    }


    public FaceletsTargetNotFoundTestCase(String name) {
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
        return (new TestSuite(FaceletsTargetNotFoundTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------------ Test Methods

    /*
     * Check that not found errors give a 500 status, instead of the 404 that
     * they used to give.
     */
    public void testFaillingCode() throws Exception {

        WebClient client = new WebClient();
        client.setThrowExceptionOnFailingStatusCode(true);
        HtmlPage page;
        int code;

        try {
            page = getPage("/faces/facelets/compositionnotfound.xhtml", client) ;
            fail("A Failing status code was not received");
        } catch (FailingHttpStatusCodeException fail) {
            code = fail.getStatusCode();
            assertTrue("Expected 500, got: "+code, code==500);
        }
        try {
            page = getPage("/faces/facelets/decoratenotfound.xhtml", client) ;
            fail("A Failing status code was not received");
        } catch (FailingHttpStatusCodeException fail) {
            code = fail.getStatusCode();
            assertTrue("Expected 500, got: "+code, code==500);
        }
        try {
            page = getPage("/faces/facelets/includenotfound.xhtml", client) ;
            fail("A Failing status code was not received");
        } catch (FailingHttpStatusCodeException fail) {
            code = fail.getStatusCode();
            assertTrue("Expected 500, got: "+code, code==500);
        }

    }

}