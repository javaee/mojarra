/*
 * $Id: TestFormatPoolImpl.java,v 1.8 2003/02/20 22:50:03 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestFormatPoolImpl.java

package com.sun.faces.renderkit;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import com.sun.faces.ServletFacesTestCase;

import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import com.sun.faces.RIConstants;

/**
 *
 *  <B>TestFormatPoolImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestFormatPoolImpl.java,v 1.8 2003/02/20 22:50:03 ofung Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class TestFormatPoolImpl extends ServletFacesTestCase
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

// Attribute Instance Variables

// Relationship Instance Variables

//
// Constructors and Initializers    
//

    public TestFormatPoolImpl() {super("TestFormatPoolImpl");}
    public TestFormatPoolImpl(String name) {super(name);}

//
// Class methods
//

//
// General Methods
//

    public void testDateFormat_format() {

	String result = null;
	FormatPool formatPool = new FormatPoolImpl();
	UIInput input = new UIInput();
	input.setComponentId("input");
	Date date = null;
	
	// dateStyle == short, timezone == null, get Locale from FacesContext
	getFacesContext().setLocale(Locale.US);
	try {
	    date = DateFormat.getDateInstance(DateFormat.SHORT, 
					      Locale.US).parse("12/31/52");
	}
	catch (ParseException e) {
	    assertTrue(false);
	}
	input.setAttribute("dateStyle", "short");

	result = formatPool.dateFormat_format(getFacesContext(), input, date);
	assertTrue(null != result);
	assertTrue(result.equals("12/31/52"));

	// dateStyle == medium, timezone == null, get Locale from FacesContext
	getFacesContext().setLocale(Locale.US);
	try {
	    date = DateFormat.getDateInstance(DateFormat.MEDIUM, 
					      Locale.US).parse("Jan 12, 1952");
	}
	catch (ParseException e) {
	    assertTrue(false);
	}
	input.setAttribute("dateStyle", "medium");
	result = formatPool.dateFormat_format(getFacesContext(), input, date);
	assertTrue(null != result);
	assertTrue(result.equals("Jan 12, 1952"));

	// dateStyle == long, timezone == null, get Locale from FacesContext
	getFacesContext().setLocale(Locale.US);
	try {
	    date = DateFormat.getDateInstance(DateFormat.LONG, 
					      Locale.US).parse("January 12, 1952");
	}
	catch (ParseException e) {
	    assertTrue(false);
	}
	input.setAttribute("dateStyle", "long");
	result = formatPool.dateFormat_format(getFacesContext(), input, date);
	assertTrue(null != result);
	assertTrue(result.equals("January 12, 1952"));

	// dateStyle == full, timezone == null, get Locale from FacesContext
	getFacesContext().setLocale(Locale.US);
	try {
	    date = DateFormat.getDateInstance(DateFormat.FULL, 
					      Locale.US).parse("Saturday, April 12, 1952");
	}
	catch (ParseException e) {
	    assertTrue(false);
	}
	input.setAttribute("dateStyle", "full");
	result = formatPool.dateFormat_format(getFacesContext(), input, date);
	assertTrue(null != result);
	assertTrue(result.equals("Saturday, April 12, 1952"));

	// dateStyle == null, timezone == null, get Locale from FacesContext
	getFacesContext().setLocale(Locale.US);
	try {
	    date = DateFormat.getDateInstance(DateFormat.MEDIUM, 
					      Locale.US).parse("Apr 12, 1952");
	}
	catch (ParseException e) {
	    assertTrue(false);
	}
	input.setAttribute("dateStyle", null);
	result = formatPool.dateFormat_format(getFacesContext(), input, date);
	assertTrue(null != result);
	assertTrue(result.equals("Apr 12, 1952"));

    }

    public void testDateFormat_parse() {

	Date expectedResult = null, result = null;
	FormatPool formatPool = new FormatPoolImpl();
	UIInput input = new UIInput();
	input.setComponentId("input");
	String date = null;
	
	// dateStyle == short, timezone == null, get Locale from FacesContext
	try {
	    getFacesContext().setLocale(Locale.US);
	    date = "12/31/52";
	    input.setAttribute("dateStyle", "short");
	    result = formatPool.dateFormat_parse(getFacesContext(), input, date);
	    assertTrue(null != result);
	    assertTrue(DateFormat.
		       getDateInstance(DateFormat.SHORT, 
				       Locale.US).parse(date).equals(result));
	}
	catch (Throwable e) {
	    assertTrue(false);
	}

	// dateStyle == medium, timezone == null, get Locale from FacesContext
	try {
	    getFacesContext().setLocale(Locale.US);
	    date = "Jan 12, 1952";
	    input.setAttribute("dateStyle", "medium");
	    result = formatPool.dateFormat_parse(getFacesContext(), input, date);
	    assertTrue(null != result);
	    assertTrue(DateFormat.
		       getDateInstance(DateFormat.MEDIUM, 
				       Locale.US).parse(date).equals(result));
	}
	catch (Throwable e) {
	    assertTrue(false);
	}

	// dateStyle == long, timezone == null, get Locale from FacesContext
	try {
	    getFacesContext().setLocale(Locale.US);
	    date = "January 12, 1952";
	    input.setAttribute("dateStyle", "long");
	    result = formatPool.dateFormat_parse(getFacesContext(), input, date);
	    assertTrue(null != result);
	    assertTrue(DateFormat.
		       getDateInstance(DateFormat.MEDIUM, 
				       Locale.US).parse(date).equals(result));
	}
	catch (Throwable e) {
	    assertTrue(false);
	}

	// dateStyle == full, timezone == null, get Locale from FacesContext
	try {
	    getFacesContext().setLocale(Locale.US);
	    date = "Saturday, April 12, 1952";
	    input.setAttribute("dateStyle", "full");
	    result = formatPool.dateFormat_parse(getFacesContext(), input, date);
	    assertTrue(null != result);
	    assertTrue(DateFormat.
		       getDateInstance(DateFormat.MEDIUM, 
				       Locale.US).parse("April 12, 1952").equals(result));
	}
	catch (Throwable e) {
	    assertTrue(false);
	}

    }

    public void testDateTimeFormat_formatWithPattern() {
	String formatPattern = "EEE, MMM d, yyyy G 'at' hh:mm:ss a";
	String expectedDate = "Wed, Jul 10, 1996 AD at 12:31:31 PM";

	String result = null;
	FormatPool formatPool = new FormatPoolImpl();
	UIInput input = new UIInput();
	input.setComponentId("input");
	Date date = null;
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern,
								 Locale.US);
	
	// formatPattern == formatPattern, timezone == null, get Locale from FacesContext
	getFacesContext().setLocale(Locale.US);
	try {
	    date = simpleDateFormat.parse(expectedDate);
	}
	catch (ParseException e) {
	    assertTrue(false);
	}
	input.setAttribute("formatPattern", formatPattern);
	result = formatPool.dateTimeFormat_format(getFacesContext(), input, 
						  date);
	assertTrue(null != result);
	assertTrue(result.equals(expectedDate));
    }

    public void testDateTimeFormat_parseWithPattern() {
	String formatPattern = "EEE, MMM d, yyyy G 'at' hh:mm:ss a";
	String expectedDate = "Wed, Jul 10, 1996 AD at 12:31:31 PM";

	Date expectedResult = null, result = null;
	FormatPool formatPool = new FormatPoolImpl();
	UIInput input = new UIInput();
	input.setComponentId("input");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern,
								 Locale.US);
	
	// dateStyle == short, timezone == null, get Locale from FacesContext
	try {
	    getFacesContext().setLocale(Locale.US);
	    input.setAttribute("formatPattern", formatPattern);
	    result = formatPool.dateTimeFormat_parse(getFacesContext(), input, 
						     expectedDate);
	    assertTrue(null != result);
	    assertTrue(simpleDateFormat.parse(expectedDate).equals(result));
	}
	catch (Throwable e) {
	    assertTrue(false);
	}

    }

    /**

    * verify that two different UIComponent instances with
    * identical attribute sets return the same format instance,
    * demonstrating the hashMap is correctly used.

    */

    public void testDateFormat_hashMap() {
	UIInput input1, input2;
	DateFormat dateFormat1, dateFormat2;
	FormatPoolImpl formatPool = new FormatPoolImpl();


	input1 = new UIInput();
	input1.setComponentId("input1");
	input2 = new UIInput();
	input2.setComponentId("input2");

	// dateStyle == short, timezone == null, get Locale from FacesContext
	getFacesContext().setLocale(Locale.US);
	input1.setAttribute("dateStyle", "short");
	input2.setAttribute("dateStyle", "short");
	dateFormat1 = formatPool.getDateFormat(getFacesContext(), input1,
					       FormatPoolImpl.DATEINSTANCE);
	dateFormat2 = formatPool.getDateFormat(getFacesContext(), input2, 
					       FormatPoolImpl.DATEINSTANCE);
	assertTrue(dateFormat1 == dateFormat2);

	// dateStyle == short, timezone == PST, get Locale from FacesContext
	getFacesContext().setLocale(Locale.US);
	input1.setAttribute("timezone", "PST");
	input2.setAttribute("timezone", "PST");
	dateFormat1 = formatPool.getDateFormat(getFacesContext(), input1,
					       FormatPoolImpl.DATEINSTANCE);
	assertTrue(dateFormat1 != dateFormat2);
	dateFormat2 = formatPool.getDateFormat(getFacesContext(), input2,
					       FormatPoolImpl.DATEINSTANCE);
	assertTrue(dateFormat1 == dateFormat2);

	// dateStyle == short, timezone == PST, get Locale from attribute

	LocalizationContext locCtx = 
	    new LocalizationContext(null, Locale.FRANCE);
	getFacesContext().getHttpSession().setAttribute("basicBundle", locCtx);
	
	input1.setAttribute("bundle", "basicBundle");
	input2.setAttribute("bundle", "basicBundle");
	dateFormat1 = formatPool.getDateFormat(getFacesContext(), input1,
					       FormatPoolImpl.DATEINSTANCE);
	assertTrue(dateFormat1 != dateFormat2);
	dateFormat2 = formatPool.getDateFormat(getFacesContext(), input2,
					       FormatPoolImpl.DATEINSTANCE);
	assertTrue(dateFormat1 == dateFormat2);
	
    }

    public void testFormatPoolInServletContext() {
	assertTrue(null !=
		   getFacesContext().getServletContext().
		   getAttribute(RIConstants.FORMAT_POOL));
    }
    
    public void testNumberFormat() {

	String result = null;
	FormatPool formatPool = new FormatPoolImpl();
	UIInput input = new UIInput();
	input.setComponentId("input");
	Number testNum = new Double(1239989.60);
        Number resultNum = null;
	String formatStr = null;
        
        FacesContext context = getFacesContext();
	// style == number, format method
        input.setAttribute("numberStyle", "number");
	getFacesContext().setLocale(Locale.US);
        formatStr = formatPool.numberFormat_format(context, input,
                testNum);
        
	// style == number, parse method
        try {
	    resultNum = formatPool.numberFormat_parse(context, input,formatStr);
	}
	catch (ParseException e) {
	    assertTrue(false);
	}
        assertTrue( formatStr.equals("1,239,989.6"));
        assertTrue(testNum.doubleValue() == resultNum.doubleValue());
        
        // style == percent, format method
        testNum = new Double(.99);
        input.setAttribute("numberStyle", "percent");
	formatStr = formatPool.numberFormat_format(context, input,testNum);
        assertTrue(formatStr.equals("99%"));
        
        // style == percent, parse method
        try {
	    resultNum = formatPool.numberFormat_parse(context, input,formatStr);
	}
	catch (ParseException e) {
	    assertTrue(false);
	}
        assertTrue( testNum.equals(resultNum ));
        
         // style == currency, format method
        testNum = new Double(1234789.60);
        input.setAttribute("numberStyle", "currency");
	formatStr = formatPool.numberFormat_format(context, input, testNum);
        assertTrue( formatStr.equals("$1,234,789.60"));
        
        // style == percent, parse method
        try {
	    resultNum = formatPool.numberFormat_parse(context, input,formatStr);
	}
	catch (ParseException e) {
	    assertTrue(false);
	}
        assertTrue( testNum.equals(resultNum));
        
         // pattern="####.000", format method
        testNum = new Double(9999.988);
        input.setAttribute("formatPattern", "####.000");
        formatStr = formatPool.numberFormat_format(context, input, testNum);
        assertTrue(formatStr.equals("9999.988"));
        
        // style == percent, parse method
        try {
	    resultNum = formatPool.numberFormat_parse(context, input,formatStr);
	}
	catch (ParseException e) {
	    assertTrue(false);
	}
        assertTrue( testNum.equals(resultNum ));
    }
    
    public void testNumberFormat_hashMap() {
	UIInput input1, input2;
	NumberFormat numberFormat1, numberFormat2;
	FormatPoolImpl formatPool = new FormatPoolImpl();


	input1 = new UIInput();
	input1.setComponentId("input1");
	input2 = new UIInput();
	input2.setComponentId("input2");

	// style == percent, get Locale from FacesContext
	getFacesContext().setLocale(Locale.US);
	input1.setAttribute("numberStyle", "percent");
	input2.setAttribute("numberStyle", "percent");
	numberFormat1 = formatPool.getNumberFormat(getFacesContext(), input1);
	numberFormat2 = formatPool.getNumberFormat(getFacesContext(), input2);
	assertTrue(numberFormat1.equals(numberFormat2));

        //style="percent", get locale from attribute.
        LocalizationContext locCtx = 
	    new LocalizationContext(null, Locale.FRANCE);
	getFacesContext().getHttpSession().setAttribute("basicBundle", locCtx);
	
	input1.setAttribute("bundle", "basicBundle");
	input2.setAttribute("bundle", "basicBundle");
	numberFormat1 = formatPool.getNumberFormat(getFacesContext(), input1);
	assertTrue(!(numberFormat1.equals(numberFormat2)));
	numberFormat2 = formatPool.getNumberFormat(getFacesContext(), input2);
	assertTrue(numberFormat1.equals(numberFormat2));  
        
	// pattern="####",  get Locale from FacesContext
	getFacesContext().setLocale(Locale.US);
        input1.setAttribute("numberStyle", null);
	input2.setAttribute("numberStyle", null);
	input1.setAttribute("formatPattern", "####");
	input2.setAttribute("formatPattern", "####");
	numberFormat1 = formatPool.getNumberFormat(getFacesContext(), input1);
	assertTrue((!numberFormat1.equals(numberFormat2)));
	numberFormat2 = formatPool.getNumberFormat(getFacesContext(), input2);
	assertTrue(numberFormat1.equals(numberFormat2));
    }



} // end of class TestFormatPoolImpl
