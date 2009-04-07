package com.sun.faces.ajax;

import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AjaxAllKeywordTestCase extends AbstractTestCase {

    public AjaxAllKeywordTestCase(String name) {
        super(name);
    }

    /*
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        super.setUp();
    }


    /*
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(AjaxAllKeywordTestCase.class));
    }


    /*
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testAjaxAllKeyword1() throws Exception {

        getPage("/faces/ajax/ajaxAllKeyword1.xhtml");
        System.out.println("Start ajax All Keyword test");

        // First we'll check the first page was output correctly
        checkTrue("form1:out1","testtext");

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:allKeyword");
        HtmlPage lastpage = (HtmlPage) button1.click();

        // Check that the ajax request succeeds - if the page is rewritten, this will be the same
        checkTrue("form1:out1","testtext");

    }
    public void testAjaxAllKeyword2() throws Exception {

        getPage("/faces/ajax/ajaxAllKeyword2.xhtml");
        System.out.println("Start ajax All Keyword test");

        // First we'll check the first page was output correctly
        checkTrue("form1:out1","testtext");

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:allKeyword");
        HtmlPage lastpage = (HtmlPage) button1.click();

        // Check that the ajax request succeeds - if the page is rewritten, this will be the same
        checkTrue("form1:out1","testtext");

    }
    public void testAjaxAllKeyword3() throws Exception {

        getPage("/faces/ajax/ajaxAllKeyword3.xhtml");
        System.out.println("Start ajax All Keyword test");

        // First we'll check the first page was output correctly
        checkTrue("form1:out1","testtext");

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:allKeyword");
        HtmlPage lastpage = (HtmlPage) button1.click();

        // Check that the ajax request succeeds - if the page is rewritten, this will be the same
        checkTrue("form1:out1","testtext");

    }
}
