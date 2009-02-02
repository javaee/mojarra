package com.sun.faces.ajax;

import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AjaxEchoTestCase  extends AbstractTestCase {

    public AjaxEchoTestCase(String name) {
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
        return (new TestSuite(AjaxEchoTestCase.class));
    }


    /*
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testAjaxEcho() throws Exception {
        getPage("/faces/ajax/ajaxEcho.xhtml");
        System.out.println("Start ajax echo test");

        // First we'll check the first page was output correctly
        checkTrue("form1:out1","");
        checkTrue("form1:in1","");

        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("form1:in1");

        in1.type("hello");

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button1");
        HtmlPage lastpage = (HtmlPage) button1.click();

        // Check that the ajax request succeeds
        checkTrue("form1:out1","hello");
    }


    /*
     * Regression test for bug #939
     */
    public void testCdataEscape1() throws Exception {
        getPage("/faces/ajax/ajaxEcho.xhtml");
        System.out.println("Start ajax cdata 1 test");

        // First we'll check the first page was output correctly
        checkTrue("form1:out1","");
        checkTrue("form1:in1","");

        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("form1:in1");

        in1.type("]]>");

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button1");
        HtmlPage lastpage = (HtmlPage) button1.click();

        // Check that the ajax request succeeds
        checkTrue("form1:out1","]]>");
    }
    public void testCdataEscape2() throws Exception {
        getPage("/faces/ajax/ajaxEcho.xhtml");
        System.out.println("Start ajax cdata 2 test");

        // First we'll check the first page was output correctly
        checkTrue("form1:out1","");
        checkTrue("form1:in1","");

        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("form1:in1");

        in1.type("<!");

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button1");
        HtmlPage lastpage = (HtmlPage) button1.click();

        // Check that the ajax request succeeds
        checkTrue("form1:out1","<!");
    }
    public void testCdataEscape3() throws Exception {
        getPage("/faces/ajax/ajaxEcho.xhtml");
        System.out.println("Start ajax cdata 2 test");

        // First we'll check the first page was output correctly
        checkTrue("form1:out1","");
        checkTrue("form1:in1","");

        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("form1:in1");

        in1.type("]");

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button1");
        HtmlPage lastpage = (HtmlPage) button1.click();

        // Check that the ajax request succeeds
        checkTrue("form1:out1","]");
    }
    public void testCdataEscape4() throws Exception {
        getPage("/faces/ajax/ajaxEcho.xhtml");
        System.out.println("Start ajax cdata 2 test");

        // First we'll check the first page was output correctly
        checkTrue("form1:out1","");
        checkTrue("form1:in1","");

        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("form1:in1");

        in1.type("]");

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button1");
        HtmlPage lastpage = (HtmlPage) button1.click();

        // Check that the ajax request succeeds
        checkTrue("form1:out1","]");
    }
    public void testCdataEscape5() throws Exception {
        getPage("/faces/ajax/ajaxEcho.xhtml");
        System.out.println("Start ajax cdata 2 test");

        // First we'll check the first page was output correctly
        checkTrue("form1:out1","");
        checkTrue("form1:in1","");

        HtmlTextInput in1 = (HtmlTextInput) lastpage.getHtmlElementById("form1:in1");

        in1.type("<![CDATA[ ]]>");

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:button1");
        HtmlPage lastpage = (HtmlPage) button1.click();

        // Check that the ajax request succeeds
        checkTrue("form1:out1","<![CDATA[ ]]>");
    }
    
}
