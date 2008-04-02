/*
 * $Id: TestConverters.java,v 1.20 2003/10/15 16:59:18 jvisvanathan Exp $
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
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;

import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.ConverterException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;

import org.apache.cactus.WebRequest;

import com.sun.faces.JspFacesTestCase;
import com.sun.faces.util.Util;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestConverters.java,v 1.20 2003/10/15 16:59:18 jvisvanathan Exp $
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
        UIViewRoot viewRoot = new UIViewRoot();
	viewRoot.setViewId("viewId");
	getFacesContext().setViewRoot(viewRoot);
    }     

    public void beginConverters(WebRequest theRequest) {

    } 

    public void testConverters() {

        try {
            // create a dummy root for the tree.
            UIViewRoot root = new UIViewRoot();
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

    public static class TestBean extends Object {
	boolean [] booleans = null;
	public boolean [] getBooleans() {
	    return booleans;
	}
	public void setBooleans(boolean [] newBooleans) {
	    booleans = newBooleans;
	}

	byte [] bytes = null;
	public byte [] getBytes() {
	    return bytes;
	}
	public void setBytes(byte [] newBytes) {
	    bytes = newBytes;
	}

	char [] chars = null;
	public char [] getChars() {
	    return chars;
	}
	public void setChars(char [] newChars) {
	    chars = newChars;
	}

	short [] shorts = null;
	public short [] getShorts() {
	    return shorts;
	}
	public void setShorts(short [] newShorts) {
	    shorts = newShorts;
	}

	int [] ints = null;
	public int [] getInts() {
	    return ints;
	}
	public void setInts(int [] newInts) {
	    ints = newInts;
	}

	long [] longs = null;
	public long [] getLongs() {
	    return longs;
	}
	public void setLongs(long [] newLongs) {
	    longs = newLongs;
	}

	float [] floats = null;
	public float [] getFloats() {
	    return floats;
	}
	public void setFloats(float [] newFloats) {
	    floats = newFloats;
	}

	double [] doubles = null;
	public double [] getDoubles() {
	    return doubles;
	}
	public void setDoubles(double [] newDoubles) {
	    doubles = newDoubles;
	}

	String [] strings = null;
	public String [] getStrings() {
	    return strings;
	}
	public void setStrings(String [] newStrings) {
	    strings = newStrings;
	}

	Date [] dates = null;
	public Date [] getDates() {
	    return dates;
	}
	public void setDates(Date [] newDates) {
	    dates = newDates;
	}

	Number [] numbers = null;
	public Number [] getNumbers() {
	    return numbers;
	}
	public void setNumbers(Number [] newNumbers) {
	    numbers = newNumbers;
	}

	List stringList = null;
	public List getStringList() {
	    return stringList;
	}

	public void setStringList(List newStringList) {
	    stringList = newStringList;
	}

    }

    public void beginUISelectMany(WebRequest theRequest)
    {

	// primitives
	theRequest.addParameter("bool", "false");
	theRequest.addParameter("bool", "true");
	theRequest.addParameter("bool", "false");
	theRequest.addParameter("bool2", "false");

	theRequest.addParameter("byte", Byte.toString(Byte.MIN_VALUE));
	theRequest.addParameter("byte", Byte.toString(Byte.MAX_VALUE));
	theRequest.addParameter("byte", "1");
	theRequest.addParameter("char", "Q");				
	theRequest.addParameter("char", "A"); 				
	theRequest.addParameter("char", "z"); 
			    

	theRequest.addParameter("short", Short.toString(Short.MIN_VALUE));
	theRequest.addParameter("short", Short.toString(Short.MAX_VALUE));
	theRequest.addParameter("short", 
				Short.toString((short) (Byte.MAX_VALUE + 1)));

	theRequest.addParameter("int", Integer.toString(Integer.MIN_VALUE));
	theRequest.addParameter("int", Integer.toString(Integer.MAX_VALUE));
	theRequest.addParameter("int", 
				Integer.toString(Short.MAX_VALUE + 1));

	theRequest.addParameter("float", Float.toString(Float.MIN_VALUE));
	theRequest.addParameter("float", Float.toString(Float.MAX_VALUE));
	theRequest.addParameter("float", 
				Float.toString(Integer.MAX_VALUE + 1));

	theRequest.addParameter("long", Long.toString(Long.MIN_VALUE));
	theRequest.addParameter("long", Long.toString(Long.MAX_VALUE));
	theRequest.addParameter("long", 
				Long.toString(Integer.MAX_VALUE + 1));

	theRequest.addParameter("double", Double.toString(Double.MIN_VALUE));
	theRequest.addParameter("double", Double.toString(Double.MAX_VALUE));
	theRequest.addParameter("double", 
				Double.toString(Long.MAX_VALUE + 1));
	
	// Objects
	theRequest.addParameter("str", "value1");
	theRequest.addParameter("str", "value2");
	theRequest.addParameter("str", "value3");

	theRequest.addParameter("str2", "");

	theRequest.addParameter("date", "Jan 1, 1967");
	theRequest.addParameter("date", "May 26, 2003");
	theRequest.addParameter("date", "Aug 19, 1946");

	theRequest.addParameter("num", "12%");
	theRequest.addParameter("num", "3.14");
	theRequest.addParameter("num", "49.99");

	theRequest.addParameter("stringList", "value1");
	theRequest.addParameter("stringList", "value2");
	theRequest.addParameter("stringList", "value3");

    }

    public void testUISelectMany() throws Exception {
	// create the test bean
	TestBean bean = new TestBean();
	getFacesContext().getExternalContext().getRequestMap().put("bean",
								   bean);
	// create a dummy root for the tree.
	UIViewRoot root = new UIViewRoot();
	root.setId("root");
	getFacesContext().setViewRoot(root);

	// test model type of boolean []
	UISelectMany booleanv = new UISelectMany();
	booleanv.setId("bool");
	booleanv.setRendererType("CheckboxList");
	booleanv.setValueRef("bean.booleans");
	root.getChildren().add(booleanv);
	booleanv.decode(getFacesContext());
	booleanv.updateModel(getFacesContext());
	assertNotNull(bean.getBooleans());
	assertTrue(bean.getBooleans()[0] == false);
	assertTrue(bean.getBooleans()[1] == true);
	assertTrue(bean.getBooleans()[2] == false);

	// test model type of boolean []
	booleanv = new UISelectMany();
	booleanv.setId("bool2");
	booleanv.setRendererType("CheckboxList");
	booleanv.setValueRef("bean.booleans");
	root.getChildren().add(booleanv);
	booleanv.decode(getFacesContext());
	booleanv.updateModel(getFacesContext());
	assertNotNull(bean.getBooleans());
	assertTrue(bean.getBooleans()[0] == false);
	assertTrue(bean.getBooleans().length == 1);

	// test model type of byte []
	UISelectMany bytev = new UISelectMany();
	bytev.setId("byte");
	bytev.setRendererType("CheckboxList");
	bytev.setValueRef("bean.bytes");
	root.getChildren().add(bytev);
	bytev.decode(getFacesContext());
	bytev.updateModel(getFacesContext());
	assertNotNull(bean.getBytes());
	assertTrue(bean.getBytes()[0] == Byte.MIN_VALUE);
	assertTrue(bean.getBytes()[1] == Byte.MAX_VALUE);
	assertTrue(bean.getBytes()[2] == 1);

	// test model type of char []
	UISelectMany charv = new UISelectMany();
	charv.setId("char");
	charv.setRendererType("CheckboxList");
	charv.setValueRef("bean.chars");
	root.getChildren().add(charv);
	charv.decode(getFacesContext());
	charv.updateModel(getFacesContext());
	assertNotNull(bean.getChars());
	assertTrue(bean.getChars()[0] == 'Q');
	assertTrue(bean.getChars()[1] == 'A');
	assertTrue(bean.getChars()[2] == 'z');

	// test model type of short []
	UISelectMany shortv = new UISelectMany();
	shortv.setId("short");
	shortv.setRendererType("CheckboxList");
	shortv.setValueRef("bean.shorts");
	root.getChildren().add(shortv);
	shortv.decode(getFacesContext());
	shortv.updateModel(getFacesContext());
	assertNotNull(bean.getShorts());
	assertTrue(bean.getShorts()[0] == Short.MIN_VALUE);
	assertTrue(bean.getShorts()[1] == Short.MAX_VALUE);
	assertTrue(bean.getShorts()[2] == Byte.MAX_VALUE + 1);

	// test model type of int []
	UISelectMany intv = new UISelectMany();
	intv.setId("int");
	intv.setRendererType("CheckboxList");
	intv.setValueRef("bean.ints");
	root.getChildren().add(intv);
	intv.decode(getFacesContext());
	intv.updateModel(getFacesContext());
	assertNotNull(bean.getInts());
	assertTrue(bean.getInts()[0] == Integer.MIN_VALUE);
	assertTrue(bean.getInts()[1] == Integer.MAX_VALUE);
	assertTrue(bean.getInts()[2] == Short.MAX_VALUE + 1);

	// test model type of float []
	UISelectMany floatv = new UISelectMany();
	floatv.setId("float");
	floatv.setRendererType("CheckboxList");
	floatv.setValueRef("bean.floats");
	root.getChildren().add(floatv);
	floatv.decode(getFacesContext());
	floatv.updateModel(getFacesContext());
	assertNotNull(bean.getFloats());
	assertTrue(bean.getFloats()[0] == Float.MIN_VALUE);
	assertTrue(bean.getFloats()[1] == Float.MAX_VALUE);
	assertTrue(bean.getFloats()[2] == Integer.MAX_VALUE + 1);

	// test model type of long []
	UISelectMany longv = new UISelectMany();
	longv.setId("long");
	longv.setRendererType("CheckboxList");
	longv.setValueRef("bean.longs");
	root.getChildren().add(longv);
	longv.decode(getFacesContext());
	longv.updateModel(getFacesContext());
	assertNotNull(bean.getLongs());
	assertTrue(bean.getLongs()[0] == Long.MIN_VALUE);
	assertTrue(bean.getLongs()[1] == Long.MAX_VALUE);
	assertTrue(bean.getLongs()[2] == Integer.MAX_VALUE + 1);

	// test model type of double []
	UISelectMany doublev = new UISelectMany();
	doublev.setId("double");
	doublev.setRendererType("CheckboxList");
	doublev.setValueRef("bean.doubles");
	root.getChildren().add(doublev);
	doublev.decode(getFacesContext());
	doublev.updateModel(getFacesContext());
	assertNotNull(bean.getDoubles());
	assertTrue(bean.getDoubles()[0] == Double.MIN_VALUE);
	assertTrue(bean.getDoubles()[1] == Double.MAX_VALUE);
	assertTrue(bean.getDoubles()[2] == Long.MAX_VALUE + 1);

	// test model type of String []
	UISelectMany str = new UISelectMany();
	str.setId("str");
	str.setRendererType("CheckboxList");
	str.setValueRef("bean.strings");
	root.getChildren().add(str);
	str.decode(getFacesContext());
	str.updateModel(getFacesContext());
	assertNotNull(bean.getStrings());
	assertTrue("value1".equals(bean.getStrings()[0]));

	// test model type of Date []
	UISelectMany date = new UISelectMany();
	Converter dateConv = Util.getConverterForIdentifer("DateTime");
	assertNotNull(dateConv);
	date.setConverter(dateConv);
	date.setId("date");
	date.setRendererType("CheckboxList");
	date.setValueRef("bean.dates");
	root.getChildren().add(date);
	date.decode(getFacesContext());
	date.updateModel(getFacesContext());
	assertNotNull(bean.getDates());
	Object expected = null;
	try {
	    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	    expected = df.parse("19460819");
	}
	catch (ParseException e) {
	    assertTrue(e.getMessage(), false);
	}
	assertTrue(expected.equals(bean.getDates()[2]));

	// test model type of Number []
	UISelectMany number = new UISelectMany();
	Converter numberConv = Util.getConverterForIdentifer("Number");
	assertNotNull(numberConv);
	number.setConverter(numberConv);
	number.setId("num");
	number.setRendererType("CheckboxList");
	number.setValueRef("bean.numbers");
	root.getChildren().add(number);
	number.decode(getFacesContext());
	number.updateModel(getFacesContext());
	assertNotNull(bean.getNumbers());
	try {
	    DecimalFormat df = new DecimalFormat("'$'##.##");
	    expected = df.parse("$49.99");
	}
	catch (ParseException e) {
	    assertTrue(e.getMessage(), false);
	}
	assertTrue(expected.equals(bean.getNumbers()[2]));

	// test model type of List of Strings
	UISelectMany stringList = new UISelectMany();
	stringList.setId("stringList");
	stringList.setRendererType("CheckboxList");
	stringList.setValueRef("bean.stringList");
	root.getChildren().add(stringList);
	stringList.decode(getFacesContext());
	stringList.updateModel(getFacesContext());
	assertNotNull(bean.getStringList());
	assertTrue(bean.getStringList().get(0).equals("value1"));
	assertTrue(bean.getStringList().get(1).equals("value2"));
	assertTrue(bean.getStringList().get(2).equals("value3"));

    }
    
    //
    // General Methods
    //

    public void testDateConverter(UIViewRoot root) throws ConverterException,
        InstantiationException, IllegalAccessException, ClassNotFoundException {
        System.out.println("Testing DateConverter");
        UIInput text = new UIInput();
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
        text = new UIInput();
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
        text = new UIInput();
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
        UIInput text = new UIInput();
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
        UIInput text = new UIInput();
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
        UIInput text = new UIInput();
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
        application.addConverter(java.text.CharacterIterator.class, "javax.faces.convert.CharacterConverter");
        converter = null;
        converter = application.createConverter(java.text.StringCharacterIterator.class);
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
