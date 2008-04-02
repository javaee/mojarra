/*
 * $Id: TlvTestCase.java,v 1.1 2004/12/02 18:42:29 rogerk Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
