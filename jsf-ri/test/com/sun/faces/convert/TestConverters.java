/*
 * $Id: TestConverters.java,v 1.9 2003/08/22 19:25:14 rlubke Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestConverters.java

package com.sun.faces.convert;

import com.sun.faces.application.ApplicationFactoryImpl;

import java.util.Iterator;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UISelectItems;
import javax.faces.component.UICommand;
import javax.faces.component.base.UIComponentBase;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIInput;
import javax.faces.component.base.UIComponentBase;
import javax.faces.component.base.UIInputBase;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.application.MessageImpl;

import org.apache.cactus.WebRequest;

import com.sun.faces.JspFacesTestCase;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestConverters.java,v 1.9 2003/08/22 19:25:14 rlubke Exp $
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
            UIComponentBase root = new UIComponentBase() {
                public String getComponentType() { return "root"; }
            };
            root.setId("root");

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

    public void testDateConverter(UIComponent root) throws ConverterException,
        InstantiationException, IllegalAccessException, ClassNotFoundException {
        System.out.println("Testing DateConverter");
        UIInput text = new UIInputBase();
        text.setId("my_input_date");
        root.getChildren().add(text);

        Converter converter = null;
        converter = application.createConverter("Date");

        // date
        String stringToConvert = "Jan 1, 1967";
        Object obj = converter.getAsObject(getFacesContext(), text, 
            stringToConvert);
        assertTrue(obj instanceof java.util.Date);
        String str = converter.getAsString(getFacesContext(), text, obj);
        // make sure we end up with the same string we started with..
        assertTrue(str.equals(stringToConvert));

        // time
        converter = application.createConverter("Time");
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
        text = new UIInputBase();
        text.setId("my_input_datetime");
        stringToConvert = "Jan 1, 1967 10:10:10 AM";
        obj = converter.getAsObject(getFacesContext(), text, stringToConvert);
        assertTrue(obj instanceof java.util.Date);
        str = converter.getAsString(getFacesContext(), text, obj);
        // make sure we end up with the same string we started with..
        assertTrue(str.equals(stringToConvert));
    }

    public void testNumberConverter(UIComponent root) throws ConverterException,
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

    public void testBooleanConverter(UIComponent root) throws ConverterException,
        InstantiationException, IllegalAccessException, ClassNotFoundException {
        System.out.println("Tesing BooleanConverter");
        UIInput text = new UIInputBase();
        text.setId("my_input_boolean");
        root.getChildren().add(text);

        Converter converter = application.createConverter("Boolean");

        String stringToConvert = "true";
        Object obj = converter.getAsObject(getFacesContext(), text,
            stringToConvert);
        assertTrue(obj instanceof java.lang.Boolean);

        String str = converter.getAsString(getFacesContext(), text, obj);

        assertTrue(str.equals(stringToConvert));

    }


} // end of class TestConverters
