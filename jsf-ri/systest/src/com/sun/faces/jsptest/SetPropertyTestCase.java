/*
 * $Id: SetPropertyTestCase.java,v 1.6 2006/10/03 23:32:10 rlubke Exp $
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
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Test Case for f:setProperty.</p>
 */

public class SetPropertyTestCase extends AbstractTestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public SetPropertyTestCase(String name) {
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
        return (new TestSuite(SetPropertyTestCase.class));
    }


    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        super.tearDown();
    }


    // ------------------------------------------------- Individual Test Methods

    public void testSetPropertyPositive() throws Exception {
        HtmlPage page = getPage("/faces/jsp/jsp-setProperty-01.jsp");
        assertTrue(-1 != page.asText().indexOf("default value"));
        // press the button to submit the literal value
        List buttons = getAllElementsOfGivenClass(page, new ArrayList(), 
                HtmlSubmitInput.class);
        page = (HtmlPage) ((HtmlSubmitInput)buttons.get(0)).click();
        assertTrue(-1 != page.asText().indexOf("literal value"));
        
        // press the button to submit the expression value
        buttons = getAllElementsOfGivenClass(page, new ArrayList(), 
                HtmlSubmitInput.class);
        page = (HtmlPage) ((HtmlSubmitInput)buttons.get(1)).click();
        assertTrue(-1 != page.asText().indexOf("This is a String property"));
        
        // press the button to increment the property
        buttons = getAllElementsOfGivenClass(page, new ArrayList(), 
                HtmlSubmitInput.class);
        page = (HtmlPage) ((HtmlSubmitInput)buttons.get(2)).click();
        assertTrue(-1 != page.asText().indexOf("0"));
        
        // press the button to increment the property
        buttons = getAllElementsOfGivenClass(page, new ArrayList(), 
                HtmlSubmitInput.class);
        page = (HtmlPage) ((HtmlSubmitInput)buttons.get(2)).click();
        assertTrue(-1 != page.asText().indexOf("1"));

    }


}
