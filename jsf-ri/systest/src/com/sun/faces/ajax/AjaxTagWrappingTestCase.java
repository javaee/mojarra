package com.sun.faces.ajax;

import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.*;


// RELEASE_PENDING - remove thread sleeps
// RELEASE_PENDING - completely refactor code

public class AjaxTagWrappingTestCase extends AbstractTestCase {

    // interval for test
    private static final int interval = 50;
    // number of times to try to read new value
    private static final int iterate = 10;
    // wait period for a fixed time
    private static final int interval2 = interval * 3;

    // total for each test = interval * iterate


    public AjaxTagWrappingTestCase(String name) {
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
        return (new TestSuite(AjaxTagWrappingTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    /*
       Test each component to see that it behaves correctly when used with an Ajax tag
     */
    public void testAjaxTagWrapping() throws Exception {
        HtmlPage page = getPage("/faces/ajax/ajaxTagWrapping.xhtml");
        System.out.println("Start ajax tag wrapping test");

        // First we'll check the first page was output correctly
        assertTrue(check(page, "out1", "0"));
        assertTrue(check(page, "checkedvalue", "false"));
        assertTrue(check(page, "outtext", ""));

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) page.getHtmlElementById("button1");
        page = (HtmlPage) button1.click();

        // Check that the ajax request succeeds - eventually.
        boolean status = false;
        for (int i = 0; i < iterate; i++) {
            String out1 = getText(page,"out1");
            System.out.println("iteration: "+i+"  out1: "+out1);
            if ("1".equals(out1)) {
                status = true;
                break;
            }
            Thread.sleep(interval);
        }
        assertTrue(status);
        System.out.println("Button Checked");

        // Check on the text field
        HtmlTextInput intext = ((HtmlTextInput)page.getHtmlElementById("intext"));
        intext.focus();
        intext.type("test");
        intext.blur();

        status = false;
        for (int i = 0; i < iterate; i++) {
            String outtext = getText(page,"outtext");
            System.out.println("iteration: "+i+"  outtext: "+outtext);
            if ("test".equals(outtext)) {
                status = true;
                break;
            }
            Thread.sleep(interval);
        }
        assertTrue(status);
        System.out.println("Text Checked");

        HtmlCheckBoxInput checked = ((HtmlCheckBoxInput)page.getHtmlElementById("checkbox"));
        page = (HtmlPage) checked.setChecked(true);

        status = false;
        for (int i = 0; i < iterate; i++) {
            String checkedvalue = getText(page,"checkedvalue");
            System.out.println("iteration: "+i+"  outtext: "+checkedvalue);
            if ("true".equals(checkedvalue)) {
                status = true;
                break;
            }
            synchronized (page) {
                page.wait(interval);
            }
        }
        assertTrue(status);
        System.out.println("Boolean Checkbox Checked");
    }

    private String getText(HtmlPage page, String element) {
        return ((HtmlElement)page.getHtmlElementById(element)).asText();
    }

    private boolean check(HtmlPage page, String element, String expected) {
        return expected.equals(getText(page, element));
    }

}