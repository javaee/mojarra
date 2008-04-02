/*
 * $Id: CommandLinkOnClickTestCase.java,v 1.1 2005/07/25 18:34:15 rajprem Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.jsptest;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
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
            hidden = (HtmlHiddenInput)form.getInputByName("form:_idcl");
        } catch (ElementNotFoundException e) {
            assertTrue(false);
        }
        // This initial value was set by an "onLoad" javascript function in the jsp.
        assertTrue(hidden.getValueAttribute().equals("Goodbye"));

        // click the link..
        HtmlAnchor submit = (HtmlAnchor)
            page.getFirstAnchorByText("submit");
            assertTrue(submit.getOnClickAttribute().equals("var a=function(){setValue('form');};var b=function(){clearFormHiddenParams_form('form');document.forms['form']['form:_idcl'].value='form:submit'; document.forms['form'].submit(); return false;};return (a()==false) ? false : b();")); 
        try {
            page = (HtmlPage) submit.click();
	} catch (Exception e) {
	    e.printStackTrace();
	    assertTrue(false);
        }
        // The value of this field was set by the user provided "onclick" javascript
        // function.
        HtmlTextInput input = (HtmlTextInput)form.getInputByName("form:init");
        assertTrue(input.getValueAttribute().equals("Hello"));

        // The value of this field was changed by the internal Faces javascript function
        // created by CommandLinkRenderer..
        try {
            hidden = (HtmlHiddenInput)form.getInputByName("form:_idcl");
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
            hidden = (HtmlHiddenInput)form.getInputByName("form:_idcl");
        } catch (ElementNotFoundException e) {
            assertTrue(false);
        }
        // This initial value was set by an "onLoad" javascript function in the jsp.
        assertTrue(hidden.getValueAttribute().equals("Goodbye"));
                                                                                                            
        // click the link..
        HtmlAnchor submit = (HtmlAnchor)
            page.getFirstAnchorByText("submit");
            assertTrue(submit.getOnClickAttribute().equals("var a=function(){setValue('form'); return false;};var b=function(){clearFormHiddenParams_form('form');document.forms['form']['form:_idcl'].value='form:submit'; document.forms['form'].submit(); return false;};return (a()==false) ? false : b();"));
        try {
            page = (HtmlPage) submit.click();
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        HtmlTextInput input = (HtmlTextInput)form.getInputByName("form:init");
        assertTrue(input.getValueAttribute().equals("Hello"));
                                                                                                            
        // The value of this field remains unchanged from the initial value. 
        try {
            hidden = (HtmlHiddenInput)form.getInputByName("form:_idcl");
        } catch (ElementNotFoundException e) {
            assertTrue(false);
        }
        assertTrue(hidden.getValueAttribute().equals("Goodbye"));
    }

}
