package com.sun.faces.ajax;

import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;

public class AjaxTagResolveTestCase extends AbstractTestCase {

    public AjaxTagResolveTestCase(String name) {
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
        return (new TestSuite(AjaxTagResolveTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    /*
     * Check that the id's resolve correctly.
     */
    public void testAjaxTagWrapping() throws Exception {
        getPage("/faces/ajax/ajaxTagResolve.xhtml");
        System.out.println("Start ajax tag resolution test");

        // First we'll check the first page was output correctly
        assertTrue(check("form1:out1", "0"));
        assertTrue(check("form1:out5", "1"));
        assertTrue(check("form2:out2", "2"));
        assertTrue(check("out3", "3"));
        assertTrue(check("out4", "4"));

        HtmlSubmitInput button;
        button = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button1");
        lastpage = (HtmlPage) button.click();
        assertTrue(check("form1:out1","5"));

        button = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button2");
        lastpage = (HtmlPage) button.click();
        assertTrue(check("form2:out2","6"));

        button = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button3");
        lastpage = (HtmlPage) button.click();
        assertTrue(check("out3","7"));

        button = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button4");
        lastpage = (HtmlPage) button.click();
        assertTrue(check("form1:out1","8"));

        button = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button5");
        lastpage = (HtmlPage) button.click();
        assertTrue(check("form1:out1","9"));
        assertTrue(check("form2:out2","10"));
        assertTrue(check("out3","11"));

        // Check that nothing updated that we didn't want
        assertTrue(check("out4","4"));
        assertTrue(check("form1:out5","1"));


    }

}