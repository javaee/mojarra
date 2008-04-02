/*
 * $Id: JspIntegrationTestCase.java,v 1.15 2006/03/14 16:33:24 edburns Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.jsptest;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.htmlunit.AbstractTestCase;
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
                          "[A]{ } [Z]");
        checkJspDynamic01("?mode=create&id=C1&value=[1]&javax.faces.ViewState=j_id2:j_id3",
                          "[A]{ [1] } [Z]");
        checkJspDynamic01("?mode=create&id=C2&value=[2]&javax.faces.ViewState=j_id2:j_id4",
                          "[A]{ [1] [2] } [Z]");
        checkJspDynamic01("?mode=create&id=C3&value=[3]&javax.faces.ViewState=j_id2:j_id5",
                          "[A]{ [1] [2] [3] } [Z]");
        checkJspDynamic01("?mode=delete&id=C2&javax.faces.ViewState=j_id2:j_id6",
                          "[A]{ [1] [3] } [Z]");

        checkJspDynamic00();
        client = new WebClient();
        checkJspDynamic01("",
                          "[A]{ } [Z]");

        // Check with children that do not have ids
	/********** PENDING(edburns): pending resolution of 5040235
        checkJspDynamic00();
        checkJspDynamic01("",
                          "[A] { } [Z]");
        checkJspDynamic01("?mode=create&value=[1]",
                          "[A] { [1] } [Z]");
        checkJspDynamic01("?mode=create&value=[2]",
                          "[A] { [1] [2] } [Z]");
        checkJspDynamic01("?mode=create&value=[3]",
                          "[A] { [1] [2] [3] } [Z]");
	**************/
        checkJspDynamic00();
        client = new WebClient();
        checkJspDynamic01("",
                          "[A]{ } [Z]");
    }


    // NOTE:  testJspIncludeXX tests are analogous to testJstlImportXX
    // tests, but exercise <jsp:include> instead of <c:import>.


    // Test importing JSPs with literal text
    public void testJspInclude01() throws Exception {

        checkJspInclude00();
        checkJspInclude01();
        checkJspInclude01();

        checkJspInclude00();
        checkJspInclude01();
        checkJspInclude01();

    }


    // Test importing JSPs with simple components
    public void testJspInclude02() throws Exception {

        checkJspInclude00();
        checkJspInclude02();
        checkJspInclude02();

        checkJspInclude00();
        checkJspInclude02();
        checkJspInclude02();

    }


    // Test selectively importing JSPs with simple components (explicit ids)
    public void testJspInclude03() throws Exception {

        // Check each individual case multiple times
        checkJspInclude00();
        checkJspInclude03();
        checkJspInclude03a();
        checkJspInclude03a();
        checkJspInclude03a();
        checkJspInclude00();
        checkJspInclude03();
        checkJspInclude03b();
        checkJspInclude03b();
        checkJspInclude03b();
        checkJspInclude00();
        checkJspInclude03();
        checkJspInclude03c();
        checkJspInclude03c();
        checkJspInclude03c();

        // Check cases in ascending order
        checkJspInclude00();
        checkJspInclude03();
        checkJspInclude03a();
        checkJspInclude03b();
        checkJspInclude03c();

        // Check cases in descending order
        checkJspInclude00();
        checkJspInclude03();
        checkJspInclude03c();
        checkJspInclude03b();
        checkJspInclude03a();

        // Check cases in random order
        checkJspInclude00();
        checkJspInclude03();
        checkJspInclude03b();
        checkJspInclude03a();
        checkJspInclude03c();

    }


    // Test selectively importing JSPs with simple components (naming container)
    public void testJspInclude04() throws Exception {

        // Check each individual case multiple times
        checkJspInclude00();
        checkJspInclude04();
        checkJspInclude04a();
        checkJspInclude04a();
        checkJspInclude04a();
        checkJspInclude00();
        checkJspInclude04();
        checkJspInclude04b();
        checkJspInclude04b();
        checkJspInclude04b();
        checkJspInclude00();
        checkJspInclude04();
        checkJspInclude04c();
        checkJspInclude04c();
        checkJspInclude04c();

        // Check cases in ascending order
        checkJspInclude00();
        checkJspInclude04();
        checkJspInclude04a();
        checkJspInclude04b();
        checkJspInclude04c();

        // Check cases in descending order
        checkJspInclude00();
        checkJspInclude04();
        checkJspInclude04c();
        checkJspInclude04b();
        checkJspInclude04a();

        // Check cases in random order
        checkJspInclude00();
        checkJspInclude04();
        checkJspInclude04b();
        checkJspInclude04a();
        checkJspInclude04c();

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
	String bodyText = getBodyText(page);
        assertTrue("Correct body element",
                     -1 != bodyText.indexOf(result));

    }


    // Check the reset page to force a new component tree
    private void checkJspInclude00() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jsp-include-00.jsp");
        assertEquals("Correct page title",
                     "jsp-include-00", page.getTitleText());

    }


    // Check imports with literal text
    private void checkJspInclude01() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jsp-include-01.jsp");
        assertEquals("Correct page title",
                     "jsp-include-01", page.getTitleText());
        assertEquals("Correct body element",
                     "[A] [B] [C] [D] [E]", getBodyText(page));

    }


    // Check imports with simple components
    private void checkJspInclude02() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jsp-include-02.jsp");
        assertEquals("Correct page title",
                     "jsp-include-02", page.getTitleText());
        assertEquals("Correct body element",
                     "[A][B][C][D][E]", getBodyText(page));

    }

    private void checkJspInclude03() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jsp-include-03.jsp");
        assertEquals("Correct page title",
                     "jsp-include-03", page.getTitleText());
        assertEquals("Correct body element",
                     "[1][2c][2x][3]", getBodyText(page));

    }



    // Check selective imports with simple components (explicit ids)
    private void checkJspInclude03a() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jsp-include-03.jsp?choose=a&javax.faces.ViewState=j_id2:j_id3");
        assertEquals("Correct page title",
                     "jsp-include-03", page.getTitleText());
        assertEquals("Correct body element",
                     "[1][2a][2z][3]", getBodyText(page));

    }


    // Check selective imports with simple components (explicit ids)
    private void checkJspInclude03b() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jsp-include-03.jsp?choose=b&javax.faces.ViewState=j_id2:j_id3");
        assertEquals("Correct page title",
                     "jsp-include-03", page.getTitleText());
        assertEquals("Correct body element",
                     "[1][2b][2y][3]", getBodyText(page));

    }


    // Check selective imports with simple components (explicit ids)
    private void checkJspInclude03c() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jsp-include-03.jsp?choose=c&javax.faces.ViewState=j_id2:j_id3");
        assertEquals("Correct page title",
                     "jsp-include-03", page.getTitleText());
        assertEquals("Correct body element",
                     "[1][2c][2x][3]", getBodyText(page));

    }

    private void checkJspInclude04() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jsp-include-04.jsp");
        assertEquals("Correct page title",
                     "jsp-include-04", page.getTitleText());
        assertEquals("Correct body element",
                     "[1][2c][2x][3]", getBodyText(page));

    }



    // Check selective imports with simple components (naming container)
    private void checkJspInclude04a() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jsp-include-04.jsp?choose=a&javax.faces.ViewState=j_id2:j_id3");
        assertEquals("Correct page title",
                     "jsp-include-04", page.getTitleText());
        assertEquals("Correct body element",
                     "[1][2a][2z][3]", getBodyText(page));

    }


    // Check selective imports with simple components (naming container)
    private void checkJspInclude04b() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jsp-include-04.jsp?choose=b&javax.faces.ViewState=j_id2:j_id3");
        assertEquals("Correct page title",
                     "jsp-include-04", page.getTitleText());
        assertEquals("Correct body element",
                     "[1][2b][2y][3]", getBodyText(page));

    }


    // Check selective imports with simple components (naming container)
    private void checkJspInclude04c() throws Exception {

        HtmlPage page = getPage("/faces/jsp/jsp-include-04.jsp?choose=c&javax.faces.ViewState=j_id2:j_id3");
        assertEquals("Correct page title",
                     "jsp-include-04", page.getTitleText());
        assertEquals("Correct body element",
                     "[1][2c][2x][3]", getBodyText(page));

    }


}
