/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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


package com.sun.faces.ajax;

import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.List;


public class AjaxErrorTestCase extends AbstractTestCase {


     public AjaxErrorTestCase(String name) {
        super(name);
    }

    /*
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        super.setUp();
    }


    /*
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(AjaxErrorTestCase.class));
    }


    /*
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------------ Test Methods


    public void testAjaxError() throws Exception {

        List<String> collectedAlerts = new ArrayList<String>(1);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        HtmlPage page = getPage("/faces/ajax/ajaxError2.xhtml");

        HtmlSubmitInput button = (HtmlSubmitInput)
              getInputContainingGivenId(page, "form:error");
        assertNotNull(button);

        button.click();

        assertEquals(1, collectedAlerts.size());
        assertEquals("serverError: errorName Error Message", collectedAlerts.get(0));
        
    }


    public void testAjaxServerError() throws Exception {

        List<String> collectedAlerts = new ArrayList<String>(1);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        HtmlPage page = getPage("/faces/ajax/ajaxError.xhtml");

        HtmlSubmitInput button = (HtmlSubmitInput)
              getInputContainingGivenId(page, "form:eval");
        assertNotNull(button);

        button.click();

        assertEquals(1, collectedAlerts.size());
        String serverError = "serverError: class javax.faces.el.MethodNotFoundException /ajax/ajaxError.xhtml @52,76 action=\"#{evalBean.error}\": Method not found:";
        assertEquals(serverError.substring(0,130), collectedAlerts.get(0).substring(0,130));

        page = getPage("/faces/ajax/ajaxError3.xhtml");
        button = (HtmlSubmitInput) getInputContainingGivenId(page, "form:error");
        assertNotNull(button);

        HtmlPage page1 = button.click();
        HtmlElement element = page1.getElementById("statusArea");
        assertNotNull(element);
        String statusText = element.getAttribute("value");
        assertTrue(statusText.equals("Name: form:error Error: serverError "));

    }

    public void testAjaxMalformedXMLError() throws Exception {

        List<String> collectedAlerts = new ArrayList<String>(1);
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        HtmlPage page = getPage("/faces/ajax/ajaxMalformedXML.xhtml");

        HtmlSubmitInput button = (HtmlSubmitInput)
              getInputContainingGivenId(page, "form:error");
        assertNotNull(button);

        button.click();

        assertEquals(1, collectedAlerts.size());
        assertEquals("malformedXML: During update: doesntExist not found", collectedAlerts.get(0));

}

}
