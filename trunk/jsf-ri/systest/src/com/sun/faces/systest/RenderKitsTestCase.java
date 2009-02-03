/*
 * $Id: RenderKitsTestCase.java,v 1.5 2007/04/27 22:01:10 ofung Exp $
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
