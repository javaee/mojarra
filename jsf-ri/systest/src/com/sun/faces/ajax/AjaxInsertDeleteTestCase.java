package com.sun.faces.ajax;

import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlHorizontalRule;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AjaxInsertDeleteTestCase extends AbstractTestCase {

    public AjaxInsertDeleteTestCase(String name) {
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
        return (new TestSuite(AjaxInsertDeleteTestCase.class));
    }


    /*
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------------------ Test Methods


    public void testInsertDelete() throws Exception {

        HtmlPage page = getPage("/faces/ajax/ajaxInsertDelete.xhtml");

        assertNull(getBeforeHeading(page));
        assertNull(getAfterHeading(page));

        HtmlSubmitInput beforeButton = getBeforeButton(page);
        assertNotNull(beforeButton);
        page = beforeButton.click();

        HtmlHeading2 beforeHeading = getBeforeHeading(page);
        assertNotNull(beforeHeading);
        assertTrue(beforeHeading.getNextSibling() instanceof HtmlHorizontalRule);

        HtmlSubmitInput afterButton = getAfterButton(page);
        assertNotNull(afterButton);
        page = afterButton.click();

        HtmlHeading2 afterHeading = getAfterHeading(page);
        assertNotNull(afterHeading);
        assertTrue(afterHeading.getPreviousSibling() instanceof HtmlHorizontalRule);

        HtmlSubmitInput removeBefore = getRemoveBeforeButton(page);
        assertNotNull(removeBefore);
        page = removeBefore.click();

        assertNull(getBeforeHeading(page));
        assertNotNull(getAfterHeading(page));

        HtmlSubmitInput removeAfter = getRemoveAfterButton(page);
        assertNotNull(removeAfter);
        page = removeAfter.click();

        assertNull(getBeforeHeading(page));
        assertNull(getAfterHeading(page));

    }


    // --------------------------------------------------------  Private Methods


    private HtmlSubmitInput getBeforeButton(HtmlPage page) {

        return (HtmlSubmitInput) getInputContainingGivenId(page, "form1:before");

    }


    private HtmlSubmitInput getAfterButton(HtmlPage page) {

        return (HtmlSubmitInput) getInputContainingGivenId(page, "form1:after");

    }


    private HtmlSubmitInput getRemoveBeforeButton(HtmlPage page) {

        return (HtmlSubmitInput) getInputContainingGivenId(page, "form1:removeBefore");

    }


    private HtmlSubmitInput getRemoveAfterButton(HtmlPage page) {

        return (HtmlSubmitInput) getInputContainingGivenId(page, "form1:removeAfter");

    }


    private HtmlHeading2 getBeforeHeading(HtmlPage page) {

        return (HtmlHeading2) page.getElementById("h2before");

    }


    private HtmlHeading2 getAfterHeading(HtmlPage page) {

        return (HtmlHeading2) page.getElementById("h2after");

    }
}
