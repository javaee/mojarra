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

package com.sun.faces.ajax;

import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import com.sun.faces.render.MessageRenderTestCase;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import junit.framework.Test;
import junit.framework.TestSuite;


public class AjaxMessageTestCase extends HtmlUnitFacesTestCase {

    public AjaxMessageTestCase(String name) {
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
        return (new TestSuite(AjaxMessageTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testCommandButtonButton() throws Exception {
        getPage("/faces/ajax/ajaxMessage.xhtml");


        // Check that ids were rendered
        try {
            lastpage.getHtmlElementById("testform1:msgs");
        } catch (Exception e) {
            fail("testform1:msgs not rendered");
        }
        try {
            lastpage.getHtmlElementById("testform1a:msgs");
        } catch (Exception e) {
            fail("testform1a:msgs not rendered");
        }
        try {
            lastpage.getHtmlElementById("testform2:msg");
        } catch (Exception e) {
            fail("testform2:msg not rendered");
        }

        // check that other ids weren't

        try {
            lastpage.getHtmlElementById("testform3:msgs");
            fail("testform3:msgs rendered - not correct");
        } catch (Exception e) {
            //  Success
        }
        try {
            lastpage.getHtmlElementById("testform3a:msgs");
            fail("testform3:msgs rendered - not correct");
        } catch (Exception e) {
            //  Success
        }
        try {
            lastpage.getHtmlElementById("testform4:msg");
            fail("testform4:msg rendered - not correct");
        } catch (Exception e) {
            //  Success
        }

        // Check initial state
        checkTrue("testform1:in1","0");
        checkTrue("testform1a:in1","0");
        checkTrue("testform2:in1","0");
        checkTrue("testform3:in1","0");
        checkTrue("testform3a:in1","0");
        checkTrue("testform4:in1","0");


        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("testform1:in1");

        in1.type("1");

        // Submit the ajax request
        HtmlSubmitInput button2 = (HtmlSubmitInput) lastpage.getHtmlElementById("testform1:button2");
        lastpage = (HtmlPage) button2.click();

        // Check that the ajax request succeeds
        checkTrue("testform1:in1","1");

        // And that others weren't effected
        checkTrue("testform1a:in1","0");
        checkTrue("testform2:in1","0");
        checkTrue("testform3:in1","0");
        checkTrue("testform3a:in1","0");
        checkTrue("testform4:in1","0");


        in1 = (HtmlTextInput) lastpage.getHtmlElementById("testform1:in1");

        in1.type("a");

        // Submit the ajax request
        button2 = (HtmlSubmitInput) lastpage.getHtmlElementById("testform1:button2");
        lastpage = (HtmlPage) button2.click();

        HtmlUnorderedList ul = (HtmlUnorderedList) lastpage.getHtmlElementById("testform1:msgs");
        DomNode node = ul.getFirstChild();
        System.out.println(node.asText());
        assertTrue("not equal to: testform1:in1: '1a' must be a number consisting of one or more digits. ",
                node.asText().trim().equals("testform1:in1: '1a' must be a number consisting of one or more digits."));


        checkTrue("testform1a:in1","0");
        checkTrue("testform2:in1","0");
        checkTrue("testform3:in1","0");
        checkTrue("testform3a:in1","0");
        checkTrue("testform4:in1","0");

        // RELEASE_PENDING
        // FINISH WRITING TESTS FOR OTHER 5 TEST CASES

        
    }
}
