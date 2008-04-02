/*
 * $Id: JspIntegrationTestCase.java,v 1.1 2003/09/24 23:58:53 craigmcc Exp $
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
 * <p>Test Case for JSP Interoperability.</p>
 */

public class JspIntegrationTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public JspIntegrationTestCase(String name) {
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
        return (new TestSuite(JspIntegrationTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods


    // Test dynamically adding and removing components
    public void testJspDynamic01() throws Exception {

        // Check with children that have explicit ids
        checkJspDynamic00();
        checkJspDynamic01("",
                          "[A] { } [Z]");
        checkJspDynamic01("?mode=create&id=C1&value=[1]",
                          "[A] { [1] } [Z]");
        checkJspDynamic01("?mode=create&id=C2&value=[2]",
                          "[A] { [1] [2] } [Z]");
        checkJspDynamic01("?mode=create&id=C3&value=[3]",
                          "[A] { [1] [2] [3] } [Z]");
        checkJspDynamic01("?mode=delete&id=C2",
                          "[A] { [1] [3] } [Z]");

        checkJspDynamic00();
        /* PENDING(craigmcc) - this should have switched pages and deleted
           the previous component tree.  However, the following check fails
           because the old children are still present.  Need to investigate
           why that happens, because it means we can't run the remainder
           of the tests in this method.
        checkJspDynamic01("",
                          "[A] { } [Z]");

        // Check with children that do not have ids
        checkJspDynamic00();
        checkJspDynamic01("",
                          "[A] { } [Z]");
        checkJspDynamic01("?mode=create&value=[1]",
                          "[A] { [1] } [Z]");
        checkJspDynamic01("?mode=create&value=[2]",
                          "[A] { [1] [2] } [Z]");
        checkJspDynamic01("?mode=create&value=[3]",
                          "[A] { [1] [2] [3] } [Z]");
        checkJspDynamic00();
        checkJspDynamic01("",
                          "[A] { } [Z]");
        */

    }


    // --------------------------------------------------------- Private Methods


    // Check the reset page to force a new component tree
    private void checkJspDynamic00() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jsp-dynamic-00.jsp");
        assertEquals("Correct page title",
                     "jsp-dynamic-00", page.getTitleText());

    }


    // Check the result of requesting the specified page
    private void checkJspDynamic01(String query, String result)
        throws Exception {

        HtmlPage page = getPage("/faces/jsp/jsp-dynamic-01.jsp" + query);
        assertEquals("Correct page title",
                     "jsp-dynamic-01", page.getTitleText());
        assertEquals("Correct body element",
                     result, getBodyText(page));

    }


}
