/*
 * $Id: TlvTestCase.java,v 1.2 2005/08/22 22:11:25 ofung Exp $
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

package com.sun.faces.taglib;


import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


public class TlvTestCase extends TestCase {


    // ------------------------------------------------------ Instance Variables


    public TlvTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() { }



    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(TlvTestCase.class));

    }


    // Tear down instance variables required by this test case.
    public void tearDown() {

    }


    // ------------------------------------------------- Individual Test Methods


    public void testDesignTime() {

        java.beans.Beans.setDesignTime(true);
        TestHtmlBasicValidator hbv = new TestHtmlBasicValidator();
        org.xml.sax.helpers.DefaultHandler handler  =  hbv.getSAXHandler();
        assertTrue(null == handler);

        java.beans.Beans.setDesignTime(false);
        handler =  hbv.getSAXHandler();
        assertTrue(null != handler);

        java.beans.Beans.setDesignTime(true);
        TestCoreValidator cv = new TestCoreValidator();
        handler  =  cv.getSAXHandler();
        assertTrue(null == handler);

        java.beans.Beans.setDesignTime(false);
        handler  =  cv.getSAXHandler();
        assertTrue(null != handler);
    }

}
