/*
 * $Id: TestConverterFactory.java,v 1.4 2003/02/20 22:49:54 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestConverterFactory.java

package com.sun.faces.convert;

import com.sun.faces.convert.ConverterFactoryImpl;

import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
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
 * @version $Id: TestConverterFactory.java,v 1.4 2003/02/20 22:49:54 ofung Exp $
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

    public void testByName() {

        converterFactory = new ConverterFactoryImpl();

        // 1. Verify that no by-class converters are registered initially
        assertEquals("No initial byClass converters",
                     0, countByClass());

        // 2. Register some test converters and validate the count
        Converter converter0 = new ConcreteConverter("java.sql.Types");
        Converter converter1 = new ConcreteConverter("java.sql.Date");
        Converter converter2 = new ConcreteConverter("java.util.Date");
        converterFactory.addConverter(java.sql.Types.class, converter0);
        converterFactory.addConverter(java.sql.Date.class, converter1);
        converterFactory.addConverter(java.util.Date.class, converter2);
        assertEquals("Correct number registered",
                     3, countByClass());
        Converter converter = null;

        // 3. Test exact-match returns and validate the count

        converter = converterFactory.getConverter(java.sql.Types.class);
        assertNotNull("Found converter0", converter);
        assertTrue("Correct converter0", converter == converter0);
        converter = converterFactory.getConverter(java.sql.Types.class);
        assertNotNull("Found converter0 again", converter);
        assertTrue("Correct converter0 again", converter == converter0);

        assertEquals("Correct number registered",
                     3, countByClass());

        converter = converterFactory.getConverter(java.sql.Date.class);
        assertNotNull("Found converter1", converter);
        assertTrue("Correct converter1", converter == converter1);

        assertEquals("Correct number registered",
                     3, countByClass());

        converter = converterFactory.getConverter(java.util.Date.class);
        assertNotNull("Found converter2", converter);
        assertTrue("Correct converter2", converter == converter2);

        assertEquals("Correct number registered",
                     3, countByClass());

        // 4. Test inherit-from-interface returns and validate the count

        // PENDING(craigmcc):  add test case for this

        // 5. Test inherit-from-superclass and validate the count

        converter = converterFactory.getConverter(ConcreteDate.class);
        assertNotNull("Found converter1 indirectly", converter);
        assertTrue("Correct converter1 indirectly", converter == converter1);

        assertEquals("Correct number registered",
                     4, countByClass()); // New one implicitly registered

        // 6. Test inherit-from-superclass-interface and validate the count

        // PENDING(craigmcc):  add test case for this

        // 7. Test an unsupported class

        converter = converterFactory.getConverter(java.lang.Number.class);
        assertNull("No converter for unregistered class", converter);

    }


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


    private int countByClass() {
        int n = 0;
        Iterator keys = converterFactory.getConverterClasses();
        while (keys.hasNext()) {
            keys.next();
            n++;
        }
        return (n);
    }
            
            
} // end of class TestConverterFactory


class ConcreteConverter implements Converter {

    public ConcreteConverter(String id) {
        this.id = id;
    }

    private String id;
    public String getId() {
        return (this.id);
    }

    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) throws ConverterException {
        return (value);
    }


    public String getAsString(FacesContext context, UIComponent component,
                              Object value) throws ConverterException {
        if (value == null) {
            return (null);
        } else if (value instanceof String) {
            return ((String) value);
        } else {
            return (value.toString());
        }

    }


}


class ConcreteDate extends java.sql.Date {

    public ConcreteDate() {
        super(0);
    }

}
