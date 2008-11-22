package com.sun.faces.ajax;

import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;


// RELEASE_PENDING - remove thread sleeps
// RELEASE_PENDING - completely refactor code

public class AjaxTagWrappingTestCase extends AbstractTestCase {

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
        getPage("/faces/ajax/ajaxTagWrapping.xhtml");
        System.out.println("Start ajax tag wrapping test");

        // First we'll check the first page was output correctly
        assertTrue(check("out1", "0"));
        assertTrue(check("checkedvalue", "false"));
        assertTrue(check("outtext", ""));

        // Submit the ajax request
        HtmlSubmitInput button1 = (HtmlSubmitInput) lastpage.getHtmlElementById("button1");
        lastpage = (HtmlPage) button1.click();

        // Check that the ajax request succeeds - eventually.
        assertTrue(check("out1","1"));
        System.out.println("Button Checked");

        // Check on the text field
        HtmlTextInput intext = ((HtmlTextInput)lastpage.getHtmlElementById("intext"));
        intext.focus();
        intext.type("test");
        intext.blur();

        assertTrue(check("outtext","test"));
        System.out.println("Text Checked");

        // Check on the checkbox
        HtmlCheckBoxInput checked = ((HtmlCheckBoxInput)lastpage.getHtmlElementById("checkbox"));
        checked.setChecked(true);

        assertTrue(check("checkedvalue","true"));
        System.out.println("Boolean Checkbox Checked");
    }

}