/*
 * $Id: TestConverters.java,v 1.5 2003/03/11 01:20:26 jvisvanathan Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestConverters.java

package com.sun.faces.convert;

import com.sun.faces.convert.ConverterFactoryImpl;

import java.util.Iterator;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import javax.faces.component.SelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIInput;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.MessageImpl;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;

import org.apache.cactus.WebRequest;

import com.sun.faces.JspFacesTestCase;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestConverters.java,v 1.5 2003/03/11 01:20:26 jvisvanathan Exp $
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

    }     

    public void beginConverters(WebRequest theRequest) {

    } 

    public void testConverters() {

        try {
            // create a dummy root for the tree.
            UIComponentBase root = new UIComponentBase() {
                public String getComponentType() { return "root"; }
            };
            root.setComponentId("root");

            testDateConverter(root);
            testNumberConverter(root);
            testBooleanConverter(root);
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

    public void testDateConverter(UIComponent root) throws ConverterException {
        System.out.println("Testing DateConverter");
        UIInput text = new UIInput();
        text.setComponentId("my_input_date");
        root.addChild(text);

        ConverterFactoryImpl converterFactory = new ConverterFactoryImpl();
        Converter converter = converterFactory.getConverter("Date");

        // date
        String stringToConvert = "Jan 1, 1967";
        Object obj = converter.getAsObject(getFacesContext(), text, 
            stringToConvert);
        assertTrue(obj instanceof java.util.Date);
        String str = converter.getAsString(getFacesContext(), text, obj);
        // make sure we end up with the same string we started with..
        assertTrue(str.equals(stringToConvert));

        // time
        converter = converterFactory.getConverter("Time");
        text = new UIInput();
        text.setComponentId("my_input_time");
        stringToConvert = "10:10:10 AM";
        obj = converter.getAsObject(getFacesContext(), text, stringToConvert);
        assertTrue(obj instanceof java.util.Date);
        str = converter.getAsString(getFacesContext(), text, obj);
        // make sure we end up with the same string we started with..
        assertTrue(str.equals(stringToConvert));

        // datetime
        converter = converterFactory.getConverter("DateTime");
        text = new UIInput();
        text.setComponentId("my_input_datetime");
        stringToConvert = "Jan 1, 1967 10:10:10 AM";
        obj = converter.getAsObject(getFacesContext(), text, stringToConvert);
        assertTrue(obj instanceof java.util.Date);
        str = converter.getAsString(getFacesContext(), text, obj);
        // make sure we end up with the same string we started with..
        assertTrue(str.equals(stringToConvert));
    }

    public void testNumberConverter(UIComponent root) throws ConverterException {
        System.out.println("Tesing NumberConverter");
        UIInput text = new UIInput();
        text.setComponentId("my_input_number");
        root.addChild(text);

        ConverterFactoryImpl converterFactory = new ConverterFactoryImpl();
        Converter converter = converterFactory.getConverter("Number");

        String stringToConvert = "99.9";
        Object obj = converter.getAsObject(getFacesContext(), text,
            stringToConvert);
        assertTrue(obj instanceof java.lang.Number);
        String str = converter.getAsString(getFacesContext(), text, obj);
        assertTrue(str.equals(stringToConvert));

    }

    public void testBooleanConverter(UIComponent root) throws ConverterException {
        System.out.println("Tesing BooleanConverter");
        UIInput text = new UIInput();
        text.setComponentId("my_input_boolean");
        root.addChild(text);

        ConverterFactoryImpl converterFactory = new ConverterFactoryImpl();
        Converter converter = converterFactory.getConverter("Boolean");

        String stringToConvert = "true";
        Object obj = converter.getAsObject(getFacesContext(), text,
            stringToConvert);
        assertTrue(obj instanceof java.lang.Boolean);

        String str = converter.getAsString(getFacesContext(), text, obj);

        assertTrue(str.equals(stringToConvert));

    }


} // end of class TestConverters
