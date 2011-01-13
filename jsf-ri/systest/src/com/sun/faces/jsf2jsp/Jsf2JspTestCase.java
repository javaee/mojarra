/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Sun Microsystems, Inc. All rights reserved.
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


package com.sun.faces.jsf2jsp;


import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.jsptest.ValidatorTestCase;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class Jsf2JspTestCase extends AbstractTestCase {


    public Jsf2JspTestCase() {
        this("Jsf2Js2TestCase");
    }


    public Jsf2JspTestCase(String name) {
        super(name);
    }

    public static Test suite() {
        return (new TestSuite(Jsf2JspTestCase.class));
    }


    // ------------------------------------------------------------ Test Methods


    public void testUnsupportedFeaturesAreUnsupported() throws Exception {

        // These features are not implemented in JSP
        assert500Response("/faces/jsf2jsp/head-gives-500.jspx");
        assert500Response("/faces/jsf2jsp/body-gives-500.jspx");
        assert500Response("/faces/jsf2jsp/outputScript-gives-500.jspx");
        assert500Response("/faces/jsf2jsp/outputStylesheet-gives-500.jspx");
        assert500Response("/faces/jsf2jsp/button-gives-500.jspx");
        assert500Response("/faces/jsf2jsp/link-gives-500.jspx");
        assert500Response("/faces/jsf2jsp/resource-ELResolver-gives-500.jspx");
        assert500Response("/faces/jsf2jsp/ajax-gives-500.jspx");
        assert500Response("/faces/jsf2jsp/event-gives-500.jspx");
        assert500Response("/faces/jsf2jsp/metadata-gives-500.jspx");

    }

    public void testSupportedFeaturesAreSupported() throws Exception {

        // These features are implemented in JSP
        HtmlPage page = getPage("/faces/jsf2jsp/commandButton-parameter-children-gives-hidden-fields.jspx");
        HtmlSubmitInput button = (HtmlSubmitInput) page.getElementById("reload");
        page = button.click();
        String text = page.asText();
        assertTrue(text.contains("name01=value01"));
        assertTrue(text.contains("name02=value02"));


        page = getPage("/faces/jsf2jsp/resources.jspx");
        text = page.asXml();
        assertTrue(text.contains("duke.gif"));
        assertTrue(text.contains("vLibrary"));
        assertTrue(text.contains("2_01_1"));

        assert200Response("/faces/jsf2jsp/selectManyJsf2Features.jspx");

        Test validatorTest = ValidatorTestCase.suite();
        TestResult validatorResult = new TestResult();
        validatorTest.run(validatorResult);
        assertTrue(validatorResult.failureCount() == 0);



    }
    
    private void assert500Response(String urlFragment) throws Exception {
        client.setThrowExceptionOnFailingStatusCode(true);
        HtmlPage page = null;
        int code;
        try {
            page = getPage(urlFragment);
        } catch (FailingHttpStatusCodeException fail) {
            code = fail.getStatusCode();
            assertTrue("GET " + urlFragment + " Expected 500, got: "+code, code==500);
        }
        
    }

    private void assert200Response(String urlFragment) throws Exception {
        client.setThrowExceptionOnFailingStatusCode(true);
        HtmlPage page = null;
        int code;
        try {
            page = getPage(urlFragment);
        } catch (FailingHttpStatusCodeException fail) {
            code = fail.getStatusCode();
            assertTrue("GET " + urlFragment + " Expected 200, got: "+code, code==200);
        }

    }

}
