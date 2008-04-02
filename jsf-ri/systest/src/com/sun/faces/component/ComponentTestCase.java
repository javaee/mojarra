package com.sun.faces.component;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ComponentTestCase extends AbstractTestCase {

    // ------------------------------------------------------------ Constructors


    public ComponentTestCase(String name) {

        super(name);

    }

    // ---------------------------------------------------------- Public Methods


    /** Return the tests included in this test suite. */
    public static Test suite() {

        return (new TestSuite(ComponentTestCase.class));

    }


    /** Set up instance variables required by this test case. */
    public void setUp() throws Exception {

        super.setUp();

    }


    /** Tear down instance variables required by this test case. */
    public void tearDown() {

        super.tearDown();

    }


    /**
     * Added for issue 212.
     * Ensure no exception when restoring state containing
     * transient components.
     *
     * @throws Exception
     */
    public void testComponentTransienceRestoration() throws Exception {

        HtmlPage page = getPage("/faces/component02.jsp");
        HtmlSubmitInput submit = (HtmlSubmitInput)
              getInputContainingGivenId(page, "submit");
        try {
            submit.click();
        } catch (Exception ioe) {
            fail("No exception should have been thrown: " +
                 ioe.getMessage());
        }

    }

} // end of class PathTestCase
