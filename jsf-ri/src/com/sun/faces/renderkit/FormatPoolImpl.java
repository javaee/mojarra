/*
 * $Id: FormatPoolImpl.java,v 1.5 2002/08/15 23:22:59 eburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FormatPoolImpl.java

package com.sun.faces.renderkit;

import org.mozilla.util.Assert;
import org.mozilla.util.ParameterCheck;

import java.util.TimeZone;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

import com.sun.faces.util.Util;

/**
 *
 *  <B>FormatPoolImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FormatPoolImpl.java,v 1.5 2002/08/15 23:22:59 eburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class FormatPoolImpl extends Object implements FormatPool
{
//
// Protected Constants
//

    protected static final String DEFAULT_DATE_STYLE = "MEDIUM";
    protected static final int DEFAULT_DATE_STYLE_INT = DateFormat.MEDIUM;

    static final int DATEINSTANCE = 0;
    static final int DATETIMEINSTANCE = 1;

//
// Class Variables
//

//
// Instance Variables
//

// Attribute Instance Variables

// Relationship Instance Variables

    /**

    * Keys are concatenation of string values used to create the
    * formatter.  Values are Format instances.

    */

    protected HashMap formatters = null;

//
// Constructors and Initializers    
//

public FormatPoolImpl()
{
    super();

    formatters = new HashMap();
}

//
// Class methods
//

