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
        HtmlPage page = getPage("/faces/ajax/ajaxCount.xhtml");

        // First we'll check the first page outputted correctly
        HtmlElement out1 = page.getHtmlElementById("countForm:out1");
        String out1Content = out1.asText();
        System.out.println("initial out1Content: "+out1Content);
        assertTrue("0".equals(out1Content));
        HtmlElement out2 = page.getHtmlElementById("out2"); // not in the form
        String out2Content = out2.asText();
        System.out.println("intial out2Content: "+out2Content);
        assertTrue("1".equals(out2Content));


        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) page.getHtmlElementById("countForm:button1");
        HtmlPage page2 = (HtmlPage) button1.click();

        // Check that the ajax request succeeds - eventually.  Give it four seconds.
        boolean status = false;
        //try 20 times to wait .2 second each for filling the page.
        for (int i = 0; i < 20; i++) {
            out1 = page2.getHtmlElementById("countForm:out1");
            out1Content = out1.asText();
            System.out.println("iteration: "+i+"  out1Content: "+out1Content);
            if ("2".equals(out1Content)) {
                status = true;
                break;
            }
            synchronized (page2) {
                page2.wait(200);
            }
        }
        assertTrue(status);

        // Check that the request did NOT update the rest of the page.
        out2 = page.getHtmlElementById("out2");
        out2Content = out2.asText();
        System.out.println("after Ajax out2Content: "+out2Content);
        assertTrue("1".equals(out2Content));

    }

    /*
    public void testBasicAjaxRequest() throws Exception {
        HtmlPage page1 = getPage("/faces/ajax/ajaxCount.xhtml");
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
