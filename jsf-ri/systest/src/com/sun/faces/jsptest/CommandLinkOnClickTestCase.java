/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.faces.jsptest;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.*;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>Test Case for Multiple RenderKits.</p>
 */

public class CommandLinkOnClickTestCase extends AbstractTestCase {

    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public CommandLinkOnClickTestCase(String name) {
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
        return (new TestSuite(CommandLinkOnClickTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }

    // ------------------------------------------------- Individual Test Methods

    // This method tests that a user provided commandLink "onclick" javascript
    // method will get executed in addition to the internal one rendered
    // as part of CommandLinkRenderer.

    public void testOnClickReturnTrue() throws Exception {
        HtmlPage page = getPage("/faces/jsp/commandLinkOnClickTrue.jsp");

        HtmlForm form = getFormById(page, "form");
        assertNotNull("form exists", form);
        HtmlHiddenInput hidden = null;
        try {
            hidden = (HtmlHiddenInput) form.getInputByName("form:j_idcl");
        } catch (ElementNotFoundException e) {
            assertTrue(false);
        }
        // This initial value was set by an "onLoad" javascript function in the jsp.
        assertTrue(hidden.getValueAttribute().equals("Goodbye"));

        // click the link..
        HtmlAnchor submit = (HtmlAnchor)
                page.getFirstAnchorByText("submit");
        assertTrue(submit.getOnClickAttribute().equals("var a=function(){setValue('form');};var b=function(){clearFormHiddenParams_form('form');document.forms['form']['form:j_idcl'].value='form:submit'; document.forms['form'].submit(); return false;};return (a()==false) ? false : b();"));
        try {
            page = (HtmlPage) submit.click();
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        // The value of this field was set by the user provided "onclick" javascript
        // function.
        HtmlTextInput input = (HtmlTextInput) form.getInputByName("form:init");
        assertTrue(input.getValueAttribute().equals("Hello"));

        // The value of this field was changed by the internal Faces javascript function
        // created by CommandLinkRenderer..
        try {
            hidden = (HtmlHiddenInput) form.getInputByName("form:j_idcl");
        } catch (ElementNotFoundException e) {
            assertTrue(false);
        }
        assertTrue(hidden.getValueAttribute().equals("form:submit"));
    }

    // This method tests that a user provided commandLink "onclick" javascript
    // method will get executed.  The user provided function returns "false",
    // so, the internal Faces function should not execute.
    public void testOnClickReturnFalse() throws Exception {
        HtmlPage page = getPage("/faces/jsp/commandLinkOnClickFalse.jsp");

        HtmlForm form = getFormById(page, "form");
        assertNotNull("form exists", form);
        HtmlHiddenInput hidden = null;
        try {
            hidden = (HtmlHiddenInput) form.getInputByName("form:j_idcl");
        } catch (ElementNotFoundException e) {
            assertTrue(false);
        }
        // This initial value was set by an "onLoad" javascript function in the jsp.
        assertTrue(hidden.getValueAttribute().equals("Goodbye"));

        // click the link..
        HtmlAnchor submit = (HtmlAnchor)
                page.getFirstAnchorByText("submit");
        assertTrue(submit.getOnClickAttribute().equals("var a=function(){setValue('form'); return false;};var b=function(){clearFormHiddenParams_form('form');document.forms['form']['form:j_idcl'].value='form:submit'; document.forms['form'].submit(); return false;};return (a()==false) ? false : b();"));
        try {
            page = (HtmlPage) submit.click();
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        HtmlTextInput input = (HtmlTextInput) form.getInputByName("form:init");
        assertTrue(input.getValueAttribute().equals("Hello"));

        // The value of this field remains unchanged from the initial value. 
        try {
            hidden = (HtmlHiddenInput) form.getInputByName("form:j_idcl");
        } catch (ElementNotFoundException e) {
            assertTrue(false);
        }
        assertTrue(hidden.getValueAttribute().equals("Goodbye"));
    }

}
