package com.sun.faces.ajax;

import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;


public class AjaxRequestTestCase extends AbstractTestCase {

    public AjaxRequestTestCase(String name) {
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


    public void testAjaxCount() throws Exception {
        HtmlPage page = getPage("/faces/ajax/ajaxRequest.xhtml");
        HtmlElement out1 = page.getHtmlElementById("countForm:out1");
        String out1Content = out1.asText();
        assertTrue("0".equals(out1Content));
    }

    /*
    public void testBasicAjaxRequest() throws Exception {
        HtmlPage page1 = getPage("/faces/ajax/ajaxRequest.xhtml");
        HtmlElement text1 = page1.getHtmlElementById("text1");
        String initial = text1.asText();
        assertTrue("initial".equals(initial));
        HtmlSubmitInput submit = (HtmlSubmitInput)
                getInputContainingGivenId(page1, "button1");
        HtmlPage page2 = (HtmlPage) submit.click();
        text1 = page2.getHtmlElementById("text1");
        String result = text1.asText();
        assertTrue("action1".equals(result));
    }
    */


}