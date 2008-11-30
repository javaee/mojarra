package com.sun.faces.render;

import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.gargoylesoftware.htmlunit.html.*;

// RELEASE_PENDING - completely refactor code

public class CommandButtonTestCase extends AbstractTestCase {

    public CommandButtonTestCase(String name) {
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
        return (new TestSuite(CommandButtonTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    public void testCommandButtonButton() throws Exception {
        getPage("/faces/render/commandButtonButton.xhtml");
        System.out.println("Start command Button type=button test");
        // First we'll check the first page was output correctly
        assertTrue(check("out1","0"));
        assertTrue(check("outside","1"));

        // Submit the ajax tagged request
        HtmlButtonInput button = (HtmlButtonInput) lastpage.getHtmlElementById("button1");
        lastpage = (HtmlPage) button.click();

        // Check that the ajax request succeeds
        assertTrue(check("out1","2"));

        // Check that the request did NOT update the rest of the page.
        assertTrue(check("outside","1"));

        // Submit the onclick enhanced request - with no return false
        button = (HtmlButtonInput) lastpage.getHtmlElementById("button2");
        lastpage = (HtmlPage) button.click();

        // Check that the ajax request succeeds
        assertTrue(check("out1","3"));

        // Check that the request did NOT update the rest of the page.
        assertTrue(check("outside","1"));

        fail();
    }
}