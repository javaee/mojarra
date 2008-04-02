/*
 * $Id: MissingActionListenerMethodTestCase.java,v 1.2 2007/02/01 21:57:47 jdlee Exp $
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


import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.sun.faces.htmlunit.AbstractTestCase;


/**
 * 
 * @author Jason Lee
 */

public class MissingActionListenerMethodTestCase extends AbstractTestCase {
    // ------------------------------------------------------------ Constructors
    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public MissingActionListenerMethodTestCase(String name) {
        super(name);
    }

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(MissingActionListenerMethodTestCase.class));
    }

    // ------------------------------------------------- Individual Test Methods

    public void testMissingActionListenerMethod() throws Exception {
        HtmlPage page = getPage("/faces/jsp/testMissingActionListenerMethod.jsp");
        HtmlSubmitInput button = (HtmlSubmitInput) page.getHtmlElementById("testForm:testButton");
        try {
            button.click();
        } catch (Exception e) {
            Assert.fail();
        }
    }
}