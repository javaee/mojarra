/*
 * $Id: JstlIntegrationTestCase.java,v 1.2 2003/09/05 18:57:06 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.jsptest;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.htmlunit.AbstractTestCase;
import java.net.URL;
import java.util.Iterator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;



/**
 * <p>Test Case for JSTL Interoperability.</p>
 */

public class JstlIntegrationTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public JstlIntegrationTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------- Overall Test Methods


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
        return (new TestSuite(JstlIntegrationTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Components Inside Conditional
    public void testJstlIf01() throws Exception {

        // Check the "true" case multiple times in a row
        checkJstlIf00();
        checkJstlIf01a();
        checkJstlIf01a();
        checkJstlIf01a();
        checkJstlIf01a();

        // Check the "false case multiple times in a row
        checkJstlIf00();
        checkJstlIf01b();
        checkJstlIf01b();
        checkJstlIf01b();
        checkJstlIf01b();

        // Check alternating access to the same page (first pattern)
        checkJstlIf00();
        checkJstlIf01a();
        checkJstlIf01b();
        checkJstlIf01a();
        checkJstlIf01b();

        // Check alternating access to the same page (second pattern)
        checkJstlIf00();
        checkJstlIf01b();
        checkJstlIf01a();
        checkJstlIf01b();
        checkJstlIf01a();

    }


    // Components and facets inside conditional
    public void testJstlIf02() throws Exception {

        // Check each style sequentially
        checkJstlIf00();
        checkJstlIf02a();
        checkJstlIf02a();
        checkJstlIf02a();
        checkJstlIf00();
        checkJstlIf02b();
        checkJstlIf02b();
        checkJstlIf02b();
        checkJstlIf00();
        checkJstlIf02c();
        checkJstlIf02c();
        checkJstlIf02c();
        checkJstlIf00();
        checkJstlIf02d();
        checkJstlIf02d();
        checkJstlIf02d();
        checkJstlIf00();
        checkJstlIf02e();
        checkJstlIf02e();
        checkJstlIf02e();

        // Check each style in ascending order
        checkJstlIf00();
        checkJstlIf02a();
        checkJstlIf02b();
        checkJstlIf02c();
        checkJstlIf02d();
        checkJstlIf02e();

        // Check each style in descending order
        checkJstlIf00();
        checkJstlIf02e();
        checkJstlIf02d();
        checkJstlIf02c();
        checkJstlIf02b();
        checkJstlIf02a();

        // Check each style in a more random order
        checkJstlIf00();
        checkJstlIf02c();
        checkJstlIf02e();
        checkJstlIf02a();
        checkJstlIf02d();
        checkJstlIf02b();

    }


    // Component and Template Text Inside Conditional
    public void testJstlIf03() throws Exception {

        // Check the "true" case multiple times in a row
        checkJstlIf00();
        checkJstlIf03a();
        checkJstlIf03a();
        checkJstlIf03a();
        checkJstlIf03a();

        // Check the "false case multiple times in a row
        checkJstlIf00();
        checkJstlIf03b();
        checkJstlIf03b();
        checkJstlIf03b();
        checkJstlIf03b();

        // Check alternating access to the same page (first pattern)
        checkJstlIf00();
        checkJstlIf03a();
        checkJstlIf03b();
        checkJstlIf03a();
        checkJstlIf03b();

        // Check alternating access to the same page (second pattern)
        checkJstlIf00();
        checkJstlIf03b();
        checkJstlIf03a();
        checkJstlIf03b();
        checkJstlIf03a();

    }


    // Renders-Child Component Inside Conditional (no nested template text) (
    public void testJstlIf04() throws Exception {

        // Check the "true" case multiple times in a row
        checkJstlIf00();
        checkJstlIf04a();
        checkJstlIf04a();
        checkJstlIf04a();
        checkJstlIf04a();

        // Check the "false case multiple times in a row
        checkJstlIf00();
        checkJstlIf04b();
        checkJstlIf04b();
        checkJstlIf04b();
        checkJstlIf04b();

        // Check alternating access to the same page (first pattern)
        checkJstlIf00();
        checkJstlIf04a();
        checkJstlIf04b();
        checkJstlIf04a();
        checkJstlIf04b();

        // Check alternating access to the same page (second pattern)
        checkJstlIf00();
        checkJstlIf04b();
        checkJstlIf04a();
        checkJstlIf04b();
        checkJstlIf04a();

    }


    // --------------------------------------------------------- Private Methods


    // Check the reset page to force a new component tree
    private void checkJstlIf00() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jstl-if-00.jsp");
        assertEquals("Correct page title",
                     "jstl-if-00", page.getTitleText());

    }


    // Check the actual conditional page with a "true" flag
    private void checkJstlIf01a() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jstl-if-01.jsp?cond=true");
        assertEquals("Correct page title",
                     "jstl-if-01", page.getTitleText());
        assertEquals("Correct body element",
                     "[First] [Second] [Third]", getBodyText(page));

    }


    // Check the actual conditional page with a "false" flag
    private void checkJstlIf01b() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jstl-if-01.jsp?cond=false");
        assertEquals("Correct page title",
                     "jstl-if-01", page.getTitleText());
        assertEquals("Correct body element",
                     "[First] [Third]", getBodyText(page));

    }

    // Check the actual facet page with true/true/true flags
    private void checkJstlIf02a() throws Exception {

        HtmlPage page = getPage
            ("/faces/jsp/jstl-if-02.jsp?component=true&header=true&footer=true");
        assertEquals("Correct page title",
                     "jstl-if-02", page.getTitleText());
        assertEquals("Correct body element",
                     "[First] [Header] [Second] [Footer] [Third]",
                     getBodyText(page));

    }


    // Check the actual facet page with true/true/false flags
    private void checkJstlIf02b() throws Exception {

        HtmlPage page = getPage
            ("/faces/jsp/jstl-if-02.jsp?component=true&header=true&footer=false");
        assertEquals("Correct page title",
                     "jstl-if-02", page.getTitleText());
        assertEquals("Correct body element",
                     "[First] [Header] [Second] [] [Third]",
                     getBodyText(page));

    }


    // Check the actual facet page with true/false/true flags
    private void checkJstlIf02c() throws Exception {

        HtmlPage page = getPage
            ("/faces/jsp/jstl-if-02.jsp?component=true&header=false&footer=true");
        assertEquals("Correct page title",
                     "jstl-if-02", page.getTitleText());
        assertEquals("Correct body element",
                     "[First] [] [Second] [Footer] [Third]",
                     getBodyText(page));

    }


    // Check the actual facet page with true/false/false flags
    private void checkJstlIf02d() throws Exception {

        HtmlPage page = getPage
            ("/faces/jsp/jstl-if-02.jsp?component=true&header=false&footer=false");
        assertEquals("Correct page title",
                     "jstl-if-02", page.getTitleText());
        assertEquals("Correct body element",
                     "[First] [] [Second] [] [Third]",
                     getBodyText(page));

    }


    // Check the actual facet page with false/true/true flags
    private void checkJstlIf02e() throws Exception {

        HtmlPage page = getPage
            ("/faces/jsp/jstl-if-02.jsp?component=false&header=true&footer=true");
        assertEquals("Correct page title",
                     "jstl-if-02", page.getTitleText());
        assertEquals("Correct body element",
                     "[First] [Third]",
                     getBodyText(page));

    }


    // Check the actual template page with a "true" flag
    private void checkJstlIf03a() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jstl-if-03.jsp?cond=true");
        assertEquals("Correct page title",
                     "jstl-if-03", page.getTitleText());
        assertEquals("Correct body element",
                     "[1] [2] [3] [4] [5]", getBodyText(page));

    }


    // Check the actual conditional page with a "false" flag
    private void checkJstlIf03b() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jstl-if-03.jsp?cond=false");
        assertEquals("Correct page title",
                     "jstl-if-03", page.getTitleText());
        assertEquals("Correct body element",
                     "[1] [5]", getBodyText(page));

    }


    // Check the actual conditional page with a "true" flag
    private void checkJstlIf04a() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jstl-if-04.jsp?cond=true");
        assertEquals("Correct page title",
                     "jstl-if-04", page.getTitleText());
        assertEquals("Correct body element",
                     "[1] [2] [3] { [4a] [4b] [4c] } [5] [6] [7]",
                     getBodyText(page));

    }


    // Check the actual conditional page with a "false" flag
    private void checkJstlIf04b() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jstl-if-04.jsp?cond=false");
        assertEquals("Correct page title",
                     "jstl-if-04", page.getTitleText());
        assertEquals("Correct body element",
                     "[1] [7]", getBodyText(page));

    }


}
