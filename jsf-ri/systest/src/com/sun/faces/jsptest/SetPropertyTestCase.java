/*
 * $Id: SetPropertyTestCase.java,v 1.1 2005/07/25 18:40:26 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.jsptest;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.faces.htmlunit.AbstractTestCase;
import java.util.ArrayList;
import java.util.List;
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
