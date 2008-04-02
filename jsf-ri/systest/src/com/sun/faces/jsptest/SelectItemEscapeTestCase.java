/*
 * $Id: SelectItemEscapeTestCase.java,v 1.2 2006/03/29 22:38:47 rlubke Exp $
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


import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/** <p>Verify that required validation occurrs for Select* components.</p> */

public class SelectItemEscapeTestCase extends AbstractTestCase {

    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public SelectItemEscapeTestCase(String name) {

        super(name);

    }

    // ---------------------------------------------------------- Public Methods


    /** Return the tests included in this test suite. */
    public static Test suite() {

        return (new TestSuite(SelectComponentValueTestCase.class));

    }


    /** Set up instance variables required by this test case. */
    public void setUp() throws Exception {

        super.setUp();

    }


    /** Tear down instance variables required by this test case. */
    public void tearDown() {

        super.tearDown();

    }

    // ------------------------------------------------------ Instance Variables

    // ------------------------------------------------- Individual Test Methods

    /** <p>Verify that the required validator works for SelectOne</p> */

    public void testSelectOneNoValue() throws Exception {

        HtmlPage page = getPage("/faces/selectItemEscape.jsp");

        assertTrue(-1 != page.asText().indexOf("menu1_Wayne &lt;Gretzky&gt;"));
        assertTrue(-1 != page.asText().indexOf("menu1_Bobby +Orr+"));
        assertTrue(-1 != page.asText().indexOf("menu1_Brad &amp;{Park}"));
        assertTrue(-1 != page.asText().indexOf("menu1_Brad &amp;{Park}"));

        assertTrue(-1 != page.asText().indexOf("menu2_Wayne &lt;Gretzky&gt;"));
        assertTrue(-1 != page.asText().indexOf("menu2_Bobby +Orr+"));
        assertTrue(-1 != page.asText().indexOf("menu2_Brad &amp;{Park}"));
        assertTrue(-1 != page.asText().indexOf("menu2_Brad &amp;{Park}"));

        assertTrue(-1 != page.asText().indexOf("menu3_Wayne <Gretzky>"));
        assertTrue(-1 != page.asText().indexOf("menu3_Bobby +Orr+"));
        assertTrue(-1 != page.asText().indexOf("menu3_Brad &{Park}"));
        assertTrue(-1 != page.asText().indexOf("menu3_Gordie &Howe&"));

    }

}

