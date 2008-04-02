/*
 * $Id: TestConverters.java,v 1.11 2003/08/25 22:35:48 horwat Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestConverters.java

package com.sun.faces.convert;

import com.sun.faces.application.ApplicationFactoryImpl;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.base.UIInputBase;
import javax.faces.component.base.UIViewRootBase;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.ConverterException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;

import org.apache.cactus.WebRequest;

import com.sun.faces.JspFacesTestCase;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestConverters.java,v 1.11 2003/08/25 22:35:48 horwat Exp $
 * 
 *
 */

public class TestConverters extends JspFacesTestCase
{
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private FacesContextFactory  facesContextFactory = null;

    // Attribute Instance Variables
    // Relationship Instance Variables

    protected Application application;
    //
    // Constructors and Initializers    
    //

    public TestConverters() {super("TestConverters");}
    public TestConverters(String name) {super(name);}

    //
    // Class methods
    //

    //
    // Methods from TestCase
    //
    public void setUp() {
	super.setUp();
        ApplicationFactory aFactory = 
	    (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = aFactory.getApplication();
    }     

    public void beginConverters(WebRequest theRequest) {

    } 

    public void testConverters() {

        try {
            // create a dummy root for the tree.
            UIViewRoot root = new UIViewRootBase();
            root.setId("root");

            testDateConverter(root);
            testNumberConverter(root);
            testBooleanConverter(root);
            testConverterInheritance(root);
            //assertTrue(verifyExpectedOutput());
        } catch (Throwable t) {
            t.printStackTrace();
            assertTrue(false);
            return;
        }
    }
    
    //
    // General Methods
    //

    public void testDateConverter(UIViewRoot root) throws ConverterException,
        InstantiationException, IllegalAccessException, ClassNotFoundException {
        System.out.println("Testing DateConverter");
        UIInput text = new UIInputBase();
        text.setId("my_input_date");
        root.getChildren().add(text);

        Converter converter = null;
        converter = application.createConverter("DateTime");

        // date
        String stringToConvert = "Jan 1, 1967";
        Object obj = converter.getAsObject(getFacesContext(), text, 
            stringToConvert);
        assertTrue(obj instanceof java.util.Date);
        String str = converter.getAsString(getFacesContext(), text, obj);
        // make sure we end up with the same string we started with..
        assertTrue(str.equals(stringToConvert));

        // time
        converter = application.createConverter("DateTime");
        ((DateTimeConverter)converter).setType("time");
        text = new UIInputBase();
        text.setId("my_input_time");
        stringToConvert = "10:10:10 AM";
        obj = converter.getAsObject(getFacesContext(), text, stringToConvert);
        assertTrue(obj instanceof java.util.Date);
        str = converter.getAsString(getFacesContext(), text, obj);
        // make sure we end up with the same string we started with..
        assertTrue(str.equals(stringToConvert));

        // datetime
        converter = application.createConverter("DateTime");
        ((DateTimeConverter)converter).setType("both");
        text = new UIInputBase();
        text.setId("my_input_datetime");
        stringToConvert = "Jan 1, 1967 10:10:10 AM";
        obj = converter.getAsObject(getFacesContext(), text, stringToConvert);
        assertTrue(obj instanceof java.util.Date);
        str = converter.getAsString(getFacesContext(), text, obj);
        // make sure we end up with the same string we started with..
        assertTrue(str.equals(stringToConvert));
    }

    public void testNumberConverter(UIViewRoot root) throws ConverterException,
        InstantiationException, IllegalAccessException, ClassNotFoundException {
        System.out.println("Tesing NumberConverter");
        UIInput text = new UIInputBase();
        text.setId("my_input_number");
        root.getChildren().add(text);

        Converter converter = application.createConverter("Number");

        String stringToConvert = "99.9";
        Object obj = converter.getAsObject(getFacesContext(), text,
            stringToConvert);
        assertTrue(obj instanceof java.lang.Number);
        String str = converter.getAsString(getFacesContext(), text, obj);
        assertTrue(str.equals(stringToConvert));

    }

    public void testBooleanConverter(UIViewRoot root) throws ConverterException,
        InstantiationException, IllegalAccessException, ClassNotFoundException {
        System.out.println("Tesing BooleanConverter");
        UIInput text = new UIInputBase();
        text.setId("my_input_boolean");
        root.getChildren().add(text);

        Converter converter = application.createConverter(java.lang.Boolean.class);

        String stringToConvert = "true";
        Object obj = converter.getAsObject(getFacesContext(), text,
            stringToConvert);
        assertTrue(obj instanceof java.lang.Boolean);

        String str = converter.getAsString(getFacesContext(), text, obj);

        assertTrue(str.equals(stringToConvert));

    }

    /**
     * Test to verify that a class that registers itself is properly found
     * using the search mechanism. The J2SE classes are used for their
     * inheritance hierarchy to test that converters registered to
     * interfaces and superclasses are properly found.
     *
     * This test is meant for inheritance lookup only.
     *
     */
    public void testConverterInheritance(UIViewRoot root) throws ConverterException,
        InstantiationException, IllegalAccessException, ClassNotFoundException {
        System.out.println("Testing ConverterInheritance");

        Converter converter;
        UIInput text = new UIInputBase();
        text.setId("my_date_converter");
        root.getChildren().add(text);

        //java.lang.Integer extends java.lang.Number. 
        //Test to show that the standard converter registered to 
        //java.lang.Integer should chosen over the inherited 
        //java.lang.Number converter
        application.addConverter(java.lang.Number.class, "javax.faces.convert.NumberConverter");
        converter = application.createConverter(java.lang.Integer.class);
        assertTrue(converter!=null);
        assertTrue(converter instanceof javax.faces.convert.IntegerConverter);


        //java.sql.Date extends java.util.Date
        //Test to find converter registered to java.util.Date
        application.addConverter(java.util.Date.class, "javax.faces.convert.DateTimeConverter");
        converter = null;
        converter = application.createConverter(java.sql.Date.class);
        assertTrue(converter!=null);

        //java.util.HashSet is a subclass of java.util.AbstractSet which is
        //a subclass of java.util.AbstractCollection 
        //Test to find the converter registered to java.util.AbstractCollection
        application.addConverter(java.util.AbstractCollection.class, "javax.faces.convert.DateTimeConverter");
        converter = null;
        converter = application.createConverter(java.util.HashSet.class);
        assertTrue(converter!=null);


        //java.lang.String implements java.lang.CharSequence
        //Test to find the converter registered to java.lang.CharSequence
        application.addConverter(java.lang.CharSequence.class, "javax.faces.convert.CharacterConverter");
        converter = null;
        converter = application.createConverter(java.lang.String.class);
        assertTrue(converter!=null);

        //java.text.StringCharacterIterator implements 
        //java.text.CharacterIterator which has a super-interface
        //java.lang.Cloneable
        //Test to find the converter registered to java.lang.Cloneable
        application.addConverter(java.lang.Cloneable.class, "javax.faces.convert.CharacterConverter");
        converter = null;
        converter = application.createConverter(java.text.StringCharacterIterator.class);
        assertTrue(converter!=null);
    }


} // end of class TestConverters
