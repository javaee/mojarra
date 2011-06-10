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

package com.sun.faces.systest.implicitnav;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class ImplicitNavigationTestCase extends HtmlUnitFacesTestCase {


    public ImplicitNavigationTestCase(String name) {
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
           return (new TestSuite(ImplicitNavigationTestCase.class));
       }


       /**
        * Tear down instance variables required by this test case.
        */
       public void tearDown() {
           super.tearDown();
       }


       // ------------------------------------------------------------ Test Methods

       public void testImplicitNavigation() throws Exception {

           HtmlPage page = getPage("/faces/implicitnav/page01.xhtml");

           // Assert that the navCase getters work correctly
           String text = page.asText();
           assertTrue(-1 != text.indexOf("/jsf-systest/faces/implicitnav/page01.xhtml"));
           assertTrue(-1 != text.indexOf("/jsf-systest/implicitnav/page01.xhtml"));
           // Make sure we're on the expected page
           assertTrue(-1 != text.indexOf("[page01]"));

           // click the page02 button
           HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("page02");
           page = button.click();
           text = page.asText();
           // Make sure we're on the expected page
           assertTrue(-1 != text.indexOf("[page02]"));

           // click the page01 button to go back to page01
           button = (HtmlSubmitInput) page.getElementById("page01");
           page = button.click();
           text = page.asText();
           // Make sure we're on the expected page
           assertTrue(-1 != text.indexOf("[page01]"));

           // go back again to page02
           button = (HtmlSubmitInput) page.getElementById("page02");
           page = button.click();
           text = page.asText();
           // Make sure we're on the expected page
           assertTrue(-1 != text.indexOf("[page02]"));

           // go forward to page03
           button = (HtmlSubmitInput) page.getElementById("page03");
           page = button.click();
           text = page.asText();
           // Make sure we're on the expected page
           assertTrue(-1 != text.indexOf("[page03]"));

           // click the page02 button to go back to page02
           button = (HtmlSubmitInput) page.getElementById("page02");
           page = button.click();
           text = page.asText();
           // Make sure we're on the expected page
           assertTrue(-1 != text.indexOf("[page02]"));

           // go forward to page03
           button = (HtmlSubmitInput) page.getElementById("page03");
           page = button.click();
           text = page.asText();
           // Make sure we're on the expected page
           assertTrue(-1 != text.indexOf("[page03]"));

           // go forward to page04.  This uses a redirect
           button = (HtmlSubmitInput) page.getElementById("page04");
           client.setRedirectEnabled(false);
           boolean exceptionThrown = false;
           try {
               page = button.click();
           } catch (FailingHttpStatusCodeException fhsce) {
               exceptionThrown = true;
               assertEquals(302, fhsce.getStatusCode());
           }
           assertTrue(exceptionThrown);
           client.setRedirectEnabled(true);
           page = button.click();
           text = page.asText();
           // Make sure we're on the expected page
           assertTrue(-1 != text.indexOf("This is the last page."));




       }


       public void testImplicitNavigationWithredirect() throws Exception {

           HtmlPage page = getPage("/faces/implicitnav/implicitNavRedirect.xhtml");
           String text;
           final String SEARCH_TEXT = "SEARCH_TEXT";

           // case 1, h:button, make sure the input value is lost.
           HtmlTextInput input = (HtmlTextInput) page.getElementById("input");
           input.setValueAttribute(SEARCH_TEXT);
           HtmlButtonInput buttonButton = (HtmlButtonInput) page.getElementById("httpGet");

           page = buttonButton.click();
           text = page.asText();;
           assertTrue(!text.contains(SEARCH_TEXT));

           // case 2 h:commandButton that does redirect.  Make sure a redirect is
           // performed and the value is lost.
           client.setRedirectEnabled(false);
           boolean exceptionThrown = false;
           page = getPage("/faces/implicitnav/implicitNavRedirect.xhtml");
           input = (HtmlTextInput) page.getElementById("input");
           input.setValueAttribute(SEARCH_TEXT);
           HtmlSubmitInput submitButton = (HtmlSubmitInput) page.getElementById("httpPostRedirect");
           try {
               page = submitButton.click();
           } catch (FailingHttpStatusCodeException e) {
               assertEquals(302, e.getStatusCode());
               exceptionThrown = true;
           }
           assertTrue(exceptionThrown);
           client.setRedirectEnabled(true);
           page = submitButton.click();
           text = page.asText();;
           assertTrue(!text.contains(SEARCH_TEXT));

           // case 3 h:commandButton with empty query string
           page = getPage("/faces/implicitnav/implicitNavRedirect.xhtml");
           input = (HtmlTextInput) page.getElementById("input");
           input.setValueAttribute(SEARCH_TEXT);
           submitButton = (HtmlSubmitInput) page.getElementById("httpPostInvalidQueryString");
           page = submitButton.click();
           text = page.asText();
           assertTrue(text.contains(SEARCH_TEXT));
           assertTrue(text.contains("Invalid query string in outcome \'implicitNavRedirect02?\'"));

           // case 4: h:commandButton, regular post.
           page = getPage("/faces/implicitnav/implicitNavRedirect.xhtml");
           input = (HtmlTextInput) page.getElementById("input");
           input.setValueAttribute(SEARCH_TEXT);
           submitButton = (HtmlSubmitInput) page.getElementById("httpPost");
           page = submitButton.click();
           text = page.asText();;
           assertTrue(text.contains(SEARCH_TEXT));

       }

       public void testImplicitNavEmptyString() throws Exception {
           HtmlPage page = getPage("/faces/implicitnav/implicitNavEmptyString.xhtml");
           HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("button");
           page = button.click();
           assertTrue(page.asText().contains("Implicit Navigation with empty string action"));


       }

}
