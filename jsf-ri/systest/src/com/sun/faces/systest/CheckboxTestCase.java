package com.sun.faces.systest;

import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
  *
 */
public class CheckboxTestCase extends AbstractTestCase {

    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public CheckboxTestCase(String name) {
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
        return (new TestSuite(CheckboxTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------------ Test Methods


    /**
     * Added for issue 904.
     */
    public void testBooleanCheckboxSubmittedValue() throws Exception {

        HtmlPage page = getPage("/faces/standard/checkboxSubmittedValue.xhtml");
        HtmlCheckBoxInput box1 = (HtmlCheckBoxInput) page.getHtmlElementById("box1");
        assertNotNull(box1);
        page = (HtmlPage) box1.click();
        HtmlCheckBoxInput box2 = (HtmlCheckBoxInput) page.getHtmlElementById("box2");
        assertNotNull(box2);
        HtmlPage newPage = (HtmlPage) box2.click();
        box1 = (HtmlCheckBoxInput) newPage.getHtmlElementById("box1");
        assertTrue(box1.isChecked());
        box2 = (HtmlCheckBoxInput) newPage.getHtmlElementById("box2");
        assertTrue(box2.isChecked());

    }
}
