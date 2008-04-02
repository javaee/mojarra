/*
 * $Id: TestConverterFactory.java,v 1.2 2003/02/04 16:19:19 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestConverterFactory.java

package com.sun.faces.convert;

import com.sun.faces.convert.ConverterFactoryImpl;

import java.util.Iterator;

import javax.faces.convert.Converter;
import javax.faces.convert.ConverterFactory;
import javax.faces.FacesException;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.ParameterCheck;
import org.apache.cactus.ServletTestCase;

/**
 *
 *  <B>TestConverterFactory</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestConverterFactory.java,v 1.2 2003/02/04 16:19:19 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestConverterFactory extends ServletTestCase {
//
// Protected Constants
//

//
// Class Variables
//

//
// Instance Variables
//
    private ConverterFactoryImpl converterFactory = null;

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestConverterFactory() {super("TestConverterFactory");}
    public TestConverterFactory(String name) {super(name);}
//
// Class methods
//

//
// General Methods
//

    public void testFactory() {
        converterFactory = new ConverterFactoryImpl();

        // 1. Verify "getConverter" returns the same Converter instance
        //    if called multiple times with the same identifier.
        //  
        Converter converter1 = converterFactory.getConverter("Date");
        Converter converter2 = converterFactory.getConverter("Date");
        assertTrue(converter1 == converter2);

        // 2. Verify "addConverter" adds instances.. /
        //      "getConverterIds returns iteration..
        //
        converterFactory.addConverter("Foo", converter1);
        converterFactory.addConverter("Bar", converter2);
        Iterator iter = converterFactory.getConverterIds();
        int i = 0;
        while (iter.hasNext()) {
            iter.next();
            i++;
        }
        assertTrue(i == 3);
        
    }

    public void testSpecCompliance() {
        converterFactory = new ConverterFactoryImpl();

        assertTrue(null != converterFactory.getConverter("Date"));
        assertTrue(null != converterFactory.getConverter("DateFormat"));
        assertTrue(null != converterFactory.getConverter("DateTime"));
        assertTrue(null != converterFactory.getConverter("Number"));
        assertTrue(null != converterFactory.getConverter("NumberFormat"));
        
    }

    public void testExceptions() {
        converterFactory = new ConverterFactoryImpl();
        DateConverter converter = new DateConverter();

        // 1. Verify "IllegalArg exception which occurs when attempting
        //    to add the same converter id.
        //
        boolean thrown = false;
        try {
            converterFactory.addConverter("foo", converter); 
            converterFactory.addConverter("foo", converter); 
        } catch (IllegalArgumentException ia) {
            thrown = true;
        }
        assertTrue(thrown);
    }
            
            
} // end of class TestConverterFactory
