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

package com.sun.faces.systest.lifecycle;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.HtmlUnitFacesTestCase;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Test Case for JSP Interoperability.</p>
 */

public class ManagedBeanLifecycleAnnotationTestCase extends HtmlUnitFacesTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ManagedBeanLifecycleAnnotationTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


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
        return (new TestSuite(ManagedBeanLifecycleAnnotationTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    public void testRequestLifecycle() throws Exception {
        String text = null;
        HtmlPage page = getPage("/faces/managed08.jsp");
        HtmlSubmitInput button = (HtmlSubmitInput) 
            page.getHtmlElementById("form:clearStatusMessage");
        page = (HtmlPage) button.click();
        text = page.asText();
        Pattern pattern = null;
        assertTrue(-1 != text.indexOf("requestBean PostConstruct: true"));
        assertTrue(-1 != text.indexOf("requestBean PreDestroy: false"));
        assertTrue(-1 != text.indexOf("sessionBean PostConstruct: true"));
        assertTrue(-1 != text.indexOf("sessionBean PreDestroy: false"));
        assertTrue(-1 != text.indexOf("applicationBean PostConstruct: true"));
        assertTrue(-1 != text.indexOf("applicationBean PreDestroy: false"));
        assertTrue(Pattern.matches("(?s).*-----------------.*bean: requestBean postConstructCalled: true.*",
                text));
        
        button = (HtmlSubmitInput) 
            page.getHtmlElementById("form:reload");
        page = (HtmlPage) button.click();
        text = page.asText();
        assertTrue(Pattern.matches("(?s).*-----------------.*bean: requestBean postConstructCalled: true.*bean: requestBean preDestroyCalled: true.*-----------------.*bean: requestBean postConstructCalled: true.*",
                text));
        

        button = (HtmlSubmitInput) 
            page.getHtmlElementById("form:clearStatusMessage");
        page = (HtmlPage) button.click();
        button = (HtmlSubmitInput) 
            page.getHtmlElementById("form:removeSessionBean");
        page = (HtmlPage) button.click();
        text = page.asText();
        assertTrue(Pattern.matches("(?s).*-----------------.*bean: requestBean postConstructCalled: true.*bean: requestBean preDestroyCalled: true.*bean: sessionBean preDestroyCalled: true.*-----------------.*bean: requestBean postConstructCalled: true.*bean: sessionBean postConstructCalled: true.*",
                text));

        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:clearStatusMessage");
        page = (HtmlPage) button.click();
        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:removeSessionBean2");
        page = (HtmlPage) button.click();
        text = page.asText();
        assertTrue(Pattern.matches("(?s).*-----------------.*bean: requestBean postConstructCalled: true.*bean: requestBean preDestroyCalled: true.*bean: sessionBean preDestroyCalled: true.*-----------------.*bean: requestBean postConstructCalled: true.*bean: sessionBean postConstructCalled: true.*",
                text));
        
        button = (HtmlSubmitInput) 
            page.getHtmlElementById("form:clearStatusMessage");
        page = (HtmlPage) button.click();
        button = (HtmlSubmitInput) 
            page.getHtmlElementById("form:removeApplicationBean");
        page = (HtmlPage) button.click();
        text = page.asText();
        assertTrue(Pattern.matches("(?s).*-----------------.*bean: requestBean postConstructCalled: true.*bean: requestBean preDestroyCalled: true.*bean: applicationBean preDestroyCalled: true.*-----------------.*bean: requestBean postConstructCalled: true.*bean: applicationBean postConstructCalled: true.*",
                text));

        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:clearStatusMessage");
        page = (HtmlPage) button.click();
        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:removeApplicationBean2");
        page = (HtmlPage) button.click();
        text = page.asText();
        assertTrue(Pattern.matches("(?s).*-----------------.*bean: requestBean postConstructCalled: true.*bean: requestBean preDestroyCalled: true.*bean: applicationBean preDestroyCalled: true.*-----------------.*bean: requestBean postConstructCalled: true.*bean: applicationBean postConstructCalled: true.*",
                text));

        button = (HtmlSubmitInput) 
            page.getHtmlElementById("form:clearStatusMessage");
        page = (HtmlPage) button.click();
        button = (HtmlSubmitInput) 
            page.getHtmlElementById("form:invalidateSession");
        page = (HtmlPage) button.click();
        text = page.asText();
        assertTrue(Pattern.matches("(?s).*-----------------.*bean: requestBean postConstructCalled: true.*bean: requestBean preDestroyCalled: true.*bean: sessionBean preDestroyCalled: true.*-----------------.*bean: requestBean postConstructCalled: true.*bean: sessionBean postConstructCalled: true.*",
                text));

        button = (HtmlSubmitInput) 
            page.getHtmlElementById("form:clearStatusMessage");
        page = (HtmlPage) button.click();
        button = (HtmlSubmitInput) 
            page.getHtmlElementById("form:clearSessionMapTwice");
        page = (HtmlPage) button.click();
        text = page.asText();
        assertTrue(Pattern.matches("(?s).*-----------------.*bean: requestBean postConstructCalled: true.*bean: requestBean preDestroyCalled: true.*bean: sessionBean preDestroyCalled: true.*-----------------.*bean: requestBean postConstructCalled: true.*bean: sessionBean postConstructCalled: true.*",
                text));

        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:clearStatusMessage");
        page = (HtmlPage) button.click();
        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:replaceRequestBean");
        page = (HtmlPage) button.click();
        text = page.asText();
        assertTrue(Pattern.matches("(?s).*-----------------.*bean: requestBean postConstructCalled: true.*bean: requestBean preDestroyCalled: true.*-----------------.*",
                text));

        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:clearStatusMessage");
        page = (HtmlPage) button.click();
        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:replaceRequestBean2");
        button.click();
        page = (HtmlPage) button.click();
        text = page.asText();
        assertTrue(Pattern.matches("(?s).*-----------------.*bean: requestBean postConstructCalled: true.*bean: requestBean preDestroyCalled: true.*-----------------.*bean: requestBean postConstructCalled: true.*bean: requestBean preDestroyCalled: true.*-----------------.*bean: requestBean postConstructCalled: true.*",
                text));

        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:removeSessionBean");
        page = (HtmlPage) button.click();
         button = (HtmlSubmitInput)
            page.getHtmlElementById("form:clearStatusMessage");
        page = (HtmlPage) button.click();
        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:replaceSessionBean");
        page = (HtmlPage) button.click();
        text = page.asText();
        assertTrue(Pattern.matches("(?s).*-----------------.*bean: requestBean postConstructCalled: true.*bean: requestBean preDestroyCalled: true.*bean: sessionBean preDestroyCalled: true.*-----------------.*bean: requestBean postConstructCalled: true.*",
                text));

        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:removeSessionBean");
        page = (HtmlPage) button.click();
         button = (HtmlSubmitInput)
            page.getHtmlElementById("form:clearStatusMessage");
        page = (HtmlPage) button.click();
        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:replaceSessionBean2");
        page = (HtmlPage) button.click();
        text = page.asText();
        assertTrue(Pattern.matches("(?s).*-----------------.*bean: requestBean postConstructCalled: true.*bean: requestBean preDestroyCalled: true.*-----------------.*bean: requestBean postConstructCalled: true.*",
                text));

        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:removeApplicationBean");
        page = (HtmlPage) button.click();
         button = (HtmlSubmitInput)
            page.getHtmlElementById("form:clearStatusMessage");
        page = (HtmlPage) button.click();
        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:replaceApplicationBean");
        page = (HtmlPage) button.click();
        text = page.asText();
        assertTrue(Pattern.matches("(?s).*-----------------.*bean: requestBean postConstructCalled: true.*bean: requestBean preDestroyCalled: true.*bean: applicationBean preDestroyCalled: true.*-----------------.*bean: requestBean postConstructCalled: true.*",
                text));

        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:removeApplicationBean");
        page = (HtmlPage) button.click();
         button = (HtmlSubmitInput)
            page.getHtmlElementById("form:clearStatusMessage");
        page = (HtmlPage) button.click();
        button = (HtmlSubmitInput)
            page.getHtmlElementById("form:replaceApplicationBean2");
        page = (HtmlPage) button.click();
        text = page.asText();
        assertTrue(Pattern.matches("(?s).*-----------------.*bean: requestBean postConstructCalled: true.*bean: requestBean preDestroyCalled: true.*-----------------.*bean: requestBean postConstructCalled: true.*",
                text));
        
    }


}
