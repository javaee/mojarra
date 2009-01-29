package com.sun.faces.ajax;

import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import junit.framework.Test;
import junit.framework.TestSuite;


public class AjaxIncludedTextTestCase  extends AbstractTestCase {

    public AjaxIncludedTextTestCase(String name) {
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
        return (new TestSuite(AjaxIncludedTextTestCase.class));
    }


    /*
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testAjaxIncludedText() throws Exception {
        getPage("/faces/ajax/ajaxIncludedText.xhtml");
        System.out.println("Start ajax included text test");

        // First we'll check the first page was output correctly
        checkTrue("finalSpan1","Text should stay");
        checkTrue("finalSpan2","Text should stay");

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("form1:refresh");
        lastpage = (HtmlPage) button1.click();
        HtmlSubmitInput button2 = (HtmlSubmitInput) lastpage.getHtmlElementById("form2:refresh");
        lastpage = (HtmlPage) button2.click();

        // Check that the ajax request succeeds
        checkTrue("finalSpan1","Text should stay");
        checkTrue("finalSpan2","Text should stay");
    }
}

