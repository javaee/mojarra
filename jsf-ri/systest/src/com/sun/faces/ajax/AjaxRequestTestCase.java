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
        System.out.println("Start ajax count test");

        // First we'll check the first page was output correctly
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

        // Check that the ajax request succeeds - eventually.  Give it three seconds.
        boolean status = false;
        //try 30 times to wait .1 second each for filling the page.
        for (int i = 0; i < 30; i++) {
            out1 = page2.getHtmlElementById("countForm:out1");
            out1Content = out1.asText();
            System.out.println("iteration: "+i+"  out1Content: "+out1Content);
            if ("2".equals(out1Content)) {
                status = true;
                break;
            }
            synchronized (page2) {
                page2.wait(100);
            }
        }
        assertTrue(status);

        // Check that the request did NOT update the rest of the page.
        out2 = page.getHtmlElementById("out2");
        out2Content = out2.asText();
        System.out.println("after Ajax out2Content: "+out2Content);
        assertTrue("1".equals(out2Content));

    }

    public void testAjaxRequestDefaultsButton() throws Exception {
        System.out.println("Starting Request Defaults Button Test");
        HtmlPage page = getPage("/faces/ajax/ajaxRequestDefaultsButton.xhtml");

        // First, we'll test to make sure the initial values come out right
        String out1 = ((HtmlElement)page.getHtmlElementById("form1:out1")).asText();
        String out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
        String out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("Initial values: "+out1+" "+out2+" "+out3);
        assertTrue("0".equals(out1));
        assertTrue("1".equals(out2));
        assertTrue("2".equals(out3));

        // Reload the page
        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("form1:reload");
        page = (HtmlPage) button.click();

        // Check that the page updated correctly
        out1 = ((HtmlElement)page.getHtmlElementById("form1:out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("Reloaded values: "+out1+" "+out2+" "+out3);
        assertTrue("3".equals(out1));
        assertTrue("4".equals(out2));
        assertTrue("5".equals(out3));

        // Now, make the Ajax call to first reset button
        button = (HtmlSubmitInput) page.getHtmlElementById("form1:reset1");
        page = (HtmlPage) button.click();
        // Check that the ajax request succeeds - eventually.  Give it three seconds.
        boolean status = false;
        //try 30 times to wait .1 second each for filling the page.
        for (int i = 0; i < 30; i++) {
            HtmlElement out1ele = page.getHtmlElementById("form1:out1");
            out1 = out1ele.asText();
            System.out.println("iteration "+i+": "+out1);
            if ("0".equals(out1)) {
                out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be the same as above, unchanged by the ajax request
                assertTrue("4".equals(out2));
                assertTrue("5".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(100);
            }
        }
        System.out.println("After reset1 values: "+out1+" "+out2+" "+out3);
        assertTrue(status);

        // Reload the page
        button = (HtmlSubmitInput) page.getHtmlElementById("form1:reload");
        page = (HtmlPage) button.click();

        // Check that the page updated correctly
        out1 = ((HtmlElement)page.getHtmlElementById("form1:out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("Reloaded values: "+out1+" "+out2+" "+out3);
        assertTrue("1".equals(out1));
        assertTrue("2".equals(out2));
        assertTrue("3".equals(out3));

        // Now, make the Ajax call to second reset button
        button = (HtmlSubmitInput) page.getHtmlElementById("form1:reset2");
        page = (HtmlPage) button.click();
        // Check that the ajax request succeeds - eventually.  Give it three seconds.
        status = false;
        //try 30 times to wait .1 second each for filling the page.
        for (int i = 0; i < 30; i++) {
            HtmlElement out1ele = page.getHtmlElementById("form1:out1");
            out1 = out1ele.asText();
            System.out.println("iteration "+i+": "+out1);
            if ("0".equals(out1)) {
                out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be the same as above, unchanged by the ajax request
                assertTrue("2".equals(out2));
                assertTrue("3".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(100);
            }
        }
        System.out.println("After reset2 values: "+out1+" "+out2+" "+out3);
        assertTrue(status);

        // Reload the page
        button = (HtmlSubmitInput) page.getHtmlElementById("form1:reload");
        page = (HtmlPage) button.click();

        // Check that the page updated correctly
        out1 = ((HtmlElement)page.getHtmlElementById("form1:out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("Reloaded values: "+out1+" "+out2+" "+out3);
        assertTrue("1".equals(out1));
        assertTrue("2".equals(out2));
        assertTrue("3".equals(out3));

        // Now, make the Ajax call to third reset button
        button = (HtmlSubmitInput) page.getHtmlElementById("form1:reset3");
        page = (HtmlPage) button.click();
        // Check that the ajax request succeeds - eventually.  Give it three seconds.
        status = false;
        //try 30 times to wait .1 second each for filling the page.
        for (int i = 0; i < 30; i++) {
            HtmlElement out1ele = page.getHtmlElementById("form1:out1");
            out1 = out1ele.asText();
            System.out.println("iteration "+i+": "+out1);
            if ("0".equals(out1)) {
                out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be changed from above, changed by the ajax request
                assertTrue("1".equals(out2));
                assertTrue("2".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(100);
            }
        }
        System.out.println("After reset3 values: "+out1+" "+out2+" "+out3);
        assertTrue(status);

        // Reload the page
        button = (HtmlSubmitInput) page.getHtmlElementById("form1:reload");
        page = (HtmlPage) button.click();

        // Check that the page updated correctly
        out1 = ((HtmlElement)page.getHtmlElementById("form1:out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("Reloaded values: "+out1+" "+out2+" "+out3);
        assertTrue("3".equals(out1));
        assertTrue("4".equals(out2));
        assertTrue("5".equals(out3));

        // Now, make the Ajax call to fourth reset button
        button = (HtmlSubmitInput) page.getHtmlElementById("form1:reset4");
        page = (HtmlPage) button.click();
        // Check that the ajax request succeeds - eventually.  Give it three seconds.
        status = false;
        //try 30 times to wait .1 second each for filling the page.
        for (int i = 0; i < 30; i++) {
            HtmlElement out1ele = page.getHtmlElementById("form1:out1");
            out1 = out1ele.asText();
            System.out.println("iteration "+i+": "+out1);
            if ("0".equals(out1)) {
                out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be changed from above, changed by the ajax request
                assertTrue("1".equals(out2));
                assertTrue("2".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(100);
            }
        }
        System.out.println("After reset4 values: "+out1+" "+out2+" "+out3);
        assertTrue(status);

    }

    /*
    public void testAjaxRequestDefaultsButtonNoPrepend() throws Exception {
        System.out.println("Starting Request Defaults Button No Prepend Test");
        HtmlPage page = getPage("/faces/ajax/ajaxRequestDefaultsButtonNoPrepend.xhtml");

        // First, we'll test to make sure the initial values come out right
        String out1 = ((HtmlElement)page.getHtmlElementById("out1")).asText();
        String out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
        String out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("Initial values: "+out1+" "+out2+" "+out3);
        assertTrue("0".equals(out1));
        assertTrue("1".equals(out2));
        assertTrue("2".equals(out3));

        // Reload the page
        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("reload");
        page = (HtmlPage) button.click();

        // Check that the page updated correctly
        out1 = ((HtmlElement)page.getHtmlElementById("out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("Reloaded values: "+out1+" "+out2+" "+out3);
        assertTrue("3".equals(out1));
        assertTrue("4".equals(out2));
        assertTrue("5".equals(out3));

        // Now, make the Ajax call to first reset button
        button = (HtmlSubmitInput) page.getHtmlElementById("reset1");
        page = (HtmlPage) button.click();
        // Check that the ajax request succeeds - eventually.  Give it three seconds.
        boolean status = false;
        //try 30 times to wait .1 second each for filling the page.
        for (int i = 0; i < 30; i++) {
            HtmlElement out1ele = page.getHtmlElementById("out1");
            out1 = out1ele.asText();
            System.out.println("iteration "+i+": "+out1);
            if ("0".equals(out1)) {
                out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be the same as above, unchanged by the ajax request
                assertTrue("4".equals(out2));
                assertTrue("5".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(100);
            }
        }
        System.out.println("After reset1 values: "+out1+" "+out2+" "+out3);
        assertTrue(status);

        // Reload the page
        button = (HtmlSubmitInput) page.getHtmlElementById("reload");
        page = (HtmlPage) button.click();

        // Check that the page updated correctly
        out1 = ((HtmlElement)page.getHtmlElementById("out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("Reloaded values: "+out1+" "+out2+" "+out3);
        assertTrue("1".equals(out1));
        assertTrue("2".equals(out2));
        assertTrue("3".equals(out3));

        // Now, make the Ajax call to second reset button
        button = (HtmlSubmitInput) page.getHtmlElementById("reset2");
        page = (HtmlPage) button.click();
        // Check that the ajax request succeeds - eventually.  Give it three seconds.
        status = false;
        //try 30 times to wait .1 second each for filling the page.
        for (int i = 0; i < 30; i++) {
            HtmlElement out1ele = page.getHtmlElementById("out1");
            out1 = out1ele.asText();
            System.out.println("iteration "+i+": "+out1);
            if ("0".equals(out1)) {
                out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be the same as above, unchanged by the ajax request
                assertTrue("2".equals(out2));
                assertTrue("3".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(100);
            }
        }
        System.out.println("After reset2 values: "+out1+" "+out2+" "+out3);
        assertTrue(status);

        // Reload the page
        button = (HtmlSubmitInput) page.getHtmlElementById("reload");
        page = (HtmlPage) button.click();

        // Check that the page updated correctly
        out1 = ((HtmlElement)page.getHtmlElementById("out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("Reloaded values: "+out1+" "+out2+" "+out3);
        assertTrue("1".equals(out1));
        assertTrue("2".equals(out2));
        assertTrue("3".equals(out3));

        // Now, make the Ajax call to third reset button
        button = (HtmlSubmitInput) page.getHtmlElementById("reset3");
        page = (HtmlPage) button.click();
        // Check that the ajax request succeeds - eventually.  Give it three seconds.
        status = false;
        //try 30 times to wait .1 second each for filling the page.
        for (int i = 0; i < 30; i++) {
            HtmlElement out1ele = page.getHtmlElementById("out1");
            out1 = out1ele.asText();
            System.out.println("iteration "+i+": "+out1);
            if ("0".equals(out1)) {
                out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be changed from above, changed by the ajax request
                assertTrue("1".equals(out2));
                assertTrue("2".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(100);
            }
        }
        System.out.println("After reset3 values: "+out1+" "+out2+" "+out3);
        assertTrue(status);

        // Reload the page
        button = (HtmlSubmitInput) page.getHtmlElementById("reload");
        page = (HtmlPage) button.click();

        // Check that the page updated correctly
        out1 = ((HtmlElement)page.getHtmlElementById("out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("Reloaded values: "+out1+" "+out2+" "+out3);
        assertTrue("3".equals(out1));
        assertTrue("4".equals(out2));
        assertTrue("5".equals(out3));

        // Now, make the Ajax call to fourth reset button
        button = (HtmlSubmitInput) page.getHtmlElementById("reset4");
        page = (HtmlPage) button.click();
        // Check that the ajax request succeeds - eventually.  Give it three seconds.
        status = false;
        //try 30 times to wait .1 second each for filling the page.
        for (int i = 0; i < 30; i++) {
            HtmlElement out1ele = page.getHtmlElementById("out1");
            out1 = out1ele.asText();
            System.out.println("iteration "+i+": "+out1);
            if ("0".equals(out1)) {
                out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be changed from above, changed by the ajax request
                assertTrue("1".equals(out2));
                assertTrue("2".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(100);
            }
        }
        System.out.println("After reset4 values: "+out1+" "+out2+" "+out3);
        assertTrue(status);
    }
    */

    //RELEASE_PENDING continue adding tests


}