//
// General Methods
//

    /**

    * PRECONDITION: Must be called from synchronized method <P>

    * Build the hashKey from the component attributes and get the
    * DateFormat from the hashMap.  Create and store if necessary. <P>

    * attribute <CODE>formatStyle</CODE> is SHORT, MEDIUM, LONG, FULL.  If not
    * present, defaults to {@link DEFAULT_DATE_STYLE}.  Not required.<P>

    * attribute <CODE>timezone</CODE> is put directly into
    * <CODE>TimeZone.getTimeZone()</CODE>.  Not required.

    * @return the DateFormat for the given context and component

    */

    DateFormat getDateFormat(FacesContext context, UIComponent component,
			     int formatVariety) {
	String 
	    formatPattern = null, 
	    dateStyle = null, 
	    timeStyle = null, 
	    timezone = null, 
	    hashKey = null;
	int 
	    dateStyleInt = DEFAULT_DATE_STYLE_INT,
	    timeStyleInt = DEFAULT_DATE_STYLE_INT;
	DateFormat dateFormat = null;
	Locale locale = Util.getLocaleFromContextOrComponent(context, 
							     component);
	//
	// build the hashKey
	//

	// get the dateStyle
	if (null ==(dateStyle =(String) component.getAttribute("dateStyle"))) {
	    dateStyle = DEFAULT_DATE_STYLE;
	    dateStyleInt = DEFAULT_DATE_STYLE_INT;
	}
	else {
	    if (dateStyle.equals("SHORT")) {
		dateStyleInt = DateFormat.SHORT;
	    }
	    else if (dateStyle.equals("MEDIUM")) {
		dateStyleInt = DateFormat.MEDIUM;
	    }
	    else if (dateStyle.equals("LONG")) {
		dateStyleInt = DateFormat.LONG;
	    }
	    else if (dateStyle.equals("FULL")) {
		dateStyleInt = DateFormat.FULL;
	    }
	    else {
		dateStyle = DEFAULT_DATE_STYLE;
		dateStyleInt = DEFAULT_DATE_STYLE_INT;
	    }
	}
	// get the timeStyle
	if (null ==(timeStyle =(String) component.getAttribute("timeStyle"))) {
	    timeStyle = DEFAULT_DATE_STYLE;
	    timeStyleInt = DEFAULT_DATE_STYLE_INT;
	}
	else {
	    if (timeStyle.equals("SHORT")) {
		timeStyleInt = DateFormat.SHORT;
	    }
	    else if (timeStyle.equals("MEDIUM")) {
		timeStyleInt = DateFormat.MEDIUM;
	    }
	    else if (timeStyle.equals("LONG")) {
		timeStyleInt = DateFormat.LONG;
	    }
	    else if (timeStyle.equals("FULL")) {
		timeStyleInt = DateFormat.FULL;
	    }
	    else {
		timeStyle = DEFAULT_DATE_STYLE;
		timeStyleInt = DEFAULT_DATE_STYLE_INT;
	    }
	}
	
	// get the timezone, null is ok.
	timezone = (String) component.getAttribute("timezone");

	// get the formatPattern, null is ok
	formatPattern = (String) component.getAttribute("formatPattern");
	
	hashKey = locale.toString() + dateStyle + timeStyle + timezone + 
	    formatPattern;
	
	// Look in the formatters map
	if (null == (dateFormat = (DateFormat) formatters.get(hashKey))) {
	    
	    // if not present, create one
	    switch (formatVariety) {
	    case DATEINSTANCE:
		dateFormat = DateFormat.getDateInstance(dateStyleInt, 
							locale);
		break;
	    case DATETIMEINSTANCE:
		dateFormat = DateFormat.getDateTimeInstance(dateStyleInt, 
							    timeStyleInt,
							    locale);
		break;
	    default:
		Assert.assert_it(false);
		break;
	    }
	    if (null != formatPattern) {
		if (dateFormat instanceof SimpleDateFormat) {
                    ((SimpleDateFormat)dateFormat).applyLocalizedPattern(formatPattern);
                }
		else {
		    dateFormat = new SimpleDateFormat(formatPattern, locale);
		}
	    }

	    if (null != timezone) {
		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
	    }
	    
	    // and store it
	    formatters.put(hashKey, dateFormat);
	}
	
	Assert.assert_it(null != dateFormat);
        
	return dateFormat;
    }
    
    protected NumberFormat getNumberFormat(FacesContext context, 
            UIComponent component) {
	String pattern = null;
	String formatStyle = null;
        String hashKey = null;
        
        NumberFormat numberFormat = null;
	Locale locale = Util.getLocaleFromContextOrComponent(context,component);
        // get the pattern, null is ok
        formatStyle = (String) component.getAttribute("formatStyle");
	pattern = (String) component.getAttribute("formatPattern");
        
        // build hashKey
	hashKey = locale.toString() + formatStyle + pattern;
	
	// Look in the formatters map
	if (null == (numberFormat = (NumberFormat) formatters.get(hashKey))) {
	    // we need to create a NumberFormat instance based on style or
            // pattern
	    if ( pattern != null ) {
                numberFormat =  NumberFormat.getInstance(locale);
                if (numberFormat instanceof DecimalFormat) {
                    ((DecimalFormat)numberFormat).applyLocalizedPattern(pattern);
                   
                } else {
                    try {
                        numberFormat = new DecimalFormat(pattern);
                    } catch (IllegalArgumentException iae) {
                        numberFormat = NumberFormat.getInstance();
                    }    
                }     
	    } else {
                if ( formatStyle == null ) {
                    numberFormat = NumberFormat.getNumberInstance(locale);
                }    
                else if (formatStyle.equalsIgnoreCase("CURRENCY")) {
                    numberFormat = NumberFormat.getCurrencyInstance(locale);
	        }
	        else if (formatStyle.equalsIgnoreCase("PERCENT")) {
		    numberFormat = NumberFormat.getPercentInstance(locale);
                }
	        else {
                    // PENDING (visvan) should INTEGER be treated separately ?
                    // doesn't seem it is necessary because there is no specific
                    // method in API to support that.
                    numberFormat = NumberFormat.getNumberInstance(locale);
	        }
            }    
            // and store it
	    formatters.put(hashKey, numberFormat);
        }
	Assert.assert_it(null != numberFormat);
        return numberFormat;
    }


    //
    // Methods from FormatPool
    //

    public synchronized String dateFormat_format(FacesContext context, 
						 UIComponent component, 
						 Date date) {
	String result = null;
	DateFormat dateFormat = getDateFormat(context, component, 
					      DATEINSTANCE);

	result = dateFormat.format(date);
	return result;
    }

    public synchronized Date dateFormat_parse(FacesContext context, 
					      UIComponent component, 
					      String date) throws ParseException {
	Date result = null;
	DateFormat dateFormat = getDateFormat(context, component, 
					      DATEINSTANCE);

	result = dateFormat.parse(date);
	return result;
    }

    public synchronized String dateTimeFormat_format(FacesContext context, 
						 UIComponent component, 
						 Date date) {
	String result = null;
	DateFormat dateTimeFormat = getDateFormat(context, component,
						  DATETIMEINSTANCE);

	result = dateTimeFormat.format(date);
	return result;
    }

    public synchronized Date dateTimeFormat_parse(FacesContext context, 
					      UIComponent component, 
					      String date) throws ParseException {
	Date result = null;
	DateFormat dateTimeFormat = getDateFormat(context, component,
						  DATETIMEINSTANCE);

	result = dateTimeFormat.parse(date);
	return result;
    }
    

    
    public synchronized String numberFormat_format(FacesContext context, 
						 UIComponent component, 
						 Number number) {
	String result = null;
	NumberFormat numberFormat = getNumberFormat(context, component);
        result = numberFormat.format(number);
        return result;
    }

    public synchronized Number numberFormat_parse(FacesContext context, 
					      UIComponent component, 
					      String number) throws ParseException {
	Number result = null;
        if ( number != null ) {
            number = number.trim();
        }    
	NumberFormat numberFormat = getNumberFormat(context, component);
        result = numberFormat.parse(number);
	return result;
    }

// The testcase for this class is TestFormatPoolImpl.java 


} // end of class FormatPoolImpl
