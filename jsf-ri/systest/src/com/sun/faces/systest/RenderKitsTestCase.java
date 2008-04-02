/*
 * $Id: RenderKitsTestCase.java,v 1.2 2005/08/22 22:10:38 ofung Exp $
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

package com.sun.faces.systest;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.faces.component.NamingContainer;

/**
 * <p>Test Case for Multiple RenderKits.</p>
 */

public class RenderKitsTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public RenderKitsTestCase(String name) {
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
        return (new TestSuite(RenderKitsTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods

    public void testRenderKits() throws Exception {
        HtmlPage page = getPage("/faces/renderkit04.jsp");
        assertTrue(-1 != page.asText().indexOf("HTML_BASIC"));
            assertTrue(-1 != page.asText().indexOf("com.sun.faces.renderkit.html_basic.HtmlResponseWriter"));

        HtmlForm form = getFormById(page, "form");
        assertNotNull("form exists", form);
        HtmlSubmitInput submit = (HtmlSubmitInput)
            form.getInputByName("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "submit");
        try {
            page = (HtmlPage) submit.click();
            assertTrue(-1 != page.asText().indexOf("CUSTOM"));
            assertTrue(-1 != page.asText().indexOf("com.sun.faces.systest.render.CustomResponseWriter"));
	} catch (Exception e) {
	    e.printStackTrace();
	    assertTrue(false);
        }

        form = getFormById(page, "form");
        assertNotNull("form exists", form);
        submit = (HtmlSubmitInput)
            form.getInputByName("form" +
                                NamingContainer.SEPARATOR_CHAR +
                                "submit");
        try {
            page = (HtmlPage) submit.click();
            assertTrue(-1 != page.asText().indexOf("HTML_BASIC"));
            assertTrue(-1 != page.asText().indexOf("com.sun.faces.renderkit.html_basic.HtmlResponseWriter"));
	} catch (Exception e) {
	    e.printStackTrace();
	    assertTrue(false);
        }
    }

    // Assert with no renderKitId specfied on the view, and no defaultRenderKitId, 
    // HTML_BASIC is set.  Also assert that the hidden field was not written; 
    public void testNoRenderKitId() throws Exception {
        HtmlPage page = getPage("/faces/renderkit06.jsp");
        HtmlForm form = getFormById(page, "form");
        assertNotNull("form exists", form);
        HtmlHiddenInput hidden = null;
        boolean exceptionThrown = false;
        try {
            hidden = (HtmlHiddenInput)form.getInputByName("javax.faces.RenderKitId");
        } catch (ElementNotFoundException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        assertTrue(-1 != page.asText().indexOf("HTML_BASIC"));
        assertTrue(-1 != page.asText().indexOf("com.sun.faces.renderkit.html_basic.HtmlResponseWriter"));
    }

    // Test when default-render-kit-id is set for application;
    public void testDefaultRenderKitId() throws Exception {
        // sets default-render-kit-id in application
        HtmlPage page = getPage("/faces/renderkit-default.jsp");

        // Load a page with renderKitId="CUSTOM"; 
        // Assert hidden field is not written because renderKitId == defaultRenderKitId;
        page = getPage("/faces/renderkit06.jsp");
        HtmlForm form = getFormById(page, "form");
        assertNotNull("form exists", form);
        HtmlHiddenInput hidden = null;
        boolean exceptionThrown = false;
        try {
            hidden = (HtmlHiddenInput)form.getInputByName("javax.faces.RenderKitId");
        } catch (ElementNotFoundException e) {
            exceptionThrown = true;
        }
        assertTrue(-1 != page.asText().indexOf("CUSTOM"));
        assertTrue(-1 != page.asText().indexOf("com.sun.faces.systest.render.CustomResponseWriter"));

        // Load a page with renderKitId="HTML_BASIC";
        // Assert hidden field is written because renderKitId != defaultRenderKitId;
        page = getPage("/faces/renderkit04.jsp");
        form = getFormById(page, "form");
        assertNotNull("form exists", form);
        hidden = (HtmlHiddenInput)form.getInputByName("javax.faces.RenderKitId");
        assertNotNull("hidden exists", hidden);
        assertTrue(-1 != page.asText().indexOf("HTML_BASIC"));
        assertTrue(-1 != page.asText().indexOf("com.sun.faces.renderkit.html_basic.HtmlResponseWriter"));
        page = getPage("/faces/renderkit-default-clear.jsp");
    }
}
