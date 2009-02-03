package com.sun.faces.ajax;

import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class AjaxRequestMultiRenderTestCase extends AbstractTestCase {

    public AjaxRequestMultiRenderTestCase(String name) {
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
        return (new TestSuite(AjaxRequestTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testAjaxMultiRender() throws Exception {
        getPage("/faces/ajax/ajaxRequestMultiRender.xhtml");
        System.out.println("Start ajax multi render test");

        // First we'll check the first page was output correctly
        assertTrue(check("out1","0"));
        assertTrue(check("out2","0"));
        assertTrue(check("out3","0"));
        assertTrue(check("out4","0"));

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("button1");
        lastpage = (HtmlPage) button1.click();

        // Check that the request succeeds
        assertTrue(check("out1","1"));
        assertTrue(check("out2","1"));
        assertTrue(check("out3","1"));

        // Check that the request did NOT update the rest of the page.
        assertTrue(check("out4","0"));

        // Submit the reset
        HtmlSubmitInput reset = (HtmlSubmitInput) lastpage.getHtmlElementById("reset");
        lastpage = (HtmlPage) reset.click();

        // Check that reset succeeds
        assertTrue(check("out1","0"));
        assertTrue(check("out2","0"));
        assertTrue(check("out3","0"));
        assertTrue(check("out4","0"));

    }

}