/*
 * $Id: FormatPoolImpl.java,v 1.1 2002/08/08 23:40:52 eburns Exp $
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

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

import com.sun.faces.util.Util;

/**
 *
 *  <B>FormatPoolImpl</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: FormatPoolImpl.java,v 1.1 2002/08/08 23:40:52 eburns Exp $
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

    * attribute <CODE>style</CODE> is SHORT, MEDIUM, LONG, FULL.  If not
    * present, defaults to {@link DEFAULT_DATE_STYLE}.  Not required.<P>

    * attribute <CODE>timezone</CODE> is put directly into
    * <CODE>TimeZone.getTimeZone()</CODE>.  Not required.

    * @return the DateFormat for the given context and component

    */

    DateFormat getDateFormat(FacesContext context, UIComponent component) {
	String 
	    pattern = null, 
	    formatStyle = null, 
	    timezone = null, 
	    hashKey = null;
	int formatStyleInt = DEFAULT_DATE_STYLE_INT;
	DateFormat dateFormat = null;
	Locale locale = Util.getLocaleFromContextOrComponent(context, 
							     component);
	//
	// build the hashKey
	//

	// get the formatStyle
	if (null == (formatStyle = (String) component.getAttribute("style"))) {
	    formatStyle = DEFAULT_DATE_STYLE;
	    formatStyleInt = DEFAULT_DATE_STYLE_INT;
	}
	else {
	    if (formatStyle.equals("SHORT")) {
		formatStyleInt = DateFormat.SHORT;
	    }
	    else if (formatStyle.equals("MEDIUM")) {
		formatStyleInt = DateFormat.MEDIUM;
	    }
	    else if (formatStyle.equals("LONG")) {
		formatStyleInt = DateFormat.LONG;
	    }
	    else if (formatStyle.equals("FULL")) {
		formatStyleInt = DateFormat.FULL;
	    }
	    else {
		formatStyle = DEFAULT_DATE_STYLE;
		formatStyleInt = DEFAULT_DATE_STYLE_INT;
	    }
	}
	
	// get the timezone, null is ok.
	timezone = (String) component.getAttribute("timezone");

	// get the pattern, null is ok
	pattern = (String) component.getAttribute("pattern");
	
	hashKey = locale.toString() + formatStyle + timezone + pattern;
	
	// Look in the formatters map
	if (null == (dateFormat = (DateFormat) formatters.get(hashKey))) {
	    
	    // if not present, create one
	    dateFormat = DateFormat.getDateInstance(formatStyleInt, locale);
	    if (null != pattern) {
		if (dateFormat instanceof SimpleDateFormat) {
		    ((SimpleDateFormat)dateFormat).applyPattern(pattern);
		}
		else {
		    dateFormat = new SimpleDateFormat(pattern, locale);
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

//
// Methods from FormatPool
//

    public synchronized String dateFormat_format(FacesContext context, 
						 UIComponent component, 
						 Date date) {
	String result = null;
	DateFormat dateFormat = getDateFormat(context, component);

	result = dateFormat.format(date);
	return result;
    }

    public synchronized Date dateFormat_parse(FacesContext context, 
					      UIComponent component, 
					      String date) throws ParseException {
	Date result = null;
	DateFormat dateFormat = getDateFormat(context, component);

	result = dateFormat.parse(date);
	return result;
    }

// The testcase for this class is TestFormatPoolImpl.java 


} // end of class FormatPoolImpl
