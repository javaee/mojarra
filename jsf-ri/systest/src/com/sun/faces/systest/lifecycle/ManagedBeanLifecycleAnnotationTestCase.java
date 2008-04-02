/*
 * $Id: ManagedBeanLifecycleAnnotationTestCase.java,v 1.3 2005/10/25 20:39:57 rlubke Exp $
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

package com.sun.faces.systest.lifecycle;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Test Case for JSP Interoperability.</p>
 */

public class ManagedBeanLifecycleAnnotationTestCase extends AbstractTestCase {


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
