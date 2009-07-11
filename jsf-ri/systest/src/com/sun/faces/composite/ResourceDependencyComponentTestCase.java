package com.sun.faces.composite;

import org.w3c.dom.NodeList;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.htmlunit.AbstractTestCase;


/**
 * Unit tests for Composite Components.
 */
public class ResourceDependencyComponentTestCase extends AbstractTestCase {


    public ResourceDependencyComponentTestCase() {
        this("ResourceDependencyComponentTestCase");
    }

    public ResourceDependencyComponentTestCase(String name) {
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
        return (new TestSuite(ResourceDependencyComponentTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }
    

    // -------------------------------------------------------------- Test Cases

    public void testForwardingToNextPageProcessesResourceDependencies() throws Exception {
        HtmlPage page = getPage("/faces/composite/resourceDependencyComponentUsingPage.xhtml");
        assertNrOfLinksPresent(page, 1);
        HtmlInput button = getInputContainingGivenId(page, "navigateAway");
        page = button.click();
        assertTrue(-1 != page.asText().indexOf("Next page"));
        assertNrOfLinksPresent(page, 1);
    }
    
    public void testStayingOnSamePageProcessesResourceDependencies() throws Exception {
        HtmlPage page = getPage("/faces/composite/resourceDependencyComponentUsingPage.xhtml");
        assertNrOfLinksPresent(page, 1);
        HtmlInput button = getInputContainingGivenId(page, "stay");
        page = button.click();
        assertTrue(-1 != page.asText().indexOf("Using page"));
        assertNrOfLinksPresent(page, 1);
    }

    private void assertNrOfLinksPresent(HtmlPage page, int number) {
        NodeList nodeList = page.getElementsByTagName("link");
        assertEquals(1, nodeList.getLength());
    }

}
