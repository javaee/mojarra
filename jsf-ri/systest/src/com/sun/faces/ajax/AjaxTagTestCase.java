package com.sun.faces.ajax;

import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;


// RELEASE_PENDING - remove thread sleeps
// RELEASE_PENDING - completely refactor code

public class AjaxTagTestCase extends AbstractTestCase {

    // interval for test
    private static final int interval = 50;
    // number of times to try to read new value
    private static final int iterate = 20;
    // wait period for a fixed time
    private static final int interval2 = interval * 3;

    // total for each test = interval * iterate


    public AjaxTagTestCase(String name) {
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
        return (new TestSuite(AjaxTagTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testAjaxTagCount() throws Exception {
        HtmlPage page = getPage("/faces/ajax/ajaxTagCount.xhtml");
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
        for (int i = 0; i < iterate; i++) {
            out1 = page2.getHtmlElementById("countForm:out1");
            out1Content = out1.asText();
            System.out.println("iteration: "+i+"  out1Content: "+out1Content);
            if ("2".equals(out1Content)) {
                status = true;
                break;
            }
            synchronized (page2) {
                page2.wait(interval);
            }
        }
        assertTrue(status);

        // Check that the request did NOT update the rest of the page.
        out2 = page.getHtmlElementById("out2");
        out2Content = out2.asText();
        System.out.println("after Ajax out2Content: "+out2Content);
        assertTrue("1".equals(out2Content));

    }

    public void testAjaxTagDefaultsButton() throws Exception {
        System.out.println("Starting Ajax Tag Defaults Button Test");
        HtmlPage page = getPage("/faces/ajax/ajaxTagDefaultsButton.xhtml");

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
        for (int i = 0; i < iterate; i++) {
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
                page.wait(interval);
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
        status = false;
        for (int i = 0; i < iterate; i++) {
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
                page.wait(interval);
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

        // Check the page did *not* update
        out1 = ((HtmlElement)page.getHtmlElementById("form1:out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("After reset3 values: "+out1+" "+out2+" "+out3);
        assertTrue("1".equals(out1));
        assertTrue("2".equals(out2));
        assertTrue("3".equals(out3));

        // Now, Reload the page, to check that reset3 actually executed
        Thread.sleep(interval2);
        button = (HtmlSubmitInput) page.getHtmlElementById("form1:reload");
        page = (HtmlPage) button.click();
        out1 = ((HtmlElement)page.getHtmlElementById("form1:out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("After reset3+reload values: "+out1+" "+out2+" "+out3);
        assertTrue("0".equals(out1));
        assertTrue("1".equals(out2));
        assertTrue("2".equals(out3));

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

        // Check the page did *not* update
        out1 = ((HtmlElement)page.getHtmlElementById("form1:out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("After reset4 values: "+out1+" "+out2+" "+out3);
        assertTrue("3".equals(out1));
        assertTrue("4".equals(out2));
        assertTrue("5".equals(out3));

        // Now, Reload the page, to check that reset4 actually executed
        Thread.sleep(interval2);
        button = (HtmlSubmitInput) page.getHtmlElementById("form1:reload");
        page = (HtmlPage) button.click();
        out1 = ((HtmlElement)page.getHtmlElementById("form1:out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("After reset4+reload values: "+out1+" "+out2+" "+out3);
        assertTrue("0".equals(out1));
        assertTrue("1".equals(out2));
        assertTrue("2".equals(out3));
    }

    public void testAjaxTagDefaultsButtonNoPrepend() throws Exception {
        System.out.println("Starting Tag Defaults Button No Prepend Test");
        HtmlPage page = getPage("/faces/ajax/ajaxTagDefaultsButtonNoPrepend.xhtml");

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
        for (int i = 0; i < iterate; i++) {
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
                page.wait(interval);
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
        status = false;
        for (int i = 0; i < iterate; i++) {
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
                page.wait(interval);
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

        // Check the page did *not* update
        out1 = ((HtmlElement)page.getHtmlElementById("out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("After reset3 values: "+out1+" "+out2+" "+out3);
        assertTrue("1".equals(out1));
        assertTrue("2".equals(out2));
        assertTrue("3".equals(out3));

        // Now, Reload the page, to check that reset3 actually executed
        Thread.sleep(interval2);
        button = (HtmlSubmitInput) page.getHtmlElementById("reload");
        page = (HtmlPage) button.click();
        out1 = ((HtmlElement)page.getHtmlElementById("out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("After reset3+reload values: "+out1+" "+out2+" "+out3);
        assertTrue("0".equals(out1));
        assertTrue("1".equals(out2));
        assertTrue("2".equals(out3));

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

        // Check the page did *not* update
        out1 = ((HtmlElement)page.getHtmlElementById("out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("After reset4 values: "+out1+" "+out2+" "+out3);
        assertTrue("3".equals(out1));
        assertTrue("4".equals(out2));
        assertTrue("5".equals(out3));

        // Now, Reload the page, to check that reset4 actually executed
        Thread.sleep(interval2);
        button = (HtmlSubmitInput) page.getHtmlElementById("reload");
        page = (HtmlPage) button.click();
        out1 = ((HtmlElement)page.getHtmlElementById("out1")).asText();
        out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
        out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("After reset4+reload values: "+out1+" "+out2+" "+out3);
        assertTrue("0".equals(out1));
        assertTrue("1".equals(out2));
        assertTrue("2".equals(out3));
    }

    public void testAjaxTagDefaultsEdit() throws Exception {
        System.out.println("Starting Tag Defaults Edit Test");
        HtmlPage page = getPage("/faces/ajax/ajaxTagDefaultsEdit.xhtml");

        // First, we'll test to make sure the initial values come out right
        String out1 = ((HtmlElement)page.getHtmlElementById("form1:out1")).asText();
        String echo1Out = ((HtmlElement)page.getHtmlElementById("form1:echo1Out")).asText();
        String echo2Out = ((HtmlElement)page.getHtmlElementById("form1:echo2Out")).asText();
        String echo3Out = ((HtmlElement)page.getHtmlElementById("form1:echo3Out")).asText();
        String echo4Out = ((HtmlElement)page.getHtmlElementById("form1:echo4Out")).asText();
        String out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
        String out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("Initial values: "+out1+" "+out2+" "+out3+" "+echo1Out+" "+echo2Out+" "+echo3Out+" "+echo4Out);
        assertTrue("echo".equals(out1));
        assertTrue("echo".equals(out2));
        assertTrue("echo".equals(out3));
        assertTrue("".equals(echo1Out));
        assertTrue("".equals(echo2Out));
        assertTrue("".equals(echo3Out));
        assertTrue("".equals(echo4Out));

        // Next, enter data into first field
        HtmlTextInput echo1 = ((HtmlTextInput)page.getHtmlElementById("form1:edit1"));
        echo1.focus();
        echo1.type("test1");
        echo1.blur();
        Thread.sleep(interval2);

        // Refresh the panel to check the listener fired
        HtmlSubmitInput refresh = page.getHtmlElementById("form1:refresh");
        refresh.click();
        boolean status = false;
        for (int i = 0; i < iterate; i++) {
            out1 = ((HtmlElement)page.getHtmlElementById("form1:out1")).asText();
            System.out.println("iteration "+i+": "+out1);
            if ("test1".equals(out1)) {
                echo1Out = ((HtmlElement)page.getHtmlElementById("form1:echo1Out")).asText();
                assertTrue("test1".equals(echo1Out));
                out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be the same as above, unchanged by the ajax request
                assertTrue("echo".equals(out2));
                assertTrue("echo".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(interval);
            }
        }
        System.out.println("After echo1 values: "+echo1Out+" "+out1+" "+out2+" "+out3);
        assertTrue(status);

        // Next, enter data into second field
        HtmlTextInput echo2 = ((HtmlTextInput)page.getHtmlElementById("form1:edit2"));
        echo2.focus();
        echo2.type("test2");
        echo2.blur();
        Thread.sleep(interval2);

        // Refresh the panel to check the listener fired
        refresh = page.getHtmlElementById("form1:refresh");
        refresh.click();
        status = false;
        for (int i = 0; i < iterate; i++) {
            out1 = ((HtmlElement)page.getHtmlElementById("form1:out1")).asText();
            System.out.println("iteration "+i+": "+out1);
            if ("test2".equals(out1)) {
                echo2Out = ((HtmlElement)page.getHtmlElementById("form1:echo2Out")).asText();
                assertTrue("test2".equals(echo2Out));
                out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be the same as above, unchanged by the ajax request
                assertTrue("echo".equals(out2));
                assertTrue("echo".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(interval);
            }
        }
        System.out.println("After echo2 values: "+echo2Out+" "+out1+" "+out2+" "+out3);
        assertTrue(status);


        // Next, enter data into third field
        HtmlTextInput echo3 = ((HtmlTextInput)page.getHtmlElementById("form1:edit3"));
        echo3.focus();
        echo3.type("test3");
        echo3.blur();
        Thread.sleep(interval2);

        // Refresh the panel to check the listener fired
        refresh = page.getHtmlElementById("form1:refresh");
        refresh.click();
        status = false;
        for (int i = 0; i < iterate; i++) {
            out1 = ((HtmlElement)page.getHtmlElementById("form1:out1")).asText();
            System.out.println("iteration "+i+": "+out1);
            if ("test3".equals(out1)) {
                echo3Out = ((HtmlElement)page.getHtmlElementById("form1:echo3Out")).asText();
                assertTrue("test3".equals(echo3Out));
                out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be the same as above, unchanged by the ajax request
                assertTrue("echo".equals(out2));
                assertTrue("echo".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(interval);
            }
        }
        System.out.println("After echo3 values: "+echo3Out+" "+out1+" "+out2+" "+out3);
        assertTrue(status);

        // Next, enter data into the fourth field
        HtmlTextInput echo4 = ((HtmlTextInput)page.getHtmlElementById("form1:edit4"));
        echo4.focus();
        echo4.type("test4");
        echo4.blur();
        Thread.sleep(interval2);

        // Refresh the panel to check the listener fired
        refresh = page.getHtmlElementById("form1:refresh");
        refresh.click();
        status = false;
        for (int i = 0; i < iterate; i++) {
            out1 = ((HtmlElement)page.getHtmlElementById("form1:out1")).asText();
            System.out.println("iteration "+i+": "+out1);
            if ("test4".equals(out1)) {
                echo4Out = ((HtmlElement)page.getHtmlElementById("form1:echo4Out")).asText();
                assertTrue("test4".equals(echo4Out));
                out2 = ((HtmlElement)page.getHtmlElementById("form1:out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be the same as above, unchanged by the ajax request
                assertTrue("echo".equals(out2));
                assertTrue("echo".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(interval);
            }
        }
        System.out.println("After echo4 values: "+echo4Out+" "+out1+" "+out2+" "+out3);
        assertTrue(status);

    }
    public void testAjaxTagDefaultsEditNoPrepend() throws Exception {
        System.out.println("Starting Tag Defaults Edit No Prepend Test");
        HtmlPage page = getPage("/faces/ajax/ajaxTagDefaultsEditNoPrepend.xhtml");

        // First, we'll test to make sure the initial values come out right
        String out1 = ((HtmlElement)page.getHtmlElementById("out1")).asText();
        String echo1Out = ((HtmlElement)page.getHtmlElementById("echo1Out")).asText();
        String echo2Out = ((HtmlElement)page.getHtmlElementById("echo2Out")).asText();
        String echo3Out = ((HtmlElement)page.getHtmlElementById("echo3Out")).asText();
        String echo4Out = ((HtmlElement)page.getHtmlElementById("echo4Out")).asText();
        String out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
        String out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
        System.out.println("Initial values: "+out1+" "+out2+" "+out3+" "+echo1Out+" "+echo2Out+" "+echo3Out+" "+echo4Out);
        assertTrue("echo".equals(out1));
        assertTrue("echo".equals(out2));
        assertTrue("echo".equals(out3));
        assertTrue("".equals(echo1Out));
        assertTrue("".equals(echo2Out));
        assertTrue("".equals(echo3Out));
        assertTrue("".equals(echo4Out));

        // Next, enter data into first field
        HtmlTextInput echo1 = ((HtmlTextInput)page.getHtmlElementById("edit1"));
        echo1.focus();
        echo1.type("test1");
        echo1.blur();
        Thread.sleep(interval2);

        // Refresh the panel to check the listener fired
        HtmlSubmitInput refresh = page.getHtmlElementById("refresh");
        refresh.click();
        boolean status = false;
        for (int i = 0; i < iterate; i++) {
            out1 = ((HtmlElement)page.getHtmlElementById("out1")).asText();
            System.out.println("iteration "+i+": "+out1);
            if ("test1".equals(out1)) {
                echo1Out = ((HtmlElement)page.getHtmlElementById("echo1Out")).asText();
                assertTrue("test1".equals(echo1Out));
                out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be the same as above, unchanged by the ajax request
                assertTrue("echo".equals(out2));
                assertTrue("echo".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(interval);
            }
        }
        System.out.println("After echo1 values: "+echo1Out+" "+out1+" "+out2+" "+out3);
        assertTrue(status);

        // Next, enter data into second field
        HtmlTextInput echo2 = ((HtmlTextInput)page.getHtmlElementById("edit2"));
        echo2.focus();
        echo2.type("test2");
        echo2.blur();
        Thread.sleep(interval2);

        // Refresh the panel to check the listener fired
        refresh = page.getHtmlElementById("refresh");
        refresh.click();
        status = false;
        for (int i = 0; i < iterate; i++) {
            out1 = ((HtmlElement)page.getHtmlElementById("out1")).asText();
            System.out.println("iteration "+i+": "+out1);
            if ("test2".equals(out1)) {
                echo2Out = ((HtmlElement)page.getHtmlElementById("echo2Out")).asText();
                assertTrue("test2".equals(echo2Out));
                out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be the same as above, unchanged by the ajax request
                assertTrue("echo".equals(out2));
                assertTrue("echo".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(interval);
            }
        }
        System.out.println("After echo2 values: "+echo2Out+" "+out1+" "+out2+" "+out3);
        assertTrue(status);


        // Next, enter data into third field
        HtmlTextInput echo3 = ((HtmlTextInput)page.getHtmlElementById("edit3"));
        echo3.focus();
        echo3.type("test3");
        echo3.blur();
        Thread.sleep(interval2);

        // Refresh the panel to check the listener fired
        refresh = page.getHtmlElementById("refresh");
        refresh.click();
        status = false;
        for (int i = 0; i < iterate; i++) {
            out1 = ((HtmlElement)page.getHtmlElementById("out1")).asText();
            System.out.println("iteration "+i+": "+out1);
            if ("test3".equals(out1)) {
                echo3Out = ((HtmlElement)page.getHtmlElementById("echo3Out")).asText();
                assertTrue("test3".equals(echo3Out));
                out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be the same as above, unchanged by the ajax request
                assertTrue("echo".equals(out2));
                assertTrue("echo".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(interval);
            }
        }
        System.out.println("After echo3 values: "+echo3Out+" "+out1+" "+out2+" "+out3);
        assertTrue(status);

        // Next, enter data into the fourth field
        HtmlTextInput echo4 = ((HtmlTextInput)page.getHtmlElementById("edit4"));
        echo4.focus();
        echo4.type("test4");
        echo4.blur();
        Thread.sleep(interval2);

        // Refresh the panel to check the listener fired
        refresh = page.getHtmlElementById("refresh");
        refresh.click();
        status = false;
        for (int i = 0; i < iterate; i++) {
            out1 = ((HtmlElement)page.getHtmlElementById("out1")).asText();
            System.out.println("iteration "+i+": "+out1);
            if ("test4".equals(out1)) {
                echo4Out = ((HtmlElement)page.getHtmlElementById("echo4Out")).asText();
                assertTrue("test4".equals(echo4Out));
                out2 = ((HtmlElement)page.getHtmlElementById("out2")).asText();
                out3 = ((HtmlElement)page.getHtmlElementById("out3")).asText();
                // These should be the same as above, unchanged by the ajax request
                assertTrue("echo".equals(out2));
                assertTrue("echo".equals(out3));
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(interval);
            }
        }
        System.out.println("After echo4 values: "+echo4Out+" "+out1+" "+out2+" "+out3);
        assertTrue(status);

    }

}